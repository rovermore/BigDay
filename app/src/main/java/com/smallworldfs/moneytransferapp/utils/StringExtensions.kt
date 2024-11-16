package com.smallworldfs.moneytransferapp.utils

import android.net.Uri
import android.os.Build
import android.text.Html
import android.text.Spanned
import com.google.gson.GsonBuilder
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success

fun String.toHtml(): Spanned {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        Html.fromHtml(this, Html.FROM_HTML_MODE_COMPACT)
    else
        Html.fromHtml(this)
}

inline fun <reified T> String?.parseJSON() =
    try {
        this ?: Failure(Error.UnsupportedOperation("Data is empty"))
        val result = GsonBuilder().create().fromJson(
            this,
            T::class.java,
        )
        result?.let {
            Success(result)
        } ?: Failure(Error.UncompletedOperation("Data couldn't be read"))
    } catch (e: java.lang.Exception) {
        Failure(Error.UncompletedOperation("Data couldn't be read"))
    }

inline fun String.whenNotBlank(block: Action) {
    if (!this.isNullOrBlank()) block.invoke()
}

fun String.parseQueryParamFromUrl(param: String): OperationResult<String, Error> {
    return if (this.isNotEmpty()) {
        val uri = Uri.parse(this)
        if (!uri.isOpaque && uri.queryParameterNames.isNotEmpty()) {
            uri.getQueryParameter(param)?.let {
                Success(it)
            } ?: Failure(Error.UnsupportedOperation("Could not parse url"))
        } else Failure(Error.UnsupportedOperation("Could not parse url"))
    } else Failure(Error.UnsupportedOperation("Could not parse url"))
}

fun String?.toBool(): Boolean = this == "1"

fun String?.extractUrlFromHtmlString(): String {
    var finalUrl = STRING_EMPTY
    val regex = "<a[^>]+href\\s*=\\s*['\"]([^'\"]+)['\"][^>]*>".toRegex()
    regex.findAll(this ?: STRING_EMPTY).forEach { matchResult ->
        val href = matchResult.groupValues[1]
        finalUrl = href
    }
    return finalUrl
}
