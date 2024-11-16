package com.smallworldfs.moneytransferapp.domain.migrated.account.contactsupport.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.account.contactsupport.model.ContactSupportDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.contactsupport.repository.ContactSupportRepository
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import javax.inject.Inject

class ContactSupportUseCase @Inject constructor(
    private val contactSupportRepository: ContactSupportRepository,
    private val userDataRepository: UserDataRepository,

) {

    fun getContactSupport(): OperationResult<ContactSupportDTO, Error> {
        return userDataRepository.getUserData()
            .mapFailure {
                return Failure(Error.UncompletedOperation("Could not retrieve user"))
            }
            .map { user ->
                return contactSupportRepository.getContactSupport(user.country.countries.first().iso3)
                    .map {
                        return Success(it.copy(isLimitedUser = user.isLimited()))
                    }
            }
    }

    fun getCurrentUser(): OperationResult<UserDTO, Error> {
        return userDataRepository.getLoggedUser()
    }
}
