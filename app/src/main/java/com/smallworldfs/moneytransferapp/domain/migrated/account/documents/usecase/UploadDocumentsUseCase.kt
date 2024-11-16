package com.smallworldfs.moneytransferapp.domain.migrated.account.documents.usecase

import com.smallworldfs.moneytransferapp.data.common.locale.LocaleRepository
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.DocumentFileDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.repository.DocumentsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.capabilities.CapabilityChecker
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import javax.inject.Inject

class UploadDocumentsUseCase @Inject constructor(
    private val documentsRepository: DocumentsRepository,
    private val userDataRepository: UserDataRepository,
    private val localeRepository: LocaleRepository,
    private val capabilityChecker: CapabilityChecker
) {

    fun saveDocument(documentFileDTO: DocumentFileDTO): OperationResult<Boolean, Error> {
        return userDataRepository.getLoggedUser()
            .map {
                return documentsRepository.saveDocument(
                    localeRepository.getLang(),
                    it.userToken,
                    it.uuid,
                    documentFileDTO.copy(country = it.country.countries.first().iso3)
                )
            }
    }

    fun getDocumentForm(documentId: String, documentType: String): OperationResult<ArrayList<Field>, Error> {
        return userDataRepository.getLoggedUser()
            .map {
                return documentsRepository.getDocumentForm(
                    localeRepository.getLang(),
                    documentId,
                    it.userToken,
                    it.uuid,
                    documentType
                )
            }
    }

    fun checkReadWritePermissions() = capabilityChecker.askForReadWritePermissions()
}
