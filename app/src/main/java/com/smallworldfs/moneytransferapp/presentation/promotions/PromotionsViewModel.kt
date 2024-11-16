package com.smallworldfs.moneytransferapp.presentation.promotions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.then
import com.smallworldfs.moneytransferapp.domain.migrated.promotions.usecase.PromotionsUseCase
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.base.ErrorTypeMapper
import com.smallworldfs.moneytransferapp.presentation.promotions.model.PromotionUIModel
import com.smallworldfs.moneytransferapp.presentation.promotions.model.PromotionUIModelMapper
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromotionsViewModel @Inject constructor(
    private val promotionsUseCase: PromotionsUseCase,
    private val promotionUIModelMapper: PromotionUIModelMapper,
    private val errorTypeMapper: ErrorTypeMapper
) : ViewModel() {

    private val _error: MutableStateFlow<ErrorType> = MutableStateFlow(ErrorType.None)
    val error: StateFlow<ErrorType> get() = _error.asStateFlow()

    private val _sendingCurrency: MutableStateFlow<String> = MutableStateFlow(STRING_EMPTY)
    val sendingCurrency: StateFlow<String> get() = _sendingCurrency.asStateFlow()

    private val _promotions: MutableStateFlow<List<PromotionUIModel>> = MutableStateFlow(emptyList())
    val promotions: StateFlow<List<PromotionUIModel>> get() = _promotions.asStateFlow()

    private val _selectedPromotion: MutableStateFlow<PromotionUIModel> = MutableStateFlow(PromotionUIModel())
    val selectedPromotion: StateFlow<PromotionUIModel> get() = _selectedPromotion.asStateFlow()

    private val _validPromotion: MutableStateFlow<String> = MutableStateFlow(STRING_EMPTY)
    val validPromotion: StateFlow<String> get() = _validPromotion.asStateFlow()

    private val _destinationCountry: MutableStateFlow<String> = MutableStateFlow(STRING_EMPTY)
    val destinationCountry: StateFlow<String> get() = _destinationCountry.asStateFlow()

    private val _showAddCodeDialog: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val showAddCodeDialog: StateFlow<Boolean> get() = _showAddCodeDialog.asStateFlow()

    private val _loading: MutableStateFlow<Boolean> = MutableStateFlow(false)
    val loading: StateFlow<Boolean> get() = _loading.asStateFlow()

    private val _promotionInvalid: MutableStateFlow<ErrorType> = MutableStateFlow(ErrorType.None)
    val promotionInvalid: StateFlow<ErrorType> get() = _promotionInvalid.asStateFlow()

    fun loadInformation(payoutCountry: String) {
        viewModelScope.launch(Dispatchers.IO) {
            promotionsUseCase.getSendingCurrency()
                .map { currency ->
                    _sendingCurrency.value = currency
                    promotionsUseCase.getPromotions(payoutCountry)
                        .map {
                            _promotions.value = promotionUIModelMapper.map(it, currency)
                        }
                }.mapFailure {
                    _error.value = errorTypeMapper.map(it)
                }
        }
    }

    fun getSelectedPromotion() {
        promotionsUseCase.getSelectedPromotion()
            .map {
                _selectedPromotion.value = promotionUIModelMapper.map(it, _sendingCurrency.value)
            }
    }

    fun checkPromotion(code: String) {
        _loading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            promotionsUseCase.checkPromotion(code)
                .map {
                    _validPromotion.value = code
                }.mapFailure {
                    _promotionInvalid.value = errorTypeMapper.map(it)
                }.then {
                    _loading.value = false
                }
        }
    }

    fun selectPromotion(promotion: PromotionUIModel) {
        viewModelScope.launch(Dispatchers.IO) {
            promotionsUseCase.setSelectedPromotion(promotionUIModelMapper.map(promotion))
                .mapFailure {
                    _error.value = errorTypeMapper.map(it)
                }
        }
    }

    fun updateSelectedPromotion() {
        viewModelScope.launch(Dispatchers.IO) {
            promotionsUseCase.getSelectedPromotion()
                .map {
                    _selectedPromotion.value = promotionUIModelMapper.map(it, _sendingCurrency.value)
                }.mapFailure {
                    _error.value = errorTypeMapper.map(it)
                }
        }
    }

    fun getDestinationCountry() {
        viewModelScope.launch(Dispatchers.IO) {
            promotionsUseCase.getDestinationCountry()
                .map {
                    _destinationCountry.value = it
                }.mapFailure {
                    _error.value = errorTypeMapper.map(it)
                }
        }
    }

    fun showAddCodeDialog() {
        _showAddCodeDialog.value = true
    }

    fun hideAddCodeDialog() {
        _showAddCodeDialog.value = false
    }

    fun hidePromotionInvalidError() {
        _promotionInvalid.value = ErrorType.None
    }

    fun hideGenericErrorView() {
        _error.value = ErrorType.None
    }
}
