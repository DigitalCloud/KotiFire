/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifireexample

import android.app.Application

/**
 * Created by Abdullah Hussein on 11/12/2018.
 * for more details : a.hussein@dce.sa
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()
        KotiFire.initialize(this,
            BuildConfig.BASE_URL,
            NetworkHeadersUtil.headers
        )
    }
}