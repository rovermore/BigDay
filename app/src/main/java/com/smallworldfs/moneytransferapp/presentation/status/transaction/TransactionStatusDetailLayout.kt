package com.smallworldfs.moneytransferapp.presentation.status.transaction

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.app.ActivityOptionsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.colorGreenMain
import com.smallworldfs.moneytransferapp.compose.colors.defaultGreyControl
import com.smallworldfs.moneytransferapp.compose.colors.loadingBackground
import com.smallworldfs.moneytransferapp.compose.widgets.SWCircularLoader
import com.smallworldfs.moneytransferapp.compose.widgets.SWComposableLifecycleObserver
import com.smallworldfs.moneytransferapp.compose.widgets.SWErrorScreenLayout
import com.smallworldfs.moneytransferapp.compose.widgets.SWTopAppBar
import com.smallworldfs.moneytransferapp.compose.widgets.SWTopError
import com.smallworldfs.moneytransferapp.compose.widgets.transferdetails.SWTransferBeneficiaryInfo
import com.smallworldfs.moneytransferapp.compose.widgets.transferdetails.SWTransferDetailInfo
import com.smallworldfs.moneytransferapp.compose.widgets.transferdetails.SWTransferHelpButtons
import com.smallworldfs.moneytransferapp.compose.widgets.transferdetails.SWTransferHelpButtonsListener
import com.smallworldfs.moneytransferapp.compose.widgets.transferdetails.SWTransferStatusInfo
import com.smallworldfs.moneytransferapp.compose.widgets.transferdetails.SWTransferStatusInfoListener
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.TransactionMapper
import com.smallworldfs.moneytransferapp.modules.status.domain.model.Bank
import com.smallworldfs.moneytransferapp.modules.status.presentation.ui.activity.PayNowActivity
import com.smallworldfs.moneytransferapp.presentation.account.contact.SelectContactSupportActivity
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.status.transaction.TransactionStatusDetailActivity.Companion.TRANSACTION_MTN
import com.smallworldfs.moneytransferapp.presentation.status.transaction.TransactionStatusDetailActivity.Companion.TRANSACTION_OFFLINE
import com.smallworldfs.moneytransferapp.presentation.status.transaction.listener.TransactionStatusDetailContentListener
import com.smallworldfs.moneytransferapp.presentation.status.transaction.listener.TransactionStatusDetailLaunchedEffectsListener
import com.smallworldfs.moneytransferapp.presentation.status.transaction.listener.TransactionStatusDetailsDialogListener
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.SecondaryAction
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionItemValueUIModel
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModel
import com.smallworldfs.moneytransferapp.presentation.transferdetails.TransferDetailActivity
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.findActivity
import com.smallworldfs.moneytransferapp.utils.widget.timer.sms.SWTimer

