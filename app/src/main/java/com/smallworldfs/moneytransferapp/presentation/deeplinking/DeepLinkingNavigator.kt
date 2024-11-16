package com.smallworldfs.moneytransferapp.presentation.deeplinking

import android.app.Activity
import android.content.Intent
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity
import com.smallworldfs.moneytransferapp.presentation.resetpassword.ResetPasswordActivity
import com.smallworldfs.moneytransferapp.presentation.splash.SplashActivity
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeepLinkingNavigator @Inject constructor(
    private val activity: Activity
) {

    /**
     * Navigate to home activity
     */
    fun navigateToHomeActivity() {
        val i = Intent(activity, HomeActivity::class.java)
        activity.startActivity(i)
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        activity.finish()
    }

    /**
     * Navigate to Reset Password activity
     */
    fun navigateToResetPasswordActivity(userResetToken: String = STRING_EMPTY) {
        val i = Intent(activity, ResetPasswordActivity::class.java)
        if (userResetToken.isNotEmpty())
            i.putExtra(Constants.DEEP_LINK.APP_TOKEN_EXTRA, userResetToken)
        activity.startActivity(i)
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        activity.finish()
    }

    /**
     * Navigate to Splash activity
     */
    fun navigateToSplashActivity() {
        val i = Intent(activity, SplashActivity::class.java)
        activity.startActivity(i)
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        activity.finish()
    }
}
