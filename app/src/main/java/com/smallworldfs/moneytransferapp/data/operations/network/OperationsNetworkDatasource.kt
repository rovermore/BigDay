package com.smallworldfs.moneytransferapp.data.operations.network

import com.smallworldfs.moneytransferapp.data.auth.oauth.model.Integrity
import com.smallworldfs.moneytransferapp.data.auth.oauth.model.RequestInfo
import com.smallworldfs.moneytransferapp.data.base.network.NetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIError
import com.smallworldfs.moneytransferapp.data.operations.model.OTPResponse
import com.smallworldfs.moneytransferapp.data.operations.model.Profile
import com.smallworldfs.moneytransferapp.data.operations.model.SendOTPRequest
import com.smallworldfs.moneytransferapp.data.operations.model.ValidateOtpRequest
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import javax.inject.Inject

class OperationsNetworkDatasource @Inject constructor(
    private val service: OperationsService
) : NetworkDatasource() {

    fun validateOTP(lang: String, uuid: String, userToken: String, operationId: String, otp: String): OperationResult<OTPResponse, APIError> =
        executeCall(service.validateOTP(lang, operationId, uuid, userToken, ValidateOtpRequest(otp)))

    fun sendSMSOTP(
        lang: String,
        uuid: String,
        userToken: String,
        factorType: String,
        mobilePhone: String,
        countryCode: String,
        nonce: String,
        integrityToken: String
    ): OperationResult<OTPResponse, APIError> =
        executeCall(
            service.sendOTP(
                lang,
                uuid,
                userToken,
                SendOTPRequest(
                    factorType,
                    Profile.SMSProfile(countryCode, mobilePhone),
                    Integrity(
                        nonce,
                        RequestInfo(
                            integrityToken
                        )
                    )
                )
            )
        )

    fun sendEmailOTP(
        lang: String,
        uuid: String,
        userToken: String,
        factorType: String,
        email: String,
        nonce: String,
        integrityToken: String
    ): OperationResult<OTPResponse, APIError> =
        executeCall(
            service.sendOTP(
                lang,
                uuid,
                userToken,
                SendOTPRequest(
                    factorType,
                    Profile.EmailProfile(email),
                    Integrity(
                        nonce,
                        RequestInfo(
                            integrityToken
                        )
                    )
                )
            )
        )

    fun resendOTP(
        lang: String,
        operationId: String,
        uuid: String,
        userToken: String,
        factorType: String,
        phone: String,
        countryCode: String,
        nonce: String,
        integrityToken: String
    ): OperationResult<OTPResponse, APIError> =
        executeCall(
            service.resendOTP(
                lang,
                operationId,
                uuid,
                userToken,
                SendOTPRequest(
                    factorType,
                    Profile.SMSProfile(
                        countryCode,
                        phone
                    ),
                    Integrity(
                        nonce,
                        RequestInfo(
                            integrityToken
                        )
                    )
                )
            )
        )
}
