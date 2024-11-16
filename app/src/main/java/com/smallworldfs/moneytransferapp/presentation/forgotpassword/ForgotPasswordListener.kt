package com.smallworldfs.moneytransferapp.presentation.forgotpassword

interface ForgotPasswordListener {

    fun registerEventCallback(eventAction: String, eventLabel: String, formType: String)
    fun onBackAction()
    fun trackScreenName(screenName: String)
}
