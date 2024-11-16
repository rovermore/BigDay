package com.smallworldfs.moneytransferapp.data.userdata.network

import com.smallworldfs.moneytransferapp.data.auth.oauth.model.Integrity
import com.smallworldfs.moneytransferapp.data.auth.oauth.model.RequestInfo
import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.login.model.LimitedLoginRequest
import com.smallworldfs.moneytransferapp.data.login.model.LimitedLoginResponse
import com.smallworldfs.moneytransferapp.data.login.model.LoginRequest
import com.smallworldfs.moneytransferapp.data.login.model.LoginResponse
import com.smallworldfs.moneytransferapp.data.login.model.RegisterCredentialsRequest
import com.smallworldfs.moneytransferapp.data.login.model.RegisterUserRequest
import com.smallworldfs.moneytransferapp.data.login.model.SoftRegisterResponse
import com.smallworldfs.moneytransferapp.data.login.model.UserResponse
import com.smallworldfs.moneytransferapp.data.login.network.LoginService
import com.smallworldfs.moneytransferapp.data.login.network.LoginServiceV3
import com.smallworldfs.moneytransferapp.data.marketing.model.FormSettingsServer
import com.smallworldfs.moneytransferapp.data.marketing.model.SaveMarketingPreferencesRequest
import com.smallworldfs.moneytransferapp.data.marketing.model.SaveMarketingPreferencesResponse
import com.smallworldfs.moneytransferapp.data.userdata.model.LogoutResponse
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.PasswordDTO

class UserDataNetworkDatasource(
    private val userDataService: UserDataService,
    private val loginService: LoginService,
    private val loginServiceV3: LoginServiceV3,
    private val userServiceV3: UserServiceV3
) : NetworkDatasource() {

    fun getUser(lang: String, uuid: String, userToken: String): OperationResult<UserResponse, APIError> =
        executeCall(userDataService.getUser(lang, uuid, userToken))

    fun requestLogin(lang: String, email: String, passwordDTO: PasswordDTO, country: String): OperationResult<LoginResponse, APIError> =
        executeCall(loginService.login(lang, LoginRequest(email, String(passwordDTO.code), country)))

    fun requestLimitedLogin(lang: String, appToken: String = "", originCountry: String, destinationCountry: String): OperationResult<LimitedLoginResponse, APIError> =
        executeCall(loginService.limitedLogin(lang, LimitedLoginRequest(appToken, originCountry, destinationCountry)))

    fun registerCredentials(
        lang: String,
        appToken: String? = null,
        uuid: String? = null,
        email: String,
        countryOrigin: CountryDTO,
        password: PasswordDTO,
        state: String,
        checkMarketing: Boolean,
        checkPrivacy: Boolean,
        checkTerms: Boolean,
        nonce: String,
        integrityToken: String
    ): OperationResult<SoftRegisterResponse, APIError> =
        executeCall(
            loginService.registerCredentials(
                lang,
                RegisterCredentialsRequest(
                    appToken,
                    uuid,
                    email,
                    countryOrigin.iso3,
                    String(password.code),
                    state,
                    checkMarketing,
                    checkPrivacy,
                    checkTerms,
                    Integrity(
                        nonce,
                        RequestInfo(
                            integrityToken
                        )
                    )
                )
            )
        )

    fun registerUser(
        lang: String,
        uuid: String,
        userToken: String,
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
    ): OperationResult<UserResponse, APIError> =
        executeCall(
            loginService.registerUser(
                lang,
                uuid,
                userToken,
                RegisterUserRequest(
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
                    signature
                )
            )
        )

    fun logout(userToken: String, id: String): OperationResult<LogoutResponse, APIError> =
        executeCall(loginServiceV3.logout(userToken = userToken, id = id))

    fun requestMarketingPreferences(userToken: String, userId: String, fromView: String): OperationResult<FormSettingsServer, APIError> =
        executeCall(
            userServiceV3.requestMarketingPreferences(
                userToken, userId, fromView
            )
        )

    fun updateMarketingPreferences(marketingPreferences: HashMap<String, String>, userToken: String, userId: String): OperationResult<SaveMarketingPreferencesResponse, APIError> =
        executeCall(
            userServiceV3.saveMarketingPreferences(
                SaveMarketingPreferencesRequest(
                    userToken,
                    userId,
                    marketingPreferences
                )
            )
        )
}
