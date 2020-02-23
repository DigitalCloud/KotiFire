package com.digitalcloud.kotifireexample.network.api

import androidx.collection.ArrayMap
import com.digitalcloud.kotifire.KotiCachePolicy
import com.digitalcloud.kotifire.KotiMethod
import com.digitalcloud.kotifire.KotiRoute
import com.digitalcloud.kotifire.models.RequestModel
import com.digitalcloud.kotifireexample.BuildConfig
import com.digitalcloud.kotifireexample.network.NetworkHeadersUtil
import java.io.File

sealed class PostsAPIs : KotiRoute {

    class Posts(val requestModel: RequestModel) : PostsAPIs()
    class CreatePost(val requestModel: RequestModel) : PostsAPIs()

    override val basePath = BuildConfig.BASE_URL

    override val method: KotiMethod
        get() {
            return when (this) {
                is Posts -> KotiMethod.GET
                is CreatePost -> KotiMethod.POST
            }
        }

    override val cachingType: KotiCachePolicy
        get() {
            return when (this) {
                is Posts -> KotiCachePolicy.CACHE_THEN_NETWORK
                is CreatePost -> KotiCachePolicy.NETWORK_ONLY
            }
        }

    override val path: String
        get() {
            return when (this) {
                is Posts -> "/posts"
                is CreatePost -> "/posts"
            }
        }

    override val params: RequestModel
        get() {
            return when (this) {
                is Posts -> this.requestModel
                is CreatePost -> this.requestModel
            }
        }


    override val files: ArrayMap<String, File>
        get() {
            return when (this) {
                is Posts -> ArrayMap()
                is CreatePost -> ArrayMap()
            }
        }

    override val bytes: ByteArray?
        get() {
            return when (this) {
                is Posts -> null
                is CreatePost -> null
            }
        }

    override val body: String
        get() {
            return when (this) {
                is Posts -> ""
                is CreatePost -> ""
            }
        }

    override val headers: ArrayMap<String, String>
        get() {
            return NetworkHeadersUtil.headers
        }
}