package com.smallworldfs.moneytransferapp.presentation.freeuser.country_selection

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName
import com.smallworldfs.moneytransferapp.compose.colors.linksGraphicElements
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.compose.colors.onPrimaryHighEmphasis
import com.smallworldfs.moneytransferapp.compose.colors.onSurfaceHighEmphasis
import com.smallworldfs.moneytransferapp.compose.state.SWButtonState
import com.smallworldfs.moneytransferapp.compose.style.SWTextStyle.Heading
import com.smallworldfs.moneytransferapp.compose.widgets.CountryListType
import com.smallworldfs.moneytransferapp.compose.widgets.SWBottomSheetContentCountryList
import com.smallworldfs.moneytransferapp.compose.widgets.SWFormButton
import com.smallworldfs.moneytransferapp.compose.widgets.SWStepperHorizontal
import com.smallworldfs.moneytransferapp.compose.widgets.SWText
import com.smallworldfs.moneytransferapp.compose.widgets.model.SWCountryListWithSectionsItemUIModel
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel
import com.smallworldfs.moneytransferapp.presentation.freeuser.ReceivingCountryLayout
import com.smallworldfs.moneytransferapp.presentation.freeuser.SendingCountryLayout
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import kotlinx.coroutines.launch

@Composable
fun CountrySelectionLayout(
    viewModel: CountrySelectionViewModel = viewModel(),
    countrySelectionListener: CountrySelectionListener
) {
    val selectedOriginCountry = viewModel.selectedOriginCountry.collectAsStateWithLifecycle()
    val selectedDestinationCountry by viewModel.selectedDestinationCountry.collectAsStateWithLifecycle()
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val image = Constants.COUNTRY.FLAG_IMAGE_ASSETS + selectedOriginCountry.value.iso3 + Constants.COUNTRY.FLAG_IMAGE_EXTENSION
    val originCountries by viewModel.filteredOriginList.collectAsStateWithLifecycle()
    val destinationCountries by viewModel.filteredDestinationList.collectAsStateWithLifecycle()
    val featuredCountries by viewModel.featuredCountries.collectAsStateWithLifecycle()
    val freeUserCreated by viewModel.freeUserCreated.collectAsStateWithLifecycle()

    Content(
        loading = loading,
        image = image,
        originCountries = originCountries,
        destinationCountries = destinationCountries,
        featuredCountries = featuredCountries,
        selectedOriginCountry = selectedOriginCountry,
        selectedDestinationCountry = selectedDestinationCountry,
        contentCallbacks = object : CountrySelectionContentCallbacks {
            override fun onOriginCountrySelected(country: CountryUIModel) {
                countrySelectionListener.onOriginCountrySelected(country)
                viewModel.saveOriginCountry(country)
            }

            override fun onDestinationCountrySelected(country: CountryUIModel) {
                countrySelectionListener.onDestinationCountrySelected(country)
                viewModel.saveDestinationCountry(country)
            }

            override fun onStepChanged() {
                countrySelectionListener.onContinueButtonClicked(
                    eventLabel = viewModel.selectedOriginCountry.value.iso3,
                    origin = viewModel.selectedOriginCountry.value.iso3,
                )
                viewModel.getDestinationCountries(selectedOriginCountry.value.iso3)
            }

            override fun onQueryOriginChanged(query: String) {
                viewModel.filterOrigin(query)
            }

            override fun onQueryDestinationChanged(query: String) {
                viewModel.filterDestination(query)
            }

            override fun onOriginSearchClicked() {
                countrySelectionListener.onOriginSearchClick(
                    eventName = "search_sending_from",
                    hierarchy = ScreenName.SENDING_FROM_SCREEN.value,
                )
            }

            override fun onDestinationSearchClicked() {
                countrySelectionListener.onDestinationSearchClick(
                    eventName = "search_sending_to",
                    hierarchy = ScreenName.SENDING_TO_SCREEN.value,
                )
            }

            override fun onFreeUserCreated(user: UserDTO, originCountry: CountryUIModel, destinationCountry: CountryUIModel) {
                countrySelectionListener.onFreeUserCreated(
                    user = user,
                    originCountry = originCountry,
                    destinationCountry = destinationCountry,
                )
            }

            override fun trackSendingFromScreen() {
                countrySelectionListener.trackSendingFromScreen()
            }

            override fun trackSendingToScreen() {
                countrySelectionListener.trackSendingToScreen()
            }

            override fun dismissBottomSheetEvent(eventAction: String, hierarchy: String) {
                countrySelectionListener.dismissBottomSheetEvent(eventAction, hierarchy)
            }
        },
    )

    LaunchedEffect(freeUserCreated) {
        if (freeUserCreated.id.isNotEmpty())
            countrySelectionListener.onFreeUserCreated(
                user = freeUserCreated,
                originCountry = viewModel.origin,
                destinationCountry = viewModel.destination,
            )
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Content(
    loading: Boolean,
    image: String,
    originCountries: List<SWCountryListWithSectionsItemUIModel>,
    destinationCountries: List<SWCountryListWithSectionsItemUIModel>,
    featuredCountries: List<CountryUIModel>,
    selectedOriginCountry: State<CountryUIModel>,
    selectedDestinationCountry: CountryUIModel,
    contentCallbacks: CountrySelectionContentCallbacks,
) {
    var currentStep by rememberSaveable {
        mutableIntStateOf(INT_ZERO)
    }

    val coroutineScope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = true,
    )

    val continueButtonState by remember {
        derivedStateOf {
            if (selectedOriginCountry.value.iso3.isEmpty())
                SWButtonState.Disabled
            else
                SWButtonState.Enabled
        }
    }

    val countryListType by remember {
        derivedStateOf {
            if (currentStep == 0) {
                CountryListType.SendMoneyFrom
            } else {
                CountryListType.SendMoneyTo
            }
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
        sheetContent = {
            SWBottomSheetContentCountryList(
                sheetState = sheetState,
                countryListType = countryListType,
                elements = if (countryListType is CountryListType.SendMoneyFrom) originCountries else destinationCountries,
                dismissBottomSheetEvent = { eventAction, hierarchy ->
                    contentCallbacks.dismissBottomSheetEvent(
                        eventAction = eventAction,
                        hierarchy = hierarchy,
                    )
                },
                selectedOriginCountry = selectedOriginCountry.value,
                selectedDestinationCountry = selectedDestinationCountry,
                onQueryChanged = {
                    if (countryListType is CountryListType.SendMoneyFrom) {
                        contentCallbacks.onQueryOriginChanged(it)
                    } else {
                        contentCallbacks.onQueryDestinationChanged(it)
                    }
                },
                onOriginCountrySelected = { contentCallbacks.onOriginCountrySelected(it) },
                onDestinationCountrySelected = { contentCallbacks.onDestinationCountrySelected(it) },
            )
        },
    ) {
        Column(
            modifier = Modifier
                .background(neutral0)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
        ) {
            Row(
                modifier = Modifier
                    .padding(top = 16.dp, bottom = 32.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (currentStep == 1) {
                    Icon(
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                currentStep = 0
                            },
                        painter = painterResource(id = R.drawable.linear_arrow_left),
                        contentDescription = "arrow_back",
                        tint = linksGraphicElements,
                    )
                }
                SWText(
                    modifier = Modifier
                        .fillMaxWidth(),
                    style = Heading,
                    text = stringResource(id = R.string.set_up_your_account),
                    color = onPrimaryHighEmphasis,
                    textAlign = Center,
                )
            }

            SWStepperHorizontal(
                selectedStep = { currentStep },
                onStepClicked = {
                    if (continueButtonState !is SWButtonState.Disabled) {
                        if (it == 1) {
                            contentCallbacks.onStepChanged()
                        }
                        currentStep = it
                    }
                },
                steps = listOf(0, 1),
            )

            SWText(
                modifier = Modifier.padding(
                    top = 32.dp,
                    bottom = 16.dp,
                ),
                style = Heading,
                color = onSurfaceHighEmphasis,
                text = if (currentStep == 0) {
                    stringResource(id = R.string.select_your_sending_country)
                } else {
                    stringResource(id = R.string.select_the_receiving_country)
                },
            )

            SWFormButton(
                text = if (currentStep == 0) {
                    stringResource(id = R.string.sending_money_from)
                } else {
                    stringResource(id = R.string.sending_money_to)
                },
                onButtonClicked = {
                    coroutineScope.launch {
                        sheetState.show()
                    }
                },
            )

            when (currentStep) {
                0 -> {
                    contentCallbacks.trackSendingFromScreen()
                    SendingCountryLayout(
                        modifier = Modifier
                            .padding(top = 16.dp),
                        countryName = selectedOriginCountry.value.name,
                        urlFlag = image,
                        buttonState = continueButtonState,
                        isLoading = loading,
                        onContinueClicked = {
                            contentCallbacks.onStepChanged()
                            currentStep = 1
                        },
                    )
                }

                1 -> {
                    contentCallbacks.trackSendingToScreen()
                    ReceivingCountryLayout(
                        modifier = Modifier
                            .padding(top = 16.dp),
                        isLoading = loading,
                        topCountries = featuredCountries,
                        onChipClicked = {
                            contentCallbacks.onDestinationCountrySelected(it)
                        },
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 420)
@Composable
fun CountrySelectionLayoutLayoutPreview() {
    Content(
        loading = false,
        image = STRING_EMPTY,
        originCountries = emptyList(),
        destinationCountries = emptyList(),
        featuredCountries = emptyList(),
        selectedOriginCountry = remember { mutableStateOf(CountryUIModel()) },
        selectedDestinationCountry = CountryUIModel(),
        contentCallbacks = object : CountrySelectionContentCallbacks {
            override fun onOriginCountrySelected(country: CountryUIModel) {}
            override fun onDestinationCountrySelected(country: CountryUIModel) {}
            override fun onStepChanged() {}
            override fun onOriginSearchClicked() {}
            override fun onDestinationSearchClicked() {}
            override fun onFreeUserCreated(user: UserDTO, originCountry: CountryUIModel, destinationCountry: CountryUIModel) {}
            override fun trackSendingFromScreen() {}
            override fun trackSendingToScreen() {}
            override fun dismissBottomSheetEvent(eventAction: String, hierarchy: String) {}
            override fun onQueryDestinationChanged(query: String) {}
            override fun onQueryOriginChanged(query: String) {}
        },
    )
}
