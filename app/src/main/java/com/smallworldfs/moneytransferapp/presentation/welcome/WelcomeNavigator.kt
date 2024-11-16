package com.smallworldfs.moneytransferapp.presentation.welcome

import android.app.Activity
import android.content.Intent
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.presentation.freeuser.country_selection.CountrySelectionActivity
import com.smallworldfs.moneytransferapp.presentation.login.LoginActivity
import com.smallworldfs.moneytransferapp.presentation.softregister.SignupActivity
import javax.inject.Inject

class WelcomeNavigator @Inject constructor(
    private val activity: Activity
) {

    companion object {
        const val EXTRA_MODE = "EXTRA_MODE"
    }

    fun navigateToSignup() {
        val intent = Intent(activity, SignupActivity::class.java)
        activity.startActivity(intent)
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    fun navigateToLogin() {
        val intent = Intent(activity, LoginActivity::class.java)
        intent.putExtra(EXTRA_MODE, 0)
        activity.startActivity(intent)
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    fun navigateToTransferCountrySelection() {
        val intent = Intent(activity, CountrySelectionActivity::class.java)
        activity.startActivity(intent)
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}
