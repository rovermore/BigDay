package com.smallworldfs.moneytransferapp.data.base.network.interceptors

import android.content.Context
import com.google.android.play.core.integrity.IntegrityManagerFactory
import com.smallworldfs.moneytransferapp.api.Api
import com.smallworldfs.moneytransferapp.base.data.net.api.CoroutineCallAdapterFactoryNullSupport
import com.smallworldfs.moneytransferapp.data.auth.oauth.model.Integrity
import com.smallworldfs.moneytransferapp.data.auth.oauth.model.RequestInfo
import com.smallworldfs.moneytransferapp.data.auth.oauth.model.RequestOAuthTokenDataModel
import com.smallworldfs.moneytransferapp.data.integrity.network.IntegrityNetworkDatasource
import com.smallworldfs.moneytransferapp.data.integrity.network.IntegrityService
import com.smallworldfs.moneytransferapp.data.oauth.repository.local.OAuthLocal
import com.smallworldfs.moneytransferapp.data.oauth.repository.network.OAuthService
import com.smallworldfs.moneytransferapp.domain.migrated.base.get
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TokenInterceptor @Inject constructor(
    private val context: Context,
    private val oAuthLocal: OAuthLocal
) : Interceptor {

    companion object {
        private const val OAUTH = "oauth"
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val token = oAuthLocal.getPersistedOAuthToken().ifEmpty {
            requestToken()
        }

        return with(chain) {
            proceed(
                request().newBuilder().apply {
                    header(Constants.CONFIGURATION.AUTHORIZATION_KEY, token)
                }.build(),
            )
        }
    }

    private fun requestToken(): String {
        val retrofitClient = getRetrofitClient()
        val service = retrofitClient.create(OAuthService::class.java)
        val oAuthRequest = RequestOAuthTokenDataModel(getIntegrityData())
        val refreshTokenRequest = service.refreshAccessToken(oAuthRequest)
        val token = refreshTokenRequest.execute().body()
        val oauthToken = token?.getOAuthToken() ?: STRING_EMPTY
        oAuthLocal.persistOAuthToken(oauthToken)
        return oauthToken
    }

    private fun getRetrofitClient(): Retrofit {
        val client = OkHttpClient.Builder().addInterceptor(DeviceInfoInterceptor(context)).build()
        return Retrofit.Builder().apply {
            baseUrl(Api.BASE_URL)
            addConverterFactory(ScalarsConverterFactory.create())
            addConverterFactory(GsonConverterFactory.create())
            addCallAdapterFactory(CoroutineCallAdapterFactoryNullSupport.invoke())
            client(client)
        }.build()
    }
    private fun getIntegrityData(): Integrity {
        val retrofitClient = getRetrofitClient()
        val integrityService = retrofitClient.create(IntegrityService::class.java)
        val integrityNetworkDatasource = IntegrityNetworkDatasource(
            integrityService,
            IntegrityManagerFactory.create(context),
        )
        return integrityNetworkDatasource.getIntegrity(OAUTH)
            .map {
                Integrity(
                    it.data.nonce,
                    RequestInfo(
                        it.signature
                    )
                )
            }.mapFailure {
                Integrity(
                    STRING_EMPTY,
                    RequestInfo(
                        STRING_EMPTY
                    )
                )
            }.get()
    }
}
