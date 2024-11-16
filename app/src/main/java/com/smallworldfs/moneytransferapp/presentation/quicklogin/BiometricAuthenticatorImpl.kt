package com.smallworldfs.moneytransferapp.presentation.quicklogin

import android.hardware.biometrics.BiometricPrompt.BIOMETRIC_ERROR_CANCELED
import android.hardware.biometrics.BiometricPrompt.BIOMETRIC_ERROR_LOCKOUT
import android.hardware.biometrics.BiometricPrompt.BIOMETRIC_ERROR_LOCKOUT_PERMANENT
import android.hardware.biometrics.BiometricPrompt.BIOMETRIC_ERROR_TIMEOUT
import android.hardware.biometrics.BiometricPrompt.BIOMETRIC_ERROR_USER_CANCELED
import androidx.biometric.BiometricPrompt
import androidx.biometric.BiometricPrompt.ERROR_NEGATIVE_BUTTON
import androidx.core.content.ContextCompat
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.ErrorAction
import com.smallworldfs.moneytransferapp.domain.migrated.capabilities.CapabilityChecker
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.presentation.quicklogin.models.BiometricAuthenticatorUIModel
import dagger.hilt.android.qualifiers.ActivityContext
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class BiometricAuthenticatorImpl @Inject constructor(
    @ActivityContext private val activity: GenericActivity,
    private val capabilityChecker: CapabilityChecker,
) : BiometricAuthenticator {

    override fun checkBiometrics(model: BiometricAuthenticatorUIModel, onSuccess: Action, onError: ErrorAction, onCanceled: Action) {
        if (!capabilityChecker.hasBiometricCapability()) {
            onError.invoke(Error.UnsupportedOperation("Biometrics unavailable"))
        } else {
            val executor = ContextCompat.getMainExecutor(activity)

            val callback = object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    when (errorCode) {
                        ERROR_NEGATIVE_BUTTON,
                        BIOMETRIC_ERROR_CANCELED,
                        BIOMETRIC_ERROR_USER_CANCELED,
                        BIOMETRIC_ERROR_TIMEOUT -> onCanceled.invoke()
                        BIOMETRIC_ERROR_LOCKOUT,
                        BIOMETRIC_ERROR_LOCKOUT_PERMANENT -> onError.invoke(Error.BiometricAuthenticationFail(""))
                    }
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    onSuccess.invoke()
                }
            }

            val biometricPrompt = BiometricPrompt(activity, executor, callback)

            val promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle(model.title)
                .setSubtitle(model.subtitle)
                .setDescription(model.description)
                .setNegativeButtonText(model.cancelText)
                .build()

            biometricPrompt.authenticate(promptInfo)
        }
    }
}
