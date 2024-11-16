package com.smallworldfs.moneytransferapp.data.account.documents.model

import com.google.gson.annotations.SerializedName

data class GetDocumentsResponse(
    val msg: String,
    val documents: List<Document?>?
)

data class Document(
    val documentType: String?,
    val uid: String?,
    val client_id: Int?,
    val type: String?,
    val subtype: String?,
    val status: String?,
    @SerializedName("change_date")
    val changeDate: String?,
    @SerializedName("created_at")
    val createdAt: String?,
    @SerializedName("updated_at")
    val updatedAt: String?,
    val number: String?,
    @SerializedName("issued_by")
    val issuedBy: String?,
    @SerializedName("issue_country")
    val issueCountry: String?,
    @SerializedName("expiration_date")
    val expirationDate: String?,
    val mtn: Long?,
    val solved: Int?,
    @SerializedName("client_type")
    val clientType: String?,
    val block: Boolean?,
    val upload: Boolean?,
    val current: String?,
    val doc: String?,
    val locale: Locale?
)

data class Locale(
    val type: String?,
    val status: String?,
    @SerializedName("issue_country")
    val issueCountry: String?,
    @SerializedName("issued_by")
    val issuedBy: String?,
    @SerializedName("expiration_date")
    val expirationDate: String?,
    val title: String?,
    val description: String?
)
