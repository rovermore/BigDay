package com.smallworldfs.moneytransferapp.mocks.dto

import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.TypesOfDocumentsDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.Validations

object TypesOfDocumentsDTOMock {

    private val typesOfDocumentsDTO = TypesOfDocumentsDTO(
        "id",
        "name",
        "country",
        Validations(front = false, back = false)
    )

    val typesOfDocumentsDTOList = listOf(typesOfDocumentsDTO)
}
