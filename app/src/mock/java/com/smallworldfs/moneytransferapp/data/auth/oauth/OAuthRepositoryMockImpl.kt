package com.smallworldfs.moneytransferapp.data.auth.oauth

import com.smallworldfs.moneytransferapp.domain.migrated.oauth.repository.OAuthRepository
import javax.inject.Inject

class OAuthRepositoryMockImpl @Inject constructor() : OAuthRepository {

    override suspend fun requestOAuthTokenAsync(): String {
        return "Bearer token mocked"
    }

}