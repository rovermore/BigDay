package com.smallworldfs.moneytransferapp.domain.usertoken.repository.local

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserTokenLocal @Inject constructor() {

    companion object {
        private var userToken: String = ""
    }

    fun setUserToken(userToken: String?) {
        UserTokenLocal.userToken = userToken ?: ""
    }

    fun getUserToken(): String = userToken

    fun clearUserToken() {
        userToken = ""
    }
}
