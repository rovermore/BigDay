package com.smallworldfs.moneytransferapp.presentation.status.transaction

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.dialogs.SWInfoDialog
import com.smallworldfs.moneytransferapp.presentation.status.transaction.listener.TransactionStatusDetailsDialogListener

@Composable
fun TransactionStatusDetailDialog(
    showChangedPaymentMethodDialog: Boolean,
    changedPaymentMethod: String,
    showChangePaymentMethodDialog: Boolean,
    showGenericErrorDialog: Boolean,
    showSuccessCancellationDialog: Boolean,
    successCancellationMessage: String,
    showErrorCancellationDialog: Boolean,
    errorCancellationMessage: String,
    showCancelDialog: Boolean,
    dialogListener: TransactionStatusDetailsDialogListener
) {
    if (showChangedPaymentMethodDialog) {
        SWInfoDialog(
            title = stringResource(id = R.string.method_changed),
            content = changedPaymentMethod,
            positiveText = stringResource(id = R.string.accept_text),
            positiveAction = { dialogListener.dismissChangedPayment() },
            dismissAction = { dialogListener.dismissChangedPayment() },
        )
    }

    if (showChangePaymentMethodDialog) {
        SWInfoDialog(
            title = stringResource(id = R.string.change_payment_method_dialog_text_title),
            content = stringResource(id = R.string.change_payment_method_dialog_text_content),
            positiveText = stringResource(id = R.string.continue_text_button),
            negativeText = stringResource(id = R.string.cancel),
            positiveAction = { dialogListener.positiveChangePayment() },
            negativeAction = { dialogListener.dismissChangePayment() },
            dismissAction = { dialogListener.dismissChangePayment() },
        )
    }

    if (showGenericErrorDialog) {
        SWInfoDialog(
            title = stringResource(id = R.string.generic_title_error),
            content = stringResource(id = R.string.not_app_pdf_viewer),
            dismissAction = { dialogListener.dismissGenericErrorDialog() },
        )
    }

    if (showSuccessCancellationDialog) {
        SWInfoDialog(
            content = successCancellationMessage,
            positiveText = stringResource(id = R.string.ok),
            positiveAction = { dialogListener.dismissSuccessCancellationDialog() },
            dismissAction = { dialogListener.dismissSuccessCancellationDialog() },
        )
    }

    if (showErrorCancellationDialog) {
        SWInfoDialog(
            content = errorCancellationMessage,
            positiveText = stringResource(id = R.string.ok),
            positiveAction = { dialogListener.dismissErrorCancellationDialog() },
            dismissAction = { dialogListener.dismissErrorCancellationDialog() },
        )
    }

    if (showCancelDialog) {
        SWInfoDialog(
            title = stringResource(id = R.string.cancel_transaction_dialog_title),
            content = stringResource(id = R.string.cancel_transaction_dialog_message),
            positiveText = stringResource(id = R.string.ok),
            negativeText = stringResource(id = R.string.close),
            positiveAction = { dialogListener.positiveCancel() },
            negativeAction = { dialogListener.dismissCancel() },
            dismissAction = { dialogListener.dismissCancel() },
        )
    }
}
