package com.smallworldfs.moneytransferapp.domain.migrated.beneficiaries.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.list.model.BeneficiaryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.list.usecase.BeneficiaryListUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.repository.BeneficiaryRepository
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.calculator.repository.CalculatorRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import junit.framework.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class GetBeneficiaryListUseCaseTest {

    @Mock
    lateinit var beneficiaryRepository: BeneficiaryRepository

    @Mock
    lateinit var userDataRepository: UserDataRepository

    @Mock
    lateinit var calculatorRepository: CalculatorRepository

    private lateinit var getBeneficiaryListUseCase: BeneficiaryListUseCase

    private val successUser = Success(UserDTOMock.userDTO)
    private val successBeneficiaries = Success(listOf<BeneficiaryDTO>())
    private val error = Failure(Error.UncompletedOperation("Uncompleted operation"))

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        getBeneficiaryListUseCase = BeneficiaryListUseCase(
            beneficiaryRepository,
            calculatorRepository,
            userDataRepository,
        )
    }

    @Test
    fun `when getBeneficiaryList success userDataRepository getUser is called`() {
        `when`(userDataRepository.getLoggedUser()).thenReturn(successUser)
        getBeneficiaryListUseCase.getBeneficiaryList("", "", "")
        verify(userDataRepository, times(1)).getLoggedUser()
    }

    @Test
    fun `when getBeneficiaryList success userDataRepository getUser returns success`() {
        `when`(userDataRepository.getLoggedUser()).thenReturn(successUser)
        `when`(beneficiaryRepository.requestBeneficiaryList("", "", "", UserDTOMock.userDTO.id, UserDTOMock.userDTO.userToken)).thenReturn(successBeneficiaries)
        val result = getBeneficiaryListUseCase.getBeneficiaryList("", "", "")
        assertEquals(successBeneficiaries, result)
    }

    @Test
    fun `when getBeneficiaryList failure userDataRepository getUser returns error`() {
        `when`(userDataRepository.getLoggedUser()).thenReturn(error)
        `when`(beneficiaryRepository.requestBeneficiaryList("", "", "", UserDTOMock.userDTO.id, UserDTOMock.userDTO.userToken)).thenReturn(error)
        val result = getBeneficiaryListUseCase.getBeneficiaryList("", "", "")
        assertEquals(error, result)
    }

    @Test
    fun `when getBeneficiaryList success beneficiaryRepository requestBeneficiaryList is called`() {
        `when`(userDataRepository.getLoggedUser()).thenReturn(successUser)
        `when`(beneficiaryRepository.requestBeneficiaryList("", "", "", UserDTOMock.userDTO.id, UserDTOMock.userDTO.userToken)).thenReturn(successBeneficiaries)
        getBeneficiaryListUseCase.getBeneficiaryList("", "", "")
        verify(beneficiaryRepository, times(1)).requestBeneficiaryList("", "", "", UserDTOMock.userDTO.id, UserDTOMock.userDTO.userToken)
    }

    @Test
    fun `when getBeneficiaryList success beneficiaryRepository requestBeneficiaryList returns success`() {
        `when`(userDataRepository.getLoggedUser()).thenReturn(successUser)
        `when`(beneficiaryRepository.requestBeneficiaryList("", "", "", UserDTOMock.userDTO.id, UserDTOMock.userDTO.userToken)).thenReturn(successBeneficiaries)
        val result = getBeneficiaryListUseCase.getBeneficiaryList("", "", "")
        assertEquals(successBeneficiaries, result)
    }

    @Test
    fun `when getBeneficiaryList failure beneficiaryRepository requestBeneficiaryList returns error`() {
        `when`(userDataRepository.getLoggedUser()).thenReturn(successUser)
        `when`(beneficiaryRepository.requestBeneficiaryList("", "", "", UserDTOMock.userDTO.id, UserDTOMock.userDTO.userToken)).thenReturn(error)
        val result = getBeneficiaryListUseCase.getBeneficiaryList("", "", "")
        assertEquals(error, result)
    }
}
