/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire.provides.cache

import android.content.Context
import com.digitalcloud.kotifire.DataHandler
import com.digitalcloud.kotifire.SourceType
import com.google.gson.Gson
import com.orhanobut.hawk.Hawk

import java.util.ArrayList
import kotlin.reflect.KClass

/**
 * Created by Abdullah Hussein on 7/3/2018.
 * for more details : a.hussein@dce.sa
 */
internal class HawkCacheProvider<T : Any> internal constructor(context: Context, type: KClass<T>) :
    CacheProvider<T>(context, type) {

    private operator fun get(key: String): T {
        return Hawk.get(key)
    }

    private operator fun contains(key: String): Boolean {
        return Hawk.contains(key)
    }

    override fun put(url: String, t: T): Boolean {
        return Hawk.put(url, t)
    }

    override fun put(url: String, t: ArrayList<T>): Boolean {
        return Hawk.put(url, t)
    }

    override fun get(url: String, dataHandler: DataHandler<T>) {
        if (!contains(url)) return

        val data = get(url)
        try {
            if (data is ArrayList<*>) {
                val objects = data as ArrayList<T>
                dataHandler.onSuccess(objects, SourceType.CACHE)
            } else {
                if (type.isInstance(data)) {
                    dataHandler.onSuccess(data, SourceType.CACHE)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    override fun isLikeCache(key: String, t: T): Boolean {
        if (!contains(key)) return false
        val tCache = get(key)
        val gson = Gson()
        return gson.toJson(t).hashCode() == gson.toJson(tCache).hashCode()
    }

    override fun isLikeCache(key: String, objects: ArrayList<T>): Boolean {
        if (!contains(key)) return false
        val tCache = get(key)
        val gson = Gson()
        return gson.toJson(objects).hashCode() == gson.toJson(tCache).hashCode()
    }

    override fun isNotTheSameCache(key: String, data: String): Boolean {
        if (!contains(key)) return true
        val tCache = get(key)
        return data.hashCode() != tCache.hashCode()
    }
}