package com.digitalcloud.kotifire

import androidx.collection.ArrayMap
import com.digitalcloud.kotifire.models.RequestModel
import java.io.File
import kotlin.reflect.KClass

interface KotiRoute {

    val basePath: String

    val method: KotiMethod

    val cachingType: KotiCachePolicy

    val path: String

    val params: RequestModel

    val bytes: ByteArray?

    val files: ArrayMap<String, File>

    val body: String

    val headers: ArrayMap<String, String>

    fun <T : Any> getKotiRequest(
        responseType: KClass<T>,
        mDataHandler: DataHandlerInterface<T>
    ): KotiRequest<T> {
        val request = KotiRequest(responseType)

        request.baseURl = basePath
        request.method = method
        request.cachingType = cachingType
        request.endpoint = path
        request.params = params
        request.headers = headers
        request.mDataHandler = mDataHandler
        request.files = files

        return request
    }
}