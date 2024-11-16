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
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.PassCodeDTO
import com.smallworldfs.moneytransferapp.domain.migrated.passcode.usecase.PassCodeUseCase
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.base.ErrorTypeMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PassCodeViewModel @Inject constructor(
    private val passCodeUseCase: PassCodeUseCase,
    private val errorTypeMapper: ErrorTypeMapper
) : ViewModel() {

    private val _passcodeSaved: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val passcodeSaved: LiveData<Event<Boolean>> get() = _passcodeSaved

    private val _passcodeValidated: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val passcodeValidated: LiveData<Event<Boolean>> get() = _passcodeValidated

    private val _passcodeReseted: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val passcodeReseted: LiveData<Event<Boolean>> get() = _passcodeReseted

    private val _passcodeError: MutableLiveData<Event<ErrorType>> = MutableLiveData()
    val passcodeError: LiveData<Event<ErrorType>> get() = _passcodeError

    fun savePasscode(passcode: CharArray) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = passCodeUseCase.savePasscode(passcode)) {
                is Success -> _passcodeSaved.postValue(Event(true))
                is Failure -> _passcodeError.postValue(Event(errorTypeMapper.map(result.get())))
            }
        }
    }

    fun validatePasscode(passcode: CharArray) {
        viewModelScope.launch(Dispatchers.IO) {
            passCodeUseCase.validatePassCode(PassCodeDTO(passcode))
                .peek {
                    _passcodeValidated.postValue(Event(true))
                }
                .peekFailure {
                    _passcodeError.postValue(Event(ErrorType.WrongPasscodeError("Wrong Passcode")))
                }
        }
    }

    fun resetPasscode() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = passCodeUseCase.resetPasscode()) {
                is Success -> _passcodeReseted.postValue(Event(true))
                is Failure -> _passcodeError.postValue(Event(errorTypeMapper.map(result.get())))
            }
        }
    }
}
