package com.smallworldfs.moneytransferapp.presentation.resetpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.resetpassword.usecase.ResetPasswordUseCase
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Attributes
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.base.ErrorTypeMapper
import com.smallworldfs.moneytransferapp.utils.Constants.FIELD_TYPE.PASSWORD
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val errorTypeMapper: ErrorTypeMapper
) : ViewModel() {

    private val _errorResetPassword = MutableStateFlow<ErrorType>(ErrorType.None)
    val errorResetPassword: StateFlow<ErrorType> = _errorResetPassword.asStateFlow()

    private val _successResetPassword = MutableStateFlow(false)
    val successResetPassword: StateFlow<Boolean> = _successResetPassword

    private val _passwordAttributes = MutableStateFlow(Attributes())
    val passwordAttributes: StateFlow<Attributes> = _passwordAttributes

    private val _passwordRequirementsText = MutableStateFlow(STRING_EMPTY)
    val passwordRequirementsText: StateFlow<String> = _passwordRequirementsText

    init {
        getForm()
    }

    fun resetPassword(password: String, token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            resetPasswordUseCase.resetPassword(password, token)
                .map {
                    _successResetPassword.value = it
                }
                .mapFailure {
                    showErrorView(errorTypeMapper.map(it))
                }
        }
    }

    private fun getForm() {
        viewModelScope.launch(Dispatchers.IO) {
            resetPasswordUseCase.getForm()
                .map { fields ->
                    fields.fields.forEach { field ->
                        if (field.type.equals(PASSWORD, true) && field.attributes != null) {
                            _passwordAttributes.value = field.attributes
                            _passwordRequirementsText.value = field.attributes.textRequirements
                        }
                    }
                }
                .mapFailure {
                    showErrorView(errorTypeMapper.map(it))
                }
        }
    }

    fun showErrorView(error: ErrorType) {
        _errorResetPassword.value = error
    }

    fun hideErrorView() {
        _errorResetPassword.value = ErrorType.None
    }

    fun hideDialog() {
        _successResetPassword.value = false
    }
}
