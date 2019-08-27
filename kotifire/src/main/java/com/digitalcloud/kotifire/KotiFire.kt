/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire

import android.content.Context
import androidx.collection.ArrayMap
import com.orhanobut.hawk.Hawk

/**
 * Created by Abdullah Hussein on 11/14/2018.
 * for more details : a.hussein@dce.sa
 */
class KotiFire private constructor(url: String, arrayMap: ArrayMap<String, String>) {

    init {
        baseUrl = url
        headers = arrayMap
    }

    fun getBaseUrl(): String {
        return baseUrl
    }

    fun getHeaders(): ArrayMap<String, String> {
        return headers
    }

    fun updateBaseURL(newBaseURL: String) {
        baseUrl = newBaseURL
    }

    fun updateHeaders(newHeaders: ArrayMap<String, String>) {
        headers = newHeaders
    }

    companion object {

        private var baseUrl: String = ""
        private var headers: ArrayMap<String, String> = ArrayMap()

        private var ourInstance: KotiFire? = null

        @JvmStatic
        fun initialize(context: Context, baseUrl: String, headers: ArrayMap<String, String>) {
            ourInstance = KotiFire(baseUrl, headers)
            Hawk.init(context).build()
        }

        val instance: KotiFire
            get() {
                if (ourInstance == null)
                    throw NullPointerException("Repository must be initialize")

                return ourInstance as KotiFire
            }
    }
}