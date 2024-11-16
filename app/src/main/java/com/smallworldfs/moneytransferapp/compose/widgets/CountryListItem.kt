package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel
import com.smallworldfs.moneytransferapp.compose.colors.colorGrayLight

@Composable
fun CountryListItem(
    country: CountryUIModel,
    onItemClicked: (CountryUIModel) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 16.dp, start = 12.dp)
                .clickable { onItemClicked(country) },
            verticalAlignment = Alignment.CenterVertically
        ) {
            SWImageFlag(imageUrl = country.logo)
            SWText(
                modifier = Modifier.padding(8.dp),
                text = country.name,
                color = colorGrayLight,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(
            modifier = Modifier
                .padding(start = 64.dp)
                .height(1.dp)
                .background(color = colorGrayLight)
                .fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun CountryListItemPreview() {
    CountryListItem(
        country = CountryUIModel(
            iso3 = "ESP",
            name = "España",
        ),
        onItemClicked = {}
    )
}
