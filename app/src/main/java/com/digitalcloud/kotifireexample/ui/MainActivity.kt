/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifireexample.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.digitalcloud.kotifire.*
import com.digitalcloud.kotifire.provides.network.volley.StatusCodeEvent
import com.digitalcloud.kotifireexample.R
import com.digitalcloud.kotifireexample.network.Post
import com.digitalcloud.kotifireexample.network.api.PostsAPIs
import com.digitalcloud.kotifireexample.network.api.UsersAPIs
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        makePostRequest()
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

        val mDataHandler = object : DataHandler<String>() {
            override fun onSuccess(objects: ArrayList<String>, source: SourceType) {
            }

            override fun onSuccess(t: String, source: SourceType) {
                handleResponseAsObject(t)
            }

            override fun onFail(o: Any, isConnectToInternet: Boolean) {
                handleFail(o)
            }
        }

        KotiFireProvider(UsersAPIs.Users().getKotiRequest(String::class, mDataHandler)).execute()
    }

    private fun makePostRequest() {

        val mDataHandler = object : DataHandler<String>() {
            override fun onSuccess(objects: ArrayList<String>, source: SourceType) {
            }

            override fun onSuccess(t: String, source: SourceType) {
                handleResponseAsObject(t)
            }

            override fun onFail(o: Any, isConnectToInternet: Boolean) {
                handleFail(o)
            }
        }

        KotiFireProvider(PostsAPIs.CreatePost(Post()).getKotiRequest(String::class, mDataHandler)).execute()
    }

    private fun handleResponseAsObject(t: String) {
        textView.text = t
    }

    private fun handleFail(o: Any) {
        textView.text = DataHandler.getNetworkErrorMessage(o)
    }

    @Subscribe
    fun onMessageEvent(event: StatusCodeEvent) {
        Log.e("Error", "Status Code : " + event.statusCode)
    }
}