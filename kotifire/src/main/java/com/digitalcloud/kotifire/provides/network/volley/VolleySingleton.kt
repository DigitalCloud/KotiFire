/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire.provides.network.volley

import android.content.Context
import android.text.TextUtils
import com.android.volley.DefaultRetryPolicy
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.toolbox.Volley

import java.net.CookieHandler
import java.net.CookieManager
import java.net.CookiePolicy

/**
 * Created by Abdullah Hussein on 7/2/2018.
 * for more details : a.hussein@dce.sa
 */

class VolleySingleton private constructor() {

    init {
        CookieHandler.setDefault(CookieManager(null, CookiePolicy.ACCEPT_ALL))
    }

    fun <T> addToRequestQueue(req: Request<T>, tag: String) {
        req.tag = if (TextUtils.isEmpty(tag)) TAG else tag
        req.setShouldCache(true)

        req.retryPolicy = DefaultRetryPolicy(
            50000,
            3,
            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT
        )

        mRequestQueue!!.add(req)
    }

    fun <T> addToRequestQueue(req: Request<T>) {
        addToRequestQueue(req, "")
    }

    fun cancelPendingRequests(tag: Any) {
        if (mRequestQueue != null) {
            mRequestQueue!!.cancelAll(tag)
        }
    }

    companion object {
        val TAG = VolleySingleton::class.java.simpleName
        private var mRequestQueue: RequestQueue? = null
        private var mInstance: VolleySingleton? = null

        @Synchronized
        fun getInstance(context: Context): VolleySingleton {
            if (mInstance == null) {
                mInstance = VolleySingleton()
                mRequestQueue = Volley.newRequestQueue(context.applicationContext)
            }
            return mInstance as VolleySingleton
        }
    }
}