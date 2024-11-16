package com.smallworldfs.moneytransferapp.domain.migrated.account.profile.edit.usecase

import com.smallworldfs.moneytransferapp.data.account.profile.model.EditProfileFormRequest
import com.smallworldfs.moneytransferapp.data.account.profile.model.SaveProfileFormRequest
import com.smallworldfs.moneytransferapp.data.account.profile.model.StateRequest
import com.smallworldfs.moneytransferapp.domain.migrated.account.profile.edit.model.CitiesOfStateDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.profile.edit.model.EditProfileDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.profile.edit.repository.EditProfileRepository
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import javax.inject.Inject

class EditProfileUseCase @Inject constructor(
    private val editProfileRepository: EditProfileRepository,
    private val userDataRepository: UserDataRepository
) {

    fun requestEditProfile(input: EditProfileFormRequest): OperationResult<EditProfileDTO, Error> {
        return editProfileRepository.requestEditProfileForm(input)
    }

    fun getUser(): OperationResult<UserDTO, Error> {
        return userDataRepository.getLoggedUser()
    }

    fun getCitiesFromCountries(input: StateRequest): OperationResult<CitiesOfStateDTO, Error> {
        return editProfileRepository.requestCitiesFromState(input)
    }

    fun saveProfileForm(input: SaveProfileFormRequest): OperationResult<String, Error> {
        return editProfileRepository.saveProfileForm(input)
    }
}
