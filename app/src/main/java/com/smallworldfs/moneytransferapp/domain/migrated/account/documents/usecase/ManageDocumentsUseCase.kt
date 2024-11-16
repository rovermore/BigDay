package com.smallworldfs.moneytransferapp.domain.migrated.account.documents.usecase

import com.smallworldfs.moneytransferapp.data.common.locale.LocaleRepository
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.ComplianceDocDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.ComplianceType
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.DocumentDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.DocumentFileDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.DocumentStatusDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.TypesOfDocumentsDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.repository.DocumentsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.concat
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import java.io.File
import javax.inject.Inject

class ManageDocumentsUseCase @Inject constructor(
    private val documentsRepository: DocumentsRepository,
    private val localeRepository: LocaleRepository,
    private val userDataRepository: UserDataRepository,
) {

    fun getDocuments(): OperationResult<List<DocumentDTO>, Error> {
        return userDataRepository.getLoggedUser()
            .map {
                return documentsRepository.getDocuments(
                    localeRepository.getLang(),
                    it.userToken,
                    it.uuid
                )
            }
    }

    fun getMandatoryDocuments(): OperationResult<List<DocumentDTO>, Error> {
        return userDataRepository.getLoggedUser()
            .map { user ->
                val documentRequests = listOf(
                    documentsRepository.getDocuments(
                        localeRepository.getLang(),
                        user.userToken,
                        user.uuid,
                        listOf("transactionCompliance"),
                        listOf("TAX_CODE_DOCUMENT", "ID_MISSING_OR_EXPIRED", "FACE_VERIFICATION"),
                        listOf("MISSING", "UNDER_REVIEW"),
                    ),
                    documentsRepository.getDocuments(
                        localeRepository.getLang(),
                        user.userToken,
                        user.uuid,
                        listOf("userId"),
                        emptyList(),
                        listOf("MISSING", "UNDER_REVIEW")
                    ),
                )
                return documentRequests.concat().map {
                    it.flatten().toMutableList().let { documentsReceived ->
                        val idMissingOrExpiredDoc = documentsReceived.firstOrNull {
                            it is ComplianceDocDTO && it.type == ComplianceType.ID_MISSING_OR_EXPIRED
                        }
                        val nonFaceToFaceDoc = documentsReceived.firstOrNull {
                            it is ComplianceDocDTO && it.type == ComplianceType.FACE_VERIFICATION
                        }
                        if ((idMissingOrExpiredDoc == null || idMissingOrExpiredDoc.status.id.toString() != DocumentStatusDTO.MISSING.toString()) &&
                            nonFaceToFaceDoc != null
                        )
                            documentsReceived.remove(nonFaceToFaceDoc)

                        return Success(documentsReceived)
                    }
                }
            }
    }

    fun getDocumentById(documentId: String): OperationResult<DocumentDTO, Error> {
        return userDataRepository.getLoggedUser()
            .map {
                return documentsRepository.getDocumentById(
                    localeRepository.getLang(),
                    it.userToken,
                    it.uuid,
                    documentId
                )
            }
    }

    fun saveDocument(documentFileDTO: DocumentFileDTO): OperationResult<Boolean, Error> {
        return userDataRepository.getLoggedUser()
            .map {
                return documentsRepository.saveDocument(
                    localeRepository.getLang(),
                    it.userToken,
                    it.uuid,
                    documentFileDTO
                )
            }
    }

    fun getAttachment(attachmentId: String, fileName: String): OperationResult<File, Error> {
        return userDataRepository.getLoggedUser()
            .map {
                return documentsRepository.getAttachment(
                    localeRepository.getLang(),
                    attachmentId,
                    fileName,
                    it.userToken,
                    it.uuid
                )
            }
    }

    fun getDocumentTypes(): OperationResult<List<TypesOfDocumentsDTO>, Error> {
        return userDataRepository.getLoggedUser()
            .map { user ->
                return documentsRepository.getDocumentTypes(
                    localeRepository.getLang(),
                    user.country.countries.first().iso3,
                )
            }
    }
}
