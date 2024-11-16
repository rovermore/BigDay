package com.smallworldfs.moneytransferapp.domain.migrated.calculator.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class DeliveryMethodDTO(
    val code: String = STRING_EMPTY,
    val translation: String = STRING_EMPTY
)
