package com.smallworldfs.moneytransferapp.base.presentation.ui

import android.content.Context
import android.view.WindowManager
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.loadImage
import com.smallworldfs.moneytransferapp.databinding.DialogPasscodeLayoutBinding
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericDialog
import com.smallworldfs.moneytransferapp.presentation.custom_views.PassCodeKeyboard

class PassCodeDialog(
    context: Context,
    title: String,
    content: String,
    passCodeResultListener: PassCodeKeyboard.PassCodeResultListener
) : GenericDialog(context) {

    private var passCodeKeyboard: PassCodeKeyboard

    private var binding: DialogPasscodeLayoutBinding = DialogPasscodeLayoutBinding.inflate(layoutInflater)

    init {
        setCancelable(true)
        setContentView(binding.root)

        window?.apply {
            setLayout(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT
            )
            setBackgroundDrawableResource(android.R.color.transparent)
        }

        with(binding) {
            dialogPasscodeTitle.text = title
            dialogPasscodeMessage.text = content

            passCodeKeyboard = dialogPassCodeKeyboard
            passCodeKeyboard.apply {
                setPassCodeResultListener(passCodeResultListener)
                setCreateMode(false)
            }

            dialogCloseIcon.setOnClickListener {
                passCodeResultListener.onDismiss()
                dismiss()
            }
            dialogIcon.loadImage(R.drawable.popup_icn_app3x)
        }

        analyticsTag = ScreenName.ENTER_PASSCODE_SCREEN.value
    }

    fun onPassCodeError() {
        passCodeKeyboard.showPasscodeError()
    }

    fun setLoginMode() {
        passCodeKeyboard.setLoginMode()
    }
}
