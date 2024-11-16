package com.smallworldfs.moneytransferapp.data.operations.repository

import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.data.common.locale.LocaleRepository
import com.smallworldfs.moneytransferapp.data.integrity.network.IntegrityNetworkDatasource
import com.smallworldfs.moneytransferapp.data.operations.model.IntegrityDTOMapper
import com.smallworldfs.moneytransferapp.data.operations.model.OtpDTO
import com.smallworldfs.moneytransferapp.data.operations.model.OtpDTOMapper
import com.smallworldfs.moneytransferapp.data.operations.network.OperationsNetworkDatasource
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.flatMap
import com.smallworldfs.moneytransferapp.domain.migrated.base.flatMapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.operations.model.IntegrityDTO
import com.smallworldfs.moneytransferapp.domain.migrated.operations.repository.OperationsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import javax.inject.Inject

class OperationsRepositoryImpl @Inject constructor(
    private val operationsNetworkDatasource: OperationsNetworkDatasource,
    private val integrityNetworkDatasource: IntegrityNetworkDatasource,
    private val apiErrorMapper: APIErrorMapper,
    private val localeRepository: LocaleRepository,
    private val userDataRepository: UserDataRepository,
    private val otpDTOMapper: OtpDTOMapper,
    private val integrityDTOMapper: IntegrityDTOMapper
) : OperationsRepository {

    companion object {
        private const val FACTOR_TYPE_SMS = "sms"
        private const val FACTOR_TYPE_EMAIL = "email"
    }

    override fun validateOTP(otp: String, operationId: String): OperationResult<Boolean, Error> {
        return userDataRepository.getLoggedUser()
            .flatMap { userDTO ->
                operationsNetworkDatasource.validateOTP(
                    localeRepository.getLang(),
                    userDTO.uuid,
                    userDTO.userToken,
                    operationId,
                    otp,
                ).mapFailure {
                    apiErrorMapper.map(it)
                }.map {
                    return Success(true)
                }
            }.flatMapFailure {
                return Failure(it)
            }
    }

    override fun sendSMSOTP(
        phone: String,
        countryCode: String,
        nonce: String,
        integrityToken: String
    ): OperationResult<OtpDTO, Error> =
        userDataRepository.getLoggedUser()
            .flatMap { userDTO ->
                operationsNetworkDatasource.sendSMSOTP(
                    localeRepository.getLang(),
                    userDTO.uuid,
                    userDTO.userToken,
                    FACTOR_TYPE_SMS,
                    phone,
                    countryCode,
                    nonce,
                    integrityToken
                ).mapFailure {
                    return Failure(apiErrorMapper.map(it))
                }.map {
                    return otpDTOMapper.map(it)
                }
            }.flatMapFailure {
                return Failure(it)
            }

    override fun resendSMSOTP(
        operationId: String,
        phone: String,
        countryCode: String,
        nonce: String,
        integrityToken: String
    ): OperationResult<OtpDTO, Error> =
        userDataRepository.getLoggedUser()
            .flatMap { userDTO ->
                operationsNetworkDatasource.resendOTP(
                    localeRepository.getLang(),
                    operationId,
                    userDTO.uuid,
                    userDTO.userToken,
                    FACTOR_TYPE_SMS,
                    phone,
                    countryCode,
                    nonce,
                    integrityToken
                ).mapFailure {
                    return Failure(apiErrorMapper.map(it))
                }.map {
                    return otpDTOMapper.map(it)
                }
            }.flatMapFailure {
                return Failure(it)
            }

    override fun sendEmailOTP(nonce: String, integrityToken: String): OperationResult<OtpDTO, Error> =
        userDataRepository.getLoggedUser()
            .flatMap { userDTO ->
                operationsNetworkDatasource.sendEmailOTP(
                    localeRepository.getLang(),
                    userDTO.uuid,
                    userDTO.userToken,
                    FACTOR_TYPE_EMAIL,
                    userDTO.email,
                    nonce,
                    integrityToken
                ).mapFailure {
                    return Failure(apiErrorMapper.map(it))
                }.map {
                    return otpDTOMapper.map(it)
                }
            }.flatMapFailure {
                return Failure(it)
            }

    override fun getIntegrityDTO(operation: String): OperationResult<IntegrityDTO, Error> {
        return integrityNetworkDatasource.getIntegrity(operation)
            .map {
                return Success(integrityDTOMapper.map(it))
            }
            .mapFailure {
                return Failure(apiErrorMapper.map(it))
            }
    }
}
