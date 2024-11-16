package com.smallworldfs.moneytransferapp.data.transactions

import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.data.transactions.network.TransactionsNetworkDatasource
import com.smallworldfs.moneytransferapp.data.transactions.repository.MyActivityMapper
import com.smallworldfs.moneytransferapp.data.transactions.repository.TransactionsRepositoryImpl
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.get
import com.smallworldfs.moneytransferapp.domain.migrated.transactions.repository.TransactionsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.mocks.dto.TransactionsHistoryDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import com.smallworldfs.moneytransferapp.mocks.response.MyActivityResponseMock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class TransactionsRepositoryTest {

    @Mock
    lateinit var transactionsNetworkDatasource: TransactionsNetworkDatasource

    @Mock
    lateinit var userDataRepository: UserDataRepository

    @Mock
    lateinit var myActivityMapper: MyActivityMapper

    @Mock
    lateinit var apiErrorMapper: APIErrorMapper

    lateinit var transactionsRepository: TransactionsRepository

    private val unmappedApiError = APIError.UnmappedError(432, "Unmapped error")
    private val unmappedDomainError = Error.Unmapped("Unmapped error")

    private val userDTOSuccess = Success(UserDTOMock.userDTO)

    private val myActivityResponse = MyActivityResponseMock.myActivityResponse
    private val transactionsHistoryDTO = TransactionsHistoryDTOMock.transactionsHistoryDTO

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        transactionsRepository = TransactionsRepositoryImpl(
            transactionsNetworkDatasource,
            myActivityMapper,
            userDataRepository,
            apiErrorMapper
        )
    }

    @Test
    fun `when getUserTransactions success transactionsNetworkDatasource requestUserTransactions is called`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(myActivityMapper.map(myActivityResponse)).thenReturn(transactionsHistoryDTO)
        Mockito.`when`(
            transactionsNetworkDatasource.requestUserTransactions(
                userDTOSuccess.get().id,
                userDTOSuccess.get().userToken,
                "sender"
            )
        ).thenReturn(Success(myActivityResponse))
        transactionsRepository.getUserTransactions()
        Mockito.verify(transactionsNetworkDatasource, Mockito.times(1))
            .requestUserTransactions(
                userDTOSuccess.get().id,
                userDTOSuccess.get().userToken,
                "sender"
            )
    }

    @Test
    fun `when getUserTransactions success transactionsNetworkDatasource requestUserTransactions returns success`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(myActivityMapper.map(myActivityResponse)).thenReturn(transactionsHistoryDTO)
        Mockito.`when`(
            transactionsNetworkDatasource.requestUserTransactions(
                userDTOSuccess.get().id,
                userDTOSuccess.get().userToken,
                "sender"
            )
        ).thenReturn(Success(myActivityResponse))
        val result = transactionsRepository.getUserTransactions()
        Assert.assertEquals(Success(transactionsHistoryDTO), result)
    }

    @Test
    fun `when getUserTransactions failure transactionsNetworkDatasource requestUserTransactions returns error`() {
        Mockito.`when`(apiErrorMapper.map(unmappedApiError))
            .thenReturn(unmappedDomainError)
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(myActivityMapper.map(myActivityResponse)).thenReturn(transactionsHistoryDTO)
        Mockito.`when`(
            transactionsNetworkDatasource.requestUserTransactions(
                userDTOSuccess.get().id,
                userDTOSuccess.get().userToken,
                "sender"
            )
        ).thenReturn(Failure(unmappedApiError))
        val result = transactionsRepository.getUserTransactions()
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }
}
