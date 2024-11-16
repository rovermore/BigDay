package com.smallworldfs.moneytransferapp.presentation.autentix.model

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.webkit.GeolocationPermissions
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.domain.migrated.capabilities.CapabilityChecker
import com.smallworldfs.moneytransferapp.presentation.base.PickPictureContract
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import javax.inject.Inject

class CapabilityBridge @Inject constructor(
    private val activity: AppCompatActivity,
    private val capabilityChecker: CapabilityChecker
) : WebChromeClient() {

    private var filePathCallback: ValueCallback<Array<Uri>>? = null

    private val startDocumentPicker =
        activity.registerForActivityResult(
            PickPictureContract(),
        ) {
            it?.let { filePathCallback?.onReceiveValue(arrayOf(it)) }
        }

    private fun requestPermissions(permissions: Array<String>, onGrantedPermissions: (Boolean, List<PermissionGrantedResponse>) -> Unit) {
        capabilityChecker.requestPermissions(activity, permissions = *permissions, onGrantedPermissions = onGrantedPermissions)
    }

    override fun onPermissionRequest(request: PermissionRequest?) {
        val mappedPermissions = mapPermissions(request?.resources?.toList() ?: emptyList()).toTypedArray()
        requestPermissions(mappedPermissions) { arePermissionsGranted, permissionsGrantedList ->
            val permissionsList: List<String> = mapToPermissionRequest(permissionsGrantedList)
            if (arePermissionsGranted) request?.grant(permissionsList.toTypedArray())
        }
    }

    private fun mapPermissions(webPermissions: List<String>): List<String> {
        val mappedPermissions = mutableListOf<String>()
        webPermissions.forEach {
            when (it) {
                PermissionRequest.RESOURCE_VIDEO_CAPTURE -> {
                    mappedPermissions.add(Manifest.permission.CAMERA)
                    mappedPermissions.add(Manifest.permission.RECORD_AUDIO)
                    mappedPermissions.add(Manifest.permission.MODIFY_AUDIO_SETTINGS)
                }
                PermissionRequest.RESOURCE_AUDIO_CAPTURE -> Manifest.permission.RECORD_AUDIO
                else -> STRING_EMPTY
            }
        }
        return mappedPermissions
    }

    private fun mapToPermissionRequest(permissions: List<PermissionGrantedResponse>) = permissions.map { permission ->
        when (permission.permissionName) {
            Manifest.permission.CAMERA -> PermissionRequest.RESOURCE_VIDEO_CAPTURE
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.MODIFY_AUDIO_SETTINGS -> PermissionRequest.RESOURCE_AUDIO_CAPTURE
            else -> STRING_EMPTY
        }
    }

    override fun onGeolocationPermissionsShowPrompt(origin: String?, callback: GeolocationPermissions.Callback?) {
        callback?.let { callback ->
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)) { arePermissionsGranted, _ ->
                if (arePermissionsGranted && origin != null) callback.invoke(origin, false, false)
            }
        }
    }

    override fun onShowFileChooser(webView: WebView?, filePathCallback: ValueCallback<Array<Uri>>?, fileChooserParams: FileChooserParams?): Boolean {
        this.filePathCallback = filePathCallback
        val intent = Intent().apply {
            type = "image/*"
            action = Intent.ACTION_GET_CONTENT
            addCategory(Intent.CATEGORY_OPENABLE)
        }
        startDocumentPicker.launch(intent)
        return true
    }

    override fun getDefaultVideoPoster(): Bitmap? {
        val bitmap = BitmapFactory.decodeResource(
            activity.resources,
            R.drawable.ic_splash_logo,
        )
        return if (super.getDefaultVideoPoster() == null) {
            bitmap
        } else {
            super.getDefaultVideoPoster()
        }
    }
}
