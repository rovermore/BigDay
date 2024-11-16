package com.smallworldfs.moneytransferapp.presentation.mtn.model

import com.smallworldfs.moneytransferapp.domain.migrated.mtn.model.MtnStatusDTO
import com.smallworldfs.moneytransferapp.utils.Constants
import javax.inject.Inject

class MtnStatusUIModelMapper @Inject constructor() {

    fun map(mtnStatusDTO: MtnStatusDTO): MtnStatusUIModel {
        val statusUIModel = mutableListOf<StatusUIModel>()
        mtnStatusDTO.statusList.forEach {
            statusUIModel.add(mapStatusUIModel(it.title, it.status))
        }
        return MtnStatusUIModel(
            statusUIModel,
            mtnStatusDTO.country,
            mtnStatusDTO.mtn
        )
    }

    private fun mapStatusUIModel(title: String, status: String): StatusUIModel {
        return when (status) {
            Constants.TRACKER_STATUS.DONE -> StatusUIModel.Done(title)
            Constants.TRACKER_STATUS.IN_PROGRESS -> StatusUIModel.InProgress(title)
            Constants.TRACKER_STATUS.PENDING -> StatusUIModel.Pending(title)
            else -> StatusUIModel.Cancelled(title)
        }
    }
}
