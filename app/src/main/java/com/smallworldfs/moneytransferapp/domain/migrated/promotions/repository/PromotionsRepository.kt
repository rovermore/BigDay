package com.smallworldfs.moneytransferapp.domain.migrated.promotions.repository

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.promotions.model.CalculatorPromotionDTO
import com.smallworldfs.moneytransferapp.domain.migrated.promotions.model.PromotionDTO

interface PromotionsRepository {

    fun requestPromotions(
        originCountry: String,
        payoutCountry: String,
        clientId: String
    ): OperationResult<List<PromotionDTO>, Error>

    fun clearCalculatorPromotion(): OperationResult<Boolean, Error>
    fun setCalculatorPromotion(promotion: CalculatorPromotionDTO): OperationResult<Boolean, Error>
    fun getCalculatorPromotion(): OperationResult<CalculatorPromotionDTO, Error>

    fun setPromotionsAvailable(promotions: List<PromotionDTO>): OperationResult<Boolean, Error>
    fun getPromotionsAvailable(): OperationResult<List<PromotionDTO>, Error>
    fun clearPromotionsAvailable(): OperationResult<Boolean, Error>

    fun setPromotionSelected(promotion: PromotionDTO): OperationResult<Boolean, Error>
    fun getPromotionSelected(): OperationResult<PromotionDTO, Error>

    fun getPromotionCodePresent(): OperationResult<String, Error>
    fun getDiscountAndTypePromotion(): OperationResult<Pair<String, Double>, Error>
}
