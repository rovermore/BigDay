package com.smallworldfs.moneytransferapp.utils

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

// Avoid collision with Freshchat SDK
class OwnFileProvider : FileProvider() {

    fun getOwnUriForFile(context: Context, authority: String, file: File): Uri = getUriForFile(context, authority, file)
}
