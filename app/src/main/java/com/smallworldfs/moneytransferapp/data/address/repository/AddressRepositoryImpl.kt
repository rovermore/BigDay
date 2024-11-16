package com.smallworldfs.moneytransferapp.data.address.repository

import com.smallworldfs.moneytransferapp.data.address.model.AddressMapper
import com.smallworldfs.moneytransferapp.data.address.network.AddressNetworkDatasource
import com.smallworldfs.moneytransferapp.data.base.network.models.APIErrorMapper
import com.smallworldfs.moneytransferapp.domain.migrated.addess.model.AddressDTO
import com.smallworldfs.moneytransferapp.domain.migrated.addess.repository.AddressRepository
import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.Failure
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import com.smallworldfs.moneytransferapp.domain.migrated.base.mapFailure
import com.smallworldfs.moneytransferapp.domain.migrated.base.peek
import com.smallworldfs.moneytransferapp.domain.migrated.base.peekFailure
import com.smallworldfs.moneytransferapp.domain.migrated.userdata.repository.UserDataRepository
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import java.util.Locale
import javax.inject.Inject

class AddressRepositoryImpl @Inject constructor(
    private val addressNetworkDatasource: AddressNetworkDatasource,
    private val userDataRepository: UserDataRepository,
    private val apiErrorMapper: APIErrorMapper,
    private val addressMapper: AddressMapper
) : AddressRepository {

    override fun searchAddressByText(text: String): OperationResult<List<AddressDTO>, Error> {
        userDataRepository.getLoggedUser()
            .peek {
                addressNetworkDatasource.searchAddress(
                    Locale.getDefault().language.toLowerCase(),
                    text,
                    it.country.countries[0].iso3,
                    it.userToken,
                    it.uuid
                )
                    .map {
                        return addressMapper.map(it)
                    }.mapFailure {
                        return Failure(apiErrorMapper.map(it))
                    }
            }.mapFailure {
                return Failure(it)
            }
        return Failure(Error.OperationCompletedWithError("Address not found"))
    }

    override fun searchAddressByParentId(parentId: String): OperationResult<List<AddressDTO>, Error> {
        userDataRepository.getLoggedUser()
            .peek {
                addressNetworkDatasource.searchAddressByParentId(
                    Locale.getDefault().language.toLowerCase(),
                    parentId,
                    it.country.countries[0].iso3,
                    it.userToken,
                    it.uuid
                )
                    .map {
                        return addressMapper.map(it)
                    }.mapFailure {
                        return Failure(apiErrorMapper.map(it))
                    }
            }.mapFailure {
                return Failure(it)
            }
        return Failure(Error.OperationCompletedWithError("Address not found"))
    }

    override fun getAddressById(id: String): OperationResult<List<Field>, Error> {
        userDataRepository.getLoggedUser()
            .peek {
                addressNetworkDatasource.getAddressById(
                    Locale.getDefault().language.toLowerCase(),
                    id,
                    it.country.countries[0].iso3,
                    it.userToken,
                    it.uuid
                )
                    .peek {
                        return Success(it.data.form)
                    }.peekFailure {
                        return Failure(apiErrorMapper.map(it))
                    }
            }.mapFailure {
                return Failure(it)
            }
        return Failure(Error.OperationCompletedWithError("Address not found"))
    }

    override fun getAddressForm(): OperationResult<List<Field>, Error> {
        userDataRepository.getLoggedUser()
            .peek {
                addressNetworkDatasource.getAddressForm(
                    Locale.getDefault().language.toLowerCase(),
                    it.country.countries[0].iso3,
                    it.userToken,
                    it.uuid
                )
                    .peek {
                        return Success(it.data.form)
                    }
                    .peekFailure {
                        return Failure(apiErrorMapper.map(it))
                    }
            }.mapFailure {
                return Failure(it)
            }
        return Failure(Error.OperationCompletedWithError("Address not found"))
    }
}
