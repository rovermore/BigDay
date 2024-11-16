package com.smallworldfs.moneytransferapp.modules.changepassword.usecase

import com.smallworldfs.moneytransferapp.data.common.form.FormRepository
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ChangePasswordClearPasswordFieldsUseCase @Inject constructor(
    private val formRepository: FormRepository
) : ChangePasswordClearPasswordFieldsUseCaseContract {

    override fun clearPasswordFields(form: MutableList<Field>): Single<MutableList<Field>> = formRepository.clearPasswordField(form).firstOrError()
        .subscribeOn(Schedulers.io())
}

interface ChangePasswordClearPasswordFieldsUseCaseContract {
    fun clearPasswordFields(form: MutableList<Field>): Single<MutableList<Field>>
}
