package com.smallworldfs.moneytransferapp.data.base.network.interceptors

import android.content.Context
import com.chuckerteam.chucker.api.ChuckerCollector
import com.chuckerteam.chucker.api.ChuckerInterceptor
import okhttp3.Interceptor
import javax.inject.Inject

class RequestLogInterceptor @Inject constructor(context: Context) : Interceptor {

    private val chuckerInterceptor = ChuckerInterceptor.Builder(context)
        .collector(ChuckerCollector(context))
        .maxContentLength(250000L)
        .redactHeaders(emptySet())
        .alwaysReadResponseBody(false)
        .build()

    override fun intercept(chain: Interceptor.Chain) = chuckerInterceptor.intercept(chain)
}
