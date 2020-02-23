/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire.provides.cache

import com.digitalcloud.kotifire.DataHandler
import kotlin.reflect.KClass

/**
 * Created by Abdullah Hussein on 7/2/2018.
 * for more details : a.hussein@dce.sa
 */
abstract class CacheProvider<T : Any>(val type: KClass<T>) {

    abstract fun put(url: String, response: String)

    abstract fun get(url: String, dataHandler: DataHandler<T>)

    abstract fun isNotTheSameCache(key: String, data: String): Boolean
}