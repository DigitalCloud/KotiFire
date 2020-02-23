package com.digitalcloud.kotifire

import androidx.collection.ArrayMap
import com.digitalcloud.kotifire.models.RequestModel
import java.io.File
import kotlin.reflect.KClass

class KotiRequest<T : Any>(var responseType: KClass<T>) {
    var baseURl: String = KotiFire.baseUrl
    var endpoint: String = ""
    var headers: ArrayMap<String, String> = KotiFire.headers
    var method: KotiMethod = KotiMethod.GET
    var params: RequestModel = RequestModel()
    var files: ArrayMap<String, File> = ArrayMap()
    var cachingType: KotiCachePolicy = KotiCachePolicy.NETWORK_ONLY
    var mDataHandler: DataHandlerInterface<T>? = null
}