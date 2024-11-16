package com.smallworldfs.moneytransferapp.data.account.profile.repository.network

import com.smallworldfs.moneytransferapp.data.account.profile.model.CitiesOfStateResponse
import com.smallworldfs.moneytransferapp.data.account.profile.model.EditProfileFormRequest
import com.smallworldfs.moneytransferapp.data.account.profile.model.EditProfileFormResponse
import com.smallworldfs.moneytransferapp.data.account.profile.model.SaveProfileFormRequest
import com.smallworldfs.moneytransferapp.data.account.profile.model.SaveProfileFormResponse
import com.smallworldfs.moneytransferapp.data.account.profile.model.StateRequest
import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import javax.inject.Inject

class EditProfileNetwork @Inject constructor(
    private val service: EditProfileService
) : NetworkDatasource() {

    fun requestEditProfileForm(editProfileFormRequest: EditProfileFormRequest): OperationResult<EditProfileFormResponse, APIError> =
        executeCall(
            service.requestEditProfileFormAsync(
                editProfileFormRequest.userToken,
                editProfileFormRequest.userId
            )
        )

    fun requestCitiesFromState(stateRequest: StateRequest): OperationResult<CitiesOfStateResponse, APIError> =
        executeCall(
            service.requestCitiesFromState(
                stateRequest.country,
                stateRequest.state,
                stateRequest.type,
            )
        )

    fun saveProfileForm(saveProfileFormRequest: SaveProfileFormRequest): OperationResult<SaveProfileFormResponse, APIError> =
        executeCall(
            service.saveProfileForm(
                saveProfileFormRequest
            )
        )
}
