package com.smallworldfs.moneytransferapp.data.base.network

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import com.smallworldfs.moneytransferapp.SmallWorldApplication.Companion.app
import com.smallworldfs.moneytransferapp.data.oauth.repository.local.OAuthLocal
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.base.network.models.ErrorResponse
import com.smallworldfs.moneytransferapp.data.base.network.models.ExpiredSessionErrorResponse
import com.smallworldfs.moneytransferapp.data.base.network.models.ValidationErrorResponse
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.presentation.common.session.SessionHandler
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors.fromApplication
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.HttpException
import retrofit2.Response
import rx.Observable
import rx.Single
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import javax.inject.Inject

open class NetworkDatasource @Inject constructor() {

    var oAuthLocal: OAuthLocal
    var sessionHandler: SessionHandler

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    internal interface DaggerHiltEntryPoint {
        fun providesNetworkOAuthLocal(): OAuthLocal
        fun providesSessionHandler(): SessionHandler
    }

    companion object {
        private const val UNAUTHORIZED_CODE = 403
        private const val LOGIN_TIMEOUT_CODE = 440
        private const val UNAUTHORIZED_MSG = "unauthorized"
    }

    init {
        val hiltEntryPoint = fromApplication(app, DaggerHiltEntryPoint::class.java)
        oAuthLocal = hiltEntryPoint.providesNetworkOAuthLocal()
        sessionHandler = hiltEntryPoint.providesSessionHandler()
    }

    fun <T> executeCall(call: Call<T>): OperationResult<T, APIError> {
        return try {
            val response = call.execute()
            if (response.isSuccessful && response.body() != null) {
                Success(response.body()!!)
            } else {
                Failure(checkUnauthorizedError(response))
            }
        } catch (e: NetworkException) {
            Failure(mapAPIError(e))
        } catch (e: Exception) {
            Failure(APIError.UnmappedError(-1, ""))
        }
    }

