package com.smallworldfs.moneytransferapp.presentation.account.profile.edit

import android.app.Activity
import android.app.Activity.RESULT_OK
import android.content.Intent
import com.smallworldfs.moneytransferapp.presentation.form.selector.FormSelectorActivity
import com.smallworldfs.moneytransferapp.presentation.form.selector.FormSelectorState
import com.smallworldfs.moneytransferapp.utils.INPUT_STATE
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EditProfileNavigator @Inject constructor(
    private val activity: Activity
) {

    companion object {
        const val REQUEST_CODE_BIRTH_COUNTRY = 1001
        const val REQUEST_CODE_STATE = 1002
        const val REQUEST_CODE_PREFIX = 1003
        const val REQUEST_CODE_STREET_TYPE = 1004
        const val REQUEST_CODE_CITY = 1005
    }

    /**
     * Navigate for get birth country
     */
    fun navigateToFormSelectItemFromBirthCountry(state: FormSelectorState) {
        val i = Intent(activity, FormSelectorActivity::class.java)
        i.putExtra(INPUT_STATE, state)
        activity.startActivityForResult(i, REQUEST_CODE_BIRTH_COUNTRY)
    }

    /**
     * Navigate for get state
     */
    fun navigateToFormSelectItemFromState(state: FormSelectorState) {
        val i = Intent(activity, FormSelectorActivity::class.java)
        i.putExtra(INPUT_STATE, state)
        activity.startActivityForResult(i, REQUEST_CODE_STATE)
    }

    /**
     * Navigate for get city
     */
    fun navigateToFormSelectItemFromCity(state: FormSelectorState) {
        val i = Intent(activity, FormSelectorActivity::class.java)
        i.putExtra(INPUT_STATE, state)
        activity.startActivityForResult(i, REQUEST_CODE_CITY)
    }

    /**
     * Navigate for get prefix
     */
    fun navigateToFormSelectItemFromPhonePrefix(state: FormSelectorState) {
        val i = Intent(activity, FormSelectorActivity::class.java)
        i.putExtra(INPUT_STATE, state)
        activity.startActivityForResult(i, REQUEST_CODE_PREFIX)
    }

    /**
     * Navigate for get street type
     */
    fun navigateToFormSelectItemFromStreetType(state: FormSelectorState) {
        val i = Intent(activity, FormSelectorActivity::class.java)
        i.putExtra(INPUT_STATE, state)
        activity.startActivityForResult(i, REQUEST_CODE_STREET_TYPE)
    }

    /**
     * Navigate to profile details after save the form
     */
    fun navigateToProfileDetails() {
        activity.setResult(RESULT_OK)
        activity.finish()
    }
}
