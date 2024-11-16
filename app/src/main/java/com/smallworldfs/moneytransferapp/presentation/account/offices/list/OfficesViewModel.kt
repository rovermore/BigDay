package com.smallworldfs.moneytransferapp.presentation.account.offices.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.peek
import com.smallworldfs.moneytransferapp.domain.migrated.base.then
import com.smallworldfs.moneytransferapp.domain.migrated.offices.usecase.OfficesUseCase
import com.smallworldfs.moneytransferapp.presentation.account.offices.model.OfficeUIModel
import com.smallworldfs.moneytransferapp.presentation.account.offices.model.OfficesUIModelMapper
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.base.ErrorTypeMapper
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel
import com.smallworldfs.moneytransferapp.presentation.freeuser.CountryUIModelMapper
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfficesViewModel @Inject constructor(
    private val officesUseCase: OfficesUseCase,
    private val errorTypeMapper: ErrorTypeMapper,
    private val countryUIModelMapper: CountryUIModelMapper,
    private val officesUIModelMapper: OfficesUIModelMapper
) : ViewModel() {

    private val _selectedCountry: MutableStateFlow<CountryUIModel> = MutableStateFlow(CountryUIModel())
    val selectedCountry: StateFlow<CountryUIModel> get() = _selectedCountry.asStateFlow()

    private val _countryList: MutableStateFlow<List<CountryUIModel>> = MutableStateFlow(emptyList())
    val countryList: StateFlow<List<CountryUIModel>> get() = _countryList.asStateFlow()

    private val _cityList: MutableStateFlow<List<String>> = MutableStateFlow(emptyList())
    val cityList: StateFlow<List<String>> get() = _cityList.asStateFlow()

    private var officeList: List<OfficeUIModel> = emptyList()

    private val _filteredOfficeList: MutableStateFlow<List<OfficeUIModel>> = MutableStateFlow(emptyList())
    val filteredOfficeList: StateFlow<List<OfficeUIModel>> get() = _filteredOfficeList.asStateFlow()

    private val _filterSelected: MutableStateFlow<String> = MutableStateFlow(STRING_EMPTY)
    val filterSelected: StateFlow<String> get() = _filterSelected.asStateFlow()

    private val _officesError: MutableStateFlow<ErrorType> = MutableStateFlow(ErrorType.None)
    val officesError: StateFlow<ErrorType> get() = _officesError.asStateFlow()

    init {
        getUserCountry()
    }

    private fun getUserCountry() {
        viewModelScope.launch(Dispatchers.IO) {
            officesUseCase.getUser()
                .peek {
                    _selectedCountry.value = countryUIModelMapper.mapToUIModel(it.country.countries.first())
                }.then {
                    getAllCountries()
                }
        }
    }

    private fun getAllCountries() {
        viewModelScope.launch(Dispatchers.IO) {
            officesUseCase.getAllOfficeCountries()
                .map {
                    _countryList.value = countryUIModelMapper.mapToUIModel(it.countries).sortedBy { it.name }
                }
                .mapFailure {
                    _officesError.value = errorTypeMapper.map(it)
                }.then {
                    getOffices()
                    getCities()
                }
        }
    }

    private fun getCities() {
        viewModelScope.launch(Dispatchers.IO) {
            officesUseCase.getCities(selectedCountry.value.iso3)
                .map { cityDTOLIst ->
                    val cityList = mutableListOf<String>()
                    cityDTOLIst.forEach { cityList.add(it.name) }
                    _cityList.value = cityList.sorted()
                }.mapFailure {
                    _officesError.value = errorTypeMapper.map(it)
                }
        }
    }

    private fun getOffices(country: String = STRING_EMPTY, city: String = STRING_EMPTY) {
        viewModelScope.launch(Dispatchers.IO) {
            officesUseCase.getOffices(country.ifEmpty { selectedCountry.value.iso3 }, city)
                .map {
                    val officesUIModel = officesUIModelMapper.map(it)
                    officeList = officesUIModel
                    _filteredOfficeList.value = officesUIModel
                }.mapFailure {
                    _officesError.value = errorTypeMapper.map(it)
                }
        }
    }

    fun setSelectedCountry(countryIso: String, name: String) {
        _selectedCountry.value = CountryUIModel(countryIso, name)
        _filterSelected.value = name
        getOffices()
        getCities()
    }

    fun filterByCity(city: String) {
        _filteredOfficeList.value = if (city.isEmpty()) officeList else officeList.filter { it.city == city }
        _filterSelected.value = city
    }

    fun hideErrorView() {
        _officesError.value = ErrorType.None
    }
}
