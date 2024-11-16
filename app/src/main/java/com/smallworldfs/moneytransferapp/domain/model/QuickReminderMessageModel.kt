package com.smallworldfs.moneytransferapp.domain.model

import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

data class QuickReminderMessageModel(
    var title: String? = STRING_EMPTY,
    var description: String? = STRING_EMPTY
)
