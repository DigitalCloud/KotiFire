/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire.provides.cache

import android.util.Log
import com.digitalcloud.kotifire.DataHandler
import com.digitalcloud.kotifire.KotiRequest
import com.digitalcloud.kotifire.SourceType
import com.google.gson.Gson
import com.orhanobut.hawk.Hawk
import org.json.JSONArray

import java.util.ArrayList
import kotlin.reflect.KClass

/**
 * Created by Abdullah Hussein on 7/3/2018.
 * for more details : a.hussein@dce.sa
 */
internal class HawkCacheProvider<T : Any> internal constructor(type: KClass<T>) :
    CacheProvider<T>(type) {

    private val gson = Gson()

    private operator fun contains(key: String): Boolean {
        return Hawk.contains(key)
    }

    fun getString(url: String): String {
        return Hawk.get(url)
    }

    override fun put(url: String, response: String) {
        Hawk.put(url, response)
    }

    override fun get(url: String, dataHandler: DataHandler<T>) {
        if (!contains(url)) return
        dataHandler.onSuccess(getString(url), SourceType.CACHE)
    }

    override fun isNotTheSameCache(key: String, data: String): Boolean {
        if (!contains(key)) return true
        val tCache = getString(key)
        return data.hashCode() != tCache.hashCode()
    }

    fun makeRequest(mKotiRequest: KotiRequest<T>) {
        val url = mKotiRequest.baseURl + mKotiRequest.endpoint

        Log.e("Error", "makeCacheRequest url : $url")

        if (!contains(url)) return

        val response: String = getString(url)

        Log.e("Error", "Cache Data : $response")

        mKotiRequest.mDataHandler!!.onSuccess(response, SourceType.CACHE)

        when (type) {
            String::class,
            Any::class -> {
                mKotiRequest.mDataHandler!!.onSuccess(response, SourceType.CACHE)
            }
            else -> {
                try {
                    val tArrayList = ArrayList<T>()
                    val jsonArray = JSONArray(response)
                    for (i in 0 until jsonArray.length()) {
                        val jsonString = jsonArray.get(i).toString()
                        val mData = gson.fromJson(jsonString, type.java)
                        tArrayList.add(mData)
                    }
                    mKotiRequest.mDataHandler!!.onSuccess(tArrayList, SourceType.NETWORK)
                } catch (e: Exception) {
                    val res = gson.fromJson(response, type.java)
                    mKotiRequest.mDataHandler!!.onSuccess(res, SourceType.NETWORK)
                }
            }
        }
    }
}