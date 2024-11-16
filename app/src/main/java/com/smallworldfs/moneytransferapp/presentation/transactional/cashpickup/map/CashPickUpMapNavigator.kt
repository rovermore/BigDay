package com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map

import android.app.Activity
import android.content.Intent
import com.smallworldfs.moneytransferapp.base.presentation.navigator.BaseNavigator
import com.smallworldfs.moneytransferapp.presentation.form.selector.FormSelectorActivity
import com.smallworldfs.moneytransferapp.presentation.form.selector.FormSelectorState
import com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.utils.CashPickupResultModel
import com.smallworldfs.moneytransferapp.utils.INPUT_STATE
import com.smallworldfs.moneytransferapp.utils.PROCESS_CATEGORY
import com.smallworldfs.moneytransferapp.utils.RESULT_ITEM
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CashPickUpMapNavigator @Inject constructor() : BaseNavigator() {

    companion object {
        const val REQUEST_CODE_STATE = 1001
        const val REQUEST_CODE_CITY = 1002
        const val REQUEST_CODE_PAYMENT_NETWORK = 1003
    }

    /**
     * Give the codes for register launchers
     */
    override val dynamicCodes: List<Int> = listOf(REQUEST_CODE_STATE, REQUEST_CODE_CITY, REQUEST_CODE_PAYMENT_NETWORK)

    /**
     * Navigate to selector screen that allow the user choose an option
     */
    fun navigateToFormSelectFromCashPickUpMap(state: FormSelectorState, code: Int) {
        Intent(activityRef.get(), FormSelectorActivity::class.java).let { intent ->
            intent.putExtra(INPUT_STATE, state)
            intent.putExtra(PROCESS_CATEGORY, "pickup")
            launchForResult(intent, false, code)
        }
    }

    /**
     * Navigate back returning results
     */
    fun navigateToPreviousScreenGivingResult(selectedLocation: CashPickupResultModel) {
        val data = Intent()
        data.putExtra(RESULT_ITEM, selectedLocation)

        activityRef.get()?.setResult(Activity.RESULT_OK, data)
        activityRef.get()?.finish()
    }
}
