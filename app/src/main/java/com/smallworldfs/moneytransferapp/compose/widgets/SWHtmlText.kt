package com.smallworldfs.moneytransferapp.compose.widgets

import android.os.Build.VERSION.SDK_INT
import android.text.Html
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.semantics.CustomAccessibilityAction
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.customActions
import androidx.compose.ui.semantics.onClick
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.smallworldfs.moneytransferapp.compose.colors.darkGrey
import com.smallworldfs.moneytransferapp.utils.toAnnotatedString

@Composable
fun SWHtmlText(
    modifier: Modifier = Modifier,
    text: String,
    onTextLayout: (TextLayoutResult) -> Unit = {},
    style: TextStyle = LocalTextStyle.current,
    onUriClick: ((String) -> Unit)? = null,
) {
    val annotatedString = if (SDK_INT < 24) {
        Html.fromHtml(text)
    } else {
        Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY)
    }.toAnnotatedString()

    val uriHandler = LocalUriHandler.current
    val layoutResult = remember { mutableStateOf<TextLayoutResult?>(null) }

    val urls = remember(layoutResult, annotatedString) {
        annotatedString.getStringAnnotations("url", 0, annotatedString.lastIndex)
    }

    Text(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { pos ->
                        layoutResult.value?.let { layoutResult ->
                            val position = layoutResult.getOffsetForPosition(pos)
                            annotatedString
                                .getStringAnnotations(position, position)
                                .firstOrNull()
                                ?.let { sa ->
                                    if (sa.tag == "url") {
                                        val url = sa.item
                                        onUriClick?.let { it(url) } ?: uriHandler.openUri(url)
                                    }
                                }
                        }
                    },
                )
            }
            .semantics {
                if (urls.size == 1) {
                    role = Role.Button
                    onClick("Link (${annotatedString.substring(urls[0].start, urls[0].end)}") {
                        val url = urls[0].item
                        onUriClick?.let { it(url) } ?: uriHandler.openUri(url)
                        true
                    }
                } else {
                    customActions = urls.map {
                        CustomAccessibilityAction("Link (${annotatedString.substring(it.start, it.end)})") {
                            val url = it.item
                            onUriClick?.let { it(url) } ?: uriHandler.openUri(url)
                            true
                        }
                    }
                }
            },
        text = annotatedString,
        onTextLayout = {
            layoutResult.value = it
            onTextLayout(it)
        },
        style = style,
        color = darkGrey,
        fontSize = 16.sp,
    )
}
