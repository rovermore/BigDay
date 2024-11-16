package com.smallworldfs.moneytransferapp.presentation.status.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.peek
import com.smallworldfs.moneytransferapp.domain.migrated.base.peekFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.then
import com.smallworldfs.moneytransferapp.domain.migrated.status.usecase.TransactionStatusDetailUseCase
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.FormData
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.base.ErrorTypeMapper
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.PayTransactionUIModel
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionDetailsUIModelMapper
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModel
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class TransactionStatusDetailViewModel @Inject constructor(
    private val transactionStatusDetailUseCase: TransactionStatusDetailUseCase,
    private val transactionDetailsUIModelMapper: TransactionDetailsUIModelMapper,
    private val errorTypeMapper: ErrorTypeMapper
) : ViewModel() {

    private val _transactionDetail: MutableStateFlow<TransactionUIModel> = MutableStateFlow(TransactionUIModel())
    val transactionDetail: StateFlow<TransactionUIModel> get() = _transactionDetail.asStateFlow()

    private val _errorOperation: MutableStateFlow<ErrorType> = MutableStateFlow(ErrorType.None)
    val errorOperation: StateFlow<ErrorType> get() = _errorOperation.asStateFlow()

    private val _topErrorOperation: MutableStateFlow<ErrorType> = MutableStateFlow(ErrorType.None)
    val topErrorOperation: StateFlow<ErrorType> get() = _topErrorOperation.asStateFlow()

    private val _payTransaction: MutableStateFlow<PayTransactionUIModel> = MutableStateFlow(PayTransactionUIModel())
    val payTransaction: StateFlow<PayTransactionUIModel> get() = _payTransaction.asStateFlow()

    private val _requestPaymentMethods: MutableStateFlow<FormData> = MutableStateFlow(FormData())
    val requestPaymentMethods: StateFlow<FormData> get() = _requestPaymentMethods.asStateFlow()

    private val _changePaymentMethod: MutableStateFlow<String> = MutableStateFlow(STRING_EMPTY)
    val changePaymentMethod: StateFlow<String> get() = _changePaymentMethod.asStateFlow()

    private val _pdfReceived: MutableStateFlow<File?> = MutableStateFlow(null)
    val pdfReceived: StateFlow<File?> = _pdfReceived

    private var _transactionCancelled: MutableStateFlow<String> = MutableStateFlow(STRING_EMPTY)
    val transactionCancelled: StateFlow<String> get() = _transactionCancelled.asStateFlow()

    private var _transactionCancelledError: MutableStateFlow<ErrorType> = MutableStateFlow(ErrorType.None)
    val transactionCancelledError: StateFlow<ErrorType> get() = _transactionCancelledError.asStateFlow()

    private val _showChangedPaymentMethodDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showChangedPaymentMethodDialog: StateFlow<Boolean> get() = _showChangedPaymentMethodDialog.asStateFlow()

    private val _showChangePaymentMethodDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showChangePaymentMethodDialog: StateFlow<Boolean> get() = _showChangePaymentMethodDialog.asStateFlow()

    private val _showSuccessCancellationDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showSuccessCancellationDialog: StateFlow<Boolean> get() = _showSuccessCancellationDialog.asStateFlow()

    private val _showGenericErrorDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showGenericErrorDialog: StateFlow<Boolean> get() = _showGenericErrorDialog.asStateFlow()

    private val _showCancelDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showCancelDialog: StateFlow<Boolean> get() = _showCancelDialog.asStateFlow()

    private val _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading.asStateFlow()

    fun getTransaction(mtn: String, offline: Boolean) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            transactionStatusDetailUseCase.getTransactionDetail(mtn, offline)
                .peek {
                    _transactionDetail.value = transactionDetailsUIModelMapper.map(it).transaction
                }.peekFailure {
                    _errorOperation.value = errorTypeMapper.map(it)
                }.then {
                    _loading.value = false
                }
        }
    }

    fun showReceipt() {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val transaction = _transactionDetail.value
            transactionStatusDetailUseCase.getReceipt(transaction.offline, transaction.mtn, false)
                .peek {
                    _pdfReceived.value = it
                }.peekFailure {
                    _topErrorOperation.value = errorTypeMapper.map(it)
                }.then {
                    _loading.value = false
                }
        }
    }

    fun showPreReceipt() {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val transaction = _transactionDetail.value
            transactionStatusDetailUseCase.getReceipt(transaction.offline, transaction.mtn, true)
                .peek {
                    _pdfReceived.value = it
                }.peekFailure {
                    _topErrorOperation.value = errorTypeMapper.map(it)
                }.then {
                    _loading.value = false
                }
        }
    }

    fun cancelTransaction() {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val transaction = _transactionDetail.value
            transactionStatusDetailUseCase.cancelTransaction(transaction.mtn)
                .peek {
                    _transactionCancelled.value = it
                }.mapFailure {
                    _transactionCancelledError.value = errorTypeMapper.map(it)
                }.then {
                    _loading.value = false
                }
        }
    }

    fun requestPaymentMethods(paymentMethod: String) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val transaction = _transactionDetail.value
            transactionStatusDetailUseCase.requestGetPaymentMethodToChangeToBankTransfer(transaction.senderCountry, paymentMethod)
                .peek {
                    _requestPaymentMethods.value = it
                }.peekFailure {
                    _topErrorOperation.value = errorTypeMapper.map(it)
                }.then {
                    _loading.value = false
                }
        }
    }

    fun onBankSelected(depositBankBranchId: String, depositBankId: String) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val transaction = _transactionDetail.value
            transactionStatusDetailUseCase.changePayment(transaction.paymentMethod, transaction.mtn, depositBankBranchId, depositBankId)
                .peek {
                    _changePaymentMethod.value = it
                }.peekFailure {
                    _topErrorOperation.value = errorTypeMapper.map(it)
                }.then {
                    _loading.value = false
                }
        }
    }

    fun payTransaction() {
        val transaction = _transactionDetail.value
        if (transaction.boleto) {
            _payTransaction.value = PayTransactionUIModel(transaction.mtn, transaction.paymentUrl)
        } else {
            _payTransaction.value = PayTransactionUIModel(transaction.mtn)
        }
    }

    fun showChangedPaymentMethodDialog(changedPaymentMethod: String) {
        _changePaymentMethod.value = changedPaymentMethod
        _showChangedPaymentMethodDialog.value = true
    }

    fun hideChangedPaymentMethodDialog() {
        _showChangedPaymentMethodDialog.value = false
    }

    fun showChangePaymentMethodDialog() {
        _showChangePaymentMethodDialog.value = true
    }

    fun hideChangePaymentMethodDialog() {
        _showChangePaymentMethodDialog.value = false
    }

    fun showGenericErrorDialog() {
        _showGenericErrorDialog.value = true
    }

    fun hideGenericErrorDialog() {
        _showGenericErrorDialog.value = false
    }

    fun showSuccessCancellationDialog() {
        _showSuccessCancellationDialog.value = true
    }

    fun hideSuccessCancellationDialog() {
        _showSuccessCancellationDialog.value = false
    }

    fun hideErrorCancellationDialog() {
        _transactionCancelledError.value = ErrorType.None
    }

    fun showCancelDialog() {
        _showCancelDialog.value = true
    }

    fun hideCancelDialog() {
        _showCancelDialog.value = false
    }

    fun showTopError() {
        _topErrorOperation.value = ErrorType.GenericError(STRING_EMPTY)
    }

    fun hideTopError() {
        _topErrorOperation.value = ErrorType.None
    }

    fun hideErrorScreen() {
        _errorOperation.value = ErrorType.None
    }
}
