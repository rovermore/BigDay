package com.smallworldfs.moneytransferapp.data.account.documents.repository.network

import com.smallworldfs.moneytransferapp.data.account.documents.model.GetDocumentResponse
import com.smallworldfs.moneytransferapp.data.account.documents.model.GetDocumentsResponse
import com.smallworldfs.moneytransferapp.data.account.documents.model.TypesOfDocumentsResponse
import com.smallworldfs.moneytransferapp.data.account.documents.model.UploadDocumentResponse
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.PartMap
import retrofit2.http.Path
import retrofit2.http.Query

interface DocumentService {

    @GET(EndPoint.DOCUMENTS)
    fun requestDocuments(
        @Path("lang") lang: String,
        @Path("uuid") uuid: String,
        @Query("userToken") userToken: String,
        @Query("docType[]") docType: List<String>,
        @Query("subtype[]") subtype: List<String>,
        @Query("status[]") status: List<String>
    ): Call<GetDocumentsResponse>

    @GET(EndPoint.DOCUMENT_BY_ID)
    fun requestDocument(
        @Path("lang") lang: String,
        @Path("Uid") documentId: String,
        @Query("userUuid") uuid: String,
        @Query("userToken") userToken: String,
    ): Call<GetDocumentResponse>

    @JvmSuppressWildcards
    @Multipart
    @POST(EndPoint.DOCUMENT_UPLOAD)
    fun uploadDocument(
        @Path("lang") lang: String,
        @Part front: MultipartBody.Part,
        @Part back: MultipartBody.Part?,
        @PartMap uploadDocumentRequest: Map<String, RequestBody>
    ): Call<UploadDocumentResponse>

    @GET(EndPoint.DOCUMENT_DOWNLOAD_ATTACHMENT)
    fun downloadAttachment(@Path("lang") lang: String, @Path("uid") id: String, @Query("userToken") userToken: String, @Query("uuid") uuid: String): Call<ResponseBody>

    @GET(EndPoint.GET_DOCUMENTS_TYPES)
    fun getTypesOfDocuments(@Path("lang") lang: String, @Query("country") country: String): Call<List<TypesOfDocumentsResponse>>

    @GET(EndPoint.NEW_DOCUMENT_FORM)
    fun requestDocumentForm(@Path("lang") lang: String, @Path("uid") uid: String, @Query("userToken") userToken: String, @Query("uuid") uuid: String, @Query("documentType") documentType: String): Call<ArrayList<Field>>
}
