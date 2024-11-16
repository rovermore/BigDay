package com.smallworldfs.moneytransferapp.presentation.form.adapter

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import java.util.TreeMap

fun extractCountryPhonePrefix(data: ArrayList<TreeMap<String, String>>, countryKey: String?): String? {
    for (country in data) {
        for ((key, value) in country) {
            if (key.equals(countryKey, ignoreCase = true)) {
                return extractPrefix(value)
            }
        }
    }
    return STRING_EMPTY
}

fun extractPrefix(country: String): String? {
    return if (country.contains("(") && country.contains(")")) {
        val firstIdx = country.indexOf("(") + 1
        val lastIdx = country.indexOf(")")
        "+" + country.substring(firstIdx, lastIdx)
    } else {
        STRING_EMPTY
    }
}
