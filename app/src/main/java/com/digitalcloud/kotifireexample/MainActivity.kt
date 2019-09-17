/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifireexample

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        makeGetRequest()
    }

    public override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    public override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    private fun makeGetRequest() {
        val request = KotiRequest(String::class)
        request.endpoint = "/users"
        request.method = KotiMethod.GET
        request.cachingType = KotiCachePolicy.CACHE_THEN_NETWORK
        request.mDataHandler = object : DataHandler<String>() {
            override fun onSuccess(objects: ArrayList<String>, source: SourceType) {
            }

            override fun onSuccess(t: String, source: SourceType) {
                handleResponseAsObject(t)
            }

            override fun onFail(o: Any, isConnectToInternet: Boolean) {
                handleFail(o)
            }
        }

        KotiFireProvider(this, request).execute()
    }

    private fun makePostRequest() {

        val request = KotiRequest(String::class)
        request.endpoint = "/users"
        request.method = KotiMethod.GET
        request.mDataHandler = object : DataHandler<String>() {
            override fun onSuccess(objects: ArrayList<String>, source: SourceType) {
            }

            override fun onSuccess(t: String, source: SourceType) {
                handleResponseAsObject(t)
            }

            override fun onFail(o: Any, isConnectToInternet: Boolean) {
                handleFail(o)
            }
        }

        KotiFireProvider(this, request).execute()
    }

    private fun handleResponseAsObject(t: String) {
        textView.text = t
    }

    private fun handleFail(o: Any) {
        textView.text = DataHandler.getNetworkErrorMessage(o)
    }

    @Subscribe()
    fun onMessageEvent(event: StatusCodeEvent) {
        Log.e("Error", "Status Code : " + event.statusCode)
    };
}