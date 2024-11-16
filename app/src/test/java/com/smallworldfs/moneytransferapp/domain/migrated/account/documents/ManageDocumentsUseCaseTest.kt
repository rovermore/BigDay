package com.smallworldfs.moneytransferapp.domain.migrated.account.documents

import com.smallworldfs.moneytransferapp.data.common.locale.LocaleRepository
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.repository.DocumentsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.usecase.ManageDocumentsUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.mocks.FileMock
import com.smallworldfs.moneytransferapp.mocks.dto.DocumentDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.DocumentFileDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.TypesOfDocumentsDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class ManageDocumentsUseCaseTest {

    @Mock
    lateinit var documentsRepository: DocumentsRepository

    @Mock
    lateinit var userDataRepository: UserDataRepository

    @Mock
    lateinit var localeRepository: LocaleRepository

    lateinit var manageDocumentsUseCase: ManageDocumentsUseCase

    private val user = UserDTOMock.userDTO
    private val userSuccess = Success(user)
    private val error = Failure(Error.UncompletedOperation("Operation could not be completed"))
    private val documentFileDTO = DocumentFileDTOMock.documentFileDTO
    private val documentId = "1"
    private val type = emptyList<String>()
    private val subtype = emptyList<String>()
    private val status = emptyList<String>()
    private val documentDTOList = DocumentDTOMock.listOfDocumentDTO
    private val document = DocumentDTOMock.documentDTO
    private val attachmentId = "attachmentId"
    private val file = FileMock.file
    private val typeOfDocumentsList = TypesOfDocumentsDTOMock.typesOfDocumentsDTOList
    private val fileName = "fileName"
    private val lang = "es-es"

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        manageDocumentsUseCase = ManageDocumentsUseCase(
            documentsRepository,
            localeRepository,
            userDataRepository
        )
    }

    @Test
    fun `when getDocuments success documentsRepository getDocuments is called`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            documentsRepository.getDocuments(
                lang,
                user.userToken,
                user.uuid,
                type,
                subtype,
                status
            )
        ).thenReturn(Success(documentDTOList))
        manageDocumentsUseCase.getDocuments()
        Mockito.verify(documentsRepository, Mockito.times(1)).getDocuments(
            lang,
            user.userToken,
            user.uuid,
            type,
            subtype,
            status
        )
    }

    @Test
    fun `when getDocuments success documentsRepository getDocuments returns success`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            documentsRepository.getDocuments(
                lang,
                user.userToken,
                user.uuid,
                type,
                subtype,
                status
            )
        ).thenReturn(Success(documentDTOList))
        val result = manageDocumentsUseCase.getDocuments()
        Assert.assertEquals(Success(documentDTOList), result)
    }

    @Test
    fun `when getDocuments failure documentsRepository getDocuments returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            documentsRepository.getDocuments(
                lang,
                user.userToken,
                user.uuid,
                type,
                subtype,
                status
            )
        ).thenReturn(error)
        val result = manageDocumentsUseCase.getDocuments()
        Assert.assertEquals(error, result)
    }

    @Test
    fun `when getMandatoryDocuments success documentsRepository getDocuments is called`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            documentsRepository.getDocuments(
                lang,
                user.userToken,
                user.uuid,
                listOf("transactionCompliance"),
                listOf("TAX_CODE_DOCUMENT", "ID_MISSING_OR_EXPIRED", "FACE_VERIFICATION"),
                listOf("MISSING", "UNDER_REVIEW"),
            ),
        ).thenReturn(Success(documentDTOList))
        Mockito.`when`(
            documentsRepository.getDocuments(
                lang,
                user.userToken,
                user.uuid,
                listOf("userId"),
                emptyList(),
                listOf("MISSING", "UNDER_REVIEW")
            )
        ).thenReturn(Success(documentDTOList))
        manageDocumentsUseCase.getMandatoryDocuments()
        Mockito.verify(documentsRepository, Mockito.times(1)).getDocuments(
            lang,
            user.userToken,
            user.uuid,
            listOf("transactionCompliance"),
            listOf("TAX_CODE_DOCUMENT", "ID_MISSING_OR_EXPIRED", "FACE_VERIFICATION"),
            listOf("MISSING", "UNDER_REVIEW"),
        )
        Mockito.verify(documentsRepository, Mockito.times(1)).getDocuments(
            lang,
            user.userToken,
            user.uuid,
            listOf("userId"),
            emptyList(),
            listOf("MISSING", "UNDER_REVIEW")
        )
    }

    @Test
    fun `when getMandatoryDocuments success documentsRepository getDocuments returns success`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            documentsRepository.getDocuments(
                lang,
                user.userToken,
                user.uuid,
                listOf("transactionCompliance"),
                listOf("TAX_CODE_DOCUMENT", "ID_MISSING_OR_EXPIRED", "FACE_VERIFICATION"),
                listOf("MISSING", "UNDER_REVIEW"),
            ),
        ).thenReturn(Success(documentDTOList))
        Mockito.`when`(
            documentsRepository.getDocuments(
                lang,
                user.userToken,
                user.uuid,
                listOf("userId"),
                emptyList(),
                listOf("MISSING", "UNDER_REVIEW")
            )
        ).thenReturn(Success(documentDTOList))
        val result = manageDocumentsUseCase.getMandatoryDocuments()
        Assert.assertEquals(Success(listOf(document, document)), result)
    }

    @Test
    fun `when getMandatoryDocuments success documentsRepository getDocuments returns failure`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            documentsRepository.getDocuments(
                lang,
                user.userToken,
                user.uuid,
                listOf("transactionCompliance"),
                listOf("TAX_CODE_DOCUMENT", "ID_MISSING_OR_EXPIRED", "FACE_VERIFICATION"),
                listOf("MISSING", "UNDER_REVIEW"),
            ),
        ).thenReturn(error)
        Mockito.`when`(
            documentsRepository.getDocuments(
                lang,
                user.userToken,
                user.uuid,
                listOf("userId"),
                emptyList(),
                listOf("MISSING", "UNDER_REVIEW")
            )
        ).thenReturn(error)
        val result = manageDocumentsUseCase.getMandatoryDocuments()
        Assert.assertEquals(error, result)
    }

    @Test
    fun `when getDocumentById success documentsRepository getDocumentById is called`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            documentsRepository.getDocumentById(
                lang,
                user.userToken,
                user.uuid,
                documentId
            )
        ).thenReturn(Success(documentDTOList.first()))
        manageDocumentsUseCase.getDocumentById(documentId)
        Mockito.verify(documentsRepository, Mockito.times(1)).getDocumentById(
            lang,
            user.userToken,
            user.uuid,
            documentId
        )
    }

    @Test
    fun `when getDocumentById success documentsRepository getDocumentById returns success`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            documentsRepository.getDocumentById(
                lang,
                user.userToken,
                user.uuid,
                documentId
            )
        ).thenReturn(Success(documentDTOList.first()))
        val result = manageDocumentsUseCase.getDocumentById(documentId)
        Assert.assertEquals(Success(documentDTOList.first()), result)
    }

    @Test
    fun `when getDocumentById failure documentsRepository getDocumentById returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            documentsRepository.getDocumentById(
                lang,
                user.userToken,
                user.uuid,
                documentId
            )
        ).thenReturn(error)
        val result = manageDocumentsUseCase.getDocumentById(documentId)
        Assert.assertEquals(error, result)
    }

    @Test
    fun `when saveDocument success documentsRepository saveDocument is called`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            documentsRepository.saveDocument(
                lang,
                user.userToken,
                user.uuid,
                documentFileDTO
            )
        ).thenReturn(Success(true))
        manageDocumentsUseCase.saveDocument(documentFileDTO)
        Mockito.verify(documentsRepository, Mockito.times(1)).saveDocument(
            lang,
            user.userToken,
            user.uuid,
            documentFileDTO
        )
    }

    @Test
    fun `when saveDocument success documentsRepository saveDocument returns success`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            documentsRepository.saveDocument(
                lang,
                user.userToken,
                user.uuid,
                documentFileDTO
            )
        ).thenReturn(Success(true))
        val result = manageDocumentsUseCase.saveDocument(documentFileDTO)
        Assert.assertEquals(Success(true), result)
    }

    @Test
    fun `when saveDocument failure documentsRepository saveDocument returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            documentsRepository.saveDocument(
                lang,
                user.userToken,
                user.uuid,
                documentFileDTO
            )
        ).thenReturn(error)
        val result = manageDocumentsUseCase.saveDocument(documentFileDTO)
        Assert.assertEquals(error, result)
    }

    @Test
    fun `when getAttachment success documentsRepository getAttachment is called`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(documentsRepository.getAttachment(lang, attachmentId, fileName, user.userToken, user.uuid))
            .thenReturn(Success(file))
        manageDocumentsUseCase.getAttachment(attachmentId, fileName)
        Mockito.verify(documentsRepository, Mockito.times(1)).getAttachment(lang, attachmentId, fileName, user.userToken, user.uuid)
    }

    @Test
    fun `when getAttachment success documentsRepository getAttachment returns success`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(documentsRepository.getAttachment(lang, attachmentId, fileName, user.userToken, user.uuid))
            .thenReturn(Success(file))
        val result = manageDocumentsUseCase.getAttachment(attachmentId, fileName)
        Assert.assertEquals(Success(file), result)
    }

    @Test
    fun `when getAttachment failure documentsRepository getAttachment returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(documentsRepository.getAttachment(lang, attachmentId, fileName, user.userToken, user.uuid))
            .thenReturn(error)
        val result = manageDocumentsUseCase.getAttachment(attachmentId, fileName)
        Assert.assertEquals(error, result)
    }

    @Test
    fun `when getDocumentTypes success documentsRepository getDocumentTypes is called`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            documentsRepository.getDocumentTypes(
                lang,
                user.country.countries.first().iso3,
            )
        ).thenReturn(Success(typeOfDocumentsList))
        manageDocumentsUseCase.getDocumentTypes()
        Mockito.verify(documentsRepository, Mockito.times(1)).getDocumentTypes(
            lang,
            user.country.countries.first().iso3,
        )
    }

    @Test
    fun `when getDocumentTypes success documentsRepository getDocumentTypes returns success`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            documentsRepository.getDocumentTypes(
                lang,
                user.country.countries.first().iso3,
            )
        ).thenReturn(Success(typeOfDocumentsList))
        val result = manageDocumentsUseCase.getDocumentTypes()
        Assert.assertEquals(Success(typeOfDocumentsList), result)
    }

    @Test
    fun `when getDocumentTypes failure documentsRepository getDocumentTypes returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            documentsRepository.getDocumentTypes(
                lang,
                user.country.countries.first().iso3,
            )
        ).thenReturn(error)
        val result = manageDocumentsUseCase.getDocumentTypes()
        Assert.assertEquals(error, result)
    }
}
