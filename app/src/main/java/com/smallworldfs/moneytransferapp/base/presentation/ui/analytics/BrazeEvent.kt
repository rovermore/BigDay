package com.smallworldfs.moneytransferapp.base.presentation.ui.analytics

class BrazeEvent(
    val name: String,
    var properties: Map<String, String>,
    val type: BrazeEventType = BrazeEventType.ACTION
)

enum class BrazeEventName(val value: String) {
    REGISTER_FIRST_STEP("Registration Starting 1st step"),
    REGISTER_SECOND_STEP("Registration starting 2nd step"),
    REGISTER_THIRD_STEP("Registration starting 3rd step"),
    FULL_REGISTRATION("Full Registration"),
    LOGIN_OK("Login OK"),
    LOGIN_KO("Login KO"),
    SELECT_PROMOTIONAL_CODE("Select Promotional Code"),
    RESET_PASSWORD("Reset Password"),
    REGISTRATION_DESTINATION("Registration Destination"),
    RESEND_SMS("Resend SMS"),
    PRIME_FOR_PUSH("prime-for-push-app"),

    BENEFICIARY_CREATION_STEP_1("Beneficiary Creation 1st Step"),
    BENEFICIARY_CREATION_STEP_2("Beneficiary Creation 2nd Step"),
    BENEFICIARY_CREATION_STEP_3("Beneficiary Creation 3rd Step"),
    BENEFICIARY_CREATION_STEP_4("Beneficiary Creation 4th Step"),
    BENEFICIARY_CREATED("Beneficiary Created"),
    BENEFICIARY_KO("Beneficiary KO"),

    TRANSACTION_CREATION_STEP_1("Transaction 1st Step"),
    TRANSACTION_CREATION_STEP_2("Transaction 2nd Step"),
    TRANSACTION_CANCELLED("Transaction Cancelled"),
    TRANSACTION_CREATED("Transaction Created"),
    TRANSACTION_KO("Transaction KO")
}

enum class BrazeEventProperty(val value: String) {
    SCREEN_TYPE("Screen Type"),
    DESTINATION_COUNTRY("Destination Country"),
    BENEFICIARY_FULL_NAME("Beneficiary Full Name"),
    PAYER("Payer"),
    DELIVERY_METHOD("Delivery Method"),
    PAYMENT_METHOD("Payment Method"),
    PAID_AMOUNT("Paid amount"),
    REGISTER_COUNTRY("Registration Destination Country"),
    PRODUCT_ID("productId"),
    CURRENCY_CODE("currencyCode"),
    PRICE("price"),
    QUANTITY("quantity"),
    SENDING_COUNTRY("Sending Country"),
    PROMOTIONAL_CODE("Promotional Code"),
    PROBLEM("Problem"),
    REGISTRATION_SENDING_COUNTRY("Registration Sending Country"),
    REGISTRATION_DESTINATION_COUNTRY("Registration Destination Country"),
    ERROR_REASON("Error Reason"),
    BENEFICIARY_TYPE("Send Money To")
}

enum class BrazeEventType(val value: String) {
    ACTION("Action"),
    PURCHASE("Purchase")
}
