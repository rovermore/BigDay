package com.smallworldfs.moneytransferapp.presentation.freeuser

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.compose.colors.onSurfaceMediumEmphasis
import com.smallworldfs.moneytransferapp.compose.state.SWChipState
import com.smallworldfs.moneytransferapp.compose.style.SWChipStyle
import com.smallworldfs.moneytransferapp.compose.style.SWTextStyle
import com.smallworldfs.moneytransferapp.compose.widgets.SWChip
import com.smallworldfs.moneytransferapp.compose.widgets.SWChipLeftBox
import com.smallworldfs.moneytransferapp.compose.widgets.SWProgressSpinner
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel

@Composable
fun ReceivingCountryLayout(
    modifier: Modifier = Modifier,
    isLoading: Boolean,
    topCountries: List<CountryUIModel>,
    onChipClicked: (CountryUIModel) -> Unit,
) {
    Column(
        modifier = modifier
            .background(neutral0)
            .fillMaxHeight(),
    ) {
        if (isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                SWProgressSpinner(modifier = Modifier.size(59.dp))
            }
        } else if (topCountries.size == 5) {
            SWText(
                modifier = Modifier
                    .fillMaxWidth(),
                text = stringResource(id = R.string.our_top_receiving_countries),
                style = SWTextStyle.Title,
                color = onSurfaceMediumEmphasis,
                textAlign = TextAlign.Center,
            )
            FeaturedCountries(
                topCountries = topCountries,
                onChipClicked = onChipClicked,
            )
        }
    }
}

@Composable
private fun FeaturedCountries(
    topCountries: List<CountryUIModel>,
    onChipClicked: (CountryUIModel) -> Unit
) {

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Row {
            SWChip(
                modifier = Modifier
                    .padding(4.dp),
                text = topCountries[0].name,
                state = SWChipState.Enabled,
                leftBox = { SWChipLeftBox(style = SWChipStyle.Flag(topCountries[0].logo)) },
                onClick = {
                    onChipClicked(topCountries[0])
                },
            )

            Spacer(modifier = Modifier)

            SWChip(
                modifier = Modifier
                    .padding(4.dp),
                text = topCountries[1].name,
                state = SWChipState.Enabled,
                leftBox = { SWChipLeftBox(style = SWChipStyle.Flag(topCountries[1].logo)) },
                onClick = {
                    onChipClicked(topCountries[1])
                },
            )
        }

        Row {
            SWChip(
                modifier = Modifier
                    .padding(4.dp),
                text = topCountries[2].name,
                state = SWChipState.Enabled,
                leftBox = { SWChipLeftBox(style = SWChipStyle.Flag(topCountries[2].logo)) },
                onClick = {
                    onChipClicked(topCountries[2])
                },
            )

            Spacer(modifier = Modifier)

            SWChip(
                modifier = Modifier
                    .padding(4.dp),
                text = topCountries[3].name,
                state = SWChipState.Enabled,
                leftBox = { SWChipLeftBox(style = SWChipStyle.Flag(topCountries[3].logo)) },
                onClick = {
                    onChipClicked(topCountries[3])
                },
            )
        }

        SWChip(
            modifier = Modifier.padding(4.dp),
            text = topCountries[4].name,
            state = SWChipState.Enabled,
            leftBox = { SWChipLeftBox(style = SWChipStyle.Flag(topCountries[4].logo)) },
            onClick = {
                onChipClicked(topCountries[4])
            },
        )
    }
}

@Preview(showBackground = true, name = "Select Origin Country preview", widthDp = 420)
@Composable
fun ReceivingCountryLayoutPreview() {
    ReceivingCountryLayout(
        isLoading = false,
        topCountries = listOf(
            CountryUIModel("ESP", "España"),
            CountryUIModel("PT", "Portugal"),
            CountryUIModel("PK", "Pakistan"),
            CountryUIModel("PH", "Filipinas"),
            CountryUIModel("PH", "Filipinas"),
        ),
        onChipClicked = { _ -> },
    )
}

@Preview(showBackground = true, name = "Select Origin Country preview", widthDp = 420)
@Composable
fun ReceivingCountryLayoutLoadingPreview() {
    ReceivingCountryLayout(
        isLoading = true,
        topCountries = listOf(
            CountryUIModel("ESP", "España"),
            CountryUIModel("PT", "Portugal"),
            CountryUIModel("PK", "Pakistan"),
            CountryUIModel("PH", "Filipinas"),
            CountryUIModel("PH", "Filipinas"),
        ),
        onChipClicked = { _ -> },
    )
}
