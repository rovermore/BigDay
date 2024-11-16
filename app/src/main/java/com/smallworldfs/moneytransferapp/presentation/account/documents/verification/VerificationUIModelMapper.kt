package com.smallworldfs.moneytransferapp.presentation.account.documents.verification

import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.DocumentDTO
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.DocumentUIModelMapper
import javax.inject.Inject

class VerificationUIModelMapper @Inject constructor(
    private val documentUIModelMapper: DocumentUIModelMapper
) {

    fun map(documents: List<DocumentDTO>) = documentUIModelMapper.map(documents)
}
