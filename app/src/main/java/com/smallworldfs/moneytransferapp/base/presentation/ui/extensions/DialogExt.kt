package com.smallworldfs.moneytransferapp.base.presentation.ui.extensions

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Layout.BREAK_STRATEGY_SIMPLE
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.View.GONE
import android.view.WindowManager
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.PassCodeDialog
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericDialog
import com.smallworldfs.moneytransferapp.presentation.custom_views.PassCodeKeyboard
import com.smallworldfs.moneytransferapp.presentation.splash.SplashActivity
import com.smallworldfs.moneytransferapp.utils.Constants.COUNTRY
import com.smallworldfs.moneytransferapp.utils.LONG_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.gone
import com.smallworldfs.moneytransferapp.utils.toHtml
import com.smallworldfs.moneytransferapp.utils.visible
import com.smallworldfs.moneytransferapp.utils.widget.StyledButton
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView
import java.util.Calendar

// This class will disappear alongside app migration
class DialogExt {

    fun AppCompatActivity.showSingleActionErrorDialog(
        title: String?,
        content: String?,
        positiveActionString: String? = null,
        positiveAction: OnPositiveClick?
    ) {
        val dialog = createDialog(this, title ?: STRING_EMPTY, content ?: STRING_EMPTY)
        dialog.findViewById<StyledTextView>(R.id.positive_action).text = if (positiveActionString.isNullOrEmpty()) getString(R.string.accept_text) else positiveActionString
        dialog.findViewById<StyledTextView>(R.id.positive_action).setOnClickListener {
            dialog.dismiss()
            positiveAction?.onClick()
        }
        dialog.findViewById<StyledTextView>(R.id.negative_action).visibility = GONE
        val params = dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams
        params.width = 0
        dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams = params
        dialog.show()
    }

    fun Context.showSingleActionErrorDialog(
        title: String?,
        content: String?,
        positiveAction: OnPositiveClick?
    ): Dialog {
        val dialog = createDialog(this, title ?: STRING_EMPTY, content ?: STRING_EMPTY)
        dialog.findViewById<StyledTextView>(R.id.positive_action).text = getString(R.string.accept_text)
        dialog.findViewById<StyledTextView>(R.id.positive_action).setOnClickListener {
            dialog.dismiss()
            positiveAction?.onClick()
        }
        dialog.findViewById<StyledTextView>(R.id.negative_action).visibility = GONE
        val params = dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams
        params.width = 0
        dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams = params
        dialog.show()
        return dialog
    }

    fun AppCompatActivity.showSingleActionInfoDialog(
        title: String?,
        content: String?,
        positiveActionString: String? = null,
        positiveAction: OnPositiveClick?,
        screenName: String = STRING_EMPTY
    ) {
        val dialog = createInfoDialog(this, title ?: STRING_EMPTY, content ?: STRING_EMPTY, screenName = screenName)
        dialog.findViewById<StyledTextView>(R.id.positive_action).text = if (positiveActionString.isNullOrEmpty()) getString(R.string.accept_text) else positiveActionString
        dialog.findViewById<StyledTextView>(R.id.positive_action).setOnClickListener {
            dialog.dismiss()
            positiveAction?.onClick()
        }
        dialog.setOnCancelListener {
            dialog.dismiss()
            positiveAction?.onClick()
        }
        dialog.findViewById<StyledTextView>(R.id.negative_action).visibility = GONE
        val params = dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams
        params.width = 0
        dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams = params
        dialog.show()
    }

    fun Context.showSingleActionInfoDialog(
        title: String?,
        body: String?,
        buttonText: String?,
        positiveAction: OnPositiveClick?,
        dismissAction: OnDismissClick?
    ): Dialog {
        val dialog = createInfoDialog(this, title ?: STRING_EMPTY, body ?: STRING_EMPTY)
        dialog.findViewById<StyledTextView>(R.id.positive_action).text = buttonText ?: getString(R.string.accept_text)
        dialog.findViewById<StyledTextView>(R.id.positive_action).setOnClickListener {
            dialog.dismiss()
            positiveAction?.onClick()
        }
        dialog.setOnCancelListener {
            dialog.dismiss()
            dismissAction?.onClick()
        }
        dialog.findViewById<StyledTextView>(R.id.negative_action).visibility = GONE
        val params = dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams
        params.width = 0
        dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams = params
        dialog.show()
        return dialog
    }

