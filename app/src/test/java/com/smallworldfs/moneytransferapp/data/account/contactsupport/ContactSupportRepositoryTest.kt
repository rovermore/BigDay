package com.smallworldfs.moneytransferapp.data.account.contactsupport

import com.smallworldfs.moneytransferapp.data.account.contactsupport.repository.ContactSupportRepositoryImpl
import com.smallworldfs.moneytransferapp.data.account.contactsupport.repository.mappers.ContactSupportDTOMapper
import com.smallworldfs.moneytransferapp.data.account.contactsupport.repository.network.ContactSupportNetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.mocks.dto.ContactSupportDTOMock
import com.smallworldfs.moneytransferapp.mocks.response.ContactSupportInfoResponseMock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class ContactSupportRepositoryTest {

    @Mock
    lateinit var contactSupportNetworkDatasource: ContactSupportNetworkDatasource

    @Mock
    lateinit var contactSupportDTOMapper: ContactSupportDTOMapper

    @Mock
    lateinit var apiErrorMapper: APIErrorMapper

    lateinit var contactSupportRepository: ContactSupportRepositoryImpl

    private val contactSupportDTO = ContactSupportDTOMock.contactSupportDTO
    private val contactSupportInfoResponse = ContactSupportInfoResponseMock.contactSupportInfoResponse

    private val unmappedApiError = APIError.UnmappedError(432, "Unmapped error")
    private val unmappedErrorFailure = Failure(unmappedApiError)
    private val unregisteredUSerDomainError = Error.UnregisteredUser("No user found in device")

    private val country = "es"

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        contactSupportRepository = ContactSupportRepositoryImpl(
            contactSupportNetworkDatasource,
            apiErrorMapper,
            contactSupportDTOMapper
        )
    }

    @Test
    fun `when getContactSupport success contactSupportNetworkDatasource requestContactSupport is called`() {
        Mockito.`when`(contactSupportDTOMapper.map(contactSupportInfoResponse))
            .thenReturn(contactSupportDTO)
        Mockito.`when`(contactSupportNetworkDatasource.requestContactSupport(country))
            .thenReturn(Success(contactSupportInfoResponse))
        contactSupportRepository.getContactSupport(country)
        Mockito.verify(contactSupportNetworkDatasource, Mockito.times(1))
            .requestContactSupport(country)
    }

    @Test
    fun `when getContactSupport success contactSupportNetworkDatasource requestContactSupport returns success`() {
        Mockito.`when`(contactSupportDTOMapper.map(contactSupportInfoResponse))
            .thenReturn(contactSupportDTO)
        Mockito.`when`(contactSupportNetworkDatasource.requestContactSupport(country))
            .thenReturn(Success(contactSupportInfoResponse))
        val result = contactSupportRepository.getContactSupport(country)
        Assert.assertEquals(Success(contactSupportDTO), result)
    }

    @Test
    fun `when getContactSupport failure contactSupportNetworkDatasource requestContactSupport returns error`() {
        Mockito.`when`(apiErrorMapper.map(unmappedErrorFailure.reason))
            .thenReturn(unregisteredUSerDomainError)
        Mockito.`when`(contactSupportNetworkDatasource.requestContactSupport(country))
            .thenReturn(unmappedErrorFailure)
        val result = contactSupportRepository.getContactSupport(country)
        Assert.assertEquals(Failure(unregisteredUSerDomainError), result)
    }
}
