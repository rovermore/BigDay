package com.smallworldfs.moneytransferapp.data.account.documents.model

import okhttp3.MultipartBody
import okhttp3.RequestBody

interface MultipartRequest {
    fun getBody(): Map<String, RequestBody>
    fun getMultiParts(): Map<String, MultipartBody.Part>
}