    fun AppCompatActivity.showDeviceRootedDialog() {
        val dialog = createDialog(
            this,
            getString(R.string.device_rooted_title),
            getString(R.string.device_rooted_message),
        )
        dialog.findViewById<StyledTextView>(R.id.positive_action).text = getString(R.string.accept_text)
        dialog.findViewById<StyledTextView>(R.id.positive_action).setOnClickListener {
            dialog.dismiss()
            finishAffinity()
        }

        dialog.findViewById<StyledTextView>(R.id.negative_action).visibility = GONE
        val params = dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams
        params.width = 0
        dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams = params

        dialog.show()
    }

    fun AppCompatActivity.showDoubleActionGeneralDialog(
        title: String?,
        content: String?,
        positiveText: String?,
        positiveAction: OnPositiveClick,
        negativeText: String?,
        negativeAction: OnNegativeClick
    ) {
        val dialog = createDialog(this, title ?: STRING_EMPTY, content ?: STRING_EMPTY)
        dialog.findViewById<StyledTextView>(R.id.positive_action).text = positiveText ?: STRING_EMPTY
        dialog.findViewById<StyledTextView>(R.id.positive_action).setOnClickListener {
            dialog.dismiss()
            positiveAction.onClick()
        }
        dialog.findViewById<StyledTextView>(R.id.negative_action).text = negativeText ?: STRING_EMPTY
        dialog.findViewById<StyledTextView>(R.id.negative_action).setOnClickListener {
            dialog.dismiss()
            negativeAction.onClick()
        }
        dialog.show()
    }

    fun AppCompatActivity.showInfoDoubleActionGeneralDialog(
        title: String?,
        content: String?,
        positiveText: String?,
        positiveAction: OnPositiveClick,
        negativeText: String?,
        negativeAction: OnNegativeClick
    ): GenericDialog {
        val dialog = createInfoDialog(this, title ?: STRING_EMPTY, content ?: STRING_EMPTY)
        dialog.findViewById<StyledTextView>(R.id.positive_action).text = positiveText ?: STRING_EMPTY
        dialog.findViewById<StyledTextView>(R.id.positive_action).setOnClickListener {
            dialog.dismiss()
            positiveAction.onClick()
        }
        dialog.findViewById<StyledTextView>(R.id.negative_action).text = negativeText ?: STRING_EMPTY
        dialog.findViewById<StyledTextView>(R.id.negative_action).setOnClickListener {
            dialog.dismiss()
            negativeAction.onClick()
        }
        dialog.show()
        return dialog
    }

    fun AppCompatActivity.showLoadingDialog(
        title: String?,
        content: String?,
        showTitle: Boolean
    ): Dialog {
        val dialog = createProgressDialog(this, title ?: STRING_EMPTY, content ?: STRING_EMPTY, showTitle)
        dialog.show()
        return dialog
    }

    fun Context.showLoadingDialog(
        title: String?,
        content: String?,
        showTitle: Boolean
    ): Dialog {
        val dialog = createProgressDialog(this, title ?: STRING_EMPTY, content ?: STRING_EMPTY, showTitle)
        dialog.show()
        return dialog
    }

    fun AppCompatActivity.showInfoDialog(
        title: String?,
        content: String?,
        positiveAction: OnPositiveClick?,
        negativeAction: OnNegativeClick?,
        positiveText: String = getString(R.string.accept_text),
        negativeText: String = getString(R.string.cancel)
    ): Dialog {
        val dialog = createInfoDialog(this, title ?: STRING_EMPTY, content ?: STRING_EMPTY)
        positiveAction?.let {
            dialog.findViewById<StyledTextView>(R.id.positive_action).text = positiveText
            dialog.findViewById<StyledTextView>(R.id.positive_action).setOnClickListener {
                dialog.dismiss()
                positiveAction.onClick()
            }
        } ?: run {
            dialog.findViewById<StyledTextView>(R.id.positive_action).visibility = GONE
        }
        negativeAction?.let {
            dialog.findViewById<StyledTextView>(R.id.negative_action).text = negativeText
            dialog.findViewById<StyledTextView>(R.id.negative_action).setOnClickListener {
                dialog.dismiss()
                negativeAction.onClick()
            }
        } ?: run {
            dialog.findViewById<StyledTextView>(R.id.negative_action).visibility = GONE
            val params = dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams
            params.width = 0
            dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams = params
        }
        dialog.show()

        return dialog
    }

