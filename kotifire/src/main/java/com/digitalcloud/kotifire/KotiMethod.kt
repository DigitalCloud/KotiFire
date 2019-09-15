/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire

/**
 * Created by Abdullah Hussein on 11/11/2018.
 * for more details : a.hussein@dce.sa
 */
enum class KotiMethod(val type: Int) {
    GET(0),
    POST(1),
    PUT(2),
    DELETE(3),
    HEAD(4),
    OPTIONS(5),
    TRACE(6),
    PATCH(7)
}