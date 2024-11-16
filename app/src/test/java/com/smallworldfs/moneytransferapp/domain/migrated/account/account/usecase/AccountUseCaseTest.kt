package com.smallworldfs.moneytransferapp.domain.migrated.account.account.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.account.AccountRepository
import com.smallworldfs.moneytransferapp.domain.migrated.account.AccountUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.mocks.dto.AccountMenuDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class AccountUseCaseTest {

    @Mock
    lateinit var userDataRepository: UserDataRepository

    @Mock
    lateinit var accountRepository: AccountRepository

    lateinit var accountUseCase: AccountUseCase

    private val logoutSuccess = Success(true)
    private val logoutError = Failure(Error.UncompletedOperation("Uncompleted logout"))

    private val accountMenuSuccess = Success(AccountMenuDTOMock.accountMenuDTO)
    private val accountMenuError = Failure(Error.UncompletedOperation("Could't retrieve account menu"))

    private val user = UserDTOMock.userDTO
    private val userError = Failure(Error.UnregisteredUser("No user found in device"))

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        accountUseCase = AccountUseCase(
            accountRepository,
            userDataRepository
        )
    }

    @Test
    fun `when getAccountMenu success accountRepository getAccountMenu is called`() {
        accountUseCase.getAccountMenu()
        verify(accountRepository, times(1)).getAccountMenu()
    }

    @Test
    fun `when getAccountMenu success accountRepository getAccountMenu returns success`() {
        `when`(accountRepository.getAccountMenu()).thenReturn(accountMenuSuccess)
        val result = accountUseCase.getAccountMenu()
        assertEquals(accountMenuSuccess, result)
    }

    @Test
    fun `when getAccountMenu failure accountRepository getAccountMenu returns error`() {
        `when`(accountRepository.getAccountMenu()).thenReturn(accountMenuError)
        val result = accountUseCase.getAccountMenu()
        assertEquals(accountMenuError, result)
    }

    @Test
    fun `when logout success loginRepository logout is called`() {
        accountUseCase.logout()
        verify(userDataRepository, times(1)).logout()
    }

    @Test
    fun `when logout success loginRepository logout returns success`() {
        `when`(userDataRepository.logout()).thenReturn(logoutSuccess)
        val result = accountUseCase.logout()
        assertEquals(logoutSuccess, result)
    }

    @Test
    fun `when logout failure loginRepository logout returns error`() {
        `when`(userDataRepository.logout()).thenReturn(logoutError)
        val result = accountUseCase.logout()
        assertEquals(logoutError, result)
    }

    @Test
    fun `when getExistingUser success accountRepository getExistingUser returns success`() {
        `when`(userDataRepository.getLoggedUser()).thenReturn(Success(user))
        val result = accountUseCase.getExistingUser()
        assertEquals(Success(user), result)
    }

    @Test
    fun `when getExistingUser failure accountRepository getExistingUser returns error`() {
        `when`(userDataRepository.getLoggedUser()).thenReturn(userError)
        val result = accountUseCase.getExistingUser()
        assertEquals(userError, result)
    }

    @Test
    fun `when clear user success accountRepository clearUserData is called`() {
        accountUseCase.clearUser()
        verify(userDataRepository, times(1)).clearUserData()
    }
}