    fun Context.showInfoDialog(
        title: String?,
        content: String?,
        positiveAction: OnPositiveClick?,
        negativeAction: OnNegativeClick?,
        positiveText: String = getString(R.string.accept_text),
        negativeText: String = getString(R.string.cancel)
    ): Dialog {
        val dialog = createInfoDialog(this, title ?: STRING_EMPTY, content ?: STRING_EMPTY)
        positiveAction?.let {
            dialog.findViewById<StyledTextView>(R.id.positive_action).text = positiveText
            dialog.findViewById<StyledTextView>(R.id.positive_action).setOnClickListener {
                dialog.dismiss()
                positiveAction.onClick()
            }
        } ?: run {
            dialog.findViewById<StyledTextView>(R.id.positive_action).visibility = GONE
        }
        negativeAction?.let {
            dialog.findViewById<StyledTextView>(R.id.negative_action).text = negativeText
            dialog.findViewById<StyledTextView>(R.id.negative_action).setOnClickListener {
                dialog.dismiss()
                negativeAction.onClick()
            }
        } ?: run {
            dialog.findViewById<StyledTextView>(R.id.negative_action).visibility = GONE
            val params = dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams
            params.width = 0
            dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams = params
        }
        dialog.show()

        return dialog
    }

    fun Context.showWelcomeTipDialog(
        sendFrom: OnPositiveClick?,
        gotIt: OnPositiveClick?,
        countryOrigin: String,
        countryOriginImage: String,
        destinationCountry: String,
        destinationCountryImage: String
    ): Dialog {
        val dialog = createWelcomeTipDialog(
            this,
            countryOrigin,
            countryOriginImage,
            destinationCountry,
            destinationCountryImage,
        )
        dialog.findViewById<View>(R.id.country_selector_view).setOnClickListener {
            dialog.dismiss()
            sendFrom?.onClick()
        }

        dialog.findViewById<StyledTextView>(R.id.got_it_button_welcome).setOnClickListener {
            dialog.dismiss()
            gotIt?.onClick()
        }

        dialog.show()

        return dialog
    }

    fun showDateDialog(
        context: Context,
        minDate: Calendar? = null,
        maxDate: Calendar? = null,
        listener: DatePickerDialog.OnDateSetListener
    ) {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(context, R.style.MyDialogTheme, listener, year, month, day).apply {
            minDate?.timeInMillis?.let { this.datePicker.minDate = it }
            maxDate?.timeInMillis?.let { this.datePicker.maxDate = it }
        }

        datePickerDialog.show()
    }

    interface OnNegativeClick {
        fun onClick()
    }

    interface OnDismissClick {
        fun onClick()
    }

    interface OnPositiveClick {
        fun onClick()
    }

    interface OnPositiveInputClick {
        fun onClick(input: String?)
    }
}

fun AppCompatActivity.showInfoDialog(
    title: String?,
    content: String?,
    positiveText: String?,
    positiveAction: Action = {},
    negativeText: String?,
    negativeAction: Action = {}
): Dialog {
    val dialog = createInfoDialog(this, title ?: STRING_EMPTY, content ?: STRING_EMPTY)
    positiveText?.let {
        dialog.findViewById<StyledTextView>(R.id.positive_action).text = positiveText
        dialog.findViewById<StyledTextView>(R.id.positive_action).setOnClickListener {
            dialog.dismiss()
            positiveAction.invoke()
        }
    } ?: run {
        dialog.findViewById<StyledTextView>(R.id.positive_action).visibility = GONE
    }
    negativeText?.let {
        dialog.findViewById<StyledTextView>(R.id.negative_action).text = negativeText
        dialog.findViewById<StyledTextView>(R.id.negative_action).setOnClickListener {
            dialog.dismiss()
            negativeAction.invoke()
        }
    } ?: run {
        dialog.findViewById<StyledTextView>(R.id.negative_action).visibility = GONE
        val params = dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams
        params.width = 0
        dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams = params
    }
    dialog.show()

    return dialog
}

fun AppCompatActivity.showInfoDoubleActionGeneralDialog(
    title: String?,
    content: String?,
    positiveText: String?,
    positiveAction: Action? = null,
    negativeText: String?,
    negativeAction: Action? = null
) {
    val dialog = createInfoDialog(this, title ?: STRING_EMPTY, content ?: STRING_EMPTY)
    dialog.findViewById<StyledTextView>(R.id.positive_action).text = positiveText ?: STRING_EMPTY
    dialog.findViewById<StyledTextView>(R.id.positive_action).setOnClickListener {
        dialog.dismiss()
        positiveAction?.invoke()
    }
    dialog.findViewById<StyledTextView>(R.id.negative_action).text = negativeText ?: STRING_EMPTY
    dialog.findViewById<StyledTextView>(R.id.negative_action).setOnClickListener {
        dialog.dismiss()
        negativeAction?.invoke()
    }
    dialog.show()
}

