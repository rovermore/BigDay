package com.smallworldfs.moneytransferapp.data.account.documents.mappers

import com.smallworldfs.moneytransferapp.data.account.documents.model.TypesOfDocumentsResponse
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.TypesOfDocumentsDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.Validations
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import javax.inject.Inject

class TypeOfDocumentsDTOMapper @Inject constructor() {

    fun map(requiredDocuments: List<TypesOfDocumentsResponse>): List<TypesOfDocumentsDTO> {
        val listOfTypesOfDocumentsDTO = mutableListOf<TypesOfDocumentsDTO>()
        requiredDocuments.forEach {
            listOfTypesOfDocumentsDTO.add(
                TypesOfDocumentsDTO(
                    id = it.document ?: STRING_EMPTY,
                    name = it.trans ?: STRING_EMPTY,
                    country = it.country ?: STRING_EMPTY,
                    validations = Validations(
                        it.frontend != INT_ZERO,
                        it.backend != INT_ZERO,
                    )
                ),
            )
        }
        return listOfTypesOfDocumentsDTO
    }
}
