package com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.SmallWorldApplication
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.BaseStateViewModel
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.Event
import com.smallworldfs.moneytransferapp.data.transactional.cashpickup.model.RequestCashPickUpChooseLocationDataModel
import com.smallworldfs.moneytransferapp.data.transactional.cashpickup.model.RequestCashPickUpLocationsDataModel
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.transactional.cashpickup.usecase.GetPickUpLocationsUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.transactional.cashpickup.usecase.SetTheChoosePickUpLocationsUseCase
import com.smallworldfs.moneytransferapp.presentation.form.selector.FormSelectorItem
import com.smallworldfs.moneytransferapp.presentation.form.selector.FormSelectorState
import com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.CashPickUpMapNavigator.Companion.REQUEST_CODE_CITY
import com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.CashPickUpMapNavigator.Companion.REQUEST_CODE_PAYMENT_NETWORK
import com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.CashPickUpMapNavigator.Companion.REQUEST_CODE_STATE
import com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.utils.CashPickUpMarkerPresentationModel
import com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.utils.CashPickupResultModel
import com.smallworldfs.moneytransferapp.utils.Constants.DELIVERY_METHODS.CASH_PICKUP
import com.smallworldfs.moneytransferapp.utils.DOUBLE_ZERO
import com.smallworldfs.moneytransferapp.utils.INT_ONE
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import javax.inject.Inject

class CashPickUpMapViewModel @Inject constructor(private val getPickUpLocationsUseCase: GetPickUpLocationsUseCase, private val setTheChoosePickUpLocationsUseCase: SetTheChoosePickUpLocationsUseCase) : BaseStateViewModel<CashPickUpMapState, CashPickUpMapNavigator>() {

    override val initialViewState: CashPickUpMapState = CashPickUpMapState()

    private val _mapLocations: MutableLiveData<Event<List<CashPickUpMarkerPresentationModel>>> = MutableLiveData()
    var mapLocations: LiveData<Event<List<CashPickUpMarkerPresentationModel>>> = _mapLocations

    private val _selectedLocation: MutableLiveData<Event<CashPickUpMarkerPresentationModel>> = MutableLiveData()
    var selectedLocation: LiveData<Event<CashPickUpMarkerPresentationModel>> = _selectedLocation

    /**
     * On start the first time
     */
    override fun onStartFirstTime(statePreloaded: Boolean) {}

    /**
     * Actions of the user
     */
    fun onActionOpenStateSelector(screenName: String) {
        checkDataState { state ->
            val listToShow: MutableList<FormSelectorItem> = mutableListOf()
            state.stateList.forEach { listToShow.add(FormSelectorItem(it, it)) }
            if (listToShow.size > INT_ONE) {
                navigator.navigateToFormSelectFromCashPickUpMap(FormSelectorState(SmallWorldApplication.getStr(R.string.activityCashPickUpMapChooseState), listToShow, true, screenName = screenName), REQUEST_CODE_STATE)
            }
        }
    }

    fun onActionOpenStateCitySelector(screenName: String) {
        checkDataState { state ->
            val listToShow: MutableList<FormSelectorItem> = mutableListOf()
            state.cityList.forEach { listToShow.add(FormSelectorItem(it, it)) }
            if (listToShow.size > INT_ONE) {
                navigator.navigateToFormSelectFromCashPickUpMap(FormSelectorState(SmallWorldApplication.getStr(R.string.activityCashPickUpMapChooseCity), listToShow, true, screenName = screenName), REQUEST_CODE_CITY)
            }
        }
    }

    fun onActionOpenPaymentNetworkSelector(screenName: String) {
        checkDataState { state ->
            val listToShow: MutableList<FormSelectorItem> = mutableListOf()
            state.paymentNetworkList.forEach { listToShow.add(FormSelectorItem(it, it)) }
            if (listToShow.size > INT_ONE) {
                navigator.navigateToFormSelectFromCashPickUpMap(FormSelectorState(SmallWorldApplication.getStr(R.string.activityCashPickUpMapChoosePaymentNetwork), listToShow, true, screenName = screenName), REQUEST_CODE_PAYMENT_NETWORK)
            }
        }
    }

    fun onActionNewFieldSelected(fieldSelected: Int, value: String) {
        updateToAlternativeState()

        checkDataState { state ->

            when (fieldSelected) {
                REQUEST_CODE_STATE -> {
                    // Change the state selected
                    val stateListSelected = state.stateList.indexOf(value)

                    // Check if the user select all states/provinces
                    if (stateListSelected != INT_ZERO) {

                        // Update the list of cities that match with this state
                        var cityList = mutableListOf<String>()
                        state.allLocationResponses.filter { !it.locationCity.isNullOrEmpty() && !it.locationState.isNullOrEmpty() && it.locationState == value }.forEach {
                            it.locationCity?.let { location ->
                                cityList.add(location)
                            }
                        }
                        cityList = cityList.distinct().sorted().toMutableList()
                        cityList.add(INT_ZERO, SmallWorldApplication.getStr(R.string.activityCashPickUpMapAllCities))

                        // Change the city selected
                        val cityListSelected = INT_ZERO

                        // Update view state
                        updateToNormalState {
                            copy(
                                cityList = cityList,
                                stateListSelected = stateListSelected,
                                cityListSelected = cityListSelected,
                                locationSelected = null
                            )
                        }
                    } else {
                        // Create list of cities
                        val cityList = mutableListOf(SmallWorldApplication.getStr(R.string.activityCashPickUpMapAllCities))

                        // Update view state
                        updateToNormalState {
                            copy(
                                cityList = cityList,
                                stateListSelected = stateListSelected,
                                cityListSelected = INT_ZERO,
                                locationSelected = null,
                                searchBoxValue = STRING_EMPTY
                            )
                        }
                    }
                }
                REQUEST_CODE_CITY -> {
                    // Change the city selected
                    val cityListSelected = state.cityList.indexOf(value)

                    // Update view state
                    updateToNormalState {
                        copy(
                            cityListSelected = cityListSelected,
                            locationSelected = null
                        )
                    }
                }
                REQUEST_CODE_PAYMENT_NETWORK -> {
                    // Change the payment Network selected
                    val paymentNetworkListSelected = state.paymentNetworkList.indexOf(value)

                    // Update view state
                    updateToNormalState {
                        copy(
                            paymentNetworkListSelected = paymentNetworkListSelected,
                            locationSelected = null
                        )
                    }
                }
            }
        }
    }

