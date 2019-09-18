/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire

/**
 * Created by Abdullah Hussein on 11/11/2018.
 * for more details : a.hussein@dce.sa
 */
enum class KotiCachePolicy(val type: Int) {
    NETWORK_ONLY(1),
    CACHE_ONLY(2),
    CACHE_THEN_NETWORK(3),
    NETWORK_ELSE_CACHE(4)
}