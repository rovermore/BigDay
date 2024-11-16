package com.smallworldfs.moneytransferapp.presentation.quicklogin

import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.domain.migrated.base.ErrorAction
import com.smallworldfs.moneytransferapp.presentation.quicklogin.models.BiometricAuthenticatorUIModel

interface BiometricAuthenticator {
    fun checkBiometrics(model: BiometricAuthenticatorUIModel, onSuccess: Action, onError: ErrorAction = {}, onCancelled: Action = {})
}
