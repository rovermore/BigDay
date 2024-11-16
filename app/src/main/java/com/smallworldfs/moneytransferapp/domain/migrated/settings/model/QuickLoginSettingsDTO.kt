package com.smallworldfs.moneytransferapp.domain.migrated.settings.model

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success

class QuickLoginSettingsDTO(
    _biometricsAvailable: Boolean,
    private var _biometricsActive: Boolean,
    private var _passcodeActive: Boolean,
) {

    init {
        if (!_biometricsAvailable) _biometricsActive = false
    }

    var changePassCodeVisible = false
        private set

    private var showBiometrics = _biometricsAvailable

    var biometricsActive = _biometricsActive
        get() = _biometricsActive
        private set(value) {
            _biometricsActive = value
            field = value
        }

    var passcodeActive = _passcodeActive
        get() = _passcodeActive
        private set(value) {
            _passcodeActive = value
            field = value
        }

    private var _state: State = if (_biometricsAvailable) {
        if (_biometricsActive && _passcodeActive) FullyActiveBiometricDevice(this)
        else if (!_biometricsActive && _passcodeActive) PassCodeActive(this)
        else if (_biometricsActive && !_passcodeActive) BiometricsActive(this)
        else FullyDeactivatedBiometricDevice(this)
    } else {
        if (_passcodeActive) FullyActiveDevice(this)
        else FullyDeactiveDevice(this)
    }

    var state = _state
        get() = _state
        private set(value) {
            _state = value
            field = value
        }

    fun activateBiometrics() = state.activateBiometrics()
    fun deactivateBiometrics() = state.deactivateBiometrics()
    fun activatePassCode() = state.activatePassCode()
    fun deactivatePassCode() = state.deactivatePassCode()

    abstract class State(val quickLoginSettingsDTO: QuickLoginSettingsDTO) {
        open fun activateBiometrics(): OperationResult<QuickLoginSettingsDTO, Error> {
            return Failure(Error.UnsupportedOperation("Can't activate biometrics"))
        }

        open fun deactivateBiometrics(): OperationResult<QuickLoginSettingsDTO, Error> {
            return Failure(Error.UnsupportedOperation("Can't deactivate biometrics"))
        }

        open fun activatePassCode(): OperationResult<QuickLoginSettingsDTO, Error> {
            return Failure(Error.UnsupportedOperation("Can't activate passcode"))
        }

        open fun deactivatePassCode(): OperationResult<QuickLoginSettingsDTO, Error> {
            return Failure(Error.UnsupportedOperation("Can't activate biometrics"))
        }
    }

    abstract class BiometricDeviceQuickLoginSettingsState(quickLoginSettingsDTO: QuickLoginSettingsDTO) : State(quickLoginSettingsDTO)
    abstract class QuickLoginSettingsState(quickLoginSettingsDTO: QuickLoginSettingsDTO) : State(quickLoginSettingsDTO)

    class FullyActiveBiometricDevice(quickLoginSettingsDTO: QuickLoginSettingsDTO) : BiometricDeviceQuickLoginSettingsState(quickLoginSettingsDTO) {

        init {
            quickLoginSettingsDTO._biometricsActive = true
            quickLoginSettingsDTO._passcodeActive = true
            quickLoginSettingsDTO.changePassCodeVisible = true
        }

        override fun deactivateBiometrics(): OperationResult<QuickLoginSettingsDTO, Error> {
            quickLoginSettingsDTO.state = FullyDeactivatedBiometricDevice(quickLoginSettingsDTO)
            return Success(quickLoginSettingsDTO)
        }

        override fun activatePassCode(): OperationResult<QuickLoginSettingsDTO, Error> {
            quickLoginSettingsDTO.state = FullyActiveBiometricDevice(quickLoginSettingsDTO)
            return Success(quickLoginSettingsDTO)
        }
    }

    class FullyDeactivatedBiometricDevice(quickLoginSettingsDTO: QuickLoginSettingsDTO) : BiometricDeviceQuickLoginSettingsState(quickLoginSettingsDTO) {

        init {
            quickLoginSettingsDTO._biometricsActive = false
            quickLoginSettingsDTO._passcodeActive = false
            quickLoginSettingsDTO.changePassCodeVisible = false
        }

        override fun activateBiometrics(): OperationResult<QuickLoginSettingsDTO, Error> {
            quickLoginSettingsDTO.state = BiometricsActive(quickLoginSettingsDTO)
            return Success(quickLoginSettingsDTO)
        }

        override fun activatePassCode(): OperationResult<QuickLoginSettingsDTO, Error> {
            quickLoginSettingsDTO.state = PassCodeActive(quickLoginSettingsDTO)
            return Success(quickLoginSettingsDTO)
        }
    }

    class BiometricsActive(quickLoginSettingsDTO: QuickLoginSettingsDTO) : BiometricDeviceQuickLoginSettingsState(quickLoginSettingsDTO) {

        init {
            quickLoginSettingsDTO._biometricsActive = true
        }

        override fun deactivateBiometrics(): OperationResult<QuickLoginSettingsDTO, Error> {
            quickLoginSettingsDTO.state = FullyDeactivatedBiometricDevice(quickLoginSettingsDTO)
            return Success(quickLoginSettingsDTO)
        }

        override fun activatePassCode(): OperationResult<QuickLoginSettingsDTO, Error> {
            quickLoginSettingsDTO.state = FullyActiveBiometricDevice(quickLoginSettingsDTO)
            return Success(quickLoginSettingsDTO)
        }
    }

    class PassCodeActive(quickLoginSettingsDTO: QuickLoginSettingsDTO) : BiometricDeviceQuickLoginSettingsState(quickLoginSettingsDTO) {

        init {
            quickLoginSettingsDTO._biometricsActive = false
            quickLoginSettingsDTO.showBiometrics = true
            quickLoginSettingsDTO._passcodeActive = true
            quickLoginSettingsDTO.changePassCodeVisible = true
        }

        override fun activatePassCode(): OperationResult<QuickLoginSettingsDTO, Error> {
            quickLoginSettingsDTO.state = PassCodeActive(quickLoginSettingsDTO)
            return Success(quickLoginSettingsDTO)
        }

        override fun activateBiometrics(): OperationResult<QuickLoginSettingsDTO, Error> {
            quickLoginSettingsDTO.state = FullyActiveBiometricDevice(quickLoginSettingsDTO)
            return Success(quickLoginSettingsDTO)
        }
    }

    class FullyActiveDevice(quickLoginSettingsDTO: QuickLoginSettingsDTO) : QuickLoginSettingsState(quickLoginSettingsDTO) {

        init {
            quickLoginSettingsDTO._passcodeActive = true
            quickLoginSettingsDTO.changePassCodeVisible = true
            quickLoginSettingsDTO.biometricsActive = false
        }

        override fun deactivatePassCode(): OperationResult<QuickLoginSettingsDTO, Error> {
            quickLoginSettingsDTO.state = FullyDeactiveDevice(quickLoginSettingsDTO)
            return Success(quickLoginSettingsDTO)
        }

        override fun activatePassCode(): OperationResult<QuickLoginSettingsDTO, Error> {
            quickLoginSettingsDTO.state = FullyActiveDevice(quickLoginSettingsDTO)
            return Success(quickLoginSettingsDTO)
        }
    }

    class FullyDeactiveDevice(quickLoginSettingsDTO: QuickLoginSettingsDTO) : QuickLoginSettingsState(quickLoginSettingsDTO) {

        init {
            quickLoginSettingsDTO._passcodeActive = false
            quickLoginSettingsDTO.changePassCodeVisible = false
            quickLoginSettingsDTO.biometricsActive = false
        }

        override fun activatePassCode(): OperationResult<QuickLoginSettingsDTO, Error> {
            quickLoginSettingsDTO.state = FullyActiveDevice(quickLoginSettingsDTO)
            return Success(quickLoginSettingsDTO)
        }
    }
}
