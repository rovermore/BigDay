package com.smallworldfs.moneytransferapp.modules.changepassword.usecase

import com.smallworldfs.moneytransferapp.base.data.net.api.ApiException
import com.smallworldfs.moneytransferapp.data.login.mappers.UserMapperFromDTO
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User
import io.reactivex.Single
import javax.inject.Inject

class ChangePasswordGetUserUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val userMapperFromDTO: UserMapperFromDTO
) : ChangePasswordGetUserUseCaseContract {

    override fun getCurrentUser(): Single<User> {
        userDataRepository.getLoggedUser()
            .map { userModel ->
                return Single.just(userMapperFromDTO.mapFromUserDTO(userModel))
            }
            .mapFailure {

                return Single.error(ApiException())
            }
        return Single.error(ApiException())
    }
}

interface ChangePasswordGetUserUseCaseContract {
    fun getCurrentUser(): Single<User>
}
