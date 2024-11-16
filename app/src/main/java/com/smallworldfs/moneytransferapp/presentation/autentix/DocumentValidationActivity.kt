package com.smallworldfs.moneytransferapp.presentation.autentix

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.activity.viewModels
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.DialogExt
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showSingleActionErrorDialog
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showValidationLoadingDialog
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.EventObserver
import com.smallworldfs.moneytransferapp.databinding.ActivityAutentixBinding
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.DocumentUIModel
import com.smallworldfs.moneytransferapp.presentation.account.documents.selector.DocumentsSelectorActivity
import com.smallworldfs.moneytransferapp.presentation.autentix.model.AutentixErrorMessageUIModel
import com.smallworldfs.moneytransferapp.presentation.autentix.model.AutentixIgnoredEventUIModel
import com.smallworldfs.moneytransferapp.presentation.autentix.model.AutentixSuccessMessageUIModel
import com.smallworldfs.moneytransferapp.presentation.autentix.model.AutentixViewConfig
import com.smallworldfs.moneytransferapp.presentation.autentix.model.CapabilityBridge
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.gone
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DocumentValidationActivity : GenericActivity() {

    companion object {
        const val PENDING = "PENDING"
        const val FINISHED = "FINISHED"
        const val FACE_COMPARE = "faceCompare"
        const val DOCUMENT_TYPE = "documentType"
        const val DOCUMENT = "document"
        const val AUTENTIX_ERROR = 1
    }

    private val viewModel: DocumentValidationViewModel by viewModels()

    @Inject
    lateinit var capabilityBridge: CapabilityBridge

    private var _binding: ActivityAutentixBinding? = null
    private val binding get() = _binding!!

    private var dialog: Dialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAutentixBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()

        val faceCompare = intent.getBooleanExtra(FACE_COMPARE, false)

        val documentType = intent.getStringExtra(DOCUMENT_TYPE) ?: STRING_EMPTY

        viewModel.startSession(faceCompare, documentType)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupObservers() {

        viewModel.autentixViewConfig.observe(
            this,
            EventObserver {
                startAutentixSession(it)
            },
        )

        viewModel.onError.observe(
            this,
            EventObserver {
                showErrorView()
            },
        )

        viewModel.onAutentixEvent.observe(
            this,
            EventObserver {
                when (it) {
                    is AutentixErrorMessageUIModel -> showErrorView()
                    is AutentixSuccessMessageUIModel -> {
                        showLoadingView()
                        viewModel.checkAutentixSessionStatus(
                            viewModel.autentixViewConfig.value?.peekContent()?.timeout
                                ?: 0L,
                            viewModel.autentixViewConfig.value?.peekContent()?.externalId
                                ?: STRING_EMPTY,
                        )
                    }

                    is AutentixIgnoredEventUIModel -> {}
                }
            },
        )

        viewModel.autentixSessionStatus.observe(
            this,
            EventObserver {
                hideLoadingView()
                close(true)
            },
        )
    }

    private fun showLoadingView() {
        dialog = showValidationLoadingDialog(
            getString(R.string.validating_your_id_title),
            getString(R.string.validating_your_id_description),
        )
        binding.webview.gone()
        binding.generalLoadingView.gone()
    }

    private fun hideLoadingView() {
        dialog?.dismiss()
        dialog = null
    }

    private fun startAutentixSession(autentixConfig: AutentixViewConfig) {
        binding.generalLoadingView.root.gone()
        with(binding.webview) {
            webChromeClient = capabilityBridge
            settings.javaScriptEnabled = true
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.mediaPlaybackRequiresUserGesture = false
            addJavascriptInterface(autentixConfig.localStorageJavaScriptInterface, "LocalStorage")
            addJavascriptInterface(autentixConfig.jsInterface, "webview")
            settings.setGeolocationDatabasePath(filesDir.parentFile.path + "/databases/")
            settings.domStorageEnabled = true
            settings.databaseEnabled = true
            settings.databasePath = filesDir.parentFile.path + "/databases/"

            webViewClient = object : WebViewClient() {
                override fun shouldOverrideUrlLoading(view: WebView?, url: String): Boolean {
                    view?.loadUrl(url)
                    return true
                }
            }
            loadUrl(autentixConfig.url)
        }
    }

    private fun close(success: Boolean) {
        val document = intent.extras!!.getSerializable(DocumentsSelectorActivity.DOCUMENT) as DocumentUIModel
        setResult(
            if (success) {
                RESULT_OK
            } else {
                AUTENTIX_ERROR
            },
            Intent().putExtra(
                DOCUMENT,
                document,
            ),
        )
        finish()
    }

    private fun showErrorView() {
        hideLoadingView()
        binding.generalLoadingView.root.visibility = View.GONE
        if (dialog == null) {
            dialog = showSingleActionErrorDialog(
                getString(R.string.generic_error_view_text),
                getString(R.string.generic_error_view_subtitle),
                object : DialogExt.OnPositiveClick {
                    override fun onClick() {
                        dialog = null
                        close(false)
                    }
                },
            )
        }
    }
}
