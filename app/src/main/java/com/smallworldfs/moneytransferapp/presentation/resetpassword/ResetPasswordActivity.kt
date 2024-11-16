package com.smallworldfs.moneytransferapp.presentation.resetpassword

import android.os.Bundle
import androidx.activity.compose.setContent
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResetPasswordActivity : GenericActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var userToken: String? = ""
        if (intent.extras != null) {
            userToken = intent.extras!!.getString(Constants.DEEP_LINK.APP_TOKEN_EXTRA, "")
        }

        setContent {
            ResetPasswordLayout(
                userToken = userToken ?: STRING_EMPTY,
                onBackAction = { finish() },
            )
        }
    }
}
