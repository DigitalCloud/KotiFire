/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire.provides.network

import com.digitalcloud.kotifire.KotiRequest
import kotlin.reflect.KClass

/**
 * Created by Abdullah Hussein on 7/2/2018.
 * for more details : a.hussein@dce.sa
 */
abstract class NetworkProvider<T : Any> internal constructor(val type: KClass<T>) {
    abstract fun makeRequest(mKotiRequest: KotiRequest<T>)
}