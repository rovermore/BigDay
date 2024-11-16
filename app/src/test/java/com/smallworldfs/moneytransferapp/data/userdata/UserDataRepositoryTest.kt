package com.smallworldfs.moneytransferapp.data.userdata

import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
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
import com.smallworldfs.moneytransferapp.data.userdata.repository.UserDataRepositoryImpl
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.get
import com.smallworldfs.moneytransferapp.domain.migrated.capabilities.CapabilityChecker
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.PassCodeDTO
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.PasswordDTO
import com.smallworldfs.moneytransferapp.domain.migrated.settings.model.QuickLoginSettingsMapper
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.mocks.dto.CountriesDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.MarketingPreferenceDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.QuickLoginSettingsDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.UserDTOMock
import com.smallworldfs.moneytransferapp.mocks.dto.UserStatusDTOMock
import com.smallworldfs.moneytransferapp.mocks.response.FormSettingsServerMock
import com.smallworldfs.moneytransferapp.mocks.response.LimitedLoginResponseMock
import com.smallworldfs.moneytransferapp.mocks.response.LoginResponseMock
import com.smallworldfs.moneytransferapp.mocks.response.QuickLoginSettingsModelMock
import com.smallworldfs.moneytransferapp.mocks.response.SaveMarketingPreferencesResponseMock
import com.smallworldfs.moneytransferapp.mocks.response.SoftRegisterResponseMock
import com.smallworldfs.moneytransferapp.mocks.response.UserResponseMock
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.toJSONString
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class UserDataRepositoryTest {

    @Mock
    lateinit var capabilityChecker: CapabilityChecker

    @Mock
    lateinit var apiErrorMapper: APIErrorMapper

    @Mock
    lateinit var userDataLocalDatasource: UserDataLocalDatasource

    @Mock
    lateinit var preferencesLocal: PreferencesLocal

    @Mock
    lateinit var userMapper: UserMapperFromDTO

    @Mock
    lateinit var userDTOMapper: UserDTOMapper

    @Mock
    lateinit var userDataNetworkDatasource: UserDataNetworkDatasource

    @Mock
    lateinit var localeRepository: LocaleRepository

    @Mock
    lateinit var userStatusMapper: UserResponseMapper

    @Mock
    lateinit var limitedUserMapper: LimitedUserMapper

    @Mock
    lateinit var userResponseMapper: UserResponseMapper

    @Mock
    lateinit var loginResponseMapper: LoginResponseMapper

    @Mock
    lateinit var quickLoginSettingsMapper: QuickLoginSettingsMapper

    @Mock
    lateinit var oAuthLocal: OAuthLocal

    @Mock
    lateinit var saveMarketingPreferencesRequestMapper: SaveMarketingPreferencesRequestMapper

    @Mock
    lateinit var brazeDatasource: BrazeDatasource

    lateinit var userDataRepository: UserDataRepository

    private val unmappedApiError = APIError.UnmappedError(432, "Unmapped error")
    private val unmappedDomainError = Error.Unmapped("Unmapped error")
    private val operationCompletedWithError = Error.OperationCompletedWithError("No status found")

    private val quickLoginSettingsModel = QuickLoginSettingsModelMock.quickLoginSettingsModel
    private val quickLoginSettingsDTO = QuickLoginSettingsDTOMock.quickLoginSettingsDTOMock

    private val passcodeDTO = PassCodeDTO("passcode".toCharArray())
    private val passwordDTO = PasswordDTO("password".toCharArray())
    private val userDTO = UserDTOMock.userDTO
    private val user = User()
    private val loginResponse = LoginResponseMock.loginResponse
    private val countryDTO = CountriesDTOMock.country1
    private val destinationCountryDTO = CountriesDTOMock.country2
    private val softRegisterResponse = SoftRegisterResponseMock.softRegisterResponse
    private val userResponse = UserResponseMock.userResponse
    private val formResponse = FormSettingsServerMock.formSettingsServerMock
    private val saveMarketingPreferencesResponse = SaveMarketingPreferencesResponseMock.saveMarketingPreferencesResponse
    private val limitedLoginResponse = LimitedLoginResponseMock.limitedLoginResponse
    private val userStatusDTO = UserStatusDTOMock.userStatusDTO

    private val lang = "en"
    private val country = "gb"
    private val state = "state"
    private val nonce = "nonce"
    private val integrityToken = "integrityToken"
    private val marketingPreferencesDTO = MarketingPreferenceDTOMock.marketingPreferencesDTO
    private val marketingPreferences = MarketingPreferenceDTOMock.hashMapPreferences

    @Before
    fun setupCommon() {
        MockitoAnnotations.initMocks(this)
        userDataRepository = UserDataRepositoryImpl(
            capabilityChecker,
            userDataLocalDatasource,
            preferencesLocal,
            userMapper,
            userDTOMapper,
            userDataNetworkDatasource,
            localeRepository,
            userStatusMapper,
            apiErrorMapper,
            limitedUserMapper,
            userResponseMapper,
            loginResponseMapper,
            quickLoginSettingsMapper,
            oAuthLocal,
            saveMarketingPreferencesRequestMapper,
            brazeDatasource,
        )
    }

    @Test
    fun `when getQuickLoginSettings success preferencesLocal getQuickLoginSettings is called`() {
        Mockito.`when`(preferencesLocal.getQuickLoginSettings()).thenReturn(quickLoginSettingsModel.toJSONString().get().toString())
        Mockito.`when`(capabilityChecker.hasBiometricCapability()).thenReturn(true)
        userDataRepository.getQuickLoginSettings()
        Mockito.verify(preferencesLocal, Mockito.times(1))
            .getQuickLoginSettings()
    }

    @Test
    fun `when getQuickLoginSettings success preferencesLocal getQuickLoginSettings success`() {
        Mockito.`when`(preferencesLocal.getQuickLoginSettings()).thenReturn(quickLoginSettingsModel.toJSONString().get().toString())
        Mockito.`when`(capabilityChecker.hasBiometricCapability()).thenReturn(true)
        val result = userDataRepository.getQuickLoginSettings()
        Assert.assertEquals(Success(quickLoginSettingsDTO).javaClass, result.javaClass)
    }

    @Test
    fun `when saveQuickLoginSettings success preferencesLocal setQuickLoginSettings is called`() {
        Mockito.`when`(quickLoginSettingsMapper.map(quickLoginSettingsDTO)).thenReturn(quickLoginSettingsModel)
        userDataRepository.saveQuickLoginSettings(quickLoginSettingsDTO)
        Mockito.verify(preferencesLocal, Mockito.times(1))
            .setQuickLoginSettings(quickLoginSettingsModel.toJSONString().get().toString())
    }

    @Test
    fun `when saveQuickLoginSettings success preferencesLocal setQuickLoginSettings success`() {
        Mockito.`when`(quickLoginSettingsMapper.map(quickLoginSettingsDTO)).thenReturn(quickLoginSettingsModel)
        val result = userDataRepository.saveQuickLoginSettings(quickLoginSettingsDTO)
        Assert.assertEquals(Success(quickLoginSettingsDTO).javaClass, result.javaClass)
    }

    /**
     * PassCode
     */

    @Test
    fun `when setPassCode success userDataLocalDatasource savePassCode is called`() {
        Mockito.`when`(userDataLocalDatasource.savePassCode(passcodeDTO)).thenReturn(Success(true))
        userDataRepository.setPassCode(passcodeDTO)
        Mockito.verify(userDataLocalDatasource, Mockito.times(1))
            .savePassCode(passcodeDTO)
    }

    @Test
    fun `when setPassCode success userDataLocalDatasource savePassCode returns success`() {
        Mockito.`when`(userDataLocalDatasource.savePassCode(passcodeDTO)).thenReturn(Success(true))
        val result = userDataRepository.setPassCode(passcodeDTO)
        Assert.assertEquals(Success(true), result)
    }

    @Test
    fun `when setPassCode failure userDataLocalDatasource savePassCode returns error`() {
        Mockito.`when`(userDataLocalDatasource.savePassCode(passcodeDTO)).thenReturn(Failure(unmappedDomainError))
        val result = userDataRepository.setPassCode(passcodeDTO)
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when getPassCode success userDataLocalDatasource retrievePassCode is called`() {
        Mockito.`when`(userDataLocalDatasource.retrievePassCode()).thenReturn(Success(passcodeDTO))
        userDataRepository.getPassCode()
        Mockito.verify(userDataLocalDatasource, Mockito.times(1))
            .retrievePassCode()
    }

    @Test
    fun `when getPassCode success userDataLocalDatasource retrievePassCode returns success`() {
        Mockito.`when`(userDataLocalDatasource.retrievePassCode()).thenReturn(Success(passcodeDTO))
        val result = userDataRepository.getPassCode()
        Assert.assertEquals(Success(passcodeDTO), result)
    }

    @Test
    fun `when getPassCode failure userDataLocalDatasource retrievePassCode returns error`() {
        Mockito.`when`(userDataLocalDatasource.retrievePassCode()).thenReturn(Failure(unmappedDomainError))
        val result = userDataRepository.getPassCode()
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when deletePassCode success userDataLocalDatasource removePassCode is called`() {
        Mockito.`when`(userDataLocalDatasource.removePassCode()).thenReturn(Success(true))
        userDataRepository.deletePassCode()
        Mockito.verify(userDataLocalDatasource, Mockito.times(1))
            .removePassCode()
    }

    @Test
    fun `when deletePassCode success userDataLocalDatasource removePassCode returns success`() {
        Mockito.`when`(userDataLocalDatasource.removePassCode()).thenReturn(Success(true))
        val result = userDataRepository.deletePassCode()
        Assert.assertEquals(Success(true), result)
    }

    @Test
    fun `when deletePassCode failure userDataLocalDatasource removePassCode returns error`() {
        Mockito.`when`(userDataLocalDatasource.removePassCode()).thenReturn(Failure(unmappedDomainError))
        val result = userDataRepository.deletePassCode()
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    /**
     * Password
     */

    @Test
    fun `when setPassword success userDataLocalDatasource savePassword is called`() {
        Mockito.`when`(userDataLocalDatasource.savePassword(passwordDTO)).thenReturn(Success(true))
        userDataRepository.setPassword(passwordDTO)
        Mockito.verify(userDataLocalDatasource, Mockito.times(1))
            .savePassword(passwordDTO)
    }

    @Test
    fun `when setPassword success userDataLocalDatasource savePassword returns success`() {
        Mockito.`when`(userDataLocalDatasource.savePassword(passwordDTO)).thenReturn(Success(true))
        val result = userDataRepository.setPassword(passwordDTO)
        Assert.assertEquals(Success(true), result)
    }

    @Test
    fun `when setPassword failure userDataLocalDatasource savePassword returns error`() {
        Mockito.`when`(userDataLocalDatasource.savePassword(passwordDTO)).thenReturn(Failure(unmappedDomainError))
        val result = userDataRepository.setPassword(passwordDTO)
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when getPassword success userDataLocalDatasource retrievePassword is called`() {
        Mockito.`when`(userDataLocalDatasource.retrievePassword()).thenReturn(Success(passwordDTO))
        userDataRepository.getPassword()
        Mockito.verify(userDataLocalDatasource, Mockito.times(1))
            .retrievePassword()
    }

    @Test
    fun `when getPassword success userDataLocalDatasource retrievePassword returns success`() {
        Mockito.`when`(userDataLocalDatasource.retrievePassword()).thenReturn(Success(passwordDTO))
        val result = userDataRepository.getPassword()
        Assert.assertEquals(Success(passwordDTO), result)
    }

    @Test
    fun `when getPassword failure userDataLocalDatasource retrievePassword returns error`() {
        Mockito.`when`(userDataLocalDatasource.retrievePassword()).thenReturn(Failure(unmappedDomainError))
        val result = userDataRepository.getPassword()
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when deletePassword success userDataLocalDatasource savePassword is called`() {
        Mockito.`when`(userDataLocalDatasource.removePassword()).thenReturn(Success(true))
        userDataRepository.deletePassword()
        Mockito.verify(userDataLocalDatasource, Mockito.times(1))
            .removePassword()
    }

    @Test
    fun `when deletePassword success userDataLocalDatasource savePassword returns success`() {
        Mockito.`when`(userDataLocalDatasource.removePassword()).thenReturn(Success(true))
        val result = userDataRepository.deletePassword()
        Assert.assertEquals(Success(true), result)
    }

    @Test
    fun `when deletePassword failure userDataLocalDatasource savePassword returns error`() {
        Mockito.`when`(userDataLocalDatasource.removePassword()).thenReturn(Failure(unmappedDomainError))
        val result = userDataRepository.deletePassword()
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    /**
     * User
     */

    @Test
    fun `when setLoggedUser success userDataLocalDatasource saveUser is called`() {
        Mockito.`when`(userDataLocalDatasource.saveUser(userDTO)).thenReturn(Success(true))
        userDataRepository.setLoggedUser(userDTO)
        Mockito.verify(userDataLocalDatasource, Mockito.times(1))
            .saveUser(userDTO)
    }

    @Test
    fun `when setLoggedUser success userDataLocalDatasource saveUser returns success`() {
        Mockito.`when`(userDataLocalDatasource.saveUser(userDTO)).thenReturn(Success(true))
        val result = userDataRepository.setLoggedUser(userDTO)
        Assert.assertEquals(Success(true), result)
    }

    @Test
    fun `when setLoggedUser failure userDataLocalDatasource saveUser returns error`() {
        Mockito.`when`(userDataLocalDatasource.saveUser(userDTO)).thenReturn(Failure(unmappedDomainError))
        val result = userDataRepository.setLoggedUser(userDTO)
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when getLoggedUser success userDataLocalDatasource retrieveUser is called`() {
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Success(userDTO))
        userDataRepository.getLoggedUser()
        Mockito.verify(userDataLocalDatasource, Mockito.times(1))
            .retrieveUser()
    }

    @Test
    fun `when getLoggedUser success userDataLocalDatasource retrieveUser returns success`() {
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Success(userDTO))
        val result = userDataRepository.getLoggedUser()
        Assert.assertEquals(Success(userDTO), result)
    }

    @Test
    fun `when getLoggedUser failure userDataLocalDatasource retrieveUser returns error`() {
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Failure(unmappedDomainError))
        val result = userDataRepository.getLoggedUser()
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when deleteLoggedUser success userDataLocalDatasource retrieveUser is called`() {
        Mockito.`when`(userDataLocalDatasource.removeUser()).thenReturn(Success(true))
        userDataRepository.deleteLoggedUser()
        Mockito.verify(userDataLocalDatasource, Mockito.times(1))
            .removeUser()
    }

    @Test
    fun `when deleteLoggedUser success userDataLocalDatasource retrieveUser returns success`() {
        Mockito.`when`(userDataLocalDatasource.removeUser()).thenReturn(Success(true))
        val result = userDataRepository.deleteLoggedUser()
        Assert.assertEquals(Success(true), result)
    }

    @Test
    fun `when deleteLoggedUser failure userDataLocalDatasource retrieveUser returns error`() {
        Mockito.`when`(userDataLocalDatasource.removeUser()).thenReturn(Failure(unmappedDomainError))
        val result = userDataRepository.deleteLoggedUser()
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when retrieveUser success userDataLocalDatasource retrieveUser returns success`() {
        Mockito.`when`(userMapper.mapFromUserDTO(userDTO)).thenReturn(user)
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Success(userDTO))
        val result = userDataRepository.retrieveUser()
        Assert.assertEquals(user, result)
    }

    @Test
    fun `when retrieveUser failure userDataLocalDatasource retrieveUser returns error`() {
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Failure(unmappedDomainError))
        val result = userDataRepository.retrieveUser()
        Assert.assertEquals(null, result)
    }

    @Test
    fun `when deleteQuickLogin success userDataLocalDatasource saveQuickLoginSettings is called`() {
        val result = userDataRepository.deleteQuickLogin()
        Assert.assertEquals(Unit, result)
    }

    @Test
    fun `when putUser success userDTOMapper mapOrNull is called`() {
        Mockito.`when`(userDTOMapper.mapOrNull(user)).thenReturn(userDTO)
        userDataRepository.putUser(user)
        Mockito.verify(userDTOMapper, Mockito.times(1))
            .mapOrNull(user)
    }

    @Test
    fun `when getUserStatus success userDataRepository getUserData returns success`() {
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Success(userDTO))
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(userDataNetworkDatasource.getUser(localeRepository.getLang(), userDTO.uuid, userDTO.userToken)).thenReturn(Success(userResponse))
        Mockito.`when`(userResponseMapper.map(userResponse)).thenReturn(userDTO)
        Mockito.`when`(userDataLocalDatasource.saveUser(userDTO)).thenReturn(Success(true))
        Mockito.`when`(userStatusMapper.mapStatus(userDTO.status)).thenReturn(userStatusDTO)
        val result = userDataRepository.getUserStatus()
        Assert.assertEquals(Success(userStatusDTO), result)
    }

    @Test
    fun `when getUserStatus failure userDataRepository getUserData returns error`() {
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Failure(unmappedDomainError))
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(userDataNetworkDatasource.getUser(localeRepository.getLang(), userDTO.uuid, userDTO.userToken)).thenReturn(Success(userResponse))
        Mockito.`when`(userResponseMapper.map(userResponse)).thenReturn(userDTO)
        Mockito.`when`(userDataLocalDatasource.saveUser(userDTO)).thenReturn(Success(true))
        Mockito.`when`(userStatusMapper.mapStatus(userDTO.status)).thenReturn(userStatusDTO)
        val result = userDataRepository.getUserStatus()
        Assert.assertEquals(Failure(operationCompletedWithError).javaClass, result.javaClass)
    }

    @Test
    fun `when getUserData success userDataRepository getLoggedUser returns success`() {
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Success(userDTO))
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(userDataNetworkDatasource.getUser(lang, userDTO.uuid, userDTO.userToken)).thenReturn(Success(userResponse))
        Mockito.`when`(userResponseMapper.map(userResponse)).thenReturn(userDTO)
        val result = userDataRepository.getUserData()
        Assert.assertEquals(Success(userDTO), result)
    }

    @Test
    fun `when getUserData success userDataRepository getLoggedUser returns error`() {
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Success(userDTO))
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(userDataNetworkDatasource.getUser(lang, userDTO.uuid, userDTO.userToken)).thenReturn(Failure(unmappedApiError))
        Mockito.`when`(apiErrorMapper.map(unmappedApiError)).thenReturn(unmappedDomainError)
        val result = userDataRepository.getUserData()
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when getUserData failure userDataRepository getLoggedUser returns error`() {
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Failure(unmappedDomainError))
        val result = userDataRepository.getUserData()
        Assert.assertEquals(Failure(operationCompletedWithError).javaClass, result.javaClass)
    }

    /**
     * User from Network
     */

    @Test
    fun `when isWelcomeShown success userDataLocalDatasource isWelcomeShow is called`() {
        Mockito.`when`(userDataLocalDatasource.isWelcomeShow()).thenReturn(true)
        userDataRepository.isWelcomeShown()
        Mockito.verify(userDataLocalDatasource, Mockito.times(1))
            .isWelcomeShow()
    }

    @Test
    fun `when isWelcomeShown success userDataLocalDatasource isWelcomeShow returns success`() {
        Mockito.`when`(userDataLocalDatasource.isWelcomeShow()).thenReturn(true)
        val result = userDataRepository.isWelcomeShown()
        Assert.assertEquals(true, result)
    }

    @Test
    fun `when setWelcomeShown success userDataLocalDatasource setWelcomeShown is called`() {
        userDataRepository.setWelcomeShown(true)
        Mockito.verify(userDataLocalDatasource, Mockito.times(1))
            .setWelcomeShown(true)
    }

    @Test
    fun `when isUploadDialogShown success userDataLocalDatasource isUploadDialogShown is called`() {
        Mockito.`when`(userDataLocalDatasource.isUploadDialogShown()).thenReturn(true)
        userDataRepository.isUploadDialogShown()
        Mockito.verify(userDataLocalDatasource, Mockito.times(1))
            .isUploadDialogShown()
    }

    @Test
    fun `when isUploadDialogShown success userDataLocalDatasource isUploadDialogShown returns success`() {
        Mockito.`when`(userDataLocalDatasource.isUploadDialogShown()).thenReturn(true)
        val result = userDataRepository.isUploadDialogShown()
        Assert.assertEquals(true, result)
    }

    @Test
    fun `when setUploadDialogShown success userDataLocalDatasource setUploadDialogShown is called`() {
        userDataRepository.setUploadDialogShown(true)
        Mockito.verify(userDataLocalDatasource, Mockito.times(1))
            .setUploadDialogShown(true)
    }

    /**
     * Login
     */

    @Test
    fun `when login success userDataNetworkDatasource requestLogin is called`() {
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(loginResponseMapper.map(loginResponse)).thenReturn(Success(userDTO))
        Mockito.`when`(
            userDataNetworkDatasource.requestLogin(
                localeRepository.getLang(),
                userDTO.email,
                passwordDTO,
                country
            )
        ).thenReturn(Success(loginResponse))
        userDataRepository.login(
            userDTO.email,
            passwordDTO,
            country
        )
        Mockito.verify(userDataNetworkDatasource, Mockito.times(1))
            .requestLogin(
                localeRepository.getLang(),
                userDTO.email,
                passwordDTO,
                country
            )
    }

    @Test
    fun `when login success userDataNetworkDatasource requestLogin returns success`() {
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(loginResponseMapper.map(loginResponse)).thenReturn(Success(userDTO))
        Mockito.`when`(
            userDataNetworkDatasource.requestLogin(
                localeRepository.getLang(),
                userDTO.email,
                passwordDTO,
                country
            )
        ).thenReturn(Success(loginResponse))
        val result = userDataRepository.login(
            userDTO.email,
            passwordDTO,
            country
        )
        Assert.assertEquals(Success(userDTO), result)
    }

    @Test
    fun `when login failure userDataNetworkDatasource requestLogin returns error`() {
        Mockito.`when`(apiErrorMapper.map(unmappedApiError)).thenReturn(unmappedDomainError)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(loginResponseMapper.map(loginResponse)).thenReturn(Success(userDTO))
        Mockito.`when`(
            userDataNetworkDatasource.requestLogin(
                localeRepository.getLang(),
                userDTO.email,
                passwordDTO,
                country
            )
        ).thenReturn(Failure(unmappedApiError))
        val result = userDataRepository.login(
            userDTO.email,
            passwordDTO,
            country
        )
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when login failure userDataNetworkDatasource requestLoginloginResponseMapper returns error`() {
        Mockito.`when`(apiErrorMapper.map(unmappedApiError)).thenReturn(unmappedDomainError)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(loginResponseMapper.map(loginResponse)).thenReturn(Failure(unmappedDomainError))
        Mockito.`when`(
            userDataNetworkDatasource.requestLogin(
                localeRepository.getLang(),
                userDTO.email,
                passwordDTO,
                country
            )
        ).thenReturn(Success(loginResponse))
        val result = userDataRepository.login(
            userDTO.email,
            passwordDTO,
            country
        )
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when getLimitedLogin success userDataNetworkDatasource requestLimitedLogin returns success`() {
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(userDataNetworkDatasource.requestLimitedLogin(lang, userDTO.appToken, CountriesDTOMock.country1.iso3, destinationCountryDTO.iso3))
            .thenReturn(Success(limitedLoginResponse))
        Mockito.`when`(limitedUserMapper.map(limitedLoginResponse)).thenReturn(Success(userDTO))
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Success(userDTO))
        val result = userDataRepository.getLimitedLogin(CountriesDTOMock.country1, destinationCountryDTO)
        Assert.assertEquals(Success(userDTO), result)
    }

    @Test
    fun `when getLimitedLogin success userDataNetworkDatasource requestLimitedLogin returns error`() {
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(userDataNetworkDatasource.requestLimitedLogin(lang, userDTO.appToken, CountriesDTOMock.country1.iso3, destinationCountryDTO.iso3))
            .thenReturn(Failure(unmappedApiError))
        Mockito.`when`(limitedUserMapper.map(limitedLoginResponse)).thenReturn(Success(userDTO))
        Mockito.`when`(apiErrorMapper.map(unmappedApiError)).thenReturn(unmappedDomainError)
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Success(userDTO))
        val result = userDataRepository.getLimitedLogin(CountriesDTOMock.country1, destinationCountryDTO)
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when getLimitedLogin failure userDataNetworkDatasource requestLimitedLogin returns success`() {
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(userDataNetworkDatasource.requestLimitedLogin(lang, userDTO.appToken, CountriesDTOMock.country1.iso3, destinationCountryDTO.iso3))
            .thenReturn(Success(limitedLoginResponse))
        Mockito.`when`(limitedUserMapper.map(limitedLoginResponse)).thenReturn(Success(userDTO))
        Mockito.`when`(apiErrorMapper.map(unmappedApiError)).thenReturn(unmappedDomainError)
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Failure(unmappedDomainError))
        val result = userDataRepository.getLimitedLogin(CountriesDTOMock.country1, destinationCountryDTO)
        Assert.assertEquals(Success(userDTO), result)
    }

    @Test
    fun `when getLimitedLogin failure userDataNetworkDatasource requestLimitedLogin returns error`() {
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(userDataNetworkDatasource.requestLimitedLogin(lang, userDTO.appToken, CountriesDTOMock.country1.iso3, destinationCountryDTO.iso3))
            .thenReturn(Failure(unmappedApiError))
        Mockito.`when`(limitedUserMapper.map(limitedLoginResponse)).thenReturn(Failure(unmappedDomainError))
        Mockito.`when`(apiErrorMapper.map(unmappedApiError)).thenReturn(unmappedDomainError)
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Failure(unmappedDomainError))
        val result = userDataRepository.getLimitedLogin(CountriesDTOMock.country1, destinationCountryDTO)
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when checkPassword success userDataNetworkDatasource requestLogin is called`() {
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(loginResponseMapper.map(loginResponse)).thenReturn(Success(userDTO))
        Mockito.`when`(
            userDataNetworkDatasource.requestLogin(
                localeRepository.getLang(),
                userDTO.email,
                passwordDTO,
                country
            )
        ).thenReturn(Success(loginResponse))
        userDataRepository.checkPassword(
            userDTO.email,
            passwordDTO,
            country
        )
        Mockito.verify(userDataNetworkDatasource, Mockito.times(1))
            .requestLogin(
                localeRepository.getLang(),
                userDTO.email,
                passwordDTO,
                country
            )
    }

    @Test
    fun `when checkPassword success userDataNetworkDatasource requestLogin returns success`() {
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(loginResponseMapper.map(loginResponse)).thenReturn(Success(userDTO))
        Mockito.`when`(
            userDataNetworkDatasource.requestLogin(
                localeRepository.getLang(),
                userDTO.email,
                passwordDTO,
                country
            )
        ).thenReturn(Success(loginResponse))
        val result = userDataRepository.checkPassword(
            userDTO.email,
            passwordDTO,
            country
        )
        Assert.assertEquals(Success(true), result)
    }

    @Test
    fun `when checkPassword failure userDataNetworkDatasource requestLogin returns error`() {
        Mockito.`when`(apiErrorMapper.map(unmappedApiError)).thenReturn(unmappedDomainError)
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(loginResponseMapper.map(loginResponse)).thenReturn(Success(userDTO))
        Mockito.`when`(
            userDataNetworkDatasource.requestLogin(
                localeRepository.getLang(),
                userDTO.email,
                passwordDTO,
                country
            )
        ).thenReturn(Failure(unmappedApiError))
        val result = userDataRepository.checkPassword(
            userDTO.email,
            passwordDTO,
            country
        )
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when registerCredentials success userDataNetworkDatasource registerCredentials is called`() {
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Success(userDTO))
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            userDataNetworkDatasource.registerCredentials(
                localeRepository.getLang(),
                userDTO.appToken,
                userDTO.uuid,
                userDTO.email,
                countryDTO,
                passwordDTO,
                state,
                true,
                true,
                true,
                nonce,
                integrityToken
            )
        ).thenReturn(Success(softRegisterResponse))
        userDataRepository.registerCredentials(
            userDTO.email,
            countryDTO,
            passwordDTO,
            state,
            true,
            true,
            true,
            nonce,
            integrityToken
        )
        Mockito.verify(userDataNetworkDatasource, Mockito.times(1))
            .registerCredentials(
                localeRepository.getLang(),
                userDTO.appToken,
                userDTO.uuid,
                userDTO.email,
                countryDTO,
                passwordDTO,
                state,
                true,
                true,
                true,
                nonce,
                integrityToken
            )
    }

    @Test
    fun `when registerCredentials success userDataNetworkDatasource registerCredentials returns success`() {
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Success(userDTO))
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            userDataNetworkDatasource.registerCredentials(
                localeRepository.getLang(),
                userDTO.appToken,
                userDTO.uuid,
                userDTO.email,
                countryDTO,
                passwordDTO,
                state,
                true,
                true,
                true,
                nonce,
                integrityToken
            )
        ).thenReturn(Success(softRegisterResponse))
        val result = userDataRepository.registerCredentials(
            userDTO.email,
            countryDTO,
            passwordDTO,
            state,
            true,
            true,
            true,
            nonce,
            integrityToken
        )
        Assert.assertEquals(Success(true), result)
    }

    @Test
    fun `when registerCredentials failure userDataNetworkDatasource registerCredentials returns success`() {
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Failure(unmappedDomainError))
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            userDataNetworkDatasource.registerCredentials(
                lang = localeRepository.getLang(),
                email = userDTO.email,
                countryOrigin = countryDTO,
                password = passwordDTO,
                state = state,
                checkMarketing = true,
                checkPrivacy = true,
                checkTerms = true,
                nonce = nonce,
                integrityToken = integrityToken
            )
        ).thenReturn(Success(softRegisterResponse))
        val result = userDataRepository.registerCredentials(
            userDTO.email,
            countryDTO,
            passwordDTO,
            state,
            true,
            true,
            true,
            nonce,
            integrityToken
        )
        Assert.assertEquals(Success(true), result)
    }

    @Test
    fun `when registerCredentials success userDataNetworkDatasource registerCredentials returns error`() {
        Mockito.`when`(apiErrorMapper.map(unmappedApiError)).thenReturn(unmappedDomainError)
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Success(userDTO))
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            userDataNetworkDatasource.registerCredentials(
                localeRepository.getLang(),
                userDTO.appToken,
                userDTO.uuid,
                userDTO.email,
                countryDTO,
                passwordDTO,
                state,
                true,
                true,
                true,
                nonce,
                integrityToken
            )
        ).thenReturn(Failure(unmappedApiError))
        val result = userDataRepository.registerCredentials(
            userDTO.email,
            countryDTO,
            passwordDTO,
            state,
            true,
            true,
            true,
            nonce,
            integrityToken
        )
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when registerCredentials failure userDataNetworkDatasource registerCredentials returns error`() {
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Failure(unmappedDomainError))
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(
            userDataNetworkDatasource.registerCredentials(
                lang = localeRepository.getLang(),
                email = userDTO.email,
                countryOrigin = countryDTO,
                password = passwordDTO,
                state = state,
                checkMarketing = true,
                checkPrivacy = true,
                checkTerms = true,
                nonce = nonce,
                integrityToken = integrityToken
            )
        ).thenReturn(Failure(unmappedApiError))
        Mockito.`when`(apiErrorMapper.map(unmappedApiError)).thenReturn(unmappedDomainError)
        val result = userDataRepository.registerCredentials(
            userDTO.email,
            countryDTO,
            passwordDTO,
            state,
            true,
            true,
            true,
            nonce,
            integrityToken
        )
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when registerUser success userDataNetworkDatasource registerUser is called`() {
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Success(userDTO))
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(userResponseMapper.map(userResponse)).thenReturn(userDTO)
        Mockito.`when`(
            userDataNetworkDatasource.registerUser(
                localeRepository.getLang(),
                userDTO.uuid,
                userDTO.userToken,
                userDTO.name,
                userDTO.secondName,
                userDTO.birthDate,
                userDTO.city,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
            )
        ).thenReturn(Success(userResponse))
        userDataRepository.registerUser(
            userDTO.name,
            userDTO.secondName,
            userDTO.birthDate,
            userDTO.city,
            STRING_EMPTY,
            STRING_EMPTY,
            STRING_EMPTY,
            STRING_EMPTY,
            STRING_EMPTY,
            STRING_EMPTY,
            STRING_EMPTY,
            STRING_EMPTY
        )
        Mockito.verify(userDataNetworkDatasource, Mockito.times(1))
            .registerUser(
                localeRepository.getLang(),
                userDTO.uuid,
                userDTO.userToken,
                userDTO.name,
                userDTO.secondName,
                userDTO.birthDate,
                userDTO.city,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
            )
    }

    @Test
    fun `when registerUser success userDataNetworkDatasource registerUser returns success`() {
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Success(userDTO))
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(userResponseMapper.map(userResponse)).thenReturn(userDTO)
        Mockito.`when`(
            userDataNetworkDatasource.registerUser(
                localeRepository.getLang(),
                userDTO.uuid,
                userDTO.userToken,
                userDTO.name,
                userDTO.secondName,
                userDTO.birthDate,
                userDTO.city,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
            )
        ).thenReturn(Success(userResponse))
        val result = userDataRepository.registerUser(
            userDTO.name,
            userDTO.secondName,
            userDTO.birthDate,
            userDTO.city,
            STRING_EMPTY,
            STRING_EMPTY,
            STRING_EMPTY,
            STRING_EMPTY,
            STRING_EMPTY,
            STRING_EMPTY,
            STRING_EMPTY,
            STRING_EMPTY
        )
        Assert.assertEquals(Success(true), result)
    }

    @Test
    fun `when registerUser failure userDataNetworkDatasource registerUser returns error`() {
        Mockito.`when`(apiErrorMapper.map(unmappedApiError)).thenReturn(unmappedDomainError)
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Success(userDTO))
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(userResponseMapper.map(userResponse)).thenReturn(userDTO)
        Mockito.`when`(
            userDataNetworkDatasource.registerUser(
                localeRepository.getLang(),
                userDTO.uuid,
                userDTO.userToken,
                userDTO.name,
                userDTO.secondName,
                userDTO.birthDate,
                userDTO.city,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
            )
        ).thenReturn(Failure(unmappedApiError))
        val result = userDataRepository.registerUser(
            userDTO.name,
            userDTO.secondName,
            userDTO.birthDate,
            userDTO.city,
            STRING_EMPTY,
            STRING_EMPTY,
            STRING_EMPTY,
            STRING_EMPTY,
            STRING_EMPTY,
            STRING_EMPTY,
            STRING_EMPTY,
            STRING_EMPTY
        )
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when registerUser failure userDataNetworkDatasource getLoggedUser returns error`() {
        Mockito.`when`(apiErrorMapper.map(unmappedApiError)).thenReturn(unmappedDomainError)
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Failure(unmappedDomainError))
        Mockito.`when`(localeRepository.getLang()).thenReturn(lang)
        Mockito.`when`(userResponseMapper.map(userResponse)).thenReturn(userDTO)
        Mockito.`when`(
            userDataNetworkDatasource.registerUser(
                localeRepository.getLang(),
                userDTO.uuid,
                userDTO.userToken,
                userDTO.name,
                userDTO.secondName,
                userDTO.birthDate,
                userDTO.city,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
                STRING_EMPTY,
            )
        ).thenReturn(Failure(unmappedApiError))
        val result = userDataRepository.registerUser(
            userDTO.name,
            userDTO.secondName,
            userDTO.birthDate,
            userDTO.city,
            STRING_EMPTY,
            STRING_EMPTY,
            STRING_EMPTY,
            STRING_EMPTY,
            STRING_EMPTY,
            STRING_EMPTY,
            STRING_EMPTY,
            STRING_EMPTY
        )
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when requestMarketingPreferences success userDataNetworkDatasource requestMarketingPreferences is called`() {
        Mockito.`when`(
            userDataNetworkDatasource.requestMarketingPreferences(
                userDTO.userToken, userDTO.id, ""
            )
        ).thenReturn(Success(formResponse))
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Success(userDTO))
        userDataRepository.requestMarketingPreferences("")
        Mockito.verify(userDataNetworkDatasource, Mockito.times(1)).requestMarketingPreferences(
            userDTO.userToken, userDTO.id, ""
        )
    }

    @Test
    fun `when requestMarketingPreferences success userDataNetworkDatasource requestMarketingPreferences returns success`() {
        Mockito.`when`(
            userDataNetworkDatasource.requestMarketingPreferences(
                userDTO.userToken, userDTO.id, ""
            )
        ).thenReturn(Success(formResponse))
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Success(userDTO))
        val result = userDataRepository.requestMarketingPreferences("")
        Assert.assertEquals(Success(formResponse.form), result)
    }

    @Test
    fun `when requestMarketingPreferences failure userDataNetworkDatasource requestMarketingPreferences returns error`() {
        Mockito.`when`(
            userDataNetworkDatasource.requestMarketingPreferences(
                userDTO.userToken, userDTO.id, ""
            )
        ).thenReturn(Failure(unmappedApiError))
        Mockito.`when`(apiErrorMapper.map(unmappedApiError)).thenReturn(unmappedDomainError)
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Success(userDTO))
        val result = userDataRepository.requestMarketingPreferences("")
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when requestMarketingPreferences failure userDataLocalDatasource retrieveUser returns error`() {
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Failure(unmappedDomainError))
        val result = userDataRepository.requestMarketingPreferences("")
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when updateMarketingPreferences success userDataNetworkDatasource updateMarketingPreferences is called`() {
        Mockito.`when`(saveMarketingPreferencesRequestMapper.map(marketingPreferencesDTO)).thenReturn(marketingPreferences)
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Success(userDTO))
        Mockito.`when`(
            userDataNetworkDatasource.updateMarketingPreferences(marketingPreferences, userDTO.userToken, userDTO.id)
        ).thenReturn(Success(saveMarketingPreferencesResponse))
        userDataRepository.updateMarketingPreferences(marketingPreferencesDTO)
        Mockito.verify(userDataNetworkDatasource, Mockito.times(1)).updateMarketingPreferences(
            marketingPreferences, userDTO.userToken, userDTO.id
        )
    }

    @Test
    fun `when updateMarketingPreferences success userDataNetworkDatasource updateMarketingPreferences returns success`() {
        Mockito.`when`(saveMarketingPreferencesRequestMapper.map(marketingPreferencesDTO)).thenReturn(marketingPreferences)
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Success(userDTO))
        Mockito.`when`(
            userDataNetworkDatasource.updateMarketingPreferences(marketingPreferences, userDTO.userToken, userDTO.id)
        ).thenReturn(Success(saveMarketingPreferencesResponse))
        val result = userDataRepository.updateMarketingPreferences(marketingPreferencesDTO)
        Assert.assertEquals(Success(Unit), result)
    }

    @Test
    fun `when updateMarketingPreferences failure userDataNetworkDatasource updateMarketingPreferences returns error`() {
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Success(userDTO))
        Mockito.`when`(saveMarketingPreferencesRequestMapper.map(marketingPreferencesDTO)).thenReturn(marketingPreferences)
        Mockito.`when`(apiErrorMapper.map(unmappedApiError)).thenReturn(unmappedDomainError)
        Mockito.`when`(
            userDataNetworkDatasource.updateMarketingPreferences(
                marketingPreferences, userDTO.userToken, userDTO.id
            )
        ).thenReturn(Failure(unmappedApiError))
        val result = userDataRepository.updateMarketingPreferences(marketingPreferencesDTO)
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when updateMarketingPreferences failure userDataLocalDatasource retrieveUser returns error`() {
        Mockito.`when`(userDataLocalDatasource.retrieveUser()).thenReturn(Failure(unmappedDomainError))
        val result = userDataRepository.updateMarketingPreferences(marketingPreferencesDTO)
        Assert.assertEquals(Failure(unmappedDomainError), result)
    }

    @Test
    fun `when setSMSConsentShown success userDataNetworkDatasource setSMSConsentShown is called`() {
        Mockito.doNothing().`when`(userDataLocalDatasource).setSMSConsentShown(true)
        userDataRepository.setSMSConsentShown()
        Mockito.verify(userDataLocalDatasource, Mockito.times(1)).setSMSConsentShown(true)
    }

    // TODO IMPLEMENT TEST WHEN SettingsRepository.java IS REFACTORED
    /*@Test
    fun `when logout success userDataNetworkDatasource deleteDevice is called`() { }
    @Test
    fun `when logout success userDataNetworkDatasource deleteDevice returns success`() { }
    @Test
    fun `when logout failure userDataNetworkDatasource deleteDevice returns error`() { }*/
}
