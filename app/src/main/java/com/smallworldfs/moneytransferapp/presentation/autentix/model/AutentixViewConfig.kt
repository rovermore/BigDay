package com.smallworldfs.moneytransferapp.presentation.autentix.model

data class AutentixViewConfig(
    val url: String,
    val timeout: Long,
    val externalId: String,
    val localStorageJavaScriptInterface: LocalStorageJavaScriptInterface,
    val jsInterface: JSInterface
)
