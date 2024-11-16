package com.smallworldfs.moneytransferapp.utils

import android.content.Context
import android.text.format.DateFormat
import java.util.Date
import javax.inject.Inject

class DateFormatter @Inject constructor(
    private val context: Context
) {

    fun getSimpleDateString(timestamp: Long): String {
        val expiration = Date().apply { time = timestamp }
        return DateFormat.getMediumDateFormat(context).format(expiration)
    }
}
