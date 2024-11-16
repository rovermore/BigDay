package com.smallworldfs.moneytransferapp.modules.notifications.domain.handler

import android.content.Context
import com.braze.models.inappmessage.IInAppMessage
import javax.inject.Inject

class InAppMessageClickHandlerFactory @Inject constructor(
    private val pushPrimeClickHandler: PushPrimeClickHandler
) {

    fun createClickActionHandler(context: Context, inAppMessage: IInAppMessage): InAppClickHandler = when (inAppMessage.extras["msg-id"]) {
        "push-primer" -> pushPrimeClickHandler
        else -> object : InAppClickHandler { }
    }
}
