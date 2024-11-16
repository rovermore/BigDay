package com.smallworldfs.moneytransferapp.presentation.common.countries

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smallworldfs.moneytransferapp.compose.widgets.CountryListItem
import com.smallworldfs.moneytransferapp.compose.widgets.SWSearchBar
import com.smallworldfs.moneytransferapp.compose.widgets.SWTopAppBar
import com.smallworldfs.moneytransferapp.compose.widgets.SearchEmptyView
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

@Composable
fun SearchCountry(
    onItemClicked: (CountryUIModel) -> Unit,
    onValueChange: (String) -> Unit,
    countryList: List<CountryUIModel> = emptyList(),
    onBackPressed: Action,
    barTitle: String = STRING_EMPTY
) {
    MainSearchCountryView(onItemClicked, onValueChange, countryList, onBackPressed, barTitle)
}

@Composable
fun MainSearchCountryView(
    onItemClicked: (CountryUIModel) -> Unit,
    onValueChange: (String) -> Unit,
    countryList: List<CountryUIModel>,
    onBackPressed: Action,
    barTitle: String,
    viewModel: SearchCountryViewModel = viewModel()
) {
    val fetchedCountries by viewModel.countries.collectAsStateWithLifecycle()
    var countries by rememberSaveable { mutableStateOf(countryList) }
    var showEmptyView by rememberSaveable { mutableStateOf(false) }

    if (fetchedCountries.isNotEmpty() && countries.isEmpty()) countries = fetchedCountries

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        SWTopAppBar(
            barTitle = barTitle,
            onBackPressed = onBackPressed,
        )
        SWSearchBar(
            onValueChange = { text ->
                if (text.isNotEmpty()) {
                    countries = countries.filter { it.name.toLowerCase().contains(text.toLowerCase()) }
                    showEmptyView = countries.isEmpty()
                } else {
                    countries = countryList
                    showEmptyView = false
                }
                onValueChange(text)
            },
        )

        if (showEmptyView) {
            SearchEmptyView()
        } else {
            LazyColumn(modifier = Modifier.fillMaxHeight()) {
                items(countries) { country ->
                    CountryListItem(country = country, onItemClicked = onItemClicked)
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Select Destination Country preview", widthDp = 420)
@Composable
fun SearchCountryPreview() {
    SearchCountry(onItemClicked = {}, onValueChange = {}, onBackPressed = {})
}
