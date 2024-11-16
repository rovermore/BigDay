package com.smallworldfs.moneytransferapp.presentation.login

import android.app.Activity
import android.content.Intent
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity
import com.smallworldfs.moneytransferapp.presentation.forgotpassword.ForgotPasswordActivity
import com.smallworldfs.moneytransferapp.presentation.freeuser.country_selection.CountrySelectionActivity
import com.smallworldfs.moneytransferapp.presentation.quicklogin.QuickLoginActivity
import com.smallworldfs.moneytransferapp.presentation.softregister.SignupActivity
import com.smallworldfs.moneytransferapp.presentation.softregister.model.RegisterStep
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LoginNavigator @Inject constructor(
    private val activity: Activity
) {

    /**
     * Navigate to registration activity
     */
    fun navigateToRegisterActivity(step: RegisterStep = RegisterStep.RegisterCredentials) {
        val i = Intent(activity, SignupActivity::class.java).apply { putExtra(SignupActivity.STEP, step) }
        activity.startActivity(i)
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    /**
     * Navigate to forgot password activity
     */
    fun navigateToForgotPasswordActivity() {
        val i = Intent(activity, ForgotPasswordActivity::class.java)
        activity.startActivity(i)
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

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
     * Navigate to pass code activity
     */
    fun navigateToPassCodeActivity() {
        val i = Intent(activity, QuickLoginActivity::class.java)
        activity.startActivity(i)
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        activity.finish()
    }

    /**
     * Navigate to app customization activity
     */
    fun navigateToAppCustomizationActivity() {
        val i = Intent(activity, CountrySelectionActivity::class.java)
        activity.startActivity(i)
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        activity.finish()
    }
}
