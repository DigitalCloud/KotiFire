/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire.provides.network.volley

import android.content.Context
import android.util.Log
import androidx.collection.ArrayMap
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.digitalcloud.kotifire.*
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
class VolleyNetworkProvider<T : Any> internal constructor(context: Context, type: KClass<T>) :
    NetworkProvider<T>(context, type) {

    private val TAG = "VolleyNetworkProvider"
    private val gson = Gson()

    override fun makeRequest(mKotiRequest: KotiRequest<T>) {
        var url = mKotiRequest.baseURl + mKotiRequest.endpoint

        val params: ArrayMap<String, String> = if (mKotiRequest.method.type == Request.Method.GET) {
            url += mKotiRequest.params.generateGetParams()
            ArrayMap()
        } else {
            mKotiRequest.params.generatePostParams()
        }

        if (mKotiRequest.method.type == Request.Method.PATCH) {
            params["_method"] = "patch"
            mKotiRequest.method = KotiMethod.POST
        }


        if (mKotiRequest.files.isEmpty) {
            makeStringRequest(
                mKotiRequest.method.type,
                url,
                params,
                mKotiRequest.mDataHandler!!
            )
        } else {
            val multiParts: ArrayMap<String, DataPart> = ArrayMap()
            mKotiRequest.files.map {
                multiParts.put(it.key, DataPart(it.value.absolutePath, ""))
            }

            makeMultiPartRequest(
                mKotiRequest.method.type,
                url,
                params,
                multiParts,
                mKotiRequest.mDataHandler!!
            )
        }
    }

    private fun makeStringRequest(
        method: Int,
        url: String,
        params: ArrayMap<String, String>,
        dataHandler: DataHandlerInterface<T>
    ) {
        var newUrl = url

        if (!url.startsWith("http")) {
            newUrl = KotiFire.instance.getBaseUrl() + url
        }

        Log.e(TAG, "makeStringRequest url : $newUrl")

        val stringRequest = object : StringRequest(method, newUrl, Response.Listener { response ->
            Log.e(TAG, response)
            handleResponse(response, dataHandler)
        }, Response.ErrorListener { error -> extractResponseError(error, dataHandler) }) {

            override fun getParams(): Map<String, String> {
                return params
            }

            override fun getHeaders(): Map<String, String> {
                return KotiFire.instance.getHeaders()
            }
        }

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest)
    }

    private fun makeMultiPartRequest(
        method: Int,
        url: String,
        params: ArrayMap<String, String>,
        multiParts: ArrayMap<String, DataPart>,
        dataHandler: DataHandlerInterface<T>
    ) {
        var newUrl = url

        if (!url.startsWith("http")) {
            newUrl = KotiFire.instance.getBaseUrl() + url
        }

        Log.e(TAG, "makeMultiPartRequest url : $newUrl")

        val volleyMultipartRequest =
            object :
                VolleyMultipartRequest(method, url, Response.Listener { response ->
                    val data = String(response.data)
                    Log.e(TAG, data)
                    handleResponse(data, dataHandler)
                }, Response.ErrorListener { error -> extractResponseError(error, dataHandler) }) {

                override val byteData: Map<String, DataPart>?
                    get() = multiParts

                override fun getParams(): Map<String, String> {
                    return params
                }
            }

        VolleySingleton.getInstance(context).addToRequestQueue(volleyMultipartRequest)
    }

    private fun handleResponse(response: String, dataHandler: DataHandlerInterface<T>) {
        when (type) {
            String::class,
            Any::class -> {
                dataHandler.onSuccess(response, SourceType.NETWORK)
            }
            else -> {
                try {
                    val tArrayList = ArrayList<T>()
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val jsonString = jsonArray.get(i).toString()
                        val mData = gson.fromJson(jsonString, type.java)
                        tArrayList.add(mData)
                    }
                    dataHandler.onSuccess(tArrayList, SourceType.NETWORK)
                } catch (e: Exception) {
                    dataHandler.onSuccess(gson.fromJson(response, type.java), SourceType.NETWORK)
                }
            }
        }
    }

    private fun extractResponseError(error: VolleyError?, dataHandler: DataHandlerInterface<T>?) {
        try {
            if (dataHandler != null) {
                if (error?.networkResponse != null) {

                    val response = String(error.networkResponse.data)

                    Log.e(TAG, "statusCode : " + error.networkResponse.statusCode)
                    Log.e(TAG, "response : $response")

                    postEventBus(StatusCodeEvent(error.networkResponse.statusCode))
                    dataHandler.onFail(extractErrorMessages(response), false)
                } else {
                    Log.e(
                        TAG, "VolleyErrorUtil : " + VolleyErrorUtil.getMessage(
                            context,
                            error
                        )!!
                    )
                    dataHandler.onFail(
                        VolleyErrorUtil.getMessage(
                            context,
                            error
                        )!!, true
                    )
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
            dataHandler!!.onFail(e.localizedMessage, false)
        }
    }

    private fun postEventBus(mStatusCodeEvent: StatusCodeEvent) {
        EventBus.getDefault().post(mStatusCodeEvent)
    }

    private fun extractErrorMessages(errorResponse: String): ArrayMap<String, String> {
        val messages = ArrayMap<String, String>()
        try {
            val jsonObject = JSONObject(errorResponse)
            val message = jsonObject.optString("message")
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

            if (messages.isEmpty) {
                if (!message.isNullOrEmpty()) {
                    messages["message"] = message
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
        }

        return messages
    }
}