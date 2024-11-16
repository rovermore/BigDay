package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.compose.colors.blueAccentColor

@Composable
fun SWCircularLoader(
    modifier: Modifier = Modifier,
    size: Dp = 64.dp,
    color: Color = blueAccentColor
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(size),
            color = color,
        )
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWCircularLoaderPreview() {
    SWCircularLoader()
}
