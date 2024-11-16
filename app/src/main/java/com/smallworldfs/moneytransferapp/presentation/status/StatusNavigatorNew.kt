package com.smallworldfs.moneytransferapp.presentation.status

import android.app.Activity
import android.content.Intent
import androidx.core.app.ActivityOptionsCompat
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.TransactionMapper
import com.smallworldfs.moneytransferapp.presentation.transferdetails.TransferDetailActivity
import com.smallworldfs.moneytransferapp.presentation.myactivity.TransactionHistoryActivity
import com.smallworldfs.moneytransferapp.presentation.status.transaction.TransactionStatusDetailActivity
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModel
import com.smallworldfs.moneytransferapp.utils.Constants.REQUEST_CODES
import javax.inject.Inject

class StatusNavigatorNew @Inject constructor(
    private val activity: Activity
) {

    fun navigateToMyActivityActivity() {
        val i = Intent(activity, TransactionHistoryActivity::class.java)
        activity.startActivity(i)
        activity.overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left)
    }

    fun navigateToTransferDetails(transaction: TransactionUIModel) {
        val intent = Intent(activity, TransferDetailActivity::class.java)
        intent.putExtra(TransactionStatusDetailActivity.TRANSACTION_EXTRA, TransactionMapper().map(transaction))
        activity.startActivityForResult(intent, REQUEST_CODES.TRANSFER_DETAILS_REQUEST_CODE)
    }

    fun navigateToTransactionStatusDetail(mtn: String, offline: Boolean) {
        val intent = Intent(activity, TransactionStatusDetailActivity::class.java)
        intent.putExtra(TransactionStatusDetailActivity.TRANSACTION_MTN, mtn)
        intent.putExtra(TransactionStatusDetailActivity.TRANSACTION_OFFLINE, offline)
        val compat = ActivityOptionsCompat.makeCustomAnimation(activity, R.anim.from_right_to_left, R.anim.from_position_to_left)
        activity.startActivityForResult(intent, REQUEST_CODES.TRANSACTION_DETAIL_REQUEST_CODE, compat.toBundle())
    }
}
