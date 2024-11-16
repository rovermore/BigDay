package com.smallworldfs.moneytransferapp.presentation.account.beneficiary.detail

import android.app.Activity
import android.content.Intent
import androidx.core.app.ActivityOptionsCompat
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction
import com.smallworldfs.moneytransferapp.modules.status.presentation.ui.activity.PayNowActivity
import com.smallworldfs.moneytransferapp.presentation.transferdetails.TransferDetailActivity
import com.smallworldfs.moneytransferapp.presentation.status.transaction.TransactionStatusDetailActivity
import com.smallworldfs.moneytransferapp.utils.Constants
import javax.inject.Inject

class BeneficiaryDetailNavigator @Inject constructor(
    private val activity: Activity
) {
    /**
     * Navigate to pay now activity
     */
    fun navigateToPayNowActivity(transactionMtn: String) {
        val i = Intent(activity, PayNowActivity::class.java)
        i.putExtra(PayNowActivity.TRANSACTION_MTN_EXTRA, transactionMtn)
        activity.startActivityForResult(i, Constants.REQUEST_CODES.PAY_NOW_ACTIVITY_REQUEST_CODE)
        activity.overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left)
    }

    /**
     * Navigate to transfer details
     */
    fun navigateToTransferDetails(transaction: Transaction) {
        val i = Intent(activity, TransferDetailActivity::class.java)
        i.putExtra(TransactionStatusDetailActivity.TRANSACTION_EXTRA, transaction)
        activity.startActivityForResult(i, Constants.REQUEST_CODES.TRANSFER_DETAILS_REQUEST_CODE)
        activity.overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left)
    }

    fun navigateToTransactionStatusDetail(mtn: Long, transaction: Transaction) {
        val i = Intent(activity, TransactionStatusDetailActivity::class.java)
        i.putExtra(TransactionStatusDetailActivity.TRANSACTION_MTN, mtn)
        i.putExtra(TransactionStatusDetailActivity.TRANSACTION_OFFLINE, transaction.isOffline)
        activity.startActivityForResult(i, Constants.REQUEST_CODES.TRANSACTION_DETAIL_REQUEST_CODE, ActivityOptionsCompat.makeCustomAnimation(activity, R.anim.from_right_to_left, R.anim.from_position_to_left).toBundle())
    }
}
