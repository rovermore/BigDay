package com.smallworldfs.moneytransferapp.presentation.softregister.phone

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.activityViewModels
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.AnalyticsSender
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventName
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventProperty
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventType
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showSingleActionInfoDialog
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.EventObserver
import com.smallworldfs.moneytransferapp.databinding.FragmentRegisterPhoneBinding
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericFragment
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel
import com.smallworldfs.moneytransferapp.presentation.common.countries.SearchCountryActivity
import com.smallworldfs.moneytransferapp.presentation.softregister.SignupViewModel
import com.smallworldfs.moneytransferapp.presentation.softregister.model.PhoneRegisterStep
import com.smallworldfs.moneytransferapp.presentation.softregister.model.SendPhone
import com.smallworldfs.moneytransferapp.presentation.softregister.model.SmsAttempt
import com.smallworldfs.moneytransferapp.presentation.softregister.model.VerificationCompleted
import com.smallworldfs.moneytransferapp.presentation.softregister.model.VerifyCode
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.gone
import com.smallworldfs.moneytransferapp.utils.visible
import com.smallworldfs.moneytransferapp.utils.widget.listener.SmsCodeListener
import com.smallworldfs.moneytransferapp.utils.widget.timer.sms.SWTimer
import com.smallworldfs.moneytransferapp.utils.widget.timer.sms.SWTimerFragmentLifecycleCallback
import com.smallworldfs.moneytransferapp.utils.widget.timer.sms.SWTimerProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.form_group_phone.view.*
import javax.inject.Inject

@AndroidEntryPoint
class RegisterPhoneFragment : GenericFragment(), SWTimerProvider {

    companion object {
        const val PHONE_CODE_VIEW = "PHONE_CODE_VIEW"
        const val SAME_PREFIX_THAN_ORIGIN_COUNTRY = "same"
    }

    @Inject
    lateinit var analyticsSender: AnalyticsSender

    private val viewModelActivity: SignupViewModel by activityViewModels()

    private var _binding: FragmentRegisterPhoneBinding? = null
    private val binding get() = _binding!!

    private var code = STRING_EMPTY
    private var phoneRegisterStep: PhoneRegisterStep = SendPhone

    private var countryPrefix: CountryUIModel? = null

