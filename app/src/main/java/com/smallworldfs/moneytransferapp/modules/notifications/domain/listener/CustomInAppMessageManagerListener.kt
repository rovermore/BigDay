package com.smallworldfs.moneytransferapp.modules.notifications.domain.listener

import android.content.Context
import com.braze.models.inappmessage.IInAppMessage
import com.braze.models.inappmessage.MessageButton
import com.braze.ui.inappmessage.BrazeInAppMessageManager
import com.braze.ui.inappmessage.InAppMessageOperation
import com.braze.ui.inappmessage.listeners.IInAppMessageManagerListener
import com.smallworldfs.moneytransferapp.modules.notifications.domain.handler.InAppMessageClickHandlerFactory
import javax.inject.Inject

class CustomInAppMessageManagerListener @Inject constructor(
    private val context: Context,
    private val factory: InAppMessageClickHandlerFactory,
) : IInAppMessageManagerListener {

    override fun beforeInAppMessageDisplayed(inAppMessage: IInAppMessage): InAppMessageOperation {
        return InAppMessageOperation.DISPLAY_NOW
    }

    override fun onInAppMessageButtonClicked(inAppMessage: IInAppMessage, button: MessageButton): Boolean {
        BrazeInAppMessageManager.getInstance().resetAfterInAppMessageClose()
        return factory.createClickActionHandler(context, inAppMessage).onButtonClicked(button)
    }
}
