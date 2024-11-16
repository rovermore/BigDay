package com.smallworldfs.moneytransferapp.domain.migrated.account

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import javax.inject.Inject

class AccountUseCase @Inject constructor(
    private val accountRepository: AccountRepository,
    private val userDataRepository: UserDataRepository
) {

    fun getAccountMenu(): OperationResult<AccountMenuDTO, Error> = accountRepository.getAccountMenu()

    fun logout(): OperationResult<Boolean, Error> = userDataRepository.logout()

    fun getExistingUser(): OperationResult<UserDTO, Error> = userDataRepository.getLoggedUser()

    fun clearUser() = userDataRepository.clearUserData()
}
