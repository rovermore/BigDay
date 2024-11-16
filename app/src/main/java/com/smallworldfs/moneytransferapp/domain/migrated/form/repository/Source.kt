package com.smallworldfs.moneytransferapp.domain.migrated.form.repository

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import java.io.Serializable

sealed class Source : Serializable {
    class API(val url: String, val queryParams: List<String>, val auxParam: String = STRING_EMPTY) : Source()
    object Local : Source()
}
