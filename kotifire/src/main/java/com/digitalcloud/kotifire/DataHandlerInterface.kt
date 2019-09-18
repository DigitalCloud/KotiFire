/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire

import java.util.ArrayList

/**
 * Created by Abdullah Hussein on 7/3/2018.
 * for more details : a.hussein@dce.sa
 */
interface DataHandlerInterface<T> {

    fun onSuccess(response: String, source: SourceType)

    fun onSuccess(objects: ArrayList<T>, source: SourceType)

    fun onSuccess(t: T, source: SourceType)

    fun onFail(o: Any, isConnectToInternet: Boolean)
}