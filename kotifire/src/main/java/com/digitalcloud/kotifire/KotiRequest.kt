package com.digitalcloud.kotifire

import androidx.collection.ArrayMap
import kotlin.reflect.KClass

class KotiRequest<T : Any>(var responseType: KClass<T>) {
    var baseURl: String = KotiFire.instance.getBaseUrl()
    var endpoint: String = ""
    var headers: ArrayMap<String, String> = KotiFire.instance.getHeaders()
    var method: KotiMethod = KotiMethod.GET
    var params: ArrayMap<String, String> = ArrayMap()
    var files = ""
    var cachingType: KotiCachePolicy = KotiCachePolicy.NETWORK_ONLY
    var mDataHandler: DataHandlerInterface<T>? = null
}