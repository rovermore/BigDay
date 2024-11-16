package com.smallworldfs.moneytransferapp.domain.migrated.login.usecase

import com.smallworldfs.moneytransferapp.base.domain.utils.Base64Tool
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.peek
import com.smallworldfs.moneytransferapp.domain.migrated.base.peekFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.then
import com.smallworldfs.moneytransferapp.domain.migrated.capabilities.LocationRepository
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountriesDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.repository.CountryRepository
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.PassCodeDTO
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.PasswordDTO
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.domain.usertoken.repository.local.UserTokenLocal
import com.smallworldfs.moneytransferapp.presentation.common.coordinates.SWCoordinates
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userTokenLocal: UserTokenLocal,
    private val base64Tool: Base64Tool,
    private val userDataRepository: UserDataRepository,
    private val locationRepository: LocationRepository,
    private val countryRepository: CountryRepository
) {

    fun login(
        email: String,
        passwordDTO: PasswordDTO,
        countryDTO: CountryDTO
    ): OperationResult<UserDTO, Error> {
        val encodedPasswordDTO = PasswordDTO(base64Tool.encode(String(passwordDTO.code)).toCharArray())
        return userDataRepository.login(email, encodedPasswordDTO, countryDTO.iso3)
            .peek { user ->
                when {
                    user.isLimited() -> return Failure(Error.UnregisteredUser())
                    user.hasPendingPhoneValidation() -> return Failure(Error.PendingPhoneValidation())
                    user.hasPendingProfileValidation() -> return Failure(Error.PendingProfileValidation())
                }
                updatePassword(passwordDTO)
                userTokenLocal.setUserToken(user.userToken)
                saveUserDTO(passwordDTO)
                return Success(user)
            }.peekFailure {
                return Failure(it)
            }.then {
                passwordDTO.release()
            }
    }

    fun autoLogin(): OperationResult<UserDTO, Error> {
        return userDataRepository.getPassword()
            .map { passwordDTO ->
                return userDataRepository.getLoggedUser()
                    .map { userModel ->
                        return login(
                            userModel.email,
                            passwordDTO,
                            userModel.country.countries.first(),
                        )
                    }
                    .mapFailure {
                        return Failure(Error.UnregisteredUser("No user found in device"))
                    }
            }
            .mapFailure {
                return Failure(Error.UnregisteredUser("No password found in device"))
            }
    }

    fun isQuickLoginActive(): OperationResult<Boolean, Error> = userDataRepository.getQuickLoginSettings().map { it.passcodeActive }

    fun getExistingUser(): OperationResult<UserDTO, Error> {
        return userDataRepository.getLoggedUser()
            .map {
                return if (it.isLimited()) Failure(Error.UnregisteredUser())
                else Success(it)
            }
    }

    private fun updatePassword(passwordDTO: PasswordDTO) {
        userDataRepository.setPassword(passwordDTO)
    }

    private fun saveUserDTO(passwordDTO: PasswordDTO) {
        with(userDataRepository) {
            setPassword(passwordDTO)
            getUserStatus()
        }
    }

    fun getUserEmail() = userDataRepository.getLoggedUser().map { it.email }

    fun hasPasscode(): OperationResult<Boolean, Error> {
        return userDataRepository.getQuickLoginSettings().map {
            return Success(it.passcodeActive)
        }
    }

    fun isBiometricsEnabled(): OperationResult<Boolean, Error> {
        return userDataRepository.getQuickLoginSettings().map {
            it.biometricsActive
        }
    }

    fun validatePassCode(passCodeDTO: PassCodeDTO): OperationResult<Boolean, Error> {
        return userDataRepository.getQuickLoginSettings().map {
            return if (it.passcodeActive) {
                userDataRepository.getPassCode()
                    .map {
                        return if (it == passCodeDTO) {
                            Success(true)
                        } else {
                            Failure(Error.OperationCompletedWithError())
                        }
                    }
            } else {
                Failure(Error.OperationCompletedWithError())
            }
        }
    }

    fun resetQuickLogin() = userDataRepository.deleteQuickLogin()

    fun getCountries(): OperationResult<CountriesDTO, Error> {
        return getUserLocation()
            .map { location ->
                return countryRepository.getOriginCountries(location.latitude, location.longitude)
            }.mapFailure {
                return countryRepository.getOriginCountries()
            }
    }

    private fun getUserLocation(): OperationResult<SWCoordinates, Error> = locationRepository.getUserLocation()
}
