package com.smallworldfs.moneytransferapp.data.address

import com.smallworldfs.moneytransferapp.data.address.model.AddressMapper
import com.smallworldfs.moneytransferapp.data.address.network.AddressNetworkDatasource
import com.smallworldfs.moneytransferapp.data.address.repository.AddressRepositoryImpl
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.domain.migrated.addess.repository.AddressRepository
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.get
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.mocks.dto.AddressDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import com.smallworldfs.moneytransferapp.mocks.response.AddressFormResponseMock
import com.smallworldfs.moneytransferapp.mocks.response.AddressResponseMock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.Locale

class AddressRepositoryTest {

    @Mock
    lateinit var addressNetworkDatasource: AddressNetworkDatasource

    @Mock
    lateinit var userDataRepository: UserDataRepository

    @Mock
    lateinit var apiErrorMapper: APIErrorMapper

    @Mock
    lateinit var addressMapper: AddressMapper

    lateinit var addressRepository: AddressRepository

    private val unmappedApiError = APIError.UnmappedError(432, "Unmapped error")
    private val unmappedApiErrorFailure = Failure(unmappedApiError)
    private val unmappedDomainError = Error.Unmapped("Unmapped error")
    private val unregisteredUserDomainError = Error.UnregisteredUser("No user found in device")

    private val addressResponseSuccess = Success(AddressResponseMock.addressResponse)
    private val addressDTOSuccess = Success(AddressDTOMock.addressDTOList)

    private val addressFormResponse = Success(AddressFormResponseMock.addressFormResponse)

