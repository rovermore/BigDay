package com.smallworldfs.moneytransferapp.modules.login.domain.interactors.implementation

import android.os.Handler
import android.os.Looper
import android.text.TextUtils
import androidx.core.util.Pair
import com.smallworldfs.moneytransferapp.SmallWorldApplication.Companion.app
import com.smallworldfs.moneytransferapp.data.login.mappers.UserDTOMapper
import com.smallworldfs.moneytransferapp.data.login.mappers.UserMapperFromDTO
import com.smallworldfs.moneytransferapp.data.oauth.repository.local.OAuthLocal
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.repository.BeneficiaryRepository
import com.smallworldfs.moneytransferapp.modules.calculator.domain.interactor.implementation.CalculatorInteractorImpl
import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.AbstractInteractor
import com.smallworldfs.moneytransferapp.modules.customization.domain.repository.AppCustomizationRepository
import com.smallworldfs.moneytransferapp.modules.login.domain.interactors.LoginInteractor
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User
import com.smallworldfs.moneytransferapp.modules.login.domain.model.server.LogoutRequest
import com.smallworldfs.moneytransferapp.modules.login.domain.repository.LoginRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.components.SingletonComponent
import retrofit2.Response
import rx.Subscriber
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import rx.subscriptions.CompositeSubscription

/**
 * Created by luismiguel on 4/5/17
 */
class LoginInteractorImpl : AbstractInteractor(AndroidSchedulers.mainThread(), Schedulers.io()), LoginInteractor {
    private var mCompositeSubscriptions: CompositeSubscription? = null
    private var mHandler: Handler? = null
    private var logoutCallback: LoginInteractor.Callback? = null

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    internal interface DaggerHiltEntryPoint {
        fun bindUserDataRepository(): UserDataRepository
        fun provideUserMapperFromDTO(): UserMapperFromDTO
        fun provideUserDTOMapper(): UserDTOMapper
        fun provideLoginRepository(): LoginRepository
        fun providesNetworkOAuthLocal(): OAuthLocal
    }

    var userDataRepository: UserDataRepository
    var userMapperFromDTO: UserMapperFromDTO
    var userDTOMapper: UserDTOMapper
    var loginRepository: LoginRepository
    var oAuthLocal: OAuthLocal

    private fun init() {
        mHandler = Handler(Looper.getMainLooper())
        mCompositeSubscriptions = CompositeSubscription()
    }

    fun setLogoutCallback(logoutCallback: LoginInteractor.Callback) {
        this.logoutCallback = logoutCallback
    }

    override fun run() {}
    override fun removeCallbacks() {}
    override fun destroy() {
        if (mCompositeSubscriptions != null) {
            mCompositeSubscriptions!!.unsubscribe()
            mCompositeSubscriptions!!.clear()
            mCompositeSubscriptions = null
        }
    }

    fun logout(user: User) {
        if (!TextUtils.isEmpty(user.userToken) && !TextUtils.isEmpty(user.id)) {
            performLogout(user)
        }
    }

    interface OnDeletedDeviceCallback {
        fun onError()
        fun onDeletedDevice()
    }

    private fun performLogout(user: User) {
        // Perform POST to API
        val request = LogoutRequest(user.id, user.userToken)
        // Clear current user
        userDataRepository.clearUserData()
        // Clear calculator
        CalculatorInteractorImpl.getInstance().destroy()
        // Clear beneficiaries
        if (BeneficiaryRepository.getInstance().cachedBeneficiaries != null) BeneficiaryRepository.getInstance().cachedBeneficiaries.clear()
        logoutCallback?.onLogout()
        mCompositeSubscriptions!!.add(
            loginRepository
                .logout(request)
                .subscribeOn(mSubscriberOn)
                ?.observeOn(mObserveOn)
                ?.subscribe(
                    object : Subscriber<Response<Void>?>() {
                        override fun onCompleted() {
                        }

                        override fun onError(e: Throwable?) {
                        }

                        override fun onNext(t: Response<Void>?) {
                        }
                    },
                ),
        )
        oAuthLocal.clearPersistedOAuthToken()
    }

    val user: User
        get() = loginRepository.user!!

    /**
     * Get countries selected from login repository
     */
    val countrySelected: Pair<String, String>?
        get() = loginRepository.originLoginCountry

    /**
     * Set countries selected listener in global repository
     */
    fun setCountrySelected(country: Pair<String, String>) {
        AppCustomizationRepository.getInstance().countrySelected = country
    }

    companion object {
        // private static final String TAG = "LoginInteractorImpl";
        private var sInstance: LoginInteractorImpl? = null

        @JvmStatic
        val instance: LoginInteractorImpl
            get() {
                if (sInstance == null) {
                    sInstance = LoginInteractorImpl()
                    sInstance?.init()
                }
                return sInstance as LoginInteractorImpl
            }
    }

    init {
        val hiltEntryPoint = EntryPointAccessors.fromApplication(app, DaggerHiltEntryPoint::class.java)
        userDataRepository = hiltEntryPoint.bindUserDataRepository()
        userMapperFromDTO = hiltEntryPoint.provideUserMapperFromDTO()
        userDTOMapper = hiltEntryPoint.provideUserDTOMapper()
        loginRepository = hiltEntryPoint.provideLoginRepository()
        oAuthLocal = hiltEntryPoint.providesNetworkOAuthLocal()
    }
}
