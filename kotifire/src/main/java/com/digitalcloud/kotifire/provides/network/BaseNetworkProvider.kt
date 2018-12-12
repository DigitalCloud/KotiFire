/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire.provides.network

import android.content.Context
import android.support.v4.util.ArrayMap
import com.digitalcloud.kotifire.DataHandlerInterface
import com.digitalcloud.kotifire.models.RequestModel
import com.digitalcloud.kotifire.provides.network.NetworkHandler
import kotlin.reflect.KClass

/**
 * Created by Abdullah Hussein on 7/2/2018.
 * for more details : a.hussein@dce.sa
 */
internal interface BaseNetworkProvider {

    fun get(url: String, networkHandler: NetworkHandler)

//    fun post(url: String, networkHandler: NetworkHandler)
//
//    fun post(url: String, requestModel: RequestModel, networkHandler: NetworkHandler)
//
//    fun postFiles(url: String, params: ArrayMap<String, String>, networkHandler: NetworkHandler)
//
//    fun postFiles(
//        url: String,
//        requestModel: RequestModel,
//        params: ArrayMap<String, String>,
//        networkHandler: NetworkHandler
//    )
//
//    fun put(url: String, networkHandler: NetworkHandler)
//
//    fun put(url: String, requestModel: RequestModel, networkHandler: NetworkHandler)
//
//    fun patch(url: String, networkHandler: NetworkHandler)
//
//    fun patch(url: String, requestModel: RequestModel, networkHandler: NetworkHandler)
//
//    fun delete(url: String, networkHandler: NetworkHandler)
//
//    fun delete(url: String, requestModel: RequestModel, networkHandler: NetworkHandler)
}