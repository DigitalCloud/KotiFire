/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire.models

import com.google.gson.Gson

/**
 * Created by Abdullah Hussein on 7/4/2018.
 * for more details : a.hussein@dce.sa
 */
data class ResponseModel(
    var message: String = "",
    var go_to: String = "",
    var data: Any = Any(),
    var errors: Any = Any(),
    var remember_token: String = "",
    var access_token: String = ""
) {
    val dataAsString: String
        get() = Gson().toJson(data)
}