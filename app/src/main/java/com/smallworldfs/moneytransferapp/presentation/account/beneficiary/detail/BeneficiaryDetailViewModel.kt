package com.smallworldfs.moneytransferapp.presentation.account.beneficiary.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.Event
import com.smallworldfs.moneytransferapp.data.account.beneficiary.model.BeneficiaryDTOMapper
import com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.detail.usecase.BeneficiaryDetailUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.peek
import com.smallworldfs.moneytransferapp.domain.migrated.base.then
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Beneficiary
import com.smallworldfs.moneytransferapp.modules.calculator.domain.interactor.CalculatorInteractor.PassiveCallback
import com.smallworldfs.moneytransferapp.modules.calculator.domain.interactor.implementation.CalculatorInteractorImpl
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.CreateTransactionResponse
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.detail.model.BeneficiaryActivityUIModel
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.detail.model.BeneficiaryActivityUIModelMapper
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.model.BeneficiaryUIModel
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.model.BeneficiaryUIModelMapper
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.base.ErrorTypeMapper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BeneficiaryDetailViewModel @Inject constructor(
    private val beneficiaryDetailUseCase: BeneficiaryDetailUseCase,
    private val errorTypeMapper: ErrorTypeMapper,
    private val beneficiaryActivityUIModelMapper: BeneficiaryActivityUIModelMapper,
    private val beneficiaryUIModelMapper: BeneficiaryUIModelMapper,
    private val beneficiaryDTOMapper: BeneficiaryDTOMapper
) : ViewModel() {

    private val _beneficiaryDeleted: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val beneficiaryDeleted: LiveData<Event<Boolean>> get() = _beneficiaryDeleted

    private val _calculatorUpdated: MutableLiveData<Event<BeneficiaryUIModel>> = MutableLiveData()
    val calculatorUpdated: LiveData<Event<BeneficiaryUIModel>> get() = _calculatorUpdated

    private val _beneficiaryActivity: MutableLiveData<Event<BeneficiaryActivityUIModel>> = MutableLiveData()
    val beneficiaryActivity: LiveData<Event<BeneficiaryActivityUIModel>> get() = _beneficiaryActivity

    private val _beneficiaryError: MutableLiveData<Event<ErrorType>> = MutableLiveData()
    val beneficiaryError: LiveData<Event<ErrorType>> get() = _beneficiaryError

    private val _showProgressDialog: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val showProgressDialog: LiveData<Event<Boolean>> get() = _showProgressDialog

    private val _showDialogCheckout: MutableLiveData<Event<CreateTransactionResponse>> = MutableLiveData()
    val showDialogCheckout: LiveData<Event<CreateTransactionResponse>> get() = _showDialogCheckout

    private val _showEditBeneficiary: MutableLiveData<Event<Beneficiary>> = MutableLiveData()
    val showEditBeneficiary: LiveData<Event<Beneficiary>> get() = _showEditBeneficiary

    private val _refreshBeneficiary: MutableLiveData<Event<Boolean>> = MutableLiveData()
    val refreshBeneficiary: LiveData<Event<Boolean>> get() = _refreshBeneficiary

    lateinit var beneficiary: BeneficiaryUIModel
        private set

    fun deleteBeneficiary() {
        viewModelScope.launch(Dispatchers.IO) {
            beneficiaryDetailUseCase.deleteBeneficiary(this@BeneficiaryDetailViewModel.beneficiary.id)
                .peek {
                    _beneficiaryDeleted.postValue(Event(it))
                }.mapFailure {
                    _beneficiaryError.postValue(Event(errorTypeMapper.map(it)))
                }
        }
    }

    fun getBeneficiaryActivity() {
        _showProgressDialog.value = Event(true)
        viewModelScope.launch(Dispatchers.IO) {
            beneficiaryDetailUseCase.getBeneficiaryActivity(this@BeneficiaryDetailViewModel.beneficiary.id)
                .peek {
                    _beneficiaryActivity.postValue(Event(beneficiaryActivityUIModelMapper.map(it)))
                }.mapFailure {
                    _beneficiaryError.postValue(Event(errorTypeMapper.map(it)))
                }.then {
                    _showProgressDialog.postValue(Event(false))
                }
        }
    }

    fun setBeneficiary(beneficiary: BeneficiaryUIModel) {
        this.beneficiary = beneficiary
    }

    fun beneficiaryEdited(beneficiary: Beneficiary) {
        this.beneficiary = beneficiaryUIModelMapper.map(beneficiaryDTOMapper.map(beneficiary))
        _refreshBeneficiary.value = Event(true)
    }

    fun updateCalculatorWithBeneficiary() {
        CalculatorInteractorImpl.getInstance().addPassiveCallback(
            object : PassiveCallback {
                override fun onCalculatorChanges(amountBeneficiaryReceive: String?, amountYouPay: String?, currencyPayout: String?) {
                    _calculatorUpdated.value = Event(beneficiary)
                    _showProgressDialog.value = Event(false)
                    CalculatorInteractorImpl.getInstance().removePassiveCallback(this)
                }

                override fun onCalculatorUpdatedWithBeneficiary(beneficiary: BeneficiaryUIModel?) {}
                override fun onCalculatorError() {
                    _beneficiaryError.value = Event(ErrorType.UnmappedError("Could not set beneficiary in calculator"))
                }
            },
        )
        _showProgressDialog.value = Event(true)
        CalculatorInteractorImpl.getInstance().updateCalculatorWithBeneficiary(null, beneficiary)
    }

    fun showDialogCheckout(transactionCreated: CreateTransactionResponse?) {
        transactionCreated?.let { _showDialogCheckout.value = Event(it) }
    }

    fun editBeneficiary() {
        val beneficiary = beneficiaryActivityUIModelMapper.map(beneficiary)
        _showEditBeneficiary.value = Event(beneficiary)
    }
}
