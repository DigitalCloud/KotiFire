/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire

import android.content.Context
import androidx.collection.ArrayMap
import com.digitalcloud.kotifire.provides.network.volley.VolleySingleton
import com.orhanobut.hawk.Hawk

/**
 * Created by Abdullah Hussein on 11/14/2018.
 * for more details : a.hussein@dce.sa
 */
object KotiFire {

    var baseUrl: String = ""
    var headers: ArrayMap<String, String> = ArrayMap()

    fun initialize(
        context: Context,
        baseUrl: String,
        headers: ArrayMap<String, String>,
        internalError: String
    ) {
        this.baseUrl = baseUrl
        this.headers = headers

        Hawk.init(context).build()
        VolleySingleton.initInstance(context, internalError, internalError)
    }

    fun setNewBaseUrl(baseUrl: String) {
        this.baseUrl = baseUrl
    }

    fun setNewHeaders(headers: ArrayMap<String, String>) {
        this.headers = headers
    }
}