    private val address = "address"
    private val parentId = "parentId"
    private val id = "id"
    private val userDTOSuccess = Success(UserDTOMock.userDTO)

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        addressRepository = AddressRepositoryImpl(
            addressNetworkDatasource,
            userDataRepository,
            apiErrorMapper,
            addressMapper
        )
    }

    @Test
    fun `when searchAddressByText success addressNetworkDatasource searchAddress is called`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(addressMapper.map(addressResponseSuccess.get())).thenReturn(addressDTOSuccess)
        Mockito.`when`(
            addressNetworkDatasource.searchAddress(
                Locale.getDefault().language.toLowerCase(),
                address,
                userDTOSuccess.get().country.countries[0].iso3,
                userDTOSuccess.get().userToken,
                userDTOSuccess.get().uuid
            )
        ).thenReturn(addressResponseSuccess)
        addressRepository.searchAddressByText(address)
        Mockito.verify(addressNetworkDatasource, Mockito.times(1))
            .searchAddress(
                Locale.getDefault().language.toLowerCase(),
                address,
                userDTOSuccess.value.country.countries[0].iso3,
                userDTOSuccess.value.userToken,
                userDTOSuccess.value.uuid
            )
    }

    @Test
    fun `when searchAddressByText success addressNetworkDatasource searchAddress returns success`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(addressMapper.map(addressResponseSuccess.get())).thenReturn(addressDTOSuccess)
        Mockito.`when`(
            addressNetworkDatasource.searchAddress(
                Locale.getDefault().language.toLowerCase(),
                address,
                userDTOSuccess.get().country.countries[0].iso3,
                userDTOSuccess.get().userToken,
                userDTOSuccess.get().uuid
            )
        ).thenReturn(addressResponseSuccess)
        val result = addressRepository.searchAddressByText(address)
        Assert.assertEquals(addressDTOSuccess, result)
    }

    @Test
    fun `when searchAddressByText failure addressNetworkDatasource searchAddress returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(apiErrorMapper.map(unmappedApiErrorFailure.reason))
            .thenReturn(unmappedDomainError)
        Mockito.`when`(
            addressNetworkDatasource.searchAddress(
                Locale.getDefault().language.toLowerCase(),
                address,
                userDTOSuccess.get().country.countries[0].iso3,
                userDTOSuccess.get().userToken,
                userDTOSuccess.get().uuid
            )
        ).thenReturn(unmappedApiErrorFailure)
        val result = addressRepository.searchAddressByText(address)
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when searchAddressByText failure userDataRepository getLoggedUser returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(Failure(unregisteredUserDomainError))
        val result = addressRepository.searchAddressByText(address)
        Assert.assertEquals(Failure(unregisteredUserDomainError), result)
    }

    @Test
    fun `when searchAddressByParentId success addressNetworkDatasource searchAddressByParentId is called`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(addressMapper.map(addressResponseSuccess.get())).thenReturn(addressDTOSuccess)
        Mockito.`when`(
            addressNetworkDatasource.searchAddressByParentId(
                Locale.getDefault().language.toLowerCase(),
                parentId,
                userDTOSuccess.get().country.countries[0].iso3,
                userDTOSuccess.get().userToken,
                userDTOSuccess.get().uuid
            )
        ).thenReturn(addressResponseSuccess)
        addressRepository.searchAddressByParentId(parentId)
        Mockito.verify(addressNetworkDatasource, Mockito.times(1))
            .searchAddressByParentId(
                Locale.getDefault().language.toLowerCase(),
                parentId,
                userDTOSuccess.value.country.countries[0].iso3,
                userDTOSuccess.value.userToken,
                userDTOSuccess.value.uuid
            )
    }

    @Test
    fun `when searchAddressByParentId success addressNetworkDatasource searchAddressByParentId returns success`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(addressMapper.map(addressResponseSuccess.get())).thenReturn(addressDTOSuccess)
        Mockito.`when`(
            addressNetworkDatasource.searchAddressByParentId(
                Locale.getDefault().language.toLowerCase(),
                parentId,
                userDTOSuccess.get().country.countries[0].iso3,
                userDTOSuccess.get().userToken,
                userDTOSuccess.get().uuid
            )
        ).thenReturn(addressResponseSuccess)
        val result = addressRepository.searchAddressByParentId(parentId)
        Assert.assertEquals(addressDTOSuccess, result)
    }

    @Test
    fun `when searchAddressByParentId failure addressNetworkDatasource searchAddressByParentId returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(apiErrorMapper.map(unmappedApiErrorFailure.reason))
            .thenReturn(unmappedDomainError)
        Mockito.`when`(
            addressNetworkDatasource.searchAddressByParentId(
                Locale.getDefault().language.toLowerCase(),
                parentId,
                userDTOSuccess.get().country.countries[0].iso3,
                userDTOSuccess.get().userToken,
                userDTOSuccess.get().uuid
            )
        ).thenReturn(unmappedApiErrorFailure)
        val result = addressRepository.searchAddressByParentId(parentId)
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when searchAddressByParentId failure userDataRepository getLoggedUser returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(Failure(unregisteredUserDomainError))
        val result = addressRepository.searchAddressByParentId(parentId)
        Assert.assertEquals(Failure(unregisteredUserDomainError), result)
    }

    @Test
    fun `when getAddressById success addressNetworkDatasource getAddressById is called`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(
            addressNetworkDatasource.getAddressById(
                Locale.getDefault().language.toLowerCase(),
                id,
                userDTOSuccess.get().country.countries[0].iso3,
                userDTOSuccess.get().userToken,
                userDTOSuccess.get().uuid
            )
        ).thenReturn(addressFormResponse)
        addressRepository.getAddressById(id)
        Mockito.verify(addressNetworkDatasource, Mockito.times(1))
            .getAddressById(
                Locale.getDefault().language.toLowerCase(),
                id,
                userDTOSuccess.value.country.countries[0].iso3,
                userDTOSuccess.value.userToken,
                userDTOSuccess.value.uuid
            )
    }

    @Test
    fun `when getAddressById success addressNetworkDatasource getAddressById returns success`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(
            addressNetworkDatasource.getAddressById(
                Locale.getDefault().language.toLowerCase(),
                id,
                userDTOSuccess.get().country.countries[0].iso3,
                userDTOSuccess.get().userToken,
                userDTOSuccess.get().uuid
            )
        ).thenReturn(addressFormResponse)

        val result = addressRepository.getAddressById(id)
        Assert.assertEquals(Success(addressFormResponse.get().data.form), result)
    }

    @Test
    fun `when getAddressById failure addressNetworkDatasource getAddressById returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(apiErrorMapper.map(unmappedApiErrorFailure.reason))
            .thenReturn(unmappedDomainError)
        Mockito.`when`(
            addressNetworkDatasource.getAddressById(
                Locale.getDefault().language.toLowerCase(),
                id,
                userDTOSuccess.get().country.countries[0].iso3,
                userDTOSuccess.get().userToken,
                userDTOSuccess.get().uuid
            )
        ).thenReturn(unmappedApiErrorFailure)
        val result = addressRepository.getAddressById(id)
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when getAddressById failure userDataRepository getLoggedUser returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(Failure(unregisteredUserDomainError))
        val result = addressRepository.getAddressById(id)
        Assert.assertEquals(Failure(unregisteredUserDomainError), result)
    }

    @Test
    fun `when getAddressForm success addressNetworkDatasource getAddressForm is called`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(
            addressNetworkDatasource.getAddressForm(
                Locale.getDefault().language.toLowerCase(),
                userDTOSuccess.get().country.countries[0].iso3,
                userDTOSuccess.get().userToken,
                userDTOSuccess.get().uuid
            )
        ).thenReturn(addressFormResponse)
        addressRepository.getAddressForm()
        Mockito.verify(addressNetworkDatasource, Mockito.times(1))
            .getAddressForm(
                Locale.getDefault().language.toLowerCase(),
                userDTOSuccess.value.country.countries[0].iso3,
                userDTOSuccess.value.userToken,
                userDTOSuccess.value.uuid
            )
    }

    @Test
    fun `when getAddressForm success addressNetworkDatasource getAddressForm returns success`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(
            addressNetworkDatasource.getAddressForm(
                Locale.getDefault().language.toLowerCase(),
                userDTOSuccess.get().country.countries[0].iso3,
                userDTOSuccess.get().userToken,
                userDTOSuccess.get().uuid
            )
        ).thenReturn(addressFormResponse)

        val result = addressRepository.getAddressForm()
        Assert.assertEquals(Success(addressFormResponse.get().data.form), result)
    }

    @Test
    fun `when getAddressForm failure addressNetworkDatasource getAddressForm returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(apiErrorMapper.map(unmappedApiErrorFailure.reason))
            .thenReturn(unmappedDomainError)
        Mockito.`when`(
            addressNetworkDatasource.getAddressForm(
                Locale.getDefault().language.toLowerCase(),
                userDTOSuccess.get().country.countries[0].iso3,
                userDTOSuccess.get().userToken,
                userDTOSuccess.get().uuid
            )
        ).thenReturn(unmappedApiErrorFailure)
        val result = addressRepository.getAddressForm()
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when getAddressForm failure userDataRepository getLoggedUser returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(Failure(unregisteredUserDomainError))
        val result = addressRepository.getAddressForm()
        Assert.assertEquals(Failure(unregisteredUserDomainError), result)
    }
}
