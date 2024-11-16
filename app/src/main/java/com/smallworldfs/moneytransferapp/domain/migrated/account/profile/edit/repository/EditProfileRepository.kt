package com.smallworldfs.moneytransferapp.domain.migrated.account.profile.edit.repository

import com.smallworldfs.moneytransferapp.data.account.profile.model.EditProfileFormRequest
import com.smallworldfs.moneytransferapp.data.account.profile.model.SaveProfileFormRequest
import com.smallworldfs.moneytransferapp.data.account.profile.model.StateRequest
import com.smallworldfs.moneytransferapp.domain.migrated.account.profile.edit.model.CitiesOfStateDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.profile.edit.model.EditProfileDTO
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult

interface EditProfileRepository {

    fun requestEditProfileForm(editProfileFormRequest: EditProfileFormRequest): OperationResult<EditProfileDTO, Error>

    fun requestCitiesFromState(stateRequest: StateRequest): OperationResult<CitiesOfStateDTO, Error>

    fun saveProfileForm(saveProfileFormRequest: SaveProfileFormRequest): OperationResult<String, Error>
}
