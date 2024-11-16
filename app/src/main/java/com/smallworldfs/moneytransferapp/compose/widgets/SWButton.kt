package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallworldfs.moneytransferapp.compose.colors.colorGreenMain
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.compose.state.SWButtonState
import com.smallworldfs.moneytransferapp.compose.style.SWButtonStyle
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

@Composable
fun SWButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    text: String = STRING_EMPTY,
    contentColor: Color = neutral0,
    backgroundColor: Color = colorGreenMain,
    shape: Shape = RoundedCornerShape(4.dp),
    textModifier: Modifier = Modifier.padding(
        start = 20.dp,
        end = 20.dp,
        top = 8.dp,
        bottom = 8.dp
    ),
    textAlign: TextAlign = TextAlign.Center,
    textColor: Color = neutral0,
    fontSize: TextUnit = 12.sp,
    fontWeight: FontWeight = FontWeight.ExtraBold,
    content: @Composable RowScope.() -> Unit = {}
) {
    Button(
        modifier = modifier,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = contentColor,
            backgroundColor = backgroundColor,
        ),
        shape = shape,
        onClick = { onClick() },
    ) {
        SWText(
            modifier = textModifier,
            textAlign = textAlign,
            text = text,
            fontSize = fontSize,
            fontWeight = fontWeight,
            color = textColor
        )
        content()
    }
}

@Composable
fun SWButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    text: String = STRING_EMPTY,
    style: SWButtonStyle,
    state: SWButtonState = SWButtonState.Enabled,
    content: @Composable RowScope.() -> Unit = {}
) {

    Button(
        modifier = modifier,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = style.contentColor,
            backgroundColor = style.backgroundColor,
        ),
        shape = style.shape,
        onClick = {
            if (state is SWButtonState.Enabled) {
                onClick()
            }
        },
    ) {
        SWText(
            modifier = Modifier.padding(
                start = 20.dp,
                end = 20.dp
            ),
            textAlign = style.textAlign,
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = style.textColor
        )
        content()
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWButtonPreview() {
    SWButton(
        text = "Text ",
        onClick = {}
    )
}
