package com.smallworldfs.moneytransferapp.presentation.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.domain.migrated.base.peek
import com.smallworldfs.moneytransferapp.domain.migrated.base.peekFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.then
import com.smallworldfs.moneytransferapp.domain.migrated.settings.usecase.GetSettingsUseCase
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType.None
import com.smallworldfs.moneytransferapp.presentation.base.ErrorTypeMapper
import com.smallworldfs.moneytransferapp.presentation.settings.model.AppSettings
import com.smallworldfs.moneytransferapp.presentation.settings.model.QuickLoginSettingsMapper
import com.smallworldfs.moneytransferapp.presentation.settings.model.QuickLoginSettingsUIModel
import com.smallworldfs.moneytransferapp.presentation.settings.model.SettingsDTOMapper
import com.smallworldfs.moneytransferapp.presentation.settings.model.SettingsUIModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getSettingsUseCase: GetSettingsUseCase,
    private val settingsDTOMapper: SettingsDTOMapper,
    private val quickLoginSettingsMapper: QuickLoginSettingsMapper,
    private val errorTypeMapper: ErrorTypeMapper
) : ViewModel() {

    private val _settingState = MutableStateFlow(SettingsUIModel())
    val settingsState = _settingState.asStateFlow()

    private val _requestSettingsError: MutableStateFlow<ErrorType> = MutableStateFlow(None)
    val requestSettingsError = _requestSettingsError.asStateFlow()

    private val _biometricsError: MutableStateFlow<ErrorType> = MutableStateFlow(None)
    val biometricsError = _biometricsError.asStateFlow()

    private val _quickLoginSettings: MutableStateFlow<QuickLoginSettingsUIModel> = MutableStateFlow(
        QuickLoginSettingsUIModel(),
    )
    val quickLoginSettings: StateFlow<QuickLoginSettingsUIModel> get() = _quickLoginSettings.asStateFlow()

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(
        true,
    )
    val isLoading = _isLoading.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog = _showDialog.asStateFlow()

    fun getSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            _isLoading.value = true
            _requestSettingsError.value = None
            getSettingsUseCase.getSettings()
                .peek {
                    _settingState.value = settingsDTOMapper.map(it)
                }.peekFailure {
                    _requestSettingsError.value = errorTypeMapper.map(it)
                }
                .then {
                    _isLoading.value = false
                }
        }
    }

    fun getQuickLoginSettings() {
        viewModelScope.launch(Dispatchers.IO) {
            getSettingsUseCase.getQuickLoginSettings()
                .peek { _quickLoginSettings.value = quickLoginSettingsMapper.map(it) }
                .peekFailure { _requestSettingsError.value = (errorTypeMapper.map(it)) }
        }
    }

    fun updateNotificationsState(enabled: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            getSettingsUseCase.setNotificationsState(enabled)
            _settingState.update {
                _settingState.value.copy(
                    appSettings = AppSettings(enabled),
                )
            }
        }
    }

    fun enableBiometrics() {
        viewModelScope.launch(Dispatchers.IO) {
            getSettingsUseCase.setBiometricsEnabled()
                .peek { _quickLoginSettings.value = quickLoginSettingsMapper.map(it) }
                .peekFailure { _biometricsError.value = (errorTypeMapper.map(it)) }
        }
    }

    fun disableBiometrics() {
        viewModelScope.launch(Dispatchers.IO) {
            getSettingsUseCase.setBiometricsDisabled()
                .peek { _quickLoginSettings.value = quickLoginSettingsMapper.map(it) }
                .peekFailure { _biometricsError.value = (errorTypeMapper.map(it)) }
        }
    }

    fun disablePassCode() {
        viewModelScope.launch(Dispatchers.IO) {
            getSettingsUseCase.setPasscodeDisabled()
                .peek { _quickLoginSettings.value = quickLoginSettingsMapper.map(it) }
                .peekFailure { _biometricsError.value = (errorTypeMapper.map(it)) }
        }
    }

    fun removeBiometricError() {
        _biometricsError.value = None
    }

    fun hideDialog() {
        _showDialog.value = false
    }

    fun showDialog() {
        _showDialog.value = true
    }
}
