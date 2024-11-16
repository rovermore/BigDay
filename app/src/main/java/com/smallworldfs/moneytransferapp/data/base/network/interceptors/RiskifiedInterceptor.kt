package com.smallworldfs.moneytransferapp.data.base.network.interceptors

import com.smallworldfs.moneytransferapp.base.data.net.RiskifiedInstance
import com.smallworldfs.moneytransferapp.domain.usertoken.repository.local.UserTokenLocal
import com.smallworldfs.moneytransferapp.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RiskifiedInterceptor @Inject constructor(
    private val riskifiedInstance: RiskifiedInstance,
    private val userTokenLocal: UserTokenLocal
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()

        val params = request.url.queryParameterNames
        if (!params.contains(Constants.CONFIGURATION.PARAM_PASSWORD) && userTokenLocal.getUserToken().isNotEmpty()) {
            riskifiedInstance.logRequest(request.url.toString())
        }

        return chain.proceed(request)
    }
}
