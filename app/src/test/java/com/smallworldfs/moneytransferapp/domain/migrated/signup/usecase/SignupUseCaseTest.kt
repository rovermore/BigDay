package com.smallworldfs.moneytransferapp.domain.migrated.signup.usecase

import com.smallworldfs.moneytransferapp.base.domain.utils.Base64Tool
import com.smallworldfs.moneytransferapp.domain.migrated.addess.repository.AddressRepository
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.repository.CountryRepository
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.PasswordDTO
import com.smallworldfs.moneytransferapp.domain.migrated.operations.repository.OperationsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.signup.SignupUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.model.UserStatusDTO
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.mocks.dto.AddressDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.CountriesDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.IntegrityDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.OtpDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.StateDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.text.DateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class SignupUseCaseTest {

    @Mock
    private lateinit var userDataRepository: UserDataRepository

    @Mock
    private lateinit var addressRepository: AddressRepository

    @Mock
    private lateinit var countryRepository: CountryRepository

    @Mock
    private lateinit var operationsRepository: OperationsRepository

    @Mock
    private val base64Tool = Base64Tool()

    private lateinit var signupUseCase: SignupUseCase

    private val error = Failure(Error.UncompletedOperation("Uncompleted operation"))
    private val countryDTO = CountriesDTOMock.country1
    private val countriesSuccess = Success(CountriesDTOMock.countriesDTO)
    private val passwordDTO = PasswordDTO("U21hbGxUZXN0MjMh".toCharArray())
    private val encodedPassword = "U21hbGxUZXN0MjMh"
    private val password = "SmallTest23!".toCharArray()
    private val user = UserDTOMock.userDTO
    private val successUser = Success(user)
    private val email = "roberto@test.com"
    private val success = Success(true)
    private val userStatusDTOSuccess = Success(UserStatusDTO.Approved("APROVED"))
    private val code = "1234"
    private val operationId = "f5498hffpeui"
    private val phoneNumber = "645488348"
    private val otpDTO = OtpDTOMock.otpDTO
    private val successOtpDTO = Success(otpDTO)
    private val stateList = StateDTOMock.stateList
    private val successState = Success(stateList)
    private val address = "gran via 30 madrid"
    private val addressSuccess = Success(AddressDTOMock.addressDTOList)
    private val parentId = "234325"
    private val fullFirstName = "John"
    private val fullLastName = "Doe"
    private val dateOfBirthDate = "2022-10-04"

    private fun getDateofBirth(): Date {
        val calendar = Calendar.getInstance(Locale.getDefault())
        calendar.set(2022, 9, 4)
        val date = calendar.time
        return date
    }

    private val city = "Madrid"
    private val streetType = "Calle"
    private val streetName = "Gran vía"
    private val streetNumber = "30"
    private val buildingName = ""
    private val zip = "28015"
    private val state = "Madrid"
    private val signature = "32087f52487fgdoeiufgw80347f4"

    private val nonce = "02e41cceec437c211178b7a030c57179"
    private val integrityToken = "eyJhbGciOiJBMjU2S1ciLCJlbmMiOiJBMjU2R0NNIn0.-gJKpdvTcWNrqwx-5wfNOdmTDdAeH9rhwygZL-_jc0jQXaVAc0wuVQ.FlRsQp5QeQ2eYfoJ.tyExMYUR5BOyoB"
    private val operation = "operation"
    private val OTP = "otp"
    private val OTP_RESEND = "otp-resend"
    private val SOFT_REGISTER = "soft-register"
    private val integrityDTOMock = IntegrityDTOMock.integrityDTO
    private val isSMSMarketingChecked = true

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        signupUseCase = SignupUseCase(
            userDataRepository,
            addressRepository,
            countryRepository,
            operationsRepository,
            base64Tool,
        )
    }

    @Test
    fun `when getOriginCountries success countryRepository getOriginCountries is called`() {
        signupUseCase.getOriginCountries()
        verify(countryRepository, times(1)).getOriginCountries()
    }

    @Test
    fun `when getOriginCountries success countryRepository getOriginCountries returns success`() {
        `when`(countryRepository.getOriginCountries()).thenReturn(countriesSuccess)
        val result = signupUseCase.getOriginCountries()
        assertEquals(countriesSuccess, result)
    }

    @Test
    fun `when getOriginCountries failure countryRepository getOriginCountries returns error`() {
        `when`(countryRepository.getOriginCountries()).thenReturn(error)
        val result = signupUseCase.getOriginCountries()
        assertEquals(error, result)
    }

    @Test
    fun `when getCountries success countryRepository getCountries is called`() {
        signupUseCase.getCountries()
        verify(countryRepository, times(1)).getCountries()
    }

    @Test
    fun `when getCountries success countryRepository getCountries returns success`() {
        `when`(countryRepository.getCountries()).thenReturn(countriesSuccess)
        val result = signupUseCase.getCountries()
        assertEquals(countriesSuccess, result)
    }

    @Test
    fun `when getCountries failure countryRepository getCountries returns error`() {
        `when`(countryRepository.getCountries()).thenReturn(error)
        val result = signupUseCase.getCountries()
        assertEquals(error, result)
    }

    @Test
    fun `when registerCredentials success userDataRepository registerCredentials is called`() {
        `when`(operationsRepository.getIntegrityDTO(SOFT_REGISTER)).thenReturn(Success(integrityDTOMock))
        `when`(
            userDataRepository.registerCredentials(
                email,
                countryDTO,
                passwordDTO,
                "state",
                checkMarketing = true,
                checkPrivacy = true,
                checkTerms = true,
                nonce = nonce,
                integrityToken = integrityToken
            ),
        ).thenReturn(success)
        `when`(base64Tool.encode(String(password))).thenReturn(encodedPassword)
        signupUseCase.registerCredentials(
            email,
            countryDTO,
            password,
            "state",
            checkMarketing = true,
            checkPrivacy = true,
            checkTerms = true
        )
        verify(userDataRepository, times(1)).registerCredentials(
            email,
            countryDTO,
            passwordDTO,
            "state",
            checkMarketing = true,
            checkPrivacy = true,
            checkTerms = true,
            nonce = nonce,
            integrityToken = integrityToken
        )
    }

    @Test
    fun `when registerCredentials success userDataRepository registerCredentials returns success`() {
        `when`(operationsRepository.getIntegrityDTO(SOFT_REGISTER)).thenReturn(Success(integrityDTOMock))
        `when`(base64Tool.encode(String(passwordDTO.code))).thenReturn(encodedPassword)
        `when`(
            userDataRepository.registerCredentials(
                email,
                countryDTO,
                PasswordDTO(base64Tool.encode(String(passwordDTO.code)).toCharArray()),
                "state",
                checkMarketing = true,
                checkPrivacy = true,
                checkTerms = true,
                nonce = nonce,
                integrityToken = integrityToken
            ),
        ).thenReturn(success)
        `when`(
            userDataRepository.setPassword(
                PasswordDTO(passwordDTO.code)
            ),
        ).thenReturn(success)
        val result = signupUseCase.registerCredentials(
            email,
            countryDTO,
            passwordDTO.code,
            "state",
            checkMarketing = true,
            checkPrivacy = true,
            checkTerms = true
        )
        assertEquals(success, result)
    }

    @Test
    fun `when registerCredentials failure userDataRepository registerCredentials returns error`() {
        `when`(operationsRepository.getIntegrityDTO(SOFT_REGISTER)).thenReturn(Success(integrityDTOMock))
        `when`(base64Tool.encode(String(passwordDTO.code))).thenReturn(encodedPassword)
        `when`(
            userDataRepository.registerCredentials(
                email,
                countryDTO,
                passwordDTO,
                "state",
                checkMarketing = true,
                checkPrivacy = true,
                checkTerms = true,
                nonce = nonce,
                integrityToken = integrityToken
            ),
        ).thenReturn(error)
        `when`(
            userDataRepository.setPassword(
                PasswordDTO(passwordDTO.code)
            ),
        ).thenReturn(error)
        val result = signupUseCase.registerCredentials(
            email,
            countryDTO,
            passwordDTO.code,
            "state",
            checkMarketing = true,
            checkPrivacy = true,
            checkTerms = true
        )
        assertEquals(error, result)
    }

    @Test
    fun `when getUserData success userDataRepository getUserData is called`() {
        signupUseCase.getUserData()
        verify(userDataRepository, times(1)).getUserData()
    }

    @Test
    fun `when getUserData success userDataRepository getUserData and setLoggedUser returns success`() {
        `when`(userDataRepository.getUserData()).thenReturn(Success(user))
        `when`(userDataRepository.setLoggedUser(user)).thenReturn(Success(true))
        val result = signupUseCase.getUserData()
        assertEquals(Success(user), result)
    }

    @Test
    fun `when getUserData failure userDataRepository getUserData and setLoggedUser returns error`() {
        `when`(userDataRepository.getUserData()).thenReturn(error)
        `when`(userDataRepository.setLoggedUser(user)).thenReturn(error)
        val result = signupUseCase.getUserData()
        assertEquals(error, result)
    }

    @Test
    fun `when verifyCode success operationsRepository validateOTP is called`() {
        signupUseCase.verifyCode(code, operationId)
        verify(operationsRepository, times(1)).validateOTP(code, operationId)
    }

    @Test
    fun `when verifyCode success operationsRepository validateOTP returns success`() {
        `when`(operationsRepository.validateOTP(code, operationId)).thenReturn(success)
        val result = signupUseCase.verifyCode(code, operationId)
        assertEquals(success, result)
    }

    @Test
    fun `when verifyCode failure operationsRepository validateOTP returns error`() {
        `when`(operationsRepository.validateOTP(code, operationId)).thenReturn(error)
        val result = signupUseCase.verifyCode(code, operationId)
        assertEquals(error, result)
    }

    @Test
    fun `when registerPhone success operationsRepository sendSMSOTP is called`() {
        `when`(operationsRepository.getIntegrityDTO(OTP)).thenReturn(Success(integrityDTOMock))
        `when`(operationsRepository.sendSMSOTP(phoneNumber, countryDTO.countryCode, nonce, integrityToken)).thenReturn(successOtpDTO)
        `when`(userDataRepository.getLoggedUser()).thenReturn(successUser)
        signupUseCase.registerPhone(phoneNumber, countryDTO.countryCode, isSMSMarketingChecked)
        verify(operationsRepository, times(1)).sendSMSOTP(phoneNumber, countryDTO.countryCode, nonce, integrityToken)
    }

    @Test
    fun `when registerPhone success operationsRepository sendSMSOTP returns success`() {
        `when`(operationsRepository.getIntegrityDTO(OTP)).thenReturn(Success(integrityDTOMock))
        `when`(operationsRepository.sendSMSOTP(phoneNumber, countryDTO.countryCode, nonce, integrityToken)).thenReturn(successOtpDTO)
        `when`(userDataRepository.getLoggedUser()).thenReturn(successUser)
        val result = signupUseCase.registerPhone(phoneNumber, countryDTO.countryCode, isSMSMarketingChecked)
        assertEquals(successOtpDTO, result)
    }

    @Test
    fun `when registerPhone failure operationsRepository sendSMSOTP returns error`() {
        `when`(operationsRepository.getIntegrityDTO(OTP)).thenReturn(Success(integrityDTOMock))
        `when`(operationsRepository.sendSMSOTP(phoneNumber, countryDTO.countryCode, nonce, integrityToken)).thenReturn(error)
        val result = signupUseCase.registerPhone(phoneNumber, countryDTO.countryCode, isSMSMarketingChecked)
        assertEquals(error, result)
    }

    @Test
    fun `when resendCode success operationsRepository resendSMSOTP is called`() {
        `when`(operationsRepository.getIntegrityDTO(OTP_RESEND)).thenReturn(Success(integrityDTOMock))
        `when`(operationsRepository.resendSMSOTP(operationId, phoneNumber, countryDTO.countryCode, nonce, integrityToken)).thenReturn(successOtpDTO)
        signupUseCase.resendCode(operationId, phoneNumber, countryDTO.countryCode)
        verify(operationsRepository, times(1)).resendSMSOTP(operationId, phoneNumber, countryDTO.countryCode, nonce, integrityToken)
    }

    @Test
    fun `when resendCode success operationsRepository resendSMSOTP returns success`() {
        `when`(operationsRepository.getIntegrityDTO(OTP_RESEND)).thenReturn(Success(integrityDTOMock))
        `when`(operationsRepository.resendSMSOTP(operationId, phoneNumber, countryDTO.countryCode, nonce, integrityToken)).thenReturn(successOtpDTO)
        val result = signupUseCase.resendCode(operationId, phoneNumber, countryDTO.countryCode)
        assertEquals(successOtpDTO, result)
    }

    @Test
    fun `when resendCode failure operationsRepository resendSMSOTP returns error`() {
        `when`(operationsRepository.getIntegrityDTO(OTP_RESEND)).thenReturn(Success(integrityDTOMock))
        `when`(operationsRepository.resendSMSOTP(operationId, phoneNumber, countryDTO.countryCode, nonce, integrityToken)).thenReturn(error)
        val result = signupUseCase.resendCode(operationId, phoneNumber, countryDTO.countryCode)
        assertEquals(error, result)
    }

    @Test
    fun `when userDataRepository getUser failure updateUserPhone returns error`() {
        `when`(userDataRepository.getLoggedUser()).thenReturn(error)
        val result = signupUseCase.updateUserPhone(phoneNumber)
        assertEquals(error, result)
    }

    @Test
    fun `when getUSAStates success countryRepository getStates is called`() {
        `when`(countryRepository.getStates(CountryDTO(iso3 = "USA"))).thenReturn(successState)
        signupUseCase.getUSAStates()
        verify(countryRepository, times(1)).getStates(CountryDTO(iso3 = "USA"))
    }

    @Test
    fun `when getUSAStates success countryRepository getStates returns success`() {
        `when`(countryRepository.getStates(CountryDTO(iso3 = "USA"))).thenReturn(successState)
        val result = signupUseCase.getUSAStates()
        assertEquals(successState, result)
    }

    @Test
    fun `when getUSAStates failure countryRepository getStates returns error`() {
        `when`(countryRepository.getStates(CountryDTO(iso3 = "USA"))).thenReturn(error)
        val result = signupUseCase.getUSAStates()
        assertEquals(error, result)
    }

    @Test
    fun `when searchAddress success addressRepository searchAddressByText is called`() {
        signupUseCase.searchAddress(address)
        verify(addressRepository, times(1)).searchAddressByText(address)
    }

    @Test
    fun `when searchAddress success addressRepository searchAddressByText returns success`() {
        `when`(addressRepository.searchAddressByText(address)).thenReturn(addressSuccess)
        val result = signupUseCase.searchAddress(address)
        assertEquals(addressSuccess, result)
    }

    @Test
    fun `when searchAddress failure addressRepository searchAddressByText returns error`() {
        `when`(addressRepository.searchAddressByText(address)).thenReturn(error)
        val result = signupUseCase.searchAddress(address)
        assertEquals(error, result)
    }

    @Test
    fun `when searchAddressByParentId success addressRepository searchAddressByParentId is called`() {
        signupUseCase.searchAddressByParentId(parentId)
        verify(addressRepository, times(1)).searchAddressByParentId(parentId)
    }

    @Test
    fun `when searchAddressByParentId success addressRepository searchAddressByParentId returns success`() {
        `when`(addressRepository.searchAddressByParentId(parentId)).thenReturn(addressSuccess)
        val result = signupUseCase.searchAddressByParentId(parentId)
        assertEquals(addressSuccess, result)
    }

    @Test
    fun `when searchAddressByParentId failure addressRepository searchAddressByParentId returns error`() {
        `when`(addressRepository.searchAddressByParentId(parentId)).thenReturn(error)
        val result = signupUseCase.searchAddressByParentId(parentId)
        assertEquals(error, result)
    }

    @Test
    fun `when getAddressById success addressRepository getAddressById is called`() {
        signupUseCase.getAddressById(parentId)
        verify(addressRepository, times(1)).getAddressById(parentId)
    }

    @Test
    fun `when getAddressById failure addressRepository getAddressById returns error`() {
        `when`(addressRepository.getAddressById(parentId)).thenReturn(error)
        val result = signupUseCase.getAddressById(parentId)
        assertEquals(error, result)
    }

    @Test
    fun `when getAddressForm success addressRepository getAddressById is called`() {
        signupUseCase.getAddressForm()
        verify(addressRepository, times(1)).getAddressForm()
    }

    @Test
    fun `when getAddressForm failure addressRepository getAddressById returns error`() {
        `when`(addressRepository.getAddressForm()).thenReturn(error)
        val result = signupUseCase.getAddressForm()
        assertEquals(error, result)
    }
    @Test
    fun `when registerUser success userDataRepository registerUser is called`() {
        `when`(
            userDataRepository.registerUser(
                fullFirstName,
                fullLastName,
                dateOfBirthDate,
                city,
                streetType,
                streetName,
                streetNumber,
                buildingName,
                zip,
                state,
                address,
                signature
            )
        ).thenReturn(success)
        signupUseCase.registerUser(
            fullFirstName,
            fullLastName,
            DateFormat.getDateInstance().format(getDateofBirth()),
            city,
            streetType,
            streetName,
            streetNumber,
            buildingName,
            zip,
            state,
            address,
            signature
        )
        verify(userDataRepository, times(1)).registerUser(
            fullFirstName,
            fullLastName,
            dateOfBirthDate,
            city,
            streetType,
            streetName,
            streetNumber,
            buildingName,
            zip,
            state,
            address,
            signature
        )
    }

    @Test
    fun `when registerUser success userDataRepository registerUser returns success`() {
        `when`(
            userDataRepository.registerUser(
                fullFirstName,
                fullLastName,
                dateOfBirthDate,
                city,
                streetType,
                streetName,
                streetNumber,
                buildingName,
                zip,
                state,
                address,
                signature
            )
        ).thenReturn(success)
        val result = signupUseCase.registerUser(
            fullFirstName,
            fullLastName,
            DateFormat.getDateInstance().format(getDateofBirth()),
            city,
            streetType,
            streetName,
            streetNumber,
            buildingName,
            zip,
            state,
            address,
            signature
        )
        assertEquals(success, result)
    }

    @Test
    fun `when registerUser failure userDataRepository registerUser returns error`() {
        `when`(
            userDataRepository.registerUser(
                fullFirstName,
                fullLastName,
                dateOfBirthDate,
                city,
                streetType,
                streetName,
                streetNumber,
                buildingName,
                zip,
                state,
                address,
                signature
            )
        ).thenReturn(error)
        val result = signupUseCase.registerUser(
            fullFirstName,
            fullLastName,
            DateFormat.getDateInstance().format(getDateofBirth()),
            city,
            streetType,
            streetName,
            streetNumber,
            buildingName,
            zip,
            state,
            address,
            signature
        )
        assertEquals(error, result)
    }

    @Test
    fun `when dateOfBirthString parse failure userDataRepository registerUser is called with empty string`() {
        `when`(
            userDataRepository.registerUser(
                fullFirstName,
                fullLastName,
                "",
                city,
                streetType,
                streetName,
                streetNumber,
                buildingName,
                zip,
                state,
                address,
                signature
            )
        ).thenReturn(error)
        val result = signupUseCase.registerUser(
            fullFirstName,
            fullLastName,
            dateOfBirthString = "not valid date",
            city,
            streetType,
            streetName,
            streetNumber,
            buildingName,
            zip,
            state,
            address,
            signature
        )
        assertEquals(error, result)
    }

    @Test
    fun `when getIntegrityDTO success operationsRepository getIntegrityDTO is called`() {
        `when`(operationsRepository.getIntegrityDTO(SOFT_REGISTER)).thenReturn(Success(integrityDTOMock))
        `when`(base64Tool.encode(String(passwordDTO.code))).thenReturn(encodedPassword)
        `when`(
            userDataRepository.registerCredentials(
                email,
                countryDTO,
                passwordDTO,
                "state",
                checkMarketing = true,
                checkPrivacy = true,
                checkTerms = true,
                nonce = nonce,
                integrityToken = integrityToken
            ),
        ).thenReturn(success)
        signupUseCase.registerCredentials(
            email,
            countryDTO,
            passwordDTO.code,
            "state",
            checkMarketing = true,
            checkPrivacy = true,
            checkTerms = true
        )
        verify(operationsRepository, times(1)).getIntegrityDTO(SOFT_REGISTER)
    }

    @Test
    fun `when getIntegrityDTO failure operationsRepository getIntegrityDTO returns error`() {
        `when`(operationsRepository.getIntegrityDTO(SOFT_REGISTER)).thenReturn(error)
        val result = signupUseCase.registerCredentials(
            email,
            countryDTO,
            passwordDTO.code,
            "state",
            checkMarketing = true,
            checkPrivacy = true,
            checkTerms = true
        )
        assertEquals(error, result)
    }
}
