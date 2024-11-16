package com.smallworldfs.moneytransferapp.data.address.model

import com.smallworldfs.moneytransferapp.domain.migrated.addess.model.AddressDTO
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import javax.inject.Inject

class AddressMapper @Inject constructor() {

    fun map(addressResponse: AddressResponse): OperationResult<List<AddressDTO>, Error> {
        val listAddressDTO = mutableListOf<AddressDTO>()
        addressResponse.data.forEach {
            listAddressDTO.add(
                AddressDTO(
                    id = it.id,
                    type = it.type,
                    detail = AddressDTO.Detail(
                        text = it.detail?.text ?: STRING_EMPTY,
                        description = it.detail?.description ?: STRING_EMPTY
                    )
                )
            )
        }
        return Success(listAddressDTO.toList())
    }
}