fun Context.showInfoDialog(title: String?, content: String?, positiveAction: Action? = null, negativeAction: Action? = null, positiveText: String = getString(R.string.accept_text), negativeText: String = getString(R.string.cancel)): Dialog {
    val dialog = createInfoDialog(this, title ?: STRING_EMPTY, content ?: STRING_EMPTY)
    positiveAction?.let { it ->
        dialog.findViewById<StyledTextView>(R.id.positive_action).text = positiveText
        dialog.findViewById<StyledTextView>(R.id.positive_action).setOnClickListener {
            dialog.dismiss()
            positiveAction.invoke()
        }
    } ?: run { dialog.findViewById<StyledTextView>(R.id.positive_action).visibility = GONE }

    negativeAction?.let {
        dialog.findViewById<StyledTextView>(R.id.negative_action).text = negativeText
        dialog.findViewById<StyledTextView>(R.id.negative_action).setOnClickListener {
            dialog.dismiss()
            negativeAction.invoke()
        }
    } ?: run {
        dialog.findViewById<StyledTextView>(R.id.negative_action).visibility = GONE
        val params = dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams
        params.width = 0
        dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams = params
    }
    dialog.show()

    return dialog
}

fun showUploadDocumentDialog(
    context: Context,
    uploadIdAndSelfie: DialogExt.OnPositiveClick?,
    uploadId: DialogExt.OnPositiveClick?
): GenericDialog {
    val dialog = GenericDialog(context)
    dialog.setContentView(R.layout.upload_document_dialog)
    dialog.setCanceledOnTouchOutside(false)
    dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    dialog.window?.findViewById<ImageView>(R.id.icon)?.visible()
    dialog.window?.findViewById<StyledTextView>(R.id.title)?.gone()
    dialog.window?.findViewById<StyledTextView>(R.id.body)?.text = context.getString(R.string.checkout_validation_description)
    dialog.window?.findViewById<View>(R.id.separator)?.gone()
    dialog.window?.findViewById<StyledButton>(R.id.action_button)?.text = context.getString(R.string.checkout_continue_bank_transfer)
    dialog.window?.findViewById<StyledButton>(R.id.upload_id_and_selfie_button)?.setOnClickListener {
        uploadIdAndSelfie?.onClick()
        dialog.dismiss()
    }
    dialog.window?.findViewById<StyledButton>(R.id.action_button)?.setOnClickListener {
        if (uploadId != null) {
            uploadId.onClick()
            dialog.dismiss()
        } else {
            dialog.dismiss()
        }
    }
    dialog.show()
    return dialog
}

fun AppCompatActivity.createPasscodeDialog(
    title: String,
    content: String,
    passcodeResultListener: PassCodeKeyboard.PassCodeResultListener
) = PassCodeDialog(
    this,
    title,
    content,
    passcodeResultListener,
)

fun AppCompatActivity.showDoubleActionGeneralDialog(
    title: String?,
    content: String?,
    positiveText: String?,
    positiveAction: Action = {},
    negativeText: String?,
    negativeAction: Action = {},
    screenName: String = STRING_EMPTY,
    positiveContentDescription: String = STRING_EMPTY,
    negativeContentDescription: String = STRING_EMPTY
) {
    val dialog = createDialog(this, title ?: STRING_EMPTY, content ?: STRING_EMPTY, screenName = screenName)
    dialog.findViewById<StyledTextView>(R.id.positive_action).text = positiveText ?: STRING_EMPTY
    dialog.findViewById<StyledTextView>(R.id.positive_action).contentDescription = positiveContentDescription
    dialog.findViewById<StyledTextView>(R.id.positive_action).setOnClickListener {
        dialog.dismiss()
        positiveAction.invoke()
    }
    dialog.findViewById<StyledTextView>(R.id.negative_action).text = negativeText ?: STRING_EMPTY
    dialog.findViewById<StyledTextView>(R.id.negative_action).contentDescription = negativeContentDescription
    dialog.findViewById<StyledTextView>(R.id.negative_action).setOnClickListener {
        dialog.dismiss()
        negativeAction.invoke()
    }
    dialog.show()
}

fun Context.showSingleActionErrorDialog(
    title: String?,
    content: String?,
    positiveAction: DialogExt.OnPositiveClick?
): Dialog {
    val dialog = createDialog(this, title ?: STRING_EMPTY, content ?: STRING_EMPTY)
    dialog.findViewById<StyledTextView>(R.id.positive_action).text = getString(R.string.accept_text)
    dialog.findViewById<StyledTextView>(R.id.positive_action).setOnClickListener {
        dialog.dismiss()
        positiveAction?.onClick()
    }
    dialog.findViewById<StyledTextView>(R.id.negative_action).visibility = GONE
    val params = dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams
    params.width = 0
    dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams = params
    dialog.show()
    return dialog
}

