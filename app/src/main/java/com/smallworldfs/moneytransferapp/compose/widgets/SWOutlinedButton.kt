package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.OutlinedButton
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
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.compose.colors.colorGreenMain
import com.smallworldfs.moneytransferapp.compose.colors.neutral0

@Composable
fun SWOutlinedButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    text: String = STRING_EMPTY,
    contentColor: Color = neutral0,
    backgroundColor: Color = colorGreenMain,
    border: BorderStroke = BorderStroke(0.dp, Color.Black),
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
    OutlinedButton(
        modifier = modifier,
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = contentColor,
            backgroundColor = backgroundColor
        ),
        border = border,
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

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWOutlinedButtonPreview() {
    SWOutlinedButton(
        text = "Text"
    )
}
