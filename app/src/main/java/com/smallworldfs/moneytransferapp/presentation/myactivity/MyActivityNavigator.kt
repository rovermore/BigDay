package com.smallworldfs.moneytransferapp.presentation.myactivity

import android.app.Activity
import android.content.Intent
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity
import com.smallworldfs.moneytransferapp.presentation.status.transaction.TransactionStatusDetailActivity
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModel
import com.smallworldfs.moneytransferapp.utils.Constants.REQUEST_CODES
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MyActivityNavigator @Inject constructor(
    private val activity: Activity
) {

    /**
     * Navigate back
     */
    fun navigateBack() {
        activity.setResult(Activity.RESULT_OK, Intent().putExtra(TransactionStatusDetailActivity.RETURN_INTENT_DATA, true))
        activity.finish()
    }

    /**
     * Navigate to send money screen
     */
    fun navigateToSendMoney() {
        activity.setResult(Activity.RESULT_OK, Intent().putExtra(HomeActivity.SHOW_SEND_TO_TAB_EXTRA, true))
        activity.finish()
    }

    /**
     * Navigate to transaction detail
     */
    fun navigateToTransactionDetail(item: TransactionUIModel) {
        val i = Intent(activity, TransactionStatusDetailActivity::class.java)
        i.putExtra(TransactionStatusDetailActivity.TRANSACTION_MTN, item.mtn)
        i.putExtra(TransactionStatusDetailActivity.TRANSACTION_OFFLINE, item.offline)

        activity.startActivityForResult(i, REQUEST_CODES.TRANSACTION_DETAIL_REQUEST_CODE)
        activity.overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left)
    }
}
