package com.smallworldfs.moneytransferapp.presentation.account.documents.list

import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.DocumentModel
import java.io.Serializable

data class DocumentsListState(
    val requiredDocuments: List<DocumentModel> = listOf(),
    val uploadedDocuments: List<DocumentModel> = listOf()
) : Serializable
