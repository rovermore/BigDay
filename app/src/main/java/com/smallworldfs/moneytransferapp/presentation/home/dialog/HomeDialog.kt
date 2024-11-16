package com.smallworldfs.moneytransferapp.presentation.home.dialog

import android.content.DialogInterface
import android.os.Bundle
import android.text.SpannableString
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.viewModels
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.databinding.HomeDialogBinding
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericDialogFragment

class HomeDialog(
    private val dialogTitle: String,
    private val dialogText: SpannableString,
    private val dialogBodyIcon: Int,
    private val buttonText: String,
    private val buttonAction: (() -> Unit)?,
    private val onDismiss: (() -> Unit)?
) : GenericDialogFragment() {

    private val viewModel: HomeDialogViewModel by viewModels()

    private var _binding: HomeDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = HomeDialogBinding.inflate(layoutInflater)

        setStyle(STYLE_NO_TITLE, R.style.NoFloating)

        setupView()

        return binding.root
    }

    override fun onStart() {
        super.onStart()
        dialog?.apply {
            window?.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT)
            window?.setBackgroundDrawableResource(android.R.color.transparent)
            setCanceledOnTouchOutside(true)
        }
    }

    fun setupView() {
        with(binding) {
            title.text = dialogTitle
            bodyIcon.setImageResource(dialogBodyIcon)
            message.text = dialogText
            button.text = buttonText
            button.setOnClickListener {
                if (buttonAction == null) {
                    viewModel.sendEmail()
                } else {
                    buttonAction.invoke()
                    dismissDialog()
                }
            }
            exitIcon.setOnClickListener {
                dismissDialog()
            }
        }
    }

    private fun dismissDialog() {
        dialog?.dismiss()
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismiss?.invoke()
    }
}
