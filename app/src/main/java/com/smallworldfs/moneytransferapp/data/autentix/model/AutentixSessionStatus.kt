package com.smallworldfs.moneytransferapp.data.autentix.model

sealed class AutentixSessionStatus {
    object FINISHED : AutentixSessionStatus()
    object PENDING : AutentixSessionStatus()
}
