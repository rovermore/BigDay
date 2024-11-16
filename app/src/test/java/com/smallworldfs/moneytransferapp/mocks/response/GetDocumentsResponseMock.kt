package com.smallworldfs.moneytransferapp.mocks.response

import com.smallworldfs.moneytransferapp.data.account.documents.model.Document
import com.smallworldfs.moneytransferapp.data.account.documents.model.GetDocumentResponse
import com.smallworldfs.moneytransferapp.data.account.documents.model.GetDocumentsResponse

object GetDocumentsResponseMock {

    private val document = Document(
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
        null,
    )

    val getDocumentListResponse = GetDocumentsResponse("msg", listOf(document))
    val getDocumentResponse = GetDocumentResponse("msg", document)
}
