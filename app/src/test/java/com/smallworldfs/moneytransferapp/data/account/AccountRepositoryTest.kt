package com.smallworldfs.moneytransferapp.data.account

import com.smallworldfs.moneytransferapp.data.account.account.AccountRepositoryImpl
import com.smallworldfs.moneytransferapp.data.account.account.mappers.AccountMenuResponseMapper
import com.smallworldfs.moneytransferapp.data.account.account.network.AccountNetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.mocks.dto.AccountMenuDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import com.smallworldfs.moneytransferapp.mocks.response.AccountMenuResponseMock
import junit.framework.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class AccountRepositoryTest {

    @Mock
    lateinit var accountNetworkDatasource: AccountNetworkDatasource

    @Mock
    lateinit var accountMenuResponseMapper: AccountMenuResponseMapper

    @Mock
    lateinit var apiErrorMapper: APIErrorMapper

    @Mock
    lateinit var userDataRepository: UserDataRepository

    lateinit var accountRepository: AccountRepositoryImpl

    private val accountMenuResponse = AccountMenuResponseMock.accountMenuResponse
    private val successAccountMenuResponse = Success(accountMenuResponse)
    private val accountMenuSuccess = Success(AccountMenuDTOMock.accountMenuDTO)
    private val error = APIError.UnmappedError(432, "Unmapped error")
    private val apiError = Failure(error)

    private val user = UserDTOMock.userDTO
    private val userSuccess = Success(user)
    private val domainError = Error.UnregisteredUser("No user found in device")

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        accountRepository = AccountRepositoryImpl(
            accountNetworkDatasource,
            accountMenuResponseMapper,
            apiErrorMapper,
            userDataRepository
        )
    }

    @Test
    fun `when getAccountMenu success accountNetworkDatasource getAccountMenu is called`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        Mockito.`when`(accountMenuResponseMapper.map(successAccountMenuResponse.value))
            .thenReturn(AccountMenuDTOMock.accountMenuDTO)
        Mockito.`when`(
            accountNetworkDatasource.getAccountMenu(
                userSuccess.value.userToken,
                userSuccess.value.id
            )
        ).thenReturn(successAccountMenuResponse)
        accountRepository.getAccountMenu()
        Mockito.verify(accountNetworkDatasource, Mockito.times(1))
            .getAccountMenu(
                userSuccess.value.userToken,
                userSuccess.value.id
            )
    }

    @Test
    fun `when getAccountMenu success accountNetworkDatasource getAccountMenu returns success`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        Mockito.`when`(accountMenuResponseMapper.map(successAccountMenuResponse.value))
            .thenReturn(AccountMenuDTOMock.accountMenuDTO)
        Mockito.`when`(
            accountNetworkDatasource.getAccountMenu(
                userSuccess.value.userToken,
                userSuccess.value.id
            )
        ).thenReturn(successAccountMenuResponse)
        val result = accountRepository.getAccountMenu()
        Assert.assertEquals(accountMenuSuccess, result)
    }

    @Test
    fun `when getAccountMenu failure accountNetworkDatasource getAccountMenu returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        Mockito.`when`(apiErrorMapper.map(apiError.reason))
            .thenReturn(domainError)
        Mockito.`when`(
            accountNetworkDatasource.getAccountMenu(
                userSuccess.value.userToken,
                userSuccess.value.id
            )
        ).thenReturn(apiError)
        val result = accountRepository.getAccountMenu()
        Assert.assertEquals(Failure(domainError), result)
    }

    @Test
    fun `when getAccountMenu failure userDataRepository getLoggedUser returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(Failure(domainError))
        Mockito.`when`(apiErrorMapper.map(apiError.reason))
            .thenReturn(domainError)
        Mockito.`when`(
            accountNetworkDatasource.getAccountMenu(
                userSuccess.value.userToken,
                userSuccess.value.id
            )
        ).thenReturn(apiError)
        val result = accountRepository.getAccountMenu()
        Assert.assertEquals(Failure(domainError), result)
    }
}
