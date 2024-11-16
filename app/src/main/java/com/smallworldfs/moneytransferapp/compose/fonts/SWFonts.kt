package com.smallworldfs.moneytransferapp.compose.fonts

import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.smallworldfs.moneytransferapp.R

val fonts = FontFamily(
    Font(R.font.nunito_black, weight = FontWeight.ExtraBold),
    Font(R.font.nunito_bold, weight = FontWeight.Bold),
    Font(R.font.nunito_light, weight = FontWeight.Light),
    Font(R.font.nunito_semi_bold, weight = FontWeight.SemiBold),
)

val openSansFonts = FontFamily(
    Font(R.font.opensans_light, weight = FontWeight.Light),
    Font(R.font.opensans_bold, weight = FontWeight.Bold),
    Font(R.font.opensans_regular, weight = FontWeight.Normal),
    Font(R.font.opensans_semi_bold, weight = FontWeight.SemiBold),
)
