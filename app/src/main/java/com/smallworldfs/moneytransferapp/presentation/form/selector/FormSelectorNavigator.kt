package com.smallworldfs.moneytransferapp.presentation.form.selector

import android.app.Activity.RESULT_OK
import android.content.Intent
import com.smallworldfs.moneytransferapp.base.presentation.navigator.BaseNavigator
import com.smallworldfs.moneytransferapp.utils.FIELD_NAME
import com.smallworldfs.moneytransferapp.utils.RESULT_ITEM
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FormSelectorNavigator @Inject constructor() : BaseNavigator() {

    fun finishActivityAndReturnResult(itemModel: FormSelectorItem, fieldName: String) {
        val data = Intent()
        data.putExtra(FIELD_NAME, fieldName)
        data.putExtra(RESULT_ITEM, itemModel)

        activityRef.get()?.setResult(RESULT_OK, data)
        activityRef.get()?.finish()
    }
}
