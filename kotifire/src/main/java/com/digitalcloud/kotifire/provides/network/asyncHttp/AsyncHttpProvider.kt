/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire.provides.network.asyncHttp

import android.util.Log
import com.digitalcloud.kotifire.KotiFire
import com.digitalcloud.kotifire.provides.network.BaseNetworkProvider
import com.digitalcloud.kotifire.provides.network.NetworkHandler
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import java.nio.charset.Charset

/**
 * Created by Abdullah Hussein on 7/3/2018.
 * for more details : a.hussein@dce.sa
 */

private const val TAG = "AsyncHttpProvider"

internal class AsyncHttpProvider : BaseNetworkProvider {
    override fun get(url: String, networkHandler: NetworkHandler) {
        Log.e(TAG, "get url : $url")
        AsyncHttpClient().get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?) {
                if (responseBody != null)
                    networkHandler.onSuccess(responseBody.toString(Charset.forName("UTF-8")))
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                if (responseBody != null)
                    networkHandler.onFailure(statusCode, responseBody.toString(Charset.forName("UTF-8")))
            }
        })
    }
}