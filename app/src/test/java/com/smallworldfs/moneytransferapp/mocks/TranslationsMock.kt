package com.smallworldfs.moneytransferapp.mocks

object TranslationsMock {

    val paymentMethodTranslated = HashMap<String, String>().apply {
        put("BANKWIRE", "Transferencia Bancaria")
        put("WORLDPAY", "Pago por Tarjeta")
        put("SOFORT", "SOFORT")
    }
}
