package com.smallworldfs.moneytransferapp.utils

import android.text.Spanned
import android.text.style.URLSpan
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.core.text.getSpans
import com.smallworldfs.moneytransferapp.compose.colors.blueAccentColor

fun Spanned.toAnnotatedString(): AnnotatedString {
    return buildAnnotatedString {
        append(this@toAnnotatedString.toString())
        val urlSpans = getSpans<URLSpan>()
        urlSpans.forEach { urlSpan ->
            val start = getSpanStart(urlSpan)
            val end = getSpanEnd(urlSpan)
            addStyle(
                SpanStyle(
                    textDecoration = TextDecoration.Underline,
                    color = blueAccentColor,
                ),
                start, end,
            )
            addStringAnnotation("url", urlSpan.url, start, end)
        }
    }
}
