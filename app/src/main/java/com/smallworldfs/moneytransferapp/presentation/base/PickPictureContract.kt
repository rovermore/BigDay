package com.smallworldfs.moneytransferapp.presentation.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

class PickPictureContract : ActivityResultContract<Intent, Uri>() {

    override fun createIntent(context: Context, input: Intent): Intent {
        return input
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri {
        return if (resultCode == Activity.RESULT_OK) {
            return intent?.data ?: Uri.parse("")
        } else {
            Uri.parse("")
        }
    }
}
