package com.smallworldfs.moneytransferapp.presentation.account.profile.edit.model

import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field

data class FormUIModel(
    val fields: ArrayList<Field> = arrayListOf()
)
