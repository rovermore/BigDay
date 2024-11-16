package com.smallworldfs.moneytransferapp.data.account.profile.repository

import com.smallworldfs.moneytransferapp.data.account.profile.model.EditProfileFormRequest
import com.smallworldfs.moneytransferapp.data.account.profile.model.SaveProfileFormRequest
import com.smallworldfs.moneytransferapp.data.account.profile.model.StateRequest
import com.smallworldfs.moneytransferapp.data.account.profile.repository.network.EditProfileNetwork
import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.domain.migrated.account.profile.edit.model.CitiesOfStateDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.profile.edit.model.EditProfileDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.profile.edit.repository.EditProfileRepository
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import javax.inject.Inject

class EditProfileRepositoryImpl @Inject constructor(
    private val apiErrorMapper: APIErrorMapper,
    private val editProfileNetworkDatasource: EditProfileNetwork
) : EditProfileRepository {

    override fun requestEditProfileForm(editProfileFormRequest: EditProfileFormRequest): OperationResult<EditProfileDTO, Error> {
        return editProfileNetworkDatasource.requestEditProfileForm(editProfileFormRequest)
            .map {
                EditProfileDTO(
                    it.form
                )
            }.mapFailure {
                apiErrorMapper.map(it)
            }
    }

    override fun requestCitiesFromState(stateRequest: StateRequest): OperationResult<CitiesOfStateDTO, Error> {
        return editProfileNetworkDatasource.requestCitiesFromState(stateRequest)
            .map {
                CitiesOfStateDTO(
                    it.data
                )
            }.mapFailure {
                apiErrorMapper.map(it)
            }
    }

    override fun saveProfileForm(saveProfileFormRequest: SaveProfileFormRequest): OperationResult<String, Error> {
        return editProfileNetworkDatasource.saveProfileForm(saveProfileFormRequest)
            .map {
                it.msg
            }.mapFailure {
                apiErrorMapper.map(it)
            }
    }
}
