package com.smallworldfs.moneytransferapp.modules.changepassword.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.gson.GsonBuilder
import com.smallworldfs.moneytransferapp.base.data.net.api.ApiErrorResponse
import com.smallworldfs.moneytransferapp.base.data.net.api.ApiErrors.Companion.GENERIC_ERROR
import com.smallworldfs.moneytransferapp.base.data.net.api.ApiErrors.Companion.UNAUTHORIZED_ERROR
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.BaseViewModel
import com.smallworldfs.moneytransferapp.modules.changepassword.ui.ChangePasswordNavigator
import com.smallworldfs.moneytransferapp.modules.changepassword.usecase.ChangePasswordChangePasswordUseCaseContract
import com.smallworldfs.moneytransferapp.modules.changepassword.usecase.ChangePasswordClearPasswordFieldsUseCaseContract
import com.smallworldfs.moneytransferapp.modules.changepassword.usecase.ChangePasswordCreateFormUseCaseContract
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import io.reactivex.android.schedulers.AndroidSchedulers
import retrofit2.HttpException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.nio.charset.Charset
import javax.inject.Inject

class ChangePasswordViewModel @Inject constructor(
    private val changePasswordCreateFormUseCaseContract: ChangePasswordCreateFormUseCaseContract,
    private val changePasswordClearPasswordFieldsUseCaseContract: ChangePasswordClearPasswordFieldsUseCaseContract,
    private val changePasswordChangePasswordUseCaseContract: ChangePasswordChangePasswordUseCaseContract
) : BaseViewModel<ChangePasswordNavigator>(), ChangePasswordViewModelContract {

    private var form = mutableListOf<Field>()

    private val changePasswordState = MutableLiveData<ChangePasswordState>()
    fun getChangePasswordState(): LiveData<ChangePasswordState> = changePasswordState

    override fun initViewModel() {
        changePasswordState.postValue(Loading)
        compositeDisposable.add(
            changePasswordCreateFormUseCaseContract.requestChangePasswordForm()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ form ->
                    this.form = form
                    changePasswordState.postValue(Loaded(ChangePasswordFormLoaded(ChangePasswordForm(form))))
                }, { error ->
                    when (error) {
                        is HttpException -> {
                            if (error.code() == UNAUTHORIZED_ERROR) {
                                changePasswordState.postValue(Error(SessionExpired))
                            } else {
                                changePasswordState.postValue(Error(NoConnection))
                            }
                        }
                        else -> changePasswordState.postValue(Error(NoConnection))
                    }
                })
        )
    }

    override fun clearPasswordFields() {
        compositeDisposable.add(
            changePasswordClearPasswordFieldsUseCaseContract.clearPasswordFields(form)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ form ->
                    this.form = form
                    changePasswordState.value = Loaded(ChangePasswordFormLoaded(ChangePasswordForm(form)))
                }, {
                    changePasswordState.value = Error(ChangePasswordCommonError)
                })
        )
    }

    override fun changePassword(parameters: HashMap<String, String>) {
        val password = parameters["password"] ?: ""
        val newPassword = parameters["newPassword"] ?: ""
        if (password.isNotEmpty() && newPassword.isNotEmpty()) {
            changePasswordState.postValue(Loading)
            compositeDisposable.add(
                changePasswordChangePasswordUseCaseContract.changePassword(password, newPassword)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        changePasswordState.postValue(Loaded(PasswordChanged))
                    }, { error ->
                        when (error) {
                            is UnknownHostException,
                            is ConnectException,
                            is SocketTimeoutException -> changePasswordState.value = Error(NoConnection)
                            is HttpException -> {
                                if (error.code() == UNAUTHORIZED_ERROR) {
                                    changePasswordState.postValue(Error(SessionExpired))
                                } else {
                                    try {
                                        val responseBody = error.response()?.errorBody()
                                        val source = responseBody?.source()
                                        source?.request(java.lang.Long.MAX_VALUE)
                                        val buffer = source?.buffer
                                        val responseBodyString = buffer?.clone()?.readString(Charset.forName("UTF-8"))

                                        val response = GsonBuilder().create()
                                            .fromJson<ApiErrorResponse>(responseBodyString, ApiErrorResponse::class.java)
                                        changePasswordState.value = Error(
                                            PasswordNotChanged(
                                                response.title
                                                    ?: "",
                                                response.text ?: ""
                                            )
                                        )
                                    } catch (exception: Exception) {
                                        changePasswordState.value = Error(
                                            if (error.code() == GENERIC_ERROR) {
                                                ChangePasswordCommonError
                                            } else {
                                                PasswordNotChanged("", "")
                                            }
                                        )
                                    }
                                }
                            }
                            else -> changePasswordState.value = Error(ChangePasswordCommonError)
                        }
                        clearPasswordFields()
                    })
            )
        }
    }
}

interface ChangePasswordViewModelContract {
    fun initViewModel()
    fun clearPasswordFields()
    fun changePassword(parameters: HashMap<String, String>)
}
