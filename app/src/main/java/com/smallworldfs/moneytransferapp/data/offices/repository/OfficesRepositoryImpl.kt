package com.smallworldfs.moneytransferapp.data.offices.repository

import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.data.common.resources.country.model.CountriesDTOMapper
import com.smallworldfs.moneytransferapp.data.offices.model.CityDTOMapper
import com.smallworldfs.moneytransferapp.data.offices.model.OfficeDTOMapper
import com.smallworldfs.moneytransferapp.data.offices.network.OfficeNetworkDatasource
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.peek
import com.smallworldfs.moneytransferapp.domain.migrated.base.peekFailure
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountriesDTO
import com.smallworldfs.moneytransferapp.domain.migrated.offices.model.CityDTO
import com.smallworldfs.moneytransferapp.domain.migrated.offices.model.OfficeDTO
import com.smallworldfs.moneytransferapp.domain.migrated.offices.repository.OfficesRepository
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import javax.inject.Inject

class OfficesRepositoryImpl @Inject constructor(
    private val officeNetworkDatasource: OfficeNetworkDatasource,
    private val userDataRepository: UserDataRepository,
    private val apiErrorMapper: APIErrorMapper,
    private val countriesDTOMapper: CountriesDTOMapper,
    private val cityDTOMapper: CityDTOMapper,
    private val officeDTOMapper: OfficeDTOMapper
) : OfficesRepository {

    private val CONTRY_MAPCOUNTRIES_TYPE = "mapcountries"

    override fun requestCountries(): OperationResult<CountriesDTO, Error> {
        userDataRepository.getLoggedUser().peek { userDTO ->
            return officeNetworkDatasource.requestCountries(
                CONTRY_MAPCOUNTRIES_TYPE,
                userDTO.country.countries.first().iso3,
            ).map {
                return Success(countriesDTOMapper.mapFromOfficeCountryResponse(it))
            }.mapFailure {
                return Failure(apiErrorMapper.map(it))
            }
        }.peekFailure {
            return Failure(it)
        }
        return Failure(Error.UncompletedOperation("countries could not be fetched"))
    }

    override fun requestCities(country: String): OperationResult<List<CityDTO>, Error> {
        return officeNetworkDatasource.requestCities(country)
            .map {
                return Success(cityDTOMapper.map(it.cities))
            }.mapFailure {
                return Failure(apiErrorMapper.map(it))
            }
    }

    override fun requestOffices(country: String, city: String): OperationResult<List<OfficeDTO>, Error> {
        return officeNetworkDatasource.requestOffices(country, city)
            .map {
                return Success(officeDTOMapper.mapFromBranches(it))
            }.mapFailure {
                return Failure(apiErrorMapper.map(it))
            }
    }

    override fun requestOfficesPoi(
        longitude: String,
        latitude: String
    ): OperationResult<List<OfficeDTO>, Error> {
        return officeNetworkDatasource.requestPoi(longitude, latitude)
            .map {
                return Success(officeDTOMapper.mapFromLocations(it))
            }.mapFailure {
                return Failure(apiErrorMapper.map(it))
            }
    }
}
