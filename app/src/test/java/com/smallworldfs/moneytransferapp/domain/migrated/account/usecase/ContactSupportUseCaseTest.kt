package com.smallworldfs.moneytransferapp.domain.migrated.account.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.account.contactsupport.repository.ContactSupportRepository
import com.smallworldfs.moneytransferapp.domain.migrated.account.contactsupport.usecase.ContactSupportUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.mocks.dto.ContactSupportDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class ContactSupportUseCaseTest {

    @Mock
    lateinit var userDataRepository: UserDataRepository

    @Mock
    lateinit var contactSupportRepository: ContactSupportRepository

    lateinit var contactSupportUseCase: ContactSupportUseCase

    private val user = UserDTOMock.userDTO
    private val userError = Failure(Error.UnregisteredUser("No user found in device"))

    private val contactSupportDTO = ContactSupportDTOMock.contactSupportDTO
    private val contactSupportDTOSuccess = Success(contactSupportDTO)
    private val contactSupportDTOError = Failure(Error.UncompletedOperation("Could not complete operation"))

    private val emailSubject = "subject"
    private val emailMsg = "message"
    private val email = "email"

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        contactSupportUseCase = ContactSupportUseCase(
            contactSupportRepository,
            userDataRepository
        )
    }

    @Test
    fun `when getContactSupport success contactSupportRepository getContactSupport is called`() {
        Mockito.`when`(userDataRepository.getUserData()).thenReturn(Success(user))
        Mockito.`when`(contactSupportRepository.getContactSupport(user.country.countries.first().iso3)).thenReturn(contactSupportDTOSuccess)
        contactSupportUseCase.getContactSupport()
        Mockito.verify(contactSupportRepository, Mockito.times(1)).getContactSupport(user.country.countries.first().iso3)
    }

    @Test
    fun `when getContactSupport success contactSupportRepository getContactSupport returns success`() {
        Mockito.`when`(userDataRepository.getUserData()).thenReturn(Success(user))
        Mockito.`when`(contactSupportRepository.getContactSupport(user.country.countries.first().iso3)).thenReturn(contactSupportDTOSuccess)
        val result = contactSupportUseCase.getContactSupport()
        Assert.assertEquals(contactSupportDTOSuccess, result)
    }

    @Test
    fun `when getContactSupport failure contactSupportRepository getContactSupport returns error`() {
        Mockito.`when`(userDataRepository.getUserData()).thenReturn(Success(user))
        Mockito.`when`(contactSupportRepository.getContactSupport(user.country.countries.first().iso3)).thenReturn(contactSupportDTOError)
        val result = contactSupportUseCase.getContactSupport()
        Assert.assertEquals(contactSupportDTOError, result)
    }

    @Test
    fun `when getCurrentUser success userDataRepository getLoggedUser is called`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(Success(user))
        contactSupportUseCase.getCurrentUser()
        Mockito.verify(userDataRepository, Mockito.times(1)).getLoggedUser()
    }

    @Test
    fun `when getCurrentUser success userDataRepository getLoggedUser returns success`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(Success(user))
        val result = contactSupportUseCase.getCurrentUser()
        Assert.assertEquals(Success(user), result)
    }

    @Test
    fun `when getCurrentUser failure userDataRepository getLoggedUser returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userError)
        val result = contactSupportUseCase.getCurrentUser()
        Assert.assertEquals(userError, result)
    }
}
