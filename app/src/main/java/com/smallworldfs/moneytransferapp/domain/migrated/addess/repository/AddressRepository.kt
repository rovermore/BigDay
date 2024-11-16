package com.smallworldfs.moneytransferapp.domain.migrated.addess.repository

import com.smallworldfs.moneytransferapp.domain.migrated.addess.model.AddressDTO
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field

interface AddressRepository {
    fun searchAddressByText(text: String): OperationResult<List<AddressDTO>, Error>
    fun searchAddressByParentId(parentId: String): OperationResult<List<AddressDTO>, Error>
    fun getAddressById(id: String): OperationResult<List<Field>, Error>
    fun getAddressForm(): OperationResult<List<Field>, Error>
}
