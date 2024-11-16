package com.smallworldfs.moneytransferapp.presentation.common.countries

import com.smallworldfs.moneytransferapp.domain.migrated.addess.model.AddressDTO
import javax.inject.Inject

class AddressUIMapper @Inject constructor() {

    fun mapList(listAddressDTO: List<AddressDTO>): List<AddressUIModel> {
        val listAddressUIModel = mutableListOf<AddressUIModel>()
        listAddressDTO.forEach {
            listAddressUIModel.add(
                AddressUIModel(
                    id = it.id,
                    type = it.type,
                    detail = AddressUIModel.Detail(
                        text = it.detail.text,
                        description = it.detail.description
                    )
                )
            )
        }
        return listAddressUIModel.toList()
    }
}
