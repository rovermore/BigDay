package com.smallworldfs.moneytransferapp.presentation.account.documents.upload

import android.app.Activity
import android.content.Intent
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.presentation.account.documents.form.TipsActivity
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.ComplianceDocUIModel
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.DocumentUIModel
import javax.inject.Inject

class UploadDocumentsNavigator @Inject constructor(
    private val activity: Activity
) {

    fun navigateToTipsActivity(data: DocumentUIModel) {
        val i = Intent(activity, TipsActivity::class.java)
        i.putExtra(TipsActivity.TIP_DATA, if (data is ComplianceDocUIModel) data.subtype else "other")
        activity.startActivity(i)
        activity.overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left)
    }
}
