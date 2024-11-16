package com.smallworldfs.moneytransferapp.utils

import android.content.Context
import com.smallworldfs.moneytransferapp.R
import java.util.Calendar
import javax.inject.Inject

class NewUtils @Inject constructor() {

    @Inject
    lateinit var context: Context

    fun getGreetingTextDay(): String {
        val calendar = Calendar.getInstance()
        return when (calendar.get(Calendar.HOUR_OF_DAY)) {
            in 7..11 -> context.getString(R.string.good_morning)
            in 12..20 -> context.getString(R.string.good_evening)
            else -> context.getString(R.string.good_night)
        }
    }
}
