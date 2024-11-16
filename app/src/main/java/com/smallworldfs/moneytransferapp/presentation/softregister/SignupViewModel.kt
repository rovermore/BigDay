package com.smallworldfs.moneytransferapp.presentation.softregister

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.Event
import com.smallworldfs.moneytransferapp.data.userdata.model.UserResponseMapper
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.get
import com.smallworldfs.moneytransferapp.domain.migrated.base.peek
import com.smallworldfs.moneytransferapp.domain.migrated.base.peekFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.then
import com.smallworldfs.moneytransferapp.domain.migrated.base.zipFailure
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.domain.migrated.signup.SignupUseCase
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.base.ErrorTypeMapper
import com.smallworldfs.moneytransferapp.presentation.common.countries.AddressUIMapper
import com.smallworldfs.moneytransferapp.presentation.common.countries.AddressUIModel
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel
import com.smallworldfs.moneytransferapp.presentation.common.countries.StateUIModel
import com.smallworldfs.moneytransferapp.presentation.freeuser.CountryUIModelMapper
import com.smallworldfs.moneytransferapp.presentation.freeuser.StateUIModelMapper
import com.smallworldfs.moneytransferapp.presentation.softregister.model.RegisterStepMapper
import com.smallworldfs.moneytransferapp.presentation.softregister.model.RegisterUIModel
import com.smallworldfs.moneytransferapp.presentation.softregister.model.SmsAttempt
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignupViewModel @Inject constructor(
    private val signupUseCase: SignupUseCase,
    private val errorTypeMapper: ErrorTypeMapper,
    private val countryUIModelMapper: CountryUIModelMapper,
    private val stateUIModelMapper: StateUIModelMapper,
    private val registerStepMapper: RegisterStepMapper,
    private val addressUIMapper: AddressUIMapper,
    private val userStatusMapper: UserResponseMapper,
) : ViewModel() {

    private val _userRegisterAlreadyStarted: MutableLiveData<Event<RegisterUIModel>> = MutableLiveData()
    val userRegisterAlreadyStarted: LiveData<Event<RegisterUIModel>> get() = _userRegisterAlreadyStarted

    private val _countries: MutableLiveData<Event<List<CountryUIModel>>> = MutableLiveData()
    val countries: LiveData<Event<List<CountryUIModel>>> get() = _countries

    private val _states: MutableLiveData<Event<List<StateUIModel>>> = MutableLiveData()
    val states: LiveData<Event<List<StateUIModel>>> get() = _states

    private val _originCountries: MutableLiveData<Event<List<CountryUIModel>>> = MutableLiveData()
    val originCountries: LiveData<Event<List<CountryUIModel>>> get() = _originCountries

    private val _selectedCountry: MutableLiveData<Event<CountryUIModel>> = MutableLiveData()
    val selectedCountry: LiveData<Event<CountryUIModel>> get() = _selectedCountry

    private val _selectedPhoneCountry: MutableLiveData<Event<CountryUIModel>> = MutableLiveData()
    val selectedPhoneCountry: LiveData<Event<CountryUIModel>> get() = _selectedPhoneCountry

    private val _phoneRegisterSuccess: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val phoneRegisterSuccess: LiveData<Event<Boolean>> get() = _phoneRegisterSuccess

    private val _codeVerificationSuccess: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val codeVerificationSuccess: LiveData<Event<Boolean>> get() = _codeVerificationSuccess

    private val _registerCredentialsError: MutableLiveData<Event<ErrorType>> = MutableLiveData()
    val registerCredentialsError: LiveData<Event<ErrorType>> get() = _registerCredentialsError

    private val _registerPhoneError: MutableLiveData<Event<ErrorType>> = MutableLiveData()
    val registerPhoneError: LiveData<Event<ErrorType>> get() = _registerPhoneError

    private val _registerUserError: MutableLiveData<Event<ErrorType>> = MutableLiveData()
    val registerUserError: LiveData<Event<ErrorType>> get() = _registerUserError

    private val _activityGenericError: MutableLiveData<Event<ErrorType>> = MutableLiveData()
    val activityGenericError: LiveData<Event<ErrorType>> get() = _activityGenericError

    private val _timer: MutableLiveData<Event<SmsAttempt>> = MutableLiveData()
    val timer: LiveData<Event<SmsAttempt>> get() = _timer

    private val _searchAddressList: MutableLiveData<Event<List<AddressUIModel>>> = MutableLiveData()
    val searchAddressList: LiveData<Event<List<AddressUIModel>>> get() = _searchAddressList

    private val _addressForm: MutableLiveData<Event<List<Field>>> = MutableLiveData()
    val addressForm: LiveData<Event<List<Field>>> get() = _addressForm

    private val _registerCompleted: MutableLiveData<Event<String>> = MutableLiveData()
    val registerCompleted: LiveData<Event<String>> get() = _registerCompleted

    private val _loading: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val loading: LiveData<Event<Boolean>> get() = _loading

    private val _navigateToLoginScreen: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val navigateToLoginScreen: LiveData<Event<Boolean>> get() = _navigateToLoginScreen

    private val _navigateToProfileStep: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val navigateToProfileStep: LiveData<Event<Boolean>> get() = _navigateToProfileStep

    private var smsCodesSent = 0

    private var smsVerificationId = ""

    var user: UserDTO? = null
        private set

    companion object {
        private const val SMS_20_SECONDS_COUNTDOWN: Long = 20000
        private const val SMS_10_MINUTES_COUNTDOWN: Long = 600000
    }

    private fun checkRegisterState() {
        signupUseCase.getUserData()
            .peek {
                _userRegisterAlreadyStarted.postValue(
                    Event(
                        RegisterUIModel(
                            registerStepMapper.map(
                                userStatusMapper.mapStatus(it.status).status
                            ),
                            it
                        )
                    )
                )
                user = it
            }.peekFailure {
                _registerCredentialsError.postValue(Event(errorTypeMapper.map(it)))
            }
    }

    fun getCountriesData() {
        _loading.value = Event(true)
        viewModelScope.launch(Dispatchers.IO) {
            listOf(
                signupUseCase.getCountries().peek {
                    _countries.postValue(Event(countryUIModelMapper.mapToUIModel(it.countries).sortedBy { it.name }))
                },
                signupUseCase.getOriginCountries().peek {
                    _originCountries.postValue(Event(countryUIModelMapper.mapToUIModel(it.countries).sortedBy { it.name }))
                    it.countries.find { it.featured }?.let {
                        _selectedCountry.postValue(Event(countryUIModelMapper.mapToUIModel(it)))
                        _selectedPhoneCountry.postValue(Event(countryUIModelMapper.mapToUIModel(it)))
                    }
                }
            ).zipFailure { error ->
                _registerCredentialsError.postValue(Event(errorTypeMapper.map(error)))
            }.then {
                _loading.postValue(Event(false))
            }
            signupUseCase.getUSAStates()
                .peek {
                    _states.postValue(Event(stateUIModelMapper.mapToUIModel(it).sortedBy { it.name }))
                }.peekFailure {
                    _states.postValue(Event(emptyList()))
                }
        }
    }

    fun registerUserCredentials(
        email: String,
        countryName: String,
        password: CharArray,
        state: String,
        checkMarketing: Boolean,
        checkPrivacy: Boolean,
        checkTerms: Boolean
    ) {
        _loading.value = Event(true)
        viewModelScope.launch(Dispatchers.IO) {
            val country = _originCountries.value?.peekContent()?.find { it.name == countryName } ?: CountryUIModel()
            signupUseCase.registerCredentials(
                email,
                countryUIModelMapper.mapToDTO(country),
                password,
                _states.value?.peekContent()?.firstOrNull { it.name == state }?.code ?: STRING_EMPTY,
                checkMarketing,
                checkPrivacy,
                checkTerms,
            ).peek {
                checkRegisterState()
            }.peekFailure {
                _registerCredentialsError.postValue(Event(errorTypeMapper.map(it)))
            }.then {
                _loading.postValue(Event(false))
            }
        }
    }

    fun verifyCode(code: String) {
        _loading.value = Event(true)
        viewModelScope.launch(Dispatchers.IO) {
            signupUseCase.verifyCode(code, smsVerificationId)
                .peek {
                    _codeVerificationSuccess.postValue(Event(true))
                }.peekFailure {
                    _registerPhoneError.postValue(Event(errorTypeMapper.map(it)))
                }.then {
                    _loading.postValue(Event(false))
                }
        }
    }

    fun resendSMS(phoneNumber: String) {
        _loading.postValue(Event(true))
        viewModelScope.launch(Dispatchers.IO) {
            selectedPhoneCountry.value?.peekContent()?.countryCode?.let { countryCode ->

                signupUseCase.resendCode(
                    smsVerificationId,
                    phoneNumber,
                    countryCode,
                ).peek {
                    smsCodesSent++
                    smsVerificationId = it.operationId
                    _phoneRegisterSuccess.postValue(Event(true))
                }.peekFailure {
                    val error = errorTypeMapper.map(it)
                    if (error is ErrorType.SecurityErrorForbidden) {
                        smsCodesSent++
                    }
                    _registerPhoneError.postValue(Event(error))
                }.then {
                    _loading.postValue(Event(false))
                }
            }?.peekFailure {
                _registerPhoneError.postValue(Event(errorTypeMapper.map(it)))
            }?.then {
                _loading.postValue(Event(false))
            }
            when (smsCodesSent) {
                2 -> _timer.postValue(Event(SmsAttempt(SMS_20_SECONDS_COUNTDOWN, 2)))
                else -> _timer.postValue(Event(SmsAttempt(SMS_10_MINUTES_COUNTDOWN, 3)))
            }
        }
    }

    fun registerPhone(phoneNumber: String, isSMSMarketingChecked: Boolean) {
        _loading.postValue(Event(true))
        viewModelScope.launch(Dispatchers.IO) {
            selectedPhoneCountry.value?.peekContent()?.countryCode?.let { countryCode ->
                signupUseCase.registerPhone(
                    phoneNumber,
                    countryCode,
                    isSMSMarketingChecked
                ).peek {
                    smsCodesSent++
                    smsVerificationId = it.operationId
                    _phoneRegisterSuccess.postValue(Event(true))
                    _timer.postValue(Event(SmsAttempt(SMS_20_SECONDS_COUNTDOWN, smsCodesSent)))
                }.peekFailure {
                    val error = errorTypeMapper.map(it)
                    if (error is ErrorType.SecurityErrorForbidden) {
                        smsCodesSent = 0
                    }
                    _registerPhoneError.postValue(Event(error))
                }.then {
                    _loading.postValue(Event(false))
                }
            }?.peekFailure {
                _registerPhoneError.postValue(Event(errorTypeMapper.map(it)))
            }?.then {
                _loading.postValue(Event(false))
            }
        }
    }

    fun updateSelectedCountry(countryData: CountryUIModel) {
        _selectedCountry.postValue(Event(countryData))
        updateSelectedPhoneCountry(countryData)
    }

    fun updateSelectedPhoneCountry(countryData: CountryUIModel) {
        _selectedPhoneCountry.postValue(Event((countryData)))
    }

    fun searchAddress(address: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = signupUseCase.searchAddress(address)) {
                is Success -> _searchAddressList.postValue(Event(addressUIMapper.mapList(result.get())))
                is Failure -> _registerUserError.postValue(Event(errorTypeMapper.map(result.get())))
            }
        }
    }

    fun searchAddressByParentId(parentId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = signupUseCase.searchAddressByParentId(parentId)) {
                is Success -> _searchAddressList.postValue(Event(addressUIMapper.mapList(result.get())))
                is Failure -> _registerUserError.postValue(Event(errorTypeMapper.map(result.get())))
            }
        }
    }

    fun getAddressById(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            signupUseCase.getAddressById(id)
                .peek {
                    _addressForm.postValue(Event(it))
                }.peekFailure {
                    _registerUserError.postValue(Event(errorTypeMapper.map(it)))
                }
        }
    }

    fun getAddressForm() {
        viewModelScope.launch(Dispatchers.IO) {
            signupUseCase.getAddressForm()
                .peek {
                    _addressForm.postValue(Event(it))
                }.peekFailure {
                    _registerUserError.postValue(Event(errorTypeMapper.map(it)))
                }
        }
    }

    fun registerUser(
        fullFirstName: String,
        fullLastName: String,
        dateOfBirth: String,
        city: String,
        streetType: String,
        streetName: String,
        streetNumber: String,
        buildingName: String,
        zip: String,
        state: String,
        address: String,
        signature: String,
        addressType: String
    ) {
        _loading.value = Event(true)
        viewModelScope.launch(Dispatchers.IO) {
            signupUseCase.registerUser(
                fullFirstName,
                fullLastName,
                dateOfBirth,
                city,
                streetType,
                streetName,
                streetNumber,
                buildingName,
                zip,
                state,
                address,
                signature
            ).peek {
                _registerCompleted.postValue(Event(addressType))
            }.peekFailure {
                _registerUserError.postValue(Event(errorTypeMapper.map(it)))
            }.then {
                _loading.postValue(Event(false))
            }
        }
    }

    fun navigateToLoginScreen() {
        _navigateToLoginScreen.postValue(Event(true))
    }

    fun showErrorGenericErrorView(errorType: ErrorType) {
        _activityGenericError.postValue(Event(errorType))
    }

    fun navigateToProfileStep() {
        _navigateToProfileStep.postValue(Event(true))
    }
}