@Composable
fun TransactionStatusDetailLayout(
    cancelTimer: SWTimer,
    registerEventCallBack: (String) -> Unit,
    onBackActionCallback: Action,
    viewModel: TransactionStatusDetailViewModel = viewModel()
) {
    val context = LocalContext.current

    val transaction by viewModel.transactionDetail.collectAsStateWithLifecycle()
    val paymentTransaction by viewModel.payTransaction.collectAsStateWithLifecycle()
    val requestPaymentMethods by viewModel.requestPaymentMethods.collectAsStateWithLifecycle()
    val changePaymentMethod by viewModel.changePaymentMethod.collectAsStateWithLifecycle()
    val showChangedPaymentMethodDialog by viewModel.showChangedPaymentMethodDialog.collectAsStateWithLifecycle()
    val pdfReceived by viewModel.pdfReceived.collectAsStateWithLifecycle()
    val showChangePaymentMethodDialog by viewModel.showChangePaymentMethodDialog.collectAsStateWithLifecycle()
    val showGenericErrorDialog by viewModel.showGenericErrorDialog.collectAsStateWithLifecycle()
    val showSuccessCancellationDialog by viewModel.showSuccessCancellationDialog.collectAsStateWithLifecycle()
    val transactionCancelledMessage by viewModel.transactionCancelled.collectAsStateWithLifecycle()
    val transactionCancelledError by viewModel.transactionCancelledError.collectAsStateWithLifecycle()
    val showCancelDialog by viewModel.showCancelDialog.collectAsStateWithLifecycle()
    val topErrorView by viewModel.topErrorOperation.collectAsStateWithLifecycle()
    val genericErrorView by viewModel.errorOperation.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()

    val mtnExtra = context.findActivity()?.intent?.extras?.getString(TRANSACTION_MTN, STRING_EMPTY) ?: STRING_EMPTY
    val offlineExtra = context.findActivity()?.intent?.extras?.getBoolean(TRANSACTION_OFFLINE, false) ?: false

    val launchedEffectsListener = object : TransactionStatusDetailLaunchedEffectsListener {
        override fun timerExpired() {
            viewModel.getTransaction(transaction.mtn, transaction.offline)
        }

        override fun requestPaymentMethodsLauncher(bank: Bank) {
            viewModel.onBankSelected(bank.depositBankBranchId ?: STRING_EMPTY, bank.depositBankId ?: STRING_EMPTY)
        }

        override fun changedPaymentMethod(changedPaymentMethod: String) {
            viewModel.showChangedPaymentMethodDialog(changedPaymentMethod)
        }

        override fun pdfReceivedError() {
            viewModel.showGenericErrorDialog()
        }

        override fun successTransactionCancelled() {
            viewModel.showSuccessCancellationDialog()
        }
    }

    val dialogListener = object : TransactionStatusDetailsDialogListener {
        override fun dismissChangedPayment() {
            viewModel.hideChangedPaymentMethodDialog()
            viewModel.getTransaction(mtnExtra, offlineExtra)
        }

        override fun positiveChangePayment() {
            viewModel.hideChangePaymentMethodDialog()
            viewModel.requestPaymentMethods(Constants.PAYNMENT_METHODS.BANKWIRE)
        }

        override fun dismissChangePayment() {
            viewModel.hideChangedPaymentMethodDialog()
        }

        override fun dismissGenericErrorDialog() {
            viewModel.hideGenericErrorDialog()
        }

        override fun dismissSuccessCancellationDialog() {
            viewModel.hideSuccessCancellationDialog()
            viewModel.getTransaction(mtnExtra, offlineExtra)
        }

        override fun dismissErrorCancellationDialog() {
            viewModel.hideErrorCancellationDialog()
            viewModel.getTransaction(mtnExtra, offlineExtra)
        }

        override fun positiveCancel() {
            viewModel.hideCancelDialog()
            viewModel.cancelTransaction()
        }

        override fun dismissCancel() {
            viewModel.hideCancelDialog()
        }
    }

    val contentListener = object : TransactionStatusDetailContentListener {
        override fun onContactSupportClick() {
            registerEventCallBack("click_contact_customer_support")
            val intent = Intent(context, SelectContactSupportActivity::class.java)
            val options = ActivityOptionsCompat.makeCustomAnimation(context, R.anim.from_right_to_left, R.anim.from_position_to_left)
            context.startActivity(intent, options.toBundle())
        }

        override fun onShowPreReceiptClick() {
            viewModel.showPreReceipt()
        }

        override fun onShowReceiptClick() {
            registerEventCallBack("click_show_receipt")
            viewModel.showReceipt()
        }

        override fun onCancelButtonClick() {
            viewModel.showCancelDialog()
        }

        override fun onShowDetailsClick(transactionUIModel: TransactionUIModel) {
            val intent = Intent(context, TransferDetailActivity::class.java)
            intent.putExtra(TransactionStatusDetailActivity.TRANSACTION_EXTRA, TransactionMapper().map(transactionUIModel))
            context.startActivity(intent)
        }

        override fun onRightButtonClick() {
            viewModel.showChangePaymentMethodDialog()
        }

        override fun onPayNowClick() {
            viewModel.payTransaction()
        }

        override fun onLeftButtonClick() {
            val intent = Intent(context, PayNowActivity::class.java)
            intent.putExtra(PayNowActivity.TRANSACTION_MTN_EXTRA, mtnExtra)
            context.startActivity(intent)
        }

        override fun onTopErrorClick() {
            viewModel.hideTopError()
        }

        override fun genericErrorRetryAction() {
            viewModel.hideErrorScreen()
            viewModel.getTransaction(mtnExtra, offlineExtra)
        }
    }

    TransactionStatusDetailsLaunchedEffects(
        context = context,
        paymentTransaction = paymentTransaction,
        requestPaymentMethods = requestPaymentMethods,
        changedPaymentMethod = changePaymentMethod,
        pdfReceived = pdfReceived,
        successTransactionCancelled = transactionCancelledMessage,
        cancelTimer = cancelTimer,
        transaction = transaction,
        launchedEffectsListener = launchedEffectsListener,
    )

    TransactionStatusDetailDialog(
        showChangedPaymentMethodDialog = showChangedPaymentMethodDialog,
        changedPaymentMethod = changePaymentMethod,
        showChangePaymentMethodDialog = showChangePaymentMethodDialog,
        showGenericErrorDialog = showGenericErrorDialog,
        showSuccessCancellationDialog = showSuccessCancellationDialog,
        successCancellationMessage = transactionCancelledMessage,
        showErrorCancellationDialog = transactionCancelledError !is ErrorType.None,
        errorCancellationMessage = transactionCancelledError.message,
        showCancelDialog = showCancelDialog,
        dialogListener = dialogListener,
    )

    TransactionStatusDetailContent(
        transaction = transaction,
        registerEventCallBack = registerEventCallBack,
        onBackActionCallback = { onBackActionCallback() },
        showTopError = topErrorView !is ErrorType.None,
        showGenericErrorView = genericErrorView !is ErrorType.None,
        loading = loading,
        contentListener = contentListener,
    )

    SWComposableLifecycleObserver(
        onEvent = { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                context.findActivity()?.intent?.extras?.let {
                    viewModel.getTransaction(
                        it.getString(TRANSACTION_MTN, STRING_EMPTY),
                        it.getBoolean(TRANSACTION_OFFLINE, false),
                    )
                } ?: run {
                    viewModel.showTopError()
                }
            }
        },
    )
}

