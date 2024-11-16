package com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.fragments.common

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.Event
import com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.list.usecase.BeneficiaryListUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.model.BeneficiaryUIModel
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.model.BeneficiaryUIModelMapper
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.base.ErrorTypeMapper
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.TreeMap
import javax.inject.Inject

@HiltViewModel
class BeneficiaryListViewModel @Inject constructor(
    private val beneficiaryListUseCase: BeneficiaryListUseCase,
    private val beneficiaryUIModelMapper: BeneficiaryUIModelMapper,
    private val errorTypeMapper: ErrorTypeMapper
) : ViewModel() {

    companion object {
        const val PAGE_ITEMS = 10
    }

    private val _loadingLiveData: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val loadingLiveData: LiveData<Event<Boolean>> = _loadingLiveData

    private val _beneficiaryListLiveData: MutableLiveData<Event<List<BeneficiaryUIModel>>> = MutableLiveData()
    var beneficiaryListLiveData: LiveData<Event<List<BeneficiaryUIModel>>> = _beneficiaryListLiveData

    private val _errorBeneficiaryListLiveData: MutableLiveData<Event<ErrorType>> = MutableLiveData()
    var errorBeneficiaryListLiveData: LiveData<Event<ErrorType>> = _errorBeneficiaryListLiveData

    private val _deliveryMethodsLiveData: MutableLiveData<Event<TreeMap<String, String>>> = MutableLiveData()
    var deliveryMethodsLiveData: LiveData<Event<TreeMap<String, String>>> = _deliveryMethodsLiveData

    private val _keySelectedLiveData: MutableLiveData<Event<String>> = MutableLiveData()
    private var keySelectedLiveData: LiveData<Event<String>> = _keySelectedLiveData

    private fun requestListOfBeneficiaries(cleanList: Boolean) {
        val offset = if (cleanList) INT_ZERO else beneficiaryListLiveData.value?.peekContent()?.size ?: INT_ZERO
        requestMoreBeneficiaries(offset, cleanList)
    }

    private fun requestMoreBeneficiaries(offset: Int, cleanList: Boolean) {
        _loadingLiveData.value = Event(true)
        viewModelScope.launch(Dispatchers.IO) {
            beneficiaryListUseCase.getBeneficiaryList(
                keySelectedLiveData.value?.peekContent() ?: STRING_EMPTY,
                offset.toString(),
                PAGE_ITEMS.toString(),
            ).map {
                val beneficiaries = (_beneficiaryListLiveData.value?.peekContent()?.toMutableList() ?: mutableListOf())
                if (cleanList) {
                    beneficiaries.clear()
                }
                beneficiaries.addAll(beneficiaryUIModelMapper.map(it))
                _beneficiaryListLiveData.postValue(Event(beneficiaries))
            }.mapFailure {
                _errorBeneficiaryListLiveData.postValue(Event(errorTypeMapper.map(it)))
            }
        }
    }

    fun onViewInitialized() {
        requestListOfBeneficiaries(false)
    }

    fun requestListOfMethods() {
        viewModelScope.launch {
            beneficiaryListUseCase.getDeliveryMethods()
                .map { list ->
                    val treeMap = TreeMap<String, String>()
                    list.forEach {
                        treeMap[it.code] = it.translation
                    }
                    _deliveryMethodsLiveData.value = Event(treeMap)
                }.mapFailure {
                    _errorBeneficiaryListLiveData.value = Event(errorTypeMapper.map(it))
                }
        }
    }

    fun onActionRefreshLayout() {
        requestListOfBeneficiaries(true)
    }

    fun onActionScrollDownLoadMore() {
        requestListOfBeneficiaries(false)
    }

    fun onActionFilterIndexSelected(position: Int) {
        var filterKey = STRING_EMPTY

        for ((index, value) in deliveryMethodsLiveData.value?.peekContent()?.entries?.withIndex() ?: emptyList()) {
            if (index == position) {
                filterKey = value.key
            }
        }

        _keySelectedLiveData.value = Event(filterKey)
        requestListOfBeneficiaries(true)
    }
}