    private val startSearch =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val selectedCountry = it.data!!.getParcelableExtra<CountryUIModel>(
                    SearchCountryActivity.SELECTED_COUNTRY_KEY,
                )!!
                viewModelActivity.updateSelectedPhoneCountry(selectedCountry)
                registerEvent("click_country_calling_code", "${selectedCountry.iso3}(${selectedCountry.prefix})", hierarchy = ScreenName.COUNTRY_CALLING_CODE.value)
                countryPrefix = selectedCountry
            }
        }

    private val retryCountDownTimer = SWTimer()

    private val swSmsTimerFragmentLifecycleCallback = SWTimerFragmentLifecycleCallback

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterPhoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        parentFragmentManager.registerFragmentLifecycleCallbacks(
            swSmsTimerFragmentLifecycleCallback,
            true,
        )
        setupView()
        setupObservers()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDestroy() {
        super.onDestroy()
        parentFragmentManager.unregisterFragmentLifecycleCallbacks(
            swSmsTimerFragmentLifecycleCallback,
        )
    }

    private fun setupObservers() {
        viewModelActivity.phoneRegisterSuccess.observe(
            viewLifecycleOwner,
            EventObserver { result ->
                if (result) {
                    registerEvent("formOk", eventLabel = checkPrefix(), "register")
                }
                setupVerifyCodeView()
            },
        )

        viewModelActivity.codeVerificationSuccess.observe(
            viewLifecycleOwner,
            EventObserver {
                setupVerificationCompletedView()
            },
        )

        viewModelActivity.selectedPhoneCountry.observe(
            viewLifecycleOwner,
            EventObserver {
                binding.phoneView.setCountry(it)
            },
        )

        viewModelActivity.timer.observe(
            viewLifecycleOwner,
            EventObserver {
                refreshTimer(it)
            },
        )

        viewModelActivity.registerPhoneError.observe(
            viewLifecycleOwner,
            EventObserver {
                if (phoneRegisterStep is VerifyCode) {
                    binding.codeLayout.showError()
                }
                showError(it)
            },
        )
    }

    private fun setupView() {
        binding.phoneView.setup(
            resources.getString(R.string.phone_number_text_input_layout_hint),
            { startPrefixSearchActivity() },
            { text ->
                setButtonClickable(text.isNotEmpty())
            },
        )

        with(binding.phoneActionButton) {
            setOnClickListener {
                if (isEnabled) {
                    when (phoneRegisterStep) {
                        is SendPhone -> {
                            registerEvent(
                                "click_send_code",
                                origin = countryPrefix?.iso3 ?: STRING_EMPTY,
                                eventLabel = checkPrefix()
                            )
                            val phoneNumber = binding.phoneView.formGroupPhoneTextInputLayoutEditText.text.toString().trim()
                            viewModelActivity.registerPhone(phoneNumber, binding.marketingCheckbox.isChecked)
                        }
                        is VerifyCode -> viewModelActivity.verifyCode(code)
                        is VerificationCompleted -> viewModelActivity.navigateToProfileStep()
                    }
                }
            }
        }

        binding.codeLayout.smsCodeListener = object : SmsCodeListener {
            override fun onCodeComplete(isCodeCompleted: Boolean) {
                setButtonClickable(isCodeCompleted)
                if (isCodeCompleted)
                    code = binding.codeLayout.getCode()
            }
        }

        binding.changePhoneNumber.setOnClickListener {
            registerEvent("click_change_phone_number", hierarchy = ScreenName.CONFIRM_SMS_CODE_SCREEN.value, eventLabel = checkPrefix())
            setupPhoneNumberView()
            showRetryButton(true)
        }

        binding.changePhoneNumber2.setOnClickListener {
            registerEvent("click_change_phone_number", hierarchy = ScreenName.CONFIRM_SMS_CODE_SCREEN.value, eventLabel = checkPrefix())
            setupPhoneNumberView()
            showRetryButton(true)
        }

        binding.smsSend.setOnClickListener {
            registerEvent("click_send_sms_again", hierarchy = ScreenName.CONFIRM_SMS_CODE_SCREEN.value, eventLabel = checkPrefix())
            val properties = HashMap<String, String>()

            properties[BrazeEventProperty.REGISTRATION_DESTINATION_COUNTRY.value] = viewModelActivity.selectedCountry.value?.peekContent()?.iso3 ?: STRING_EMPTY

            registerBrazeEvent(BrazeEventName.RESEND_SMS.value, properties)

            val phoneNumber = binding.phoneView.formGroupPhoneTextInputLayoutEditText.text.toString().trim()
            viewModelActivity.resendSMS(phoneNumber)
            binding.errorCodeTextView.gone()
        }

        binding.phoneView.formGroupPhoneTextInputLayoutEditText.setOnClickListener {
            registerEvent("click_edit_phone_number")
        }

        binding.marketingCheckbox.setOnCheckedChangeListener { _, isChecked ->
            registerEvent("sms_consent_usa", if (isChecked) "on" else "off")
        }
    }

    private fun checkUSASmsMarketing() {
        viewModelActivity.selectedCountry.value?.peekContent()?.iso3?.let { selectedCountry ->
            if (selectedCountry == "USA") {
                binding.marketingCheckbox.visible()
                binding.marketingText.visible()
            }
        }
    }

    private fun checkPrefix() =
        if (countryPrefix?.iso3 != null &&
            countryPrefix?.iso3!! == viewModelActivity.selectedCountry.value?.peekContent()?.iso3
        )
            countryPrefix!!.iso3
        else SAME_PREFIX_THAN_ORIGIN_COUNTRY

    override fun onResume() {
        super.onResume()
        trackScreen()
        checkUSASmsMarketing()
    }

    private fun refreshTimer(smsAttempt: SmsAttempt) {
        showRetryButton(false)

        val time = smsAttempt.time
        val attempt = smsAttempt.attempt

        val onTickCallback: (millisUntilFinished: Long) -> Unit = { millisUntilFinished ->
            val secs = (millisUntilFinished / 1000).toInt()
            when (attempt) {
                1 -> binding.smsResendMessage.text = resources.getString(R.string.sms_retry_count_message, secs)
                2 -> binding.smsResendMessage.text = resources.getString(R.string.sms_retry_count_second_message, secs)
                else -> binding.smsResendMessage.text = resources.getString(R.string.sms_retry_count_last_message)
            }
        }

        val onFinishCallback: () -> Unit = {
            showRetryButton(true)
        }

        retryCountDownTimer.launch(time, 1000, onTickCallback, onFinishCallback)
    }

    private fun showRetryButton(isShown: Boolean) {
        if (isShown) {
            retryCountDownTimer.cancel()
            binding.smsResendMessage.gone()
            binding.smsSend.tint(R.color.blue_background_welcome)
            binding.smsSend.isClickable = true
        } else {
            binding.smsSend.isClickable = false
            binding.smsSend.tint(R.color.colorGrayLight2)
            binding.smsResendMessage.visible()
        }
    }

    private fun startPrefixSearchActivity() {
        registerEvent("click_edit_phone_country_code")
        val intent = Intent(context, SearchCountryActivity::class.java).apply {
            putExtra(
                SearchCountryActivity.TITLE_KEY,
                resources.getString(R.string.country_calling_code),
            )
            putExtra(
                SearchCountryActivity.TYPE,
                PHONE_CODE_VIEW
            )
            val countries = ArrayList(
                viewModelActivity.countries.value?.peekContent()
                    ?: emptyList(),
            )
            putParcelableArrayListExtra(SearchCountryActivity.COUNTRIES_KEY, countries)
        }
        startSearch.launch(intent)
    }

    private fun setButtonClickable(isClickable: Boolean) {
        binding.phoneActionButton.isEnabled = isClickable
    }

    private fun setupVerificationCompletedView() {
        setButtonClickable(true)
        phoneRegisterStep = VerificationCompleted
        binding.codeLayout.gone()
        binding.smsSend.gone()
        binding.changePhoneNumber.gone()
        binding.smsResendMessage.gone()
        binding.completedIcon.visible()
        binding.completedText.visible()
        binding.changePhoneNumber2.visible()
        binding.errorCodeTextView.gone()
        binding.phoneActionButton.text = resources.getString(R.string.continue_text_button)
        binding.numberSection.text = String.format(
            resources.getString(R.string.verification_completed_text),
            binding.phoneView.formGroupPhoneTextInputLayoutEditText.text.toString(),
        )
        binding.marketingCheckbox.gone()
        binding.marketingText.gone()
        trackScreen()
    }

    private fun setupVerifyCodeView() {
        setButtonClickable(false)
        phoneRegisterStep = VerifyCode
        binding.phoneView.gone()
        binding.codeLayout.visible()
        binding.codeLayout.hideError()
        binding.smsSend.visible()
        binding.changePhoneNumber.visible()
        binding.phoneActionButton.text = resources.getString(R.string.next)
        binding.numberSection.text = resources.getText(R.string.code_text).toString()
            .plus(binding.phoneView.formGroupPhoneTextInputLayoutEditText.text.toString().trim())
        binding.marketingCheckbox.gone()
        binding.marketingText.gone()
        trackScreen()
    }

    private fun setupPhoneNumberView() {
        phoneRegisterStep = SendPhone
        binding.codeLayout.clearCode()
        binding.phoneView.visible()
        binding.codeLayout.gone()
        binding.smsSend.gone()
        binding.changePhoneNumber.gone()
        binding.completedIcon.gone()
        binding.completedText.gone()
        binding.changePhoneNumber2.gone()
        binding.errorCodeTextView.gone()
        binding.numberSection.text = resources.getText(R.string.verify_number)
        checkUSASmsMarketing()
        trackScreen()
    }

    private fun showError(errorType: ErrorType) {
        when (errorType) {
            is ErrorType.EntityValidationError -> {
                requireContext().showSingleActionInfoDialog(
                    title = getString(R.string.change_payment_method_dialog_text_title),
                    content = errorType.fields.firstOrNull()?.error?.firstOrNull() ?: "",
                    textButton = getString(R.string.change_number),
                    positiveAction = {},
                    showAlertIcon = true,
                )
            }

            is ErrorType.InvalidPhoneCode -> {
                binding.errorCodeTextView.apply {
                    text = errorType.message
                    visible()
                }
            }

            else -> {
                viewModelActivity.showErrorGenericErrorView(errorType)
            }
        }
    }

    fun trackScreen() {
        when (phoneRegisterStep) {
            is SendPhone -> {
                super.trackScreen(ScreenName.PHONE_VALIDATION_SCREEN.value)
                super.trackScreenBraze(
                    ScreenName.PHONE_VALIDATION_SCREEN.value,
                    mapOf(
                        BrazeEventProperty.REGISTER_COUNTRY.value to (viewModelActivity.selectedCountry.value?.peekContent()?.iso3 ?: STRING_EMPTY)
                    )
                )
            }
            is VerifyCode -> {
                super.trackScreen(ScreenName.CONFIRM_SMS_CODE_SCREEN.value)
            }
            else -> {}
        }
    }

    override fun getSwTimer() = retryCountDownTimer

    private fun registerEvent(eventAction: String, eventLabel: String = "", formType: String = "", hierarchy: String = "", origin: String = "") {
        trackEvent(
            UserActionEvent(
                ScreenCategory.ACCESS.value,
                eventAction,
                eventLabel,
                getHierarchy(hierarchy),
                formType,
                origin = origin
            ),
        )
    }

    private fun registerBrazeEvent(eventName: String, eventProperties: Map<String, String>) {
        trackEvent(
            BrazeEvent(eventName, eventProperties, BrazeEventType.ACTION),
        )
    }
}
