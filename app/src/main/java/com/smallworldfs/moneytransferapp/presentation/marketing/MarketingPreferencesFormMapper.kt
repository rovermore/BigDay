package com.smallworldfs.moneytransferapp.presentation.marketing

import com.smallworldfs.moneytransferapp.modules.register.domain.model.Form
import com.smallworldfs.moneytransferapp.utils.forms.Type
import com.smallworldfs.moneytransferapp.utils.toBool
import javax.inject.Inject

class MarketingPreferencesFormMapper @Inject constructor() {

    fun mapSwitchNumberValueToBooleanValue(form: Form): Form {
        for (field in form.fields) {
            if (field.type == Type.SWITCH) {
                field.value = field.value.toBool().toString()
            }
        }
        return form
    }
}
