package com.smallworldfs.moneytransferapp.compose.widgets

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.compose.colors.blueAccentColor
import com.smallworldfs.moneytransferapp.domain.migrated.base.Action
import com.smallworldfs.moneytransferapp.presentation.account.offices.model.OfficeUIModel

@Composable
fun SWMap(
    officeSelected: OfficeUIModel,
    officeList: List<OfficeUIModel>,
    onMarkerClicked: (OfficeUIModel) -> Unit,
    onMapLoaded: Action,
    isLocationEnabled: Boolean = false,
    selectedPosition: LatLng,
    isLocationClicked: Boolean,
    onCameraMovedToLocation: Action
) {
    var officePosition by rememberSaveable { mutableStateOf(officeSelected.location.toLatLang()) }
    var userPosition by rememberSaveable { mutableStateOf(LatLng(0.0, 0.0)) }
    if (selectedPosition != LatLng(0.0, 0.0))
        userPosition = selectedPosition

    var locationClicked by rememberSaveable { mutableStateOf(false) }
    locationClicked = isLocationClicked

    val cameraPositionState: CameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(officeSelected.location.toLatLang(), 14f)
    }

    LaunchedEffect(officePosition) {
        cameraPositionState.animate(
            update = CameraUpdateFactory.newCameraPosition(
                CameraPosition(officePosition, 14f, 0f, 0f),
            ),
            durationMs = 1000,
        )
    }
    LaunchedEffect(locationClicked) {
        if (locationClicked) {
            cameraPositionState.animate(
                update = CameraUpdateFactory.newCameraPosition(
                    CameraPosition(userPosition, 14f, 0f, 0f),
                ),
                durationMs = 1000,
            )
            onCameraMovedToLocation()
        }
    }

    val mapUiSettings by remember {
        mutableStateOf(
            MapUiSettings(
                compassEnabled = false,
                indoorLevelPickerEnabled = false,
                mapToolbarEnabled = false,
                myLocationButtonEnabled = false,
                tiltGesturesEnabled = false,
                zoomControlsEnabled = false,
            ),
        )
    }

    var showLoader by remember { mutableStateOf(true) }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            uiSettings = mapUiSettings,
            cameraPositionState = cameraPositionState,
            onMapLoaded = { onMapLoaded() },
            properties = MapProperties(isMyLocationEnabled = isLocationEnabled),
        ) {
            if (officeList.isNotEmpty()) {
                officeList.forEach {
                    Marker(
                        state = MarkerState((it.location.toLatLang())),
                        onClick = { _ ->
                            onMarkerClicked(it)
                            officePosition = it.location.toLatLang()
                            true
                        },
                        icon = BitmapDescriptorFactory.fromResource(R.drawable.account_icn_officespoi),
                    )
                }.also {
                    showLoader = false
                }
            }
        }

        if (showLoader) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .alpha(0.5f),
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.width(64.dp),
                        color = blueAccentColor,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "Map preview", widthDp = 420)
@Composable
fun SWMapPreview() {
    SWMap(
        officeSelected = OfficeUIModel(),
        officeList = emptyList(),
        onMarkerClicked = {},
        onMapLoaded = {},
        isLocationEnabled = false,
        selectedPosition = LatLng(0.0, 0.0),
        isLocationClicked = false,
        onCameraMovedToLocation = {},
    )
}
