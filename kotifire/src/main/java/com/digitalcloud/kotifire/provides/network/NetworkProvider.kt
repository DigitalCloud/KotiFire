/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire.provides.network

import android.content.Context
import androidx.collection.ArrayMap
import com.digitalcloud.kotifire.DataHandlerInterface
import com.digitalcloud.kotifire.KotiRequest
import com.digitalcloud.kotifire.provides.network.volley.DataPart
import com.digitalcloud.kotifire.models.RequestModel
import kotlin.reflect.KClass

/**
 * Created by Abdullah Hussein on 7/2/2018.
 * for more details : a.hussein@dce.sa
 */
abstract class NetworkProvider<T : Any> internal constructor(val context: Context, val type: KClass<T>) {
    abstract fun makeRequest(mKotiRequest: KotiRequest<T>)
}