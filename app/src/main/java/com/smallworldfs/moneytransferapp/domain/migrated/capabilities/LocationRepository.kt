package com.smallworldfs.moneytransferapp.domain.migrated.capabilities

import com.smallworldfs.moneytransferapp.domain.migrated.base.Error
import com.smallworldfs.moneytransferapp.domain.migrated.base.OperationResult
import com.smallworldfs.moneytransferapp.presentation.common.coordinates.SWCoordinates

interface LocationRepository {
    fun getUserLocation(): OperationResult<SWCoordinates, Error>
    fun calculateDistance(location1: SWCoordinates, location2: SWCoordinates): OperationResult<String, Error>
}
