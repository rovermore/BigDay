package com.smallworldfs.moneytransferapp.presentation.settings

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.navigator.BaseNavigator
import com.smallworldfs.moneytransferapp.modules.changepassword.ui.ChangePasswordActivity
import com.smallworldfs.moneytransferapp.presentation.quicklogin.PassCodeActivity
import com.smallworldfs.moneytransferapp.utils.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsNavigator @Inject constructor(
    private val activity: Activity
) : BaseNavigator() {

    fun navigateToConfirmPasscode() {
        val intent = Intent(activity, PassCodeActivity::class.java)
        intent.putExtra(PassCodeActivity.PASSCODE_ACTIVITY_KEY, PassCodeActivity.CONFIRM_PASSCODE_FROM_SETTINGS)
        activity.startActivity(intent)
        activity.overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left)
    }

    fun navigateToChangePassword() {
        val intent = Intent(activity, ChangePasswordActivity::class.java)
        activity.startActivityForResult(intent, Constants.REQUEST_CODES.CHANGE_PASSWORD)
        activity.overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left)
    }

    fun navigateToOpenDocument(url: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        activity.startActivity(Intent.createChooser(intent, activity.getString(R.string.open_document)))
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }
}
