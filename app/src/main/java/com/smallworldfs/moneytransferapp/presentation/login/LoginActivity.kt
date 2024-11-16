package com.smallworldfs.moneytransferapp.presentation.login

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.PassCodeDialog
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventName
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventProperty
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserProperty
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserPropertyName
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.createPasscodeDialog
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showDoubleActionGeneralDialog
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showNoConnectionDialog
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.common.countries.LegacyCountrySelectedListener
import com.smallworldfs.moneytransferapp.presentation.custom_views.PassCodeKeyboard
import com.smallworldfs.moneytransferapp.presentation.form.adapter.FormNavigator
import com.smallworldfs.moneytransferapp.presentation.login.model.UserUIModel
import com.smallworldfs.moneytransferapp.presentation.quicklogin.BiometricAuthenticator
import com.smallworldfs.moneytransferapp.presentation.quicklogin.models.BiometricAuthenticatorUIModel
import com.smallworldfs.moneytransferapp.utils.NewUtils
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity :
    GenericActivity(),
    LegacyCountrySelectedListener {
    companion object {
        const val AUTO_LOGIN = "auto_login"
        const val RESET_PASSWORD_TOKEN = "reset_password_token"
        const val SEND_MONEY_FROM = "SEND_MONEY_FROM"
    }

    @Inject
    lateinit var newUtilsDay: NewUtils

    private val viewModel: LoginViewModel by viewModels()

    @Inject
    lateinit var formNavigator: FormNavigator

    @Inject
    lateinit var loginNavigator: LoginNavigator

    @Inject
    lateinit var biometricAuthenticator: BiometricAuthenticator
    private lateinit var passCodeDialog: PassCodeDialog
    private var passCodeAttempts = 1
    private val maxCodeAttempts = 5

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        trackScreen(LoginActivity::class.java.simpleName)
        if (intent.extras?.getBoolean(AUTO_LOGIN, false) == true) {
            viewModel.performAutoLogin()
        } else {
            viewModel.getExistingUser()
        }
        formNavigator.setActivity(this)
        viewModel.getCountries()
        viewModel.getCurrentUserEmail()
        setContent {
            LoginScreen(greetingTextDay = newUtilsDay.getGreetingTextDay(), regionClickListeners = listener, registerEvent = { event -> registerEvent(event) }, loginActivityCallbacks = loginActivityCallbacks)
        }
        setupObservers()
    }

    private fun setupObservers() {
        lifecycleScope.launch {
            viewModel.passcodeValidated.collectLatest {
                if (it) {
                    registerEvent("formOk", "login", ScreenName.ENTER_PASSCODE.value)
                    passCodeDialog.dismiss()
                    viewModel.performAutoLogin()
                }
            }
        }

        lifecycleScope.launch {
            viewModel.passcodeError.collectLatest {
                if (it != ErrorType.None) {
                    if (passCodeAttempts >= maxCodeAttempts) {
                        passCodeDialog.dismiss()
                    } else {
                        passCodeDialog.onPassCodeError()
                        passCodeAttempts++
                    }
                }
            }
        }
    }

    private fun checkUserStatus(user: UserUIModel) {
        when {
            !user.isLimitedUser() && user.createPassCode -> {
                loginNavigator.navigateToPassCodeActivity()
            }

            user.isApprovedUser() -> {
                loginNavigator.navigateToHomeActivity()
            }

            else -> {
                loginNavigator.navigateToRegisterActivity()
            }
        }
    }

    private fun setupPasscodeAccess() {
        passCodeDialog = createPasscodeDialog(
            resources.getString(R.string.login_with_passcode),
            resources.getString(R.string.resume_session_with)
                .plus(viewModel.userEmail.value),
            object : PassCodeKeyboard.PassCodeResultListener {
                override fun onValidatePassCode(passCode: CharArray) {
                    viewModel.validatePasscode(passCode)
                }

                override fun onSavePassCode(passCode: CharArray) {
                }

                override fun onPassCodeError(error: String) {
                }

                override fun onDismiss() {
                    registerEvent("click_login_email", hierarchy = ScreenName.ENTER_PASSCODE.value)
                }
            },
        )
        passCodeDialog.setLoginMode()
        passCodeDialog.show()
    }

    fun registerEvent(eventAction: String, formType: String = "", hierarchy: String = "", origin: String = "") {
        trackEvent(
            UserActionEvent(
                ScreenCategory.ACCESS.value,
                eventAction,
                "",
                getHierarchy(hierarchy),
                formType,
                origin = origin,
            ),
        )
    }

    private fun registerBrazeEvent(eventName: String, eventProperties: Map<String, String>) {
        trackEvent(
            BrazeEvent(eventName, eventProperties),
        )
    }

    private val loginActivityCallbacks = object : LoginActivityCallbacks {
        override fun userLoggedIn(user: UserUIModel) {
            registerEvent("formOk", "login")

            val properties = HashMap<String, String>()
            properties[BrazeEventProperty.SENDING_COUNTRY.value] = user.country.countries.firstOrNull()?.iso3 ?: STRING_EMPTY
            registerBrazeEvent(
                BrazeEventName.LOGIN_OK.value,
                properties,
            )
            updateUserProperties(
                listOf(
                    UserProperty(UserPropertyName.USER_ID, user.id),
                    UserProperty(UserPropertyName.ORIGIN, user.country.countries.firstOrNull()?.iso3 ?: STRING_EMPTY),
                ),
            )
            checkUserStatus(user)
        }

        override fun navigateToRegisterActivity(countryIso: String) {
            registerEvent("click_sign_up", origin = countryIso)
            loginNavigator.navigateToRegisterActivity()
        }

        override fun navigateToForgotPasswordActivity() {
            loginNavigator.navigateToForgotPasswordActivity()
        }

        override fun onTryItHeaderClicked() {
            registerEvent("click_takeALook")
            loginNavigator.navigateToAppCustomizationActivity()
        }

        override fun setUpBiometricAccess() {
            registerEvent("click_associate_fingerprint")
            val model = BiometricAuthenticatorUIModel(
                getString(R.string.scan_your_fingerprint),
                viewModel.userEmail.value,
                getString(R.string.fingerprint_scan_text),
                getString(R.string.cancel).uppercase(Locale.getDefault()),
            )
            biometricAuthenticator.checkBiometrics(
                model,
                onSuccess = {
                    viewModel.performAutoLogin()
                },
                onCancelled = {
                    registerEvent("click_cancel")
                    setupPasscodeAccess()
                },
                onError = {
                    setupPasscodeAccess()
                },
            )
        }

        override fun setupPasscode() {
            setupPasscodeAccess()
        }

        override fun onError(errorType: ErrorType, iso3: String) {
            val properties = HashMap<String, String>()

            properties[BrazeEventProperty.SENDING_COUNTRY.value] = iso3
            properties[BrazeEventProperty.PROBLEM.value] = errorType.message

            registerBrazeEvent(BrazeEventName.LOGIN_KO.value, properties)

            if (errorType !is ErrorType.EntityValidationError) {
                registerEvent("formKo", "error_unable_to_login", "login")
                handleError(errorType)
            }
        }
    }

    private val listener = object : NewUserLoginScreenClickListeners {
        override fun onRegionClicked() {
            registerEvent("click_choose_country")
        }

        override fun onForgotPasswordClicked() {
            registerEvent("click_forgot_password")
            registerBrazeEvent(BrazeEventName.RESET_PASSWORD.value, emptyMap())
            loginNavigator.navigateToForgotPasswordActivity()
        }

        override fun onSubmitButtonClicked() {
            registerEvent("click_login")
        }

        override fun setFieldError(eventLabel: String) {
            registerEvent("formKo", eventLabel, "login")
        }
    }

    fun handleError(error: ErrorType) {
        when (error) {
            is ErrorType.ConnectionError -> showNoConnectionDialog { }
            is ErrorType.UserNotAvailable ->
                this.showDoubleActionGeneralDialog(
                    getString(R.string.change_payment_method_dialog_text_title),
                    error.message,
                    getString(R.string.change_email),
                    {},
                    getString(R.string.register),
                    { loginNavigator.navigateToRegisterActivity() },
                )

            is ErrorType.PartiallyRegisteredUser -> loginNavigator.navigateToRegisterActivity(error.step)
            is ErrorType.UnregisteredUser -> loginNavigator.navigateToRegisterActivity()
            is ErrorType.None -> {}
            else -> {
                viewModel.showErrorView()
            }
        }
    }
}

interface NewUserLoginScreenClickListeners {
    fun onRegionClicked()
    fun onForgotPasswordClicked()
    fun onSubmitButtonClicked()
    fun setFieldError(eventLabel: String)
}

interface LoginActivityCallbacks {
    fun userLoggedIn(user: UserUIModel)
    fun navigateToRegisterActivity(iso: String)
    fun navigateToForgotPasswordActivity()
    fun onTryItHeaderClicked()
    fun setUpBiometricAccess()
    fun setupPasscode()
    fun onError(errorType: ErrorType, iso3: String)
}
