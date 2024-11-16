package com.smallworldfs.moneytransferapp.presentation.quicklogin

import android.os.Bundle
import androidx.activity.viewModels
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.EventObserver
import com.smallworldfs.moneytransferapp.databinding.ActivityQuickLoginBinding
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.presentation.quicklogin.models.BiometricAuthenticatorUIModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class QuickLoginActivity : GenericActivity() {

    private val viewModel: QuickLoginViewModel by viewModels()

    @Inject
    lateinit var navigator: QuickLoginNavigator

    @Inject
    lateinit var biometricAuthenticator: BiometricAuthenticator

    private var _binding: ActivityQuickLoginBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityQuickLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.getCurrentUserEmail()
        setupView()
        setUpObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setUpObservers() {
        viewModel.biometricsEnabled.observe(
            this,
            EventObserver { isEnabled ->
                if (isEnabled) navigator.navigateToPassCodeWithBiometrics()
            }
        )

        viewModel.setBiometricsError.observe(
            this,
            EventObserver {
            }
        )

        viewModel.userEmail.observe(
            this,
            EventObserver {
                setupBiometricClickListener()
            }
        )
    }

    private fun setupView() {
        setupSkipClickListener()
    }

    private fun setupSkipClickListener() {
        viewModel.setBiometricsDisabled()
        binding.skipForNow.setOnClickListener {
            navigator.navigateToHomeActivity()
        }
    }

    private fun setupBiometricClickListener() {
        binding.createQuickLogin.setOnClickListener {
            val model = BiometricAuthenticatorUIModel(
                getString(R.string.scan_your_fingerprint),
                viewModel.userEmail.value!!.peekContent(),
                getString(R.string.fingerprint_scan_text),
                getString(R.string.cancel).toUpperCase()
            )
            biometricAuthenticator.checkBiometrics(
                model,
                onSuccess = {
                    registerEvent("click_associate_fingerprint")
                    viewModel.setBiometricsEnabled()
                },
                onError = {
                    viewModel.setBiometricsDisabled()
                    navigator.navigateToPassCode()
                },
                onCancelled = {
                    registerEvent("click_cancel")
                    navigator.navigateToPassCode()
                }
            )
        }
    }

    private fun registerEvent(eventAction: String, formType: String = "") {
        trackEvent(
            UserActionEvent(
                ScreenCategory.ACCESS.value,
                eventAction,
                "",
                getHierarchy(""),
                formType
            ),
        )
    }
}
