package com.smallworldfs.moneytransferapp.data.braze

import com.braze.Braze
import com.braze.BrazeUser
import com.braze.enums.NotificationSubscriptionType
import com.braze.events.IValueCallback
import com.braze.models.outgoing.BrazeProperties
import com.braze.ui.inappmessage.BrazeInAppMessageManager
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventProperty
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventType
import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.peek
import com.smallworldfs.moneytransferapp.modules.notifications.domain.listener.CustomInAppMessageManagerListener
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.runBlocking
import org.json.JSONObject
import java.math.BigDecimal
import javax.inject.Inject

class BrazeDatasource @Inject constructor(
    private val braze: Braze,
    customInAppMessageManagerListener: CustomInAppMessageManagerListener
) : NetworkDatasource() {

    init {
        // Braze
        BrazeInAppMessageManager.getInstance().setCustomInAppMessageManagerListener(customInAppMessageManagerListener)
    }

    fun logEvent(event: BrazeEvent) {
        when (event.type) {
            BrazeEventType.PURCHASE -> {
                braze.logPurchase(
                    event.properties[BrazeEventProperty.PRODUCT_ID.value].toString(),
                    event.properties[BrazeEventProperty.CURRENCY_CODE.value].toString(),
                    event.properties[BrazeEventProperty.PRICE.value]?.toBigDecimal() ?: BigDecimal.ZERO,
                    event.properties[BrazeEventProperty.QUANTITY.value]?.toInt() ?: INT_ZERO,
                )
            }

            else -> braze.logCustomEvent(
                event.name,
                BrazeProperties(JSONObject(event.properties)),
            )
        }
    }

    fun changeUser(uuid: String, email: String = "", country: String = "") {
        braze.changeUser(uuid)
        getCurrentUser().peek { user ->
            user.setEmail(email)
            user.setCustomUserAttribute("Sending Country", country)
        }
    }

    private fun getCurrentUser(): OperationResult<BrazeUser, APIError> {
        val deferred = CompletableDeferred<BrazeUser?>()
        braze.getCurrentUser(
            object : IValueCallback<BrazeUser> {
                override fun onSuccess(value: BrazeUser) {
                    deferred.complete(value)
                }

                override fun onError() {
                    deferred.complete(null)
                }
            },
        )

        val result = runBlocking {
            val deferredResult = deferred.await()
            if (deferredResult != null) {
                Success(deferredResult)
            } else {
                Failure(
                    mapAPIError(
                        mapNetworkExceptions(
                            400,
                            "Braze get user exception",
                        ),
                    ),
                )
            }
        }
        return result
    }

    fun enablePushNotifications(): OperationResult<Boolean, APIError> {
        return getCurrentUser()
            .map { brazeUser ->
                brazeUser.setPushNotificationSubscriptionType(NotificationSubscriptionType.OPTED_IN)
                true
            }
    }

    fun disablePushNotifications(): OperationResult<Boolean, APIError> {
        return getCurrentUser()
            .map { brazeUser ->
                brazeUser.setPushNotificationSubscriptionType(NotificationSubscriptionType.UNSUBSCRIBED)
                true
            }
    }
}
