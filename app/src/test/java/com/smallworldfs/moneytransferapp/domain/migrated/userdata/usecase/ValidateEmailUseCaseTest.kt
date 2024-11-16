package com.smallworldfs.moneytransferapp.domain.migrated.userdata.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.operations.repository.OperationsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.mocks.dto.IntegrityDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.OtpDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class ValidateEmailUseCaseTest {

    @Mock
    private lateinit var userDataRepository: UserDataRepository

    @Mock
    private lateinit var operationsRepository: OperationsRepository

    private lateinit var validateEmailUseCase: ValidateEmailUseCase

    private val success = Success(OtpDTOMock.otpDTO)
    private val error = Failure(Error.UncompletedOperation("Uncompleted operation"))
    private val email = UserDTOMock.userDTO.email
    private val successEmail = Success(email)

    private val nonce = "02e41cceec437c211178b7a030c57179"
    private val integrityToken = "eyJhbGciOiJBMjU2S1ciLCJlbmMiOiJBMjU2R0NNIn0.-gJKpdvTcWNrqwx-5wfNOdmTDdAeH9rhwygZL-_jc0jQXaVAc0wuVQ.FlRsQp5QeQ2eYfoJ.tyExMYUR5BOyoB"
    private val OTP = "otp"
    private val integrityDTOMock = IntegrityDTOMock.integrityDTO

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        validateEmailUseCase = ValidateEmailUseCase(
            userDataRepository,
            operationsRepository
        )
    }

    @Test
    fun `when sendEmail success operationsRepository sendEmailOTP is called`() {
        `when`(operationsRepository.getIntegrityDTO(OTP)).thenReturn(Success(integrityDTOMock))
        validateEmailUseCase.sendEmail()
        verify(operationsRepository, times(1)).sendEmailOTP(nonce, integrityToken)
    }

    @Test
    fun `when sendEmail success operationsRepository sendEmailOTP returns success`() {
        `when`(operationsRepository.getIntegrityDTO(OTP)).thenReturn(Success(integrityDTOMock))
        `when`(operationsRepository.sendEmailOTP(nonce, integrityToken)).thenReturn(success)
        val result = validateEmailUseCase.sendEmail()
        assertEquals(success, result)
    }

    @Test
    fun `when sendEmail failure operationsRepository sendEmailOTP returns error`() {
        `when`(operationsRepository.getIntegrityDTO(OTP)).thenReturn(error)
        `when`(operationsRepository.sendEmailOTP(nonce, integrityToken)).thenReturn(error)
        val result = validateEmailUseCase.sendEmail()
        assertEquals(error, result)
    }

    @Test
    fun `when getUserEmail success userDataRepository getUser is called`() {
        `when`(userDataRepository.getLoggedUser()).thenReturn(Success(UserDTOMock.userDTO))
        validateEmailUseCase.getUserEmail()
        verify(userDataRepository, times(1)).getLoggedUser()
    }

    @Test
    fun `when getUserEmail success userDataRepository getUser returns success`() {
        `when`(userDataRepository.getLoggedUser()).thenReturn(Success(UserDTOMock.userDTO))
        val result = validateEmailUseCase.getUserEmail()
        assertEquals(successEmail, result)
    }

    @Test
    fun `when getUserEmail failure userDataRepository getUser returns error`() {
        `when`(userDataRepository.getLoggedUser()).thenReturn(error)
        val result = validateEmailUseCase.getUserEmail()
        assertEquals(error, result)
    }
}
