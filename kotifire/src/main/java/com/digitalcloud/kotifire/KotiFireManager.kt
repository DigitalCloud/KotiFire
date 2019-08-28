/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire

import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import androidx.collection.ArrayMap
import com.digitalcloud.kotifire.models.RequestModel
import com.digitalcloud.kotifire.provides.cache.CacheProvider
import com.digitalcloud.kotifire.provides.cache.HawkCacheProvider
import com.digitalcloud.kotifire.provides.network.NetworkProvider
import com.digitalcloud.kotifire.provides.network.volley.DataPart
import com.digitalcloud.kotifire.provides.network.volley.VolleyNetworkProvider
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*
import kotlin.reflect.KClass

/**
 * Created by Abdullah Hussein on 11/14/2018.
 * for more details : a.hussein@dce.sa
 */

private val gson = Gson()

class KotiFireManager<T : Any>(private val context: Context, val type: KClass<T>) {

    private val cacheProvider: CacheProvider<T>
        get() = HawkCacheProvider(context, type)

    private val networkProvider: NetworkProvider<T>
        get() = VolleyNetworkProvider(context, type)

    fun postNetworkOnly(url: String, dataHandler: DataHandler<T>) {
        networkProvider.post(url, dataHandler)
    }

    fun postNetworkOnly(url: String, requestModel: RequestModel, dataHandler: DataHandler<T>) {
        networkProvider.post(url, requestModel, dataHandler)
    }

    fun postNetworkWithImageOnly(
        url: String,
        requestModel: RequestModel,
        imagePath: String,
        dataHandler: DataHandler<T>
    ) {
        networkProvider.postWithImage(url, requestModel, imagePath, dataHandler)
    }

    fun postNetworkWithImages(
        url: String,
        params: ArrayMap<String, DataPart>,
        dataHandler: DataHandler<T>
    ) {
        networkProvider.postWithImages(url, params, dataHandler)
    }

    fun postNetworkWithImages(
        url: String,
        requestModel: RequestModel,
        params: ArrayMap<String, DataPart>,
        dataHandler: DataHandler<T>
    ) {
        networkProvider.postWithImages(url, requestModel, params, dataHandler)
    }

    fun getNetworkOnly(url: String, dataHandler: DataHandler<T>) {
        networkProvider.get(url, dataHandler)
    }

    fun getNetworkOnly(url: String, requestModel: RequestModel, dataHandler: DataHandler<T>) {
        val u = url + requestModel.generateGetParams()
        networkProvider.get(u, dataHandler)
    }

    fun getCacheOnly(url: String, dataHandler: DataHandler<T>) {
        cacheProvider.get(url, dataHandler)
    }

    fun getCacheThenNetwork(url: String, dataHandler: DataHandler<T>) {
        val fullURL = KotiFire.instance.getBaseUrl() + url

        cacheProvider.get(fullURL, dataHandler)
        networkProvider.get(fullURL, object : DataHandlerInterface<T> {

            override fun onSuccess(response: String, source: SourceType) {
                dataHandler.onSuccess(response, SourceType.NETWORK)
            }

            override fun onSuccess(objects: ArrayList<T>, source: SourceType) {
                if (!cacheProvider.isLikeCache(fullURL, objects)) {
                    dataHandler.onSuccess(objects, SourceType.NETWORK)
                    cacheProvider.put(fullURL, objects)
                }
            }

            override fun onSuccess(t: T, source: SourceType) {
                if (!cacheProvider.isLikeCache(fullURL, t)) {
                    dataHandler.onSuccess(t, SourceType.NETWORK)
                    cacheProvider.put(fullURL, t)
                }
            }

            override fun onFail(o: Any, isConnectToInternet: Boolean) {
                dataHandler.onFail(o, isConnectToInternet)
            }
        })
    }

