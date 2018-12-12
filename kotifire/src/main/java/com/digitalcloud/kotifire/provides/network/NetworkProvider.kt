/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire.provides.network

import android.content.Context
import android.support.v4.util.ArrayMap
import com.digitalcloud.kotifire.DataHandlerInterface
import com.digitalcloud.kotifire.provides.network.volley.DataPart
import com.digitalcloud.kotifire.models.RequestModel
import kotlin.reflect.KClass

/**
 * Created by Abdullah Hussein on 7/2/2018.
 * for more details : a.hussein@dce.sa
 */
abstract class NetworkProvider<T : Any> internal constructor(val context: Context, val type: KClass<T>) {

    abstract fun get(url: String, dataHandler: DataHandlerInterface<T>)

    abstract fun post(url: String, dataHandler: DataHandlerInterface<T>)

    abstract fun post(url: String, requestModel: RequestModel, dataHandler: DataHandlerInterface<T>)

    abstract fun postWithImage(
            url: String,
            requestModel: RequestModel,
            imagePath: String,
            dataHandler: DataHandlerInterface<T>
    )

    abstract fun postWithImages(url: String, params: ArrayMap<String, DataPart>, dataHandler: DataHandlerInterface<T>)

    abstract fun postWithImages(
            url: String,
            requestModel: RequestModel,
            params: ArrayMap<String, DataPart>,
            dataHandler: DataHandlerInterface<T>
    )

    abstract fun put(url: String, dataHandler: DataHandlerInterface<T>)

    abstract fun put(url: String, requestModel: RequestModel, dataHandler: DataHandlerInterface<T>)

    abstract fun patch(url: String, dataHandler: DataHandlerInterface<T>)

    abstract fun patch(url: String, requestModel: RequestModel, dataHandler: DataHandlerInterface<T>)

    abstract fun delete(url: String, dataHandler: DataHandlerInterface<T>)

    abstract fun delete(url: String, requestModel: RequestModel, dataHandler: DataHandlerInterface<T>)
}