fun Context.showSingleActionInfoDialog(
    title: String?,
    content: String?,
    textButton: String = getString(R.string.accept_text),
    positiveAction: Action = {},
    showAlertIcon: Boolean = false
): Dialog {
    val dialog = createInfoDialog(this, title ?: STRING_EMPTY, content ?: STRING_EMPTY, showAlertIcon)
    dialog.findViewById<StyledTextView>(R.id.positive_action).text = textButton
    dialog.findViewById<StyledTextView>(R.id.positive_action).setOnClickListener {
        dialog.dismiss()
        positiveAction.invoke()
    }
    dialog.findViewById<StyledTextView>(R.id.negative_action).visibility = GONE
    val params = dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams
    params.width = 0
    dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams = params
    dialog.show()
    return dialog
}

fun Context.showDoubleActionGeneralDialog(
    title: String?,
    content: String?,
    positiveText: String?,
    positiveAction: Action = {},
    negativeText: String?,
    negativeAction: Action = {}
) {
    val dialog = createDialog(this, title ?: STRING_EMPTY, content ?: STRING_EMPTY)
    dialog.findViewById<StyledTextView>(R.id.positive_action).text = positiveText ?: STRING_EMPTY
    dialog.findViewById<StyledTextView>(R.id.positive_action).setOnClickListener {
        dialog.dismiss()
        positiveAction.invoke()
    }
    dialog.findViewById<StyledTextView>(R.id.negative_action).text = negativeText ?: STRING_EMPTY
    dialog.findViewById<StyledTextView>(R.id.negative_action).setOnClickListener {
        dialog.dismiss()
        negativeAction.invoke()
    }
    dialog.show()
}

fun AppCompatActivity.showForceUpdateDialog(): Dialog {
    val dialog = createDialog(this, getString(R.string.force_dialog_title), getString(R.string.force_dialog_content_text))
    dialog.findViewById<StyledTextView>(R.id.positive_action).text = getString(R.string.update_app_button)
    dialog.findViewById<StyledTextView>(R.id.positive_action).setOnClickListener {
        dialog.dismiss()
        try {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("market://details?id=$packageName"),
                ),
            )
        } catch (e: android.content.ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$packageName"),
                ),
            )
        }
    }
    dialog.findViewById<StyledTextView>(R.id.negative_action).visibility = GONE
    val params = dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams
    params.width = 0
    dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams = params
    dialog.show()
    return dialog
}

fun AppCompatActivity.showNoConnectionOrBadResponseDialog(
    title: String,
    content: String,
    clickListener: View.OnClickListener
): Dialog {
    val dialog = createDialog(this, title, content)
    dialog.findViewById<StyledTextView>(R.id.positive_action).text = getString(R.string.retry_text)
    dialog.findViewById<StyledTextView>(R.id.positive_action).setOnClickListener(clickListener)
    dialog.findViewById<StyledTextView>(R.id.negative_action).visibility = GONE
    val params = dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams
    params.width = 0
    dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams = params
    dialog.findViewById<StyledTextView>(R.id.negative_action).text = getString(R.string.cancel)
    dialog.findViewById<StyledTextView>(R.id.negative_action).setOnClickListener {
        dialog.dismiss()
        if (this is SplashActivity) {
            finishAffinity()
        }
    }
    dialog.show()
    return dialog
}

fun AppCompatActivity.showNoConnectionDialog(clickListener: View.OnClickListener): Dialog {
    val dialog = createDialog(
        this,
        getString(R.string.no_connection_available_message),
        getString(R.string.no_connection_available_content),
    )
    dialog.findViewById<StyledTextView>(R.id.positive_action).text = getString(R.string.retry_text)
    dialog.findViewById<StyledTextView>(R.id.positive_action).setOnClickListener {
        dialog.dismiss()
        clickListener.onClick(it)
    }
    dialog.findViewById<StyledTextView>(R.id.negative_action).visibility = GONE
    val params = dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams
    params.width = 0
    dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams = params
    dialog.findViewById<StyledTextView>(R.id.negative_action).text = getString(R.string.cancel)
    dialog.findViewById<StyledTextView>(R.id.negative_action).setOnClickListener {
        dialog.dismiss()
        if (this is SplashActivity) {
            finishAffinity()
        }
    }
    dialog.show()
    return dialog
}

