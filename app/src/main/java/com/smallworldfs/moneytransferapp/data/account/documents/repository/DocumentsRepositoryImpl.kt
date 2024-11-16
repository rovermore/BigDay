package com.smallworldfs.moneytransferapp.data.account.documents.repository

import com.smallworldfs.moneytransferapp.data.account.documents.mappers.DocumentsDTOMapper
import com.smallworldfs.moneytransferapp.data.account.documents.mappers.TypeOfDocumentsDTOMapper
import com.smallworldfs.moneytransferapp.data.account.documents.repository.network.DocumentsNetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.FileResolver
import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.DocumentDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.DocumentFileDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.TypesOfDocumentsDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.repository.DocumentsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.utils.EXTENSION_PDF
import java.io.File
import javax.inject.Inject

class DocumentsRepositoryImpl @Inject constructor(
    private val documentsNetworkDatasource: DocumentsNetworkDatasource,
    private val apiErrorMapper: APIErrorMapper,
    private val documentsDTOMapper: DocumentsDTOMapper,
    private val typeOfDocumentsDTOMapper: TypeOfDocumentsDTOMapper,
    private val fileBuilder: FileResolver
) : DocumentsRepository {

    override fun getDocumentTypes(lang: String, country: String): OperationResult<List<TypesOfDocumentsDTO>, Error> {
        return documentsNetworkDatasource.getDocumentTypes(lang, country)
            .map {
                return Success(typeOfDocumentsDTOMapper.map(it))
            }.mapFailure {
                return Failure(apiErrorMapper.map(it))
            }
    }

    override fun getDocuments(lang: String, userToken: String, uuid: String, type: List<String>, subtype: List<String>, status: List<String>): OperationResult<List<DocumentDTO>, Error> {
        return documentsNetworkDatasource.requestDocuments(lang, userToken, uuid, type, subtype, status)
            .map {
                return Success(documentsDTOMapper.mapGetDocumentsResponse(it))
            }.mapFailure {
                return Failure(apiErrorMapper.map(it))
            }
    }

    override fun getDocumentById(lang: String, userToken: String, uuid: String, documentId: String): OperationResult<DocumentDTO, Error> {
        return documentsNetworkDatasource.requestDocumentById(lang, userToken, uuid, documentId)
            .map {
                return Success(documentsDTOMapper.mapDocument(it.document))
            }.mapFailure {
                return Failure(apiErrorMapper.map(it))
            }
    }

    override fun saveDocument(lang: String, userToken: String, uuid: String, documentFileDTO: DocumentFileDTO): OperationResult<Boolean, Error> {
        return documentsNetworkDatasource.uploadDocument(
            lang,
            documentsDTOMapper.mapUploadDocumentsRequest(
                uuid,
                userToken,
                documentFileDTO
            )
        ).map {
            return Success(true)
        }.mapFailure {
            return Failure(apiErrorMapper.map(it))
        }
    }

    override fun getAttachment(lang: String, id: String, fileName: String, userToken: String, uuid: String): OperationResult<File, Error> {
        return documentsNetworkDatasource.downloadAttachment(lang, id, userToken, uuid)
            .map {
                val file = fileBuilder.buildFile(it, fileName, EXTENSION_PDF)
                file?.let {
                    return Success(it)
                }
                    ?: return Failure(Error.UncompletedOperation("Could not build file"))
            }.mapFailure {
                return Failure(apiErrorMapper.map(it))
            }
    }

    override fun getDocumentForm(lang: String, uid: String, userToken: String, uuid: String, documentType: String): OperationResult<ArrayList<Field>, Error> {
        return documentsNetworkDatasource.requestDocumentForm(lang, uid, userToken, uuid, documentType)
            .map { response ->
                response?.let {
                    return Success(it)
                }
                    ?: return Failure(Error.Unmapped("Fields could not be mapped"))
            }.mapFailure {
                return Failure(apiErrorMapper.map(it))
            }
    }
}
