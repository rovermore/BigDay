package com.smallworldfs.moneytransferapp.presentation.custom_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AnimationUtils
import androidx.constraintlayout.widget.ConstraintLayout
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.databinding.PassCodeKeyboardBinding

class PassCodeKeyboard @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var binding: PassCodeKeyboardBinding? = null

    private val passCode = CharArray(4)
    private val passCodeConfirmation = CharArray(4)
    private val circleList = mutableListOf<View>()
    private var counter = 0
    private var isConfirmMode = false
    private var isCreateMode = false
    private var isLoginMode = false

    private lateinit var passCodeResultListener: PassCodeResultListener

    init {
        binding = PassCodeKeyboardBinding.inflate(
            LayoutInflater.from(context),
            this,
            true
        )

        setupView()
    }

    fun setConfirmMode(confirm: Boolean) {
        isConfirmMode = confirm
    }

    fun setCreateMode(isCreateMode: Boolean) {
        this.isCreateMode = isCreateMode
    }

    fun setPassCodeResultListener(passCodeResultListener: PassCodeResultListener) {
        this.passCodeResultListener = passCodeResultListener
    }

    fun setLoginMode() {
        isLoginMode = true
        circleList.forEach {
            it.background = resources.getDrawable(R.drawable.background_green_border_light_button, null)
        }
    }

    /**
     * Public getters.
     */
    fun getConfirmMode(): Boolean = isConfirmMode

    /**
     * Private functions.
     */
    private fun setupView() {
        setCircleViewList()
        setupKeyboardView()
    }

    private fun setCircleViewList() {
        binding?.let { binding ->
            circleList.apply {
                add(binding.circle0)
                add(binding.circle1)
                add(binding.circle2)
                add(binding.circle3)
            }
        }
    }

    private fun setupKeyboardView() {
        binding?.let { binding ->
            binding.button1.setOnClickListener {
                addDigitToPassWord('1')
            }
            binding.button2.setOnClickListener {
                addDigitToPassWord('2')
            }
            binding.button3.setOnClickListener {
                addDigitToPassWord('3')
            }
            binding.button4.setOnClickListener {
                addDigitToPassWord('4')
            }
            binding.button5.setOnClickListener {
                addDigitToPassWord('5')
            }
            binding.button6.setOnClickListener {
                addDigitToPassWord('6')
            }
            binding.button7.setOnClickListener {
                addDigitToPassWord('7')
            }
            binding.button8.setOnClickListener {
                addDigitToPassWord('8')
            }
            binding.button9.setOnClickListener {
                addDigitToPassWord('9')
            }
            binding.button0.setOnClickListener {
                addDigitToPassWord('0')
            }
            binding.delete.setOnClickListener {
                removeDigitToPassWord()
            }
        }
    }

    private fun addDigitToPassWord(digit: Char) {
        if (counter < 4) {
            if (isConfirmMode) passCodeConfirmation[counter] = digit
            else passCode[counter] = digit
            counter += 1
            setDigitAsAddedInView(counter - 1)
            if (counter == 4) {
                if (isCreateMode) {
                    if (isConfirmMode)
                        checkPasscodes()
                    else
                        switchToConfirmPassword()
                } else {
                    passCodeResultListener.onValidatePassCode(passCode)
                }
            }
        }
    }

    private fun removeDigitToPassWord() {
        if (counter in 1..4) {
            counter -= 1
            setDigitAsRemovedInView(counter)
            if (isConfirmMode) passCodeConfirmation[counter] = ' '
            else passCode[counter] = ' '
        }
    }

    private fun setDigitAsAddedInView(circleNumber: Int) {
        circleList[circleNumber].background =
            if (isConfirmMode || isLoginMode) resources.getDrawable(R.drawable.background_green_button, null)
            else resources.getDrawable(R.drawable.background_blue_light_button, null)
    }

    private fun setDigitAsRemovedInView(circleNumber: Int) {
        circleList[circleNumber].background =
            if (isConfirmMode || isLoginMode) resources.getDrawable(R.drawable.background_green_border_button, null)
            else resources.getDrawable(R.drawable.background_blue_form_border_light_button, null)
    }

    private fun switchToConfirmPassword() {
        counter = 0
        isConfirmMode = true
        setCorrectView()
    }

    private fun checkPasscodes() {
        if (passCode.contentEquals(passCodeConfirmation)) {
            passCodeResultListener.onSavePassCode(passCode)
        } else {
            showPasscodeError()
            passCodeResultListener.onPassCodeError("Not matching passcodess")
        }
    }

    fun showPasscodeError() {
        binding?.circlesContainer
            ?.startAnimation(
                AnimationUtils.loadAnimation(
                    context,
                    R.anim.shake
                )
            )
        clearDigits()
    }

    private fun clearDigits() {
        circleList.forEach {
            it.background = resources.getDrawable(R.drawable.background_green_border_light_button, null)
        }

        for (index in 0 until counter) {
            if (isConfirmMode) passCodeConfirmation[index] = ' '
            else passCode[index] = ' '
        }

        counter = 0
    }

    private fun setCorrectView() {
        for (circle in circleList) {
            circle.background =
                if (isConfirmMode) resources.getDrawable(R.drawable.background_green_border_button, null)
                else resources.getDrawable(R.drawable.background_blue_form_border_light_button, null)
        }
    }

    fun releasePassCodes() {
        for (i in 0..3) {
            passCode[i] = ' '
            passCodeConfirmation[i] = ' '
        }
        counter = 0
        setCorrectView()
    }

    /**
     * Public Interface.
     */
    interface PassCodeResultListener {
        fun onValidatePassCode(passCode: CharArray)
        fun onSavePassCode(passCode: CharArray)
        fun onPassCodeError(error: String)
        fun onDismiss()
    }
}
