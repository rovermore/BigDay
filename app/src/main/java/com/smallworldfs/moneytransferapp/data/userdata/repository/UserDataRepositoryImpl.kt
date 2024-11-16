package com.smallworldfs.moneytransferapp.data.userdata.repository

import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.data.braze.BrazeDatasource
import com.smallworldfs.moneytransferapp.data.common.locale.LocaleRepository
import com.smallworldfs.moneytransferapp.data.common.preferences.repository.local.PreferencesLocal
import com.smallworldfs.moneytransferapp.data.login.mappers.LimitedUserMapper
import com.smallworldfs.moneytransferapp.data.login.mappers.LoginResponseMapper
import com.smallworldfs.moneytransferapp.data.login.mappers.UserDTOMapper
import com.smallworldfs.moneytransferapp.data.login.mappers.UserMapperFromDTO
import com.smallworldfs.moneytransferapp.data.oauth.repository.local.OAuthLocal
import com.smallworldfs.moneytransferapp.data.userdata.local.UserDataLocalDatasource
import com.smallworldfs.moneytransferapp.data.userdata.model.SaveMarketingPreferencesRequestMapper
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
import com.smallworldfs.moneytransferapp.domain.migrated.base.zipFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.zipSuccess
import com.smallworldfs.moneytransferapp.domain.migrated.capabilities.CapabilityChecker
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountriesDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.PassCodeDTO
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.PasswordDTO
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.QuickLoginSettingsDTO
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.QuickLoginSettingsMapper
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.QuickLoginSettingsModel
import com.smallworldfs.moneytransferapp.domain.migrated.signup.model.MarketingPreference
import com.smallworldfs.moneytransferapp.domain.migrated.signup.model.MarketingPreferenceDTO
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.model.UserStatusDTO
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.repository.BeneficiaryRepository
import com.smallworldfs.moneytransferapp.modules.calculator.domain.interactor.implementation.CalculatorInteractorImpl
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User
import com.smallworldfs.moneytransferapp.modules.promotions.domain.repository.PromotionsRepository
import com.smallworldfs.moneytransferapp.modules.register.domain.model.Form
import com.smallworldfs.moneytransferapp.utils.parseJSON
import com.smallworldfs.moneytransferapp.utils.toJSONString
import rx.Single
import javax.inject.Inject

