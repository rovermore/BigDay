package com.smallworldfs.moneytransferapp.data.base.network.interceptors

import android.content.Context
import android.provider.Settings
import com.smallworldfs.moneytransferapp.BuildConfig
import com.smallworldfs.moneytransferapp.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DeviceInfoInterceptor @Inject constructor(context: Context) : Interceptor {

    private var id: String = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .header(Constants.CONFIGURATION.VERSION, BuildConfig.VERSION_NAME)
            .header(Constants.CONFIGURATION.UDID, id)
            .header(Constants.CONFIGURATION.DEVICE, Constants.CONFIGURATION.DEVICE_ANDROID)
            .header(Constants.CONFIGURATION.USER_AGENT, "SmallWorld/${BuildConfig.VERSION_NAME}")
            .header("device", Constants.CONFIGURATION.DEVICE_ANDROID)
            .build()

        return chain.proceed(request)
    }
}
