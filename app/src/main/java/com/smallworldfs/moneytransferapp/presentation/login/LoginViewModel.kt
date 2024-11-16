package com.smallworldfs.moneytransferapp.presentation.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.peek
import com.smallworldfs.moneytransferapp.domain.migrated.base.peekFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.then
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.PassCodeDTO
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.PasswordDTO
import com.smallworldfs.moneytransferapp.domain.migrated.login.usecase.LoginUseCase
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.base.ErrorTypeMapper
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel
import com.smallworldfs.moneytransferapp.presentation.freeuser.CountryUIModelMapper
import com.smallworldfs.moneytransferapp.presentation.login.LoginUIState.EXISTING_USER
import com.smallworldfs.moneytransferapp.presentation.login.LoginUIState.NEW_USER
import com.smallworldfs.moneytransferapp.presentation.login.model.UserUIModel
import com.smallworldfs.moneytransferapp.presentation.login.model.UserUIModelMapper
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val countryUIModelMapper: CountryUIModelMapper,
    private val userUIModelMapper: UserUIModelMapper,
    private val errorTypeMapper: ErrorTypeMapper
) : ViewModel() {

    private val _uiState = MutableStateFlow(NEW_USER)
    val uiState get() = _uiState.asStateFlow()

    private val _errorView = MutableStateFlow<Boolean>(false)
    val errorView get() = _errorView.asStateFlow()

    private val _userLogged: MutableStateFlow<UserUIModel> = MutableStateFlow(UserUIModel())
    val userLogged get() = _userLogged.asStateFlow()

    private val _loginError = MutableStateFlow<ErrorType>(ErrorType.None)
    val loginError get() = _loginError.asStateFlow()

    private val _existingUser: MutableStateFlow<UserUIModel?> = MutableStateFlow(null)
    val existingUser = _existingUser.asStateFlow()

    private val _originCountries: MutableStateFlow<List<CountryUIModel>> = MutableStateFlow(emptyList())
    val originCountries get() = _originCountries.asStateFlow()

    private val _selectedCountry = MutableStateFlow(CountryUIModel())
    val selectedCountry get() = _selectedCountry.asStateFlow()

    private val _userEmail: MutableStateFlow<String> = MutableStateFlow(STRING_EMPTY)
    val userEmail get() = _userEmail.asStateFlow()

    private val _passcodeEnabled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val passcodeEnabled get() = _passcodeEnabled.asStateFlow()

    private val _passcodeValidated: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val passcodeValidated get() = _passcodeValidated.asStateFlow()

    private val _passcodeError: MutableStateFlow<ErrorType> = MutableStateFlow(ErrorType.None)
    val passcodeError get() = _passcodeError.asStateFlow()

    private val _biometricsEnabled: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val biometricsEnabled get() = _biometricsEnabled.asStateFlow()

    private val _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loading get() = _loading.asStateFlow()

    fun getCurrentUserEmail() {
        viewModelScope.launch(Dispatchers.IO) {
            loginUseCase.getUserEmail()
                .map {
                    _userEmail.value = it
                }
                .mapFailure {
                    _userEmail.value = STRING_EMPTY
                }
        }
    }

    private fun isPasscodeEnabled() {
        viewModelScope.launch(Dispatchers.IO) {
            loginUseCase.hasPasscode()
                .peek { _passcodeEnabled.value = it }
                .peekFailure { _passcodeEnabled.value = false }
        }
    }

    fun validatePasscode(passcode: CharArray) {
        viewModelScope.launch(Dispatchers.IO) {
            loginUseCase.validatePassCode(PassCodeDTO(passcode))
                .peek {
                    _passcodeValidated.value = true
                }.peekFailure {
                    _passcodeError.value = ErrorType.WrongPasscodeError("Wrong Passcode")
                }
        }
    }

    fun performAutoLogin() {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            loginUseCase.autoLogin()
                .peek { userDTO ->
                    _userLogged.value = userUIModelMapper.map(userDTO, false)
                }.peekFailure {
                    _loginError.value = errorTypeMapper.map(it)
                }
                .then {
                    _loading.value = false
                }
        }
    }

    fun onActionNewLogin(email: String, password: CharArray, country: String) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            loginUseCase.login(
                email = email,
                PasswordDTO(password),
                CountryDTO(iso3 = country),
            ).peek { userDTO ->
                loginUseCase.resetQuickLogin()
                loginUseCase.isQuickLoginActive().peek { active ->
                    _userLogged.value = userUIModelMapper.map(userDTO, !active)
                }.peekFailure {
                    _loginError.value = errorTypeMapper.map(it)
                }
            }.peekFailure {
                _loginError.value = errorTypeMapper.map(it)
            }.then {
                _loading.value = false
            }
        }
    }

    fun getExistingUser() {
        viewModelScope.launch(Dispatchers.IO) {
            loginUseCase.getExistingUser()
                .peek { user ->
                    if (user.isApproved()) {
                        _uiState.value = EXISTING_USER
                        _existingUser.value = userUIModelMapper.map(user, false)
                        _selectedCountry.value = countryUIModelMapper.mapToUIModel(user.country.countries[0])
                    } else {
                        _uiState.value = NEW_USER
                    }
                }.peekFailure {
                    _uiState.value = NEW_USER
                }
        }
    }

    fun getCountries() {
        viewModelScope.launch(Dispatchers.IO) {
            loginUseCase.getCountries()
                .map {
                    _originCountries.value = countryUIModelMapper.mapToUIModel(it.countries).sortedBy { it.name }
                    it.countries.find { it.featured }?.let { _selectedCountry.value = countryUIModelMapper.mapToUIModel(it) }
                }.mapFailure {
                    _originCountries.value = emptyList()
                    _loginError.value = errorTypeMapper.map(it)
                }
        }
    }

    fun updateSelectedCountry(countryData: CountryUIModel) {
        _selectedCountry.value = countryData
    }

    fun changeAccount() {
        _uiState.value = NEW_USER
        val userCountry = _originCountries.value.find {
            it.iso3 == selectedCountry.value.iso3
        }
        if (_selectedCountry.value.logo.isEmpty() && userCountry != null) {
            _selectedCountry.value = userCountry
        }
    }

    fun isBiometricsEnabled() {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = loginUseCase.isBiometricsEnabled()) {
                is Success -> {
                    _biometricsEnabled.value = result.value
                    if (!result.value)
                        isPasscodeEnabled()
                }

                is Failure -> {
                    _biometricsEnabled.value = false
                    isPasscodeEnabled()
                }
            }
        }
    }

    fun showErrorView() {
        _errorView.value = true
    }

    fun removeError() {
        _loginError.value = ErrorType.None
        _errorView.value = false
    }
}
