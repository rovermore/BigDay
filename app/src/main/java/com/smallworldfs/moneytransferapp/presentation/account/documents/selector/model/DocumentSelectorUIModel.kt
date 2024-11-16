package com.smallworldfs.moneytransferapp.presentation.account.documents.selector.model

import android.os.Parcelable
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.DocumentUIModel
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.TypesOfDocumentUIModel
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DocumentSelectorUIModel(
    val documentType: TypesOfDocumentUIModel = TypesOfDocumentUIModel(),
    val document: DocumentUIModel = DocumentUIModel()
) : Parcelable
