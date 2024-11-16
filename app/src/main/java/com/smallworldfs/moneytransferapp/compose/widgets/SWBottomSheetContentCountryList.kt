package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName
import com.smallworldfs.moneytransferapp.compose.colors.surface
import com.smallworldfs.moneytransferapp.compose.widgets.model.SWCountryListWithSectionsItemUIModel
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun SWBottomSheetContentCountryList(
    sheetState: ModalBottomSheetState,
    countryListType: CountryListType,
    elements: List<SWCountryListWithSectionsItemUIModel>,
    dismissBottomSheetEvent: (eventAction: String, hierarchy: String) -> Unit,
    selectedOriginCountry: CountryUIModel,
    selectedDestinationCountry: CountryUIModel,
    onQueryChanged: (query: String) -> Unit,
    onOriginCountrySelected: (country: CountryUIModel) -> Unit,
    onDestinationCountrySelected: (country: CountryUIModel) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(surface),
    ) {

        val localFocusManager = LocalFocusManager.current
        val coroutineScope = rememberCoroutineScope()
        val clearSearchBarText by remember { derivedStateOf { !sheetState.isVisible } }

        BottomSheetHeader(
            headerTitle = if (countryListType is CountryListType.SendMoneyFrom) stringResource(id = R.string.sending_money_from) else stringResource(id = R.string.sending_money_to),
            darkTheme = false,
            dismissListener = {
                localFocusManager.clearFocus()
                if (countryListType is CountryListType.SendMoneyFrom) {
                    dismissBottomSheetEvent(
                        "click_back_sending_from",
                        ScreenName.SENDING_FROM_SCREEN.value,
                    )
                } else {
                    dismissBottomSheetEvent(
                        "click_back_sending_to",
                        ScreenName.SENDING_TO_SCREEN.value,
                    )
                }
                coroutineScope.launch {
                    sheetState.hide()
                }
            },
        )

        SWSearchBarNew(
            modifier = Modifier
                .padding(horizontal = 16.dp),
            onValueChange = {
                onQueryChanged(it)
            },
            clearText = clearSearchBarText,
        )

        SWCountryListWithSections(
            elements = elements,
            onCountrySelected = {
                localFocusManager.clearFocus()
                if (countryListType is CountryListType.SendMoneyFrom) {
                    onOriginCountrySelected(it)
                } else {
                    onDestinationCountrySelected(it)
                }
                coroutineScope.launch {
                    sheetState.hide()
                }
            },
            selectedCountry = if (countryListType is CountryListType.SendMoneyFrom) selectedOriginCountry else selectedDestinationCountry,
        )
    }
}
