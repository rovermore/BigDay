package com.smallworldfs.moneytransferapp.data.mtn

import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.data.mtn.model.MTNRequest
import com.smallworldfs.moneytransferapp.data.mtn.model.MtnStatusDTOMapper
import com.smallworldfs.moneytransferapp.data.mtn.network.TransactionTrackingNetworkDatasource
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.mtn.repository.MTNRepository
import com.smallworldfs.moneytransferapp.mocks.dto.MtnStatusDTOMock
import com.smallworldfs.moneytransferapp.mocks.response.TransactionTrackingResponseMock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class MTNRepositoryTest {

    @Mock
    lateinit var transactionTrackingNetworkDatasource: TransactionTrackingNetworkDatasource

    @Mock
    lateinit var apiErrorMapper: APIErrorMapper

    @Mock
    lateinit var mtnStatusDTOMapper: MtnStatusDTOMapper

    lateinit var mtnRepository: MTNRepository

    private val unmappedApiError = APIError.UnmappedError(432, "Unmapped error")
    private val unmappedDomainError = Error.Unmapped("Unmapped error")

    private val mtnStatusDTO = MtnStatusDTOMock.mtnStatusDTO
    private val mtn = "mtn"
    private val country = "country"
    private val transactionTrackingResponse = TransactionTrackingResponseMock.transactionTrackingResponse

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        mtnRepository = MTNRepositoryImpl(
            transactionTrackingNetworkDatasource,
            apiErrorMapper,
            mtnStatusDTOMapper
        )
    }

    @Test
    fun `when getMtnStatus success transactionTrackingNetworkDatasource trackTransaction is called`() {
        Mockito.`when`(transactionTrackingNetworkDatasource.trackTransaction(MTNRequest(country, mtn)))
            .thenReturn(Success(transactionTrackingResponse))
        mtnRepository.getMtnStatus(mtn, country)
        Mockito.verify(transactionTrackingNetworkDatasource, Mockito.times(1))
            .trackTransaction(MTNRequest(country, mtn))
    }

    @Test
    fun `when getMtnStatus success transactionTrackingNetworkDatasource trackTransaction returns success`() {
        Mockito.`when`(mtnStatusDTOMapper.map(transactionTrackingResponse))
            .thenReturn(Success(mtnStatusDTO))
        Mockito.`when`(transactionTrackingNetworkDatasource.trackTransaction(MTNRequest(country, mtn)))
            .thenReturn(Success(transactionTrackingResponse))
        val result = mtnRepository.getMtnStatus(mtn, country)
        Assert.assertEquals(Success(mtnStatusDTO), result)
    }

    @Test
    fun `when getMtnStatus failure transactionTrackingNetworkDatasource trackTransaction returns error`() {
        Mockito.`when`(apiErrorMapper.map(unmappedApiError))
            .thenReturn(unmappedDomainError)
        Mockito.`when`(transactionTrackingNetworkDatasource.trackTransaction(MTNRequest(country, mtn)))
            .thenReturn(Failure(unmappedApiError))

        val result = mtnRepository.getMtnStatus(mtn, country)
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }
}
