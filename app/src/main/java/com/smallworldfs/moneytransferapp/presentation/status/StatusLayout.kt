package com.smallworldfs.moneytransferapp.presentation.status

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.mainBlue
import com.smallworldfs.moneytransferapp.compose.colors.mediumBlue
import com.smallworldfs.moneytransferapp.compose.dialogs.SWInfoDialog
import com.smallworldfs.moneytransferapp.compose.dialogs.SWWarningDialog
import com.smallworldfs.moneytransferapp.compose.widgets.SWCircularLoader
import com.smallworldfs.moneytransferapp.compose.widgets.SWComposableLifecycleObserver
import com.smallworldfs.moneytransferapp.compose.widgets.SWErrorScreenLayout
import com.smallworldfs.moneytransferapp.compose.widgets.transferdetails.SWTransferSummaryItem
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType.None
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModel
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.widget.timer.sms.SWTimer
import java.text.ParseException
import java.text.SimpleDateFormat

@Composable
fun StatusLayout(
    viewModel: StatusViewModel = viewModel(),
    listener: Callbacks,
    cancelTimer: SWTimer
) {
    val transactionStatusUIModel by viewModel.transactionStatusUIModel.collectAsStateWithLifecycle()
    val statusError by viewModel.statusError.collectAsStateWithLifecycle()
    val transactionCancelled by viewModel.transactionCancelled.collectAsStateWithLifecycle()
    val transactionCancelledError by viewModel.transactionCancelledError.collectAsStateWithLifecycle()
    val transactionList = transactionStatusUIModel.transactions
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val refreshing by viewModel.isRefreshing.collectAsStateWithLifecycle()
    val cancelDialog by viewModel.cancelDialog.collectAsStateWithLifecycle()
    val fetchTransaction = { viewModel.getTransactions() }

    Content(
        transactionList,
        loading,
        cancelDialog,
        transactionCancelledError,
        transactionCancelled,
        statusError,
        refreshing,
        contentCallbacks = object : ContentCallbacks {
            override fun onPositiveButtonClicked() {
                viewModel.dismissTransactionCancelledError()
                fetchTransaction()
            }

            override fun onDialogDismissed() {
                viewModel.dismissTransactionCancelledError()
            }

            override fun onCancelDialogDismissed() {
                viewModel.dismissCancelDialog()
            }

            override fun cancelDialogNegativeAction() {
                viewModel.cancelTransaction()
            }

            override fun fetchTransaction() {
                viewModel.getTransactions()
            }

            override fun onActionButtonClicked(transaction: TransactionUIModel) {
                if (transaction.cancellable)
                    viewModel.showCancellationDialog(transaction.mtn)
                else
                    listener.onActionButtonClicked(transaction)
            }

            override fun onSendMoneyButtonClicked() {
                listener.onSendMoneyButtonClicked()
            }

            override fun onCardClicked(transaction: TransactionUIModel) {
                listener.onCardClicked(transaction)
            }

            override fun onOrderHistoryClicked() {
                listener.onOrderHistoryClicked()
            }
        },
    )

    LaunchedEffect(transactionStatusUIModel) {
        if (transactionStatusUIModel.cancellable) {
            getRemainingTime(transactionList)?.let {
                cancelTimer.launch(it, onFinishCallback = viewModel::getTransactions)
            }
        }
    }

    SWComposableLifecycleObserver(
        onEvent = { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.getTransactions()
            }
        },
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Content(
    transactionList: List<TransactionUIModel>,
    loading: Boolean,
    cancelDialog: Boolean,
    transactionCancelledError: ErrorType,
    transactionCancelled: String,
    statusError: ErrorType,
    refreshing: Boolean,
    contentCallbacks: ContentCallbacks,
) {
    val pullRefreshState = rememberPullRefreshState(refreshing, { contentCallbacks.fetchTransaction() })

    Column {
        TransactionsHeader(stringResource(id = R.string.my_transactions_label))
        Box {
            if (transactionList.isEmpty() && !loading)
                StatusNoTransactionEmptyView { contentCallbacks.onSendMoneyButtonClicked() }
            else
                LazyColumn(
                    Modifier
                        .pullRefresh(pullRefreshState),
                ) {
                    items(transactionList) { transaction ->
                        SWTransferSummaryItem(
                            Modifier.padding(10.dp),
                            transaction = transaction,
                            onCardClickCallback = { contentCallbacks.onCardClicked(transaction) },
                            onActionButtonCallback = {
                                contentCallbacks.onActionButtonClicked(it)
                            },
                        )
                    }

                    item {
                        if (transactionList.isNotEmpty()) {
                            Row(
                                Modifier
                                    .padding(10.dp)
                                    .clickable { contentCallbacks.onOrderHistoryClicked() }
                                    .fillMaxWidth(),
                                horizontalArrangement = Arrangement.End, verticalAlignment = Alignment.CenterVertically,
                            ) {
                                Text(text = stringResource(id = R.string.order_history_text_button), color = mediumBlue, fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                Image(
                                    modifier = Modifier
                                        .padding(start = 10.dp)
                                        .size(20.dp),
                                    painter = painterResource(id = R.drawable.account_icn_arrowactivitycard), contentDescription = "",
                                )
                            }
                        }
                    }
                }

            PullRefreshIndicator(
                refreshing = refreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter),
                contentColor = mainBlue,
            )

            if (transactionCancelled.isNotEmpty()) {
                SWInfoDialog(
                    content = transactionCancelled,
                    positiveText = stringResource(id = R.string.ok),
                    positiveAction = { contentCallbacks.onPositiveButtonClicked() },
                    dismissAction = { contentCallbacks.onDialogDismissed() },
                )
            }

            if (transactionCancelledError != None) {
                SWWarningDialog(
                    content = transactionCancelledError.message,
                    positiveText = stringResource(id = R.string.ok),
                    positiveAction = { contentCallbacks.onPositiveButtonClicked() },
                    dismissAction = { contentCallbacks.onDialogDismissed() },
                )
            }

            if (cancelDialog)
                SWInfoDialog(
                    stringResource(R.string.cancel_transaction_dialog_title),
                    stringResource(R.string.cancel_transaction_dialog_message),
                    stringResource(R.string.close),
                    stringResource(R.string.cancel_transaction_transaction_status_button),
                    { contentCallbacks.onDialogDismissed() },
                    { contentCallbacks.cancelDialogNegativeAction() },
                    { contentCallbacks.onDialogDismissed() },
                )

            if (statusError != None) {
                SWErrorScreenLayout { contentCallbacks.fetchTransaction() }
            } else if (loading) {
                SWCircularLoader()
            }
        }
    }
}

private fun getRemainingTime(transactions: List<TransactionUIModel>): Long? {
    val remaining: List<Long> = transactions.map {
        if (it.cancellable && it.paidDate.isNotEmpty() && it.requestDate.isNotEmpty() && it.cancelTime.isNotEmpty()) {
            try {
                val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val paidDateTime = simpleDateFormat.parse(it.paidDate)
                val limitDateTime = simpleDateFormat.parse(simpleDateFormat.format(paidDateTime.time + it.cancelTime.toLong() * 1000))
                val currentDateTime = simpleDateFormat.parse(it.requestDate)
                val remainingTime = limitDateTime.time - currentDateTime.time
                if (remainingTime <= it.cancelTime.toLong() * 1000) remainingTime else 0L
            } catch (exception: ParseException) {
                0L
            }
        } else 0L
    }
    return remaining.filter { it > 0L }.minOrNull()
}

interface ContentCallbacks {
    fun onPositiveButtonClicked()

    fun onDialogDismissed()

    fun onCancelDialogDismissed()

    fun cancelDialogNegativeAction()

    fun fetchTransaction()

    fun onActionButtonClicked(transaction: TransactionUIModel)

    fun onSendMoneyButtonClicked()

    fun onCardClicked(transaction: TransactionUIModel)

    fun onOrderHistoryClicked()
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun StatusLayoutPreview() {
    Content(
        transactionList = emptyList(),
        false,
        cancelDialog = false,
        None,
        STRING_EMPTY,
        None,
        false,
        object : ContentCallbacks {
            override fun onPositiveButtonClicked() {
                TODO("Not yet implemented")
            }

            override fun onDialogDismissed() {
                TODO("Not yet implemented")
            }

            override fun onCancelDialogDismissed() {
                TODO("Not yet implemented")
            }

            override fun cancelDialogNegativeAction() {
                TODO("Not yet implemented")
            }

            override fun fetchTransaction() {
                TODO("Not yet implemented")
            }

            override fun onActionButtonClicked(transaction: TransactionUIModel) {
                TODO("Not yet implemented")
            }

            override fun onSendMoneyButtonClicked() {
                TODO("Not yet implemented")
            }

            override fun onCardClicked(transaction: TransactionUIModel) {
                TODO("Not yet implemented")
            }

            override fun onOrderHistoryClicked() {
                TODO("Not yet implemented")
            }
        },
    )
}
