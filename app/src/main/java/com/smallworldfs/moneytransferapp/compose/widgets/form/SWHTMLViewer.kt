package com.smallworldfs.moneytransferapp.compose.widgets.form

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.text.ClickableText
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.text.HtmlCompat

@Composable
fun HtmlText(htmlContent: String) {
    val annotatedString = buildAnnotatedString {
        append(HtmlCompat.fromHtml(htmlContent, HtmlCompat.FROM_HTML_MODE_COMPACT))
        withStyle(style = SpanStyle(color = Color.Blue, textDecoration = TextDecoration.Underline)) {}
    }
    val context = LocalContext.current
    ClickableText(
        text = annotatedString,
        onClick = { offset ->
            annotatedString.getStringAnnotations(
                tag = "URL",
                start = offset,
                end = offset,
            ).firstOrNull()?.let { annotation ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(annotation.item))
                context.startActivity(intent)
            }
        },
        style = TextStyle.Default,
    )
}

@Preview
@Composable
fun PreviewHtmlText() {
    HtmlText("<u>Visit our website:</u> <a href='https://www.example.com'>Example Website</a>")
}
