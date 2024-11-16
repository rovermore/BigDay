package com.smallworldfs.moneytransferapp.presentation.freeuser.country_selection

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.compose.widgets.model.SWCountryListWithSectionsItemUIModel
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.peek
import com.smallworldfs.moneytransferapp.domain.migrated.base.peekFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.then
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountriesDTO
import com.smallworldfs.moneytransferapp.domain.migrated.freeuser.CountrySelectionUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.domain.model.Coordinates
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel
import com.smallworldfs.moneytransferapp.presentation.freeuser.CountryUIModelMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CountrySelectionViewModel @Inject constructor(
    private val countrySelectionUseCase: CountrySelectionUseCase,
    private val countryUIModelMapper: CountryUIModelMapper,
    private val swCountryListWithSectionsItemUIModelMapper: SWCountryListWithSectionsItemUIModelMapper
) : ViewModel() {

    lateinit var origin: CountryUIModel
        private set
    lateinit var destination: CountryUIModel
        private set

    private val _originCountries = MutableStateFlow<List<SWCountryListWithSectionsItemUIModel>>(emptyList())
    val originCountries: StateFlow<List<SWCountryListWithSectionsItemUIModel>> = _originCountries.asStateFlow()

    private val _filteredOriginList = MutableStateFlow<List<SWCountryListWithSectionsItemUIModel>>(emptyList())
    val filteredOriginList: StateFlow<List<SWCountryListWithSectionsItemUIModel>> = _filteredOriginList.asStateFlow()

    private val _filteredDestinationList = MutableStateFlow<List<SWCountryListWithSectionsItemUIModel>>(emptyList())
    val filteredDestinationList: StateFlow<List<SWCountryListWithSectionsItemUIModel>> = _filteredDestinationList.asStateFlow()

    private val _freeUserCreated = MutableStateFlow(UserDTO())
    val freeUserCreated = _freeUserCreated.asStateFlow()

    private val _selectedOriginCountry = MutableStateFlow(CountryUIModel())
    val selectedOriginCountry: StateFlow<CountryUIModel> = _selectedOriginCountry.asStateFlow()

    private val _selectedDestinationCountry = MutableStateFlow(CountryUIModel())
    val selectedDestinationCountry: StateFlow<CountryUIModel> = _selectedDestinationCountry.asStateFlow()

    private val _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _featuredCountries = MutableStateFlow<List<CountryUIModel>>(emptyList())
    val featuredCountries: StateFlow<List<CountryUIModel>> = _featuredCountries.asStateFlow()

    private val _destinationCountries = MutableStateFlow<List<SWCountryListWithSectionsItemUIModel>>(emptyList())
    val destinationCountries = _destinationCountries.asStateFlow()

    init {
        getUserLocation()
    }

    fun getDestinationCountries(iso: String) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            countrySelectionUseCase.getDestinationCountries(iso)
                .map {
                    _featuredCountries.value = countryUIModelMapper.mapToUIModel(it.countries.filter { it.featured })
                    _destinationCountries.value = swCountryListWithSectionsItemUIModelMapper.mapToItem(countryUIModelMapper.mapToUIModel(it.countries).sortedBy { it.name })
                    _filteredDestinationList.value = _destinationCountries.value
                }
                .then {
                    _loading.value = false
                }
        }
    }

    fun getOriginCountries(lat: Double, long: Double) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            countrySelectionUseCase.getOriginCountries(Coordinates.LatLon(lat, long))
                .map {
                    updateCountries(it)
                }
                .then {
                    _loading.value = false
                }
        }
    }

    fun getOriginCountries() {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            countrySelectionUseCase.getOriginCountries(Coordinates.InvalidCoordinates)
                .map {
                    updateCountries(it)
                }
                .then {
                    _loading.value = false
                }
        }
    }

    private suspend fun updateCountries(countriesDTO: CountriesDTO) {
        _originCountries.value = swCountryListWithSectionsItemUIModelMapper
            .mapToItem(countryUIModelMapper.mapToUIModel(countriesDTO.countries).sortedBy { it.name })
        _filteredOriginList.value = _originCountries.value
        countriesDTO.countries.find { it.featured }?.let {
            val countryUIModel = countryUIModelMapper.mapToUIModel(it)
            saveOriginCountry(countryUIModel)
            _selectedOriginCountry.emit(countryUIModel)
        }
    }

    fun saveOriginCountry(country: CountryUIModel) {
        origin = country
        viewModelScope.launch(Dispatchers.IO) {
            countrySelectionUseCase.setOriginCountry(countryUIModelMapper.mapToDTO(selectedOriginCountry.value))
            _selectedOriginCountry.emit(country)
        }
    }

    fun filterOrigin(query: String) {
        _filteredOriginList.value = _originCountries.value.filterIsInstance<SWCountryListWithSectionsItemUIModel.Item>().filter { item ->
            item.country.name.contains(query, ignoreCase = true)
        }
    }

    fun filterDestination(query: String) {
        _filteredDestinationList.value = _destinationCountries.value.filterIsInstance<SWCountryListWithSectionsItemUIModel.Item>().filter { item ->
            item.country.name.contains(query, ignoreCase = true)
        }
    }

    fun saveDestinationCountry(country: CountryUIModel) {
        onOnboardingEnd()
        destination = country
        viewModelScope.launch(Dispatchers.IO) {
            countrySelectionUseCase.setDestinationCountry(countryUIModelMapper.mapToDTO(country))
                .peek {
                    createFreeUser()
                }
            _selectedDestinationCountry.value = country
        }
    }

    private fun createFreeUser() {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            countrySelectionUseCase.createFreeUser(
                countryUIModelMapper.mapToDTO(origin),
                countryUIModelMapper.mapToDTO(destination),
            ).peek {
                _freeUserCreated.value = it
            }.then {
                _loading.value = false
            }
        }
    }

    fun getUserLocation() {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            countrySelectionUseCase.getUserLocation()
                .peek {
                    getOriginCountries(it.latitude, it.longitude)
                }.peekFailure {
                    getOriginCountries()
                }.then {
                    _loading.value = false
                }
        }
    }

    private fun onOnboardingEnd() {
        countrySelectionUseCase.endOnboarding()
    }
}
