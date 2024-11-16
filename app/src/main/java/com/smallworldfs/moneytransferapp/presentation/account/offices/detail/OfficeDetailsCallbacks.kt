package com.smallworldfs.moneytransferapp.presentation.account.offices.detail

import com.smallworldfs.moneytransferapp.presentation.account.offices.model.OfficeUIModel

interface OfficeDetailsCallbacks {
    fun onPhoneClicked(phone: String)
    fun onMailClicked(mail: String)
    fun onIndicationsClicked(officeSelected: OfficeUIModel)
    fun clickBack()
    fun registerEvent(eventAction: String)
}
