package com.smallworldfs.moneytransferapp.base.presentation.ui.extensions

import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.STRING_SPACE
import java.text.SimpleDateFormat
import java.util.Locale

fun String.normalizeAnalyticsName(): String {
    return if (isNotEmpty() && isNotBlank()) {
        replace(STRING_SPACE, "_").toLowerCase(Locale.getDefault())
    } else {
        STRING_EMPTY
    }
}

/**
 * Parse datetime strings
 * From:
 *      2021-08-07
 * To:
 *      03/10/2019
 */
fun String.parseFromTimestampToDate(): String = try {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    val outputFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    outputFormat.format(inputFormat.parse(this) ?: throw Exception())
} catch (e: Exception) {
    this
}

fun String.whenNotBlank(action: Action) {
    if (this.isNotBlank()) action.invoke()
}
