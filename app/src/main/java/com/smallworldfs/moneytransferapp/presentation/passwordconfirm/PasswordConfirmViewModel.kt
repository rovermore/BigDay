package com.smallworldfs.moneytransferapp.presentation.passwordconfirm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.Event
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.get
import com.smallworldfs.moneytransferapp.domain.migrated.passwordconfirm.PasswordConfirmUseCase
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.base.ErrorTypeMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PasswordConfirmViewModel @Inject constructor(
    private val errorTypeMapper: ErrorTypeMapper,
    private val passwordConfirmUseCase: PasswordConfirmUseCase
) : ViewModel() {

    private val _passwordCorrect: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val passwordCorrect: LiveData<Event<Boolean>> get() = _passwordCorrect

    private val _passwordError: MutableLiveData<Event<ErrorType>> = MutableLiveData()
    val passwordError: LiveData<Event<ErrorType>> get() = _passwordError

    fun checkPassword(password: CharArray) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = passwordConfirmUseCase.checkPassword(password)) {
                is Success -> _passwordCorrect.postValue(Event(true))
                is Failure -> _passwordError.postValue(Event(errorTypeMapper.map(result.get())))
            }
        }
    }
}
