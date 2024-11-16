package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel
import com.smallworldfs.moneytransferapp.compose.colors.colorGrayLight

@Composable
fun CountryFlagsLazyRow(
    countries: List<CountryUIModel>,
    onItemClicked: (CountryUIModel) -> Unit,
    modifier: Modifier = Modifier
) {

    LazyRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        items(countries) { country ->
            CountryFlagItem(country = country, onItemClicked = onItemClicked)
        }
    }
}

@Composable
fun CountryFlagItem(
    country: CountryUIModel,
    onItemClicked: (CountryUIModel) -> Unit
) {
    Column(
        modifier = Modifier
            .padding(4.dp)
            .width(70.dp)
            .clickable { onItemClicked(country) },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        SWImageFlag(imageUrl = country.logo)
        SWText(
            modifier = Modifier.padding(8.dp),
            text = country.name,
            color = colorGrayLight,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun CountryFlagsLazyRowPreview() {
    CountryFlagsLazyRow(
        countries = listOf(
            CountryUIModel(
                iso3 = "ESP",
                name = "España",
            ),
            CountryUIModel(
                iso3 = "UK",
                name = "Reino Unido",
            )
        ),
        onItemClicked = {}
    )
}
