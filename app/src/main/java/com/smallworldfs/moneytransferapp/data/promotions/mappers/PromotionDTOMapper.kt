package com.smallworldfs.moneytransferapp.data.promotions.mappers

import com.smallworldfs.moneytransferapp.data.calculator.model.CalculateRateValues
import com.smallworldfs.moneytransferapp.data.promotions.model.Promotion
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.promotions.model.CalculatorPromotionDTO
import com.smallworldfs.moneytransferapp.domain.migrated.promotions.model.PromotionDTO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import javax.inject.Inject

class PromotionDTOMapper @Inject constructor() {

    fun map(promotions: List<Promotion?>?): OperationResult<List<PromotionDTO>, Error> {
        val promotionsDTO: List<PromotionDTO> = promotions?.map {
            PromotionDTO(
                it?.promotionName ?: STRING_EMPTY,
                it?.promotionCode ?: STRING_EMPTY,
                it?.type ?: STRING_EMPTY,
                it?.countryOrigin ?: STRING_EMPTY,
                it?.countryDestination ?: STRING_EMPTY,
                it?.discount ?: STRING_EMPTY,
                it?.expireDate ?: STRING_EMPTY,
            )
        } ?: return Failure(Error.UncompletedOperation("Could not map Promotion Response"))

        return Success(promotionsDTO)
    }

    fun mapToCalculatorPromotion(rateValues: CalculateRateValues) =
        with(rateValues) {
            CalculatorPromotionDTO(
                promotionName,
                discount,
                promotionNumber,
                autoAssigned,
                promotionType,
                promotionCode,
            )
        }

    fun mapToPromotion(rateValues: CalculateRateValues) =
        with(rateValues) {
            PromotionDTO(
                promotionNumber,
                promotionType,
                STRING_EMPTY,
                discount.toString(),
            )
        }
}
