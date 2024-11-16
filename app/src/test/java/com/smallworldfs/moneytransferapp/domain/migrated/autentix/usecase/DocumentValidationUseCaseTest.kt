package com.smallworldfs.moneytransferapp.domain.migrated.autentix.usecase

import com.smallworldfs.moneytransferapp.data.autentix.model.AutentixSessionStatus
import com.smallworldfs.moneytransferapp.data.autentix.model.CheckSessionStatusParams
import com.smallworldfs.moneytransferapp.data.common.locale.LocaleRepository
import com.smallworldfs.moneytransferapp.domain.migrated.autentix.repository.AutentixRepository
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Retryer
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.mocks.dto.AutentixSessionDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.AutentixSessionStatusDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class DocumentValidationUseCaseTest {

    @Mock
    lateinit var documentRepository: AutentixRepository

    @Mock
    lateinit var userDataRepository: UserDataRepository

    @Mock
    lateinit var localeRepository: LocaleRepository

    @Mock
    lateinit var retryer: Retryer

    private lateinit var documentValidationUseCase: DocumentValidationUseCase

    private val user = UserDTOMock.userDTO
    private val successUser = Success(user)
    private val error = Failure(Error.UncompletedOperation("Uncompleted operation"))
    private val autentixSessionDTO = AutentixSessionDTOMock.autentixSessionDTO
    private val documentType = "PASSPORT"
    private val lang = "es"
    private val params = CheckSessionStatusParams(user.uuid, "externalId", lang, user.userToken)
    private val finishedSessionStatus = AutentixSessionStatusDTOMock.finishedSessionStatus

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        documentValidationUseCase = DocumentValidationUseCase(
            documentRepository,
            localeRepository,
            userDataRepository,
            retryer
        )
    }

    @Test
    fun `when getAutentixSessionURL success documentRepository getAutentixSessionURL is called`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(successUser)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            documentRepository.getAutentixSessionURL(
                user.uuid,
                user.userToken,
                lang,
                true,
                documentType
            )
        ).thenReturn(Success(autentixSessionDTO))
        documentValidationUseCase.getAutentixSessionURL(true, documentType)
        Mockito.verify(documentRepository, Mockito.times(1))
            .getAutentixSessionURL(
                user.uuid,
                user.userToken,
                lang,
                true,
                documentType
            )
    }

    @Test
    fun `when getAutentixSessionURL success documentRepository getAutentixSessionURL returns success`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(successUser)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            documentRepository.getAutentixSessionURL(
                user.uuid,
                user.userToken,
                lang,
                true,
                documentType
            )
        ).thenReturn(Success(autentixSessionDTO))
        val result = documentValidationUseCase.getAutentixSessionURL(true, documentType)
        Assert.assertEquals(Success(autentixSessionDTO), result)
    }

    @Test
    fun `when getAutentixSessionURL failure documentRepository getAutentixSessionURL returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(successUser)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            documentRepository.getAutentixSessionURL(
                user.uuid,
                user.userToken,
                lang,
                true,
                documentType
            )
        ).thenReturn(error)
        val result = documentValidationUseCase.getAutentixSessionURL(true, documentType)
        Assert.assertEquals(error, result)
    }

    @Test
    fun `when checkAutentixSessionStatus success documentRepository checkAutentixSessionStatus is called`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(successUser)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(documentRepository.checkAutentixSessionStatus(params))
            .thenReturn(Success(AutentixSessionStatus.FINISHED))
        documentValidationUseCase.checkAutentixSessionStatus("externalId", 20000L)
        Mockito.verify(retryer, Mockito.times(1))
            .retry(params, documentRepository::checkAutentixSessionStatus, 20000L, 3)
    }

    @Test
    fun `when checkAutentixSessionStatus success documentRepository checkAutentixSessionStatus returns success`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(successUser)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(documentRepository.checkAutentixSessionStatus(params))
            .thenReturn(Success(AutentixSessionStatus.FINISHED))
        Mockito.`when`(retryer.retry(params, documentRepository::checkAutentixSessionStatus, 20000L, 3))
            .thenReturn(Success(AutentixSessionStatus.FINISHED))
        val result = documentValidationUseCase.checkAutentixSessionStatus("externalId", 20000L)
        Assert.assertEquals(Success(finishedSessionStatus), result)
    }

    @Test
    fun `when checkAutentixSessionStatus failure documentRepository checkAutentixSessionStatus returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(successUser)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(documentRepository.checkAutentixSessionStatus(params))
            .thenReturn(error)
        Mockito.`when`(retryer.retry(params, documentRepository::checkAutentixSessionStatus, 20000L, 3))
            .thenReturn(error)
        val result = documentValidationUseCase.checkAutentixSessionStatus("externalId", 20000L)
        Assert.assertEquals(error, result)
    }
}
