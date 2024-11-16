package com.smallworldfs.moneytransferapp.data.base.network

import com.smallworldfs.moneytransferapp.BuildConfig
import com.smallworldfs.moneytransferapp.api.Api.TIMEOUT
import com.smallworldfs.moneytransferapp.base.data.exceptions.NoInternetException
import com.smallworldfs.moneytransferapp.base.data.manager.NetworkManager
import com.smallworldfs.moneytransferapp.data.base.network.interceptors.DeviceInfoInterceptor
import com.smallworldfs.moneytransferapp.data.base.network.interceptors.RequestLogInterceptor
import com.smallworldfs.moneytransferapp.data.base.network.interceptors.RiskifiedInterceptor
import com.smallworldfs.moneytransferapp.data.base.network.interceptors.TokenInterceptor
import com.smallworldfs.moneytransferapp.utils.Constants
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Qualifier

class SWOkHttpClient @Inject constructor(
    private val tokenInterceptor: TokenInterceptor,
    private val requestLogInterceptor: RequestLogInterceptor,
    private val riskifiedInterceptor: RiskifiedInterceptor,
    private val deviceInfoInterceptor: DeviceInfoInterceptor,
    private val networkManager: NetworkManager
) {

    fun createOkHttpClient(versionInterceptor: Interceptor): OkHttpClient {
        val client = OkHttpClient.Builder()

        client.apply {
            if (Constants.TESTING.RETROFIT_LOG && BuildConfig.DEBUG) {
                addInterceptor(
                    HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.HEADERS
                        level = HttpLoggingInterceptor.Level.BODY
                    },
                )
            }

            addInterceptor(createNoInternetException())

            addInterceptor(tokenInterceptor)
            addInterceptor(riskifiedInterceptor)
            addInterceptor(deviceInfoInterceptor)
            addInterceptor(versionInterceptor)
            addInterceptor(requestLogInterceptor)

            connectTimeout(TIMEOUT, TimeUnit.SECONDS)
            readTimeout(TIMEOUT, TimeUnit.SECONDS)
            writeTimeout(TIMEOUT, TimeUnit.SECONDS)
        }

        return client.build()
    }

    private fun createNoInternetException(): Interceptor {
        return Interceptor {
            if (!networkManager.isNetworkAvailable()) {
                throw NoInternetException()
            }

            it.proceed(it.request())
        }
    }
}

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitV3

@Qualifier
@Retention(AnnotationRetention.BINARY)
annotation class RetrofitV4
