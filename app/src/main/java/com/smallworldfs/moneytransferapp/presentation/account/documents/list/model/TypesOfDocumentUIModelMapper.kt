package com.smallworldfs.moneytransferapp.presentation.account.documents.list.model

import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.TypesOfDocumentsDTO
import com.smallworldfs.moneytransferapp.utils.KeyValueModel
import javax.inject.Inject

class TypesOfDocumentUIModelMapper @Inject constructor() {

    fun map(list: List<TypesOfDocumentsDTO>): List<TypesOfDocumentUIModel> {
        val mappedList = mutableListOf<TypesOfDocumentUIModel>()
        list.forEach {
            mappedList.add(mapTypesOfDocumentsDTO(it))
        }
        return mappedList
    }

    fun mapToKeyValue(list: List<TypesOfDocumentsDTO>): List<KeyValueModel> {
        val mappedList = mutableListOf<KeyValueModel>()
        list.forEach {
            mappedList.add(
                KeyValueModel(it.id, it.name)
            )
        }
        return mappedList
    }

    private fun mapTypesOfDocumentsDTO(typesOfDocumentsDTO: TypesOfDocumentsDTO) =
        TypesOfDocumentUIModel(
            typesOfDocumentsDTO.id,
            typesOfDocumentsDTO.name,
        )
}
