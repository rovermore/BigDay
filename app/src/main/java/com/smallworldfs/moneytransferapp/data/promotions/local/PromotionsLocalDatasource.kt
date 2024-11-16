package com.smallworldfs.moneytransferapp.data.promotions.local

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.promotions.model.CalculatorPromotionDTO
import com.smallworldfs.moneytransferapp.domain.migrated.promotions.model.PromotionDTO
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PromotionsLocalDatasource @Inject constructor() {

    private var calculatorPromotion: CalculatorPromotionDTO? = null

    private var promotionsAvailables = mutableListOf<PromotionDTO>()

    private var promotionSelected: PromotionDTO? = null

    fun getCalculatorPromotion(): OperationResult<CalculatorPromotionDTO, Error> {
        return if (calculatorPromotion != null)
            Success(calculatorPromotion!!)
        else
            Failure(Error.ReadDataError("Calculator promotion not found"))
    }

    fun setCalculatorPromotion(promotion: CalculatorPromotionDTO): OperationResult<Boolean, Error> {
        calculatorPromotion = promotion
        return Success(true)
    }

    fun clearCalculatorPromotion(): OperationResult<Boolean, Error> {
        calculatorPromotion = null
        return Success(true)
    }

    fun getListPromotionsAvailable(): OperationResult<List<PromotionDTO>, Error> {
        return if (promotionsAvailables.isNotEmpty())
            Success(promotionsAvailables.toList())
        else
            Failure(Error.ReadDataError("Promotion list not found"))
    }

    fun setListPromotionsAvailable(promotions: List<PromotionDTO>): OperationResult<Boolean, Error> {
        promotionsAvailables = promotions.toMutableList()
        return Success(true)
    }

    fun clearListPromotionsAvailable(): OperationResult<Boolean, Error> {
        promotionsAvailables.clear()
        return Success(true)
    }

    fun setPromotionSelected(promotion: PromotionDTO): OperationResult<Boolean, Error> {
        promotionSelected = promotion
        return Success(true)
    }

    fun getPromotionSelected(): OperationResult<PromotionDTO, Error> {
        return if (promotionSelected != null)
            Success(promotionSelected!!)
        else
            Failure(Error.ReadDataError("Promotion selected by user not found"))
    }
}