@Composable
fun TransactionStatusDetailContent(
    transaction: TransactionUIModel,
    registerEventCallBack: (String) -> Unit,
    onBackActionCallback: Action,
    showTopError: Boolean,
    showGenericErrorView: Boolean,
    loading: Boolean,
    contentListener: TransactionStatusDetailContentListener
) {

    val helpButtonsListener = object : SWTransferHelpButtonsListener {
        override fun onContactSupport() {
            contentListener.onContactSupportClick()
        }

        override fun onShowPreReceipt() {
            contentListener.onShowPreReceiptClick()
        }

        override fun onShowReceipt() {
            contentListener.onShowReceiptClick()
        }

        override fun onCancelTransaction() {
            contentListener.onCancelButtonClick()
        }
    }

    val transferStatusInfoListener = object : SWTransferStatusInfoListener {
        override fun onCancelButton() {
            contentListener.onCancelButtonClick()
        }

        override fun onShowDetails(transaction: TransactionUIModel) {
            contentListener.onShowDetailsClick(transaction)
        }

        override fun onPayNowClick() {
            contentListener.onPayNowClick()
        }

        override fun onLeftButton() {
            contentListener.onLeftButtonClick()
        }

        override fun onRightButton() {
            contentListener.onRightButtonClick()
        }
    }

    Column(
        modifier = Modifier
            .background(defaultGreyControl)
            .fillMaxSize(),
    ) {

        if (showTopError) {
            SWTopError(
                onCloseIconClick = { contentListener.onTopErrorClick() },
            )
        }

        if (showGenericErrorView) {
            SWErrorScreenLayout(
                retryListener = {
                    contentListener.genericErrorRetryAction()
                },
            )
        }

        SWTopAppBar(
            barTitle = stringResource(id = R.string.manage_transaction_status_detail_activity),
            onBackPressed = { onBackActionCallback() },
        )

        LazyColumn(
            content = {
                item {
                    SWTransferBeneficiaryInfo(
                        transaction = transaction,
                        onCardClickCallback = {},
                        showArrowIcon = false,
                    )
                }

                item {
                    SWTransferDetailInfo(
                        transaction = transaction,
                        registerEventCallBack = registerEventCallBack,
                    )
                }

                item {
                    SWTransferStatusInfo(
                        transaction = transaction,
                        listener = transferStatusInfoListener,
                    )
                }

                item {
                    SWTransferHelpButtons(
                        transaction = transaction,
                        listener = helpButtonsListener,
                    )
                }
            },
        )
    }

    if (loading) {
        Box(
            modifier = Modifier
                .background(loadingBackground)
                .fillMaxSize(),
        ) {
            SWCircularLoader(
                color = colorGreenMain,
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun TransactionStatusDetailLayoutPreview() {
    TransactionStatusDetailContent(
        transaction = TransactionUIModel(
            offline = false,
            status = Constants.TRANSACTION_STATUS.NEW,
            statusText = "Bank Pending",
            statusMsg = "Payment pending",
            statusIcon = R.drawable.freshchat_cross_icon,
            translatedDeliveryMethod = "Cash Pickup",
            secondaryAction = SecondaryAction(buttonText = "Button Action", icon = R.drawable.ic_action_close_white),
            beneficiaryFirstName = "Test",
            beneficiaryLastName = "Test",
            beneficiaryCountry = "ARG",
            payoutPrincipal = "22354",
            payoutCurrency = "ARS",
            mtn = "28746238743",
            boleto = false,
            fixedFee = "5",
            variableFee = "2",
            totalSale = "465",
            promotionAmount = "6",
            sendingCurrency = "EUR",
            paid = false,
            deliveryMethod = "PHYSICAL_DELIVERY",
            bankName = "Santander",
            beneficiaryAccountNumber = "432876238746",
            beneficiaryAddress = "Address",
            beneficiaryZip = "Zip",
            beneficiaryMobilePhoneNumber = "2387623",
            beneficiaryCity = "City",
            rate = 10.0,
            bankAccountType = "Type",
            payerName = "Payer",
            translatedPaymentMethod = "Method",
            promotionName = "Promotion",
            senderCountry = "ARG",
            deliveryInformation = listOf(
                TransactionItemValueUIModel("Title", "Description"),
                TransactionItemValueUIModel("Title", "Description"),
            ),
            transactionInformation = listOf(
                TransactionItemValueUIModel("Title", "Description"),
                TransactionItemValueUIModel("Title", "Description"),
                TransactionItemValueUIModel("Title", "Description"),
                TransactionItemValueUIModel("Title", "Description"),
                TransactionItemValueUIModel("Title", "Description"),
            ),
            transactionTaxes = TransactionItemValueUIModel("Title", "Value"),
            taxCode = "Tax code",
            taxAmount = "22",
            cancellable = true,
            paidDate = "2024-01-31 00:00:00",
            requestDate = "2024-01-31 00:00:00",
            cancelTime = "2024-01-31 00:00:00",
        ),
        registerEventCallBack = {},
        onBackActionCallback = {},
        showTopError = false,
        showGenericErrorView = false,
        loading = false,
        contentListener = object : TransactionStatusDetailContentListener {
            override fun onContactSupportClick() = Unit
            override fun onShowPreReceiptClick() = Unit
            override fun onShowReceiptClick() = Unit
            override fun onCancelButtonClick() = Unit
            override fun onShowDetailsClick(transactionUIModel: TransactionUIModel) = Unit
            override fun onRightButtonClick() = Unit
            override fun onPayNowClick() = Unit
            override fun onLeftButtonClick() = Unit
            override fun onTopErrorClick() = Unit
            override fun genericErrorRetryAction() = Unit
        },
    )
}
