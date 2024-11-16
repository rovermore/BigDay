package com.smallworldfs.moneytransferapp.mocks.dto

import com.smallworldfs.moneytransferapp.domain.migrated.calculator.model.DeliveryMethodDTO

object DeliveryMethodDTOMock {

    val deliveryMethodsTranslated = mutableListOf<DeliveryMethodDTO>().apply {
        add(DeliveryMethodDTO("BANK_DEPOSIT", "Depósito bancario"))
        add(DeliveryMethodDTO("ON_CALL", "Pago en cualquier punto"))
        add(DeliveryMethodDTO("PICK_UP", "Entrega en ventanilla"))
    }
}
