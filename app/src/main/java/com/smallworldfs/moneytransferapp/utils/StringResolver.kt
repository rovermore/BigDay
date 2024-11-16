package com.smallworldfs.moneytransferapp.utils

import android.content.Context
import androidx.annotation.StringRes
import javax.inject.Inject

class StringResolver @Inject constructor(
    private val context: Context
) {

    fun getStringFromId(@StringRes resId: Int) = try { context.getString(resId) } catch (ignored: Exception) { STRING_EMPTY }
}
