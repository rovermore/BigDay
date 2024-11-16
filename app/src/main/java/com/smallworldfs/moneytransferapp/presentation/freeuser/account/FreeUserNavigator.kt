package com.smallworldfs.moneytransferapp.presentation.freeuser.account

import android.app.Activity
import android.content.Intent
import com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity
import javax.inject.Inject

class FreeUserNavigator @Inject constructor(private val activity: Activity) {

    companion object {
        const val DESTINATION_COUNTRY = "DESTINATION_COUNTRY"
    }

    fun navigateToHome() {
        val intent = Intent(activity, HomeActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }
}
