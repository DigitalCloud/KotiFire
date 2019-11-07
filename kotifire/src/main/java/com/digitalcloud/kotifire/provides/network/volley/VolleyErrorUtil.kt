/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire.provides.network.volley

import com.android.volley.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

import java.util.HashMap

/**
 * Created by Abdullah Hussein on 7/4/2018.
 * for more details : a.hussein@dce.sa
 */

object VolleyErrorUtil {

    private val gson = Gson()

    /**
     * Gets message.
     *
     * @param error the error
     * @return the message
     */
    fun getMessage(error: VolleyError?): String {
        try {
            if (error != null) {
                if (error is TimeoutError) {
                    return VolleySingleton.defaultError
                } else if (isServerProblem(error)) {
                    return handleServerError(error)
                } else if (isNetworkProblem(error)) {
                    return VolleySingleton.internetError
                }
                return VolleySingleton.defaultError
            } else {
                return VolleySingleton.defaultError
            }
        } catch (e: Exception) {
            return VolleySingleton.defaultError
        }

    }

    private fun handleServerError(error: VolleyError): String {
        val response = error.networkResponse
        try {
            val result = gson.fromJson<HashMap<String, String>>(
                String(response.data),
                object : TypeToken<Map<String, String>>() {}.type
            )
            return if (result != null && result.containsKey("error")) {
                result["error"] ?: VolleySingleton.defaultError
            } else if (result != null && result.containsKey("message")) {
                result["message"] ?: VolleySingleton.defaultError
            } else {
                return VolleySingleton.defaultError
            }
        } catch (e: Exception) {
            return VolleySingleton.defaultError
        }
    }

    private fun isServerProblem(error: Any): Boolean {
        return error is ServerError || error is AuthFailureError
    }

    private fun isNetworkProblem(error: Any): Boolean {
        return error is NetworkError
    }
}