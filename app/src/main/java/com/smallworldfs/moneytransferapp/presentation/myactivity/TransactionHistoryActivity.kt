package com.smallworldfs.moneytransferapp.presentation.myactivity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.compose.setContent
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.presentation.status.transaction.TransactionStatusDetailActivity
import com.smallworldfs.moneytransferapp.utils.Constants
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class TransactionHistoryActivity : GenericActivity() {

    @Inject
    lateinit var navigator: MyActivityNavigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TransactionHistoryLayout(
                onBackActionCallback = { onBackPressedDispatcher.onBackPressed() },
                onActionShowTransactionDetail = { item ->
                    navigator.navigateToTransactionDetail(item)
                },
            ) {
                registerEvent("click_send_money")
                navigator.navigateToSendMoney()
            }
        }

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    registerEvent("click_back")
                    finish()
                }
            },
        )
    }

    private fun registerEvent(eventAction: String) {
        trackEvent(
            UserActionEvent(
                ScreenCategory.DASHBOARD.value,
                eventAction,
                "",
                getHierarchy(""),
            ),
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_CODES.TRANSACTION_DETAIL_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            val showTransferDetails = data?.getBooleanExtra(TransactionStatusDetailActivity.RETURN_INTENT_DATA, false)
            onActivityResultRepeatTransactionSuccessful(showTransferDetails ?: false)
        }
    }

    private fun onActivityResultRepeatTransactionSuccessful(showTransferDetails: Boolean) {
        if (showTransferDetails) {
            val returnIntent = Intent()
            returnIntent.putExtra(TransactionStatusDetailActivity.RETURN_INTENT_DATA, true)
            navigator.navigateBack()
        }
    }
}
