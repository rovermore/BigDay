package com.smallworldfs.moneytransferapp.data.base.network

import retrofit2.Call
import retrofit2.Callback
import retrofit2.HttpException
import retrofit2.Response

class CallWithErrorHandling(
    private val delegate: Call<Any>
) : Call<Any> by delegate {

    override fun enqueue(callback: Callback<Any>) {
        delegate.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                if (response.isSuccessful) {
                    callback.onResponse(call, response)
                } else {
                    callback.onFailure(call, mapToApiException(HttpException(response)))
                }
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                callback.onFailure(call, mapToApiException(t))
            }
        })
    }

    override fun clone() = CallWithErrorHandling(delegate.clone())

    fun mapToApiException(remoteException: Throwable) = when (remoteException) {
        is HttpException -> when (remoteException.code()) {
            503 -> NetworkException.ServiceUnavailable(remoteException.message() ?: "")
            500 -> NetworkException.InternalServerError(remoteException.message() ?: "")
            422 -> NetworkException.UnprocessableEntity(remoteException.message() ?: "")
            404 -> NetworkException.NotFound(remoteException.message() ?: "")

            else -> NetworkException.UnmappedException(remoteException.code(), remoteException.message() ?: "")
        }
        else -> NetworkException.InternalServerError(remoteException.localizedMessage ?: "")
    }
}
