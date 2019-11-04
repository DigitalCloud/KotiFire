/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire

import android.content.Context
import android.util.Log
import com.digitalcloud.kotifire.provides.cache.HawkCacheProvider
import com.digitalcloud.kotifire.provides.network.volley.VolleyNetworkProvider

/**
 * Created by Abdullah Hussein on 11/14/2018.
 * for more details : a.hussein@dce.sa
 */

class KotiFireProvider<T : Any>(private var mKotiRequest: KotiRequest<T>) {

    private var mHawkCacheProvider = HawkCacheProvider(mKotiRequest.responseType)
    private var mVolleyNetworkProvider = VolleyNetworkProvider(mKotiRequest.responseType)

    fun execute() {
        when (mKotiRequest.cachingType) {
            KotiCachePolicy.CACHE_ONLY -> {
                requestCacheOnly()
            }
            KotiCachePolicy.NETWORK_ONLY -> {
                requestNetworkOnly()
            }
            KotiCachePolicy.CACHE_THEN_NETWORK -> {
                requestCacheThenNetworkOnly()
            }
            KotiCachePolicy.NETWORK_ELSE_CACHE -> {
                requestNetworkElseCacheOnly()
            }
        }
    }

    private fun requestCacheOnly() {
        mHawkCacheProvider.makeRequest(mKotiRequest)
    }

    private fun requestNetworkOnly() {
        mVolleyNetworkProvider.makeRequest(mKotiRequest)
    }

    private fun requestCacheThenNetworkOnly() {
        mHawkCacheProvider.makeRequest(mKotiRequest)
        mVolleyNetworkProvider.makeRequest(mKotiRequest)
    }

    private fun requestNetworkElseCacheOnly() {
        mVolleyNetworkProvider.makeRequest(mKotiRequest)
        mHawkCacheProvider.makeRequest(mKotiRequest)
    }
}