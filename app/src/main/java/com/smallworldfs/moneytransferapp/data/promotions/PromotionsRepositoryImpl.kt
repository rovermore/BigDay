package com.smallworldfs.moneytransferapp.data.promotions

import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.data.promotions.local.PromotionsLocalDatasource
import com.smallworldfs.moneytransferapp.data.promotions.mappers.PromotionDTOMapper
import com.smallworldfs.moneytransferapp.data.promotions.network.PromotionsNetworkDatasource
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.promotions.model.CalculatorPromotionDTO
import com.smallworldfs.moneytransferapp.domain.migrated.promotions.model.PromotionDTO
import com.smallworldfs.moneytransferapp.domain.migrated.promotions.repository.PromotionsRepository
import javax.inject.Inject

class PromotionsRepositoryImpl @Inject constructor(
    private val promotionsLocalDatasource: PromotionsLocalDatasource,
    private val promotionsNetworkDatasource: PromotionsNetworkDatasource,
    private val promotionDTOMapper: PromotionDTOMapper,
    private val apiErrorMapper: APIErrorMapper,
) : PromotionsRepository {

    override fun requestPromotions(originCountry: String, payoutCountry: String, clientId: String): OperationResult<List<PromotionDTO>, Error> {
        return promotionsNetworkDatasource.requestPromotions(originCountry, payoutCountry, clientId)
            .map {
                return promotionDTOMapper.map(it.promotions)
            }.mapFailure {
                return Failure(apiErrorMapper.map(it))
            }
    }

    override fun clearCalculatorPromotion(): OperationResult<Boolean, Error> {
        return promotionsLocalDatasource.clearCalculatorPromotion()
    }

    override fun setCalculatorPromotion(promotion: CalculatorPromotionDTO): OperationResult<Boolean, Error> {
        return promotionsLocalDatasource.setCalculatorPromotion(promotion)
    }

    override fun getCalculatorPromotion(): OperationResult<CalculatorPromotionDTO, Error> {
        return promotionsLocalDatasource.getCalculatorPromotion()
    }

    override fun setPromotionsAvailable(promotions: List<PromotionDTO>): OperationResult<Boolean, Error> {
        return promotionsLocalDatasource.setListPromotionsAvailable(promotions)
    }

    override fun getPromotionsAvailable(): OperationResult<List<PromotionDTO>, Error> {
        return promotionsLocalDatasource.getListPromotionsAvailable()
    }

    override fun clearPromotionsAvailable(): OperationResult<Boolean, Error> {
        return promotionsLocalDatasource.clearListPromotionsAvailable()
    }

    override fun setPromotionSelected(promotion: PromotionDTO): OperationResult<Boolean, Error> {
        return promotionsLocalDatasource.setPromotionSelected(promotion)
    }

    override fun getPromotionSelected(): OperationResult<PromotionDTO, Error> {
        return promotionsLocalDatasource.getPromotionSelected()
    }

    override fun getPromotionCodePresent(): OperationResult<String, Error> {
        return getCalculatorPromotion().map {
            it.promotionCode
        }.mapFailure {
            return getPromotionSelected().map {
                it.promotionCode
            }
        }
    }

    override fun getDiscountAndTypePromotion(): OperationResult<Pair<String, Double>, Error> {
        return getCalculatorPromotion().map {
            return Success(Pair(it.promotionType, it.discount))
        }.mapFailure {
            return getPromotionSelected().map {
                return Success(Pair(it.discountType, it.discount.toDouble()))
            }
        }
    }
}
