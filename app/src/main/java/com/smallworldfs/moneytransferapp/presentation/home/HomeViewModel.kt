package com.smallworldfs.moneytransferapp.presentation.home

import androidx.core.util.Pair
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.smallworldfs.moneytransferapp.compose.widgets.CountryListType
import com.smallworldfs.moneytransferapp.compose.widgets.model.SWCountryListWithSectionsItemUIModel
import com.smallworldfs.moneytransferapp.data.login.mappers.UserMapper
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.freeuser.CountrySelectionUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.home.smsrunner.SMSConsentRunner
import com.smallworldfs.moneytransferapp.domain.migrated.home.usecase.HomeUseCase
import com.smallworldfs.moneytransferapp.modules.calculator.domain.interactor.implementation.CalculatorInteractorImpl
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.CreateTransactionResponse
import com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivityCountrySelectionCallback
import com.smallworldfs.moneytransferapp.modules.login.domain.model.Gdpr
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.base.ErrorTypeMapper
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel
import com.smallworldfs.moneytransferapp.presentation.freeuser.CountryUIModelMapper
import com.smallworldfs.moneytransferapp.presentation.freeuser.country_selection.SWCountryListWithSectionsItemUIModelMapper
import com.smallworldfs.moneytransferapp.presentation.login.model.UserUIModel
import com.smallworldfs.moneytransferapp.presentation.login.model.UserUIModelMapper
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCase: HomeUseCase,
    private val countrySelectionUseCase: CountrySelectionUseCase,
    private val smsConsentRunner: SMSConsentRunner,
    private val userMapper: UserMapper,
    private val userUIModelMapper: UserUIModelMapper,
    private val countryUIModelMapper: CountryUIModelMapper,
    private val swCountryListWithSectionsItemUIModelMapper: SWCountryListWithSectionsItemUIModelMapper,
    private val errorTypeMapper: ErrorTypeMapper
) : ViewModel() {

    companion object {
        private const val MILLIS_IN_A_DAY: Long = 86400000
    }

    private lateinit var selectionCallback: HomeActivityCountrySelectionCallback

    private val _contentSquareStatus: MutableStateFlow<String> = MutableStateFlow(STRING_EMPTY)
    val contentSquareStatus: StateFlow<String> get() = _contentSquareStatus.asStateFlow()

    private val _showGDPRInfo: MutableStateFlow<Gdpr> = MutableStateFlow(Gdpr())
    val showGDPRInfo: StateFlow<Gdpr> get() = _showGDPRInfo.asStateFlow()

    private val _primeForPush: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val primeForPush: StateFlow<Boolean> get() = _primeForPush.asStateFlow()

    private val _showSendEmailValidationDialog: MutableStateFlow<String> = MutableStateFlow(STRING_EMPTY)
    val showSendEmailValidationDialog: StateFlow<String> get() = _showSendEmailValidationDialog.asStateFlow()

    private val _showAppRatingDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showAppRatingDialog: StateFlow<Boolean> get() = _showAppRatingDialog.asStateFlow()

    private val _showEmailValidated: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showEmailValidated: StateFlow<Boolean> get() = _showEmailValidated.asStateFlow()

    private val _isLimitedUser: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val isLimitedUser: StateFlow<Boolean> get() = _isLimitedUser.asStateFlow()

    private val _user: MutableStateFlow<UserUIModel> = MutableStateFlow(UserUIModel())
    val user: StateFlow<UserUIModel> get() = _user.asStateFlow()

    private val _genericError: MutableStateFlow<ErrorType> = MutableStateFlow(ErrorType.None)
    val genericError: StateFlow<ErrorType> get() = _genericError.asStateFlow()

    private val _selectedOriginCountry: MutableStateFlow<CountryUIModel> = MutableStateFlow(CountryUIModel())
    val selectedOriginCountry: StateFlow<CountryUIModel> get() = _selectedOriginCountry.asStateFlow()

    private val _selectedDestinationCountry: MutableStateFlow<CountryUIModel> = MutableStateFlow(CountryUIModel())
    val selectedDestinationCountry: StateFlow<CountryUIModel> get() = _selectedDestinationCountry.asStateFlow()

    private val _showMarketingDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showMarketingDialog: StateFlow<Boolean> get() = _showMarketingDialog.asStateFlow()

    private val _checkoutDialog: MutableStateFlow<CreateTransactionResponse> = MutableStateFlow(CreateTransactionResponse())
    val checkoutDialog: StateFlow<CreateTransactionResponse> get() = _checkoutDialog.asStateFlow()

    private val _showNotificationNumber: MutableStateFlow<Int> = MutableStateFlow(0)
    val showNotificationNumber: StateFlow<Int> get() = _showNotificationNumber.asStateFlow()

    private val _setPage: MutableStateFlow<Int> = MutableStateFlow(0)
    val setPage: StateFlow<Int> get() = _setPage.asStateFlow()

    private val _resetBehavior: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val resetBehavior: StateFlow<Boolean> get() = _resetBehavior.asStateFlow()

    private val _newBeneficiaryCreated: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val newBeneficiaryCreated: StateFlow<Boolean> get() = _newBeneficiaryCreated.asStateFlow()

    private val _originCountries = MutableStateFlow<List<SWCountryListWithSectionsItemUIModel>>(emptyList())
    val originCountries: StateFlow<List<SWCountryListWithSectionsItemUIModel>> get() = _originCountries.asStateFlow()

    private val _filteredOriginList = MutableStateFlow<List<SWCountryListWithSectionsItemUIModel>>(emptyList())
    val filteredOriginList: StateFlow<List<SWCountryListWithSectionsItemUIModel>> get() = _filteredOriginList.asStateFlow()

    private val _filteredDestinationList = MutableStateFlow<MutableList<SWCountryListWithSectionsItemUIModel>>(mutableListOf())
    val filteredDestinationList: StateFlow<MutableList<SWCountryListWithSectionsItemUIModel>> get() = _filteredDestinationList.asStateFlow()

    private val _recentCountriesUsed = MutableStateFlow<MutableList<SWCountryListWithSectionsItemUIModel>>(mutableListOf())
    val recentCountriesUsed: StateFlow<MutableList<SWCountryListWithSectionsItemUIModel>> get() = _recentCountriesUsed.asStateFlow()

    private val _countryListType = MutableStateFlow<CountryListType>(CountryListType.SendMoneyFrom)
    val countryListType: StateFlow<CountryListType> get() = _countryListType.asStateFlow()

    private val _showBottomSheet = MutableStateFlow(false)
    val showBottomSheet: StateFlow<Boolean> get() = _showBottomSheet.asStateFlow()

    private val _destinationCountries = MutableStateFlow<List<SWCountryListWithSectionsItemUIModel>>(emptyList())
    val destinationCountries: StateFlow<List<SWCountryListWithSectionsItemUIModel>> get() = _destinationCountries.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {

            homeUseCase.getUser()
                .map {
                    _isLimitedUser.value = it.isLimited()
                    _contentSquareStatus.value = it.status
                    _user.value = userUIModelMapper.map(it, false)
                    _selectedOriginCountry.value = countryUIModelMapper.mapToUIModel(it.country.countries.firstOrNull() ?: CountryDTO())
                    _selectedDestinationCountry.value = countryUIModelMapper.mapToUIModel(it.destinationCountry)
                    getOriginCountries()
                    getDestinationCountries(_selectedOriginCountry.value.iso3)
                    smsConsentRunner.checkSMSConsent(userMapper.mapFromUserDTO(it)) { consent ->
                        _showMarketingDialog.value = consent

                        if (consent) {
                            homeUseCase.setSMSConsent()
                        }
                    }

                    FirebaseCrashlytics.getInstance().setCustomKey("Country", it.country.countries.firstOrNull()?.iso3 ?: STRING_EMPTY)
                }.mapFailure {
                    _genericError.value = errorTypeMapper.map(it)
                }
            homeUseCase.getDeliveryMethodsLegacy()

            homeUseCase.getGDPRInfo()
                .map {
                    if (it.listGdprMessages != null && it.listGdprMessages.isNotEmpty() && it.type != null)
                        _showGDPRInfo.value = it
                }.mapFailure {
                    _genericError.value = errorTypeMapper.map(it)
                }

            homeUseCase.getUserCountryInfoLegacy()
        }
        showEmailValidated()
        checkPrimeForPushDailyEvent()
    }

    private fun checkPrimeForPushDailyEvent() {
        viewModelScope.launch(Dispatchers.IO) {
            val timestamp = homeUseCase.getLastPrimeForPushEventTimestamp()
            val currentTimestamp = System.currentTimeMillis()
            if (currentTimestamp >= timestamp + MILLIS_IN_A_DAY) {
                _primeForPush.value = true
                homeUseCase.setLastPrimeForPushEventTimestamp(currentTimestamp)
            }
        }
    }

    fun checkEmailValidated() {
        viewModelScope.launch(Dispatchers.IO) {
            homeUseCase.getUserData()
                .map {
                    if (!it.emailValidated) {
                        _showSendEmailValidationDialog.value = it.email
                    } else {
                        _showAppRatingDialog.value = true
                    }
                }.mapFailure {
                    _genericError.value = errorTypeMapper.map(it)
                }
        }
    }

    private fun showEmailValidated() {
        viewModelScope.launch(Dispatchers.IO) {
            homeUseCase.getUser()
                .map {
                    _showEmailValidated.value = it.showEmailValidated
                }.mapFailure {
                    _genericError.value = errorTypeMapper.map(it)
                }
        }
    }

    fun setShowEmailValidated(showEmailValidated: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            homeUseCase.getUser()
                .map {
                    it.showEmailValidated = showEmailValidated
                    homeUseCase.setUserData(userMapper.mapFromUserDTO(it))
                }.mapFailure {
                    _genericError.value = errorTypeMapper.map(it)
                }
        }
    }

    fun sendGDPRResponse(accept: Boolean) {
        viewModelScope.launch(Dispatchers.IO) {
            homeUseCase.sendGDPRResponse(accept)
                .mapFailure {
                    _genericError.value = errorTypeMapper.map(it)
                }
        }
    }

    fun getDestinationCountries(iso: String) {
        viewModelScope.launch(Dispatchers.IO) {
            countrySelectionUseCase.getDestinationCountries(iso)
                .map {
                    _destinationCountries.value = swCountryListWithSectionsItemUIModelMapper.mapToItem(countryUIModelMapper.mapToUIModel(it.countries).sortedBy { it.name })
                    _filteredDestinationList.value = _destinationCountries.value.toMutableList()
                    addRecentCountryUsed(_selectedDestinationCountry.value)
                }
        }
    }

    fun getOriginCountries() {
        viewModelScope.launch(Dispatchers.IO) {
            countrySelectionUseCase.getOriginCountries()
                .map {
                    _originCountries.value = swCountryListWithSectionsItemUIModelMapper.mapToItem(countryUIModelMapper.mapToUIModel(it.countries).sortedBy { it.name })
                    _filteredOriginList.value = _originCountries.value
                }
        }
    }

    fun filterOrigin(query: String) {
        _filteredOriginList.value = _originCountries.value.filterIsInstance<SWCountryListWithSectionsItemUIModel.Item>().filter { item ->
            item.country.name.contains(query, ignoreCase = true)
        }
    }

    fun filterDestination(query: String) {
        val header: List<SWCountryListWithSectionsItemUIModel> =
            _filteredDestinationList.value.filterIsInstance<SWCountryListWithSectionsItemUIModel.Header>()

        val newList: List<SWCountryListWithSectionsItemUIModel> =
            _destinationCountries.value.filterIsInstance<SWCountryListWithSectionsItemUIModel.Item>().filter { item ->
                item.country.name.contains(query, ignoreCase = true)
            }

        _filteredDestinationList.value = (header + newList).toMutableList()
    }

    fun showDialogCheckout(transaction: CreateTransactionResponse) {
        _checkoutDialog.value = transaction
    }

    fun showNotificationNumber(notifications: Int) {
        _showNotificationNumber.value = notifications
    }

    fun resetBehavior(shouldReset: Boolean) {
        _resetBehavior.value = shouldReset
    }

    fun showNewBeneficiaryCreatedDialog(show: Boolean) {
        _newBeneficiaryCreated.value = show
    }

    fun setPage(page: Int) {
        _setPage.value = page
    }

    fun hideGdprDialog() {
        _showGDPRInfo.value = Gdpr()
    }

    fun hideValidateEmailDialog() {
        _showSendEmailValidationDialog.value = STRING_EMPTY
    }

    fun hideEmailValidatedDialog() {
        _showEmailValidated.value = false
    }

    fun hideMarketingPrefDialog() {
        _showMarketingDialog.value = false
    }

    fun hideCheckoutDialog() {
        _checkoutDialog.value = CreateTransactionResponse()
    }

    fun setSelectionCallback(callback: HomeActivityCountrySelectionCallback) {
        selectionCallback = callback
    }

    fun updateCountryListType(countryListType: CountryListType) {
        _countryListType.value = countryListType
    }

    fun showBottomSheet() {
        _showBottomSheet.value = true
    }

    fun hideBottomSheet() {
        _showBottomSheet.value = false
    }

    fun setOriginCountry(country: CountryUIModel) {
        _selectedOriginCountry.value = country
        selectionCallback.originSelection(country)
        getDestinationCountries(country.iso3)

        CalculatorInteractorImpl.getInstance().updateCalculatorWithOriginAndPayout(
            Pair(country.iso3, country.name),
            Pair(_selectedDestinationCountry.value.iso3, _selectedDestinationCountry.value.name),
        )
    }

    fun setDestinationCountry(country: CountryUIModel) {
        _selectedDestinationCountry.value = country
        selectionCallback.destinationSelection(country)

        CalculatorInteractorImpl.getInstance().updateCalculatorWithOriginAndPayout(
            Pair(_selectedOriginCountry.value.iso3, _selectedOriginCountry.value.name),
            Pair(country.iso3, country.name),
        )

        addRecentCountryUsed(country)
    }

    private fun addRecentCountryUsed(country: CountryUIModel) {
        if (_recentCountriesUsed.value.isEmpty()) {
            _recentCountriesUsed.value.add(
                swCountryListWithSectionsItemUIModelMapper.mapToLastUsedHeader(),
            )
            _filteredDestinationList.value.add(
                0, swCountryListWithSectionsItemUIModelMapper.mapToAllCountriesHeader(),
            )
        }

        val countryAlreadyAdded = _recentCountriesUsed.value
            .filterIsInstance<SWCountryListWithSectionsItemUIModel.Item>()
            .firstOrNull { it.country.iso3 == country.iso3 }

        if (countryAlreadyAdded == null) {
            _recentCountriesUsed.value.add(swCountryListWithSectionsItemUIModelMapper.mapToItem(country))
            if (_recentCountriesUsed.value.size > 6) {
                _recentCountriesUsed.value.removeAt(1)
            }
        }
    }
}
