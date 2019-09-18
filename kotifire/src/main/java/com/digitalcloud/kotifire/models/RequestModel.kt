/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire.models

import androidx.collection.ArrayMap
import android.util.Log
import java.util.*

/**
 * Created by Abdullah Hussein on 7/4/2018.
 * for more details : a.hussein@dce.sa
 */
class RequestModel {

    fun generatePostParams(): ArrayMap<String, String> {
        val params = ArrayMap<String, String>()

        val fields = javaClass.declaredFields
        for (field in fields) {
            try {
                field.isAccessible = true
                if (field.get(this) != null) {
                    if (field.name.equals("CREATOR", ignoreCase = true)) continue
                    if (field.name.equals("serialVersionUID", ignoreCase = true)) continue

                    val o = field.get(this)
                    if (o is ArrayList<*>) {
                        for (i in o.indices) {
                            val fieldValue = o[i].toString()
                            if (fieldValue.isEmpty()) continue
                            val key = String.format(Locale.ENGLISH, field.name + "[%d]", i)
                            params[key] = fieldValue
                        }
                    } else {
                        val fieldValue = o.toString()
                        if (fieldValue.isEmpty()) continue
                        params[field.name] = fieldValue
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

                    temp.append(field.name).append("=").append(field.get(this)).append("&")
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