    fun <T> executeCall(request: Observable<Response<T>?>?): Observable<Response<T>> {
        return Observable.create { subscriber ->
            if (request != null) {
                request
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        object : Subscriber<Response<T>?>() {
                            override fun onCompleted() {}

                            override fun onError(e: Throwable?) {
                                subscriber.onNext(null)
                            }

                            override fun onNext(response: Response<T>?) {
                                if (response != null) {
                                    if (response.isSuccessful) {
                                        subscriber.onNext(response)
                                    } else {
                                        checkUnauthorizedError(response)
                                        subscriber.onError(Throwable(response.message()))
                                    }
                                    subscriber.onCompleted()
                                } else {
                                    subscriber.onError(null)
                                }
                            }
                        },
                    )
            } else {
                subscriber.onNext(null)
            }
        }
    }

    fun <T> executeCall(request: Single<Response<T>?>?): Single<Response<T>> {
        return Single.create { subscriber ->
            if (request != null) {
                request
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                        object : Subscriber<Response<T>?>() {
                            override fun onCompleted() {}

                            override fun onError(e: Throwable?) {
                                subscriber.onError(e)
                            }

                            override fun onNext(response: Response<T>?) {
                                if (response != null) {
                                    if (response.isSuccessful) {
                                        subscriber.onSuccess(response)
                                    } else {
                                        checkUnauthorizedError(response)
                                        subscriber.onError(Throwable(response.message()))
                                    }
                                } else {
                                    subscriber.onError(null)
                                }
                            }
                        },
                    )
            } else {
                subscriber.onError(null)
            }
        }
    }

    suspend fun <T> executeCall(request: Deferred<T>): T? {
        return try {
            request.await()
        } catch (e: Throwable) {
            val exception = e as? HttpException
            if (exception?.code() == UNAUTHORIZED_CODE || exception?.code() == LOGIN_TIMEOUT_CODE) {
                oAuthLocal.clearPersistedOAuthToken()
                sessionHandler.sendSessionExpiredEvent()
            }
            request.await()
        }
    }

    private fun <T> checkUnauthorizedError(response: Response<T>): APIError {
        val errorBody: String = response.errorBody()?.string() ?: STRING_EMPTY
        val mappedError = mapAPIError(
            mapNetworkExceptions(
                response.code(),
                errorBody,
            ),
        )

        if ((mappedError.code == UNAUTHORIZED_CODE && mappedError.message == UNAUTHORIZED_MSG) ||
            mappedError is APIError.LoginTimeout
        ) {
            oAuthLocal.clearPersistedOAuthToken()
            sessionHandler.sendSessionExpiredEvent()
        }

        return mappedError
    }

    fun mapAPIError(exception: NetworkException): APIError = when (exception) {
        is NetworkException.InternalServerError -> {
            val errorResponse = try {
                Gson().fromJson(exception.message, ErrorResponse::class.java)
            } catch (exception: JsonSyntaxException) {
                ErrorResponse()
            }
            APIError.InternalServerError(errorResponse.error.message)
        }
        is NetworkException.ServiceUnavailable -> {
            APIError.ServiceUnavailable(exception.localizedMessage ?: "")
        }
        is NetworkException.UnprocessableEntity -> {
            val errorResponse = try {
                var unprocessableError = Gson().fromJson(exception.message, ValidationErrorResponse::class.java)
                if (unprocessableError.validation.fields.isEmpty() && unprocessableError.validation.exceptionMessage.isEmpty()) {
                    val type = object : TypeToken<Map<String, Map<String, List<String>>>>() {}.type
                    val map = Gson().fromJson<Map<String, Map<String, List<String>>>>(exception.message, type)
                    val fieldList = mutableListOf<ValidationErrorResponse.Field>()
                    map.getValue("msg").forEach { (key, value) ->
                        fieldList.add(
                            ValidationErrorResponse.Field(
                                key,
                                listOf(value.first())
                            )
                        )
                    }
                    unprocessableError = ValidationErrorResponse(
                        ValidationErrorResponse.Validation(
                            fieldList,
                            fieldList.first().errors.first()
                        )
                    )
                }
                unprocessableError
            } catch (exception: JsonSyntaxException) {
                ValidationErrorResponse()
            }
            APIError.UnprocessableEntity(exception.message ?: "", errorResponse ?: ValidationErrorResponse())
        }
        is NetworkException.NotFound -> {
            val errorResponse = try {
                Gson().fromJson(exception.message, ErrorResponse::class.java)
            } catch (exception: Exception) {
                ErrorResponse()
            }
            APIError.NotFound(errorResponse)
        }
        is NetworkException.Forbidden -> {
            val errorResponse = try {
                var forbiddenError = Gson().fromJson(exception.message, ErrorResponse::class.java)
                with(forbiddenError.error) {
                    if (exceptionMessage.isEmpty() && message.isEmpty() && subCode.isEmpty()) {
                        val unauthorizedError = Gson().fromJson(exception.message, ExpiredSessionErrorResponse::class.java)
                        forbiddenError = ErrorResponse(
                            ErrorResponse.Error(
                                message = unauthorizedError.msg,
                            ),
                        )
                    }
                }
                forbiddenError
            } catch (exception: Exception) {
                ErrorResponse(
                    ErrorResponse.Error(
                        exception.message ?: STRING_EMPTY,
                    ),
                )
            }
            APIError.Forbidden(errorResponse)
        }
        is NetworkException.LoginTimeout -> {
            val errorResponse = try {
                Gson().fromJson(exception.message, ErrorResponse::class.java)
            } catch (exception: Exception) {
                ErrorResponse(
                    ErrorResponse.Error(
                        exception.message ?: STRING_EMPTY,
                    ),
                )
            }
            APIError.LoginTimeout(errorResponse)
        }
        is NetworkException.BadRequest -> {
            val errorResponse = try {
                Gson().fromJson(exception.message, ErrorResponse::class.java)
            } catch (exception: Exception) {
                ErrorResponse()
            }
            APIError.BadRequest(errorResponse)
        }
        is NetworkException.Unauthorized -> {
            val errorResponse = try {
                Gson().fromJson(exception.message, ErrorResponse::class.java)
            } catch (exception: Exception) {
                ErrorResponse()
            }
            APIError.Unauthorized(errorResponse)
        }
        is NetworkException.TimeOut -> {
            APIError.TimeOut(exception.localizedMessage ?: STRING_EMPTY)
        }

        else -> {
            APIError.UnmappedError(exception.code, exception.localizedMessage ?: STRING_EMPTY)
        }
    }

    fun mapNetworkExceptions(code: Int, message: String?): NetworkException =
        when (code) {
            500 -> NetworkException.InternalServerError(message)
            503 -> NetworkException.ServiceUnavailable(message)
            422 -> NetworkException.UnprocessableEntity(message)
            404 -> NetworkException.NotFound(message)
            408 -> NetworkException.TimeOut(message)
            400 -> NetworkException.BadRequest(message)
            401 -> NetworkException.Unauthorized(message)
            403 -> NetworkException.Forbidden(message)
            440 -> NetworkException.LoginTimeout(message)

            else -> NetworkException.UnmappedException(code, message)
        }
}
