package com.smallworldfs.moneytransferapp.compose.style

sealed class SWCountryContainerStyle {
    abstract val imageUrl: String

    class SmallFlag(override val imageUrl: String) : SWCountryContainerStyle()
    class BigFlag(override val imageUrl: String) : SWCountryContainerStyle()
    class ClickableText(
        override val imageUrl: String,
        val firstText: String,
        val separator: String,
        val secondText: String
    ) : SWCountryContainerStyle()
}
