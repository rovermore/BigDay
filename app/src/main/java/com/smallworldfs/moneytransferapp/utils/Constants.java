package com.smallworldfs.moneytransferapp.utils;

public class Constants {

    public interface TESTING {
        boolean RETROFIT_LOG = true;
    }

    public interface CONFIGURATION {
        int CIRCLE_REVEAL_ANIMATION = 300;
        int ACTIVITY_START_DELAY = 0;
        int MAX_RETRY_API_CALLS = 5;
        int MIN_PASSWORD_LENGTH = 6;
        int MAX_PASSWORD_LENGTH = 15;
        int PIN_DEFAULT_SIZE = 4;
        char[] CHARSET_az_09 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789".toCharArray();
        String AUTHORIZATION_KEY = "Authorization";
        String PARAM_PASSWORD = "password";
        String PARAM_LANGUAGE = "lang";
        String UDID = "X-Device-Id";
        String DEVICE = "X-Device";
        String DEVICE_ID = "X-Device-Id";
        String VERSION = "X-App-Version";
        String USER_AGENT = "User-Agent";
        String DEVICE_ANDROID = "android";
        String REPRESENTATIVE_CODE_LOCATION_SEPARATOR = "###";
        String RECEIPTS_NAME = "SmallWorldReceipt.pdf";
        String SUCCESS_RESPONSE = "success";
        String REDIRECT_WEBVIEW = "Redirect";
        String REDIRECT_ERROR_WEBVIEW = "smallworldfs://?action=error";
        String USER_ID = "userId";
    }

    public interface PASSCODE {
        int MAX_ATTEMPTS_PASSCODE = 3;
    }

    public interface COUNTRY {
        String ORIGIN_COUNTRIES_TYPE = "sending";
        String PAYOUT_COUNTRIES_TYPE = "payout";
        String ALPHABETICAL_COUNTRIES_TYPE = "alph_rest_payout";
        String POPULAR_COUNTRIES_TYPE = "popular";
        String CONTRY_PREFIX_TYPE = "country_prefix";
        String US_COUNTRY_VALUE = "USA";
        String ESP_COUNTRY_VALUE = "ESP";
        String NDL_COUNTRY_VALUE = "NLD";
        String CONTRY_MAPCOUNTRIES_TYPE = "mapcountries";

        String FLAG_IMAGE_ASSETS = "https://www.smallworldfs.com/assets/images/flagsv3/";
        String FLAG_IMAGE_EXTENSION = ".png";
    }

    public interface UserType {
        String LIMITED = "LIMITED";
        String APPROVED = "APPROVED";
        String PDT_SMS = "PDT_SMS";
        String UNDER_REVIEW = "UNDER_REVIEW";
        String PDT_MAIL = "PDT_MAIL";
    }

    public interface DEEP_LINK {
        String APP_TOKEN_EXTRA = "APP_TOKEN_EXTRA";
        String VERIFIED_USER_OK_EXTRA = "VERIFIED_USER_OK_EXTRA";
        String VERIFIED_KO_USER_EXTRA = "VERIFIED_KO_USER_EXTRA";
        String RESET_PASSWORD_SCHEME = "password/reset";
        String REGISTER_APP_SCHEME = "registerapp";
    }

    public interface SCREENS_IDENTIFIFERS {
        String SPLASH = "SPLASH";
        String APP_CUSTOMIZATION = "APP_CUSTOMIZATION";
        String SCREEN = "screen";
    }

    public interface SMS {
        String SMS_TYPE = "adm";
        String SMS_KEY = "5iyMsd977W1p0LZHSSCw37vaT1181ku5";
    }

    public interface EMAIL {
        String EMAIL_KEY = SMS.SMS_KEY;
        String TYPE = "web";
        String TYPE_VINCULATION = "adm";
        String SUBTYPE = "ACTIVACION";
        String USER_EMAIL = "email";

    }

    public interface NOTIFICATION_TOPICS {
        String SENDING_COUNTRY = "_sending";
        String DEST_COUNTRY = "_destination";
    }

    public interface NOTIFICATION_BLOCKS {
        String BENEFICIARIES = "BENEFICIARIES";
        String TRANSACTION = "TRANSACTION";
        String DOCUMENTS = "DOCUMENTS";
        String STATUS = "STATUS";
        String SENDTO = "SENDTO";
    }

