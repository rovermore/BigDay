package com.smallworldfs.moneytransferapp.data.account.profile

import com.smallworldfs.moneytransferapp.mocks.response.CitiesOfStateResponseMock
import com.smallworldfs.moneytransferapp.mocks.response.EditProfileFormRequestMock
import com.smallworldfs.moneytransferapp.mocks.response.EditProfileFormResponseMock
import com.smallworldfs.moneytransferapp.mocks.response.SaveProfileFormRequestMock
import com.smallworldfs.moneytransferapp.mocks.response.StateRequestMock
import com.smallworldfs.moneytransferapp.data.account.profile.model.SaveProfileFormResponse
import com.smallworldfs.moneytransferapp.data.account.profile.repository.EditProfileRepositoryImpl
import com.smallworldfs.moneytransferapp.data.account.profile.repository.network.EditProfileNetwork
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.domain.migrated.account.profile.edit.model.CitiesOfStateDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.profile.edit.model.EditProfileDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.profile.edit.repository.EditProfileRepository
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.get
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class EditProfileRepositoryTest {

    @Mock
    lateinit var editProfileNetworkDatasource: EditProfileNetwork

    @Mock
    lateinit var apiErrorMapper: APIErrorMapper

    lateinit var editProfileRepository: EditProfileRepository

    private val unmappedApiError = APIError.UnmappedError(432, "Unmapped error")
    private val unmappedApiErrorFailure = Failure(unmappedApiError)
    private val unmappedDomainError = Error.Unmapped("Unmapped error")

    private val editProfileFormRequest = EditProfileFormRequestMock.ditProfileFormRequest
    private val editProfileFormResponse = EditProfileFormResponseMock.editProfileFormResponse

    private val stateRequest = StateRequestMock.stateRequest
    private val citiesOfStateResponse = CitiesOfStateResponseMock.citiesOfStateResponse

    private val saveProfileFormRequest = SaveProfileFormRequestMock.saveProfileFormRequest
    private val saveProfileFormResponse = SaveProfileFormResponse("msg")

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        editProfileRepository = EditProfileRepositoryImpl(
            apiErrorMapper,
            editProfileNetworkDatasource
        )
    }

    @Test
    fun `when requestEditProfileForm success editProfileNetworkDatasource requestEditProfileForm is called`() {
        Mockito.`when`(editProfileNetworkDatasource.requestEditProfileForm(editProfileFormRequest))
            .thenReturn(Success(editProfileFormResponse))
        editProfileRepository.requestEditProfileForm(editProfileFormRequest)
        Mockito.verify(editProfileNetworkDatasource, Mockito.times(1))
            .requestEditProfileForm(editProfileFormRequest)
    }

    @Test
    fun `when requestEditProfileForm success editProfileNetworkDatasource requestEditProfileForm returns success`() {
        Mockito.`when`(editProfileNetworkDatasource.requestEditProfileForm(editProfileFormRequest))
            .thenReturn(Success(editProfileFormResponse))
        val result = editProfileRepository.requestEditProfileForm(editProfileFormRequest)
        Assert.assertEquals(EditProfileDTO(editProfileFormResponse.form).javaClass.simpleName, result.get().javaClass.simpleName)
    }

    @Test
    fun `when requestEditProfileForm failure editProfileNetworkDatasource requestEditProfileForm returns error`() {
        Mockito.`when`(apiErrorMapper.map(unmappedApiErrorFailure.reason))
            .thenReturn(unmappedDomainError)
        Mockito.`when`(editProfileNetworkDatasource.requestEditProfileForm(editProfileFormRequest))
            .thenReturn(unmappedApiErrorFailure)
        val result = editProfileRepository.requestEditProfileForm(editProfileFormRequest)
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when requestCitiesFromState success editProfileNetworkDatasource requestCitiesFromState is called`() {
        Mockito.`when`(editProfileNetworkDatasource.requestCitiesFromState(stateRequest))
            .thenReturn(Success(citiesOfStateResponse))
        editProfileRepository.requestCitiesFromState(stateRequest)
        Mockito.verify(editProfileNetworkDatasource, Mockito.times(1))
            .requestCitiesFromState(stateRequest)
    }

    @Test
    fun `when requestCitiesFromState success editProfileNetworkDatasource requestCitiesFromState returns success`() {
        Mockito.`when`(editProfileNetworkDatasource.requestCitiesFromState(stateRequest))
            .thenReturn(Success(citiesOfStateResponse))
        val result = editProfileRepository.requestCitiesFromState(stateRequest)
        Assert.assertEquals(CitiesOfStateDTO(citiesOfStateResponse.data).javaClass.simpleName, result.get().javaClass.simpleName)
    }

    @Test
    fun `when requestCitiesFromState failure editProfileNetworkDatasource requestCitiesFromState returns error`() {
        Mockito.`when`(apiErrorMapper.map(unmappedApiErrorFailure.reason))
            .thenReturn(unmappedDomainError)
        Mockito.`when`(editProfileNetworkDatasource.requestCitiesFromState(stateRequest))
            .thenReturn(unmappedApiErrorFailure)
        val result = editProfileRepository.requestCitiesFromState(stateRequest)
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when saveProfileForm success editProfileNetworkDatasource saveProfileForm is called`() {
        Mockito.`when`(editProfileNetworkDatasource.saveProfileForm(saveProfileFormRequest))
            .thenReturn(Success(saveProfileFormResponse))
        editProfileRepository.saveProfileForm(saveProfileFormRequest)
        Mockito.verify(editProfileNetworkDatasource, Mockito.times(1))
            .saveProfileForm(saveProfileFormRequest)
    }

    @Test
    fun `when saveProfileForm success editProfileNetworkDatasource saveProfileForm returns success`() {
        Mockito.`when`(editProfileNetworkDatasource.saveProfileForm(saveProfileFormRequest))
            .thenReturn(Success(saveProfileFormResponse))
        val result = editProfileRepository.saveProfileForm(saveProfileFormRequest)
        Assert.assertEquals(Success(saveProfileFormResponse.msg), result)
    }

    @Test
    fun `when saveProfileForm failure editProfileNetworkDatasource saveProfileForm returns error`() {
        Mockito.`when`(apiErrorMapper.map(unmappedApiErrorFailure.reason))
            .thenReturn(unmappedDomainError)
        Mockito.`when`(editProfileNetworkDatasource.saveProfileForm(saveProfileFormRequest))
            .thenReturn(unmappedApiErrorFailure)
        val result = editProfileRepository.saveProfileForm(saveProfileFormRequest)
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }
}
