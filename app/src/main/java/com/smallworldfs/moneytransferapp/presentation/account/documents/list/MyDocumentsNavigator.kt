package com.smallworldfs.moneytransferapp.presentation.account.documents.list

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import com.smallworldfs.moneytransferapp.BuildConfig
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.SmallWorldApplication
import com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.DocumentUIModel
import com.smallworldfs.moneytransferapp.presentation.account.documents.selector.DocumentsSelectorActivity
import com.smallworldfs.moneytransferapp.presentation.account.documents.upload.UploadDocumentsActivity
import com.smallworldfs.moneytransferapp.presentation.autentix.DocumentValidationActivity
import java.io.File
import javax.inject.Inject

class MyDocumentsNavigator @Inject constructor(
    private val activity: Activity
) {
    fun navigateToDownloadDocument(file: File) {
        val filePath: Uri = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            FileProvider.getUriForFile(
                activity.baseContext,
                BuildConfig.APPLICATION_ID + ".fileprovider",
                file
            )
        } else {
            Uri.fromFile(file)
        }

        val pdfIntent = Intent(Intent.ACTION_VIEW)
        pdfIntent.setDataAndType(filePath, "application/pdf")
        pdfIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        pdfIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        pdfIntent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY)

        val i = Intent.createChooser(pdfIntent, SmallWorldApplication.getStr(R.string.open_document))
        activity.startActivity(i)
        activity.overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left)
    }

    fun navigateToSelectTypeOfDocument(documentUIModel: DocumentUIModel, attachResultListener: ActivityResultLauncher<Intent>) {
        val i = Intent(activity, DocumentsSelectorActivity::class.java)
        i.putExtra(DocumentsSelectorActivity.DOCUMENT, documentUIModel)
        attachResultListener.launch(i)
        activity.overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left)
    }

    fun navigateToManualUpload(data: DocumentUIModel, attachResultListener: ActivityResultLauncher<Intent>) {
        val intent = Intent(activity, UploadDocumentsActivity::class.java)
        intent.putExtra(DocumentsSelectorActivity.DOCUMENT_SELECTION, data)
        attachResultListener.launch(intent)
        activity.overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left)
    }

    fun navigateToUrl(url: String?) {
        val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
        activity.startActivity(i)
        activity.overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left)
    }

    fun navigateToHome() {
        val i = Intent(activity, HomeActivity::class.java)
        activity.startActivity(i)
        activity.overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left)
    }

    fun navigateToValidateDocument(documentUIModel: DocumentUIModel, documentType: String, faceCompare: Boolean, attachResultListener: ActivityResultLauncher<Intent>) {
        val intent = Intent(activity, DocumentValidationActivity::class.java)
        intent.putExtra(DocumentValidationActivity.FACE_COMPARE, faceCompare)
        intent.putExtra(DocumentValidationActivity.DOCUMENT_TYPE, documentType)
        intent.putExtra(DocumentValidationActivity.DOCUMENT, documentUIModel)
        attachResultListener.launch(intent)
        activity.overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left)
    }
}
