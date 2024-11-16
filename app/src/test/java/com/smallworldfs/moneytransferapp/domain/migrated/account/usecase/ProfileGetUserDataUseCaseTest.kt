package com.smallworldfs.moneytransferapp.domain.migrated.account.usecase

import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import com.smallworldfs.moneytransferapp.domain.migrated.account.profile.show.usecase.ProfileGetUserDataUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.MockitoAnnotations

class ProfileGetUserDataUseCaseTest {

    @Mock
    private lateinit var userDataRepository: UserDataRepository

    private lateinit var profileGetUserDataUseCase: ProfileGetUserDataUseCase

    private val successProfile = Success(UserDTOMock.userDTO)
    private val errorProfile = Failure(Error.UncompletedOperation("Uncompleted operation"))

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        profileGetUserDataUseCase = ProfileGetUserDataUseCase(userDataRepository)
    }

    @Test
    fun `when getUserData success profileRepository getUserFromNetwork is called`() {
        `when`(userDataRepository.getUserData()).thenReturn(successProfile)
        profileGetUserDataUseCase.getUserData()
        verify(userDataRepository, Mockito.times(1)).getUserData()
    }

    @Test
    fun `when getUserData success profileRepository getUserFromNetwork returns success`() {
        `when`(userDataRepository.getUserData()).thenReturn(successProfile)
        val result = profileGetUserDataUseCase.getUserData()
        assertEquals(successProfile, result)
    }

    @Test
    fun `when getUserData failure profileRepository getUserFromNetwork returns error`() {
        `when`(userDataRepository.getUserData()).thenReturn(errorProfile)
        val result = profileGetUserDataUseCase.getUserData()
        assertEquals(errorProfile, result)
    }
}
