package com.smallworldfs.moneytransferapp.data.splash

import com.smallworldfs.moneytransferapp.data.splash.model.ResponseMinVersionDataModel
import com.smallworldfs.moneytransferapp.domain.migrated.splash.repository.SplashRepository
import javax.inject.Inject

class SplashRepositoryMockImpl @Inject constructor() : SplashRepository {

    override suspend fun requestMinVersionAsync(): ResponseMinVersionDataModel {
        return ResponseMinVersionDataModel("success", "2020121801", "3.3.6", "3.3.6.0")
    }

}