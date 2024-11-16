package com.smallworldfs.moneytransferapp.domain.migrated.encoding

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.utils.Utils
import java.io.BufferedInputStream
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import javax.inject.Inject

class EncodeToBase64UseCase @Inject constructor() {

    fun encodeFileToBase64(file: File): OperationResult<String, Error> {
        // Setup values
        var jpgRedux = 19
        val maxSize = 4 * 1000 * 1000

        // Log in crashlytics data
        FirebaseCrashlytics.getInstance().log("Start encoding, available memory: " + Runtime.getRuntime().freeMemory().toString())

        // Transform to byte array image
        return file2JPG(file)
            .map {
                var byteArrayImage: ByteArray? = it
                while (it.size > maxSize) {
                    FirebaseCrashlytics.getInstance().log("JPGRedux level: $jpgRedux available memory: ${Runtime.getRuntime().freeMemory()}")

                    byteArrayImage = file2JPGRedux(file, maxSize * jpgRedux)
                    jpgRedux -= 2
                }
                Utils.logActionCrashlytics("Finish encoding, available memory: " + Runtime.getRuntime().freeMemory().toString())

                // Encode to Base64
                byteArrayImage?.let {
                    return Success(Base64.encodeToString(it, Base64.DEFAULT))
                } ?: return Failure(Error.UnableToEncodeFile("Could not encode file to base 64"))
            }
    }

    private fun file2JPGRedux(file: File, maxSize: Int): ByteArray? {
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
        return baos.toByteArray()
    }

    private fun file2JPG(file: File): OperationResult<ByteArray, Error> {
        val size = file.length().toInt()
        val fileDecoded = ByteArray(size)
        try {
            val buf = BufferedInputStream(FileInputStream(file))
            buf.read(fileDecoded, 0, fileDecoded.size)
            buf.close()
            return Success(fileDecoded)
        } catch (e: IOException) {
            e.printStackTrace()
        }
        return Failure(Error.UnableToEncodeFile("Could not encode file to base 64"))
    }
}
