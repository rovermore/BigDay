package com.smallworldfs.moneytransferapp.data.status.mappers

import com.smallworldfs.moneytransferapp.utils.Constants
import okhttp3.ResponseBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import javax.inject.Inject
import javax.inject.Named

class ReceiptPDFMapper @Inject constructor(
    @Named("ExternalFilesPath") private val path: String
) {

    fun map(body: ResponseBody?): File? {
        return if (body == null) {
            null
        } else try {
            val pdfReceipt = File(path + File.separator + Constants.CONFIGURATION.RECEIPTS_NAME)
            var inputStream: InputStream? = null
            var outputStream: OutputStream? = null
            try {
                val fileReader = ByteArray(4096)
                inputStream = body.byteStream()
                outputStream = FileOutputStream(pdfReceipt)
                while (true) {
                    val read = inputStream.read(fileReader)
                    if (read == -1) {
                        break
                    }
                    outputStream.write(fileReader, 0, read)
                }
                outputStream.flush()
                pdfReceipt
            } catch (e: Exception) {
                null
            } finally {
                inputStream?.close()
                outputStream?.close()
            }
        } catch (e: Exception) {
            null
        }
    }
}
