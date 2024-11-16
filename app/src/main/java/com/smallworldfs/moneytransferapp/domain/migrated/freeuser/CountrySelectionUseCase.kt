package com.smallworldfs.moneytransferapp.domain.migrated.freeuser

import androidx.core.util.Pair
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.capabilities.CapabilityChecker
import com.smallworldfs.moneytransferapp.domain.migrated.capabilities.LocationRepository
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountriesDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.repository.CountryRepository
import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO
import com.smallworldfs.moneytransferapp.domain.migrated.settings.repository.SettingsRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.domain.model.Coordinates
import com.smallworldfs.moneytransferapp.domain.usertoken.repository.local.UserTokenLocal
import com.smallworldfs.moneytransferapp.modules.customization.domain.repository.AppCustomizationRepository
import com.smallworldfs.moneytransferapp.presentation.common.coordinates.SWCoordinates
import javax.inject.Inject

class CountrySelectionUseCase @Inject constructor(
    private val countryRepository: CountryRepository,
    private val userDataRepository: UserDataRepository,
    private val settingsRepository: SettingsRepository,
    private val userTokenLocal: UserTokenLocal,
    private val capabilityChecker: CapabilityChecker,
    private val locationRepository: LocationRepository,
) {

    fun getOriginCountries(coordinates: Coordinates) = when (coordinates) {
        is Coordinates.LatLon -> countryRepository.getOriginCountries(
            coordinates.latitude,
            coordinates.longitude,
        )

        else -> countryRepository.getOriginCountries()
    }

    fun getOriginCountries() = countryRepository.getOriginCountries()

    fun getDestinationCountries(originIso: String) = countryRepository.getDestinationCountries(originIso)

    fun setOriginCountry(country: CountryDTO) = countryRepository.setOriginCountry(country)

    fun setDestinationCountry(country: CountryDTO) = countryRepository.setDestinationCountry(country)

    fun createFreeUser(origin: CountryDTO, destination: CountryDTO): OperationResult<UserDTO, Error> {
        return userDataRepository.getLimitedLogin(origin, destination)
            .map {
                AppCustomizationRepository.getInstance().countrySelected = Pair(
                    origin.iso3,
                    origin.name,
                )
                userTokenLocal.setUserToken(it.userToken)
                val user = it.copy(
                    destinationCountry = destination,
                    country = CountriesDTO(countries = mutableListOf(origin)),
                )
                userDataRepository.setLoggedUser(user)
                return Success(user)
            }.mapFailure {
                return Failure(it)
            }
    }

    fun endOnboarding() {
        settingsRepository.setOnboardingShown(true)
    }

    fun getUserLocation(): OperationResult<SWCoordinates, Error> {
        return capabilityChecker.askForLocationPermissions()
            .map {
                return locationRepository.getUserLocation()
            }
    }
}
