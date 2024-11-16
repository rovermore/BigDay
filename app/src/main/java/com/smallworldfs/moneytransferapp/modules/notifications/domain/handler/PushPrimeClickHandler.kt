package com.smallworldfs.moneytransferapp.modules.notifications.domain.handler

import android.content.Context
import android.content.Intent
import com.braze.models.inappmessage.MessageButton
import javax.inject.Inject

class PushPrimeClickHandler @Inject constructor(private val context: Context) : InAppClickHandler {

    companion object {
        const val REQUEST_NOTIFICATION_PERMISSION = "REQUEST_NOTIFICATION_PERMISSION"
    }

    override fun onButtonClicked(button: MessageButton): Boolean {
        if (button.id == 1) {
            context.sendBroadcast(Intent(REQUEST_NOTIFICATION_PERMISSION))
        }
        return false
    }
}
