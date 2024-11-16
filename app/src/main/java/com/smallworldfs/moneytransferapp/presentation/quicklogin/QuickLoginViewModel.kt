package com.smallworldfs.moneytransferapp.presentation.quicklogin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.Event
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.get
import com.smallworldfs.moneytransferapp.domain.migrated.base.peek
import com.smallworldfs.moneytransferapp.domain.migrated.base.peekFailure
import com.smallworldfs.moneytransferapp.domain.migrated.quicklogin.QuickLoginUseCase
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.base.ErrorTypeMapper
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class QuickLoginViewModel @Inject constructor(
    private val quickLoginUseCase: QuickLoginUseCase,
    private val errorTypeMapper: ErrorTypeMapper
) : ViewModel() {

    private val _biometricsEnabled: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val biometricsEnabled: LiveData<Event<Boolean>> get() = _biometricsEnabled

    private val _setBiometricsError: MutableLiveData<Event<ErrorType>> = MutableLiveData()
    val setBiometricsError: LiveData<Event<ErrorType>> get() = _setBiometricsError

    private val _userEmail: MutableLiveData<Event<String>> = MutableLiveData()
    val userEmail: LiveData<Event<String>> get() = _userEmail

    fun getCurrentUserEmail() {
        viewModelScope.launch(Dispatchers.IO) {
            quickLoginUseCase.getUserEmail()
                .peek { _userEmail.postValue(Event(it)) }
                .peekFailure { _userEmail.postValue(Event(STRING_EMPTY)) }
        }
    }

    fun setBiometricsEnabled() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = quickLoginUseCase.setBiometricsEnabled()) {
                is Success -> _biometricsEnabled.postValue(Event(true))
                is Failure -> _setBiometricsError.postValue(Event(errorTypeMapper.map(result.get())))
            }
        }
    }

    fun setBiometricsDisabled() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = quickLoginUseCase.setBiometricsDisabled()) {
                is Success -> _biometricsEnabled.postValue(Event(false))
                is Failure -> _setBiometricsError.postValue(Event(errorTypeMapper.map(result.get())))
            }
        }
    }
}
