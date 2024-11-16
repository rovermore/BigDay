package com.smallworldfs.moneytransferapp.data.base.network

import com.google.gson.GsonBuilder
import com.smallworldfs.moneytransferapp.api.Api
import com.smallworldfs.moneytransferapp.base.data.net.api.CoroutineCallAdapterFactoryNullSupport
import okhttp3.Interceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import rx.schedulers.Schedulers
import javax.inject.Inject

class RetrofitInstance @Inject constructor(private val swOkHttpClient: SWOkHttpClient) {

    fun get(versionInterceptor: Interceptor): Retrofit {
        return Retrofit.Builder()
            .baseUrl(Api.BASE_URL)
            .client(swOkHttpClient.createOkHttpClient(versionInterceptor))
            .addConverterFactory(provideGson())
            .addConverterFactory(ScalarsConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactoryNullSupport.invoke())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addCallAdapterFactory(RxJavaCallAdapterFactory.createWithScheduler(Schedulers.io()))
            .addCallAdapterFactory(ErrorsCallAdapterFactory())
            .build()
    }

    private fun provideGson(): GsonConverterFactory = GsonConverterFactory.create(GsonBuilder().create())
}
