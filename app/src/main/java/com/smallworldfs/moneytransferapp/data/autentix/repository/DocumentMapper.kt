package com.smallworldfs.moneytransferapp.data.autentix.repository

import com.smallworldfs.moneytransferapp.data.autentix.local.model.DocumentFileEntity
import java.io.File
import javax.inject.Inject

class DocumentMapper @Inject constructor() {
    fun mapDocumentFile(file: File, uri: String) = DocumentFileEntity(file, uri)
}
