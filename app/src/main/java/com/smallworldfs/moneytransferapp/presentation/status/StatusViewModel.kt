package com.smallworldfs.moneytransferapp.presentation.status

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.peek
import com.smallworldfs.moneytransferapp.domain.migrated.base.then
import com.smallworldfs.moneytransferapp.domain.migrated.status.usecase.StatusUseCase
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.base.ErrorTypeMapper
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionStatusUIModel
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModelMapper
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatusViewModel @Inject constructor(
    private val statusUseCase: StatusUseCase,
    private val errorTypeMapper: ErrorTypeMapper,
    private val transactionUIModelMapper: TransactionUIModelMapper
) : ViewModel() {

    private var mtn: String = STRING_EMPTY

    private var _statusError: MutableStateFlow<ErrorType> = MutableStateFlow(ErrorType.None)
    val statusError = _statusError.asStateFlow()

    private var _transactionStatusUIModel: MutableStateFlow<TransactionStatusUIModel> = MutableStateFlow(TransactionStatusUIModel())
    val transactionStatusUIModel = _transactionStatusUIModel.asStateFlow()

    private var _isRefreshing = MutableStateFlow(false)
    val isRefreshing = _isRefreshing.asStateFlow()

    private var _cancelDialog = MutableStateFlow(false)
    val cancelDialog = _cancelDialog.asStateFlow()

    private var _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private var _transactionCancelled = MutableStateFlow(STRING_EMPTY)
    val transactionCancelled = _transactionCancelled.asStateFlow()

    private var _transactionCancelledError: MutableStateFlow<ErrorType> = MutableStateFlow(ErrorType.None)
    val transactionCancelledError = _transactionCancelledError.asStateFlow()

    init {
        getTransactions()
    }

    fun getTransactions() {
        _loading.value = true
        _statusError.value = ErrorType.None
        viewModelScope.launch(Dispatchers.IO) {
            statusUseCase.getTransactions()
                .peek {
                    _transactionStatusUIModel.value = transactionUIModelMapper.mapToTransactionStatusUIModel(it.cancellationMessage, it.cancellable, it.transactions)
                }.mapFailure {
                    _statusError.value = errorTypeMapper.map(it)
                }
                .then {
                    _loading.value = false
                }
        }
    }

    fun cancelTransaction() {
        dismissTransactionCancelledError()
        dismissCancelDialog()
        if (mtn.isNotEmpty()) {
            _loading.value = true
            viewModelScope.launch(Dispatchers.IO) {
                statusUseCase.cancelTransaction(mtn)
                    .peek {
                        _transactionCancelled.value = it
                    }.mapFailure {
                        _transactionCancelledError.value = errorTypeMapper.map(it)
                    }.then {
                        _loading.value = false
                    }
            }
        }
    }

    fun dismissCancelDialog() {
        _cancelDialog.value = false
    }

    fun showCancellationDialog(mtn: String) {
        this.mtn = mtn
        _cancelDialog.value = true
    }

    fun dismissTransactionCancelledError() {
        _transactionCancelled.value = STRING_EMPTY
        _transactionCancelledError.value = ErrorType.None
    }
}
