package com.smallworldfs.moneytransferapp.presentation.status.transaction

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.smallworldfs.moneytransferapp.BuildConfig
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.SmallWorldApplication
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.modules.status.domain.model.Bank
import com.smallworldfs.moneytransferapp.modules.status.presentation.ui.activity.PayNowActivity
import com.smallworldfs.moneytransferapp.modules.status.presentation.ui.activity.SelectBankChangePaymentActivity
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.FormData
import com.smallworldfs.moneytransferapp.presentation.status.transaction.listener.TransactionStatusDetailLaunchedEffectsListener
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.PayTransactionUIModel
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModel
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.OwnFileProvider
import com.smallworldfs.moneytransferapp.utils.widget.timer.sms.SWTimer
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
fun TransactionStatusDetailsLaunchedEffects(
    context: Context,
    cancelTimer: SWTimer,
    transaction: TransactionUIModel,
    paymentTransaction: PayTransactionUIModel,
    requestPaymentMethods: FormData,
    changedPaymentMethod: String,
    pdfReceived: File?,
    successTransactionCancelled: String,
    launchedEffectsListener: TransactionStatusDetailLaunchedEffectsListener
) {

    LaunchedEffect(paymentTransaction) {
        if (paymentTransaction != PayTransactionUIModel()) {
            val intent = Intent(context, PayNowActivity::class.java)
            intent.putExtra(PayNowActivity.TRANSACTION_MTN_EXTRA, paymentTransaction.mtn)
            if (paymentTransaction.url.isNotBlank()) {
                intent.putExtra(PayNowActivity.TRANSACTION_URL_EXTRA, paymentTransaction.url)
            }
            context.startActivity(intent)
        }
    }

    val paymentMethodsLauncher = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val bank = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                result.data?.getParcelableExtra(SelectBankChangePaymentActivity.RESULT_DATA, Bank::class.java) ?: Bank()
            } else {
                result.data?.getParcelableExtra(SelectBankChangePaymentActivity.RESULT_DATA) ?: Bank()
            }
            launchedEffectsListener.requestPaymentMethodsLauncher(bank)
        }
    }

    LaunchedEffect(requestPaymentMethods) {
        if (requestPaymentMethods != FormData()) {
            if (requestPaymentMethods.fields != null && requestPaymentMethods.fields.size > 0 && requestPaymentMethods.fields[0].data != null) {
                val listBanks = ArrayList<Bank>()
                for (map in requestPaymentMethods.fields[0].data) {
                    listBanks.add(Bank(map))
                }
                if (listBanks.isNotEmpty()) {
                    val intent = Intent(context, SelectBankChangePaymentActivity::class.java)
                    intent.putExtra(SelectBankChangePaymentActivity.BANK_DATA_EXTRA, listBanks)
                    paymentMethodsLauncher.launch(intent)
                }
            }
        }
    }

    LaunchedEffect(changedPaymentMethod) {
        if (changedPaymentMethod.isNotEmpty()) {
            launchedEffectsListener.changedPaymentMethod(changedPaymentMethod)
        }
    }

    LaunchedEffect(pdfReceived) {
        if (pdfReceived != null) {
            try {
                val filePath: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    OwnFileProvider().getOwnUriForFile(context, BuildConfig.APPLICATION_ID + ".fileprovider", pdfReceived)
                } else {
                    Uri.fromFile(pdfReceived)
                }
                val pdfIntent = Intent(Intent.ACTION_VIEW)
                pdfIntent.setDataAndType(filePath, "application/pdf")
                pdfIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
                pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)
                val intent = Intent.createChooser(pdfIntent, SmallWorldApplication.getStr(R.string.open_document))
                context.startActivity(intent)
            } catch (e: Exception) {
                launchedEffectsListener.pdfReceivedError()
            }
        }
    }

    LaunchedEffect(successTransactionCancelled) {
        if (successTransactionCancelled.isNotEmpty()) {
            launchedEffectsListener.successTransactionCancelled()
        }
    }

    LaunchedEffect(transaction) {
        if (transaction != TransactionUIModel()) {
            startCancelTimer(
                cancelTimer = cancelTimer,
                transaction = transaction,
                timerExpiredCallback = { launchedEffectsListener.timerExpired() },
            )
        }
    }
}

private fun startCancelTimer(
    cancelTimer: SWTimer,
    transaction: TransactionUIModel,
    timerExpiredCallback: Action
) {
    if (transaction.senderCountry == Constants.COUNTRY.US_COUNTRY_VALUE && transaction.cancellable) {
        calculateCancelTime(transaction)?.let {
            cancelTimer.launch(
                it,
                onFinishCallback = {
                    transaction.cancellable = false
                    timerExpiredCallback()
                },
            )
        }
    }
}

private fun calculateCancelTime(
    transaction: TransactionUIModel
): Long? {
    var handlerTime: Long? = null
    return try {
        val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
        if (transaction.cancellable) {
            if (transaction.paidDate.isNotEmpty() && transaction.requestDate.isNotEmpty() && transaction.cancelTime.isNotEmpty()) {
                val paidDateTime = simpleDateFormat.parse(transaction.paidDate)
                val limitDateTime = simpleDateFormat.parse(simpleDateFormat.format((paidDateTime?.time ?: 0L) + transaction.cancelTime.toLong() * 1000))
                val currentDateTime = simpleDateFormat.parse(transaction.requestDate)
                handlerTime = (limitDateTime?.time ?: 0L) - (currentDateTime?.time ?: 0L)
            }
        }
        handlerTime
    } catch (ignored: Exception) {
        null
    }
}
