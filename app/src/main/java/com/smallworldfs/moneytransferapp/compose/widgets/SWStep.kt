package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.onSurfaceDisabled
import com.smallworldfs.moneytransferapp.compose.colors.success600
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.compose.state.StepState
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

@Composable
fun SWStep(
    modifier: Modifier = Modifier,
    state: StepState = StepState.Current,
    text: String = STRING_EMPTY
) {

    Box(
        modifier = modifier
            .background(
                color = if (state !is StepState.Disabled) success600 else onSurfaceDisabled,
                shape = CircleShape
            )
            .size(24.dp),
        contentAlignment = Alignment.Center
    ) {
        if (state !is StepState.Done)
            SWText(
                text = text,
                fontSize = 14.sp,
                color = neutral0,
                fontWeight = FontWeight.Bold,
            )
        else
            Icon(
                painter = painterResource(id = R.drawable.tick_circle),
                contentDescription = "",
                modifier = Modifier
                    .size(14.dp),
                tint = neutral0
            )
    }
}

@Preview(showBackground = true, widthDp = 420, backgroundColor = 4294967295)
@Composable
fun SWStepPreview() {
    SWStep(
        state = StepState.Done,
        text = "1"
    )
}
