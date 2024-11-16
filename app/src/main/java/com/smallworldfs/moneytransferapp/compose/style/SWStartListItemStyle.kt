package com.smallworldfs.moneytransferapp.compose.style

sealed class SWStartListItemStyle {
    object None : SWStartListItemStyle()
    object Star : SWStartListItemStyle()
    object Empty : SWStartListItemStyle()
    object PlaceHolder : SWStartListItemStyle()
    class TickBox(val isChecked: Boolean, val onCheckBoxClicked: (Boolean) -> Unit) : SWStartListItemStyle()
    class FlagImage(val imageUrl: String) : SWStartListItemStyle()
}
