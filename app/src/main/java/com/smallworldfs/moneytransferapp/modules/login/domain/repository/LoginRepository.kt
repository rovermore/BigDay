package com.smallworldfs.moneytransferapp.modules.login.domain.repository

import androidx.core.util.Pair
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.modules.login.domain.model.LoginUserServer
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User
import com.smallworldfs.moneytransferapp.modules.login.domain.model.server.LogoutRequest
import com.smallworldfs.moneytransferapp.modules.login.domain.model.server.ServerLoginRequest
import com.smallworldfs.moneytransferapp.modules.login.domain.service.LoginNetworkDatasource
import retrofit2.Response
import rx.Observable
import javax.inject.Inject

/**
 * Created by luismiguel on 5/5/17
 */
class LoginRepository @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val loginNetworkDatasource: LoginNetworkDatasource
) {

    /**
     * Persist origin countries to perform login
     *
     * @param originCountry
     */
    // Login Origin Country
    var originLoginCountry: Pair<String, String>? = null

    fun login(request: ServerLoginRequest?): Observable<Response<LoginUserServer>> =
        loginNetworkDatasource.login(request)

    fun logout(request: LogoutRequest?): Observable<Response<Void>> =
        loginNetworkDatasource.logout(request)

    val user: User?
        get() = userDataRepository.retrieveUser()

    /**
     * Return current user token
     * @return
     */
    val userToken: String
        get() {
            val user = user
            return if (user != null) {
                user.userToken
            } else ""
        }

    /**
     * Return current user id
     * @return
     */
    val userId: String
        get() {
            val user = user
            return if (user != null) {
                user.id
            } else ""
        }
}
