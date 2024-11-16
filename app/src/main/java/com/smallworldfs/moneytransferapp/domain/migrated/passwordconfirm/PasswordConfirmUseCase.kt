package com.smallworldfs.moneytransferapp.domain.migrated.passwordconfirm

import com.smallworldfs.moneytransferapp.base.domain.utils.Base64Tool
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.PasswordDTO
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import javax.inject.Inject

class PasswordConfirmUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val base64Tool: Base64Tool,
) {
    fun checkPassword(password: CharArray): OperationResult<Boolean, Error> {
        val encodedPasswordString = base64Tool.encode(String(password))
        val passwordDTO = PasswordDTO(encodedPasswordString.toCharArray())
        return userDataRepository.getLoggedUser()
            .map { user ->
                return userDataRepository.checkPassword(
                    user.email,
                    passwordDTO,
                    user.country.countries[0].iso3
                ).map {
                    return Success(it)
                }.mapFailure {
                    return Failure(it)
                }
            }.mapFailure {
                return Failure(it)
            }
    }
}
