package com.smallworldfs.moneytransferapp.presentation.autentix.model

import android.webkit.JavascriptInterface

interface JSInterface {
    @JavascriptInterface
    fun postMessage(json: String, origin: String) {}
}
