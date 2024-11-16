package com.smallworldfs.moneytransferapp.base.domain.utils

import android.util.Base64
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.STRING_NEW_LINE
import javax.inject.Inject

class Base64Tool @Inject constructor() {

    fun encode(data: String): String = Base64.encodeToString(data.toByteArray(), Base64.DEFAULT).replace(STRING_NEW_LINE, STRING_EMPTY)
}
