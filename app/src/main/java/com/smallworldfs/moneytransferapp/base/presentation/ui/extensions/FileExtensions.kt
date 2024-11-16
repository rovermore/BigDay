package com.smallworldfs.moneytransferapp.base.presentation.ui.extensions

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.webkit.MimeTypeMap
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.smallworldfs.moneytransferapp.utils.Utils
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException

/**
 * Get mimeType from file
 */
fun File.getMimeType(): String {
    // Get the extension of the file
    val extension = MimeTypeMap.getFileExtensionFromUrl(this.toString())

    // Get the mimeType
    var type: String? = null
    if (extension != null) {
        type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension.toLowerCase())
    }

    // If does not found the type set to generic image type
    if (type == null) {
        type = "image/*" // fallback type. You might set it to */*
    }

    // Return it
    return type
}

/**
 * Encode to base 64
 */
fun File.encodeFileToBase64(): String? {
    // Setup values
    var jpgRedux = 19
    val maxSize = 4 * 1000 * 1000

    // Log in crashlytics data
    FirebaseCrashlytics.getInstance().log("Start encoding, available memory: " + Runtime.getRuntime().freeMemory().toString())

    // Transform to byte array image
    var byteArrayImage = file2JPG(this)

    // Decrease size
    byteArrayImage?.let {
        while (it.size > maxSize) {
            FirebaseCrashlytics.getInstance().log("JPGRedux level: $jpgRedux available memory: ${Runtime.getRuntime().freeMemory()}")

            byteArrayImage = file2JPGRedux(this, maxSize * jpgRedux)
            jpgRedux -= 2
        }
        Utils.logActionCrashlytics("Finish encoding, available memory: " + Runtime.getRuntime().freeMemory().toString())

        // Encode to Base64
        return Base64.encodeToString(byteArrayImage, Base64.DEFAULT)
    }
    return null
}

private fun file2JPGRedux(file: File, maxSize: Int): ByteArray? {
    System.gc()
    var bm = BitmapFactory.decodeFile(file.path)
    val reduxRate = 1f * maxSize / bm.byteCount
    if (reduxRate < 1) {
        Utils.logActionCrashlytics(
            "Redux rate: " + reduxRate.toString() + ", available memory: " +
                Runtime.getRuntime().freeMemory().toString()
        )
        val bm2 = Bitmap.createScaledBitmap(bm, (bm.width * reduxRate).toInt(), (bm.height * reduxRate).toInt(), true)
        bm.recycle()
        bm = bm2
    }
    val baos = ByteArrayOutputStream()
    bm.compress(Bitmap.CompressFormat.JPEG, 99, baos)
    bm.recycle()
    System.gc()
    return baos.toByteArray()
}

private fun file2JPG(file: File): ByteArray? {
    val size = file.length().toInt()
    val fileDecoded = ByteArray(size)
    try {
        val buf = BufferedInputStream(FileInputStream(file))
        buf.read(fileDecoded, 0, fileDecoded.size)
        buf.close()
        return fileDecoded
    } catch (e: IOException) {
        e.printStackTrace()
    }
    return null
}