fun AppCompatActivity.showSplashErrorDialog(dismissAction: Action = {}): Dialog {
    val dialog = createDialog(this, getString(R.string.generic_title_error), getString(R.string.generic_error_view_text))
    dialog.findViewById<StyledTextView>(R.id.positive_action).text = getString(R.string.retry_text)
    dialog.findViewById<StyledTextView>(R.id.positive_action).setOnClickListener {
        dialog.dismiss()
        dismissAction.invoke()
    }
    dialog.findViewById<StyledTextView>(R.id.negative_action).visibility = GONE
    val params = dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams
    params.width = 0
    dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams = params
    dialog.findViewById<StyledTextView>(R.id.negative_action).text = getString(R.string.cancel)
    dialog.findViewById<StyledTextView>(R.id.negative_action).setOnClickListener {
        dialog.dismiss()
        finishAffinity()
    }
    dialog.show()
    return dialog
}

fun AppCompatActivity.showDateDialog(
    minDate: Long = LONG_ZERO,
    maxDate: Long = LONG_ZERO,
    listener: DatePickerDialog.OnDateSetListener
) {
    val c = Calendar.getInstance()
    val year = c.get(Calendar.YEAR)
    val month = c.get(Calendar.MONTH)
    val day = c.get(Calendar.DAY_OF_MONTH)

    val datePickerDialog = DatePickerDialog(this, R.style.MyDialogTheme, listener, year, month, day)
    if (minDate != LONG_ZERO) datePickerDialog.datePicker.minDate = minDate
    if (maxDate != LONG_ZERO) datePickerDialog.datePicker.maxDate = maxDate
    datePickerDialog.show()
}

fun AppCompatActivity.showTransferDetailErrorDialog(): Dialog {
    val dialog = createDialog(this, getString(R.string.generic_title_error), getString(R.string.generic_error_view_text))
    dialog.findViewById<StyledTextView>(R.id.positive_action).text = getString(R.string.accept_text)
    dialog.findViewById<StyledTextView>(R.id.positive_action).setOnClickListener {
        onBackPressed()
        dialog.dismiss()
    }
    dialog.findViewById<StyledTextView>(R.id.negative_action).visibility = GONE
    val params = dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams
    params.width = 0
    dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams = params
    dialog.show()
    return dialog
}

fun AppCompatActivity.showPasswordNotChangedErrorDialog(title: String, msg: String): Dialog {
    val dialog = createDialog(
        this,
        if (title.isNotEmpty()) {
            title
        } else {
            getString(R.string._account_settings_change_password_error)
        },
        msg,
    )
    dialog.findViewById<StyledTextView>(R.id.positive_action).text = getString(R.string.accept_text)
    dialog.findViewById<StyledTextView>(R.id.positive_action).setOnClickListener {
        dialog.dismiss()
    }
    dialog.findViewById<StyledTextView>(R.id.negative_action).visibility = GONE
    val params = dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams
    params.width = 0
    dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams = params
    dialog.show()
    return dialog
}

fun AppCompatActivity.showGenericErrorDialog(
    title: String,
    content: String,
    positiveText: String?,
    positiveCallback: PositiveCallback?,
    negativeText: String?,
    negativeCallback: NegativeCallback?
): Dialog {
    val dialog = createDialog(this, title, content)
    negativeText?.let {
        dialog.findViewById<StyledTextView>(R.id.negative_action).text = it
        dialog.findViewById<StyledTextView>(R.id.negative_action).setOnClickListener {
            negativeCallback?.onClick(dialog)
        }
        dialog.findViewById<StyledTextView>(R.id.negative_action).visibility = GONE
        val params = dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams
        params.width = 0
        dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams = params
    }

    positiveText?.let {
        dialog.findViewById<StyledTextView>(R.id.positive_action).text = it
        dialog.findViewById<StyledTextView>(R.id.positive_action).setOnClickListener {
            positiveCallback?.onClick(dialog)
        }
    }
    dialog.show()
    return dialog
}

fun AppCompatActivity.showSingleActionInfoDialog(
    title: String?,
    content: String?,
    textButton: String = getString(R.string.accept_text),
    showAlertIcon: Boolean = false,
    positiveAction: Action
) {
    val dialog = createInfoDialog(this, title ?: STRING_EMPTY, content ?: STRING_EMPTY, showAlertIcon)
    dialog.findViewById<StyledTextView>(R.id.positive_action).apply {
        text = textButton
        contentDescription = "ok_button"
        setOnClickListener {
            dialog.dismiss()
            positiveAction.invoke()
        }
    }
    dialog.setOnCancelListener {
        dialog.dismiss()
    }
    dialog.findViewById<StyledTextView>(R.id.negative_action).visibility = GONE
    val params = dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams
    params.width = 0
    dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams = params
    dialog.show()
}

