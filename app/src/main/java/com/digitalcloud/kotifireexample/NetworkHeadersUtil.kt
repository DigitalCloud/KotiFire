/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifireexample

import androidx.collection.ArrayMap
import android.util.Log

/**
 * Created by Abdullah Hussein on 12/07/2017.
 * abdullah.hussein109@gmail.com
 */
object NetworkHeadersUtil {

    private const val TAG = "Network Headers"

    private const val AUTHORIZATION_HEADER = "Authorization"

    private const val CONTENT_TYPE_HEADER = "Content-Type"
    private const val LANGUAGE_HEADER = "Accept-Language"
    private const val API_VERSION_HEADER = "Api-Version"
    private const val APP_VERSION_HEADER = "App-Version"
    private const val ACCEPT_HEADER = "Accept"

    private const val CONTENT_TYPE_X_WWW = "application/x-www-form-urlencoded"
    private const val CONTENT_TYPE_JSON = "application/json"
    private const val API_VERSION = "1.0"

    val headers: ArrayMap<String, String>
        get() {
            val headers = ArrayMap<String, String>()
            headers[ACCEPT_HEADER] = CONTENT_TYPE_JSON
            headers[API_VERSION_HEADER] = API_VERSION
            headers[APP_VERSION_HEADER] = BuildConfig.VERSION_NAME
            headers[LANGUAGE_HEADER] = "YOUR ACCEPT LANGUAGE"   //ADD ACCEPT LANGUAGE
            headers[AUTHORIZATION_HEADER] = "YOUR ACCESS TOKEN" //ADD ACCESS TOKEN
            Log.e(TAG, "headers : " + headers.toString())
            return headers
        }
}