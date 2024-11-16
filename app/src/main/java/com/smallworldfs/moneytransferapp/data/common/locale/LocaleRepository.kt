package com.smallworldfs.moneytransferapp.data.common.locale

import java.util.Locale
import javax.inject.Inject

class LocaleRepository @Inject constructor() {

    companion object {
        private const val GERMAN_LOCALE = "de-de"
        private const val SPANISH_LOCALE = "es-es"
        private const val FRANCE_LOCALE = "fr-fr"
        private const val ITALY_LOCALE = "it-it"
        private const val ENGLISH_LOCALE = "en-gb"
        private const val PORTUGUESE_LOCALE = "pt-pt"

        private const val DEVICE_LOCALE_LANGUAGE_DE = "de"
        private const val DEVICE_LOCALE_LANGUAGE_ES = "es"
        private const val DEVICE_LOCALE_LANGUAGE_FR = "fr"
        private const val DEVICE_LOCALE_LANGUAGE_IT = "it"
        private const val DEVICE_LOCALE_LANGUAGE_PT = "pt"
    }

    fun getLocale(): String {
        val injectLocale = Locale.getDefault()
        return when {
            injectLocale.language.toLowerCase(injectLocale).equals(DEVICE_LOCALE_LANGUAGE_DE, ignoreCase = true) -> GERMAN_LOCALE
            injectLocale.language.toLowerCase(injectLocale).equals(DEVICE_LOCALE_LANGUAGE_ES, ignoreCase = true) -> SPANISH_LOCALE
            injectLocale.language.toLowerCase(injectLocale).equals(DEVICE_LOCALE_LANGUAGE_FR, ignoreCase = true) -> FRANCE_LOCALE
            injectLocale.language.toLowerCase(injectLocale).equals(DEVICE_LOCALE_LANGUAGE_IT, ignoreCase = true) -> ITALY_LOCALE
            injectLocale.language.toLowerCase(injectLocale).equals(DEVICE_LOCALE_LANGUAGE_PT, ignoreCase = true) -> PORTUGUESE_LOCALE
            else -> ENGLISH_LOCALE
        }
    }

    fun getLang() = Locale.getDefault().language.toLowerCase(Locale.getDefault())
}
