/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire.provides.network.volley

import com.android.volley.*
import com.android.volley.toolbox.HttpHeaderParser
import com.digitalcloud.kotifire.KotiFire
import org.json.JSONObject
import java.io.*
import kotlin.math.min

/**
 * Created by Abdullah Hussein on 7/4/2018.
 * for more details : a.hussein@dce.sa
 */

open class VolleyMultipartRequest(
    method: Int,
    url: String,
    listener: Response.Listener<NetworkResponse>,
    errorListener: Response.ErrorListener
) : Request<NetworkResponse>(method, url, errorListener) {
    private val twoHyphens = "--"
    private val lineEnd = "\r\n"
    private val boundary = "apiclient-" + System.currentTimeMillis()

    private var mListener: Response.Listener<NetworkResponse> = listener
    private var mErrorListener: Response.ErrorListener = errorListener

    protected open val jsonObject: JSONObject?
        get() = null

    protected open val byteData: Map<String, DataPart>?
        get() = null

    override fun getHeaders(): Map<String, String> {
        return KotiFire.headers
    }

    override fun getBodyContentType(): String {
        return "multipart/form-data;boundary=$boundary"
    }

    @Throws(AuthFailureError::class)
    override fun getBody(): ByteArray? {
        val bos = ByteArrayOutputStream()
        val dos = DataOutputStream(bos)

        try {
            // populate text payload
            if (jsonObject != null) {
                jsonObject!!.keys().forEach { item ->
                    buildTextPart(dos, item, jsonObject!!.get(item).toString())
                }
            }

            // populate data byte payload
            val data = byteData
            if (data != null && data.isNotEmpty()) {
                dataParse(dos, data)
            }

            // close multipart form data after text and file data
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd)
            return bos.toByteArray()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return null
    }

    override fun parseNetworkResponse(response: NetworkResponse): Response<NetworkResponse> {
        return try {
            Response.success(response, HttpHeaderParser.parseCacheHeaders(response))
        } catch (e: Exception) {
            Response.error(ParseError(e))
        }

    }

    override fun deliverResponse(response: NetworkResponse) {
        mListener.onResponse(response)
    }

    override fun deliverError(error: VolleyError) {
        mErrorListener.onErrorResponse(error)
    }

    @Throws(IOException::class)
    private fun textParse(
        dataOutputStream: DataOutputStream,
        params: Map<String, String>,
        encoding: String
    ) {
        try {
            for ((key, value) in params) {
                buildTextPart(dataOutputStream, key, value)
            }
        } catch (uee: UnsupportedEncodingException) {
            throw RuntimeException("Encoding not supported: $encoding", uee)
        }

    }

    @Throws(IOException::class)
    private fun dataParse(dataOutputStream: DataOutputStream, data: Map<String, DataPart>) {
        for ((key, value) in data) {
            buildDataPart(dataOutputStream, value, key)
        }
    }

    @Throws(IOException::class)
    private fun buildTextPart(
        dataOutputStream: DataOutputStream,
        parameterName: String,
        parameterValue: String
    ) {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd)
        dataOutputStream.writeBytes("Content-Disposition: form-data; name=\"$parameterName\"$lineEnd")
        dataOutputStream.writeBytes(lineEnd)
        dataOutputStream.write(parameterValue.toByteArray(charset("utf-8")))
        dataOutputStream.writeBytes(lineEnd)
    }

    @Throws(IOException::class)
    private fun buildDataPart(
        dataOutputStream: DataOutputStream,
        dataFile: DataPart,
        inputName: String
    ) {
        dataOutputStream.writeBytes(twoHyphens + boundary + lineEnd)
        dataOutputStream.writeBytes(
            "Content-Disposition: form-data; name=\"" +
                    inputName + "\"; filename=\"" + dataFile.fileName + "\"" + lineEnd
        )
        if (dataFile.type != null && dataFile.type!!.trim { it <= ' ' }.isNotEmpty()) {
            dataOutputStream.writeBytes("Content-Type: " + dataFile.type + lineEnd)
        }
        dataOutputStream.writeBytes(lineEnd)

        val fileInputStream = ByteArrayInputStream(dataFile.content)
        var bytesAvailable = fileInputStream.available()

        val maxBufferSize = 1024 * 1024
        var bufferSize = min(bytesAvailable, maxBufferSize)
        val buffer = ByteArray(bufferSize)

        var bytesRead = fileInputStream.read(buffer, 0, bufferSize)

        while (bytesRead > 0) {
            dataOutputStream.write(buffer, 0, bufferSize)
            bytesAvailable = fileInputStream.available()
            bufferSize = min(bytesAvailable, maxBufferSize)
            bytesRead = fileInputStream.read(buffer, 0, bufferSize)
        }

        dataOutputStream.writeBytes(lineEnd)
    }
}