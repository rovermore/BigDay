package com.smallworldfs.moneytransferapp.domain.migrated.oauth.repository

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.operations.model.IntegrityDTO

interface OAuthRepository {
    fun requestOAuthTokenAsync(integrityDTO: IntegrityDTO): OperationResult<String, Error>
    fun refreshOAuthTokenAsync(integrityDTO: IntegrityDTO): OperationResult<String, Error>
}
