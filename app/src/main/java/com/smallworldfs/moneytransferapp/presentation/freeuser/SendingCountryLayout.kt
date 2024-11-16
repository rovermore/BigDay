package com.smallworldfs.moneytransferapp.presentation.freeuser

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.colorGreenMain
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.compose.colors.onPrimaryHighEmphasis
import com.smallworldfs.moneytransferapp.compose.colors.onSurfaceDisabled
import com.smallworldfs.moneytransferapp.compose.colors.surfaceOverlay80
import com.smallworldfs.moneytransferapp.compose.state.SWButtonState
import com.smallworldfs.moneytransferapp.compose.state.SWCountryContainerState
import com.smallworldfs.moneytransferapp.compose.style.SWButtonStyle
import com.smallworldfs.moneytransferapp.compose.style.SWCountryContainerStyle
import com.smallworldfs.moneytransferapp.compose.widgets.SWButton
import com.smallworldfs.moneytransferapp.compose.widgets.SWCountryContainer
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action

@Composable
fun SendingCountryLayout(
    modifier: Modifier = Modifier,
    countryName: String,
    urlFlag: String,
    isLoading: Boolean,
    buttonState: SWButtonState,
    onContinueClicked: Action
) {
    Column(
        modifier = modifier
            .background(neutral0)
            .padding(bottom = 32.dp)
            .fillMaxHeight(),
    ) {

        SWCountryContainer(
            title = countryName,
            state = SWCountryContainerState.Primary,
            isLoading = isLoading,
            style = SWCountryContainerStyle.BigFlag(urlFlag),
        )

        Spacer(modifier = Modifier.weight(1f))

        SWButton(
            modifier = Modifier
                .fillMaxWidth(),
            onClick = { onContinueClicked() },
            text = stringResource(id = R.string.continue_text_button),
            state = buttonState,
            style = SWButtonStyle.Flat(
                textColor = if (buttonState is SWButtonState.Enabled) onPrimaryHighEmphasis else onSurfaceDisabled,
                backgroundColor = if (buttonState is SWButtonState.Enabled) colorGreenMain else surfaceOverlay80,
            ),
        )
    }
}

@Preview(showBackground = true, name = "Select Origin Country preview", widthDp = 420)
@Composable
fun SendingCountryLayoutPreview() {
    SendingCountryLayout(
        countryName = "Spain",
        urlFlag = "",
        buttonState = SWButtonState.Enabled,
        isLoading = false,
        onContinueClicked = {},
    )
}
