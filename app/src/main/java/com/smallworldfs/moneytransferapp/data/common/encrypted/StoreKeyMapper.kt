package com.smallworldfs.moneytransferapp.data.common.encrypted

import com.smallworldfs.moneytransferapp.data.userdata.local.UserDataLocalDatasource
import javax.inject.Inject

class StoreKeyMapper @Inject constructor() {

    fun map(newKey: String): String =
        when (newKey) {
            UserDataLocalDatasource.PASSCODE_KEY -> "passcode"
            UserDataLocalDatasource.PASSWORD_KEY -> "password"
            UserDataLocalDatasource.USER_KEY -> "user"
            UserDataLocalDatasource.CURRENT_USER_ALIAS -> "current_user"
            UserDataLocalDatasource.TEMP_PASSWORD_KEY -> "temp_password_key"
            UserDataLocalDatasource.PASSWORD_TEMP_KEY -> "password_temp_key"
            else -> ""
        }
}
