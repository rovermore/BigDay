package com.smallworldfs.moneytransferapp.modules.notifications.domain.services

import com.braze.push.BrazeFirebaseMessagingService
import com.freshchat.consumer.sdk.Freshchat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.smallworldfs.moneytransferapp.utils.Log
import javax.inject.Singleton

@Singleton
class FCMessagingService : FirebaseMessagingService() {

    companion object {
        private val TAG = FCMessagingService::class.java.simpleName
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        if (remoteMessage.data.isNotEmpty()) {
            sendNotification(remoteMessage)
        }
    }

    override fun onNewToken(token: String) {
        Log.d(TAG, "Refreshed token: $token")
    }

    private fun sendNotification(remoteMessage: RemoteMessage) {
        if (BrazeFirebaseMessagingService.handleBrazeRemoteMessage(this, remoteMessage)) {
            // This Remote Message was originated from Braze and a push notification was displayed.
            // No further action is needed.
        } else if (Freshchat.isFreshchatNotification(remoteMessage)) {
            Freshchat.handleFcmMessage(this, remoteMessage)
        }
    }
}
