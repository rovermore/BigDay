package com.smallworldfs.moneytransferapp.base.presentation.ui.analytics

import android.content.Context
import com.google.firebase.analytics.FirebaseAnalytics
import com.smallworldfs.moneytransferapp.data.braze.BrazeRepositoryImpl
import com.smallworldfs.moneytransferapp.modules.beneficiary.presentation.ui.activity.NewBeneficiaryStepCountryActivity
import com.smallworldfs.moneytransferapp.modules.c2b.presentation.ui.activity.C2BActivity
import com.smallworldfs.moneytransferapp.modules.calculator.presentation.ui.fragment.TransactionalCalculatorFragment
import com.smallworldfs.moneytransferapp.modules.checkout.presentation.ui.activity.CheckoutActivity
import com.smallworldfs.moneytransferapp.modules.status.presentation.ui.activity.SendEmailActivity
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.PayoutLocationSelectorActivity
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.TransactionalActivity
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.fragment.SendToFragment
import com.smallworldfs.moneytransferapp.presentation.account.account.AccountFragment
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.BeneficiaryListActivity
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.fragments.all.BeneficiaryListAllFragment
import com.smallworldfs.moneytransferapp.presentation.account.contact.SelectContactSupportActivity
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.MyDocumentsActivity
import com.smallworldfs.moneytransferapp.presentation.account.documents.selector.DocumentsSelectorActivity
import com.smallworldfs.moneytransferapp.presentation.account.offices.detail.OfficeDetailActivity
import com.smallworldfs.moneytransferapp.presentation.account.offices.list.OfficesActivity
import com.smallworldfs.moneytransferapp.presentation.account.profile.edit.EditProfileActivity
import com.smallworldfs.moneytransferapp.presentation.account.profile.show.ProfileActivity
import com.smallworldfs.moneytransferapp.presentation.autentix.DocumentValidationActivity
import com.smallworldfs.moneytransferapp.presentation.common.countries.SearchCountryActivity
import com.smallworldfs.moneytransferapp.presentation.forgotpassword.ForgotPasswordActivity
import com.smallworldfs.moneytransferapp.presentation.login.LoginActivity
import com.smallworldfs.moneytransferapp.presentation.myactivity.TransactionHistoryActivity
import com.smallworldfs.moneytransferapp.presentation.promotions.PromotionsActivity
import com.smallworldfs.moneytransferapp.presentation.quicklogin.PassCodeActivity
import com.smallworldfs.moneytransferapp.presentation.settings.SettingsActivity
import com.smallworldfs.moneytransferapp.presentation.softregister.credentials.RegisterCredentialsFragment
import com.smallworldfs.moneytransferapp.presentation.softregister.phone.RegisterPhoneFragment
import com.smallworldfs.moneytransferapp.presentation.softregister.profile.RegisterProfileFragment
import com.smallworldfs.moneytransferapp.presentation.splash.SplashActivity
import com.smallworldfs.moneytransferapp.presentation.status.StatusFragment
import com.smallworldfs.moneytransferapp.presentation.status.transaction.TransactionStatusDetailActivity
import com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.CashPickUpMapActivity
import com.smallworldfs.moneytransferapp.presentation.welcome.WelcomeActivity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AnalyticsSender @Inject constructor(context: Context, private val brazeRepositoryImpl: BrazeRepositoryImpl) {

    private var fireBaseAnalytics: FirebaseAnalytics = FirebaseAnalytics.getInstance(context)

    fun trackScreen(screenName: String) {
        val screenEvent = getScreenEventProperties(screenName)
        if (screenEvent.screenName.isNotEmpty()) {
            fireBaseAnalytics.logEvent("fbView", screenEvent.toBundle())
        }
    }

    fun trackScreenBraze(screenName: String, properties: Map<String, String>) {
        getBrazeEventProperties(screenName, properties)?.let { event ->
            brazeRepositoryImpl.logEvent(event)
        }
    }

    fun trackDialog(dialogName: String, screenCategory: String?) {
        val screenEvent = getDialogEventProperties(dialogName, screenCategory)
        if (screenEvent.screenName.isNotEmpty()) {
            fireBaseAnalytics.logEvent("fbView", screenEvent.toBundle())
        }
    }

    private fun getDialogEventProperties(
        dialogName: String,
        screenCategory: String?
    ) = when (dialogName) {
        ScreenName.ENTER_PASSCODE_SCREEN.value -> ScreenEvent(screenName = ScreenName.ENTER_PASSCODE_SCREEN.value, screenCategory = ScreenCategory.ACCESS.value)
        ScreenName.MODAL_SESSION_EXPIRED.value -> ScreenEvent(screenName = ScreenName.MODAL_SESSION_EXPIRED.value, screenCategory = screenCategory ?: "")
        ScreenName.MODAL_DISCARD_TRANSFER.value -> ScreenEvent(screenName = ScreenName.MODAL_DISCARD_TRANSFER.value, screenCategory = ScreenCategory.TRANSFER.value)
        ScreenName.MODAL_MAX_AMOUNT.value -> ScreenEvent(screenName = ScreenName.MODAL_MAX_AMOUNT.value, screenCategory = ScreenCategory.TRANSFER.value)
        ScreenName.END_SESSION_DIALOG.value -> ScreenEvent(screenName = ScreenName.END_SESSION_DIALOG.value, screenCategory = ScreenCategory.DASHBOARD.value)
        else -> ScreenEvent()
    }

    fun getScreenEventProperties(screenName: String): ScreenEvent {
        return when (screenName) {
            SplashActivity::class.java.simpleName -> ScreenEvent(screenName = ScreenName.SPLASH_SCREEN.value, screenCategory = ScreenCategory.ONBOARDING.value)
            WelcomeActivity::class.java.simpleName -> ScreenEvent(screenName = ScreenName.WELCOME_SCREEN.value, screenCategory = ScreenCategory.ONBOARDING.value)
            ScreenName.INFO_STRESS_FREE_SCREEN.value -> ScreenEvent(screenName = ScreenName.INFO_STRESS_FREE_SCREEN.value, screenCategory = ScreenCategory.ONBOARDING.value)
            ScreenName.INFO_SECURE_SCREEN.value -> ScreenEvent(screenName = ScreenName.INFO_SECURE_SCREEN.value, screenCategory = ScreenCategory.ONBOARDING.value)
            ScreenName.INFO_REAL_PEOPLE_SCREEN.value -> ScreenEvent(screenName = ScreenName.INFO_REAL_PEOPLE_SCREEN.value, screenCategory = ScreenCategory.ONBOARDING.value)
            ScreenName.SENDING_FROM_SCREEN.value -> ScreenEvent(screenName = ScreenName.SENDING_FROM_SCREEN.value, screenCategory = ScreenCategory.WELCOME.value)
            ScreenName.SENDING_TO_SCREEN.value -> ScreenEvent(screenName = ScreenName.SENDING_TO_SCREEN.value, screenCategory = ScreenCategory.WELCOME.value)
            ScreenName.ENTER_PASSCODE_SCREEN.value -> ScreenEvent(screenName = ScreenName.ENTER_PASSCODE_SCREEN.value, screenCategory = ScreenCategory.ACCESS.value)
            LoginActivity::class.java.simpleName -> ScreenEvent(screenName = ScreenName.LOGIN_SCREEN.value, screenCategory = ScreenCategory.ACCESS.value)
            ForgotPasswordActivity::class.java.simpleName -> ScreenEvent(screenName = ScreenName.HELP_PASSWORD_SCREEN.value, screenCategory = ScreenCategory.ACCESS.value)
            ScreenName.SEND_MONEY_FROM_SCREEN.value -> ScreenEvent(screenName = ScreenName.SEND_MONEY_FROM_SCREEN.value, screenCategory = ScreenCategory.ACCESS.value)
            PassCodeActivity::class.java.simpleName -> ScreenEvent(screenName = ScreenName.CREATE_PASSCODE_SCREEN.value, screenCategory = ScreenCategory.ACCESS.value)
            ScreenName.RECOVER_PASSCODE_EMAIL_DIALOG.value -> ScreenEvent(screenName = ScreenName.RECOVER_PASSCODE_EMAIL_DIALOG.value, screenCategory = ScreenCategory.ACCESS.value)
            RegisterCredentialsFragment::class.java.simpleName -> ScreenEvent(screenName = ScreenName.FORM_SOFT_REGISTER_SCREEN.value, screenCategory = ScreenCategory.ACCESS.value)
            RegisterPhoneFragment::class.java.simpleName -> ScreenEvent(screenName = ScreenName.PHONE_VALIDATION_SCREEN.value, screenCategory = ScreenCategory.ACCESS.value)
            ScreenName.CONFIRM_SMS_CODE_SCREEN.value -> ScreenEvent(screenName = ScreenName.CONFIRM_SMS_CODE_SCREEN.value, screenCategory = ScreenCategory.ACCESS.value)
            RegisterProfileFragment::class.java.simpleName -> ScreenEvent(screenName = ScreenName.PROFILE_SCREEN.value, screenCategory = ScreenCategory.ACCESS.value)
            SendToFragment::class.java.simpleName -> ScreenEvent(screenName = ScreenName.SEND_TO_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            ScreenName.SEND_MONEY_TO_SCREEN.value -> ScreenEvent(screenName = ScreenName.SEND_MONEY_TO_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            StatusFragment::class.java.simpleName -> ScreenEvent(screenName = ScreenName.STATUS_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            TransactionStatusDetailActivity::class.java.simpleName -> ScreenEvent(screenName = ScreenName.MANAGE_TRANSFER_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            AccountFragment::class.java.simpleName -> ScreenEvent(screenName = ScreenName.ACCOUNT_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            ScreenName.FORM_SOFT_REGISTER_SCREEN.value -> ScreenEvent(screenName = ScreenName.FORM_SOFT_REGISTER_SCREEN.value, screenCategory = ScreenCategory.ACCESS.value)
            ScreenName.PHONE_VALIDATION_SCREEN.value -> ScreenEvent(screenName = ScreenName.PHONE_VALIDATION_SCREEN.value, screenCategory = ScreenCategory.ACCESS.value)
            ScreenName.CONFIRM_SMS_CODE_SCREEN.value -> ScreenEvent(screenName = ScreenName.CONFIRM_SMS_CODE_SCREEN.value, screenCategory = ScreenCategory.ACCESS.value)
            ScreenName.PROFILE_SCREEN.value -> ScreenEvent(screenName = ScreenName.PROFILE_SCREEN.value, screenCategory = ScreenCategory.ACCESS.value)
            SendToFragment.SCREEN_NAME -> ScreenEvent(screenName = ScreenName.SEND_TO_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            ScreenName.SEND_MONEY_TO_SCREEN.value -> ScreenEvent(screenName = ScreenName.SEND_MONEY_TO_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            StatusFragment.SCREEN_NAME -> ScreenEvent(screenName = ScreenName.STATUS_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            TransactionStatusDetailActivity::class.java.simpleName -> ScreenEvent(screenName = ScreenName.MANAGE_TRANSFER_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            AccountFragment.SCREEN_NAME -> ScreenEvent(screenName = ScreenName.ACCOUNT_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            ProfileActivity::class.java.simpleName -> ScreenEvent(screenName = ScreenName.PROFILE_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            EditProfileActivity::class.java.simpleName -> ScreenEvent(screenName = ScreenName.EDIT_PROFILE_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            MyDocumentsActivity::class.java.simpleName -> ScreenEvent(screenName = ScreenName.MY_DOCUMENTS_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            TransactionHistoryActivity::class.java.simpleName -> ScreenEvent(screenName = ScreenName.MY_ACTIVITY_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            BeneficiaryListAllFragment::class.java.simpleName -> ScreenEvent(screenName = ScreenName.MY_BENEFICIARIES_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            BeneficiaryListActivity::class.java.simpleName -> ScreenEvent(screenName = ScreenName.MY_BENEFICIARIES_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            NewBeneficiaryStepCountryActivity::class.java.simpleName -> ScreenEvent(screenName = ScreenName.NEW_BENEFICIARY_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            OfficesActivity::class.java.simpleName -> ScreenEvent(screenName = ScreenName.OFFICES_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            OfficeDetailActivity::class.java.simpleName -> ScreenEvent(screenName = ScreenName.STORE_MAP_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            ScreenName.SEARCH_OFFICE_LOCATION_SCREEN.value -> ScreenEvent(screenName = ScreenName.SEARCH_OFFICE_LOCATION_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            SettingsActivity::class.java.simpleName -> ScreenEvent(screenName = ScreenName.SETTINGS_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            ScreenName.END_SESSION_DIALOG.value -> ScreenEvent(screenName = ScreenName.END_SESSION_DIALOG.value, screenCategory = ScreenCategory.DASHBOARD.value)
            SelectContactSupportActivity::class.java.simpleName -> ScreenEvent(screenName = ScreenName.CONTACT_SMALLWORLD_SCREEN.value, screenCategory = ScreenCategory.CONTACT.value)
            ScreenName.CUSTOMER_SERVICE_CHAT_SCREEN.value -> ScreenEvent(screenName = ScreenName.CUSTOMER_SERVICE_CHAT_SCREEN.value, screenCategory = ScreenCategory.CONTACT.value)
            SendEmailActivity::class.java.simpleName -> ScreenEvent(screenName = ScreenName.CUSTOMER_SERVICE_EMAIL_SCREEN.value, screenCategory = ScreenCategory.CONTACT.value)
            C2BActivity::class.java.simpleName -> ScreenEvent(screenName = ScreenName.BENEFICIARY_TYPE_SCREEN.value, screenCategory = ScreenCategory.SEND_MONEY_TO.value)
            ScreenName.MODAL_MAX_AMOUNT.value -> ScreenEvent(screenName = ScreenName.MODAL_MAX_AMOUNT.value, screenCategory = ScreenCategory.TRANSFER.value)
            ScreenName.SEND_MONEY_CONFIGURATION_SCREEN.value -> ScreenEvent(screenName = ScreenName.SEND_MONEY_CONFIGURATION_SCREEN.value, screenCategory = ScreenCategory.TRANSFER.value)
            ScreenName.TRANSACTION_PURPOSE_SCREEN.value -> ScreenEvent(screenName = ScreenName.TRANSACTION_PURPOSE_SCREEN.value, screenCategory = ScreenCategory.TRANSFER.value)
            ScreenName.RELATIONSHIP_WITH_BENEFICIARY_SCREEN.value -> ScreenEvent(screenName = ScreenName.RELATIONSHIP_WITH_BENEFICIARY_SCREEN.value, screenCategory = ScreenCategory.TRANSFER.value)
            ScreenName.BANK_DEPOSIT_PAYERS_SCREEN.value -> ScreenEvent(screenName = ScreenName.BANK_DEPOSIT_PAYERS_SCREEN.value, screenCategory = ScreenCategory.TRANSFER.value, checkoutStep = "1_checkoutPayer")
            ScreenName.AVAILABLE_PAYERS_SCREEN.value -> ScreenEvent(screenName = ScreenName.AVAILABLE_PAYERS_SCREEN.value, screenCategory = ScreenCategory.TRANSFER.value, checkoutStep = "1_checkoutPayer")
            ScreenName.PICKUP_LOCATIONS_SCREEN.value -> ScreenEvent(screenName = ScreenName.PICKUP_LOCATIONS_SCREEN.value, screenCategory = ScreenCategory.TRANSFER.value, checkoutStep = "1_choosePickupLocation")
            PayoutLocationSelectorActivity.BANK_DEPOSIT_SCREEN_TAG -> ScreenEvent(screenName = ScreenName.BANK_DEPOSIT_PAYERS_SCREEN.value, screenCategory = ScreenCategory.TRANSFER.value, checkoutStep = "1_checkoutPayer")
            PayoutLocationSelectorActivity.MOBILE_WALLET_SCREEN_TAG -> ScreenEvent(screenName = ScreenName.MOBILE_WALLET_SCREEN.value, screenCategory = ScreenCategory.TRANSFER.value)
            CheckoutActivity::class.java.simpleName -> ScreenEvent(screenName = ScreenName.CHECKOUT_SUMMARY_SCREEN.value, screenCategory = ScreenCategory.TRANSFER.value, checkoutStep = "4_checkoutSummary")
            ScreenName.PAYMENT_METHOD_SCREEN.value -> ScreenEvent(screenName = ScreenName.PAYMENT_METHOD_SCREEN.value, screenCategory = ScreenCategory.TRANSFER.value, checkoutStep = "3_checkoutPayment")
            ScreenName.REGION_SCREEN.value -> ScreenEvent(screenName = ScreenName.REGION_SCREEN.value, screenCategory = ScreenCategory.TRANSFER.value)
            ScreenName.SOURCE_FUNDS_SCREEN.value -> ScreenEvent(screenName = ScreenName.SOURCE_FUNDS_SCREEN.value, screenCategory = ScreenCategory.TRANSFER.value, checkoutStep = "2_checkoutExtraInfo")
            ScreenName.OCCUPATION_SCREEN.value -> ScreenEvent(screenName = ScreenName.OCCUPATION_SCREEN.value, screenCategory = ScreenCategory.TRANSFER.value)
            TransactionalCalculatorFragment::class.java.simpleName -> ScreenEvent(screenName = ScreenName.CALCULATOR_SCREEN.value, screenCategory = ScreenCategory.TRANSFER.value)
            ScreenName.STATE_OR_REGION_SCREEN.value -> ScreenEvent(screenName = ScreenName.STATE_OR_REGION_SCREEN.value, screenCategory = ScreenCategory.TRANSFER.value)
            ScreenName.CITY_SCREEN.value -> ScreenEvent(screenName = ScreenName.CITY_SCREEN.value, screenCategory = ScreenCategory.TRANSFER.value)
            ScreenName.CHOOSE_BENEFICIARY_PICKUP_LOCATION_SCREEN.value -> ScreenEvent(screenName = ScreenName.CHOOSE_BENEFICIARY_PICKUP_LOCATION_SCREEN.value, screenCategory = ScreenCategory.TRANSFER.value, checkoutStep = "1_chooseBeneficiaryPickupLocation")
            ScreenName.CHOOSE_PAYMENT_NETWORK_SCREEN.value -> ScreenEvent(screenName = ScreenName.CHOOSE_PAYMENT_NETWORK_SCREEN.value, screenCategory = ScreenCategory.TRANSFER.value, checkoutStep = "1_choosePaymentNetwork")
            ScreenName.TRANSACTION_ORDER_FAILED.value -> ScreenEvent(screenName = ScreenName.TRANSACTION_ORDER_FAILED.value, screenCategory = ScreenCategory.TRANSFER.value)
            ScreenName.ORDER_PLACED_SUCCESSFULLY.value -> ScreenEvent(screenName = ScreenName.ORDER_PLACED_SUCCESSFULLY.value, screenCategory = ScreenCategory.TRANSFER.value, checkoutStep = "5_checkoutMtn")
            ScreenName.MODAL_DISCARD_TRANSFER.value -> ScreenEvent(screenName = ScreenName.MODAL_DISCARD_TRANSFER.value, screenCategory = ScreenCategory.TRANSFER.value)
            TransactionalActivity::class.java.simpleName -> ScreenEvent(screenName = ScreenName.SEND_MONEY_CONFIGURATION.value, screenCategory = ScreenCategory.TRANSFER.value)
            CashPickUpMapActivity::class.java.simpleName -> ScreenEvent(screenName = ScreenName.CHOOSE_PICK_UP_LOCATION.value, screenCategory = ScreenCategory.TRANSFER.value)
            DocumentsSelectorActivity::class.java.simpleName -> ScreenEvent(screenName = ScreenName.ADD_DOCUMENT.value, screenCategory = ScreenCategory.TRANSFER.value)
            MyDocumentsActivity::class.java.simpleName -> ScreenEvent(screenName = ScreenName.ID_SCAN.value, screenCategory = ScreenCategory.TRANSFER.value)
            ScreenName.DOCUMENT_ID_UPLOAD_AUTO.value -> ScreenEvent(screenName = ScreenName.DOCUMENT_ID_UPLOAD_AUTO.value, screenCategory = ScreenCategory.TRANSFER.value)
            ScreenName.DOCUMENT_ID_UPLOAD_MANUAL.value -> ScreenEvent(screenName = ScreenName.DOCUMENT_ID_UPLOAD_MANUAL.value, screenCategory = ScreenCategory.TRANSFER.value)
            PromotionsActivity::class.java.simpleName -> ScreenEvent(screenName = ScreenName.PROMOTIONAL_CODES.value, screenCategory = ScreenCategory.TRANSFER.value)
            DocumentValidationActivity::class.java.simpleName -> ScreenEvent(screenName = ScreenName.ID_SCAN.value, screenCategory = ScreenCategory.TRANSFER.value)
            ScreenName.PURPOSE_OF_TRANSACTION.value -> ScreenEvent(screenName = ScreenName.TRANSACTION_PURPOSE_SCREEN.value, screenCategory = ScreenCategory.TRANSFER.value)
            ScreenName.SESSION_EXPIRED.value -> ScreenEvent(screenName = ScreenName.SESSION_EXPIRED.value, screenCategory = ScreenCategory.WELCOME.value)
            ScreenName.CALCULATOR_SCREEN.value -> ScreenEvent(screenName = ScreenName.CALCULATOR_SCREEN.value, screenCategory = ScreenCategory.TRANSFER.value)
            ScreenName.COUNTRY_CALLING_CODE.value -> ScreenEvent(screenName = ScreenName.COUNTRY_CALLING_CODE.value, screenCategory = ScreenCategory.ACCESS.value)
            ScreenName.ENTER_PASSCODE.value -> ScreenEvent(screenName = ScreenName.ENTER_PASSCODE.value, screenCategory = ScreenCategory.ACCESS.value)
            ScreenName.SEND_TO_SCREEN.value -> ScreenEvent(screenName = ScreenName.SEND_TO_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            ScreenName.STATUS_SCREEN.value -> ScreenEvent(screenName = ScreenName.STATUS_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            ScreenName.ACCOUNT_SCREEN.value -> ScreenEvent(screenName = ScreenName.ACCOUNT_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            ScreenName.SEND_MONEY_TO_SCREEN.value -> ScreenEvent(screenName = ScreenName.SEND_MONEY_TO_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            ScreenName.EDIT_PROFILE_SCREEN.value -> ScreenEvent(screenName = ScreenName.EDIT_PROFILE_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            BeneficiaryListAllFragment::class.java.simpleName -> ScreenEvent(screenName = ScreenName.MY_BENEFICIARIES_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            SearchCountryActivity::class.java.simpleName -> ScreenEvent(screenName = ScreenName.SEND_MONEY_TO_SCREEN.value, screenCategory = ScreenCategory.DASHBOARD.value)
            ScreenName.SENDING_FROM_SCREEN.value -> ScreenEvent(screenName = ScreenName.SENDING_FROM_SCREEN.value, screenCategory = ScreenCategory.WELCOME.value)
            ScreenName.SENDING_TO_SCREEN.value -> ScreenEvent(screenName = ScreenName.SENDING_TO_SCREEN.value, screenCategory = ScreenCategory.WELCOME.value)
            else -> ScreenEvent()
        }
    }

    fun trackEvent(event: AnalyticsEvent) {
        fireBaseAnalytics.logEvent(event.getEventType(), event.toBundle())
    }

    fun updateUserProperty(property: UserProperty) {
        fireBaseAnalytics.setUserProperty(property.name.value, property.value)
    }

    private fun getBrazeEventProperties(screenName: String, properties: Map<String, String>): BrazeEvent? {
        return when (screenName) {
            ScreenName.FORM_SOFT_REGISTER_SCREEN.value -> BrazeEvent(BrazeEventName.REGISTER_FIRST_STEP.value, properties)
            ScreenName.PHONE_VALIDATION_SCREEN.value -> BrazeEvent(BrazeEventName.REGISTER_SECOND_STEP.value, properties)
            ScreenName.PROFILE_SCREEN.value -> BrazeEvent(BrazeEventName.REGISTER_THIRD_STEP.value, properties)
            else -> null
        }
    }
}

enum class ScreenCategory(val value: String) {
    ONBOARDING("onboarding"),
    WELCOME("welcome"),
    ACCESS("access"),
    DASHBOARD("dashboard"),
    CONTACT("contact"),
    SEND_MONEY_TO("sendMoneyTo"),
    TRANSFER("transfer")
}

enum class ScreenName(val value: String) {
    SPLASH_SCREEN("splashscreen"),
    WELCOME_SCREEN("access"),
    INFO_STRESS_FREE_SCREEN("infoStressFree"),
    INFO_SECURE_SCREEN("infoIsSecure"),
    INFO_REAL_PEOPLE_SCREEN("infoRealPeople"),
    SENDING_FROM_SCREEN("sendingFrom"),
    SENDING_TO_SCREEN("sendingTo"),
    ENTER_PASSCODE_SCREEN("enterPasscode"),
    LOGIN_SCREEN("login"),
    HELP_PASSWORD_SCREEN("helpPassword"),
    SEND_MONEY_FROM_SCREEN("sendMoneyFrom"),
    CREATE_PASSCODE_SCREEN("createPasscode"),
    RECOVER_PASSCODE_EMAIL_DIALOG("recoverPassEmailNotification"),
    FORM_SOFT_REGISTER_SCREEN("formSoftRegister"),
    PHONE_VALIDATION_SCREEN("phoneValidation"),
    CONFIRM_SMS_CODE_SCREEN("confirmSmsCode"),
    PROFILE_SCREEN("profile"),
    SEND_TO_SCREEN("sendTo"),
    SEND_MONEY_TO_SCREEN("sendMoneyTo"),
    STATUS_SCREEN("status"),
    MANAGE_TRANSFER_SCREEN("manageTransfer"),
    ACCOUNT_SCREEN("account"),
    EDIT_PROFILE_SCREEN("editProfile"),
    MY_DOCUMENTS_SCREEN("myDocuments"),
    MY_ACTIVITY_SCREEN("myActivity"),
    MY_BENEFICIARIES_SCREEN("myBeneficiaries"),
    NEW_BENEFICIARY_SCREEN("newBeneficiary"),
    OFFICES_SCREEN("offices"),
    STORE_MAP_SCREEN("storeMap"),
    SEARCH_OFFICE_LOCATION_SCREEN("searchOfficeLocation"),
    SETTINGS_SCREEN("settings"),
    END_SESSION_DIALOG("endSession"),
    CONTACT_SMALLWORLD_SCREEN("contactSmallWorld"),
    CUSTOMER_SERVICE_CHAT_SCREEN("customerServiceChat"),
    CUSTOMER_SERVICE_EMAIL_SCREEN("customerSupportEmailMessage"),
    BENEFICIARY_TYPE_SCREEN("beneficiaryType"),
    SEND_MONEY_CONFIGURATION_SCREEN("sendMoneyConfiguration"),
    TRANSACTION_PURPOSE_SCREEN("transactionPurpose"),
    RELATIONSHIP_WITH_BENEFICIARY_SCREEN("relationshipWithBeneficiary"),
    BANK_DEPOSIT_PAYERS_SCREEN("bankDepositPayers"),
    AVAILABLE_PAYERS_SCREEN("availablePayers"),
    PICKUP_LOCATIONS_SCREEN("pickupLocations"),
    MOBILE_WALLET_SCREEN("availablePayers"),
    CHECKOUT_SUMMARY_SCREEN("checkoutSummary"),
    PAYMENT_METHOD_SCREEN("paymentMethod"),
    REGION_SCREEN("region"),
    SOURCE_FUNDS_SCREEN("sourceFunds"),
    OCCUPATION_SCREEN("occupation"),
    CALCULATOR_SCREEN("calculator"),
    STATE_OR_REGION_SCREEN("stateOrRegion"),
    CITY_SCREEN("city"),
    CHOOSE_BENEFICIARY_PICKUP_LOCATION_SCREEN("chooseBeneficiaryPickupLocation"),
    CHOOSE_PAYMENT_NETWORK_SCREEN("choosePaymentNetwork"),
    TRANSACTION_ORDER_FAILED("transactionOrderFailed"),
    ORDER_PLACED_SUCCESSFULLY("orderPlacedSuccessfully"),
    SEND_MONEY_CONFIGURATION("sendMoneyConfiguration"),
    CHOOSE_PICK_UP_LOCATION("choosePickupLocation"),
    ADD_DOCUMENT("addDocument"),
    ID_SCAN("idScan"),
    DOCUMENT_ID_UPLOAD_AUTO("documentIdUploadAuto"),
    DOCUMENT_ID_UPLOAD_MANUAL("documentIdUploadManual"),
    PROMOTIONAL_CODES("promotionalCodes"),
    PURPOSE_OF_TRANSACTION("Purpose of transaction"),
    SESSION_EXPIRED("modalSessionExpired"),
    COUNTRY_CALLING_CODE("countryCallingCode"),
    ENTER_PASSCODE("enterPasscode"),
    MODAL_DISCARD_TRANSFER("modalDiscardTransfer"),
    MODAL_MAX_AMOUNT("modalMaximumAmount"),
    MODAL_SESSION_EXPIRED("modalSessionExpired")
}
