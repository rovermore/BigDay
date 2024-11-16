package com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model

import android.os.Bundle
import com.smallworldfs.moneytransferapp.modules.home.domain.model.DocumentButton
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class DocumentModel(
    // Check if is compliance or identification document
    val compliance: Boolean = false,

    // Compliance data
    val changeDate: String? = STRING_EMPTY,
    val clientId: String? = STRING_EMPTY,
    val complianceSubtype: String? = STRING_EMPTY,
    val complianceType: String? = STRING_EMPTY,
    val createdAt: String? = STRING_EMPTY,
    val doc: String? = STRING_EMPTY,
    val id: Int? = INT_ZERO,
    val mtn: String? = STRING_EMPTY,
    val status: DocumentStatus = DocumentStatus.UNDEFINED,
    val statusText: String? = STRING_EMPTY,
    val text: String? = STRING_EMPTY,
    val title: String? = STRING_EMPTY,
    val translateDescription: String? = STRING_EMPTY,
    val translateTitle: String? = STRING_EMPTY,
    val type: String? = STRING_EMPTY,
    val updatedAt: String? = STRING_EMPTY,
    val block: Boolean = false,
    val upload: Boolean = false,
    val buttons: List<DocumentButton>? = null,

    // headers
    val headers: Bundle? = null,

    // Identification data
    val clientIdId: Int? = INT_ZERO,
    val expirationDate: String? = STRING_EMPTY,
    val expirationDateFormat: String? = STRING_EMPTY,
    val issueByTrans: String? = STRING_EMPTY,
    val issueCountry: String? = STRING_EMPTY,
    val issueCountryTrans: String? = STRING_EMPTY,
    val issueDate: String? = STRING_EMPTY,
    val issueDateFormat: String? = STRING_EMPTY,
    val issueState: String? = STRING_EMPTY,
    val issuedBy: String? = STRING_EMPTY,
    val issuedByTrans: String? = STRING_EMPTY,
    val number: String? = STRING_EMPTY,
    val typeTrans: String? = STRING_EMPTY,
    val userOriginalId: String? = STRING_EMPTY,
) {
    enum class DocumentStatus {
        MISSING, EXPIRED, REJECTED, UNDER_REVIEW, APPROVED, UNDEFINED
    }
}
