package com.smallworldfs.moneytransferapp.domain.migrated.promotions.usecase

import android.text.TextUtils
import com.smallworldfs.moneytransferapp.data.base.Cache
import com.smallworldfs.moneytransferapp.data.promotions.mappers.PromotionDTOMapper
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.calculator.model.CalculatorDataDTO
import com.smallworldfs.moneytransferapp.domain.migrated.calculator.repository.CalculatorRepository
import com.smallworldfs.moneytransferapp.domain.migrated.promotions.model.CalculatorPromotionDTO
import com.smallworldfs.moneytransferapp.domain.migrated.promotions.model.PromotionDTO
import com.smallworldfs.moneytransferapp.domain.migrated.promotions.repository.PromotionsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.modules.calculator.domain.interactor.implementation.CalculatorInteractorImpl
import com.smallworldfs.moneytransferapp.modules.country.domain.repository.CountryRepository
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import javax.inject.Inject

class PromotionsUseCase @Inject constructor(
    private val promotionsRepository: PromotionsRepository,
    private val userDataRepository: UserDataRepository,
    private val calculatorRepository: CalculatorRepository,
    private val calculatorCache: Cache,
    private val promotionDTOMapper: PromotionDTOMapper
) {

    fun getSendingCurrency(): OperationResult<String, Error> =
        Success(CountryRepository.getInstance().countryUserInfo.currencies.firstEntry()?.key ?: STRING_EMPTY)

    // TODO: Use promotionRepository.getPromotionsAvailable after SendTo refactor, the list will be initialized there
    fun getPromotions(payoutCountry: String): OperationResult<List<PromotionDTO>, Error> =
        userDataRepository.getLoggedUser()
            .map {
                return promotionsRepository.requestPromotions(
                    it.getCountry(),
                    payoutCountry,
                    it.id,
                )
            }

    fun setSelectedPromotion(promotion: PromotionDTO): OperationResult<Unit, Error> =
        promotionsRepository.setPromotionSelected(promotion)
            .map {
                promotionsRepository.clearCalculatorPromotion()
            }

    fun getSelectedPromotion(): OperationResult<PromotionDTO, Error> =
        promotionsRepository.getPromotionSelected()

    fun checkPromotion(code: String): OperationResult<Unit, Error> {
        val calculatorData = calculatorCache.calculatorData ?: CalculatorDataDTO()

        return userDataRepository.getLoggedUser()
            .map {

                val amount = if ((calculatorCache.calculatorData?.currencyType ?: STRING_EMPTY) == Constants.CALCULATOR.TOTALSALE) {
                    calculatorData.youPay
                } else {
                    calculatorData.amount
                }

                calculatorData.apply { this.amount = amount }

                val request =
                    with(calculatorData) {
                        mapOf(
                            "userId" to it.id,
                            "sendingCountry" to originCountry.iso3,
                            "sendingCurrency" to sendingCurrency.isoCode,
                            "payoutCountry" to payoutCountry.iso3,
                            "payoutCurrency" to payoutCurrency.isoCode,
                            "beneficiaryId" to beneficiaryId,
                            "operationType" to operationType,
                            "currencyType" to CalculatorInteractorImpl.getInstance().currencyType,
                            "amount" to amount,
                            "deliveryMethod" to deliveryMethod.code,
                            "promotionCode" to code,
                            "representativeCode" to representativeCode,
                        )
                    }

                return calculatorRepository.calculateRate(request)
                    .map { promotion ->
                        return checkIfExistPromotion(promotion.promotionAmount, promotionDTOMapper.mapToCalculatorPromotion(promotion))
                            .map { result ->
                                if (result) {
                                    promotionsRepository.setPromotionSelected(promotionDTOMapper.mapToPromotion(promotion))
                                } else {
                                    return Failure(Error.PromotionNotExists)
                                }
                            }
                    }
            }
    }

    private fun checkIfExistPromotion(promotionAmount: Double, promotion: CalculatorPromotionDTO): OperationResult<Boolean, Error> =
        if (!TextUtils.isEmpty(promotion.promotionName) && promotionAmount > 0) {
            promotionsRepository.setCalculatorPromotion(promotion)
                .map {
                    return Success(true)
                }
        } else {
            promotionsRepository.clearPromotionsAvailable()
                .map {
                    return Success(false)
                }
        }

    fun getDestinationCountry(): OperationResult<String, Error> =
        userDataRepository.getLoggedUser()
            .map {
                it.destinationCountry.iso3
            }
}