    fun onActionChangeSearchText(textToSearch: String) {
        checkDataState { state ->
            if (state.searchBoxValue != textToSearch) {
                updateToNormalState { copy(searchBoxValue = textToSearch) }
            }
        }
    }

    fun onActionSelectLocation(location: CashPickUpMarkerPresentationModel) {
        _selectedLocation.value = Event(location)
    }

    fun onActionSelectLocationButton() {
        updateToAlternativeState()
        userDataRepository.getLoggedUser()
            .map { user ->
                checkDataState { state ->
                    _selectedLocation.value?.peekContent()?.let { selected ->
                        executeUseCaseWithException({
                            setTheChoosePickUpLocationsUseCase.execute(
                                RequestCashPickUpChooseLocationDataModel(
                                    userId = user.id,
                                    userToken = user.userToken,
                                    deliveryMethod = CASH_PICKUP,
                                    locationCode = selected.locationCode ?: STRING_EMPTY,
                                    representativeCode = selected.representativeCode.toString(),
                                    beneficiaryId = state.beneficiaryId,
                                    pickUpFee = selected.fee.toString(),
                                    pickUpRate = selected.rate ?: STRING_EMPTY
                                )
                            )

                            navigator.navigateToPreviousScreenGivingResult(
                                CashPickupResultModel(
                                    representativeName = selected.representativeName ?: STRING_EMPTY,
                                    locationAddress = selected.locationAddress ?: STRING_EMPTY,
                                    fee = selected.fee ?: DOUBLE_ZERO,
                                    rate = selected.rate ?: STRING_EMPTY,
                                    deliveryTime = selected.deliveryTime ?: STRING_EMPTY,
                                    locationCode = selected.locationCode ?: STRING_EMPTY,
                                    representativeCode = selected.representativeCode.toString()
                                )
                            )
                        }, {
                            updateToErrorState()
                        })
                    }
                }
            }
    }

    /**
     * Request information
     */
    fun requestInformation() {
        updateToAlternativeState()
        userDataRepository.getLoggedUser()
            .map { user ->
                checkDataState { state ->
                    executeUseCaseWithException({

                        // Execute use case and get locations
                        val response = getPickUpLocationsUseCase.execute(RequestCashPickUpLocationsDataModel(userToken = user.userToken, userId = user.id, amount = state.amount, currencyType = state.currencyType, currencyOrigin = state.currencyOrigin, beneficiaryId = state.beneficiaryId))

                        // Create list of states
                        var stateList = mutableListOf<String>()

                        response?.locationResponses?.let {
                            for (location in it) {
                                location.locationState?.let { locationState ->
                                    if (locationState.isNotEmpty())
                                        stateList.add(locationState)
                                }
                            }
                        }
                        stateList = stateList.distinct().sorted().toMutableList()
                        stateList.add(INT_ZERO, SmallWorldApplication.getStr(R.string.activityCashPickUpMapAllStatesProvinces))

                        // Create list of cities
                        var cityList = mutableListOf<String>()
                        response?.locationResponses?.filter {
                            !it.locationCity.isNullOrEmpty()
                        }?.forEach {
                            it.locationCity?.let { location ->
                                cityList.add(location)
                            }
                        }
                        cityList = cityList.distinct().sorted().toMutableList()
                        cityList.add(INT_ZERO, SmallWorldApplication.getStr(R.string.activityCashPickUpMapAllCities))

                        // Create list of payers
                        var paymentNetworkList = mutableListOf<String>()
                        response?.locationResponses?.filter { !it.representativeName.isNullOrEmpty() }?.forEach {
                            it.representativeName?.let { paymentNetwork ->
                                paymentNetworkList.add(paymentNetwork)
                            }
                        }
                        paymentNetworkList = paymentNetworkList.distinct().sorted().toMutableList()
                        paymentNetworkList.add(INT_ZERO, SmallWorldApplication.getStr(R.string.activityCashPickUpMapAllLocalPartners))

                        // Points to draw in the map
                        val locationsInMap = mutableListOf<CashPickUpMarkerPresentationModel>()
                        response?.locationResponses?.filter { !it.latitude.isNullOrEmpty() && !it.longitude.isNullOrEmpty() }?.forEach {
                            locationsInMap.add(it.toCashPickUpMarkerPresentationModel())
                        }
                        _mapLocations.value = Event(locationsInMap.toList())

                        // Save and refresh view
                        updateToNormalState {
                            copy(
                                allLocationResponses = response?.locationResponses ?: emptyList(),
                                stateList = stateList,
                                cityList = cityList,
                                paymentNetworkList = paymentNetworkList,
                                stateListSelected = INT_ZERO,
                                cityListSelected = INT_ZERO,
                                paymentNetworkListSelected = INT_ZERO
                            )
                        }
                    }, {
                        updateToErrorState()
                    })
                }
            }.mapFailure {
                updateToErrorState()
            }
    }
}
