package com.smallworldfs.moneytransferapp.presentation.marketing

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.then
import com.smallworldfs.moneytransferapp.domain.migrated.marketing.usecase.MarketingPreferencesUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.signup.model.MarketingPreference
import com.smallworldfs.moneytransferapp.domain.migrated.signup.model.MarketingPreferenceDTO
import com.smallworldfs.moneytransferapp.modules.register.domain.model.Form
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType.None
import com.smallworldfs.moneytransferapp.presentation.base.ErrorTypeMapper
import com.smallworldfs.moneytransferapp.utils.toBool
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MarketingPreferencesViewModel @Inject constructor(
    private val marketingPreferencesUseCase: MarketingPreferencesUseCase,
    private val marketingPreferencesFormMapper: MarketingPreferencesFormMapper,
    private val errorTypeMapper: ErrorTypeMapper
) : ViewModel() {

    private val _operationError: MutableStateFlow<ErrorType> = MutableStateFlow(None)
    val operationError = _operationError.asStateFlow()

    private val _preferences = MutableStateFlow(Form())
    val preferences get() = _preferences.asStateFlow()

    private var fieldData = HashMap<String, String>()

    private val _onPreferencesSaved = MutableStateFlow(false)
    val onPreferencesSaved get() = _onPreferencesSaved.asStateFlow()

    private val _isLoading: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val isLoading = _isLoading.asStateFlow()

    fun requestPreferences(fromView: String) {
        _isLoading.value = true
        _operationError.value = None
        viewModelScope.launch(Dispatchers.IO) {
            marketingPreferencesUseCase.requestPreferences(fromView)
                .map {
                    _preferences.value = marketingPreferencesFormMapper.mapSwitchNumberValueToBooleanValue(it)
                }.mapFailure {
                    _operationError.value = errorTypeMapper.map(it)
                }
                .then {
                    _isLoading.value = false
                }
        }
    }

    fun savePreferences() {
        if (fieldData.keys.isNotEmpty()) {
            _isLoading.value = true
            viewModelScope.launch(Dispatchers.IO) {
                val enable = mutableListOf<MarketingPreference>()
                val disable = mutableListOf<MarketingPreference>()
                if (fieldData["accept_email"].toBool()) enable.add(MarketingPreference.Email) else disable.add(MarketingPreference.Email)
                if (fieldData["accept_sms"].toBool()) enable.add(MarketingPreference.SMS) else disable.add(MarketingPreference.SMS)
                if (fieldData["accept_push"].toBool()) enable.add(MarketingPreference.PushNotification) else disable.add(MarketingPreference.PushNotification)
                marketingPreferencesUseCase.updatePreferences(MarketingPreferenceDTO(enable, disable))
                    .map {
                        _onPreferencesSaved.value = true
                    }.mapFailure {
                        _operationError.value = errorTypeMapper.map(it)
                    }
                    .then {
                        _isLoading.value = false
                    }
            }
        }
    }

    fun updatePreferences(hashMap: HashMap<String, String>) {
        fieldData = hashMap
    }
}
