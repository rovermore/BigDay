package com.smallworldfs.moneytransferapp.data.braze

import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEvent
import com.smallworldfs.moneytransferapp.data.settings.local.SettingsLocalDataSource
import com.smallworldfs.moneytransferapp.domain.migrated.base.map
import javax.inject.Inject

class BrazeRepositoryImpl @Inject constructor(
    private val settingsLocalDataSource: SettingsLocalDataSource,
    private val brazeDatasource: BrazeDatasource
) : BrazeRepository {

    override fun logEvent(event: BrazeEvent) {
        settingsLocalDataSource.getBrazeStatus()
            .map { isBrazeEnabled ->
                if (isBrazeEnabled) brazeDatasource.logEvent(event)
            }
    }

    override fun changeUser(uuid: String) {
        brazeDatasource.changeUser(uuid)
    }
}
