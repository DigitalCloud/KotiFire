/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire.provides.network.volley

import android.util.Log
import androidx.collection.ArrayMap
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.digitalcloud.kotifire.*
import com.digitalcloud.kotifire.provides.cache.HawkCacheProvider
import com.digitalcloud.kotifire.provides.network.NetworkProvider
import com.google.gson.Gson
import org.greenrobot.eventbus.EventBus
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.reflect.KClass

/**
 * Created by Abdullah Hussein on 7/3/2018.
 * for more details : a.hussein@dce.sa
 */
class VolleyNetworkProvider<T : Any> internal constructor(type: KClass<T>) :
    NetworkProvider<T>(type) {

    private var mHawkCacheProvider = HawkCacheProvider(type)

    private var url = ""
    private var mKotiCachePolicy = KotiCachePolicy.NETWORK_ONLY

    override fun makeRequest(mKotiRequest: KotiRequest<T>) {
        mKotiCachePolicy = mKotiRequest.cachingType

        url = mKotiRequest.baseURl + mKotiRequest.endpoint

        Log.e("Error", "Request URL : $url")

        if (mKotiRequest.files.isEmpty) {
            makeStringRequest(mKotiRequest)
        } else {
            makeMultiPartRequest(mKotiRequest)
        }
    }

    private fun makeStringRequest(mKotiRequest: KotiRequest<T>) {
        val stringRequest = object : StringRequest(mKotiRequest.method.type, url,
            Response.Listener { response ->
                Log.e("Error", response)
                handleResponse(response, mKotiRequest.mDataHandler!!)
            },
            Response.ErrorListener { error ->
                extractResponseError(error, mKotiRequest.mDataHandler!!)
            }) {

            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }

            override fun getBody(): ByteArray {
                return try {
                    getParamsAsJsonObject(mKotiRequest).toString().toByteArray(Charsets.UTF_8)
                } catch (e: Exception) {
                    super.getBody()
                }
            }

            override fun getHeaders(): Map<String, String> {
                return mKotiRequest.headers
            }
        }

        VolleySingleton.addToRequestQueue(stringRequest)
    }

    private fun makeMultiPartRequest(mKotiRequest: KotiRequest<T>) {

        val multiParts: ArrayMap<String, DataPart> = ArrayMap()
        mKotiRequest.files.map {
            multiParts.put(it.key, DataPart(it.value.absolutePath, ""))
        }

        val volleyMultipartRequest =
            object : VolleyMultipartRequest(mKotiRequest.method.type, url,
                Response.Listener { response ->
                    val data = String(response.data)
                    Log.e("Error", data)
                    handleResponse(data, mKotiRequest.mDataHandler!!)
                },
                Response.ErrorListener { error ->
                    extractResponseError(
                        error,
                        mKotiRequest.mDataHandler!!
                    )
                }) {

                override val jsonObject: JSONObject?
                    get() = getParamsAsJsonObject(mKotiRequest)

                override val byteData: Map<String, DataPart>?
                    get() = multiParts

                override fun getHeaders(): Map<String, String> {
                    return mKotiRequest.headers
                }
            }

        VolleySingleton.addToRequestQueue(volleyMultipartRequest)
    }

    private fun handleResponse(response: String, dataHandler: DataHandlerInterface<T>) {

        val isNeedCheckCache = mKotiCachePolicy == KotiCachePolicy.CACHE_THEN_NETWORK
        val gson = Gson()

        if (type != String::class && type != Any::class) {
            try {
                val tArrayList = ArrayList<T>()
                val jsonArray = JSONArray(response)
                for (i in 0 until jsonArray.length()) {
                    val jsonString = jsonArray.get(i).toString()
                    val mData = gson.fromJson(jsonString, type.java)
                    tArrayList.add(mData)
                }

                if (!isNeedCheckCache || mHawkCacheProvider.isNotTheSameCache(url, response)) {
                    dataHandler.onSuccess(tArrayList, SourceType.NETWORK)
                }
            } catch (e: Exception) {
                val res = gson.fromJson(response, type.java)
                if (!isNeedCheckCache || mHawkCacheProvider.isNotTheSameCache(url, response)) {
                    dataHandler.onSuccess(res, SourceType.NETWORK)
                }
            }
        } else {
            if (!isNeedCheckCache || mHawkCacheProvider.isNotTheSameCache(url, response)) {
                dataHandler.onSuccess(response, SourceType.NETWORK)
            }
        }

        if (isNeedCheckCache)
            mHawkCacheProvider.put(url, response)
    }

    private fun extractResponseError(error: VolleyError?, dataHandler: DataHandlerInterface<T>?) {
        try {
            if (dataHandler != null) {
                if (error?.networkResponse != null) {
                    val response = String(error.networkResponse.data)

                    Log.e("Error", "statusCode : " + error.networkResponse.statusCode)
                    Log.e("Error", "response : $response")

                    postEventBus(StatusCodeEvent(error.networkResponse.statusCode))
                    dataHandler.onFail(extractErrorMessages(response), false)
                } else {
                    Log.e("Error", "VolleyErrorUtil : " + VolleyErrorUtil.getMessage(error))
                    dataHandler.onFail(VolleyErrorUtil.getMessage(error), true)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            dataHandler!!.onFail(e.localizedMessage!!, false)
        }
    }

    private fun postEventBus(mStatusCodeEvent: StatusCodeEvent) {
        EventBus.getDefault().post(mStatusCodeEvent)
    }

    private fun extractErrorMessages(errorResponse: String): ArrayMap<String, String> {
        val messages = ArrayMap<String, String>()
        try {
            val jsonObject = JSONObject(errorResponse)

            try {
                val errors = jsonObject.optString("errors")
                if (!errors.isNullOrEmpty()) {
                    val errorsJson = JSONObject(errors)
                    val keys = errorsJson.names()
                    if (keys != null) {
                        for (i in 0 until keys.length()) {
                            val name = keys.getString(i)
                            var msg: String? = null
                            val messagesList = errorsJson.optJSONArray(name)
                            if (messagesList != null && messagesList.length() > 0) {
                                msg = messagesList.optString(0)
                            }
                            messages[name] = msg
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            if (messages.isEmpty) {
                val message = jsonObject.optString("message")
                if (!message.isNullOrEmpty()) {
                    messages["message"] = message
                } else {
                    messages["message"] = VolleySingleton.defaultError
                }
            }

            messages["response"] = errorResponse
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return messages
    }

    private fun getParamsAsJsonObject(mKotiRequest: KotiRequest<T>): JSONObject {
        val params: JSONObject = if (mKotiRequest.method.type == Request.Method.GET) {
            url += mKotiRequest.params.generateGetParams()
            JSONObject()
        } else {
            mKotiRequest.params.generatePostParams()
        }

        if (mKotiRequest.method.type == Request.Method.PATCH) {
            params.put("_method", "patch")
            mKotiRequest.method = KotiMethod.POST
        }

        return params
    }
}