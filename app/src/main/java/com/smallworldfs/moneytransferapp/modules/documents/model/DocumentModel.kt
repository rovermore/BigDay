package com.smallworldfs.moneytransferapp.modules.documents.model

import com.google.gson.annotations.SerializedName
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import java.io.Serializable
import java.util.TreeMap

data class DocumentModel(
    var id: String = STRING_EMPTY,

    val type: TreeMap<String, String> = TreeMap(),

    val number: String = STRING_EMPTY,

    @SerializedName("issued_by")
    val issuedBy: TreeMap<String, String> = TreeMap(),

    @SerializedName("expiration_date")
    val expirationDate: String? = STRING_EMPTY,

    @SerializedName("issue_country")
    val issueCountry: TreeMap<String, String>? = TreeMap(),

    @SerializedName("issue_date")
    val issueDate: String = STRING_EMPTY,

    @SerializedName("updated_at")
    val updatedAt: String = STRING_EMPTY
) : Serializable {

    // Old constructor compatibility
    constructor(document: Document) : this(
        if (document.id != null) document.id else STRING_EMPTY,
        if (document.type != null) document.type else TreeMap(),
        if (document.number != null) document.number else STRING_EMPTY,
        if (document.issuedBy != null) document.issuedBy else TreeMap(),
        if (document.expirationDate != null) document.expirationDate else STRING_EMPTY,
        if (document.issueCountry != null) document.issueCountry else TreeMap(),
        if (document.issueDate != null) document.issueDate else STRING_EMPTY,
        if (document.updatedAt != null) document.updatedAt else STRING_EMPTY
    )
}
