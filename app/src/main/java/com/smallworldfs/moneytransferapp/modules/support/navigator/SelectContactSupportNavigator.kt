package com.smallworldfs.moneytransferapp.modules.support.navigator

import android.app.Activity
import android.content.Intent
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.modules.status.presentation.ui.activity.SendEmailActivity
import com.smallworldfs.moneytransferapp.modules.status.presentation.ui.activity.SendEmailLimitedUserActivity
import com.smallworldfs.moneytransferapp.modules.support.model.ContactSupportInfoUIModel
import com.smallworldfs.moneytransferapp.modules.web.presentation.SWWebViewActivity
import com.smallworldfs.moneytransferapp.utils.Constants
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SelectContactSupportNavigator @Inject constructor(
    private val activity: Activity
) {

    companion object {
        const val CONTACT_SUPPORT_INFO_EXTRA = "CONTACT_SUPPORT_INFO_EXTRA"
    }

    fun navigateToLimitedSendEmail(contactSupportInfoUIModel: ContactSupportInfoUIModel) {
        val intent = Intent(activity, SendEmailLimitedUserActivity::class.java)
        intent.putExtra(CONTACT_SUPPORT_INFO_EXTRA, contactSupportInfoUIModel)
        activity.startActivityForResult(intent, Constants.REQUEST_CODES.SEND_EMAIL_REQUEST_CODE)
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    fun navigateToSendEmail(contactSupportInfoUIModel: ContactSupportInfoUIModel) {
        val intent = Intent(activity, SendEmailActivity::class.java)
        intent.putExtra(CONTACT_SUPPORT_INFO_EXTRA, contactSupportInfoUIModel)
        activity.startActivityForResult(intent, Constants.REQUEST_CODES.SEND_EMAIL_REQUEST_CODE)
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    fun navigateToFaqs(url: String) {
        val intent = Intent(activity, SWWebViewActivity::class.java)
        intent.putExtra(SWWebViewActivity.FAQS_URL, url)
        activity.startActivity(intent)
        activity.overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left)
    }
}
