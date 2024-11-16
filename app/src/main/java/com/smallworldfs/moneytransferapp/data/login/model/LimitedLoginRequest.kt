package com.smallworldfs.moneytransferapp.data.login.model

data class LimitedLoginRequest(val appToken: String, val countryOrigin: String, val countryDestination: String)
