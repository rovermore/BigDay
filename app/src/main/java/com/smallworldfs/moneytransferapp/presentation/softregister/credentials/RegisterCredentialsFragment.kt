package com.smallworldfs.moneytransferapp.presentation.softregister.credentials

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.InputType
import android.text.method.LinkMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.isEmpty
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showDoubleActionGeneralDialog
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showInfoDialog
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showSingleActionInfoDialog
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.EventObserver
import com.smallworldfs.moneytransferapp.databinding.FragmentRegisterCredentialsBinding
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericFragment
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Attributes
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel
import com.smallworldfs.moneytransferapp.presentation.common.countries.SearchCountryActivity
import com.smallworldfs.moneytransferapp.presentation.common.countries.SearchStatesActivity
import com.smallworldfs.moneytransferapp.presentation.common.countries.StateUIModel
import com.smallworldfs.moneytransferapp.presentation.softregister.SignupViewModel
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.gone
import com.smallworldfs.moneytransferapp.utils.visible
import com.smallworldfs.moneytransferapp.utils.widget.RegionPickerUIModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterCredentialsFragment : GenericFragment() {

    private val viewModelActivity: SignupViewModel by activityViewModels()

    private var _binding: FragmentRegisterCredentialsBinding? = null
    private val binding get() = _binding!!

    var isStrongPassword = false

    private var country: CountryUIModel? = null

    private val startCountrySearch =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val selectedCountry = it.data!!.getParcelableExtra<CountryUIModel>(
                    SearchCountryActivity.SELECTED_COUNTRY_KEY,
                )!!
                setCountry(selectedCountry)
                viewModelActivity.updateSelectedCountry(selectedCountry)
            }
        }

    private val startStateSearch =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val selectedState = it.data!!.getParcelableExtra<StateUIModel>(SearchStatesActivity.SELECTED_STATE_KEY)!!
                binding.stateView.setRegion(RegionPickerUIModel(selectedState.code, selectedState.name, selectedState.logo))
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterCredentialsBinding.inflate(inflater, container, false)
        setupObservers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        trackScreen(ScreenName.FORM_SOFT_REGISTER_SCREEN.value)
        trackScreenBraze(ScreenName.FORM_SOFT_REGISTER_SCREEN.value, emptyMap())
        setupView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupObservers() {
        viewModelActivity.states.observe(
            viewLifecycleOwner,
            EventObserver {
            },
        )

        viewModelActivity.selectedCountry.observe(
            viewLifecycleOwner,
            EventObserver {
                setCountry(it)
            },
        )

        viewModelActivity.registerCredentialsError.observe(
            viewLifecycleOwner,
            EventObserver {
                registerEvent("formKo", "error_validation_email", "register")
                showError(it)
            },
        )
    }

    private fun setupView() {
        val attributes = Attributes()
        with(attributes) {
            min = "8"
            max = "20"
            group = "1"
            recommended = "1"
            isAlphaNum = true
            isSpecialChar = true
            isUpperCase = true
            textRequirements = "Your password must have at least 8 characters, one upper case letter, one  number and one special character"
        }

        with(binding.termsText) {
            movementMethod = LinkMovementMethod.getInstance()
            setLinkTextColor(Color.BLUE)
        }

        with(binding.privacyText) {
            movementMethod = LinkMovementMethod.getInstance()
            setLinkTextColor(Color.BLUE)
        }

        with(binding.usaPrivacyText) {
            movementMethod = LinkMovementMethod.getInstance()
            setLinkTextColor(Color.BLUE)
        }

        PasswordViewUtils(
            binding.passwordView, attributes,
            object : PasswordViewUtils.StrongPasswordReached {

                override fun onStrongPasswordReached() {
                    isStrongPassword = true
                    setSubmitButtonColor()
                }

                override fun onWeakPasswordAdded() {
                    isStrongPassword = false
                    setSubmitButtonColor()
                }
            },
        )

        with(binding.emailView) {
            input.hint = resources.getString(R.string.email)
            inputEdit.contentDescription = "emailField"
            inputEdit.inputType = InputType.TYPE_CLASS_TEXT or
                InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            inputEdit.doOnTextChanged { _, _, _, _ ->
                setSubmitButtonColor()
            }
        }

        with(binding.countryView) {
            setHint(
                binding?.root?.context?.resources?.getString(
                    R.string.send_money_from,
                ) ?: STRING_EMPTY
            )
            setButtonClickListener() {
                registerEvent("click_select_country_send_money_from")
                val intent = Intent(context, SearchCountryActivity::class.java).apply {
                    putExtra(SearchCountryActivity.TITLE_KEY, resources.getString(R.string.sending_from_search_title))
                    val countries = ArrayList(
                        viewModelActivity.originCountries.value?.peekContent()
                            ?: emptyList(),
                    )
                    putParcelableArrayListExtra(SearchCountryActivity.COUNTRIES_KEY, countries)
                }
                startCountrySearch.launch(intent)
            }
        }

        with(binding.stateView) {
            setRegion(
                RegionPickerUIModel(
                    STRING_EMPTY,
                    STRING_EMPTY,
                    STRING_EMPTY
                )
            )
            showProgressBar(false)
            setHint(
                resources?.getString(
                    R.string.select_state_activity_title,
                ) ?: STRING_EMPTY
            )
            setButtonClickListener() {
                val intent = Intent(context, SearchStatesActivity::class.java).apply {
                    putExtra(SearchStatesActivity.TITLE_KEY, resources.getString(R.string.sending_from_search_title))
                    val states = ArrayList(
                        viewModelActivity.states.value?.peekContent()
                            ?: emptyList(),
                    )
                    putParcelableArrayListExtra(SearchStatesActivity.STATES_KEY, states)
                }
                startStateSearch.launch(intent)
            }
        }

        binding.submitCredentials.setOnClickListener {
            if (it.isEnabled) {
                registerEvent("click_sign_up", origin = country?.iso3 ?: STRING_EMPTY)
                viewModelActivity.registerUserCredentials(
                    binding.emailView.inputEdit.text.toString(),
                    binding.countryView.getText(),
                    binding.passwordView.inputEdit.text.toString().toCharArray(),
                    binding.stateView.getText(),
                    binding.marketingCheckbox.isChecked,
                    binding.privacyCheckbox.isChecked,
                    binding.privacyCheckbox.isChecked,
                )
            }
        }

        with(binding.privacyCheckbox) {
            setOnClickListener {
                setSubmitButtonColor()
            }
        }
    }

    private fun setSubmitButtonColor() {
        with(binding.submitCredentials) {
            isEnabled = areMandatoryFieldsFilled()
        }
    }

    private fun areMandatoryFieldsFilled() =
        !binding.emailView.inputEdit.isEmpty() &&
            binding.privacyCheckbox.isChecked &&
            isStrongPassword &&
            binding.countryView.getText().isNotBlank()

    private fun setCountry(selectedCountry: CountryUIModel) {
        if (selectedCountry.iso3 == "USA") {
            binding.notUsaFields.gone()
            binding.stateView.visible()
            binding.stateView.setContentDescription("state_text")
            setPrivacyTermsAndConditions(true)
        } else {
            binding.notUsaFields.visible()
            binding.stateView.gone()
            binding.stateView.setContentDescription("search_edit_text")
            setPrivacyTermsAndConditions(false)
        }
        with(binding.countryView) {
            setRegion(RegionPickerUIModel(selectedCountry.iso3, selectedCountry.name, selectedCountry.logo))
        }
    }

    private fun setPrivacyTermsAndConditions(isUSA: Boolean) {
        if (isUSA) {
            binding.usaPrivacyText.visible()
            binding.privacyText.gone()
        } else {
            binding.usaPrivacyText.gone()
            binding.privacyText.visible()
        }
    }

    private fun clearEmailAndPassword() {
        with(binding) {
            passwordView.inputEdit.text?.clear()
            emailView.inputEdit.text?.clear()
            emailView.inputEdit.requestFocus()
        }
    }

    private fun showError(errorType: ErrorType) {
        when (errorType) {
            is ErrorType.ExistingAccount ->
                requireContext().showDoubleActionGeneralDialog(
                    getString(R.string.change_payment_method_dialog_text_title),
                    errorType.message,
                    getString(R.string.change_email),
                    { clearEmailAndPassword() },
                    getString(R.string.sign_in),
                    { viewModelActivity.navigateToLoginScreen() },
                )

            is ErrorType.EmailScoreNotPassed ->
                requireContext().showSingleActionInfoDialog(
                    getString(R.string.change_payment_method_dialog_text_title),
                    errorType.message,
                    getString(R.string.change_email),
                    { clearEmailAndPassword() },
                    true,
                )
            is ErrorType.UserNotAvailable ->
                requireContext().showSingleActionInfoDialog(
                    title = getString(R.string.existing_user),
                    content = errorType.message,
                    textButton = getString(R.string.change_email),
                    showAlertIcon = true,
                    positiveAction = { clearEmailAndPassword() },
                )

            is ErrorType.StateBlocked ->
                requireContext().showInfoDialog(
                    getString(R.string.change_payment_method_dialog_text_title),
                    errorType.message,
                )

            else -> viewModelActivity.showErrorGenericErrorView(errorType)
        }
    }

    private fun registerEvent(eventAction: String, eventLabel: String = "", formType: String = "", origin: String = "") {
        trackEvent(
            UserActionEvent(
                ScreenCategory.ACCESS.value,
                eventAction,
                eventLabel,
                getHierarchy(""),
                formType,
                origin = origin
            ),
        )
    }
}
