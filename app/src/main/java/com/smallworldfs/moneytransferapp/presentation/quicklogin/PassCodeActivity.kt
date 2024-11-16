package com.smallworldfs.moneytransferapp.presentation.quicklogin

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.EventObserver
import com.smallworldfs.moneytransferapp.databinding.ActivityPassCodeBinding
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.custom_views.PassCodeKeyboard
import com.smallworldfs.moneytransferapp.presentation.passwordconfirm.PasswordConfirmActivity
import com.smallworldfs.moneytransferapp.utils.gone
import com.smallworldfs.moneytransferapp.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class PassCodeActivity : GenericActivity() {

    companion object {
        const val PASSCODE_ACTIVITY_KEY = "PASSCODE_ACTIVITY"
        const val REGISTER = "HARD_REGISTER"
        const val REGISTER_WITH_BIOMETRICS = "HARD_REGISTER_WITH_BIOMETRICS"
        const val LOGIN = "LOGIN"
        const val SPLASH = "SPLASH"
        const val CONFIRM_PASSCODE_FROM_SETTINGS = "CONFIRM_PASSCODE_FROM_SETTINGS"
        const val CREATE_PASSCODE_FROM_SETTINGS = "CREATE_PASSCODE_FROM_SETTINGS"
        const val CHANGE_PASSCODE_FROM_SETTINGS = "CHANGE_PASSCODE_FROM_SETTINGS"
    }

    private val viewModel: PassCodeViewModel by viewModels()

    @Inject
    lateinit var navigator: PassCodeNavigator

    private var _binding: ActivityPassCodeBinding? = null
    private val binding get() = _binding!!

    private var isCreateMode = false
    private var hasBiometrics = false
    private var showUsePassword = false

    private lateinit var source: String

    private val startValidatePasswordToModifyQL =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                navigator.finishWithSuccess()
            } else {
                navigator.finishWithError()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPassCodeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        source = intent.extras?.getString(PASSCODE_ACTIVITY_KEY).toString()
        when (source) {
            REGISTER -> isCreateMode = true

            REGISTER_WITH_BIOMETRICS -> {
                isCreateMode = true
                hasBiometrics = true
            }
            LOGIN -> isCreateMode = true

            CREATE_PASSCODE_FROM_SETTINGS -> isCreateMode = true

            CONFIRM_PASSCODE_FROM_SETTINGS,
            CHANGE_PASSCODE_FROM_SETTINGS -> showUsePassword = true
        }

        setupObservers()
        setupView()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupObservers() {
        viewModel.passcodeSaved.observe(
            this,
            EventObserver {
                when (source) {
                    LOGIN,
                    REGISTER,
                    REGISTER_WITH_BIOMETRICS -> navigator.navigateToHome()

                    CHANGE_PASSCODE_FROM_SETTINGS,
                    CREATE_PASSCODE_FROM_SETTINGS -> navigator.finishWithSuccess()
                }
                binding.passCodeKeyboard.releasePassCodes()
            },
        )

        viewModel.passcodeValidated.observe(
            this,
            EventObserver { access ->
                when (source) {
                    LOGIN -> if (access) registerEvent("formOk", "", "login")
                    SPLASH -> navigator.navigateToAutoLogin()

                    CONFIRM_PASSCODE_FROM_SETTINGS -> navigator.finishWithSuccess()

                    CHANGE_PASSCODE_FROM_SETTINGS -> {
                        isCreateMode = true
                        binding.passCodeKeyboard.releasePassCodes()
                        setupView()
                    }

                    else -> navigator.navigateToAutoLogin()
                }
                binding.passCodeKeyboard.releasePassCodes()
            },
        )

        viewModel.passcodeReseted.observe(
            this,
            EventObserver {
                navigator.navigateToLogin()
            },
        )

        viewModel.passcodeError.observe(
            this,
            EventObserver {
                checkError(it)
            },
        )
    }

    private fun setupView() {
        setPassCodeKeyboard()
        setTitle()
        if (showUsePassword) {
            binding.usePassword.apply {
                visible()
                setOnClickListener {
                    val intent = Intent(this@PassCodeActivity, PasswordConfirmActivity::class.java).apply {
                        if (source == CHANGE_PASSCODE_FROM_SETTINGS)
                            putExtra(
                                PASSCODE_ACTIVITY_KEY,
                                CHANGE_PASSCODE_FROM_SETTINGS
                            )
                        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
                    }
                    startValidatePasswordToModifyQL.launch(intent)
                }
            }
        }
    }

    private fun setTitle() {
        if (source.isEmpty() || source != SPLASH) {
            setSupportActionBar(binding.toolbar)
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            if (isCreateMode) {
                if (binding.passCodeKeyboard.getConfirmMode())
                    supportActionBar?.title =
                        resources.getString(R.string.passcode_screen_title_confirm)
                else
                    supportActionBar?.title =
                        resources.getString(R.string.passcode_screen_title_create)
            } else
                supportActionBar?.title = resources.getString(R.string.passcode_screen_title_login)
        } else {
            binding.toolbar.gone()
        }
        if (isCreateMode) {
            if (hasBiometrics)
                binding.passcodeText.text = getString(R.string.passcode_text_create_from_fingerprint)
            else
                binding.passcodeText.text = getString(R.string.passcode_text_create)
        } else {
            binding.passcodeText.text = getString(R.string.passcode_text_login)
        }
    }

    private fun setPassCodeKeyboard() {
        binding.passCodeKeyboard.apply {
            setCreateMode(isCreateMode)
            setPassCodeResultListener(
                object : PassCodeKeyboard.PassCodeResultListener {
                    override fun onValidatePassCode(passCode: CharArray) {
                        validatePassCode(passCode)
                    }

                    override fun onSavePassCode(passCode: CharArray) {
                        registerEvent("formOk", "", "login")
                        savePassCode(passCode)
                    }

                    override fun onPassCodeError(error: String) {
                        registerEvent("formKo", error, "login")
                    }

                    override fun onDismiss() {
                        registerEvent("click_cancel", "", "")
                    }
                })
        }
    }

    private fun savePassCode(passCode: CharArray) {
        viewModel.savePasscode(passCode)
    }

    private fun validatePassCode(passCode: CharArray) {
        viewModel.validatePasscode(passCode)
    }

    private fun resetPassCode() {
        viewModel.resetPasscode()
    }

    private fun checkError(error: ErrorType) {
        when (error) {
            is ErrorType.WrongPasscodeError -> {
                registerEvent("formKo", "error_validation_pass", "login")
                binding.passCodeKeyboard.showPasscodeError()
            }
            else -> {
                registerEvent("formKo", error.message, "login")
                showErrorView(
                    getString(R.string.generic_title_error),
                    error.message,
                    binding.genericError.errorView,
                )
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id: Int = item.itemId
        if (id == android.R.id.home) {
            if (binding.passCodeKeyboard.getConfirmMode()) {
                binding.passCodeKeyboard.setConfirmMode(false)
                setTitle()
            } else {
                super.onBackPressed()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerEvent(eventAction: String, eventLabel: String, formType: String) {
        trackEvent(
            UserActionEvent(
                ScreenCategory.ACCESS.value,
                eventAction,
                eventLabel,
                getHierarchy(""),
                formType
            ),
        )
    }
}
