package com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class DocumentFileDTO(
    val documentType: String = STRING_EMPTY,
    val numberDocument: String = STRING_EMPTY,
    val country: String = STRING_EMPTY,
    val expirationDate: Long = 0L,
    val type: String = STRING_EMPTY,
    val idIssueCountry: String = STRING_EMPTY,
    val front: String = STRING_EMPTY,
    val back: String = STRING_EMPTY,
    val issueDate: Long = 0L,
    val complianceType: String = STRING_EMPTY,
    val mtn: String = STRING_EMPTY,
    val document: String = STRING_EMPTY,
    val uid: String = STRING_EMPTY,
    val taxCode: String = STRING_EMPTY,
    val userIdType: String = STRING_EMPTY
)
