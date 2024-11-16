package com.smallworldfs.moneytransferapp.presentation.common.countries

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.searchcountry.SearchCountryUseCase
import com.smallworldfs.moneytransferapp.presentation.freeuser.CountryUIModelMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SearchCountryViewModel @Inject constructor(
    private val searchCountryUseCase: SearchCountryUseCase,
    private val countryUIModelMapper: CountryUIModelMapper
) : ViewModel() {

    private val _countries = MutableStateFlow<List<CountryUIModel>>(emptyList())
    val countries: StateFlow<List<CountryUIModel>> = _countries.asStateFlow()

    fun getDestinationCountries() {
        viewModelScope.launch(Dispatchers.IO) {
            searchCountryUseCase.getDestinationCountries().map {
                _countries.value = countryUIModelMapper.mapToUIModel(it.countries)
            }
        }
    }
}
