/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire

import android.util.Log
import java.util.*

/**
 * Created by Abdullah Hussein on 7/3/2018.
 * for more details : a.hussein@dce.sa
 */

private const val TAG = "DataHandler"

open class DataHandler<T> : DataHandlerInterface<T> {

    companion object {
        fun getNetworkErrorMessage(o: Any): String {
            return when (o) {
                is String -> o.toString()
                is androidx.collection.ArrayMap<*, *> -> {
                    val map = o as androidx.collection.ArrayMap<*, *>?
                    if (map == null || map.isEmpty)
                        ""
                    else {
                        if (map.keyAt(0) == "response")
                            map[map.keyAt(1)] as String
                        else
                            map[map.keyAt(0)] as String
                    }
                }
                else -> ""
            }
        }

        fun getNetworkErrorResponse(o: Any): String {
            return when (o) {
                is androidx.collection.ArrayMap<*, *> -> {
                    val map = o as androidx.collection.ArrayMap<*, *>?
                    if (map == null || map.isEmpty)
                        ""
                    else
                        map["response"] as String
                }
                else -> ""
            }
        }
    }

    override fun onSuccess(response: String, source: SourceType) {
        Log.e(TAG, "DataHandler onSuccess $response")
    }

    override fun onSuccess(objects: ArrayList<T>, source: SourceType) {
        Log.e(TAG, "onSuccess ArrayList<T> not implemented")
    }

    override fun onSuccess(t: T, source: SourceType) {
        Log.e(TAG, "onSuccess T not implemented")
    }

    override fun onFail(o: Any, isConnectToInternet: Boolean) {
        Log.e(TAG, "onFail object not implemented")
    }
}