    fun getCacheThenNetwork(url: String, requestModel: RequestModel, dataHandler: DataHandler<T>) {
        val newUrl = url + requestModel.generateGetParams()

        cacheProvider.get(newUrl, dataHandler)
        networkProvider.get(newUrl, object : DataHandlerInterface<T> {

            override fun onSuccess(response: String, network: SourceType) {
                dataHandler.onSuccess(response, SourceType.NETWORK)
            }

            override fun onSuccess(objects: ArrayList<T>, source: SourceType) {
                if (!cacheProvider.isLikeCache(newUrl, objects)) {
                    dataHandler.onSuccess(objects, SourceType.NETWORK)
                    cacheProvider.put(newUrl, objects)
                }
            }

            override fun onSuccess(t: T, source: SourceType) {
                if (!cacheProvider.isLikeCache(newUrl, t)) {
                    dataHandler.onSuccess(t, SourceType.NETWORK)
                    cacheProvider.put(newUrl, t)
                }
            }

            override fun onFail(o: Any, isConnectToInternet: Boolean) {
                dataHandler.onFail(o, isConnectToInternet)
            }
        })
    }

    fun getNetworkElseCache(url: String, dataHandler: DataHandler<T>) {
        if (isNetworkConnected()) {
            networkProvider.get(url, object : DataHandlerInterface<T> {

                override fun onSuccess(response: String, network: SourceType) {
                    dataHandler.onSuccess(response, SourceType.NETWORK)
                }

                override fun onSuccess(objects: ArrayList<T>, source: SourceType) {
                    if (!cacheProvider.isLikeCache(url, objects)) {
                        dataHandler.onSuccess(objects, SourceType.NETWORK)
                        cacheProvider.put(url, objects)
                    }
                }

                override fun onSuccess(t: T, source: SourceType) {
                    if (!cacheProvider.isLikeCache(url, t)) {
                        dataHandler.onSuccess(t, SourceType.NETWORK)
                        cacheProvider.put(url, t)
                    }
                }

                override fun onFail(o: Any, isConnectToInternet: Boolean) {
                    dataHandler.onFail(o, isConnectToInternet)
                }
            })
        } else {
            cacheProvider.get(url, dataHandler)
        }
    }

    fun getNetworkElseCache(url: String, requestModel: RequestModel, dataHandler: DataHandler<T>) {

        val newUrl = url + requestModel.generateGetParams()

        if (isNetworkConnected()) {
            networkProvider.get(newUrl, object : DataHandlerInterface<T> {
                override fun onSuccess(response: String, network: SourceType) {
                    dataHandler.onSuccess(response, SourceType.NETWORK)
                }

                override fun onSuccess(objects: ArrayList<T>, source: SourceType) {
                    if (!cacheProvider.isLikeCache(newUrl, objects)) {
                        dataHandler.onSuccess(objects, SourceType.NETWORK)
                        cacheProvider.put(newUrl, objects)
                    }
                }

                override fun onSuccess(t: T, source: SourceType) {
                    if (!cacheProvider.isLikeCache(newUrl, t)) {
                        dataHandler.onSuccess(t, SourceType.NETWORK)
                        cacheProvider.put(newUrl, t)
                    }
                }

                override fun onFail(o: Any, isConnectToInternet: Boolean) {
                    dataHandler.onFail(o, isConnectToInternet)
                }
            })
        } else {
            cacheProvider.get(newUrl, dataHandler)
        }
    }

    fun patchNetwork(url: String, dataHandler: DataHandler<T>) {
        networkProvider.patch(url, dataHandler)
    }

    fun patchNetwork(url: String, requestModel: RequestModel, dataHandler: DataHandler<T>) {
        networkProvider.patch(url, requestModel, dataHandler)
    }

    fun putNetwork(url: String, dataHandler: DataHandler<T>) {
        networkProvider.put(url, dataHandler)
    }

    fun putNetwork(url: String, requestModel: RequestModel, dataHandler: DataHandler<T>) {
        networkProvider.put(url, requestModel, dataHandler)
    }

    fun deleteNetwork(url: String, dataHandler: DataHandler<T>) {
        networkProvider.delete(url, dataHandler)
    }

    fun deleteNetwork(url: String, requestModel: RequestModel, dataHandler: DataHandler<T>) {
        networkProvider.delete(url, requestModel, dataHandler)
    }

    private fun isNetworkConnected(): Boolean {
        val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork = cm.activeNetworkInfo
        return activeNetwork.isConnected
    }
}