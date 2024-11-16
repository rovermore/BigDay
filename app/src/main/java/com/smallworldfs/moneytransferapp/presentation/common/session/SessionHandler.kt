package com.smallworldfs.moneytransferapp.presentation.common.session

import android.content.Context
import android.content.Intent
import javax.inject.Inject

class SessionHandler @Inject constructor(val context: Context) {

    companion object {
        const val SESSION_EXPIRED_ACTION = "SESSION_EXPIRED"
    }

    fun sendSessionExpiredEvent() {
        val intent = Intent(SESSION_EXPIRED_ACTION)
        context.sendBroadcast(intent)
    }
}
