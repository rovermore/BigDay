package com.smallworldfs.moneytransferapp.compose.style

sealed class SWChipStyle {
    class Flag(val imageUrl: String) : SWChipStyle()
    object Star : SWChipStyle()
}
