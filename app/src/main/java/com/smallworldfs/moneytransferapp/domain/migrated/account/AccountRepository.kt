package com.smallworldfs.moneytransferapp.domain.migrated.account

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult

interface AccountRepository {
    fun getAccountMenu(): OperationResult<AccountMenuDTO, Error>
}
