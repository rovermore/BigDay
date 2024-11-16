package com.smallworldfs.moneytransferapp.compose.state

import androidx.compose.ui.graphics.Color
import com.smallworldfs.moneytransferapp.compose.colors.onSurfaceDisabled
import com.smallworldfs.moneytransferapp.compose.colors.onSurfaceMediumEmphasis
import com.smallworldfs.moneytransferapp.compose.colors.primary
import com.smallworldfs.moneytransferapp.compose.colors.secondary
import com.smallworldfs.moneytransferapp.compose.colors.surfaceOverlay
import com.smallworldfs.moneytransferapp.compose.colors.surfaceOverlaySelected
import com.smallworldfs.moneytransferapp.compose.style.SWTextStyle

sealed class SWChipState(
    val textStyle: SWTextStyle,
    val textColor: Color,
    val backgroundColor: Color,
    val borderWidth: Int,
    val borderColor: Color
) {
    object Enabled : SWChipState(
        textStyle = SWTextStyle.Body2,
        textColor = onSurfaceMediumEmphasis,
        backgroundColor = surfaceOverlay,
        borderWidth = 0,
        borderColor = surfaceOverlay,
    )

    object Disabled : SWChipState(
        textStyle = SWTextStyle.Body2Disabled,
        textColor = onSurfaceDisabled,
        backgroundColor = surfaceOverlay,
        borderWidth = 0,
        borderColor = surfaceOverlay,
    )

    object SelectedPrimary : SWChipState(
        textStyle = SWTextStyle.Body2,
        textColor = onSurfaceMediumEmphasis,
        backgroundColor = surfaceOverlaySelected,
        borderWidth = 2,
        borderColor = primary,
    )

    object SelectedSecondary : SWChipState(
        textStyle = SWTextStyle.Body2,
        textColor = onSurfaceMediumEmphasis,
        backgroundColor = surfaceOverlaySelected,
        borderWidth = 2,
        borderColor = secondary,
    )
}
