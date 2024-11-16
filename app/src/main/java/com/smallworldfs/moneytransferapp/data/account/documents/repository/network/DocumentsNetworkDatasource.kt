package com.smallworldfs.moneytransferapp.data.account.documents.repository.network

import com.smallworldfs.moneytransferapp.data.account.documents.model.GetDocumentResponse
import com.smallworldfs.moneytransferapp.data.account.documents.model.GetDocumentsResponse
import com.smallworldfs.moneytransferapp.data.account.documents.model.TypesOfDocumentsResponse
import com.smallworldfs.moneytransferapp.data.account.documents.model.UploadDocumentRequest
import com.smallworldfs.moneytransferapp.data.account.documents.model.UploadDocumentRequest.Companion.BACK_PART_PARAM
import com.smallworldfs.moneytransferapp.data.account.documents.model.UploadDocumentRequest.Companion.FRONT_PART_PARAM
import com.smallworldfs.moneytransferapp.data.account.documents.model.UploadDocumentResponse
import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import okhttp3.ResponseBody

class DocumentsNetworkDatasource(
    private val documentService: DocumentService
) : NetworkDatasource() {

    fun getDocumentTypes(lang: String, country: String): OperationResult<List<TypesOfDocumentsResponse>, APIError> {
        return executeCall(documentService.getTypesOfDocuments(lang, country))
    }

    fun requestDocuments(lang: String, userToken: String, uuid: String, docType: List<String>, subtype: List<String>, status: List<String>): OperationResult<GetDocumentsResponse, APIError> {
        return executeCall(documentService.requestDocuments(lang, uuid, userToken, docType, subtype, status))
    }

    fun requestDocumentById(lang: String, userToken: String, uuid: String, documentId: String): OperationResult<GetDocumentResponse, APIError> {
        return executeCall(
            documentService.requestDocument(
                lang,
                documentId,
                uuid,
                userToken
            ),
        )
    }

    fun uploadDocument(lang: String, request: UploadDocumentRequest): OperationResult<UploadDocumentResponse, APIError> {
        val body = request.getBody()
        val parts = request.getMultiParts()
        return executeCall(
            documentService.uploadDocument(
                lang,
                parts[FRONT_PART_PARAM]!!,
                parts[BACK_PART_PARAM],
                body,
            ),
        )
    }

    fun downloadAttachment(lang: String, id: String, userToken: String, uuid: String): OperationResult<ResponseBody, APIError> {
        return executeCall(documentService.downloadAttachment(lang, id, userToken, uuid))
    }

    fun requestDocumentForm(lang: String, uid: String, userToken: String, uuid: String, documentType: String): OperationResult<ArrayList<Field>?, APIError> {
        return executeCall(documentService.requestDocumentForm(lang, uid, userToken, uuid, documentType))
    }
}
