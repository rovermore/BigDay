package com.smallworldfs.moneytransferapp.presentation.freeuser

import com.smallworldfs.moneytransferapp.domain.migrated.common.resources.country.model.StateDTO
import com.smallworldfs.moneytransferapp.presentation.common.countries.StateUIModel
import javax.inject.Inject

class StateUIModelMapper @Inject constructor() {

    fun mapToUIModel(stateDTO: StateDTO) = StateUIModel(stateDTO.code, stateDTO.name, stateDTO.logo)
    fun mapToUIModel(states: List<StateDTO>) = states.map { mapToUIModel(it) }
    fun mapToDTO(stateUIModel: StateUIModel) = StateDTO(stateUIModel.code, stateUIModel.name, stateUIModel.logo)
    fun mapToDTO(states: List<StateUIModel>) = states.map { mapToDTO(it) }
}
