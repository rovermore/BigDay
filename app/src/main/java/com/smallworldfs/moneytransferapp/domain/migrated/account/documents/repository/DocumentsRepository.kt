package com.smallworldfs.moneytransferapp.domain.migrated.account.documents.repository

import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.DocumentDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.DocumentFileDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.TypesOfDocumentsDTO
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import java.io.File

interface DocumentsRepository {
    fun getDocumentTypes(lang: String, country: String): OperationResult<List<TypesOfDocumentsDTO>, Error>
    fun getDocuments(lang: String, userToken: String, uuid: String, type: List<String> = emptyList(), subtype: List<String> = emptyList(), status: List<String> = emptyList()): OperationResult<List<DocumentDTO>, Error>
    fun getDocumentById(lang: String, userToken: String, uuid: String, documentId: String): OperationResult<DocumentDTO, Error>
    fun saveDocument(lang: String, userToken: String, uuid: String, documentDTO: DocumentFileDTO): OperationResult<Boolean, Error>
    fun getAttachment(lang: String, id: String, fileName: String, userToken: String, uuid: String): OperationResult<File, Error>
    fun getDocumentForm(lang: String, uid: String, userToken: String, uuid: String, documentType: String): OperationResult<ArrayList<Field>, Error>
}
