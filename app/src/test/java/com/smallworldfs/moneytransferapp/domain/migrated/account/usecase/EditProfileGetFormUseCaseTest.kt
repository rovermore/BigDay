package com.smallworldfs.moneytransferapp.domain.migrated.account.usecase

import com.smallworldfs.moneytransferapp.data.account.profile.model.EditProfileFormRequest
import com.smallworldfs.moneytransferapp.data.account.profile.model.SaveProfileFormRequest
import com.smallworldfs.moneytransferapp.data.account.profile.model.StateRequest
import com.smallworldfs.moneytransferapp.domain.migrated.account.profile.edit.model.CitiesOfStateDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.profile.edit.model.EditProfileDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.profile.edit.repository.EditProfileRepository
import com.smallworldfs.moneytransferapp.domain.migrated.account.profile.edit.usecase.EditProfileUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.domain.model.FormModel
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class EditProfileGetFormUseCaseTest {

    @Mock
    private lateinit var userDataRepository: UserDataRepository

    @Mock
    private lateinit var editProfileRepository: EditProfileRepository

    private lateinit var editProfileUseCase: EditProfileUseCase

    private val editProfileFormRequest = EditProfileFormRequest("token", "id")
    private val getCitiesOfStateRequest = StateRequest("", "", "")
    private val saveProfileFormRequest = SaveProfileFormRequest("", "")

    private val successUser = Success(UserDTOMock.userDTO)
    private val successForm = Success(
        EditProfileDTO(
            FormModel()
        )
    )
    private val successCities = Success(CitiesOfStateDTO(arrayListOf()))
    private val successSaveForm = Success("")

    private val error = Failure(Error.UncompletedOperation("Uncompleted operation"))

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        editProfileUseCase = EditProfileUseCase(editProfileRepository, userDataRepository)
    }

    @Test
    fun `when getUser success userDataRepository getUser is called`() {
        `when`(userDataRepository.getLoggedUser()).thenReturn(Success(UserDTOMock.userDTO))
        editProfileUseCase.getUser()
        verify(userDataRepository, Mockito.times(1)).getLoggedUser()
    }

    @Test
    fun `when getUser success userDataRepository getUser returns success`() {
        `when`(userDataRepository.getLoggedUser()).thenReturn(Success(UserDTOMock.userDTO))
        val result = editProfileUseCase.getUser()
        assertEquals(successUser, result)
    }

    @Test
    fun `when getUser failure userDataRepository getUser returns error`() {
        `when`(userDataRepository.getLoggedUser()).thenReturn(error)
        val result = editProfileUseCase.getUser()
        assertEquals(error, result)
    }

    @Test
    fun `when requestEditProfile success editProfileRepository requestEditProfileForm is called`() {
        `when`(editProfileRepository.requestEditProfileForm(editProfileFormRequest)).thenReturn(successForm)
        editProfileUseCase.requestEditProfile(editProfileFormRequest)
        verify(editProfileRepository, Mockito.times(1)).requestEditProfileForm(editProfileFormRequest)
    }

    @Test
    fun `when requestEditProfile success editProfileRepository requestEditProfileForm returns success`() {
        `when`(editProfileRepository.requestEditProfileForm(editProfileFormRequest)).thenReturn(successForm)
        val result = editProfileUseCase.requestEditProfile(editProfileFormRequest)
        assertEquals(successForm, result)
    }

    @Test
    fun `when requestEditProfile failure editProfileRepository requestEditProfileForm returns error`() {
        `when`(editProfileRepository.requestEditProfileForm(editProfileFormRequest)).thenReturn(error)
        val result = editProfileUseCase.requestEditProfile(editProfileFormRequest)
        assertEquals(error, result)
    }

    @Test
    fun `when getCitiesFromCountries success editProfileRepository requestCitiesFromState is called`() {
        `when`(editProfileRepository.requestCitiesFromState(getCitiesOfStateRequest)).thenReturn(successCities)
        editProfileUseCase.getCitiesFromCountries(getCitiesOfStateRequest)
        verify(editProfileRepository, Mockito.times(1)).requestCitiesFromState(getCitiesOfStateRequest)
    }

    @Test
    fun `when getCitiesFromCountries success editProfileRepository requestCitiesFromState returns success`() {
        `when`(editProfileRepository.requestCitiesFromState(getCitiesOfStateRequest)).thenReturn(successCities)
        val result = editProfileUseCase.getCitiesFromCountries(getCitiesOfStateRequest)
        assertEquals(successCities, result)
    }

    @Test
    fun `when getCitiesFromCountries failure editProfileRepository requestCitiesFromState returns error`() {
        `when`(editProfileRepository.requestCitiesFromState(getCitiesOfStateRequest)).thenReturn(error)
        val result = editProfileUseCase.getCitiesFromCountries(getCitiesOfStateRequest)
        assertEquals(error, result)
    }

    @Test
    fun `when saveProfileForm success editProfileRepository saveProfileForm is called`() {
        `when`(editProfileRepository.saveProfileForm(saveProfileFormRequest)).thenReturn(successSaveForm)
        editProfileUseCase.saveProfileForm(saveProfileFormRequest)
        verify(editProfileRepository, Mockito.times(1)).saveProfileForm(saveProfileFormRequest)
    }

    @Test
    fun `when saveProfileForm success editProfileRepository saveProfileForm returns success`() {
        `when`(editProfileRepository.saveProfileForm(saveProfileFormRequest)).thenReturn(successSaveForm)
        val result = editProfileUseCase.saveProfileForm(saveProfileFormRequest)
        assertEquals(successSaveForm, result)
    }

    @Test
    fun `when saveProfileForm failure editProfileRepository saveProfileForm returns error`() {
        `when`(editProfileRepository.saveProfileForm(saveProfileFormRequest)).thenReturn(error)
        val result = editProfileUseCase.saveProfileForm(saveProfileFormRequest)
        assertEquals(error, result)
    }
}
