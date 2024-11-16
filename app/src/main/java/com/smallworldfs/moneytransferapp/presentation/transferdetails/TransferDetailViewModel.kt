package com.smallworldfs.moneytransferapp.presentation.transferdetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.base.ErrorTypeMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TransferDetailViewModel @Inject constructor(
    private val transferDetailUseCase: TransferDetailUseCase,
    private val errorTypeMapper: ErrorTypeMapper,
) : ViewModel() {

    private val _transferDetail = MutableStateFlow(TransferDetailUIModel())
    val transferDetail get() = _transferDetail.asStateFlow()

    private val _transferDetailError: MutableStateFlow<ErrorType> = MutableStateFlow(ErrorType.None)
    val transferDetailError: StateFlow<ErrorType> get() = _transferDetailError.asStateFlow()

    fun getTransferDetails(transaction: Transaction) {
        viewModelScope.launch(Dispatchers.IO) {
            transferDetailUseCase.getLoggedUserCountry()
                .map { country ->
                    transferDetailUseCase.getTransferDetail(transaction)
                        .map { transactionDetail ->
                            _transferDetail.value = TransferDetailUIModel(
                                details = transactionDetail,
                                country = country
                            )
                        }.mapFailure {
                            _transferDetailError.value = errorTypeMapper.map(it)
                        }
                }.mapFailure {
                    _transferDetailError.value = errorTypeMapper.map(it)
                }
        }
    }

    fun hideErrorDialog() {
        _transferDetailError.value = ErrorType.None
    }
}
