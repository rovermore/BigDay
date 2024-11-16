package com.smallworldfs.moneytransferapp.modules.notifications.domain.handler

import com.braze.models.inappmessage.MessageButton

interface InAppClickHandler {

    fun onButtonClicked(button: MessageButton): Boolean = false
}