    public interface CALCULATOR {
        String CURRENCY_TYPE_PAYOUT_PRINCIPAL = "PAYOUTPRINCIPAL";
        String TOTALSALE = "TOTALSALE";
        String DEFAULT_CURRENCY_TYPE = TOTALSALE;
        String DEFAULT_AMOUNT = "100";
        String DEFAULT_ZERO_VALUE = "0";
        String DEFAULT_ZERO_VALUE_AND_COLLON = "0,";
        String DEFAULT_OPERATION = "SEND_TO";
        String EUR_CURRENCY = "EUR";

        int CALCULATOR_SOFT_KEYBOARD_TRANSLATE_XXXHDPI = 400;
        int CALCULATOR_SOFT_KEYBOARD_TRANSLATE_XXHDPI = CALCULATOR_SOFT_KEYBOARD_TRANSLATE_XXXHDPI;
        int CALCULATOR_SOFT_KEYBOARD_TRANSLATE_XHDPI = 230;
    }

    public interface PROMOTIONS {
        String FEE_PERCENT = "fee_percent";
        String TOTAL_PERCENT = "total_percent";
        String FEE_AMOUNT = "fee_amount";
        String TOTAL_AMOUNT = "total_amount";
    }

    public interface DELIVERY_METHODS {
        String CASH_PICKUP = "PICK_UP";
        String BANK_DEPOSIT = "BANK_DEPOSIT";
        String TOP_UP = "MOBILE_TOP_UP";
        String MOBILE_WALLET = "MOBILE_WALLET";
        String CASH_CARD_RELOAD = "CASH_CARD_RELOAD";
        String PHYSICAL_DELIVERY = "PHYSICAL_DELIVERY";
        String ON_CALL = "ON_CALL";
        String CASH_CARD_APP_PHYSICAL = "CASH_CARD_APP_PHYSICAL_DELIVERY";
    }

    public interface STEP_TYPE {
        String DELIVERY_METHOD = "DELIVERY_METHOD";
        String FORM = "FORM";
        String BENEFICIARY_FORM = "BENEFICIARY_FORM";
        String LOCATION_LIST = "LOCATION_LIST";
        String REQUIRED_REGISTER = "REQUIRE_REGISTER";
    }

    public interface FIELD_TYPE {
        String TEXT = "text";
        String TEXT_AREA = "textarea";
        String PASSWORD = "password";
        String EMAIL = "email";
        String COMBO = "combo";
        String GROUP = "group";
        String FILE = "file";
        String RADIO_BUTTON = "radiobutton";
        String DELETE = "delete";
        String REGISTER_INTERN = "register";
        String WHITE_BOX = "whitebox";
        String CHECK_BOX = "checkbox";
        String SWITCH = "switch";
        String HIDDEN = "hidden";
    }

    public interface FIELD_SUBTYPES {
        String TEXT_NUMERIC = "numeric";
        String TEXT_ALPHANUM = "alphanum";
        String TEXT_FILTER_ALPHANUM = "filteralphanum";
        String PHONE = "phone";
        String COMBO_OWN = "own";
        String COMBO_REQUEST = "request";
        String COMBO_API = "api";
        String GROUP_DATE = "date";
        String LOCATION_PICK_UP = "pick_up";
        String LOCATION_BANK_DEPOSIT = "bank_deposit";
        String LOCATION_MOBILE_TOP_UP = "mobile_top_up";
        String LOCATION_HOME_DELIVERY = "physical_delivery";
        String LOCATION_CASH_CARD_RELOAD = "cash_card_reload";
        String LOCATION_ON_CALL = "on_call";
        String LOCATION_MOBILE_WALLET = "mobile_wallet";
        String PROFILE_SECTION = "profile_section";
        String ADDRESS_SECTION = "address_section";
        String ACCOUNT_SECTION = "account_section";
        String DOCUMENT = "document";
        String DOCUMENT_FIELDS_FROM_IMAGE = "documentFieldsFromImage";
        String TEXT_GROUP = "text_group";
        String RICH_TEXT = "richtext";
    }

