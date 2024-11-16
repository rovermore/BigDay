package com.smallworldfs.moneytransferapp.presentation.account.contact

import com.smallworldfs.moneytransferapp.modules.support.model.ContactSupportInfoUIModel
import com.smallworldfs.moneytransferapp.presentation.login.model.UserUIModel

interface SelectContactSupportListener {

    fun onFaqsClick()
    fun onChatClick()
    fun onEmailClick(userUIModel: UserUIModel, contactSupportInfoUIModel: ContactSupportInfoUIModel)
    fun onBackPressed()
}
