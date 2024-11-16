package com.smallworldfs.moneytransferapp.utils.forms

class ViewType {
    companion object {
        const val PASSWORD_VIEW = 0
        const val FILE_DOCUMENT = 1
        const val COMBO_COUNTRY_VIEW = 3
        const val EMAIL_VIEW = 4
        const val BUTTON_VIEW = 5
        const val TEXT_BUTTON_VIEW = 6
        const val CHECK_BOX = 7
        const val WHITEBOX = 8
        const val SWITCH_VIEW = 9
        const val UNKNOWN_VIEW = 10
        const val TEXT_VIEW = 11
        const val GROUP_PHONE_VIEW = 12
        const val GROUP_DATE_VIEW = 13
        const val GROUP_TEXT_VIEW = 14
        const val COMBO_OWN_VIEW = 15
        const val SECTION_HEADER_VIEW = 16
    }
}

class Action {
    companion object {
        const val SEND_FORM = "send_form"
        const val FORGOT_PASSWORD = "forgot_password"
    }
}

class Type {
    companion object {
        const val GROUP = "group"
        const val FILE = "file"
        const val PASSWORD = "password"
        const val COMBO = "combo"
        const val COMBO_COUNTRY = "combo_country"
        const val EMAIL = "email"
        const val TEXT = "text"
        const val BUTTON = "button"
        const val CHECK_BOX = "checkbox"
        const val SWITCH = "switch"
        const val WHITEBOX = "whitebox"
        const val SECTION_HEADER = "section_header"
    }
}

class SubType {
    companion object {
        const val STRONG_PASSWORD = "strong_password"
        const val NUMERIC = "numeric"
        const val ALPHA_NUM = "alphanum"
        const val RICH_TEXT = "richtext"
        const val GROUP_DATE = "date"
        const val GROUP_TEXT = "text_group"
        const val GROUP_PHONE = "phone"
        const val OWN = "own"
        const val DOCUMENT = "document"
        const val DOCUMENT_FIELDS_FROM_IMAGE = "documentFieldsFromImage"
        const val USER = "user"
        const val API = "api"
        const val SECTION_HEADER_PROFILE = "section_header_profile"
        const val SECTION_HEADER_ADDRESS = "section_header_address"
    }
}
