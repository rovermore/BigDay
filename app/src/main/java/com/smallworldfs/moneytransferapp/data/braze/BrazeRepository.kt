package com.smallworldfs.moneytransferapp.data.braze

import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEvent

interface BrazeRepository {

    fun logEvent(event: BrazeEvent)

    fun changeUser(uuid: String)
}
