package com.smallworldfs.moneytransferapp.compose.state

import androidx.compose.ui.graphics.Color
import com.smallworldfs.moneytransferapp.compose.colors.linksGraphicElements
import com.smallworldfs.moneytransferapp.compose.colors.linksOverDarkBG
import com.smallworldfs.moneytransferapp.compose.colors.onSecondaryHighEmphasis
import com.smallworldfs.moneytransferapp.compose.colors.onSecondaryMediumEmphasis
import com.smallworldfs.moneytransferapp.compose.colors.onSurfaceHighEmphasis
import com.smallworldfs.moneytransferapp.compose.colors.onSurfaceMediumEmphasis
import com.smallworldfs.moneytransferapp.compose.colors.surfaceOverlay
import com.smallworldfs.moneytransferapp.compose.colors.surfaceSecondary
import com.smallworldfs.moneytransferapp.compose.style.SWTextStyle

sealed class SWCountryContainerState(
    val backgroundColor: Color,
    val titleStyle: SWTextStyle,
    val bodyStyle: SWTextStyle,
    val titleColor: Color,
    val bodyColor: Color,
    val iconTintColor: Color
) {

    object Primary : SWCountryContainerState(
        backgroundColor = surfaceOverlay,
        titleStyle = SWTextStyle.Body1,
        bodyStyle = SWTextStyle.Body2,
        titleColor = onSurfaceHighEmphasis,
        bodyColor = onSurfaceMediumEmphasis,
        iconTintColor = linksGraphicElements,
    )

    object Secondary : SWCountryContainerState(
        backgroundColor = surfaceSecondary,
        titleStyle = SWTextStyle.Body1Secondary,
        bodyStyle = SWTextStyle.Body2Secondary,
        titleColor = onSecondaryHighEmphasis,
        bodyColor = onSecondaryMediumEmphasis,
        iconTintColor = linksOverDarkBG,
    )
}
