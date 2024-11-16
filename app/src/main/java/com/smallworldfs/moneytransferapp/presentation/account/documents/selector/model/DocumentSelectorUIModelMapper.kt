package com.smallworldfs.moneytransferapp.presentation.account.documents.selector.model

import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.DocumentUIModel
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.TypesOfDocumentUIModel
import javax.inject.Inject

class DocumentSelectorUIModelMapper @Inject constructor() {

    fun map(document: DocumentUIModel, type: TypesOfDocumentUIModel) =
        DocumentSelectorUIModel(
            type,
            document
        )
}
