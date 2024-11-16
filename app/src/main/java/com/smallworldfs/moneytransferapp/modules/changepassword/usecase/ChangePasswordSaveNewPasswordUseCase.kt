package com.smallworldfs.moneytransferapp.modules.changepassword.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.PasswordDTO
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import io.reactivex.Single
import javax.inject.Inject

class ChangePasswordSaveNewPasswordUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository
) : ChangePasswordSaveNewPasswordUseCaseContract {

    override fun saveNewPassword(newPassword: String): Single<Unit> {
        val passwordDTO = PasswordDTO(newPassword.toCharArray())
        userDataRepository.setPassword(passwordDTO)
            .map {
                passwordDTO.release()
                return Single.just(Unit)
            }
            .mapFailure {
                passwordDTO.release()
                return Single.just(Unit)
            }
        passwordDTO.release()
        return Single.just(Unit)
    }
}

interface ChangePasswordSaveNewPasswordUseCaseContract {
    fun saveNewPassword(newPassword: String): Single<Unit>
}
