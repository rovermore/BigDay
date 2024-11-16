package com.smallworldfs.moneytransferapp.data.common.form

import android.content.Context
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Attributes
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.utils.forms.Action
import com.smallworldfs.moneytransferapp.utils.forms.SubType
import com.smallworldfs.moneytransferapp.utils.forms.Type
import io.reactivex.Observable
import javax.inject.Inject

class FormRepository @Inject constructor() {

    @Inject
    lateinit var context: Context

    fun clearPasswordField(list: MutableList<Field>): Observable<MutableList<Field>> = Observable.fromCallable {
        for (field: Field in list) {
            if (field.type == Type.PASSWORD) {
                field.value = ""
            }
        }
        list
    }

    fun createChangePasswordForm(attributes: Attributes): Observable<MutableList<Field>> = Observable.fromCallable {
        val list = mutableListOf<Field>()
        list.add(Field(Type.PASSWORD, "", ""))
        list[0].isRequired = true
        list[0].placeholder = context.getString(R.string.password)
        list[0].name = "password"

        list.add(Field(Type.PASSWORD, SubType.STRONG_PASSWORD, ""))
        list[1].isRequired = true
        list[1].placeholder = context.getString(R.string.change_psw_new_password_hint)
        list[1].name = "newPassword"
        list[1].attributes = attributes

        list.add(Field(Type.BUTTON, "", context.getString(R.string.change)))
        list[2].name = Action.SEND_FORM

        list
    }
}
