/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire.provides.network.volley

import android.content.Context
import android.support.v4.util.ArrayMap
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.digitalcloud.kotifire.KotiFire
import com.digitalcloud.kotifire.DataHandlerInterface
import com.digitalcloud.kotifire.models.RequestModel
import com.digitalcloud.kotifire.provides.network.NetworkProvider
import com.digitalcloud.kotifire.SourceType
import com.digitalcloud.kotifire.models.ResponseModel
import com.google.gson.Gson
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

    override fun get(url: String, dataHandler: DataHandlerInterface<T>) {
        Log.e(TAG, "get url : $url")
        makeStringRequest(Request.Method.GET, url, dataHandler)
    }

    override fun post(url: String, dataHandler: DataHandlerInterface<T>) {
        Log.e(TAG, "post url : $url")
        makeStringRequest(Request.Method.POST, url, dataHandler)
    }

    override fun post(url: String, requestModel: RequestModel, dataHandler: DataHandlerInterface<T>) {
        Log.e(TAG, "post url : $url")
        makeStringRequest(Request.Method.POST, url, requestModel.generatePostParams(), dataHandler)
    }

    override fun postWithImage(
        url: String,
        requestModel: RequestModel,
        imagePath: String,
        dataHandler: DataHandlerInterface<T>
    ) {
        var url = url

        if (!url.startsWith("http")) {
            url = KotiFire.instance.getBaseUrl() + url
        }

        Log.e(TAG, "postWithImage url : $url")

        val volleyMultipartRequest =
            object : VolleyMultipartRequest(Request.Method.POST, url, Response.Listener { response ->
                val data = String(response.data)
                Log.e(TAG, data)
                handleResponse(data, dataHandler)
            }, Response.ErrorListener { error -> extractResponseError(error, dataHandler) }) {

                protected override val byteData: Map<String, DataPart>?
                    get() {
                        val params = ArrayMap<String, DataPart>()
                        if (imagePath != null) {
                            params["image"] =
                                    DataPart(imagePath, "image/jpeg")
                            Log.e("image_path", imagePath)
                        }
                        return params
                    }

                override fun getParams(): Map<String, String> {
                    return requestModel.generatePostParams()
                }
            }

        VolleySingleton.getInstance(context).addToRequestQueue(volleyMultipartRequest)
    }

    override fun postWithImages(url: String, params: ArrayMap<String, DataPart>, dataHandler: DataHandlerInterface<T>) {
        var url = url

        if (!url.startsWith("http")) {
            url = KotiFire.instance.getBaseUrl() + url
        }

        Log.e(TAG, "postWithImage url : $url")

        val volleyMultipartRequest =
            object : VolleyMultipartRequest(Request.Method.POST, url, Response.Listener { response ->
                val data = String(response.data)
                Log.e(TAG, data)
                handleResponse(data, dataHandler)
            }, Response.ErrorListener { error -> extractResponseError(error, dataHandler) }
            ) {
                protected override val byteData: Map<String, DataPart>?
                    get() = params
            }

        VolleySingleton.getInstance(context).addToRequestQueue(volleyMultipartRequest)
    }

    override fun postWithImages(
        url: String,
        requestModel: RequestModel,
        params: ArrayMap<String, DataPart>,
        dataHandler: DataHandlerInterface<T>
    ) {
        var url = url

        if (!url.startsWith("http")) {
            url = KotiFire.instance.getBaseUrl() + url
        }

        Log.e(TAG, "postWithImage url : $url")

        val volleyMultipartRequest =
            object : VolleyMultipartRequest(Request.Method.POST, url, Response.Listener { response ->
                val data = String(response.data)
                Log.e(TAG, data)
                handleResponse(data, dataHandler)
            }, Response.ErrorListener { error -> extractResponseError(error, dataHandler) }) {

                protected override val byteData: Map<String, DataPart>?
                    get() = params

                override fun getParams(): Map<String, String> {
                    return requestModel.generatePostParams()
                }
            }

        VolleySingleton.getInstance(context).addToRequestQueue(volleyMultipartRequest)
    }

    override fun put(url: String, dataHandler: DataHandlerInterface<T>) {
        Log.e(TAG, "put url : $url")
        makeStringRequest(Request.Method.PUT, url, dataHandler)
    }

    override fun put(url: String, requestModel: RequestModel, dataHandler: DataHandlerInterface<T>) {
        Log.e(TAG, "put url : $url")
        makeStringRequest(Request.Method.PUT, url, requestModel.generatePostParams(), dataHandler)
    }

    override fun patch(url: String, dataHandler: DataHandlerInterface<T>) {
        Log.e(TAG, "patch url : $url")

        val params = ArrayMap<String, String>()
        params["_method"] = "patch"

        makeStringRequest(Request.Method.POST, url, params, dataHandler)
    }

    override fun patch(url: String, requestModel: RequestModel, dataHandler: DataHandlerInterface<T>) {
        Log.e(TAG, "patch url : $url")

        val params = requestModel.generatePostParams()
        params["_method"] = "patch"

        makeStringRequest(Request.Method.POST, url, params, dataHandler)
    }

    override fun delete(url: String, dataHandler: DataHandlerInterface<T>) {
        Log.e(TAG, "delete url : $url")
        makeStringRequest(Request.Method.DELETE, url, dataHandler)
    }

    override fun delete(url: String, requestModel: RequestModel, dataHandler: DataHandlerInterface<T>) {
        Log.e(TAG, "delete url : $url")
        makeStringRequest(Request.Method.DELETE, url, requestModel.generatePostParams(), dataHandler)
    }

    private fun makeStringRequest(method: Int, url: String, dataHandler: DataHandlerInterface<T>) {
        var url = url
        if (!url.startsWith("http")) {
            url = KotiFire.instance.getBaseUrl() + url
        }

        val stringRequest = object : StringRequest(method, url, Response.Listener { response ->
            Log.e(TAG, response)
            handleResponse(response, dataHandler)
        }, Response.ErrorListener { error -> extractResponseError(error, dataHandler) }) {
            override fun getHeaders(): Map<String, String> {
                return KotiFire.instance.getHeaders()
            }
        }

        VolleySingleton.getInstance(context).addToRequestQueue(stringRequest)
    }

    private fun makeStringRequest(
        method: Int,
        url: String,
        params: ArrayMap<String, String>,
        dataHandler: DataHandlerInterface<T>
    ) {
        var url = url

        if (!url.startsWith("http")) {
            url = KotiFire.instance.getBaseUrl() + url
        }

        val stringRequest = object : StringRequest(method, url, Response.Listener { response ->
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

    private fun handleResponse(response: String, dataHandler: DataHandlerInterface<T>) {
        when (type) {
            String::class -> {
                //dataHandler.onSuccess(response, SourceType.NETWORK)
            }
            ResponseModel::class -> dataHandler.onSuccess(
                gson.fromJson<T>(response, type.java),
                SourceType.NETWORK
            )
            else -> {
                val responseModel = gson.fromJson(response, ResponseModel::class.java)
                val data = responseModel.dataAsString
                try {
                    val tArrayList = ArrayList<T>()
                    val jsonArray = JSONArray(data)
                    for (i in 0 until jsonArray.length()) {
                        val jsonString = jsonArray.get(i).toString()
                        val `object` = gson.fromJson(jsonString, type.java)
                        tArrayList.add(`object`)
                    }
                    dataHandler.onSuccess(tArrayList, SourceType.NETWORK)
                } catch (e: Exception) {
                    dataHandler.onSuccess(gson.fromJson(data, type.java), SourceType.NETWORK)
                }
            }
        }
    }

    private fun extractResponseError(error: VolleyError?, dataHandler: DataHandlerInterface<T>?) {
        try {
            if (dataHandler != null) {
                if (error?.networkResponse != null) {

                    val response = String(error.networkResponse.data)
                    val responseModel = gson.fromJson(response, ResponseModel::class.java)

                    Log.e(TAG, "statusCode : " + error.networkResponse.statusCode)
                    Log.e(TAG, "response : $response")

                    if (error.networkResponse.statusCode == 401) {
                        goToUserGuideActivity()
                        return
                    }

                    if (error.networkResponse.statusCode == 505) {
                        showForceUpdate()
                        return
                    }

                    if (responseModel != null && !responseModel.go_to.isNullOrEmpty()) {
                        goToReActivateUserActivity()
                        return
                    }

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

    private fun goToReActivateUserActivity() {

    }

    private fun showForceUpdate() {

    }

    private fun goToUserGuideActivity() {


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