package com.smallworldfs.moneytransferapp.base.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.smallworldfs.moneytransferapp.base.data.net.RiskifiedInstance
import com.smallworldfs.moneytransferapp.base.presentation.navigator.BaseNavigator
import com.smallworldfs.moneytransferapp.base.presentation.ui.RootChecker
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.AnalyticsSender
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.domain.usertoken.repository.local.UserTokenLocal
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import io.reactivex.disposables.CompositeDisposable
import javax.inject.Inject

abstract class BaseViewModel<N : BaseNavigator> : ViewModel() {

    protected val state = MutableLiveData<State>()
    protected val compositeDisposable = CompositeDisposable()

    @Inject lateinit var rootChecker: RootChecker

    @Inject lateinit var userDataRepository: UserDataRepository

    @Inject lateinit var analyticsSender: AnalyticsSender

    @Inject lateinit var navigator: N

    @Inject lateinit var riskifiedInstance: RiskifiedInstance

    @Inject lateinit var userTokenLocal: UserTokenLocal

    fun getState(): LiveData<State> = state

    fun initNavigator(n: N) {
        navigator = n
    }

    fun initUserDataRepository(repository: UserDataRepository) {
        userDataRepository = repository
    }

    fun initRiskified() {
        if (userTokenLocal.getUserToken().isNotEmpty()) {
            riskifiedInstance.startBeacon(userTokenLocal.getUserToken())
        }
    }

    fun logRequest(request: String) = riskifiedInstance.logRequest(request)

    fun releaseRiskified() = riskifiedInstance.removeLocUpdates()

    fun resumeViewModel() {
        if (rootChecker.checkRoot()) {
            compositeDisposable.clear()
            state.postValue(State.Error(DataError(STRING_EMPTY, STRING_EMPTY, Error.DEVICE_ROOTED, ErrorType.POPUP)))
        }
    }

    fun clearUser() {
        userTokenLocal.clearUserToken()
        userDataRepository.clearUserData()
    }

    fun clearUserToken() = userTokenLocal.clearUserToken()

    override fun onCleared() {
        if (!compositeDisposable.isDisposed) {
            compositeDisposable.dispose()
        }
        super.onCleared()
    }

    /**
     * Firebase Analytics
     * By default track screen searching it on the array of screens
     *
     * It is possible to override this method and set a custom behaviour
     */
}
