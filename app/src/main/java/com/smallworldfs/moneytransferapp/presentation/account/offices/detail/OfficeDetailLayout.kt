package com.smallworldfs.moneytransferapp.presentation.account.offices.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.defaultGreyLightBackground
import com.smallworldfs.moneytransferapp.compose.colors.mediumBlue
import com.smallworldfs.moneytransferapp.compose.colors.transparent
import com.smallworldfs.moneytransferapp.compose.colors.neutral0
import com.smallworldfs.moneytransferapp.compose.dialogs.SWInfoDialog
import com.smallworldfs.moneytransferapp.compose.widgets.SWTopError
import com.smallworldfs.moneytransferapp.compose.widgets.SWMap
import com.smallworldfs.moneytransferapp.compose.widgets.SWTopAppBar
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.presentation.account.offices.model.OfficeUIModel
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.common.coordinates.SWCoordinates

@Composable
fun OfficeDetailVM(
    officeCallbacks: OfficeDetailsCallbacks,
    office: OfficeUIModel,
    viewModel: OfficeDetailViewModel = viewModel()
) {

    var officeSelected by rememberSaveable { mutableStateOf(office) }

    val distance by viewModel.distance.collectAsStateWithLifecycle()

    val officesError by viewModel.officesError.collectAsStateWithLifecycle()
    val officePoiList by viewModel.officePoiList.collectAsStateWithLifecycle()

    val userLocation by viewModel.userLocation.collectAsStateWithLifecycle()
    val userLocationError by viewModel.userLocationError.collectAsStateWithLifecycle()

    OfficeDetail(
        officeCallbacks,
        officesError,
        officeSelected,
        distance,
        userLocation,
        userLocationError,
        officePoiList,
        { viewModel.hideOfficesError() },
        { officeClicked ->
            officeSelected = officeClicked
            viewModel.calculateDistance(userLocation, officeClicked.location)
        },
        { viewModel.getOfficePoi() },
        { viewModel.getUserLocation(officeSelected.location) }
    )
}

@Composable
fun OfficeDetail(
    officeCallbacks: OfficeDetailsCallbacks,
    officesError: ErrorType,
    officeSelected: OfficeUIModel,
    distance: String,
    userLocation: SWCoordinates,
    userLocationError: ErrorType,
    officePoiList: List<OfficeUIModel>,
    onCloseError: Action,
    onOfficeSelected: (OfficeUIModel) -> Unit,
    onMapLoaded: Action,
    requestUserLocation: Action
) {

    var isUserLocationErrorShown by rememberSaveable {
        mutableStateOf(false)
    }

    OfficeDetailMain(officeCallbacks, officeSelected, distance, officePoiList, onOfficeSelected, onMapLoaded, userLocation, requestUserLocation)
    if (officesError !is ErrorType.None)
        SWTopError(
            body = officesError.message,
            onCloseIconClick = { onCloseError() },
        )

    if (userLocationError !is ErrorType.None && !isUserLocationErrorShown) {
        isUserLocationErrorShown = true
        SWInfoDialog(
            title = stringResource(id = R.string.info_text),
            content = stringResource(id = R.string.location_settings_granted_needed),
            dismissAction = { onCloseError() },
        )
    }
}

@Composable
fun OfficeDetailMain(
    officeCallbacks: OfficeDetailsCallbacks,
    office: OfficeUIModel,
    distance: String,
    officePoiList: List<OfficeUIModel>,
    onOfficeSelected: (OfficeUIModel) -> Unit,
    onMapLoaded: Action,
    userLocation: SWCoordinates,
    requestUserLocation: Action
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = defaultGreyLightBackground)
    ) {

        var locationClicked by rememberSaveable {
            mutableStateOf(false)
        }

        SWTopAppBar(barTitle = stringResource(id = R.string.offices_map_title), onBackPressed = { officeCallbacks.clickBack() })

        Box(
            modifier = Modifier
                .background(color = defaultGreyLightBackground)
                .fillMaxSize()
        ) {

            SWMap(
                officeSelected = office,
                officeList = officePoiList,
                onMarkerClicked = { officeClicked -> onOfficeSelected(officeClicked) },
                onMapLoaded = onMapLoaded,
                isLocationEnabled = userLocation !is SWCoordinates.NotDefined,
                selectedPosition = userLocation.toLatLang(),
                isLocationClicked = locationClicked,
                onCameraMovedToLocation = { locationClicked = false }
            )

            Box(
                modifier = Modifier
                    .background(color = transparent)
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
            ) {

                Column {
                    Box(
                        Modifier
                            .background(color = transparent)
                            .fillMaxWidth(),
                    ) {

                        FloatingActionButton(
                            onClick = {
                                officeCallbacks.registerEvent("click_geolocalize")
                                if (userLocation.longitude != 0.0 && userLocation.latitude != 0.0)
                                    locationClicked = true
                                else
                                    requestUserLocation()
                            },
                            modifier = Modifier
                                .background(color = transparent)
                                .padding(end = 16.dp, bottom = 64.dp)
                                .align(Alignment.TopEnd),
                            backgroundColor = neutral0
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.account_icn_officesmaplocation),
                                "Floating action button.",
                                tint = Color.Black,
                            )
                        }
                    }
                    Box(
                        Modifier
                            .background(color = neutral0)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        ConstraintLayout {

                            val button = createRef()

                            FloatingActionButton(
                                onClick = {
                                    officeCallbacks.registerEvent("click_how_to_get_there")
                                    officeCallbacks.onIndicationsClicked(office)
                                },
                                modifier = Modifier
                                    .background(color = transparent)
                                    .padding(end = 16.dp, bottom = 16.dp, start = 16.dp)
                                    .constrainAs(button) {
                                        top.linkTo(parent.top)
                                        bottom.linkTo(parent.top)
                                    },
                                backgroundColor = mediumBlue,
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.account_icn_officesmapgolocation),
                                    "Floating action button.",
                                    tint = neutral0
                                )
                            }
                        }
                    }

                    OfficeName(office, distance, officeCallbacks)
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Onboarding preview", widthDp = 420)
@Composable
fun OfficeDetailDefaultPreview() {
    OfficeDetail(
        object : OfficeDetailsCallbacks {
            override fun onPhoneClicked(phone: String) {
            }

            override fun onMailClicked(mail: String) {
            }

            override fun onIndicationsClicked(officeSelect4d: OfficeUIModel) {
            }

            override fun clickBack() {
            }

            override fun registerEvent(eventAction: String) {
            }
        },
        ErrorType.None,
        OfficeUIModel(),
        "4634 km",
        SWCoordinates.NotDefined,
        ErrorType.None,
        emptyList(),
        {},
        {},
        {},
        {}
    )
}
