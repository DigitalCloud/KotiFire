/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire.provides.network


/**
 * Created by Abdullah Hussein on 11/25/2018.
 * for more details : a.hussein@dce.sa
 */
internal interface NetworkHandler {

    fun onSuccess(response: String)

    fun onFailure(statusCode: Int, response: String)

}