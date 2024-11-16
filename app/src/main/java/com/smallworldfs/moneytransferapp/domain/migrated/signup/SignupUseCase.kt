package com.smallworldfs.moneytransferapp.domain.migrated.signup

import com.smallworldfs.moneytransferapp.base.domain.utils.Base64Tool
import com.smallworldfs.moneytransferapp.data.operations.model.OtpDTO
import com.smallworldfs.moneytransferapp.domain.migrated.addess.model.AddressDTO
import com.smallworldfs.moneytransferapp.domain.migrated.addess.repository.AddressRepository
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.flatMap
import com.smallworldfs.moneytransferapp.domain.migrated.base.flatMapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.peek
import com.smallworldfs.moneytransferapp.domain.migrated.base.peekFailure
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountriesDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.StateDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.repository.CountryRepository
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.PasswordDTO
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.domain.migrated.operations.repository.OperationsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.signup.model.MarketingPreference
import com.smallworldfs.moneytransferapp.domain.migrated.signup.model.MarketingPreferenceDTO
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class SignupUseCase @Inject constructor(
    private val userDataRepository: UserDataRepository,
    private val addressRepository: AddressRepository,
    private val countryRepository: CountryRepository,
    private val operationsRepository: OperationsRepository,
    private val base64Tool: Base64Tool
) {

    private val OTP = "otp"
    private val OTP_RESEND = "otp-resend"
    private val SOFT_REGISTER = "soft-register"

    fun getOriginCountries(): OperationResult<CountriesDTO, Error> {
        return countryRepository.getOriginCountries()
    }

    fun getOriginCountries(lat: Double, long: Double): OperationResult<CountriesDTO, Error> {
        return countryRepository.getOriginCountries(lat, long)
    }

    fun getCountries(): OperationResult<CountriesDTO, Error> {
        return countryRepository.getCountries()
    }

    fun registerCredentials(
        email: String,
        country: CountryDTO,
        password: CharArray,
        state: String = STRING_EMPTY,
        checkMarketing: Boolean = false,
        checkPrivacy: Boolean = false,
        checkTerms: Boolean = false,
    ): OperationResult<Boolean, Error> {
        return operationsRepository.getIntegrityDTO(SOFT_REGISTER).map {
            return userDataRepository.registerCredentials(
                email,
                country,
                PasswordDTO(base64Tool.encode(String(password)).toCharArray()),
                state,
                checkMarketing,
                checkPrivacy,
                checkTerms,
                it.nonce,
                it.requestInfo.signature
            ).map {
                userDataRepository.setPassword(PasswordDTO(password))
                return Success(it)
            }.peekFailure {
                return Failure(it)
            }
        }.peekFailure {
            return Failure(it)
        }
    }

    fun getUserData(): OperationResult<UserDTO, Error> {
        return userDataRepository.getUserData()
            .peek {
                userDataRepository.setLoggedUser(it)
            }
    }

    fun verifyCode(code: String, operationId: String): OperationResult<Boolean, Error> {
        return operationsRepository.validateOTP(code, operationId)
    }

    fun registerPhone(
        phoneNumber: String,
        countryCode: String,
        isSMSMarketingChecked: Boolean
    ): OperationResult<OtpDTO, Error> {
        if (isSMSMarketingChecked) userDataRepository.setSMSConsentShown()
        return operationsRepository.getIntegrityDTO(OTP).map {
            return operationsRepository.sendSMSOTP(
                phoneNumber,
                countryCode,
                it.nonce,
                it.requestInfo.signature,
            ).map {
                updateUserPhone(phoneNumber)
                userDataRepository.getUserData().peek { user ->
                    user.country.countries.firstOrNull()?.let { country ->
                        if (country.iso3 == "USA") {
                            if (isSMSMarketingChecked) {
                                userDataRepository.updateMarketingPreferences(
                                    MarketingPreferenceDTO(listOf(MarketingPreference.SMS), emptyList())
                                )
                            } else {
                                userDataRepository.updateMarketingPreferences(
                                    MarketingPreferenceDTO(emptyList(), listOf(MarketingPreference.SMS))
                                )
                            }
                        }
                    }
                }
                return Success(it)
            }.peekFailure {
                return Failure(it)
            }
        }.peekFailure {
            return Failure(it)
        }
    }

    fun resendCode(
        operationId: String,
        phoneNumber: String,
        countryCode: String
    ): OperationResult<OtpDTO, Error> {
        return operationsRepository.getIntegrityDTO(OTP_RESEND).map {
            return operationsRepository.resendSMSOTP(
                operationId,
                phoneNumber,
                countryCode,
                it.nonce,
                it.requestInfo.signature
            ).map {
                return Success(it)
            }.peekFailure {
                return Failure(it)
            }
        }.peekFailure {
            return Failure(it)
        }
    }

    fun updateUserPhone(phoneNumber: String): OperationResult<UserDTO, Error> {
        return userDataRepository.getLoggedUser().flatMap {
            userDataRepository.setLoggedUser(it.copy(phone = phoneNumber))
            Success(it)
        }.flatMapFailure {
            Failure(it)
        }
    }

    fun getUSAStates(): OperationResult<List<StateDTO>, Error> {
        return countryRepository.getStates(CountryDTO(iso3 = "USA"))
    }

    fun searchAddress(address: String): OperationResult<List<AddressDTO>, Error> {
        return addressRepository.searchAddressByText(address)
    }

    fun searchAddressByParentId(parentId: String): OperationResult<List<AddressDTO>, Error> {
        return addressRepository.searchAddressByParentId(parentId)
    }

    fun getAddressById(id: String): OperationResult<List<Field>, Error> {
        return addressRepository.getAddressById(id)
    }

    fun getAddressForm(): OperationResult<List<Field>, Error> {
        return addressRepository.getAddressForm()
    }

    fun registerUser(
        fullFirstName: String,
        fullLastName: String,
        dateOfBirthString: String,
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
        val dateOfBirth = try {
            DateFormat.getDateInstance().parse(dateOfBirthString)
                ?.let { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it) }
        } catch (e: Exception) {
            STRING_EMPTY
        }
        return userDataRepository.registerUser(
            fullFirstName,
            fullLastName,
            dateOfBirth ?: STRING_EMPTY,
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
    }
}