    public interface FIELD_CONSTANS_KEYS {
        String NAME = "name";
        String LAST_NAME = "firstLastName";
        String EMAIL = "email";
        String PHONE_NUMBER = "mobilePhoneNumber";
        String CITY = "city";
        String ADDRESS = "address";
        String POSTAL_CODE = "zip";
        String BANK_ID = "bankId";
        String LOCATION_CODE = "locationCode";
        String REPRESENTATIVE_CODE = "representativeCode";
        String FINAL_STEP = "final";
        String DOCUMENT_TYPE_ATTACH = "sender";
        String BENEFICIARY_ID = "beneficiaryId";
        String BENEFICIARY_TYPE = "beneficiaryType";
        String OPERATION = "operation";
        String CURRENCY = "currency";
        String COUNTRY_ORIGIN = "countryOrigin";
        String AMOUNT = "amount";
        String CURRENCY_TYPE = "currencyType";
        String CURRENCY_ORIGIN = "currencyOrigin";
        String STATE = "state";
        String MTN_PURPOSE = "mtn_purpose";
        String CLIENT_RELATION = "clientRelation";
        String ONE_SWIPE_SEND = "oneswipesend";
        String PROMOTION_CODE = "promotionCode";
    }

    public interface ACCOUNT_SECTIONS_KEYS {
        String MY_BENEFICIARIES = "MY_BENEFICIARIES";
        String MY_ACTIVITY = "MY_ACTIVITY";
        String REQUESTS = "REQUESTS";
        String MY_DOCUMENTS = "MY_DOCUMENTS";
        String CUSTOMER_SUPPORT = "CUSTOMER_SUPPORT";
        String OFFICES = "OFFICES";
        String TRACKING_NUMBER = "TRACKING_NUMBER";
        String SETTINGS = "SETTINGS";
        String MARKETING_PREF = "MARKETING_PREF";
    }

    public interface STEP_TAGS {
        String STEP_1 = "step1";
        String STEP_2 = "step2";
    }

    public interface BANK_DEPOSIT_TYPOLOGY {
        String BEST_RATE = "bestRate";
        String INSTANT_DELIVERY = "instant";
        String SAMEDAY_DELIVERY = "sameDay";
    }

    public interface MY_BENEFICIARIES_PAGE_ITEMS {
        int PAGE_ITEMS = 10;
    }

    public interface DATA_CONTAINER_KEYS {
        String LOCATIONS_CONTAINER_KEY = "location_key";
    }

    public interface REQUEST_CODES {
        String REQUEST_CODE = "requestCode";
        int GENERIC_DROP_SELECTOR_REQUEST_CODE = 101;
        int INFO_CONTACTS_REQUEST_CODE = 102;
        int SELECT_LOCATION_REQUEST_CODE = 103;
        int NEW_BENEFICIARY = 106;
        int BENEFICIARY_DETAIL = 107;
        int EDIT_BENEFICIARY = 108;
        int EDIT_USER = 109;
        int CHANGE_PASSWORD = 110;
        int SEND_EMAIL_REQUEST_CODE = 111;
        int SELECT_BANK_REQUEST_CODE = 112;
        int SELECT_COUNTRY = 113;
        int CHECKOUT_REQUEST_CODE = 119;
        int TRANSACTIONAL_REQUEST_CODE = 120;
        int ACCOUNT_DIALOG_PICKER_REQUEST_CODE = 121;
        int TRANSACTION_DETAIL_REQUEST_CODE = 122;
        int MY_ACTIVITY_REQUEST_CODE = 123;
        int PAY_NOW_ACTIVITY_REQUEST_CODE = 126;
        int TRANSFER_DETAILS_REQUEST_CODE = 127;
        int BENEFICIARY_LIST = 128;
        int HARD_REGISTER_CODE = 129;
        int CASH_PICK_UP = 130;
        int CREATE_PASSCODE = 131;
        int WALKTHROUGH_REQUEST_CODE = 132;
        int TRANSACTION_UPLOAD_DOCUMENTS_REQUEST_CODE = 133;
        int REQUEST_CODE_COUNTRY = 134;
        int REQUEST_CODE_CITY = 135;
        int REQUEST_DOCUMENT_TYPE = 136;
        int REQUEST_VALIDATE_ID = 137;
    }

    public interface RESULT_CODES {
        int BENEFICIARY_DELETED = 1001;
        int BENEFICIARY_UPDATED = 1002;
        int BENEFICIARY_CREATED = 1003;
    }


    public interface USER_PARAMS {
        String COUNTRY = "country";
        String USER_TOKEN = "userToken";
        String USER_ID = "userId";
        String TEST_VAR = "test";
        String KOUNT_SESS_ID = "kountsessid";
        String COUNTRY_BIRTH = "countryBirth";
    }

