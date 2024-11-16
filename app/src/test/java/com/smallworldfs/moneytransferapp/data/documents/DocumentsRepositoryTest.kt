package com.smallworldfs.moneytransferapp.data.documents

import com.smallworldfs.moneytransferapp.data.account.documents.mappers.DocumentsDTOMapper
import com.smallworldfs.moneytransferapp.data.account.documents.mappers.TypeOfDocumentsDTOMapper
import com.smallworldfs.moneytransferapp.data.account.documents.model.UploadDocumentRequest
import com.smallworldfs.moneytransferapp.data.account.documents.repository.DocumentsRepositoryImpl
import com.smallworldfs.moneytransferapp.data.account.documents.repository.network.DocumentsNetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.FileResolver
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.repository.DocumentsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.mocks.FieldMock
import com.smallworldfs.moneytransferapp.mocks.FileMock
import com.smallworldfs.moneytransferapp.mocks.dto.DocumentDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.DocumentFileDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.TypesOfDocumentsDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import com.smallworldfs.moneytransferapp.mocks.response.GetDocumentsResponseMock
import com.smallworldfs.moneytransferapp.mocks.response.ResponseBodyMock
import com.smallworldfs.moneytransferapp.mocks.response.TypesOfDocumentsResponseMock
import com.smallworldfs.moneytransferapp.mocks.response.UploadDocumentResponseMock
import com.smallworldfs.moneytransferapp.utils.EXTENSION_PDF
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class DocumentsRepositoryTest {

    @Mock
    lateinit var documentNetworkDataSource: DocumentsNetworkDatasource

    @Mock
    lateinit var fileBuilder: FileResolver

    @Mock
    lateinit var documentsDTOMapper: DocumentsDTOMapper

    @Mock
    lateinit var typeOfDocumentsDTOMapper: TypeOfDocumentsDTOMapper

    @Mock
    lateinit var apiErrorMapper: APIErrorMapper

    private lateinit var documentRepository: DocumentsRepository

    private val lang = "es-es"
    private val unmappedApiError = APIError.UnmappedError(432, "Unmapped error")
    private val uncompletedOperation = Error.UncompletedOperation()
    private val unmappedError = Error.Unmapped()
    private val user = UserDTOMock.userDTO
    private val typesOfDocumentsResponse = TypesOfDocumentsResponseMock.typesOfDocumentsResponse
    private val typesOfDocumentsDTOList = TypesOfDocumentsDTOMock.typesOfDocumentsDTOList
    private val docType = listOf("docType")
    private val subType = listOf("subtype")
    private val status = listOf("status")
    private val getDocumentListResponse = GetDocumentsResponseMock.getDocumentListResponse
    private val getDocumentResponse = GetDocumentsResponseMock.getDocumentResponse
    private val documentListDTO = DocumentDTOMock.listOfDocumentDTO
    private val documentDTO = DocumentDTOMock.documentDTO
    private val documentId = "documentId"
    private val documentFileDTO = DocumentFileDTOMock.documentFileDTO
    private val uploadDocumentResponse = UploadDocumentResponseMock.uploadDocumentResponse
    private val fileName = "fileName"
    private val file = FileMock.file
    private val uid = "uid"
    private val documentType = "documentType"
    private val fields = FieldMock.arrayFields

    private val uploadDocumentRequest = UploadDocumentRequest(
        user.uuid,
        user.userToken,
        documentFileDTO.document,
        documentFileDTO.numberDocument,
        documentFileDTO.country,
        documentFileDTO.expirationDate.toString(),
        documentFileDTO.type,
        documentFileDTO.idIssueCountry,
        documentFileDTO.issueDate.toString(),
        documentFileDTO.complianceType,
        documentFileDTO.mtn,
        documentFileDTO.documentType,
        documentFileDTO.front,
        documentFileDTO.back,
        documentFileDTO.uid,
        documentFileDTO.taxCode,
        documentFileDTO.userIdType
    )

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        documentRepository = DocumentsRepositoryImpl(
            documentNetworkDataSource,
            apiErrorMapper,
            documentsDTOMapper,
            typeOfDocumentsDTOMapper,
            fileBuilder
        )
    }

    @Test
    fun `when getDocumentTypes success documentsNetworkDatasource getDocumentTypes is called`() {
        Mockito.`when`(
            documentNetworkDataSource.getDocumentTypes(
                lang,
                user.country.countries.first().iso3
            )
        ).thenReturn(Success(typesOfDocumentsResponse))
        documentRepository.getDocumentTypes(
            lang,
            user.country.countries.first().iso3
        )
        Mockito.verify(documentNetworkDataSource, Mockito.times(1))
            .getDocumentTypes(
                lang,
                user.country.countries.first().iso3
            )
    }

    @Test
    fun `when getDocumentTypes success documentsNetworkDatasource getDocumentTypes return success`() {
        Mockito.`when`(
            documentNetworkDataSource.getDocumentTypes(
                lang,
                user.country.countries.first().iso3
            )
        ).thenReturn(Success(typesOfDocumentsResponse))
        Mockito.`when`(typeOfDocumentsDTOMapper.map(typesOfDocumentsResponse))
            .thenReturn(typesOfDocumentsDTOList)
        val result = documentRepository.getDocumentTypes(
            lang,
            user.country.countries.first().iso3
        )
        Assert.assertEquals(Success(typesOfDocumentsDTOList), result)
    }

    @Test
    fun `when getDocumentTypes fails documentsNetworkDatasource getDocumentTypes return failure`() {
        Mockito.`when`(
            documentNetworkDataSource.getDocumentTypes(
                lang,
                user.country.countries.first().iso3
            )
        ).thenReturn(Failure(unmappedApiError))
        Mockito.`when`(apiErrorMapper.map(unmappedApiError))
            .thenReturn(unmappedError)
        val result = documentRepository.getDocumentTypes(
            lang,
            user.country.countries.first().iso3
        )
        Assert.assertEquals(Failure(unmappedError), result)
    }

    @Test
    fun `when getDocuments success documentsNetworkDatasource requestDocuments is called`() {
        Mockito.`when`(
            documentNetworkDataSource.requestDocuments(
                lang,
                user.userToken,
                user.uuid,
                docType,
                subType,
                status
            )
        ).thenReturn(Success(getDocumentListResponse))
        documentRepository.getDocuments(
            lang,
            user.userToken,
            user.uuid,
            docType,
            subType,
            status
        )
        Mockito.verify(documentNetworkDataSource, Mockito.times(1))
            .requestDocuments(
                lang,
                user.userToken,
                user.uuid,
                docType,
                subType,
                status
            )
    }

    @Test
    fun `when getDocuments success documentsNetworkDatasource requestDocuments return success`() {
        Mockito.`when`(
            documentNetworkDataSource.requestDocuments(
                lang,
                user.userToken,
                user.uuid,
                docType,
                subType,
                status
            )
        ).thenReturn(Success(getDocumentListResponse))
        Mockito.`when`(documentsDTOMapper.mapGetDocumentsResponse(getDocumentListResponse))
            .thenReturn(documentListDTO)
        val result = documentRepository.getDocuments(
            lang,
            user.userToken,
            user.uuid,
            docType,
            subType,
            status
        )
        Assert.assertEquals(Success(documentListDTO), result)
    }

    @Test
    fun `when getDocuments fails documentsNetworkDatasource requestDocuments return failure`() {
        Mockito.`when`(
            documentNetworkDataSource.requestDocuments(
                lang,
                user.userToken,
                user.uuid,
                docType,
                subType,
                status
            )
        ).thenReturn(Failure(unmappedApiError))
        Mockito.`when`(apiErrorMapper.map(unmappedApiError))
            .thenReturn(unmappedError)
        val result = documentRepository.getDocuments(
            lang,
            user.userToken,
            user.uuid,
            docType,
            subType,
            status
        )
        Assert.assertEquals(Failure(unmappedError), result)
    }

    @Test
    fun `when getDocumentById success documentsNetworkDatasource requestDocumentById is called`() {
        Mockito.`when`(
            documentNetworkDataSource.requestDocumentById(
                lang,
                user.userToken,
                user.uuid,
                documentId
            )
        ).thenReturn(Success(getDocumentResponse))
        documentRepository.getDocumentById(
            lang,
            user.userToken,
            user.uuid,
            documentId
        )
        Mockito.verify(documentNetworkDataSource, Mockito.times(1))
            .requestDocumentById(
                lang,
                user.userToken,
                user.uuid,
                documentId
            )
    }

    @Test
    fun `when getDocumentById success documentsNetworkDatasource requestDocumentById return success`() {
        Mockito.`when`(
            documentNetworkDataSource.requestDocumentById(
                lang,
                user.userToken,
                user.uuid,
                documentId
            )
        ).thenReturn(Success(getDocumentResponse))
        Mockito.`when`(documentsDTOMapper.mapDocument(getDocumentResponse.document))
            .thenReturn(documentDTO)
        val result = documentRepository.getDocumentById(
            lang,
            user.userToken,
            user.uuid,
            documentId
        )
        Assert.assertEquals(Success(documentDTO), result)
    }

    @Test
    fun `when getDocumentById fails documentsNetworkDatasource requestDocumentById return failure`() {
        Mockito.`when`(
            documentNetworkDataSource.requestDocumentById(
                lang,
                user.userToken,
                user.uuid,
                documentId
            )
        ).thenReturn(Failure(unmappedApiError))
        Mockito.`when`(apiErrorMapper.map(unmappedApiError))
            .thenReturn(unmappedError)
        val result = documentRepository.getDocumentById(
            lang,
            user.userToken,
            user.uuid,
            documentId
        )
        Assert.assertEquals(Failure(unmappedError), result)
    }

    @Test
    fun `when saveDocument success documentsNetworkDatasource uploadDocument is called`() {
        Mockito.`when`(
            documentNetworkDataSource.uploadDocument(
                lang,
                uploadDocumentRequest
            )
        ).thenReturn(Success(uploadDocumentResponse))
        Mockito.`when`(
            documentsDTOMapper.mapUploadDocumentsRequest(
                user.uuid,
                user.userToken,
                documentFileDTO
            )
        ).thenReturn(uploadDocumentRequest)
        documentRepository.saveDocument(
            lang,
            user.userToken,
            user.uuid,
            documentFileDTO
        )
        Mockito.verify(documentNetworkDataSource, Mockito.times(1))
            .uploadDocument(
                lang,
                uploadDocumentRequest
            )
    }

    @Test
    fun `when saveDocument success documentsNetworkDatasource uploadDocument return success`() {
        Mockito.`when`(
            documentNetworkDataSource.uploadDocument(
                lang,
                uploadDocumentRequest
            )
        ).thenReturn(Success(uploadDocumentResponse))
        Mockito.`when`(
            documentsDTOMapper.mapUploadDocumentsRequest(
                user.uuid,
                user.userToken,
                documentFileDTO
            )
        ).thenReturn(uploadDocumentRequest)
        val result = documentRepository.saveDocument(
            lang,
            user.userToken,
            user.uuid,
            documentFileDTO
        )
        Assert.assertEquals(Success(true), result)
    }

    @Test
    fun `when saveDocument fails documentsNetworkDatasource uploadDocument return failure`() {
        Mockito.`when`(
            documentNetworkDataSource.uploadDocument(
                lang,
                uploadDocumentRequest
            )
        ).thenReturn(Failure(unmappedApiError))
        Mockito.`when`(
            documentsDTOMapper.mapUploadDocumentsRequest(
                user.uuid,
                user.userToken,
                documentFileDTO
            )
        ).thenReturn(uploadDocumentRequest)
        Mockito.`when`(apiErrorMapper.map(unmappedApiError))
            .thenReturn(unmappedError)
        val result = documentRepository.saveDocument(
            lang,
            user.userToken,
            user.uuid,
            documentFileDTO
        )
        Assert.assertEquals(Failure(unmappedError), result)
    }

    @Test
    fun `when getAttachment success documentsNetworkDatasource downloadAttachment is called`() {
        Mockito.`when`(
            documentNetworkDataSource.downloadAttachment(
                lang,
                user.id,
                user.userToken,
                user.uuid
            )
        ).thenReturn(Failure(unmappedApiError))
        documentRepository.getAttachment(
            lang,
            user.id,
            fileName,
            user.userToken,
            user.uuid
        )
        Mockito.verify(documentNetworkDataSource, Mockito.times(1))
            .downloadAttachment(
                lang,
                user.id,
                user.userToken,
                user.uuid
            )
    }

    @Test
    fun `when getAttachment success documentsNetworkDatasource downloadAttachment return success`() {
        Mockito.`when`(
            documentNetworkDataSource.downloadAttachment(
                lang,
                user.id,
                user.userToken,
                user.uuid
            )
        ).thenReturn(Success(ResponseBodyMock.emptyResponseBody))
        Mockito.`when`(fileBuilder.buildFile(ResponseBodyMock.emptyResponseBody, fileName, EXTENSION_PDF))
            .thenReturn(file)
        val result = documentRepository.getAttachment(
            lang,
            user.id,
            fileName,
            user.userToken,
            user.uuid
        )
        Assert.assertEquals(Success(file), result)
    }

    @Test
    fun `when getAttachment fails documentsNetworkDatasource fileBuilder buildFile return failure`() {
        Mockito.`when`(
            documentNetworkDataSource.downloadAttachment(
                lang,
                user.id,
                user.userToken,
                user.uuid
            )
        ).thenReturn(Success(ResponseBodyMock.emptyResponseBody))
        Mockito.`when`(fileBuilder.buildFile(ResponseBodyMock.emptyResponseBody, fileName, EXTENSION_PDF))
            .thenReturn(null)
        val result = documentRepository.getAttachment(
            lang,
            user.id,
            fileName,
            user.userToken,
            user.uuid
        )
        Assert.assertEquals(Failure(uncompletedOperation).javaClass, result.javaClass)
    }

    @Test
    fun `when getAttachment fails documentsNetworkDatasource downloadAttachment return failure`() {
        Mockito.`when`(
            documentNetworkDataSource.downloadAttachment(
                lang,
                user.id,
                user.userToken,
                user.uuid
            )
        ).thenReturn(Failure(unmappedApiError))
        Mockito.`when`(apiErrorMapper.map(unmappedApiError))
            .thenReturn(unmappedError)
        val result = documentRepository.getAttachment(
            lang,
            user.id,
            fileName,
            user.userToken,
            user.uuid
        )
        Assert.assertEquals(Failure(unmappedError), result)
    }

    @Test
    fun `when getDocumentForm success documentsNetworkDatasource requestDocumentForm is called`() {
        Mockito.`when`(
            documentNetworkDataSource.requestDocumentForm(
                lang,
                uid,
                user.userToken,
                user.uuid,
                documentType
            )
        ).thenReturn(Success(fields))
        documentRepository.getDocumentForm(
            lang,
            uid,
            user.userToken,
            user.uuid,
            documentType
        )
        Mockito.verify(documentNetworkDataSource, Mockito.times(1))
            .requestDocumentForm(
                lang,
                uid,
                user.userToken,
                user.uuid,
                documentType
            )
    }

    @Test
    fun `when getDocumentForm success documentsNetworkDatasource requestDocumentForm return success`() {
        Mockito.`when`(
            documentNetworkDataSource.requestDocumentForm(
                lang,
                uid,
                user.userToken,
                user.uuid,
                documentType
            )
        ).thenReturn(Success(fields))
        val result = documentRepository.getDocumentForm(
            lang,
            uid,
            user.userToken,
            user.uuid,
            documentType
        )
        Assert.assertEquals(Success(fields), result)
    }

    @Test
    fun `when getDocumentForm fails documentsNetworkDatasource requestDocumentForm response null return failure`() {
        Mockito.`when`(
            documentNetworkDataSource.requestDocumentForm(
                lang,
                uid,
                user.userToken,
                user.uuid,
                documentType
            )
        ).thenReturn(Success(null))
        val result = documentRepository.getDocumentForm(
            lang,
            uid,
            user.userToken,
            user.uuid,
            documentType
        )
        Assert.assertEquals(Failure(unmappedError).javaClass, result.javaClass)
    }

    @Test
    fun `when getDocumentForm fails documentsNetworkDatasource requestDocumentForm return failure`() {
        Mockito.`when`(
            documentNetworkDataSource.requestDocumentForm(
                lang,
                uid,
                user.userToken,
                user.uuid,
                documentType
            )
        ).thenReturn(Failure(unmappedApiError))
        Mockito.`when`(apiErrorMapper.map(unmappedApiError))
            .thenReturn(unmappedError)
        val result = documentRepository.getDocumentForm(
            lang,
            uid,
            user.userToken,
            user.uuid,
            documentType
        )
        Assert.assertEquals(Failure(unmappedError), result)
    }
}