fun Context.showSingleActionInfoDialog(
    title: String?,
    body: String?,
    buttonText: String?,
    positiveAction: Action?,
    dismissAction: Action?
): Dialog {
    val dialog = createInfoDialog(this, title ?: STRING_EMPTY, body ?: STRING_EMPTY)
    dialog.findViewById<StyledTextView>(R.id.positive_action).text = buttonText ?: getString(R.string.accept_text)
    dialog.findViewById<StyledTextView>(R.id.positive_action).setOnClickListener {
        dialog.dismiss()
        positiveAction?.invoke()
    }
    dialog.setOnCancelListener {
        dialog.dismiss()
        dismissAction?.invoke()
    }
    dialog.findViewById<StyledTextView>(R.id.negative_action).visibility = GONE
    val params = dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams
    params.width = 0
    dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams = params
    dialog.show()
    return dialog
}

fun AppCompatActivity.showSessionExpiredDialog(positiveCallback: (GenericDialog) -> Unit): GenericDialog {
    val dialog = createInfoDialog(this, STRING_EMPTY, getString(R.string.session_expired_text))
    dialog.setCancelable(false)
    dialog.setCanceledOnTouchOutside(false)
    dialog.analyticsTag = ScreenName.MODAL_SESSION_EXPIRED.value
    dialog.findViewById<StyledTextView>(R.id.negative_action).visibility = GONE
    val params = dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams
    params.width = 0
    dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams = params
    dialog.findViewById<StyledTextView>(R.id.positive_action).text = getString(R.string.accept_text)
    dialog.findViewById<StyledTextView>(R.id.positive_action).contentDescription = "ok_button"
    dialog.findViewById<StyledTextView>(R.id.positive_action).setOnClickListener {
        positiveCallback.invoke(dialog)
        dialog.dismiss()
    }
    dialog.show()
    return dialog
}

fun AppCompatActivity.showLoadingDialog(
    title: String?,
    content: String?,
    showTitle: Boolean
): Dialog {
    val dialog = createProgressDialog(this, title ?: STRING_EMPTY, content ?: STRING_EMPTY, showTitle)
    dialog.show()
    return dialog
}

fun AppCompatActivity.showValidationLoadingDialog(
    title: String?,
    content: String?
): Dialog {
    val dialog = GenericDialog(this)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.autentix_dialog_progress_layout)
    dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    dialog.findViewById<StyledTextView>(R.id.title).text = title
    dialog.findViewById<StyledTextView>(R.id.content).text = content
    dialog.show()
    return dialog
}

fun AppCompatActivity.showDeviceRootedDialog(): Dialog {
    val dialog = createDialog(
        this,
        getString(R.string.device_rooted_title),
        getString(R.string.device_rooted_message),
    )
    dialog.findViewById<StyledTextView>(R.id.negative_action).visibility = GONE
    val params = dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams
    params.width = 0
    dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams = params
    dialog.findViewById<StyledTextView>(R.id.positive_action).text = getString(R.string.accept_text)
    dialog.findViewById<StyledTextView>(R.id.positive_action).setOnClickListener {
        dialog.dismiss()
        finishAffinity()
    }

    dialog.show()
    return dialog
}

fun AppCompatActivity.showAnimatedDialog(
    title: String?,
    content: String?,
    showTitle: Boolean,
    animation: Int
): Dialog {
    val dialog = createAnimatedDialog(this, title ?: STRING_EMPTY, content ?: STRING_EMPTY, showTitle, animation)
    dialog.show()
    return dialog
}

fun AppCompatActivity.showSingleActionInfoDialog(
    title: String?,
    content: String?,
    positiveActionString: String? = null,
    positiveAction: DialogExt.OnPositiveClick?,
    screenName: String = STRING_EMPTY
) {
    val dialog = createInfoDialog(this, title ?: STRING_EMPTY, content ?: STRING_EMPTY, screenName = screenName)
    dialog.findViewById<StyledTextView>(R.id.positive_action).text = if (positiveActionString.isNullOrEmpty()) getString(R.string.accept_text) else positiveActionString
    dialog.findViewById<StyledTextView>(R.id.positive_action).setOnClickListener {
        dialog.dismiss()
        positiveAction?.onClick()
    }
    dialog.setOnCancelListener {
        dialog.dismiss()
        positiveAction?.onClick()
    }
    dialog.findViewById<StyledTextView>(R.id.negative_action).visibility = GONE
    val params = dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams
    params.width = 0
    dialog.findViewById<StyledTextView>(R.id.positive_action).layoutParams = params
    dialog.show()
}

