package com.smallworldfs.moneytransferapp.presentation.account.offices.detail

import android.app.Activity
import android.content.Intent
import android.net.Uri
import com.smallworldfs.moneytransferapp.R
import javax.inject.Inject

class OfficeDetailNavigator @Inject constructor(
    private val activity: Activity
) {
    /**
     * Navigate to show indications
     */
    fun navigateToUrl(url: String) {
        val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        activity.startActivity(i)
        activity.overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left)
    }

    /**
     * Navigate to phone
     */
    fun navigateToPhone(phone: String) {
        val i = Intent(Intent.ACTION_DIAL)
        i.data = Uri.parse("tel:$phone")
        activity.startActivity(i)
        activity.overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left)
    }

    /**
     * Navigate to email
     */
    fun navigateToEmail(email: String) {
        val i = Intent(Intent.ACTION_SENDTO)
        i.data = Uri.parse("mailto:")
        i.putExtra(Intent.EXTRA_EMAIL, arrayOf(email))
        activity.startActivity(i)
        activity.overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left)
    }
}
