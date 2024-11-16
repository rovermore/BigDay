package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.compose.colors.defaultGreyLightBackground
import com.smallworldfs.moneytransferapp.compose.state.StepState

@Composable
fun SWStepperHorizontal(
    selectedStep: () -> Int,
    onStepClicked: (Int) -> Unit,
    steps: List<Int>
) {

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        items(steps) { step ->

            if (step < selectedStep())
                StepAndDivider(step = step, steps = steps, state = StepState.Done, onStepClicked = onStepClicked)

            if (step == selectedStep())
                StepAndDivider(step = step, steps = steps, state = StepState.Current, onStepClicked = onStepClicked)

            if (step > selectedStep())
                StepAndDivider(step = step, steps = steps, state = StepState.Disabled, onStepClicked = onStepClicked)
        }
    }
}

@Composable
private fun StepAndDivider(step: Int, steps: List<Int>, state: StepState, onStepClicked: (Int) -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        SWStep(
            modifier = Modifier.clickable { onStepClicked(step) },
            state = state,
            text = (step + 1).toString(),
        )
        if (step < steps.size - 1)
            Divider(
                modifier = Modifier
                    .size(width = 44.dp, height = 1.dp)
                    .padding(start = 8.dp, end = 8.dp),
                color = defaultGreyLightBackground
            )
    }
}

@Preview(showBackground = true, widthDp = 420, backgroundColor = 4294967295)
@Composable
fun SWStepperHorizontalPreview() {
    SWStepperHorizontal(
        selectedStep = { 2 },
        onStepClicked = { },
        steps = listOf(0, 1, 2, 3, 4, 5)
    )
}
