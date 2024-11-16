package com.smallworldfs.moneytransferapp.presentation.mtn.model

import com.smallworldfs.moneytransferapp.utils.Constants

data class MtnStatusUIModel(
    val statusList: List<StatusUIModel>,
    val country: String,
    val mtn: String,
    val isQrMode: Boolean = false
)
sealed class StatusUIModel(val title: String, val status: String) {
    data class Done(val stepText: String) : StatusUIModel(stepText, Constants.TRACKER_STATUS.DONE)
    data class InProgress(val stepText: String) : StatusUIModel(stepText, Constants.TRACKER_STATUS.IN_PROGRESS)
    data class Pending(val stepText: String) : StatusUIModel(stepText, Constants.TRACKER_STATUS.PENDING)
    data class Cancelled(val stepText: String) : StatusUIModel(stepText, Constants.TRACKER_STATUS.CANCELLED)
}
