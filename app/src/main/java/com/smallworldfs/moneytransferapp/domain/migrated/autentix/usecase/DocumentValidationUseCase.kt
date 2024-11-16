package com.smallworldfs.moneytransferapp.domain.migrated.autentix.usecase

import com.smallworldfs.moneytransferapp.data.autentix.model.AutentixSessionStatus
import com.smallworldfs.moneytransferapp.data.autentix.model.CheckSessionStatusParams
import com.smallworldfs.moneytransferapp.data.common.locale.LocaleRepository
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.AutentixSessionDTO
import com.smallworldfs.moneytransferapp.domain.migrated.autentix.repository.AutentixRepository
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Retryer
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import javax.inject.Inject

class DocumentValidationUseCase @Inject constructor(
    private val documentRepository: AutentixRepository,
    private val localeRepository: LocaleRepository,
    private val userDataRepository: UserDataRepository,
    private val retryer: Retryer,
) {

    fun getAutentixSessionURL(faceCompare: Boolean, documentType: String): OperationResult<AutentixSessionDTO, Error> {
        return userDataRepository.getLoggedUser()
            .map {
                return documentRepository.getAutentixSessionURL(it.uuid, it.userToken, localeRepository.getLang(), faceCompare, documentType)
            }
    }

    fun checkAutentixSessionStatus(externalId: String, timeout: Long): OperationResult<AutentixSessionStatus, Error> {
        return userDataRepository.getLoggedUser()
            .map {
                val params = CheckSessionStatusParams(it.uuid, externalId, localeRepository.getLang(), it.userToken)
                return retryer.retry(params, documentRepository::checkAutentixSessionStatus, timeout, 3)
            }
    }
}
