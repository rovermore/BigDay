package com.smallworldfs.moneytransferapp.presentation.promotions.model

import com.smallworldfs.moneytransferapp.domain.migrated.promotions.model.PromotionDTO
import org.joda.time.DateTime
import org.joda.time.Days
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class PromotionUIModelMapper @Inject constructor() {

    fun map(promotionsDTO: List<PromotionDTO>, currency: String) =
        promotionsDTO.map { map(it, currency) }

    fun map(promotionDTO: PromotionDTO, currency: String): PromotionUIModel =
        with(promotionDTO) {
            PromotionUIModel(
                name = promotionName,
                origin = countryOrigin,
                destination = countryDestination,
                expireDate = expireDate,
                code = promotionCode,
                daysRemaining = getNumOfDaysRemaining(expireDate),
                discount = discount,
                discountType = discountType,
                currency = currency,
                selected = false,
            )
        }

    fun map(promotionUIModel: PromotionUIModel): PromotionDTO =
        with(promotionUIModel) {
            PromotionDTO(
                promotionName = name,
                promotionCode = code,
                countryOrigin = origin,
                countryDestination = destination,
                discount = discount,
                expireDate = expireDate,
                discountType = discountType,
            )
        }

    private fun getNumOfDaysRemaining(expireDate: String): Int {
        val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val promotionDate = formatter.parse(expireDate)
        val promotionDateTime = DateTime(promotionDate)
        return Days.daysBetween(DateTime().toLocalDate(), promotionDateTime.toLocalDate()).days + 1
    }
}
