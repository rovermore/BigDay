package com.smallworldfs.moneytransferapp.presentation.softregister.model

import com.smallworldfs.moneytransferapp.domain.migrated.login.model.UserDTO

data class RegisterUIModel(
    val step: RegisterStep,
    val user: UserDTO
)
