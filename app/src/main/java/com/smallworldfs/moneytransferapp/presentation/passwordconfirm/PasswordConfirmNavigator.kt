package com.smallworldfs.moneytransferapp.presentation.passwordconfirm

import android.app.Activity
import android.content.Intent
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.navigator.BaseNavigator
import com.smallworldfs.moneytransferapp.presentation.quicklogin.PassCodeActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PasswordConfirmNavigator @Inject constructor(
    private val activity: Activity
) : BaseNavigator() {

    fun navigateToCreatePasscode() {
        val intent = Intent(activity, PassCodeActivity::class.java)
        intent.putExtra(PassCodeActivity.PASSCODE_ACTIVITY_KEY, PassCodeActivity.CREATE_PASSCODE_FROM_SETTINGS)
        activity.startActivity(intent)
        activity.overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left)
        activity.finish()
    }

    fun finishWithSuccess() {
        activity.setResult(Activity.RESULT_OK)
        activity.finish()
    }
}