    public interface DOCUMENT_PARAMS {
        String DOCUMENT_TYPE = "document";
        String TYPE = "type";
        String NUMBER_DOCUMENT = "numberDocument";
        String DOCUMENT_TYPE_REGISTER_KEY = "user";
        String FRONT_NAME = "FRONT";
        String BACK_NAME = "BACK";
        String SELFIE_NAME = "DOCUMENT_TYPE_SPECIAL_ID_FILE";
        String SELFIE_ATTACHMENT_TYPE = "selfieSenderRegister";
        String SELFIE_DEFAULT_DOCUMENT_ID = "DEFAULT_DOCUMENT_ID_FOR_SELFIE";
        String FIELD = "field";
        String SELFIE = "selfie";
    }

    public interface DIALOG_CHECKOUT_STYLE {
        String SUCCESS_STYLE = "SUCCESS_STYLE";
        String ERROR_STYLE = "ERROR_STYLE";
    }

    public interface PAYNMENT_METHODS {
        String PAYNMENT_METHOD_KEY = "paymentMethod";
        String BANKWIRE = "BANKWIRE";
        String INGENICO = "INGENICO";
        String SOFORT = "SOFORT";
        String WORLDPAY = "WORLDPAY";
        String ONLINEPAYMENT = "ONLINEPAYMENT";

        interface BANKWIRE_KEY_FIELDS {
            String DEPOSIT_BANK_ID = "depositBankId";
            String DEPOSIT_BANK_BRANCH_ID = "depositBankBranchId";
        }
    }

    public interface TRANSACTION_STATUS {
        String ACKNOWLEDGE_PENDING = "ACKNOWLEDGE_PENDING";
        String NEW = "NEW";
        String UNDER_REVIEW = "UNDER_REVIEW";
        String IN_PROGRESS = "IN_PROGRESS";
        String TO_SUBMIT = "TO_SUBMIT";
        String VOID_PENDING = "VOID_PENDING";
        String CLOSED_CANCELLED = "CLOSED_CANCELLED";
        String CLOSED_COMPLIANCE = "CLOSED_COMPLIANCE";
        String CLOSED_REFUSED = "CLOSED_REFUSED";
        String CLOSED_DECLINED = "CLOSED_DECLINED";
        String CLOSED_PAID_OUT = "CLOSED_PAID_OUT";
        String USER_CANCEL = "USER_CANCEL";
    }

    public interface COMPLIANCE_RULES {
        String TAX_CODE_DOCUMENT = "TAX_CODE_DOCUMENT";
        String ID_MISSING_OR_EXPIRED = "ID_MISSING_OR_EXPIRED";
    }

    public interface PAY_CONSTANTS {
        String MTN = "mtn=";
        String SUCCESS = "success=ok";
        String REFUSED = "success=error";
        String CVV_WARNING = "success=cvv_warning";
        String BLOCKED_WARNING = "success=blocked_warning";
        String BLOCKED = "success=blocked";
    }

    public interface WALKTHROUGH_TYPE {
        String FIRST_OPEN_APP = "FIRST_OPEN_APP";
        String FIRST_TRANSACTION = "FIRST_TRANSACTION";
    }

    public interface NUMBER_TRANSACTIONS {
        int TRANSACTIONS_NEEDED_TO_SHOW_DIALOG = 2;
    }

    public interface QUICK_REMINDER_STYLES {
        int DEFAULT = 0;
        int GDPR = 1;
    }

    public interface GDPR_VALUES {
        String FROM_VIEW_REGISTER = "soft";
        String FROM_VIEW_ACCOUNT = "account";
        String FROM_VIEW_POPUP = "popup";
    }

    public interface FLINKS_STATE {
        String FLINKS_CONNECT = "1";
    }

    public interface C2B_CONSTANTS {
        String MYSELF_TAG = "myself";
        String C2C_TAG = "beneficiary";
        String C2B_TAG = "c2b";
        String B2C_TAG = "b2c";
        String B2B_TAG = "b2b";
    }

    public interface TRACKER_STATUS {
        String DONE = "done";
        String PENDING = "pending";
        String IN_PROGRESS = "in progress";
        String CANCELLED = "cancelled";
    }

    public interface FACEBOOK_ANALYTICS_KEYS {
        String REGISTRATION_METHOD = "RegistrationMethod";
        String COMPLETE_REGISTRATION = "Complete registration";
        String CONTENT_ID = "ContentID";
        String CONTENT_TYPE = "ContentType";
        String CURRENCY = "Currency";
        String VALUE_TO_SUM = "ValueToSum";
    }

    public interface FACEBOOK_ANALYTICS_VALUES {
        String SOFT_REGISTER = "softregister";
        String SUBMIT_APPLICATION = "Submit Application";
        String PURCHASE = "Purchase";
    }
}
