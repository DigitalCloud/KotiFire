/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire

import android.content.Context
import android.util.Log
import com.digitalcloud.kotifire.provides.network.volley.VolleyNetworkProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * Created by Abdullah Hussein on 11/14/2018.
 * for more details : a.hussein@dce.sa
 */

class KotiFireProvider {

    fun <T : Any> execute(context: Context, mKotiRequest: KotiRequest<T>) {
        GlobalScope.launch {
            runCatching {
                executeKotiRequest(context ,mKotiRequest)
            }.onFailure(::handleFailure)
        }
    }

    private fun <T : Any> executeKotiRequest(context: Context , mKotiRequest: KotiRequest<T>) {
        when (mKotiRequest.cachingType) {
            KotiCachePolicy.CACHE_ONLY -> {
            }
            KotiCachePolicy.NETWORK_ONLY -> {
                requestNetworkOnly(context, mKotiRequest)
            }
            KotiCachePolicy.CACHE_THEN_NETWORK -> {
            }
            KotiCachePolicy.NETWORK_ELSE_CACHE -> {
            }
        }
    }

    private fun <T : Any>  requestNetworkOnly(context: Context , mKotiRequest: KotiRequest<T>) {
        VolleyNetworkProvider(context,  mKotiRequest.responseType).makeRequest(mKotiRequest)
    }

    private fun handleFailure(throwable: Throwable) {
        Log.e("Error", throwable.localizedMessage)
    }
}