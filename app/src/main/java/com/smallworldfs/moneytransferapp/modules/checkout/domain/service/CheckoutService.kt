package com.smallworldfs.moneytransferapp.modules.checkout.domain.service

import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.Checkout
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.CreateTransactionResponse
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.server.CheckoutRequest
import com.smallworldfs.moneytransferapp.modules.common.domain.api.EndPoint
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.QueryMap
import rx.Observable

interface CheckoutService {

    @POST(EndPoint.CHECKOUT_CONFIRM)
    fun getCheckoutInfo(@QueryMap request: CheckoutRequest?): Observable<Response<Checkout?>?>?

    @POST(EndPoint.CHECKOUT_CREATE)
    fun createTransaction(@QueryMap request: CheckoutRequest?): Observable<Response<CreateTransactionResponse?>?>?
}
