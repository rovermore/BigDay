package com.smallworldfs.moneytransferapp.data.common.resources.country.repository

import androidx.core.util.Pair
import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.data.common.locale.LocaleRepository
import com.smallworldfs.moneytransferapp.data.common.resources.country.model.CountriesDTOMapper
import com.smallworldfs.moneytransferapp.data.common.resources.country.model.CountriesEntityMapper
import com.smallworldfs.moneytransferapp.data.common.resources.country.model.CountryListEntity
import com.smallworldfs.moneytransferapp.data.common.resources.country.model.CountryResponse
import com.smallworldfs.moneytransferapp.data.common.resources.country.model.StatesDTOMapper
import com.smallworldfs.moneytransferapp.data.common.resources.country.repository.local.CountryLocalDataSource
import com.smallworldfs.moneytransferapp.data.common.resources.country.repository.network.CountryNetworkDatasource
import com.smallworldfs.moneytransferapp.data.countries.model.GetOriginCountriesResponse
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.get
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountriesDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.CountryDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.StateDTO
import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.repository.CountryRepository
import com.smallworldfs.moneytransferapp.modules.country.domain.model.CountryInfoResponse
import com.smallworldfs.moneytransferapp.modules.customization.domain.repository.AppCustomizationRepository
import javax.inject.Inject

class CountryRepositoryImpl @Inject constructor(
    private val countryLocalDataSource: CountryLocalDataSource,
    private val countryNetworkDatasource: CountryNetworkDatasource,
    private val localeRepository: LocaleRepository,
    private val apiErrorMapper: APIErrorMapper,
    private val countriesDTOMapper: CountriesDTOMapper,
    private val statesDTOMapper: StatesDTOMapper,
    private val countriesEntityMapper: CountriesEntityMapper
) : CountryRepository {

    override fun getCountries(): OperationResult<CountriesDTO, Error> {
        return countryLocalDataSource.getCountries()
            .mapFailure {
                return countryNetworkDatasource.requestInfo(localeRepository.getLang())
                    .mapFailure {
                        apiErrorMapper.map(it)
                    }.map {
                        setCountriesLocalInfo(
                            it.data?.countries?.filter { country ->
                                !country.iso.isNullOrBlank() && !country.name.isNullOrBlank()
                            } ?: emptyList(),
                        )
                        return Success(countriesDTOMapper.mapFromNetworkCountries(it))
                    }
            }.map {
                countriesDTOMapper.mapFromLocalEntity(it)
            }
    }

    override fun getOriginCountries(): OperationResult<CountriesDTO, Error> {
        val localCountries = countryLocalDataSource.getCountries()
            .mapFailure {
                CountryListEntity()
            }.get()

        return countryLocalDataSource.getOriginCountries()
            .mapFailure {
                return countryNetworkDatasource.requestCountriesOrigin(localeRepository.getLang())
                    .mapFailure {
                        apiErrorMapper.map(it)
                    }.map {
                        setOriginCountriesLocalInfo(
                            it.data.countries?.filter { country ->
                                !country.iso.isNullOrBlank() && !country.name.isNullOrBlank()
                            } ?: emptyList(),
                            localCountries, it.data.located,
                        )
                        return Success(countriesDTOMapper.mapFromNetworkOriginCountries(it, localCountries))
                    }
            }.map {
                countriesDTOMapper.mapFromLocalEntity(it)
            }
    }

    override fun getOriginCountries(
        latitude: Double,
        longitude: Double
    ): OperationResult<CountriesDTO, Error> {
        val localCountries = countryLocalDataSource.getCountries()
            .mapFailure {
                CountryListEntity()
            }.get()

        return countryNetworkDatasource.requestCountriesOrigin(
            localeRepository.getLang(),
            latitude,
            longitude,
        ).mapFailure {
            apiErrorMapper.map(it)
        }.map {
            return Success(countriesDTOMapper.mapFromNetworkOriginCountries(it, localCountries))
        }
    }

    override fun getDestinationCountries(origin: String): OperationResult<CountriesDTO, Error> {
        val localCountries = countryLocalDataSource.getCountries()
            .mapFailure {
                CountryListEntity()
            }.get()

        return countryNetworkDatasource.requestDestinationCountries(
            localeRepository.getLang(),
            origin,
        ).mapFailure {
            apiErrorMapper.map(it)
        }.map {
            return Success(
                countriesDTOMapper.mapFromNetworkDestinationCountries(
                    it,
                    localCountries,
                ),
            )
        }
    }

    override fun setOriginCountry(country: CountryDTO): OperationResult<CountryDTO, Error> {
        AppCustomizationRepository.getInstance().countrySelected = Pair(country.iso3, country.name)
        return Success(country)
    }

    override fun setDestinationCountry(country: CountryDTO): OperationResult<CountryDTO, Error> {
        AppCustomizationRepository.getInstance().payoutCountrySelected = Pair(country.iso3, country.name)
        return Success(country)
    }

    override fun getStates(country: CountryDTO): OperationResult<List<StateDTO>, Error> =
        countryNetworkDatasource.getStates(localeRepository.getLang(), country.iso3)
            .mapFailure {
                apiErrorMapper.map(it)
            }
            .map {
                statesDTOMapper.mapFromNetworkStates(it)
            }

    override fun getCountryFromPhoneNumber(): OperationResult<String, Error> {
        return countryLocalDataSource.getCountryFromPhoneNumber()
    }

    /**
     * Auxiliary functions
     */
    private fun setCountriesLocalInfo(countries: List<CountryResponse.Country>) {
        val countryListEntity = countriesEntityMapper.mapFromNetworkCountries(
            countries,
        )
        countryLocalDataSource.setCountries(countryListEntity)
    }

    private fun setOriginCountriesLocalInfo(
        originCountries: List<GetOriginCountriesResponse.Country>,
        countriesEntityList: CountryListEntity,
        locatedCountry: GetOriginCountriesResponse.Located?
    ) {
        val originCountriesListEntity = countriesEntityMapper.mapFromNetworkOriginCountries(
            originCountries,
            countriesEntityList,
            locatedCountry,
        )
        countryLocalDataSource.setOriginCountries(originCountriesListEntity)
    }

    override fun requestCountryInfoLegacy(type: String, country: String): OperationResult<CountryInfoResponse, Error> =
        countryNetworkDatasource.requestCountryInfoLegacy(type, country)
            .mapFailure {
                apiErrorMapper.map(it)
            }
}
