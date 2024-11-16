package com.smallworldfs.moneytransferapp.data.base.network

import com.smallworldfs.moneytransferapp.data.common.locale.LocaleRepository
import com.smallworldfs.moneytransferapp.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LangInterceptor @Inject constructor(
    private val localeRepository: LocaleRepository
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()
        val url = request.url.newBuilder()
            .addQueryParameter(Constants.CONFIGURATION.PARAM_LANGUAGE, localeRepository.getLang()).build()
        request = request.newBuilder().url(url).build()

        return chain.proceed(request)
    }
}
