package com.smallworldfs.moneytransferapp.presentation.splash

import android.app.Activity
import android.content.Intent
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.presentation.login.LoginActivity
import com.smallworldfs.moneytransferapp.presentation.onboard.OnBoardActivity
import com.smallworldfs.moneytransferapp.presentation.welcome.WelcomeActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SplashNavigator @Inject constructor(private val activity: Activity) {

    /**
     * Navigate to on board activity
     */
    fun navigateToOnBoardActivity() {
        val i = Intent(activity, OnBoardActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        activity.startActivity(i)
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    /**
     * Navigate to login activity
     */
    fun navigateToLoginActivity(appAccessToken: String?) {
        val i = Intent(activity, LoginActivity::class.java).apply {
            appAccessToken?.let { token ->
                putExtra(LoginActivity.AUTO_LOGIN, false)
                putExtra(LoginActivity.RESET_PASSWORD_TOKEN, token)
            }
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        activity.startActivity(i)
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    /**
     * Navigate to pass code activity
     */
    fun navigateToWelcomeActivity() {
        val i = Intent(activity, WelcomeActivity::class.java).apply {
            putExtra(WelcomeActivity.PASSCODE_DIALOG_KEY, WelcomeActivity.LOGIN)
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        activity.startActivity(i)
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}
