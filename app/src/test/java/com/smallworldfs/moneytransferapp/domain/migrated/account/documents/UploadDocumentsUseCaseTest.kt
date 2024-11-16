package com.smallworldfs.moneytransferapp.domain.migrated.account.documents

import com.smallworldfs.moneytransferapp.data.common.locale.LocaleRepository
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.repository.DocumentsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.usecase.UploadDocumentsUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.capabilities.CapabilityChecker
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.mocks.FieldMock
import com.smallworldfs.moneytransferapp.mocks.dto.DocumentFileDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class UploadDocumentsUseCaseTest {

    @Mock
    lateinit var documentsRepository: DocumentsRepository
    @Mock
    lateinit var userDataRepository: UserDataRepository
    @Mock
    lateinit var localeRepository: LocaleRepository
    @Mock
    lateinit var capabilityChecker: CapabilityChecker

    lateinit var uploadDocumentsUseCase: UploadDocumentsUseCase

    private val user = UserDTOMock.userDTO
    private val userSuccess = Success(user)
    private val error = Failure(Error.UncompletedOperation("Operation could not be completed"))

    private val lang = "en-gb"
    private val documentFileDTO = DocumentFileDTOMock.documentFileDTO
    private val documentId = "10001"
    private val arrayFields = FieldMock.arrayFields

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        uploadDocumentsUseCase = UploadDocumentsUseCase(
            documentsRepository,
            userDataRepository,
            localeRepository,
            capabilityChecker
        )
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
                documentFileDTO.copy(country = user.country.countries.first().iso3)
            )
        ).thenReturn(Success(true))
        uploadDocumentsUseCase.saveDocument(documentFileDTO)
        Mockito.verify(documentsRepository, Mockito.times(1)).saveDocument(
            lang,
            user.userToken,
            user.uuid,
            documentFileDTO.copy(country = user.country.countries.first().iso3)
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
                documentFileDTO.copy(country = user.country.countries.first().iso3)
            )
        ).thenReturn(Success(true))
        val result = uploadDocumentsUseCase.saveDocument(documentFileDTO)
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
                documentFileDTO.copy(country = user.country.countries.first().iso3)
            )
        ).thenReturn(error)
        val result = uploadDocumentsUseCase.saveDocument(documentFileDTO)
        Assert.assertEquals(error, result)
    }

    @Test
    fun `when getDocumentForm success documentsRepository getDocumentForm is called`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            documentsRepository.getDocumentForm(
                lang,
                documentId,
                user.userToken,
                user.uuid,
                "sender"
            )
        ).thenReturn(Success(arrayFields))
        uploadDocumentsUseCase.getDocumentForm(documentId, "sender")
        Mockito.verify(documentsRepository, Mockito.times(1)).getDocumentForm(
            lang,
            documentId,
            user.userToken,
            user.uuid,
            "sender"
        )
    }

    @Test
    fun `when getDocumentForm success documentsRepository getDocumentForm returns success`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            documentsRepository.getDocumentForm(
                lang,
                documentId,
                user.userToken,
                user.uuid,
                "sender"
            )
        ).thenReturn(Success(arrayFields))
        val result = uploadDocumentsUseCase.getDocumentForm(documentId, "sender")
        Assert.assertEquals(Success(arrayFields), result)
    }

    @Test
    fun `when getDocumentForm failure documentsRepository getDocumentForm returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userSuccess)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            documentsRepository.getDocumentForm(
                lang,
                documentId,
                user.userToken,
                user.uuid,
                "sender"
            )
        ).thenReturn(error)
        val result = uploadDocumentsUseCase.getDocumentForm(documentId, "sender")
        Assert.assertEquals(error, result)
    }

    @Test
    fun `when checkReadWritePermissions success capabilityChecker askForReadWritePermissions is called`() {
        Mockito.`when`(capabilityChecker.askForReadWritePermissions()).thenReturn(Success(true))
        uploadDocumentsUseCase.checkReadWritePermissions()
        Mockito.verify(capabilityChecker, Mockito.times(1)).askForReadWritePermissions()
    }

    @Test
    fun `when checkReadWritePermissions success capabilityChecker askForReadWritePermissions returns success`() {
        Mockito.`when`(capabilityChecker.askForReadWritePermissions()).thenReturn(Success(true))
        val result = uploadDocumentsUseCase.checkReadWritePermissions()
        Assert.assertEquals(Success(true), result)
    }

    @Test
    fun `when checkReadWritePermissions failure capabilityChecker askForReadWritePermissions returns error`() {
        Mockito.`when`(capabilityChecker.askForReadWritePermissions()).thenReturn(error)
        val result = uploadDocumentsUseCase.checkReadWritePermissions()
        Assert.assertEquals(error, result)
    }
}
