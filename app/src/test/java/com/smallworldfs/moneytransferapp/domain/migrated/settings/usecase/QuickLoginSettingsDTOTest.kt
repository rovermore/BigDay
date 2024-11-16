package com.smallworldfs.moneytransferapp.domain.migrated.settings.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.QuickLoginSettingsDTO
import org.junit.Assert
import org.junit.Test

class QuickLoginSettingsDTOTest {

    @Test
    fun `(Biometrics Available) given a fully deactivate device, biometrics can be activate`() {
        val quickLoginSettingsDTO = QuickLoginSettingsDTO(true, false, false)
        val expectedResult = Success(quickLoginSettingsDTO)

        val result = quickLoginSettingsDTO.activateBiometrics()

        Assert.assertEquals(expectedResult, result)
        Assert.assertEquals(quickLoginSettingsDTO.state::class, QuickLoginSettingsDTO.BiometricsActive::class)
    }

    @Test
    fun `(Biometrics Available) given a biometrics activate device, passcode can be activate`() {
        val quickLoginSettingsDTO = QuickLoginSettingsDTO(true, true, false)
        val expectedResult = Success(quickLoginSettingsDTO)

        val result = quickLoginSettingsDTO.activatePassCode()

        Assert.assertEquals(expectedResult, result)
        Assert.assertEquals(quickLoginSettingsDTO.state::class, QuickLoginSettingsDTO.FullyActiveBiometricDevice::class)
    }

    @Test
    fun `(Biometrics Available) given a biometrics activate device, deactivating biometrics results in FullyDeactiveDevice`() {
        val quickLoginSettingsDTO = QuickLoginSettingsDTO(true, true, false)
        val expectedResult = Success(quickLoginSettingsDTO)

        val result = quickLoginSettingsDTO.deactivateBiometrics()

        Assert.assertEquals(expectedResult, result)
        Assert.assertEquals(quickLoginSettingsDTO.state::class, QuickLoginSettingsDTO.FullyDeactivatedBiometricDevice::class)
    }

    @Test
    fun `(Biometrics Available) given a fully deactivate device, passcode can be activate`() {
        val quickLoginSettingsDTO = QuickLoginSettingsDTO(true, false, false)
        val expectedResult = Success(quickLoginSettingsDTO)

        val result = quickLoginSettingsDTO.activatePassCode()

        Assert.assertEquals(expectedResult, result)
        Assert.assertEquals(quickLoginSettingsDTO.state::class, QuickLoginSettingsDTO.PassCodeActive::class)
    }

    @Test
    fun `(Biometrics Available) given a passcode activate device, biometrics can be activate`() {
        val quickLoginSettingsDTO = QuickLoginSettingsDTO(true, false, true)
        val expectedResult = Success(quickLoginSettingsDTO)

        val result = quickLoginSettingsDTO.activateBiometrics()

        Assert.assertEquals(expectedResult, result)
        Assert.assertEquals(quickLoginSettingsDTO.state::class, QuickLoginSettingsDTO.FullyActiveBiometricDevice::class)
    }

    @Test
    fun `(Biometrics Available) given a fully activate device, biometrics can be deactivate`() {
        val quickLoginSettingsDTO = QuickLoginSettingsDTO(true, true, true)
        val expectedResult = Success(quickLoginSettingsDTO)

        val result = quickLoginSettingsDTO.deactivateBiometrics()

        Assert.assertEquals(expectedResult, result)
        Assert.assertEquals(quickLoginSettingsDTO.state::class, QuickLoginSettingsDTO.FullyDeactivatedBiometricDevice::class)
    }

    @Test
    fun `(Biometrics Available) given a fully activate device, passcode can't be deactivate`() {
        val quickLoginSettingsDTO = QuickLoginSettingsDTO(true, true, true)
        val expectedResult = Failure(Error.UnsupportedOperation::class)

        val result = quickLoginSettingsDTO.deactivatePassCode()

        Assert.assertEquals(expectedResult::class, result::class)
        Assert.assertEquals(quickLoginSettingsDTO.state::class, QuickLoginSettingsDTO.FullyActiveBiometricDevice::class)
    }

    @Test
    fun `(Biometrics Unavailable) given a fully deactivate device, passcode can be activate`() {
        val quickLoginSettingsDTO = QuickLoginSettingsDTO(false, false, false)
        val expectedResult = Success(quickLoginSettingsDTO)

        val result = quickLoginSettingsDTO.activatePassCode()

        Assert.assertEquals(expectedResult, result)
        Assert.assertEquals(quickLoginSettingsDTO.state::class, QuickLoginSettingsDTO.FullyActiveDevice::class)
    }

    @Test
    fun `(Biometrics Unavailable) given a fully activate device, passcode can be deactivate`() {
        val quickLoginSettingsDTO = QuickLoginSettingsDTO(false, false, true)
        val expectedResult = Success(quickLoginSettingsDTO)

        val result = quickLoginSettingsDTO.deactivatePassCode()

        Assert.assertEquals(expectedResult, result)
        Assert.assertEquals(quickLoginSettingsDTO.state::class, QuickLoginSettingsDTO.FullyDeactiveDevice::class)
    }
}
