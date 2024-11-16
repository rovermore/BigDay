package com.smallworldfs.moneytransferapp.data.operations

import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.data.common.locale.LocaleRepository
import com.smallworldfs.moneytransferapp.data.integrity.network.IntegrityNetworkDatasource
import com.smallworldfs.moneytransferapp.data.operations.model.IntegrityDTOMapper
import com.smallworldfs.moneytransferapp.data.operations.model.OtpDTOMapper
import com.smallworldfs.moneytransferapp.data.operations.network.OperationsNetworkDatasource
import com.smallworldfs.moneytransferapp.data.operations.repository.OperationsRepositoryImpl
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.get
import com.smallworldfs.moneytransferapp.domain.migrated.operations.repository.OperationsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.mocks.dto.IntegrityDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.OtpDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import com.smallworldfs.moneytransferapp.mocks.response.IntegrityDataResponseMock
import com.smallworldfs.moneytransferapp.mocks.response.OTPResponseMock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class OperationsRepositoryTest {

    @Mock
    lateinit var operationsNetworkDatasource: OperationsNetworkDatasource

    @Mock
    lateinit var integrityNetworkDatasource: IntegrityNetworkDatasource

    @Mock
    lateinit var localeRepository: LocaleRepository

    @Mock
    lateinit var userDataRepository: UserDataRepository

    @Mock
    lateinit var apiErrorMapper: APIErrorMapper

    @Mock
    lateinit var otpDTOMapper: OtpDTOMapper

    @Mock
    lateinit var integrityDTOMapper: IntegrityDTOMapper

    lateinit var operationsRepository: OperationsRepository

    private val unmappedApiError = APIError.UnmappedError(432, "Unmapped error")
    private val unmappedDomainError = Error.Unmapped("Unmapped error")
    private val unregisteredUserDomainError = Error.UnregisteredUser("No user found in device")

    private val userDTOSuccess = Success(UserDTOMock.userDTO)

    private val otp = "otp"
    private val operationId = "operationId"
    private val phone = "phone"
    private val countryCode = "countryCode"
    private val nonce = "nonce"
    private val integrityToken = "integrityToken"
    private val FACTOR_TYPE_SMS = "sms"
    private val FACTOR_TYPE_EMAIL = "email"
    private val operation = "operation"

    private val otpResponse = OTPResponseMock.otpResponse
    private val otpDTO = OtpDTOMock.otpDTO
    private val integrityDTO = IntegrityDTOMock.integrityDTO
    private val integrityDataResponse = IntegrityDataResponseMock.integrityDataResponse

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        operationsRepository = OperationsRepositoryImpl(
            operationsNetworkDatasource,
            integrityNetworkDatasource,
            apiErrorMapper,
            localeRepository,
            userDataRepository,
            otpDTOMapper,
            integrityDTOMapper
        )
    }

    @Test
    fun `when validateOTP success operationsNetworkDatasource validateOTP is called`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(
            operationsNetworkDatasource.validateOTP(
                localeRepository.getLang(),
                userDTOSuccess.get().uuid,
                userDTOSuccess.get().userToken,
                operationId,
                otp
            )
        ).thenReturn(Success(otpResponse))
        operationsRepository.validateOTP(otp, operationId)
        Mockito.verify(operationsNetworkDatasource, Mockito.times(1))
            .validateOTP(
                localeRepository.getLang(),
                userDTOSuccess.get().uuid,
                userDTOSuccess.get().userToken,
                operationId,
                otp
            )
    }

    @Test
    fun `when validateOTP success operationsNetworkDatasource validateOTP returns success`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(
            operationsNetworkDatasource.validateOTP(
                localeRepository.getLang(),
                userDTOSuccess.get().uuid,
                userDTOSuccess.get().userToken,
                operationId,
                otp
            )
        ).thenReturn(Success(otpResponse))
        val result = operationsRepository.validateOTP(otp, operationId)
        Assert.assertEquals(Success(true), result)
    }

    @Test
    fun `when validateOTP failure operationsNetworkDatasource validateOTP returns error`() {
        Mockito.`when`(apiErrorMapper.map(unmappedApiError))
            .thenReturn(unmappedDomainError)
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(
            operationsNetworkDatasource.validateOTP(
                localeRepository.getLang(),
                userDTOSuccess.get().uuid,
                userDTOSuccess.get().userToken,
                operationId,
                otp
            )
        ).thenReturn(Failure(unmappedApiError))
        val result = operationsRepository.validateOTP(otp, operationId)
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when validateOTP failure userDataRepository getLoggedUser returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(Failure(unregisteredUserDomainError))
        val result = operationsRepository.validateOTP(otp, operationId)
        Assert.assertEquals(Failure(unregisteredUserDomainError), result)
    }

    @Test
    fun `when sendSMSOTP success operationsNetworkDatasource sendSMSOTP is called`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(otpDTOMapper.map(otpResponse)).thenReturn(Success(otpDTO))
        Mockito.`when`(
            operationsNetworkDatasource.sendSMSOTP(
                localeRepository.getLang(),
                userDTOSuccess.get().uuid,
                userDTOSuccess.get().userToken,
                FACTOR_TYPE_SMS,
                phone,
                countryCode,
                nonce,
                integrityToken
            )
        ).thenReturn(Success(otpResponse))
        operationsRepository.sendSMSOTP(phone, countryCode, nonce, integrityToken)
        Mockito.verify(operationsNetworkDatasource, Mockito.times(1))
            .sendSMSOTP(
                localeRepository.getLang(),
                userDTOSuccess.get().uuid,
                userDTOSuccess.get().userToken,
                FACTOR_TYPE_SMS,
                phone,
                countryCode,
                nonce,
                integrityToken
            )
    }

    @Test
    fun `when sendSMSOTP success operationsNetworkDatasource sendSMSOTP returns success`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(otpDTOMapper.map(otpResponse)).thenReturn(Success(otpDTO))
        Mockito.`when`(
            operationsNetworkDatasource.sendSMSOTP(
                localeRepository.getLang(),
                userDTOSuccess.get().uuid,
                userDTOSuccess.get().userToken,
                FACTOR_TYPE_SMS,
                phone,
                countryCode,
                nonce,
                integrityToken
            )
        ).thenReturn(Success(otpResponse))
        val result = operationsRepository.sendSMSOTP(phone, countryCode, nonce, integrityToken)
        Assert.assertEquals(Success(otpDTO), result)
    }

    @Test
    fun `when sendSMSOTP failure operationsNetworkDatasource sendSMSOTP returns error`() {
        Mockito.`when`(apiErrorMapper.map(unmappedApiError))
            .thenReturn(unmappedDomainError)
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(otpDTOMapper.map(otpResponse)).thenReturn(Success(otpDTO))
        Mockito.`when`(
            operationsNetworkDatasource.sendSMSOTP(
                localeRepository.getLang(),
                userDTOSuccess.get().uuid,
                userDTOSuccess.get().userToken,
                FACTOR_TYPE_SMS,
                phone,
                countryCode,
                nonce,
                integrityToken
            )
        ).thenReturn(Failure(unmappedApiError))
        val result = operationsRepository.sendSMSOTP(phone, countryCode, nonce, integrityToken)
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when sendSMSOTP failure userDataRepository getLoggedUser returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(Failure(unregisteredUserDomainError))
        val result = operationsRepository.sendSMSOTP(phone, countryCode, nonce, integrityToken)
        Assert.assertEquals(Failure(unregisteredUserDomainError), result)
    }

    @Test
    fun `when resendSMSOTP success operationsNetworkDatasource resendOTP is called`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(otpDTOMapper.map(otpResponse)).thenReturn(Success(otpDTO))
        Mockito.`when`(
            operationsNetworkDatasource.resendOTP(
                localeRepository.getLang(),
                operationId,
                userDTOSuccess.get().uuid,
                userDTOSuccess.get().userToken,
                FACTOR_TYPE_SMS,
                phone,
                countryCode,
                nonce,
                integrityToken
            )
        ).thenReturn(Success(otpResponse))
        operationsRepository.resendSMSOTP(operationId, phone, countryCode, nonce, integrityToken)
        Mockito.verify(operationsNetworkDatasource, Mockito.times(1))
            .resendOTP(
                localeRepository.getLang(),
                operationId,
                userDTOSuccess.get().uuid,
                userDTOSuccess.get().userToken,
                FACTOR_TYPE_SMS,
                phone,
                countryCode,
                nonce,
                integrityToken
            )
    }

    @Test
    fun `when resendSMSOTP success operationsNetworkDatasource resendOTP returns success`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(otpDTOMapper.map(otpResponse)).thenReturn(Success(otpDTO))
        Mockito.`when`(
            operationsNetworkDatasource.resendOTP(
                localeRepository.getLang(),
                operationId,
                userDTOSuccess.get().uuid,
                userDTOSuccess.get().userToken,
                FACTOR_TYPE_SMS,
                phone,
                countryCode,
                nonce,
                integrityToken
            )
        ).thenReturn(Success(otpResponse))
        val result = operationsRepository.resendSMSOTP(operationId, phone, countryCode, nonce, integrityToken)
        Assert.assertEquals(Success(otpDTO), result)
    }

    @Test
    fun `when resendSMSOTP failure operationsNetworkDatasource resendOTP returns error`() {
        Mockito.`when`(apiErrorMapper.map(unmappedApiError))
            .thenReturn(unmappedDomainError)
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(otpDTOMapper.map(otpResponse)).thenReturn(Success(otpDTO))
        Mockito.`when`(
            operationsNetworkDatasource.resendOTP(
                localeRepository.getLang(),
                operationId,
                userDTOSuccess.get().uuid,
                userDTOSuccess.get().userToken,
                FACTOR_TYPE_SMS,
                phone,
                countryCode,
                nonce,
                integrityToken
            )
        ).thenReturn(Failure(unmappedApiError))
        val result = operationsRepository.resendSMSOTP(operationId, phone, countryCode, nonce, integrityToken)
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when resendSMSOTP failure userDataRepository getLoggedUser returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(Failure(unregisteredUserDomainError))
        val result = operationsRepository.resendSMSOTP(operationId, phone, countryCode, nonce, integrityToken)
        Assert.assertEquals(Failure(unregisteredUserDomainError), result)
    }

    @Test
    fun `when sendEmailOTP success operationsNetworkDatasource resendOTP is called`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(otpDTOMapper.map(otpResponse)).thenReturn(Success(otpDTO))
        Mockito.`when`(
            operationsNetworkDatasource.sendEmailOTP(
                localeRepository.getLang(),
                userDTOSuccess.get().uuid,
                userDTOSuccess.get().userToken,
                FACTOR_TYPE_EMAIL,
                userDTOSuccess.get().email,
                nonce,
                integrityToken
            )
        ).thenReturn(Success(otpResponse))
        operationsRepository.sendEmailOTP(nonce, integrityToken)
        Mockito.verify(operationsNetworkDatasource, Mockito.times(1))
            .sendEmailOTP(
                localeRepository.getLang(),
                userDTOSuccess.get().uuid,
                userDTOSuccess.get().userToken,
                FACTOR_TYPE_EMAIL,
                userDTOSuccess.get().email,
                nonce,
                integrityToken
            )
    }

    @Test
    fun `when sendEmailOTP success operationsNetworkDatasource resendOTP returns success`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(otpDTOMapper.map(otpResponse)).thenReturn(Success(otpDTO))
        Mockito.`when`(
            operationsNetworkDatasource.sendEmailOTP(
                localeRepository.getLang(),
                userDTOSuccess.get().uuid,
                userDTOSuccess.get().userToken,
                FACTOR_TYPE_EMAIL,
                userDTOSuccess.get().email,
                nonce,
                integrityToken
            )
        ).thenReturn(Success(otpResponse))
        val result = operationsRepository.sendEmailOTP(nonce, integrityToken)
        Assert.assertEquals(Success(otpDTO), result)
    }

    @Test
    fun `when sendEmailOTP failure operationsNetworkDatasource resendOTP returns error`() {
        Mockito.`when`(apiErrorMapper.map(unmappedApiError))
            .thenReturn(unmappedDomainError)
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(userDTOSuccess)
        Mockito.`when`(otpDTOMapper.map(otpResponse)).thenReturn(Success(otpDTO))
        Mockito.`when`(
            operationsNetworkDatasource.sendEmailOTP(
                localeRepository.getLang(),
                userDTOSuccess.get().uuid,
                userDTOSuccess.get().userToken,
                FACTOR_TYPE_EMAIL,
                userDTOSuccess.get().email,
                nonce,
                integrityToken
            )
        ).thenReturn(Failure(unmappedApiError))
        val result = operationsRepository.sendEmailOTP(nonce, integrityToken)
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when sendEmailOTP failure userDataRepository getLoggedUser returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(Failure(unregisteredUserDomainError))
        val result = operationsRepository.sendEmailOTP(nonce, integrityToken)
        Assert.assertEquals(Failure(unregisteredUserDomainError), result)
    }

    @Test
    fun `when getIntegrityDTO success integrityNetworkDatasource getIntegrity is called`() {
        Mockito.`when`(integrityDTOMapper.map(integrityDataResponse)).thenReturn(integrityDTO)
        Mockito.`when`(integrityNetworkDatasource.getIntegrity(operation)).thenReturn(Success(integrityDataResponse))
        operationsRepository.getIntegrityDTO(operation)
        Mockito.verify(integrityNetworkDatasource, Mockito.times(1))
            .getIntegrity(operation)
    }

    @Test
    fun `when getIntegrityDTO success integrityNetworkDatasource getIntegrity returns success`() {
        Mockito.`when`(integrityDTOMapper.map(integrityDataResponse)).thenReturn(integrityDTO)
        Mockito.`when`(integrityNetworkDatasource.getIntegrity(operation)).thenReturn(Success(integrityDataResponse))
        val result = operationsRepository.getIntegrityDTO(operation)
        Assert.assertEquals(Success(integrityDTO), result)
    }

    @Test
    fun `when getIntegrityDTO failure integrityNetworkDatasource getIntegrity returns error`() {
        Mockito.`when`(apiErrorMapper.map(unmappedApiError))
            .thenReturn(unmappedDomainError)
        Mockito.`when`(integrityDTOMapper.map(integrityDataResponse)).thenReturn(integrityDTO)
        Mockito.`when`(integrityNetworkDatasource.getIntegrity(operation)).thenReturn(Failure(unmappedApiError))
        val result = operationsRepository.getIntegrityDTO(operation)
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }
}
