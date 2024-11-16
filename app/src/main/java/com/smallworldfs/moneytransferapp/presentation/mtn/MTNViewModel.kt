package com.smallworldfs.moneytransferapp.presentation.mtn

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.Event
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.peek
import com.smallworldfs.moneytransferapp.domain.migrated.base.then
import com.smallworldfs.moneytransferapp.domain.migrated.mtn.usecase.MTNUseCase
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.base.ErrorTypeMapper
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel
import com.smallworldfs.moneytransferapp.presentation.freeuser.CountryUIModelMapper
import com.smallworldfs.moneytransferapp.presentation.mtn.model.MtnQrErrorUIModel
import com.smallworldfs.moneytransferapp.presentation.mtn.model.MtnStatusUIModel
import com.smallworldfs.moneytransferapp.presentation.mtn.model.MtnStatusUIModelMapper
import com.smallworldfs.moneytransferapp.utils.parseQueryParamFromUrl
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Collections.emptyList
import javax.inject.Inject

@HiltViewModel
class MTNViewModel @Inject constructor(
    private val mtnUseCase: MTNUseCase,
    private val countryUIModelMapper: CountryUIModelMapper,
    private val errorTypeMapper: ErrorTypeMapper,
    private val mtnStatusUIModelMapper: MtnStatusUIModelMapper
) : ViewModel() {

    var countries: MutableList<CountryUIModel> = mutableListOf()
        private set

    private val _loading: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val loading: LiveData<Event<Boolean>> get() = _loading

    private val _error: MutableLiveData<Event<ErrorType>> = MutableLiveData()
    val error: LiveData<Event<ErrorType>> get() = _error

    private val _qrError: MutableLiveData<Event<MtnQrErrorUIModel>> = MutableLiveData()
    val qrError: LiveData<Event<MtnQrErrorUIModel>> get() = _qrError

    private val _mtnStatus: MutableLiveData<Event<MtnStatusUIModel>> = MutableLiveData()
    val mtnStatus: LiveData<Event<MtnStatusUIModel>> get() = _mtnStatus

    fun trackQrTransaction(url: String) {
        viewModelScope.launch(Dispatchers.IO) {
            url.parseQueryParamFromUrl("mtn")
                .peek { mtn ->
                    url.parseQueryParamFromUrl("payout_country")
                        .peek { country ->
                            mtnUseCase.getMtnStatus(mtn, country)
                                .map {
                                    val mtnStatusUIModel = mtnStatusUIModelMapper.map(it)
                                    _mtnStatus.postValue(Event(mtnStatusUIModel.copy(isQrMode = true)))
                                }.mapFailure {
                                    _qrError.postValue(
                                        Event(
                                            MtnQrErrorUIModel(
                                                errorTypeMapper.map(it),
                                                country,
                                                mtn
                                            )
                                        )
                                    )
                                }.then {
                                    _loading.postValue(Event(false))
                                }
                        }
                }
        }
    }

    fun getMTNStatus(mtn: String, id: String) {
        _loading.value = Event(true)
        viewModelScope.launch(Dispatchers.IO) {
            mtnUseCase.getMtnStatus(mtn, id)
                .map {
                    _mtnStatus.postValue(Event(mtnStatusUIModelMapper.map(it)))
                }.mapFailure {
                    _error.postValue(Event(errorTypeMapper.map(it)))
                }.then {
                    _loading.postValue(Event(false))
                }
        }
    }

    fun getCountries() {
        _loading.value = Event(true)
        viewModelScope.launch(Dispatchers.IO) {
            mtnUseCase.getCountries()
                .map {
                    countries = (countryUIModelMapper.mapToUIModel(it.countries).sortedBy { it.name }).toMutableList()
                }.mapFailure {
                    countries = emptyList()
                    _error.postValue(Event(errorTypeMapper.map(it)))
                }.then {
                    _loading.postValue(Event(false))
                }
        }
    }
}
