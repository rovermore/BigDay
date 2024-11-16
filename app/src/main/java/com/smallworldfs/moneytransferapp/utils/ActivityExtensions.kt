package com.smallworldfs.moneytransferapp.utils

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.ContextWrapper
import android.content.IntentFilter
import android.os.Build
import com.google.android.play.core.review.ReviewManagerFactory
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

fun Activity.arePermissionsGranted(
    permissions: Array<String>,
    onGrantedPermissions: (Boolean) -> Unit
) {
    Dexter.withContext(this)
        .withPermissions(*permissions)
        .withListener(object : MultiplePermissionsListener {
            override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                onGrantedPermissions.invoke(report.areAllPermissionsGranted())
            }

            override fun onPermissionRationaleShouldBeShown(
                permissions: List<PermissionRequest?>?,
                token: PermissionToken?
            ) {
                token?.continuePermissionRequest()
            }
        }).check()
}

fun Activity.registerBroadcastReceiver(receiver: BroadcastReceiver, filter: IntentFilter, isExported: Int) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        registerReceiver(receiver, filter, isExported)
    } else {
        registerReceiver(receiver, filter)
    }
}

fun Activity.launchInAppReview() {
    val reviewManager = ReviewManagerFactory.create(this)
    val request = reviewManager.requestReviewFlow()
    request.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val reviewInfo = task.result
            val flow = reviewManager.launchReviewFlow(this, reviewInfo)
            flow.addOnCompleteListener {
            }
        }
    }
}

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
