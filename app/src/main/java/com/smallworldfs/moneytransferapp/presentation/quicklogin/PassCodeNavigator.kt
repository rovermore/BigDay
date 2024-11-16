package com.smallworldfs.moneytransferapp.presentation.quicklogin

import android.app.Activity
import android.content.Intent
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity
import com.smallworldfs.moneytransferapp.presentation.login.LoginActivity
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import javax.inject.Inject

class PassCodeNavigator @Inject constructor(
    private val activity: Activity
) {

    fun navigateToLogin() {
        val intent = Intent(activity, LoginActivity::class.java)
        activity.startActivity(intent)
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        activity.finish()
    }

    fun navigateToAutoLogin() {
        val intent = Intent(activity, LoginActivity::class.java)
        intent.putExtra(LoginActivity.AUTO_LOGIN, true)
        intent.putExtra(LoginActivity.RESET_PASSWORD_TOKEN, STRING_EMPTY)
        activity.startActivity(intent)
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        activity.finish()
    }

    fun navigateToHome() {
        val intent = Intent(activity, HomeActivity::class.java)
        activity.startActivity(intent)
        activity.finish()
    }

    fun finishWithSuccess() {
        activity.setResult(Activity.RESULT_OK)
        activity.finish()
    }

    fun finishWithError() {
        activity.setResult(Activity.RESULT_CANCELED)
        activity.finish()
    }
}
