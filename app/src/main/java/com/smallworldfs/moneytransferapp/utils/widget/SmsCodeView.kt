package com.smallworldfs.moneytransferapp.utils.widget

import android.content.Context
import android.util.AttributeSet
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.children
import androidx.core.widget.addTextChangedListener
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.databinding.SmsCodeInputNumberViewBinding
import com.smallworldfs.moneytransferapp.databinding.SmsCodeViewBinding
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.widget.listener.SmsCodeListener

class SmsCodeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : TextInputLayout(context, attrs, defStyleAttr) {

    var binding: SmsCodeViewBinding? = null
    var smsCodeListener: SmsCodeListener? = null
    var maxDigits = 0

    init {
        binding = SmsCodeViewBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.SmsCodeView, 0, 0
        ).apply {
            try {
                maxDigits = getInteger(R.styleable.SmsCodeView_maxDigits, 0)
            } finally {
                recycle()
            }
        }

        for (position in 0 until maxDigits) {
            setupEditText(position)
        }
    }

    private fun setupEditText(position: Int) {
        val textInputEditTextNumberBinding = SmsCodeInputNumberViewBinding.inflate(
            LayoutInflater.from(context),
            null,
            false
        )

        with(textInputEditTextNumberBinding.smsInputNumber) {
            setOnKeyListener { view, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                    handleDeletion(position, (view as TextInputEditText).text.toString())
                }
                return@setOnKeyListener false
            }

            addTextChangedListener {
                handleInput(position, it.toString())
            }

            setOnFocusChangeListener { _, hasFocus ->
                if (getCode().isEmpty() && position != 0 && hasFocus) {
                    val childText = binding?.codeContainer?.getChildAt(0) as? TextInputEditText
                    childText?.requestFocus()
                }
            }

            (parent as ViewGroup).removeView(this)
            binding?.codeContainer?.addView(this)
        }
    }

    fun getCode(): String {
        var code = STRING_EMPTY
        binding?.let {
            (it.codeContainer as ViewGroup).children.forEach { view ->
                code += (view as TextInputEditText).text
            }
        }
        return code
    }

    fun clearCode() {
        binding?.let {
            (it.codeContainer as ViewGroup).children.forEach { view ->
                (view as TextInputEditText).text?.clear()
            }
        }
    }

    private fun handleInput(position: Int, newText: String) {
        binding?.let {
            val viewGroup = it.codeContainer as ViewGroup

            val currentChild = viewGroup.getChildAt(position)
            val currentChildTextInputEditText = currentChild as TextInputEditText

            if (newText != STRING_EMPTY) {
                val nextChild = viewGroup.getChildAt(position + 1)
                val nextChildText = nextChild as? TextInputEditText

                if (nextChild == null) {
                    currentChildTextInputEditText.setSelection(newText.length)
                } else {
                    nextChildText?.requestFocus()
                }
            }

            checkCodeComplete(viewGroup)
        }
    }

    private fun handleDeletion(position: Int, currentText: String) {
        binding?.let {
            val viewGroup = it.codeContainer as ViewGroup

            if (currentText.isEmpty()) {
                val previousChild = viewGroup.getChildAt(position - 1)
                val previousChildText = previousChild as? TextInputEditText

                previousChildText?.apply {
                    setSelection(text?.length ?: 0)
                    requestFocus()
                }
            }

            checkCodeComplete(viewGroup)
        }
    }

    private fun checkCodeComplete(viewGroup: ViewGroup) {
        val isCodeCompleted = viewGroup.children.firstOrNull { (it as? TextInputEditText)?.text.isNullOrEmpty() } == null
        smsCodeListener?.onCodeComplete(isCodeCompleted)
    }

    fun showError() {
        changeColorText(ContextCompat.getColor(context, R.color.colorRedError))
    }

    fun hideError() {
        changeColorText(ContextCompat.getColor(context, android.R.color.tab_indicator_text))
    }

    private fun changeColorText(tintColor: Int) {
        binding?.let {
            (it.codeContainer as ViewGroup).children.forEach { view ->
                (view as TextInputEditText).background.setTint(tintColor)
            }
        }
    }
}
