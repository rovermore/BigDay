package com.smallworldfs.moneytransferapp.mocks.dto

import com.smallworldfs.moneytransferapp.domain.migrated.mtn.model.MtnStatusDTO
import com.smallworldfs.moneytransferapp.domain.migrated.mtn.model.StatusDTO

object MtnStatusDTOMock {

    private val statusDTO = StatusDTO(
        "id",
        "title",
        "status"
    )

    val mtnStatusDTO = MtnStatusDTO(
        "status",
        listOf(statusDTO, statusDTO),
        "country",
        "mtn"
    )
}
