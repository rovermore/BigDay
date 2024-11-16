package com.smallworldfs.moneytransferapp.presentation.promotions.listener

interface PromotionsActivityListener {

    fun onBackPressed()
    fun onDoneAction()
    fun registerEventCallback(eventAction: String, eventLabel: String)
    fun registerBrazeEventCallback(eventName: String, eventProperties: Map<String, String>)
}
