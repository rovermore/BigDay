package com.smallworldfs.moneytransferapp.modules.changepassword.usecase

import com.smallworldfs.moneytransferapp.data.common.form.FormRepository
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ChangePasswordCreateFormUseCase @Inject constructor(
    private val changePasswordRequestPasswordAttributesUseCaseContract: ChangePasswordRequestPasswordAttributesUseCaseContract,
    private val formRepository: FormRepository
) : ChangePasswordCreateFormUseCaseContract {

    override fun requestChangePasswordForm(): Single<MutableList<Field>> = changePasswordRequestPasswordAttributesUseCaseContract.requestPasswordAttributes()
        .flatMap { attributes ->
            formRepository.createChangePasswordForm(attributes).firstOrError()
        }
        .subscribeOn(Schedulers.io())
}

interface ChangePasswordCreateFormUseCaseContract {
    fun requestChangePasswordForm(): Single<MutableList<Field>>
}
