package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.smallworldfs.moneytransferapp.compose.colors.defaultTextColor
import com.smallworldfs.moneytransferapp.compose.colors.onSurfaceHighEmphasis
import com.smallworldfs.moneytransferapp.compose.fonts.fonts
import com.smallworldfs.moneytransferapp.compose.style.SWTextStyle
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

@Composable
fun SWText(
    modifier: Modifier = Modifier,
    text: String = STRING_EMPTY,
    textAlign: TextAlign = TextAlign.Center,
    fontSize: TextUnit = 12.sp,
    fontWeight: FontWeight = FontWeight.Light,
    fontStyle: FontStyle = FontStyle.Normal,
    color: Color = defaultTextColor,
    maxLines: Int = Int.MAX_VALUE,
) {

    Text(
        text = text,
        modifier = modifier,
        textAlign = textAlign,
        fontSize = fontSize,
        fontFamily = fonts,
        fontWeight = fontWeight,
        fontStyle = fontStyle,
        color = color,
        maxLines = maxLines,
    )
}

@Composable
fun SWText(
    modifier: Modifier = Modifier,
    text: String = STRING_EMPTY,
    maxLines: Int = Int.MAX_VALUE,
    textAlign: TextAlign = TextAlign.Start,
    style: SWTextStyle,
    color: Color = onSurfaceHighEmphasis,
) {

    Text(
        text = text,
        modifier = modifier,
        fontSize = style.fontSize,
        fontFamily = fonts,
        textAlign = textAlign,
        fontWeight = style.fontWeight,
        color = color,
        maxLines = maxLines,
    )
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWTextPreview() {
    SWText(
        text = "Text",
    )
}
