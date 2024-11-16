package com.smallworldfs.moneytransferapp.presentation.quicklogin

import android.app.Activity
import android.content.Intent
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity
import javax.inject.Inject

class QuickLoginNavigator @Inject constructor(
    private val activity: Activity
) {
    fun navigateToHomeActivity() {
        val intent = Intent(activity, HomeActivity::class.java)
        activity.startActivity(intent)
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    fun navigateToPassCode() {
        val intent = Intent(activity, PassCodeActivity::class.java)
        intent.putExtra(PassCodeActivity.PASSCODE_ACTIVITY_KEY, PassCodeActivity.REGISTER)
        activity.startActivity(intent)
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    fun navigateToPassCodeWithBiometrics() {
        val intent = Intent(activity, PassCodeActivity::class.java)
        intent.putExtra(PassCodeActivity.PASSCODE_ACTIVITY_KEY, PassCodeActivity.REGISTER_WITH_BIOMETRICS)
        activity.startActivity(intent)
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}
