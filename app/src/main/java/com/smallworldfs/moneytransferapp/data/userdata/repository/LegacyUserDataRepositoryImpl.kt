package com.smallworldfs.moneytransferapp.data.userdata.repository

import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.data.common.locale.LocaleRepository
import com.smallworldfs.moneytransferapp.data.login.mappers.UserDTOMapper
import com.smallworldfs.moneytransferapp.data.login.mappers.UserMapperFromDTO
import com.smallworldfs.moneytransferapp.data.userdata.local.UserDataLocalDatasource
import com.smallworldfs.moneytransferapp.data.userdata.model.UserResponseMapper
import com.smallworldfs.moneytransferapp.data.userdata.network.UserDataNetworkDatasource
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.peek
import com.smallworldfs.moneytransferapp.domain.migrated.base.peekFailure
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.PassCodeDTO
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.PasswordDTO
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.QuickLoginSettingsDTO
import com.smallworldfs.moneytransferapp.domain.migrated.signup.model.MarketingPreferenceDTO
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.model.UserStatusDTO
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User
import com.smallworldfs.moneytransferapp.modules.register.domain.model.Form
import rx.Single
import javax.inject.Inject

class LegacyUserDataRepositoryImpl @Inject constructor(
    private val userDataLocalDatasource: UserDataLocalDatasource,
    private val userMapper: UserMapperFromDTO,
    private val localeRepository: LocaleRepository,
    private val userResponseMapper: UserResponseMapper,
    private val userDataNetworkDatasource: UserDataNetworkDatasource,
    private val apiErrorMapper: APIErrorMapper,
    private val userDTOMapper: UserDTOMapper

) : UserDataRepository {
    @Throws(UnsupportedOperationException::class)
    override fun setPassCode(passcode: PassCodeDTO): OperationResult<Boolean, Error> {
        throw UnsupportedOperationException()
    }

    @Throws(UnsupportedOperationException::class)
    override fun getPassCode(): OperationResult<PassCodeDTO, Error> {
        throw UnsupportedOperationException()
    }

    @Throws(UnsupportedOperationException::class)
    override fun deletePassCode(): OperationResult<Boolean, Error> {
        throw UnsupportedOperationException()
    }

    @Throws(UnsupportedOperationException::class)
    override fun setPassword(passwordDTO: PasswordDTO): OperationResult<Boolean, Error> {
        throw UnsupportedOperationException()
    }

    @Throws(UnsupportedOperationException::class)
    override fun getPassword(): OperationResult<PasswordDTO, Error> {
        throw UnsupportedOperationException()
    }

    @Throws(UnsupportedOperationException::class)
    override fun deletePassword(): OperationResult<Boolean, Error> {
        throw UnsupportedOperationException()
    }

    override fun setLoggedUser(userDTO: UserDTO): OperationResult<Boolean, Error> {
        return userDataLocalDatasource.saveUser(userDTO)
    }

    override fun getLoggedUser(): OperationResult<UserDTO, Error> {
        return userDataLocalDatasource.retrieveUser()
    }

    @Throws(UnsupportedOperationException::class)
    override fun deleteLoggedUser(): OperationResult<Boolean, Error> {
        throw UnsupportedOperationException()
    }

    override fun retrieveUser(): User? {
        getLoggedUser()
            .mapFailure {
                return null
            }
            .map {
                return userMapper.mapFromUserDTO(it)
            }
        return null
    }

    override fun putUser(user: User?) {
        userDTOMapper.mapOrNull(user)?.let {
            setLoggedUser(it)
        }
    }

    @Throws(UnsupportedOperationException::class)
    override fun clearUserData() {
        throw UnsupportedOperationException()
    }

    @Throws(UnsupportedOperationException::class)
    override fun getUserStatus(): OperationResult<UserStatusDTO, Error> {
        throw UnsupportedOperationException()
    }

    @Throws(UnsupportedOperationException::class)
    override fun getUserData(): OperationResult<UserDTO, Error> {
        throw UnsupportedOperationException()
    }

    override fun getUseStatusLegacy(): Single<UserDTO?> {
        return Single.create { emitter ->
            getLoggedUser().peek {
                userDataNetworkDatasource.getUser(
                    localeRepository.getLang(), it.uuid, it.userToken,
                ).peek {
                    emitter.onSuccess(userResponseMapper.map(it))
                }.peekFailure {
                    emitter.onError(Exception(it.message))
                }
            }.peekFailure {
                emitter.onError(Exception(""))
            }
        }
    }

    @Throws(UnsupportedOperationException::class)
    override fun isWelcomeShown(): Boolean {
        throw UnsupportedOperationException()
    }

    @Throws(UnsupportedOperationException::class)
    override fun setWelcomeShown(shown: Boolean) {
        throw UnsupportedOperationException()
    }

    @Throws(UnsupportedOperationException::class)
    override fun isUploadDialogShown(): Boolean {
        throw UnsupportedOperationException()
    }

    @Throws(UnsupportedOperationException::class)
    override fun setUploadDialogShown(shown: Boolean) {
        throw UnsupportedOperationException()
    }

    @Throws(UnsupportedOperationException::class)
    override fun login(
        email: String,
        passwordDTO: PasswordDTO,
        country: String
    ): OperationResult<UserDTO, Error> {
        throw UnsupportedOperationException()
    }

    @Throws(UnsupportedOperationException::class)
    override fun getLimitedLogin(originCountry: CountryDTO, destinationCountry: CountryDTO): OperationResult<UserDTO, Error> {
        throw UnsupportedOperationException()
    }

    @Throws(UnsupportedOperationException::class)
    override fun checkPassword(
        email: String,
        passwordDTO: PasswordDTO,
        country: String
    ): OperationResult<Boolean, Error> {
        throw UnsupportedOperationException()
    }

    @Throws(UnsupportedOperationException::class)
    override fun registerCredentials(email: String, countryOrigin: CountryDTO, password: PasswordDTO, state: String, checkMarketing: Boolean, checkPrivacy: Boolean, checkTerms: Boolean, nonce: String, integrityToken: String): OperationResult<Boolean, Error> {
        throw UnsupportedOperationException()
    }

    @Throws(UnsupportedOperationException::class)
    override fun registerUser(
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
    ): OperationResult<Boolean, Error> {
        throw UnsupportedOperationException()
    }

    @Throws(UnsupportedOperationException::class)
    override fun logout(): OperationResult<Boolean, Error> {
        throw UnsupportedOperationException()
    }

    override fun getQuickLoginSettings(): OperationResult<QuickLoginSettingsDTO, Error> {
        throw UnsupportedOperationException()
    }

    override fun saveQuickLoginSettings(settingsInfo: QuickLoginSettingsDTO): OperationResult<QuickLoginSettingsDTO, Error> {
        throw UnsupportedOperationException()
    }

    override fun deleteQuickLogin() {
        throw UnsupportedOperationException()
    }

    override fun requestMarketingPreferences(fromView: String): OperationResult<Form, Error> {
        return userDataLocalDatasource.retrieveUser()
            .map { user ->
                return userDataNetworkDatasource.requestMarketingPreferences(
                    user.userToken,
                    user.id,
                    fromView,
                ).map { formSettings ->
                    return Success(formSettings.form ?: Form())
                }.mapFailure { apiError ->
                    return Failure(apiErrorMapper.map(apiError))
                }
            }.mapFailure {
                return Failure(it)
            }
    }

    override fun updateMarketingPreferences(marketingPreferencesDTO: MarketingPreferenceDTO): OperationResult<Unit, Error> {
        throw UnsupportedOperationException()
    }

    @Throws(UnsupportedOperationException::class)
    override fun setSMSConsentShown() {
        throw UnsupportedOperationException()
    }

    @Throws(UnsupportedOperationException::class)
    override fun getSMSConsentShown(): Boolean {
        throw UnsupportedOperationException()
    }

    @Throws(UnsupportedOperationException::class)
    override fun getLastPrimeForPushEventTimestamp(): Long {
        throw UnsupportedOperationException()
    }

    @Throws(UnsupportedOperationException::class)
    override fun setLastPrimeForPushEventTimestamp(timestamp: Long) {
        throw UnsupportedOperationException()
    }
}
