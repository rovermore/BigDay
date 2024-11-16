package com.smallworldfs.moneytransferapp.data.oauth

import com.smallworldfs.moneytransferapp.data.auth.oauth.model.Integrity
import com.smallworldfs.moneytransferapp.data.auth.oauth.model.RequestInfo
import com.smallworldfs.moneytransferapp.data.auth.oauth.model.RequestOAuthTokenDataModel
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.data.oauth.repository.OAuthRepositoryImpl
import com.smallworldfs.moneytransferapp.data.oauth.repository.local.OAuthLocal
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.oauth.repository.OAuthRepository
import com.smallworldfs.moneytransferapp.mocks.dto.IntegrityDTOMock
import com.smallworldfs.moneytransferapp.mocks.response.ResponseOAuthTokenDataModelMock
import com.smallworldfs.moneytransferapp.modules.oauth.domain.service.OAuthNetworkDatasource
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class OAuthRepositoryTest {

    @Mock
    lateinit var oAuthNetwork: OAuthNetworkDatasource

    @Mock
    lateinit var oAuthLocal: OAuthLocal

    @Mock
    lateinit var apiErrorMapper: APIErrorMapper

    lateinit var oAuthRepository: OAuthRepository

    private val unmappedApiError = APIError.UnmappedError(432, "Unmapped error")
    private val unmappedApiErrorFailure = Failure(unmappedApiError)
    private val unmappedDomainError = Error.Unmapped("Unmapped error")

    private val oAuthToken = "oAuthToken"
    private val responseOAuthTokenDataModel = ResponseOAuthTokenDataModelMock.responseOAuthTokenDataModel
    private val integrityDTO = IntegrityDTOMock.integrityDTO

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        oAuthRepository = OAuthRepositoryImpl(
            oAuthNetwork,
            oAuthLocal,
            apiErrorMapper
        )
    }

    @Test
    fun `when requestOAuthTokenAsync success oAuthLocal getPersistedOAuthToken is called`() {
        Mockito.`when`(oAuthLocal.getPersistedOAuthToken()).thenReturn(oAuthToken)
        oAuthRepository.requestOAuthTokenAsync(integrityDTO)
        Mockito.verify(oAuthLocal, Mockito.times(3))
            .getPersistedOAuthToken()
    }

    @Test
    fun `when requestOAuthTokenAsync success oAuthRepository refreshOAuthTokenAsync is called`() {
        Mockito.`when`(oAuthLocal.getPersistedOAuthToken()).thenReturn(STRING_EMPTY)
        Mockito.`when`(
            oAuthNetwork.getSyncAccessToken(
                RequestOAuthTokenDataModel(
                    Integrity(
                        integrityDTO.nonce,
                        RequestInfo(integrityDTO.requestInfo.signature)
                    )
                )
            )
        ).thenReturn(Success(responseOAuthTokenDataModel))
        oAuthRepository.requestOAuthTokenAsync(integrityDTO)
        Mockito.verify(oAuthNetwork, Mockito.times(1))
            .getSyncAccessToken(
                RequestOAuthTokenDataModel(
                    Integrity(
                        integrityDTO.nonce,
                        RequestInfo(integrityDTO.requestInfo.signature)
                    )
                )
            )
    }

    @Test
    fun `when requestOAuthTokenAsync success oAuthLocal getPersistedOAuthToken returns success`() {
        Mockito.`when`(oAuthLocal.getPersistedOAuthToken()).thenReturn(oAuthToken)
        val result = oAuthRepository.requestOAuthTokenAsync(integrityDTO)
        Assert.assertEquals(Success(oAuthToken), result)
    }

    @Test
    fun `when requestOAuthTokenAsync failure oAuthLocal getPersistedOAuthToken returns error`() {
        Mockito.`when`(oAuthLocal.getPersistedOAuthToken()).thenReturn(STRING_EMPTY)
        Mockito.`when`(
            oAuthNetwork.getSyncAccessToken(
                RequestOAuthTokenDataModel(
                    Integrity(
                        integrityDTO.nonce,
                        RequestInfo(integrityDTO.requestInfo.signature)
                    )
                )
            )
        ).thenReturn(Success(responseOAuthTokenDataModel))

        oAuthRepository.requestOAuthTokenAsync(integrityDTO)
        val result = oAuthRepository.requestOAuthTokenAsync(integrityDTO)
        Assert.assertEquals(Success(responseOAuthTokenDataModel.accessToken), result)
    }

    @Test
    fun `when refreshOAuthTokenAsync success oAuthNetwork getSyncAccessToken is called`() {
        Mockito.`when`(oAuthLocal.getPersistedOAuthToken()).thenReturn(STRING_EMPTY)
        Mockito.`when`(
            oAuthNetwork.getSyncAccessToken(
                RequestOAuthTokenDataModel(
                    Integrity(
                        integrityDTO.nonce,
                        RequestInfo(integrityDTO.requestInfo.signature)
                    )
                )
            )
        ).thenReturn(Success(responseOAuthTokenDataModel))
        oAuthRepository.refreshOAuthTokenAsync(integrityDTO)
        Mockito.verify(oAuthLocal, Mockito.times(1))
            .persistOAuthToken(responseOAuthTokenDataModel.getOAuthToken())
    }

    @Test
    fun `when refreshOAuthTokenAsync success oAuthNetwork getSyncAccessToken returns success`() {
        Mockito.`when`(oAuthLocal.getPersistedOAuthToken()).thenReturn(STRING_EMPTY)
        Mockito.`when`(
            oAuthNetwork.getSyncAccessToken(
                RequestOAuthTokenDataModel(
                    Integrity(
                        integrityDTO.nonce,
                        RequestInfo(integrityDTO.requestInfo.signature)
                    )
                )
            )
        ).thenReturn(Success(responseOAuthTokenDataModel))
        val result = oAuthRepository.refreshOAuthTokenAsync(integrityDTO)
        Assert.assertEquals(Success(responseOAuthTokenDataModel.accessToken), result)
    }

    @Test
    fun `when refreshOAuthTokenAsync success oAuthNetwork getSyncAccessToken returns error`() {
        Mockito.`when`(oAuthLocal.getPersistedOAuthToken()).thenReturn(STRING_EMPTY)
        Mockito.`when`(apiErrorMapper.map(unmappedApiError)).thenReturn(unmappedDomainError)
        Mockito.`when`(
            oAuthNetwork.getSyncAccessToken(
                RequestOAuthTokenDataModel(
                    Integrity(
                        integrityDTO.nonce,
                        RequestInfo(integrityDTO.requestInfo.signature)
                    )
                )
            )
        ).thenReturn(unmappedApiErrorFailure)
        val result = oAuthRepository.refreshOAuthTokenAsync(integrityDTO)
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }
}
