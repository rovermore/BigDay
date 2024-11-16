package com.smallworldfs.moneytransferapp.data.base.network.interceptors

import com.smallworldfs.moneytransferapp.data.userdata.local.UserDataLocalDatasource
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.utils.Constants
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class UserIdInterceptor @Inject constructor(
    private val userDataLocalDatasource: UserDataLocalDatasource
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        userDataLocalDatasource.retrieveUser()
            .map {
                request = request.newBuilder().header(Constants.CONFIGURATION.USER_ID, it.id).build()
            }
        return chain.proceed(request)
    }
}
