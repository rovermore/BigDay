package com.smallworldfs.moneytransferapp.presentation.forgotpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.forgotpassword.usecase.ForgotPasswordUseCase
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.base.ErrorTypeMapper
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel
import com.smallworldfs.moneytransferapp.presentation.freeuser.CountryUIModelMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.util.Collections.emptyList
import javax.inject.Inject

@HiltViewModel
class ForgotPasswordViewModel @Inject constructor(
    private val forgotPasswordUseCase: ForgotPasswordUseCase,
    private val countryUIModelMapper: CountryUIModelMapper,
    private val errorTypeMapper: ErrorTypeMapper
) : ViewModel() {

    private val _countries = MutableStateFlow<List<CountryUIModel>>(emptyList())
    val countries: StateFlow<List<CountryUIModel>> = _countries.asStateFlow()

    private val _countrySelected = MutableStateFlow(CountryUIModel())
    val countrySelected: StateFlow<CountryUIModel> = _countrySelected.asStateFlow()

    private val _showCountries = MutableStateFlow(false)
    val showCountries: StateFlow<Boolean> = _showCountries.asStateFlow()

    private val _successForgotPassword: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val successForgotPassword: StateFlow<Boolean> get() = _successForgotPassword.asStateFlow()

    private val _errorForgotPassword = MutableStateFlow<ErrorType>(ErrorType.None)
    val errorForgotPassword: StateFlow<ErrorType> = _errorForgotPassword.asStateFlow()

    init {
        requestCountries()
    }

    fun requestForgotPassword(email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            forgotPasswordUseCase.requestForgotPassword(email, countrySelected.value.iso3)
                .map {
                    _successForgotPassword.value = it
                }.mapFailure {
                    showErrorView(errorTypeMapper.map(it))
                }
        }
    }

    private fun requestCountries() {
        viewModelScope.launch(Dispatchers.IO) {
            forgotPasswordUseCase.getCountries()
                .map { countriesDTO ->
                    val countryListUIModel = countryUIModelMapper.mapToUIModel(countriesDTO.countries)
                    _countries.value = countryListUIModel
                    _countrySelected.value = countryListUIModel[0]
                }
        }
    }

    fun updateCountrySelected(country: CountryUIModel) {
        _countrySelected.value = country
    }

    fun showCountries(show: Boolean) {
        _showCountries.value = show
    }

    fun showErrorView(error: ErrorType) {
        _errorForgotPassword.value = error
    }

    fun hideErrorView() {
        _errorForgotPassword.value = ErrorType.None
    }

    fun hideDialog() {
        _successForgotPassword.value = false
    }
}
