package com.smallworldfs.moneytransferapp.domain.migrated.offices.usecase

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.capabilities.CapabilityChecker
import com.smallworldfs.moneytransferapp.domain.migrated.capabilities.LocationRepository
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountriesDTO
import com.smallworldfs.moneytransferapp.domain.migrated.offices.model.CityDTO
import com.smallworldfs.moneytransferapp.domain.migrated.offices.model.OfficeDTO
import com.smallworldfs.moneytransferapp.domain.migrated.offices.repository.OfficesRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.presentation.common.coordinates.SWCoordinates
import javax.inject.Inject

class OfficesUseCase @Inject constructor(
    private val officesRepository: OfficesRepository,
    private val userDataRepository: UserDataRepository,
    private val locationRepository: LocationRepository,
    private val capabilityChecker: CapabilityChecker
) {

    fun getAllOfficeCountries(): OperationResult<CountriesDTO, Error> {
        return officesRepository.requestCountries()
    }

    fun getCities(country: String): OperationResult<List<CityDTO>, Error> {
        return officesRepository.requestCities(country)
    }

    fun getUser() = userDataRepository.getLoggedUser()

    fun getUserLocation(): OperationResult<SWCoordinates, Error> {
        return capabilityChecker.askForLocationPermissions()
            .map {
                return locationRepository.getUserLocation()
            }
    }

    fun getOffices(country: String, city: String): OperationResult<List<OfficeDTO>, Error> {
        return officesRepository.requestOffices(country, city)
    }

    fun getOfficesPoi(longitude: String, latitude: String) = officesRepository.requestOfficesPoi(longitude, latitude)

    fun calculateDistance(userLocation: SWCoordinates, officeLocation: SWCoordinates): OperationResult<String, Error> {
        return locationRepository.calculateDistance(userLocation, officeLocation)
    }
}
