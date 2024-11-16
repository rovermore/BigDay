package com.smallworldfs.moneytransferapp.presentation.account.offices.list

import android.app.Activity
import android.content.Intent
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.presentation.account.offices.detail.OfficeDetailActivity
import com.smallworldfs.moneytransferapp.presentation.account.offices.model.OfficeUIModel
import com.smallworldfs.moneytransferapp.utils.INPUT_STATE
import javax.inject.Inject

class OfficesNavigator @Inject constructor(
    private val activity: Activity
) {

    /**
     * Navigate to show offices map
     */
    fun navigateToOfficesMapActivity(state: OfficeUIModel) {
        val i = Intent(activity, OfficeDetailActivity::class.java)
        i.putExtra(INPUT_STATE, state)
        activity.startActivity(i)
        activity.overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left)
    }
}
