/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire.models

import androidx.collection.ArrayMap
import android.util.Log
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

/**
 * Created by Abdullah Hussein on 7/4/2018.
 * for more details : a.hussein@dce.sa
 */
open class RequestModel {

    fun generatePostParams(): JSONObject {
        val params = JSONObject()

        val fields = javaClass.declaredFields
        for (field in fields) {
            try {
                field.isAccessible = true
                if (field.get(this) != null) {
                    if (field.name.equals("CREATOR", ignoreCase = true)) continue
                    if (field.name.equals("serialVersionUID", ignoreCase = true)) continue

                    val value = field.get(this) ?: continue
                    if (value is ArrayList<*>) {
                        val mJSONArray = JSONArray()
                        value.forEach { item ->
                            if (item is RequestModel) {
                                mJSONArray.put(item.generatePostParams())
                            }
                        }
                        params.put(field.name, mJSONArray)
                    } else {
                        if (value is String)
                            if (value.isEmpty()) continue

                        if (value is Int)
                            if (value == 0) continue

                        params.put(field.name, value)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        Log.e("params", "generatePostParams: $params")

        return params
    }

    fun generateGetParams(): String {
        val temp = StringBuilder()

        val fields = javaClass.declaredFields
        for (field in fields) {
            try {
                field.isAccessible = true
                if (field.get(this) != null) {
                    if (field.name.equals("CREATOR", ignoreCase = true)) continue
                    if (field.name.equals("serialVersionUID", ignoreCase = true)) continue

                    val value = field.get(this) ?: continue

                    temp.append(field.name).append("=").append(value).append("&")
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        if (temp.isNotEmpty()) {
            temp.insert(0, "?")

            if (temp[temp.length - 1] == '&')
                temp.deleteCharAt(temp.length - 1)
        }

        Log.e("params", "generateGetParams: $temp")

        return temp.toString()
    }
}