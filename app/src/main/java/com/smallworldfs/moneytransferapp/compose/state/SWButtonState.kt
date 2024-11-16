package com.smallworldfs.moneytransferapp.compose.state

sealed class SWButtonState {
    object Enabled : SWButtonState()
    object Pressed : SWButtonState()
    object Disabled : SWButtonState()
}