@SuppressLint("WrongConstant")
private fun createInfoDialog(
    context: Context,
    title: String,
    content: String,
    showAlertIcon: Boolean = false,
    screenName: String = STRING_EMPTY
): GenericDialog {
    val dialog = GenericDialog(context)
    dialog.analyticsTag = screenName
    dialog.setContentView(R.layout.dialog_layout)
    dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    dialog.findViewById<StyledTextView>(R.id.dialog_title).text = title
    if (title == STRING_EMPTY) {
        dialog.findViewById<StyledTextView>(R.id.dialog_title).visibility = GONE
    }
    dialog.findViewById<StyledTextView>(R.id.dialog_message).apply {
        text = content.toHtml()
        movementMethod = LinkMovementMethod.getInstance()
    }
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
        dialog.findViewById<StyledTextView>(R.id.dialog_message).breakStrategy = BREAK_STRATEGY_SIMPLE
    }
    if (!showAlertIcon) {
        dialog.findViewById<ImageView>(R.id.dialog_icon).loadImage(
            R.drawable.popup_icn_app3x,
        )
        dialog.findViewById<StyledTextView>(R.id.positive_action).background = ContextCompat.getDrawable(context, R.drawable.button_background)
        dialog.findViewById<StyledTextView>(R.id.positive_action).setTextColor(ContextCompat.getColor(context, R.color.white))
        dialog.findViewById<StyledTextView>(R.id.negative_action).background = null
        dialog.findViewById<StyledTextView>(R.id.negative_action).setTextColor(ContextCompat.getColor(context, R.color.main_blue))
    }

    return dialog
}

private fun createWelcomeTipDialog(
    context: Context,
    countryOrigin: String,
    countryOriginImage: String,
    destinationCountry: String,
    destinationCountryImage: String
): GenericDialog {
    val dialog = GenericDialog(context)
    dialog.setContentView(R.layout.dialog_welcome_tip)
    dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    dialog.findViewById<StyledTextView>(R.id.origin_country_text).text = countryOrigin
    dialog.findViewById<StyledTextView>(R.id.destination_country_text).text = destinationCountry
    dialog.findViewById<ImageView>(R.id.origin_country_image).loadImage(
        COUNTRY.FLAG_IMAGE_ASSETS + countryOriginImage + COUNTRY.FLAG_IMAGE_EXTENSION,
    )
    dialog.findViewById<ImageView>(R.id.destination_country_image).loadImage(
        COUNTRY.FLAG_IMAGE_ASSETS + destinationCountryImage + COUNTRY.FLAG_IMAGE_EXTENSION,
    )
    return dialog
}

private fun createDialog(context: Context, title: String, content: String, screenName: String = STRING_EMPTY): GenericDialog {
    val dialog = GenericDialog(context)
    dialog.analyticsTag = screenName
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.dialog_layout)
    dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    if (title == STRING_EMPTY) {
        dialog.findViewById<StyledTextView>(R.id.dialog_title).visibility = GONE
    }
    dialog.findViewById<StyledTextView>(R.id.dialog_title).text = title
    dialog.findViewById<StyledTextView>(R.id.dialog_message).text = content
    return dialog
}

private fun createProgressDialog(
    context: Context,
    title: String,
    content: String,
    showTitle: Boolean
): GenericDialog {
    val dialog = GenericDialog(context)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.dialog_progress_layout)
    dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    if (showTitle) {
        dialog.findViewById<StyledTextView>(R.id.title).text = title
    } else {
        dialog.findViewById<StyledTextView>(R.id.title).visibility = GONE
    }
    dialog.findViewById<StyledTextView>(R.id.content).text = content
    return dialog
}

private fun createAnimatedDialog(
    context: Context,
    title: String,
    content: String,
    showTitle: Boolean,
    animation: Int
): GenericDialog {
    val dialog = GenericDialog(context)
    dialog.setCancelable(false)
    dialog.setContentView(R.layout.dialog_animation_layout)
    dialog.window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
    dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    if (showTitle) {
        dialog.findViewById<StyledTextView>(R.id.title).text = title
    } else {
        dialog.findViewById<StyledTextView>(R.id.title).visibility = GONE
    }
    dialog.findViewById<StyledTextView>(R.id.content).text = content
    dialog.findViewById<LottieAnimationView>(R.id.animationImageView).apply {
        setAnimation(animation)
        repeatCount = LottieDrawable.INFINITE
        playAnimation()
    }
    return dialog
}

interface PositiveCallback {
    fun onClick(dialog: Dialog)
}

interface NegativeCallback {
    fun onClick(dialog: Dialog)
}
