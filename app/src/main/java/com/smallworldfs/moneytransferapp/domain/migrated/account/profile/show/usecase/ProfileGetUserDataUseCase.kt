package com.smallworldfs.moneytransferapp.domain.migrated.account.profile.show.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import javax.inject.Inject

class ProfileGetUserDataUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository
) {

    fun getUserData(): OperationResult<UserDTO, Error> {
        return userDataRepository.getUserData()
    }
}
