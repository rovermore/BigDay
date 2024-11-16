package com.smallworldfs.moneytransferapp.presentation.account.documents.list.model

import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.DocumentDTO
import javax.inject.Inject

class MyDocumentsUIModelMapper @Inject constructor(
    private val documentUIModelMapper: DocumentUIModelMapper,
) {

    private fun mapMyDocuments(documents: List<DocumentUIModel>): MyDocumentsUIModel {
        val uploaded = mutableListOf<DocumentUIModel>()
        val required = mutableListOf<DocumentUIModel>()

        documents.forEach { uiModel ->
            when (uiModel.status) {
                DocumentUIModel.DocumentStatus.MISSING -> required.add(uiModel)
                DocumentUIModel.DocumentStatus.REJECTED,
                DocumentUIModel.DocumentStatus.UNDER_REVIEW,
                DocumentUIModel.DocumentStatus.EXPIRED,
                DocumentUIModel.DocumentStatus.APPROVED -> uploaded.add(uiModel)
                else -> {}
            }
        }

        return MyDocumentsUIModel(required, uploaded)
    }

    fun map(documents: List<DocumentDTO>): MyDocumentsUIModel {
        return mapMyDocuments(documentUIModelMapper.map(documents))
    }

    fun mapSingleDocument(dto: DocumentDTO) = documentUIModelMapper.map(listOf(dto)).first()
}
