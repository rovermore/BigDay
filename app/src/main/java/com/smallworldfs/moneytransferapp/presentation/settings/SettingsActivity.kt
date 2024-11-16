package com.smallworldfs.moneytransferapp.presentation.settings

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.presentation.quicklogin.BiometricAuthenticator
import com.smallworldfs.moneytransferapp.presentation.quicklogin.PassCodeActivity
import com.smallworldfs.moneytransferapp.presentation.quicklogin.models.BiometricAuthenticatorUIModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class SettingsActivity : GenericActivity(), SettingsCallbacks {

    @Inject
    lateinit var navigator: SettingsNavigator

    @Inject
    lateinit var biometricAuthenticator: BiometricAuthenticator

    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.getSettings()
        viewModel.getQuickLoginSettings()
        setContent {
            SettingsScreenContainer(listener = this)
        }
    }

    private fun registerEvent(eventAction: String, eventLabel: String = "") {
        trackEvent(
            UserActionEvent(
                ScreenCategory.DASHBOARD.value,
                eventAction,
                eventLabel,
                getHierarchy(""),
            ),
        )
    }

    private val startValidatePassCodeToDeactivateBiometrics =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                viewModel.disableBiometrics()
            }
        }

    private val startActivatePassCodeActivity =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) {
            viewModel.getQuickLoginSettings()
        }

    override fun onSettingsItemClicked(url: String) {
        navigator.navigateToOpenDocument(url)
    }

    override fun onBiometricSwitchToggle(isChecked: Boolean) {
        val subtitle = viewModel.settingsState.value.userInfoDTO.email
        val model = BiometricAuthenticatorUIModel(
            getString(R.string.scan_your_fingerprint),
            getString(R.string.fingerprint_scan_text),
            getString(R.string.cancel).uppercase(Locale.getDefault()),
            subtitle,
        )
        biometricAuthenticator.checkBiometrics(
            model,
            onSuccess = {
                if (isChecked) {
                    viewModel.enableBiometrics()
                    val intent = Intent(this@SettingsActivity, PassCodeActivity::class.java).apply {
                        putExtra(PassCodeActivity.PASSCODE_ACTIVITY_KEY, PassCodeActivity.CREATE_PASSCODE_FROM_SETTINGS)
                        overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left)
                    }
                    startActivatePassCodeActivity.launch(intent)
                } else {
                    viewModel.disableBiometrics()
                }
            },
            onError = { },
            onCancelled = {
                if (isChecked) {
                    val intent = Intent(this@SettingsActivity, PassCodeActivity::class.java).apply {
                        putExtra(PassCodeActivity.PASSCODE_ACTIVITY_KEY, PassCodeActivity.CONFIRM_PASSCODE_FROM_SETTINGS)
                        overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left)
                    }
                    startValidatePassCodeToDeactivateBiometrics.launch(intent)
                }
            },
        )
    }

    override fun onNotificationSwitchToggle(isChecked: Boolean) {
        if (isChecked) {
            registerEvent("toggle_notifications", "on")
        } else {
            registerEvent("toggle_notifications", "off")
        }
        viewModel.updateNotificationsState(isChecked)
    }

    override fun onChangePasscodeSwitchToggle(isChecked: Boolean) {
        if (isChecked) {
            val intent = Intent(this@SettingsActivity, PassCodeActivity::class.java).apply {
                putExtra(PassCodeActivity.PASSCODE_ACTIVITY_KEY, PassCodeActivity.CREATE_PASSCODE_FROM_SETTINGS)
                overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left)
            }
            startActivatePassCodeActivity.launch(intent)
        } else {
            viewModel.disablePassCode()
        }
    }

    override fun onClosedIconClicked() {
        viewModel.removeBiometricError()
    }

    override fun onBackButtonPressed() {
        onBackPressedDispatcher.onBackPressed()
        registerEvent("click_back")
        overridePendingTransition(R.anim.from_left_to_right, R.anim.from_position_to_right)
        finish()
    }
}
