/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifireexample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.digitalcloud.kotifire.DataHandler
import com.digitalcloud.kotifire.KotiFireManager
import com.digitalcloud.kotifire.SourceType
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        makeGetRequest()
    }

    private fun makeGetRequest() {
        KotiFireManager(this, Any::class).getCacheThenNetwork("/json", object : DataHandler<Any>() {

            override fun onSuccess(objects: ArrayList<Any>, source: SourceType) {
                handleResponseAsArrayList(objects)
            }

            override fun onSuccess(t: Any, source: SourceType) {
                handleResponseAsObject(t)
            }

            override fun onSuccess(response: String, source: SourceType) {
                textView.text = (response)
            }

            override fun onFail(o: Any, isConnectToInternet: Boolean) {
                handleFail(o)
            }
        })
    }

    private fun handleResponseAsArrayList(objects: ArrayList<Any>) {
    }

    private fun handleResponseAsObject(t: Any) {
    }

    private fun handleFail(o: Any) {
    }
}