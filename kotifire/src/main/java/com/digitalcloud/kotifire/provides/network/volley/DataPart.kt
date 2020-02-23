/*
 * Copyright (c) Abdullah Hussein Dce
 * a.hussein@dce.sa
 */

package com.digitalcloud.kotifire.provides.network.volley

import android.graphics.Bitmap
import android.graphics.BitmapFactory

import java.io.*
import kotlin.math.ceil
import kotlin.math.ln
import kotlin.math.max
import kotlin.math.pow

/**
 * Created by Abdullah Hussein on 10/23/2018.
 * for more details : a.hussein@dce.sa
 */
class DataPart(path: String, var type: String?) {

    var fileName: String? = null
    var content: ByteArray? = null

    init {
        val file = decodeFile(path)
        try {
            val bytes = ByteArray(file.length().toInt())
            val bis = BufferedInputStream(FileInputStream(file))
            val dis = DataInputStream(bis)
            dis.readFully(bytes)
            content = bytes
        } catch (ex: Exception) {
            ex.printStackTrace()
        }

        fileName = file.name
    }


    private fun decodeFile(imagePath: String): File {
        val f = File(imagePath)
        try {
            val o = BitmapFactory.Options()
            o.inJustDecodeBounds = true

            var fis = FileInputStream(f)
            BitmapFactory.decodeStream(fis, null, o)
            fis.close()

            val imageMaxSize = 1024
            var scale = 1
            if (o.outHeight > imageMaxSize || o.outWidth > imageMaxSize) {
                scale = 2.0.pow(
                    ceil(
                        ln(
                            imageMaxSize / max(
                                o.outHeight,
                                o.outWidth
                            ).toDouble()
                        ) / ln(0.5)
                    ).toInt().toDouble()
                ).toInt()
            }

            //Decode with inSampleSize
            val o2 = BitmapFactory.Options()
            o2.inSampleSize = scale
            fis = FileInputStream(f)
            val b = BitmapFactory.decodeStream(fis, null, o2)
            fis.close()
            val out = FileOutputStream(f)
            if (b != null) {
                b.compress(Bitmap.CompressFormat.PNG, 100, out)
                b.recycle()
            }
            out.flush()
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return f
    }
}
