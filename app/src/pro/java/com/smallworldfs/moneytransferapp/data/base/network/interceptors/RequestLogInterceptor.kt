package com.smallworldfs.moneytransferapp.data.base.network.interceptors

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class RequestLogInterceptor @Inject constructor(context: Context) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        return chain.proceed(request)
    }
}
