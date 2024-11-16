package com.smallworldfs.moneytransferapp.compose.style

import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.smallworldfs.moneytransferapp.compose.fonts.fonts
import com.smallworldfs.moneytransferapp.compose.fonts.openSansFonts

sealed class SWTextStyle(
    val fontSize: TextUnit = 20.sp,
    val fontWeight: FontWeight = FontWeight.Normal,
    val fontFamily: FontFamily = fonts
) {
    object Heading : SWTextStyle()

    object HeadingBold : SWTextStyle(
        fontWeight = FontWeight.Bold,
    )

    object Title : SWTextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
    )

    object Body1 : SWTextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = openSansFonts,
    )

    object Body1Secondary : SWTextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = openSansFonts,
    )

    object Body1Bold : SWTextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = openSansFonts,
    )

    object Body2 : SWTextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = openSansFonts,
    )

    object Body2Secondary : SWTextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        fontFamily = openSansFonts,
    )

    object Body2Bold : SWTextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = openSansFonts,
    )

    object Body2Disabled : SWTextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold,
        fontFamily = openSansFonts,
    )

    object Button : SWTextStyle(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold,
    )

    object Label : SWTextStyle(
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        fontFamily = openSansFonts,
    )

    object Overline : SWTextStyle(
        fontSize = 11.sp,
        fontFamily = openSansFonts,
    )
}