class UserDataRepositoryImpl @Inject constructor(
    private val capabilityChecker: CapabilityChecker,
    private val userDataLocalDatasource: UserDataLocalDatasource,
    private val preferencesLocal: PreferencesLocal,
    private val userMapper: UserMapperFromDTO,
    private val userDTOMapper: UserDTOMapper,
    private val userDataNetworkDatasource: UserDataNetworkDatasource,
    private val localeRepository: LocaleRepository,
    private val userStatusMapper: UserResponseMapper,
    private val apiErrorMapper: APIErrorMapper,
    private val limitedUserMapper: LimitedUserMapper,
    private val userResponseMapper: UserResponseMapper,
    private val loginResponseMapper: LoginResponseMapper,
    private val quickLoginSettingsMapper: QuickLoginSettingsMapper,
    private val oAuthLocal: OAuthLocal,
    private val saveMarketingPreferencesRequestMapper: SaveMarketingPreferencesRequestMapper,
    private val brazeDatasource: BrazeDatasource
) : UserDataRepository {

    override fun getQuickLoginSettings(): OperationResult<QuickLoginSettingsDTO, Error> {
        return preferencesLocal.getQuickLoginSettings().parseJSON<QuickLoginSettingsModel>().map {
            val biometricsAvailable = capabilityChecker.hasBiometricCapability()
            val settings = QuickLoginSettingsDTO(biometricsAvailable, it.biometricsActive, it.passCodeActive)
            saveQuickLoginSettings(settings)
            return Success(settings)
        }
    }

    override fun saveQuickLoginSettings(settingsDTO: QuickLoginSettingsDTO): OperationResult<QuickLoginSettingsDTO, Error> {
        val settingsInfo = quickLoginSettingsMapper.map(settingsDTO)
        return settingsInfo.toJSONString().map {
            preferencesLocal.setQuickLoginSettings(it)
            return Success(settingsDTO)
        }
    }

    override fun setPassCode(passcode: PassCodeDTO): OperationResult<Boolean, Error> {
        return userDataLocalDatasource.savePassCode(passcode).peek {
            return Success(true)
        }
    }

    override fun getPassCode(): OperationResult<PassCodeDTO, Error> = userDataLocalDatasource.retrievePassCode()

    override fun deletePassCode() = userDataLocalDatasource.removePassCode()

    /**
     * Password
     */
    override fun setPassword(passwordDTO: PasswordDTO): OperationResult<Boolean, Error> {
        return userDataLocalDatasource.savePassword(passwordDTO)
    }

    override fun getPassword(): OperationResult<PasswordDTO, Error> {
        return userDataLocalDatasource.retrievePassword()
    }

    override fun deletePassword(): OperationResult<Boolean, Error> {
        return userDataLocalDatasource.removePassword()
    }

    /**
     * User
     */
    override fun setLoggedUser(userDTO: UserDTO): OperationResult<Boolean, Error> {
        return userDataLocalDatasource.saveUser(userDTO)
    }

    override fun getLoggedUser(): OperationResult<UserDTO, Error> {
        return userDataLocalDatasource.retrieveUser()
    }

    override fun deleteLoggedUser(): OperationResult<Boolean, Error> {
        return userDataLocalDatasource.removeUser()
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
        userDTOMapper.mapOrNull(user)?.let { setLoggedUser(it) }
    }

    override fun clearUserData() {
        oAuthLocal.clearPersistedOAuthToken()
        deletePassword()
        deleteLoggedUser()
        deleteQuickLogin()
        CalculatorInteractorImpl.getInstance().destroy()
        PromotionsRepository.getInstance().destroy()
        if (BeneficiaryRepository.getInstance().cachedBeneficiaries != null) {
            BeneficiaryRepository.getInstance().cachedBeneficiaries.clear()
        }
    }

    override fun deleteQuickLogin() {
        saveQuickLoginSettings(QuickLoginSettingsDTO(capabilityChecker.hasBiometricCapability(), false, false))
    }

    /**
     * User from Network
     */
    override fun getUserStatus(): OperationResult<UserStatusDTO, Error> {
        return getUserData()
            .map {
                setLoggedUser(it)
                return Success(userStatusMapper.mapStatus(it.status))
            }.mapFailure {
                return Failure(it)
            }
    }

    override fun getUserData(): OperationResult<UserDTO, Error> {
        return getLoggedUser()
            .map { userDTO ->
                return userDataNetworkDatasource.getUser(localeRepository.getLang(), userDTO.uuid, userDTO.userToken)
                    .map {
                        return Success(userResponseMapper.map(it))
                    }.mapFailure {
                        return Failure(apiErrorMapper.map(it))
                    }
            }.mapFailure {
                return Failure(Error.OperationCompletedWithError("No status found"))
            }
    }

    @Throws(UnsupportedOperationException::class)
    override fun getUseStatusLegacy(): Single<UserDTO?> {
        throw UnsupportedOperationException()
    }

    override fun isWelcomeShown(): Boolean {
        return userDataLocalDatasource.isWelcomeShow()
    }

    override fun setWelcomeShown(shown: Boolean) {
        return userDataLocalDatasource.setWelcomeShown(shown)
    }

    override fun isUploadDialogShown(): Boolean {
        return userDataLocalDatasource.isUploadDialogShown()
    }

    override fun setUploadDialogShown(shown: Boolean) {
        return userDataLocalDatasource.setUploadDialogShown(shown)
    }

    /**
     * Login
     */

    override fun login(email: String, passwordDTO: PasswordDTO, country: String): OperationResult<UserDTO, Error> {
        return userDataNetworkDatasource.requestLogin(localeRepository.getLang(), email, passwordDTO, country)
            .mapFailure {
                apiErrorMapper.map(it)
            }.map {
                return loginResponseMapper.map(it)
                    .map { loginUserDTO ->
                        setLoggedUser(loginUserDTO)
                        brazeDatasource.changeUser(loginUserDTO.uuid, loginUserDTO.email, loginUserDTO.getCountry())
                        return Success(loginUserDTO)
                    }.mapFailure {
                        return Failure(it)
                    }
            }
    }

    override fun getLimitedLogin(originCountry: CountryDTO, destinationCountry: CountryDTO): OperationResult<UserDTO, Error> {
        return getLoggedUser()
            .map { user ->
                return userDataNetworkDatasource.requestLimitedLogin(localeRepository.getLang(), user.appToken, originCountry.iso3, destinationCountry.iso3)
                    .mapFailure {
                        return Failure(apiErrorMapper.map(it))
                    }.map {
                        return limitedUserMapper.map(it).peek { user ->
                            setLoggedUser(user)
                            brazeDatasource.changeUser(uuid = user.uuid, country = originCountry.iso3)
                        }
                    }
            }.mapFailure {
                return userDataNetworkDatasource.requestLimitedLogin(lang = localeRepository.getLang(), originCountry = originCountry.iso3, destinationCountry = destinationCountry.iso3)
                    .map {
                        return limitedUserMapper.map(it).peek { user ->
                            setLoggedUser(user)
                        }
                    }
                    .mapFailure {
                        return Failure(apiErrorMapper.map(it))
                    }
            }
    }

    override fun checkPassword(
        email: String,
        passwordDTO: PasswordDTO,
        country: String
    ): OperationResult<Boolean, Error> {
        return userDataNetworkDatasource.requestLogin(localeRepository.getLang(), email, passwordDTO, country)
            .mapFailure {
                passwordDTO.release()
                return Failure(apiErrorMapper.map(it))
            }.map {
                passwordDTO.release()
                return Success(true)
            }
    }

    override fun registerCredentials(
        email: String,
        countryOrigin: CountryDTO,
        password: PasswordDTO,
        state: String,
        checkMarketing: Boolean,
        checkPrivacy: Boolean,
        checkTerms: Boolean,
        nonce: String,
        integrityToken: String
    ): OperationResult<Boolean, Error> {
        return getLoggedUser()
            .map { user ->
                return userDataNetworkDatasource.registerCredentials(
                    localeRepository.getLang(),
                    user.appToken,
                    user.uuid,
                    email,
                    countryOrigin,
                    password,
                    state,
                    checkMarketing,
                    checkPrivacy,
                    checkTerms,
                    nonce,
                    integrityToken,
                ).mapFailure {
                    return Failure(apiErrorMapper.map(it))
                }.map {
                    setLoggedUser(
                        UserDTO(
                            id = it.data.user.id.toString(),
                            uuid = it.data.user.uuid,
                            status = it.data.user.status,
                            userToken = it.data.user.userToken,
                            country = CountriesDTO(mutableListOf(CountryDTO(iso3 = it.data.user.country))),
                        ),
                    )
                    brazeDatasource.changeUser(it.data.user.uuid, it.data.user.email, countryOrigin.iso3)
                    if (countryOrigin.iso3 == "USA" || checkMarketing) {
                        brazeDatasource.enablePushNotifications()
                    }
                    return Success(true)
                }
            }.mapFailure {
                return userDataNetworkDatasource.registerCredentials(
                    lang = localeRepository.getLang(),
                    email = email,
                    countryOrigin = countryOrigin,
                    password = password,
                    state = state,
                    checkMarketing = checkMarketing,
                    checkPrivacy = checkPrivacy,
                    checkTerms = checkTerms,
                    nonce = nonce,
                    integrityToken = integrityToken,
                ).mapFailure {
                    return Failure(apiErrorMapper.map(it))
                }.map {
                    setLoggedUser(
                        UserDTO(
                            id = it.data.user.id.toString(),
                            uuid = it.data.user.uuid,
                            status = it.data.user.status,
                            userToken = it.data.user.userToken,
                            country = CountriesDTO(mutableListOf(CountryDTO(iso3 = it.data.user.country))),
                        ),
                    )
                    return Success(true)
                }
            }
    }

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
        return getLoggedUser()
            .map {
                return userDataNetworkDatasource.registerUser(
                    localeRepository.getLang(),
                    it.uuid,
                    it.userToken,
                    fullFirstName,
                    fullLastName,
                    dateOfBirth,
                    city,
                    streetType,
                    streetName,
                    streetNumber,
                    buildingName,
                    zip,
                    state,
                    address,
                    signature,
                ).mapFailure {
                    return Failure(apiErrorMapper.map(it))
                }.map {
                    setLoggedUser(userResponseMapper.map(it))
                    return Success(true)
                }
            }.mapFailure {
                return Failure(it)
            }
    }

    override fun logout(): OperationResult<Boolean, Error> {
        getLoggedUser()
            .peek { user ->
                userDataLocalDatasource.setSMSConsentShown(false)
                if (user.userToken.isNotEmpty() && user.id.isNotEmpty())
                    userDataNetworkDatasource.logout(
                        user.userToken,
                        user.id,
                    ).map {
                        clearUserData()
                        return Success(true)
                    }.mapFailure {
                        return Failure(apiErrorMapper.map(it))
                    }
            }.peekFailure {
                return Failure(it)
            }
        return Failure(Error.UncompletedOperation("Uncompleted logout"))
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

    override fun setSMSConsentShown() {
        userDataLocalDatasource.setSMSConsentShown(true)
    }

    override fun getSMSConsentShown(): Boolean =
        userDataLocalDatasource.getSMSConsentShown()

    override fun updateMarketingPreferences(marketingPreferencesDTO: MarketingPreferenceDTO): OperationResult<Unit, Error> =
        userDataLocalDatasource.retrieveUser()
            .map { user ->
                listOf(
                    userDataNetworkDatasource.updateMarketingPreferences(
                        saveMarketingPreferencesRequestMapper.map(marketingPreferencesDTO),
                        user.userToken,
                        user.id,
                    ),
                    if (marketingPreferencesDTO.disable.filterIsInstance<MarketingPreference.PushNotification>().isEmpty())
                        brazeDatasource.enablePushNotifications()
                    else
                        brazeDatasource.disablePushNotifications(),
                ).zipFailure { error ->
                    return Failure(apiErrorMapper.map(error))
                }.zipSuccess {
                    return Success(Unit)
                }
            }

    override fun getLastPrimeForPushEventTimestamp(): Long =
        userDataLocalDatasource.getLastPrimeForPushEventTimestamp()

    override fun setLastPrimeForPushEventTimestamp(timestamp: Long) {
        userDataLocalDatasource.setLastPrimeForPushEventTimestamp(timestamp)
    }
}
