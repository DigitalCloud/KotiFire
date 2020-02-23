package com.digitalcloud.kotifireexample.network.api

import androidx.collection.ArrayMap
import com.digitalcloud.kotifire.KotiCachePolicy
import com.digitalcloud.kotifire.KotiMethod
import com.digitalcloud.kotifire.KotiRoute
import com.digitalcloud.kotifire.models.RequestModel
import com.digitalcloud.kotifireexample.BuildConfig
import com.digitalcloud.kotifireexample.network.NetworkHeadersUtil
import java.io.File

sealed class UsersAPIs : KotiRoute {

    class Users() : UsersAPIs()

    override val basePath = BuildConfig.BASE_URL

    override val method: KotiMethod
        get() {
            return when (this) {
                is Users -> KotiMethod.GET
            }
        }

    override val cachingType: KotiCachePolicy
        get() {
            return when (this) {
                is Users -> KotiCachePolicy.CACHE_THEN_NETWORK
            }
        }

    override val path: String
        get() {
            return when (this) {
                is Users -> "/users"
            }
        }

    override val params: RequestModel
        get() {
            return when (this) {
                is Users -> RequestModel()
            }
        }


    override val files: ArrayMap<String, File>
        get() {
            return when (this) {
                is Users -> ArrayMap()
            }
        }

    override val bytes: ByteArray?
        get() {
            return when (this) {
                is Users -> null
            }
        }

    override val body: String
        get() {
            return when (this) {
                is Users -> ""
            }
        }

    override val headers: ArrayMap<String, String>
        get() {
            return NetworkHeadersUtil.headers
        }
}