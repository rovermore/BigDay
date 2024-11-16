package com.smallworldfs.moneytransferapp.compose.state

sealed class StepState {
    object Done : StepState()
    object Current : StepState()
    object Disabled : StepState()
}
