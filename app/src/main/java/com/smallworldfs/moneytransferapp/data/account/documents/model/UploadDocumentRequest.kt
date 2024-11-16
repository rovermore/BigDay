package com.smallworldfs.moneytransferapp.data.account.documents.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.whenNotBlank
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UploadDocumentRequest(
    val uuid: String,
    val userToken: String,
    val document: String,
    val numberDocument: String,
    val country: String,
    val expirationDate: String,
    val type: String,
    val idIssueCountry: String,
    val issueDate: String,
    val complianceType: String,
    val mtn: String,
    val documentType: String,
    val front: String,
    val back: String = STRING_EMPTY,
    val uid: String = STRING_EMPTY,
    val taxCode: String = STRING_EMPTY,
    val userIdType: String = STRING_EMPTY
) : MultipartRequest {

    companion object {
        const val FRONT_PART_PARAM = "front"
        const val BACK_PART_PARAM = "back"
    }

    override fun getBody(): Map<String, RequestBody> {
        return mutableMapOf<String, RequestBody>().apply {
            put("uuid", createPartFromString(uuid))
            put("userToken", createPartFromString(userToken))
            put("document", createPartFromString(document))
            put("numberDocument", createPartFromString(numberDocument))
            put("expirationDate", createPartFromString(expirationDate))
            put("idIssueCountry", createPartFromString(idIssueCountry))
            put("issueDate", createPartFromString(issueDate))
            put("complianceType", createPartFromString(complianceType))
            put("mtn", createPartFromString(mtn))
            put("documentType", createPartFromString(documentType))
            put("uid", createPartFromString(uid))
            put("taxCode", createPartFromString(taxCode))
            put("userIdType", createPartFromString(userIdType))
            put("country", createPartFromString(country))
        }.toMap()
    }

    override fun getMultiParts() = mutableMapOf<String, MultipartBody.Part>().apply {
        val frontFile = File(front)
        put(
            FRONT_PART_PARAM,
            MultipartBody.Part.createFormData(
                FRONT_PART_PARAM,
                frontFile.name,
                frontFile.asRequestBody("image/*".toMediaTypeOrNull())
            )
        )
        back.whenNotBlank {
            val backFile = File(back)
            put(
                BACK_PART_PARAM,
                MultipartBody.Part.createFormData(
                    BACK_PART_PARAM,
                    backFile.name,
                    backFile.asRequestBody("image/*".toMediaTypeOrNull())
                )
            )
        }
    }.toMap()

    private fun createPartFromString(stringData: String): RequestBody {
        return stringData.toRequestBody("text/plain".toMediaTypeOrNull())
    }
}
