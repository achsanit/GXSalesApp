package com.achsanit.gxsales.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.File

object FileHelper {

    // create temporary file from uri
    fun createTmpFileFromUri(context: Context, uri: Uri, fileName: String): File? {
        return try {
            val stream = context.contentResolver.openInputStream(uri) // stream the uri
            val bitmap = BitmapFactory.decodeStream(stream) // create bitmap from uri for compress the file

            // return file from bitmap after compress it
            File.createTempFile(fileName, "${System.currentTimeMillis()}.jpg", context.cacheDir)
                .writeFromBitmap(bitmap)
        } catch (e: Exception) {
            null
        }
    }

    // extension func for write file from bitmap
    fun File.writeFromBitmap(
        bitmap: Bitmap, // bitmap input
        format: Bitmap.CompressFormat = Bitmap.CompressFormat.JPEG, // format compressing
        quality: Int = 80 // quality output
    ) = apply {
        // output stream file from bitmap after compress
        outputStream().use {
            bitmap.compress(format, quality, it)
            it.flush()
        }
    }
}