package com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.PassCodeDTO
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.PasswordDTO
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.QuickLoginSettingsDTO
import com.smallworldfs.moneytransferapp.domain.migrated.signup.model.MarketingPreferenceDTO
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.model.UserStatusDTO
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User
import com.smallworldfs.moneytransferapp.modules.register.domain.model.Form
import rx.Single

interface UserDataRepository {

    /**
     * Pass Code
     */
    fun setPassCode(passcode: PassCodeDTO): OperationResult<Boolean, Error>

    fun getPassCode(): OperationResult<PassCodeDTO, Error>

    fun deletePassCode(): OperationResult<Boolean, Error>

    /**
     * Password
     */
    fun setPassword(passwordDTO: PasswordDTO): OperationResult<Boolean, Error>

    fun getPassword(): OperationResult<PasswordDTO, Error>

    fun deletePassword(): OperationResult<Boolean, Error>

    /**
     * User
     */
    fun setLoggedUser(userDTO: UserDTO): OperationResult<Boolean, Error>

    fun getLoggedUser(): OperationResult<UserDTO, Error>

    fun deleteLoggedUser(): OperationResult<Boolean, Error>

    fun retrieveUser(): User?

    fun putUser(user: User?)

    fun clearUserData()

    /**
     * Fingerprint
     */

    fun getUserStatus(): OperationResult<UserStatusDTO, Error>

    fun getUserData(): OperationResult<UserDTO, Error>

    @Deprecated("Only for use in Java")
    fun getUseStatusLegacy(): Single<UserDTO?>

    /**
     * Limited user
     */
    fun isWelcomeShown(): Boolean

    fun setWelcomeShown(shown: Boolean)

    fun isUploadDialogShown(): Boolean

    fun setUploadDialogShown(shown: Boolean)

    /**
     * Login
     */

    fun login(email: String, passwordDTO: PasswordDTO, country: String): OperationResult<UserDTO, Error>

    fun getLimitedLogin(originCountry: CountryDTO, destinationCountry: CountryDTO): OperationResult<UserDTO, Error>

    fun checkPassword(email: String, passwordDTO: PasswordDTO, country: String): OperationResult<Boolean, Error>

    fun registerCredentials(
        email: String,
        countryOrigin: CountryDTO,
        password: PasswordDTO,
        state: String,
        checkMarketing: Boolean,
        checkPrivacy: Boolean,
        checkTerms: Boolean,
        nonce: String,
        integrityToken: String
    ): OperationResult<Boolean, Error>

    fun registerUser(
        fullFirstName: String,
        fullLastName: String,
        dateOfBirth: String,
        city: String,
        streetType: String,
        streetName: String,
        streetNumber: String,
        buildingName: String,
        zip: String,
        state: String,
        address: String,
        signature: String
    ): OperationResult<Boolean, Error>

    fun logout(): OperationResult<Boolean, Error>
    fun getQuickLoginSettings(): OperationResult<QuickLoginSettingsDTO, Error>
    fun saveQuickLoginSettings(settingsInfo: QuickLoginSettingsDTO): OperationResult<QuickLoginSettingsDTO, Error>
    fun deleteQuickLogin()
    fun requestMarketingPreferences(fromView: String): OperationResult<Form, Error>
    fun updateMarketingPreferences(marketingPreferencesDTO: MarketingPreferenceDTO): OperationResult<Unit, Error>
    fun setSMSConsentShown()
    fun getSMSConsentShown(): Boolean
    fun getLastPrimeForPushEventTimestamp(): Long
    fun setLastPrimeForPushEventTimestamp(timestamp: Long)
}
