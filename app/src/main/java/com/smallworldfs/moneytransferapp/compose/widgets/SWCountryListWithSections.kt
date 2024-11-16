package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.compose.colors.surfaceOverlaySelected
import com.smallworldfs.moneytransferapp.compose.style.SWStartListItemStyle
import com.smallworldfs.moneytransferapp.compose.style.SWTextStyle
import com.smallworldfs.moneytransferapp.compose.widgets.model.SWCountryListWithSectionsItemUIModel
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel

@Composable
fun SWCountryListWithSections(
    elements: List<SWCountryListWithSectionsItemUIModel>,
    onCountrySelected: (CountryUIModel) -> Unit,
    selectedCountry: CountryUIModel
) {

    LazyColumn {
        if (elements.filterIsInstance<SWCountryListWithSectionsItemUIModel.Item>().isEmpty()) {
            item {
                SWCountryListItem(
                    itemUIModel = SWCountryListWithSectionsItemUIModel.EmptyItem,
                    onItemSelected = { },
                )
            }
        } else {
            items(elements) { element ->
                SWCountryListItem(
                    itemUIModel = element,
                    onItemSelected = { country -> onCountrySelected(country) },
                    isSelected =
                    element is SWCountryListWithSectionsItemUIModel.Item &&
                        element.country.iso3 == selectedCountry.iso3,
                )
            }
        }
    }
}

@Composable
private fun SWCountryListItem(
    itemUIModel: SWCountryListWithSectionsItemUIModel,
    onItemSelected: (CountryUIModel) -> Unit,
    isSelected: Boolean = false
) {
    when (itemUIModel) {
        is SWCountryListWithSectionsItemUIModel.Header -> {
            SWText(
                modifier = Modifier
                    .padding(top = 16.dp, start = 8.dp, end = 8.dp),
                text = stringResource(id = itemUIModel.title),
                style = SWTextStyle.Title,
            )
        }

        is SWCountryListWithSectionsItemUIModel.Item -> {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (isSelected) {
                            surfaceOverlaySelected
                        } else {
                            neutral0
                        },
                    )
                    .clickable {
                        onItemSelected(itemUIModel.country)
                    },
            ) {
                SWImageFlag(
                    modifier = Modifier
                        .padding(
                            start = 16.dp,
                            top = 16.dp,
                        ),
                    size = 24.dp,
                    imageUrl = itemUIModel.country.logo,
                )
                SWText(
                    modifier = Modifier
                        .padding(
                            start = 10.dp,
                            top = 16.dp,
                            bottom = 16.dp,
                        ),
                    text = itemUIModel.country.name,
                    style = SWTextStyle.Body1,
                )
            }
        }

        is SWCountryListWithSectionsItemUIModel.EmptyItem -> {
            SWListItem(
                startListItem = SWStartListItemStyle.Empty,
                body1 = stringResource(id = R.string.country_not_found_title),
                body2 = stringResource(id = R.string.country_not_found_description),
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun SWCountryListWithSectionsPreview() {
    SWCountryListWithSections(
        elements = listOf(
            SWCountryListWithSectionsItemUIModel.Header(
                title = R.string.previously_selected,
            ),
            SWCountryListWithSectionsItemUIModel.Item(
                CountryUIModel(
                    iso3 = "ESP",
                    name = "Spain",
                    logo = "",
                    prefix = "ESP",
                    countryCode = "ESP",
                    featured = false,
                ),
            ),
            SWCountryListWithSectionsItemUIModel.Header(
                title = R.string.select_new,
            ),
            SWCountryListWithSectionsItemUIModel.Item(
                CountryUIModel(
                    iso3 = "ESP",
                    name = "Spain",
                    logo = "",
                    prefix = "ESP",
                    countryCode = "ESP",
                    featured = false,
                ),
            ),
        ),
        onCountrySelected = {},
        selectedCountry = CountryUIModel(),
    )
}
