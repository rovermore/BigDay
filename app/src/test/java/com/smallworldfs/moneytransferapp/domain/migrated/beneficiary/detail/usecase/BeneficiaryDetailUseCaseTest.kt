package com.smallworldfs.moneytransferapp.domain.migrated.beneficiary.detail.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.detail.usecase.BeneficiaryDetailUseCase
import com.smallworldfs.moneytransferapp.domain.migrated.account.beneficiary.repository.BeneficiaryRepository
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.mocks.dto.BeneficiaryActivityDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class BeneficiaryDetailUseCaseTest {

    @Mock
    lateinit var userDataRepository: UserDataRepository

    @Mock
    lateinit var beneficiaryRepository: BeneficiaryRepository

    lateinit var beneficiaryDetailUseCase: BeneficiaryDetailUseCase

    private val user = UserDTOMock.userDTO

    private val beneficiaryActivityDTO = BeneficiaryActivityDTOMock.beneficiaryActivityDTO
    private val beneficiaryActivityDTOSuccess = Success(beneficiaryActivityDTO)
    private val error = Failure(Error.UncompletedOperation("Could not complete operation"))

    private val beneficiaryId = "101"

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        beneficiaryDetailUseCase = BeneficiaryDetailUseCase(
            beneficiaryRepository,
            userDataRepository,
        )
    }

    @Test
    fun `when deleteBeneficiary success beneficiaryRepository deleteBeneficiary is called`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(Success(user))
        Mockito.`when`(
            beneficiaryRepository.deleteBeneficiary(
                user.userToken,
                user.id,
                beneficiaryId
            )
        ).thenReturn(Success(true))
        beneficiaryDetailUseCase.deleteBeneficiary(beneficiaryId)
        Mockito.verify(beneficiaryRepository, Mockito.times(1)).deleteBeneficiary(
            user.userToken,
            user.id,
            beneficiaryId
        )
    }

    @Test
    fun `when deleteBeneficiary success beneficiaryRepository deleteBeneficiary returns success`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(Success(user))
        Mockito.`when`(
            beneficiaryRepository.deleteBeneficiary(
                user.userToken,
                user.id,
                beneficiaryId
            )
        ).thenReturn(Success(true))
        val result = beneficiaryDetailUseCase.deleteBeneficiary(beneficiaryId)
        Assert.assertEquals(Success(true), result)
    }

    @Test
    fun `when deleteBeneficiary failure beneficiaryRepository deleteBeneficiary returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(Success(user))
        Mockito.`when`(
            beneficiaryRepository.deleteBeneficiary(
                user.userToken,
                user.id,
                beneficiaryId
            )
        ).thenReturn(error)
        val result = beneficiaryDetailUseCase.deleteBeneficiary(beneficiaryId)
        Assert.assertEquals(error, result)
    }

    @Test
    fun `when getBeneficiaryActivity success beneficiaryRepository getBeneficiaryActivity is called`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(Success(user))
        Mockito.`when`(
            beneficiaryRepository.getBeneficiaryActivity(
                user.userToken,
                user.id,
                beneficiaryId
            )
        ).thenReturn(beneficiaryActivityDTOSuccess)
        beneficiaryDetailUseCase.getBeneficiaryActivity(beneficiaryId)
        Mockito.verify(beneficiaryRepository, Mockito.times(1)).getBeneficiaryActivity(
            user.userToken,
            user.id,
            beneficiaryId
        )
    }

    @Test
    fun `when getBeneficiaryActivity success beneficiaryRepository getBeneficiaryActivity returns success`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(Success(user))
        Mockito.`when`(
            beneficiaryRepository.getBeneficiaryActivity(
                user.userToken,
                user.id,
                beneficiaryId
            )
        ).thenReturn(beneficiaryActivityDTOSuccess)
        val result = beneficiaryDetailUseCase.getBeneficiaryActivity(beneficiaryId)
        Assert.assertEquals(beneficiaryActivityDTOSuccess, result)
    }

    @Test
    fun `when getBeneficiaryActivity failure beneficiaryRepository getBeneficiaryActivity returns error`() {
        Mockito.`when`(userDataRepository.getLoggedUser()).thenReturn(Success(user))
        Mockito.`when`(
            beneficiaryRepository.getBeneficiaryActivity(
                user.userToken,
                user.id,
                beneficiaryId
            )
        ).thenReturn(error)
        val result = beneficiaryDetailUseCase.getBeneficiaryActivity(beneficiaryId)
        Assert.assertEquals(error, result)
    }
}
