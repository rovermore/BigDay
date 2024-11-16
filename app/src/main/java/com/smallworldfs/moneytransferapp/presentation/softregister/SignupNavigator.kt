package com.smallworldfs.moneytransferapp.presentation.softregister

import android.app.Activity
import android.content.Intent
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.presentation.login.LoginActivity
import com.smallworldfs.moneytransferapp.presentation.quicklogin.QuickLoginActivity
import javax.inject.Inject

class SignupNavigator @Inject constructor(
    private val activity: Activity
) {
    fun navigateToQuickLoginActivity() {
        val i = Intent(activity, QuickLoginActivity::class.java)
        activity.startActivity(i)
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        activity.finish()
    }

    fun navigateToLoginActivity() {
        val i = Intent(activity, LoginActivity::class.java)
        activity.startActivity(i)
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        activity.finish()
    }
}
