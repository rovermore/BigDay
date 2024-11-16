package com.smallworldfs.moneytransferapp.base.data.net

import android.content.Context
import android.webkit.URLUtil
import com.android.riskifiedbeacon.RiskifiedBeaconMain
import com.android.riskifiedbeacon.RxBeacon
import com.smallworldfs.moneytransferapp.BuildConfig
import java.net.URL
import javax.inject.Inject

class RiskifiedInstance @Inject constructor(private val context: Context) {

    companion object {
        const val RISKIFIED_SHOP_NAME = "smallworldfs.com"
    }

    // Call this method in the end of onCreate method
    fun startBeacon(userToken: String) = RiskifiedBeaconMain().startBeacon(
        RISKIFIED_SHOP_NAME,
        userToken, BuildConfig.DEBUG, context
    )

    fun updateSessionToken(token: String) {
        if (RxBeacon.instance != null) {
            RxBeacon.instance.setToken(token)
        }
    }

    fun logRequest(request: String) {
        if (RxBeacon.instance != null) {
            if (URLUtil.isValidUrl(request)) {
                val url = URL(request)
                RxBeacon.instance.logUrl(url)
            }
        }
    }

    // Optional
    fun logSensitiveDeviceInfo() {
        if (RxBeacon.instance != null) {
            RxBeacon.instance.logAccountInfo()
        }
    }

    // Call this method after onStop call
    fun removeLocUpdates() {
        if (RxBeacon.instance != null) {
            RxBeacon.instance.removeLocUpdates()
        }
    }
}
