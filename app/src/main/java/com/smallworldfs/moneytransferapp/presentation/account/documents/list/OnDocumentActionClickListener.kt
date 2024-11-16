package com.smallworldfs.moneytransferapp.presentation.account.documents.list

import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.ComplianceDocUIModel
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.DocumentUIModel

interface OnDocumentActionClickListener {
    fun onValidateIdentity(document: ComplianceDocUIModel.FullValidationUIModel)
    fun onUploadDocument(document: DocumentUIModel)
    fun onDownloadDocument(document: DocumentUIModel)
    fun onNavigateToUrl(url: String?)
}
