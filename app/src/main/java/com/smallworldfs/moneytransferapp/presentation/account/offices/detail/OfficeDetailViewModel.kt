package com.smallworldfs.moneytransferapp.presentation.account.offices.detail

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
import com.smallworldfs.moneytransferapp.presentation.common.coordinates.SWCoordinates
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.STRING_NULL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OfficeDetailViewModel @Inject constructor(
    private val officesUseCase: OfficesUseCase,
    private val errorTypeMapper: ErrorTypeMapper,
    private val officesUIModelMapper: OfficesUIModelMapper
) : ViewModel() {

    private val _officePoiList = MutableStateFlow<List<OfficeUIModel>>(emptyList())
    val officePoiList: StateFlow<List<OfficeUIModel>> get() = _officePoiList.asStateFlow()

    private val _userLocation = MutableStateFlow<SWCoordinates>(SWCoordinates.NotDefined)
    val userLocation: StateFlow<SWCoordinates> get() = _userLocation.asStateFlow()

    private val _distance = MutableStateFlow<String>(STRING_EMPTY)
    val distance: StateFlow<String> get() = _distance.asStateFlow()

    private val _officesError = MutableStateFlow<ErrorType>(ErrorType.None)
    val officesError: StateFlow<ErrorType> get() = _officesError.asStateFlow()

    private val _userLocationError = MutableStateFlow<ErrorType>(ErrorType.None)
    val userLocationError: StateFlow<ErrorType> get() = _userLocationError.asStateFlow()

    fun getUserLocation(officeLocation: SWCoordinates) {
        viewModelScope.launch(Dispatchers.IO) {
            officesUseCase.getUserLocation()
                .map { userCoordinates ->
                    _userLocation.value = userCoordinates
                }.mapFailure {
                    _userLocationError.value = errorTypeMapper.map(it)
                }.then {
                    calculateDistance(userLocation.value, officeLocation)
                }
        }
    }

    fun calculateDistance(userLocation: SWCoordinates, officeLocation: SWCoordinates) {
        if (userLocation !is SWCoordinates.NotDefined)
            viewModelScope.launch(Dispatchers.IO) {
                officesUseCase.calculateDistance(userLocation, officeLocation)
                    .peek { distance ->
                        _distance.value = distance
                    }
            }
    }

    fun getOfficePoi() {
        viewModelScope.launch(Dispatchers.IO) {
            officesUseCase.getOfficesPoi(STRING_NULL, STRING_NULL)
                .map { offices ->
                    _officePoiList.value = officesUIModelMapper.mapOfficePoi(offices)
                        .filter { it.location is SWCoordinates.LatitudeLongitude }
                }.mapFailure {
                    _officesError.value = errorTypeMapper.map(it)
                }
        }
    }

    fun hideOfficesError() {
        _officesError.value = ErrorType.None
    }
}
