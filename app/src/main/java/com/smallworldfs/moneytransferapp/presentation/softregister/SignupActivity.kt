package com.smallworldfs.moneytransferapp.presentation.softregister

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventName
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventProperty
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserProperty
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserPropertyName
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.hide
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.show
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showInfoDoubleActionGeneralDialog
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.EventObserver
import com.smallworldfs.moneytransferapp.databinding.ActivitySignupBinding
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.softregister.SignupPagerAdapter.Companion.REGISTER_DATA_PAGE
import com.smallworldfs.moneytransferapp.presentation.softregister.SignupPagerAdapter.Companion.REGISTER_PHONE_PAGE
import com.smallworldfs.moneytransferapp.presentation.softregister.SignupPagerAdapter.Companion.REGISTER_USER_CREDENTIALS_PAGE
import com.smallworldfs.moneytransferapp.presentation.softregister.model.RegisterStep
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignupActivity : GenericActivity() {

    companion object {
        const val STEP = "STEP"
    }

    private val viewModel: SignupViewModel by viewModels()

    @Inject
    lateinit var navigator: SignupNavigator

    private var _binding: ActivitySignupBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupView()
        setUpObservers()
        viewModel.getCountriesData()
        intent.extras?.getParcelable<RegisterStep>(STEP)?.let {
            checkRegisterStep(it)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setUpObservers() {
        viewModel.userRegisterAlreadyStarted.observe(
            this,
            EventObserver {
                updateUserProperties(
                    listOf(
                        UserProperty(UserPropertyName.USER_ID, it.user.id),
                        UserProperty(UserPropertyName.ORIGIN, it.user.country.countries.firstOrNull()?.iso3 ?: STRING_EMPTY)
                    )
                )
                registerEvent(
                    "formOk",
                    "register",
                    hierarchy = ScreenName.FORM_SOFT_REGISTER_SCREEN.value,
                    origin = it.user.country.countries.firstOrNull()?.iso3 ?: STRING_EMPTY
                )
                checkRegisterStep(it.step)
            }
        )

        viewModel.registerCompleted.observe(
            this,
            EventObserver { addressType ->
                registerEvent(eventAction = "formOk", formType = "register", hierarchy = ScreenName.PROFILE_SCREEN.value, origin = viewModel.selectedCountry.value?.peekContent()?.iso3 ?: STRING_EMPTY, addressType = addressType)
                trackEvent(
                    BrazeEvent(
                        BrazeEventName.FULL_REGISTRATION.value,
                        mapOf(
                            BrazeEventProperty.REGISTER_COUNTRY.value to (viewModel.selectedCountry.value?.peekContent()?.iso3 ?: STRING_EMPTY)
                        )
                    )
                )
                navigator.navigateToQuickLoginActivity()
            }
        )

        viewModel.loading.observe(
            this,
            EventObserver { loading ->
                if (loading) binding.loader.show() else binding.loader.hide()
            }
        )

        viewModel.navigateToLoginScreen.observe(
            this,
            EventObserver {
                navigator.navigateToLoginActivity()
            }
        )

        viewModel.activityGenericError.observe(
            this,
            EventObserver {
                handleActivityErrorType(it)
            }
        )

        viewModel.navigateToProfileStep.observe(
            this,
            EventObserver {
                registerEvent(
                    "formOk",
                    "profile_access",
                    hierarchy = ScreenName.PROFILE_SCREEN.value,
                    origin = viewModel.user?.country?.countries?.firstOrNull()?.iso3 ?: STRING_EMPTY
                )
                setRegisterDataPage()
            }
        )
    }

    private fun setupView() {
        setupToolbar()
        with(binding.signupViewPager) {
            isUserInputEnabled = false
            adapter = SignupPagerAdapter(this@SignupActivity)
            offscreenPageLimit = 2
        }

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() { }
            }
        )
    }

    private fun checkRegisterStep(step: RegisterStep) {
        when (step) {
            is RegisterStep.RegisterPhone -> setRegisterPhonePage()
            is RegisterStep.RegisterData ->
                this@SignupActivity.showInfoDoubleActionGeneralDialog(
                    getString(R.string.welcome_back),
                    getString(R.string.welcome_back_error_message),
                    getString(R.string.continue_text_button),
                    { setRegisterDataPage() },
                    getString(R.string.start_again),
                    { setRegisterPhonePage() }
                )
            else -> setCredentialsPage()
        }
    }

    private fun setCredentialsPage() {
        binding.signupViewPager.setCurrentItem(REGISTER_USER_CREDENTIALS_PAGE, false)
        binding.signupTablayout.setSelectedStep(REGISTER_USER_CREDENTIALS_PAGE + 1)
    }

    private fun setRegisterPhonePage() {
        binding.signupViewPager.setCurrentItem(REGISTER_PHONE_PAGE, false)
        binding.signupTablayout.setSelectedStep(REGISTER_PHONE_PAGE + 1)
    }

    private fun setRegisterDataPage() {
        binding.signupViewPager.setCurrentItem(REGISTER_DATA_PAGE, false)
        binding.signupTablayout.setSelectedStep(REGISTER_DATA_PAGE + 1)
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.signupToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.signupToolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_action_close_white)
    }

    private fun handleActivityErrorType(errorType: ErrorType) {
        when (errorType) {
            is ErrorType.SecurityErrorInvalidIntegrity, ErrorType.SecurityErrorForbidden -> {
                showErrorView(
                    getString(R.string.generic_error_view_text),
                    getString(R.string.generic_error_view_subtitle),
                    binding.genericError.errorView
                )
            }
            else -> {
                showErrorView(
                    getString(R.string.generic_title_error),
                    errorType.message,
                    binding.genericError.errorView
                )
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        registerEvent("click_close", hierarchy = ScreenName.FORM_SOFT_REGISTER_SCREEN.value)
    }

    private fun registerEvent(eventAction: String, formType: String = "", hierarchy: String = "", origin: String = STRING_EMPTY, addressType: String = STRING_EMPTY) {
        val event = UserActionEvent(
            eventCategory = ScreenCategory.ACCESS.value,
            eventAction = eventAction,
            eventLabel = "",
            hierarchy = getHierarchy(hierarchy),
            formType = formType,
            origin = origin,
            addressType = addressType,
        )
        trackEvent(event)
    }
}
