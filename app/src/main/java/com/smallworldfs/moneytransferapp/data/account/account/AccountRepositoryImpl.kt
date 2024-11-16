package com.smallworldfs.moneytransferapp.data.account.account

import com.smallworldfs.moneytransferapp.data.account.account.mappers.AccountMenuResponseMapper
import com.smallworldfs.moneytransferapp.data.account.account.network.AccountNetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.domain.migrated.account.AccountMenuDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.AccountRepository
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.peek
import com.smallworldfs.moneytransferapp.domain.migrated.base.peekFailure
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import javax.inject.Inject

class AccountRepositoryImpl @Inject constructor(
    private val accountNetworkDatasource: AccountNetworkDatasource,
    private val accountMenuResponseMapper: AccountMenuResponseMapper,
    private val apiErrorMapper: APIErrorMapper,
    private val userDataRepository: UserDataRepository,
) : AccountRepository {

    override fun getAccountMenu(): OperationResult<AccountMenuDTO, Error> {
        userDataRepository.getLoggedUser()
            .peek { user ->
                accountNetworkDatasource.getAccountMenu(
                    user.userToken,
                    user.id
                ).map {
                    return Success(accountMenuResponseMapper.map(it, user.isLimited()))
                }.mapFailure {
                    return Failure(apiErrorMapper.map(it))
                }
            }.peekFailure {
                return Failure(it)
            }
        return Failure(Error.UncompletedOperation("Could't retrieve account menu"))
    }
}
