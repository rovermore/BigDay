package com.smallworldfs.moneytransferapp.domain.migrated.version

import android.content.Context
import android.content.pm.PackageManager
import com.smallworldfs.moneytransferapp.domain.migrated.version.models.Version
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import javax.inject.Inject

class VersionExtractor @Inject constructor() {

    @Inject
    lateinit var context: Context

    fun getAppVersion(): Version {
        return try {
            val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            Version(pInfo.versionName)
        } catch (e: PackageManager.NameNotFoundException) {
            Version(STRING_EMPTY)
        }
    }
}
