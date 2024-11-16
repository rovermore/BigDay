package com.smallworldfs.moneytransferapp.data.autentix

import com.smallworldfs.moneytransferapp.data.autentix.local.AutentixDocumentsNetworkDatasource
import com.smallworldfs.moneytransferapp.data.autentix.model.CheckSessionStatusParams
import com.smallworldfs.moneytransferapp.data.autentix.repository.AutentixRepositoryImpl
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.domain.migrated.autentix.repository.AutentixRepository
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.mocks.dto.AutentixSessionDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.AutentixSessionStatusDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import com.smallworldfs.moneytransferapp.mocks.response.AutentixSessionStatusResponseMock
import com.smallworldfs.moneytransferapp.mocks.response.CreateAutentixSessionResponseMock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class AutentixRepositoryTest {

    @Mock
    lateinit var autentixDocumentsNetworkDatasource: AutentixDocumentsNetworkDatasource

    @Mock
    lateinit var apiErrorMapper: APIErrorMapper

    lateinit var autentixRepository: AutentixRepository

    private val user = UserDTOMock.userDTO
    private val lang = "es-es"
    private val documentType = "documentType"
    private val faceCompare = "faceCompare"
    private val createAutentixSessionResponse = CreateAutentixSessionResponseMock.createAutentixSessionResponse
    private val autentixSessionStatusResponse = AutentixSessionStatusResponseMock.autentixSessionStatusResponse
    private val autentixSessionStatusFinishedResponse = AutentixSessionStatusResponseMock.autentixSessionStatusFinishedResponse
    private val autentixSessionDTO = AutentixSessionDTOMock.autentixSessionDTO
    private val pendingSessionStatus = AutentixSessionStatusDTOMock.pendingSessionStatus
    private val finishedSessionStatus = AutentixSessionStatusDTOMock.finishedSessionStatus
    private val unmappedApiError = APIError.UnmappedError(432, "Unmapped error")
    private val validationPendingError = Error.ValidationPending("")
    private val externalId = "externalId"
    private val checkSessionStatusParams = CheckSessionStatusParams(user.uuid, externalId, lang, user.userToken)

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        autentixRepository = AutentixRepositoryImpl(
            autentixDocumentsNetworkDatasource,
            apiErrorMapper
        )
    }

    @Test
    fun `when getAutentixSessionURL success autentixDocumentsNetworkDatasource createAutentixSession is called`() {
        Mockito.`when`(
            autentixDocumentsNetworkDatasource.createAutentixSession(
                user.uuid,
                user.userToken,
                lang,
                documentType,
                faceCompare
            )
        ).thenReturn(Success(createAutentixSessionResponse))
        autentixRepository.getAutentixSessionURL(
            user.uuid,
            user.userToken,
            lang,
            true,
            documentType
        )
        Mockito.verify(autentixDocumentsNetworkDatasource, Mockito.times(1))
            .createAutentixSession(
                user.uuid,
                user.userToken,
                lang,
                documentType,
                faceCompare
            )
    }

    @Test
    fun `when getAutentixSessionURL success autentixDocumentsNetworkDatasource createAutentixSession returns success`() {
        Mockito.`when`(
            autentixDocumentsNetworkDatasource.createAutentixSession(
                user.uuid,
                user.userToken,
                lang,
                documentType,
                faceCompare
            )
        ).thenReturn(Success(createAutentixSessionResponse))
        val result = autentixRepository.getAutentixSessionURL(
            user.uuid,
            user.userToken,
            lang,
            true,
            documentType
        )
        Assert.assertEquals(Success(autentixSessionDTO).javaClass, result.javaClass)
    }

    @Test
    fun `when getAutentixSessionURL fails autentixDocumentsNetworkDatasource createAutentixSession returns failure`() {
        Mockito.`when`(
            autentixDocumentsNetworkDatasource.createAutentixSession(
                user.uuid,
                user.userToken,
                lang,
                documentType,
                faceCompare
            )
        ).thenReturn(Failure(unmappedApiError))
        Mockito.`when`(apiErrorMapper.map(unmappedApiError))
            .thenReturn(validationPendingError)
        val result = autentixRepository.getAutentixSessionURL(
            user.uuid,
            user.userToken,
            lang,
            true,
            documentType
        )
        Assert.assertEquals(Failure(validationPendingError), result)
    }

    @Test
    fun `when checkAutentixSessionStatus success autentixDocumentsNetworkDatasource checkAutentixSessionsStatus is called`() {
        Mockito.`when`(
            autentixDocumentsNetworkDatasource.checkAutentixSessionsStatus(
                user.uuid,
                externalId,
                lang,
                user.userToken
            )
        ).thenReturn(Success(autentixSessionStatusResponse))
        autentixRepository.checkAutentixSessionStatus(checkSessionStatusParams)
        Mockito.verify(autentixDocumentsNetworkDatasource, Mockito.times(1))
            .checkAutentixSessionsStatus(
                user.uuid,
                externalId,
                lang,
                user.userToken
            )
    }

    @Test
    fun `when checkAutentixSessionStatus success autentixDocumentsNetworkDatasource checkAutentixSessionsStatus returns success`() {
        Mockito.`when`(
            autentixDocumentsNetworkDatasource.checkAutentixSessionsStatus(
                user.uuid,
                externalId,
                lang,
                user.userToken
            )
        ).thenReturn(Success(autentixSessionStatusFinishedResponse))
        val result = autentixRepository.checkAutentixSessionStatus(checkSessionStatusParams)
        Assert.assertEquals(Success(finishedSessionStatus), result)
    }

    @Test
    fun `when checkAutentixSessionStatus fails autentixDocumentsNetworkDatasource checkAutentixSessionsStatus returns failure`() {
        Mockito.`when`(
            autentixDocumentsNetworkDatasource.checkAutentixSessionsStatus(
                user.uuid,
                externalId,
                lang,
                user.userToken
            )
        ).thenReturn(Failure(unmappedApiError))
        Mockito.`when`(apiErrorMapper.map(unmappedApiError))
            .thenReturn(validationPendingError)
        val result = autentixRepository.checkAutentixSessionStatus(checkSessionStatusParams)
        Assert.assertEquals(Failure(validationPendingError).javaClass, result.javaClass)
    }
}
