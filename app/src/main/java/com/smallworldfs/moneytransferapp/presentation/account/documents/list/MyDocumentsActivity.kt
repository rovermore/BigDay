package com.smallworldfs.moneytransferapp.presentation.account.documents.list

import android.app.Activity
import android.os.Bundle
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showDoubleActionGeneralDialog
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showSingleActionInfoDialog
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.EventObserver
import com.smallworldfs.moneytransferapp.databinding.ActivityMyDocumentsListBinding
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.ComplianceDocUIModel
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.DocumentUIModel
import com.smallworldfs.moneytransferapp.presentation.account.documents.selector.DocumentsSelectorActivity
import com.smallworldfs.moneytransferapp.presentation.autentix.DocumentValidationActivity
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.gone
import com.smallworldfs.moneytransferapp.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MyDocumentsActivity : GenericActivity() {

    private val viewModel: MyDocumentsViewModel by viewModels()

    @Inject
    lateinit var navigator: MyDocumentsNavigator

    private var _binding: ActivityMyDocumentsListBinding? = null
    private val binding get() = _binding!!

    private lateinit var documentsAdapter: MyDocumentsAdapter

    private val documentsListener = object : OnDocumentActionClickListener {
        override fun onValidateIdentity(document: ComplianceDocUIModel.FullValidationUIModel) = validateIdentity(
            document
        )
        override fun onUploadDocument(document: DocumentUIModel) = uploadDocument(document)
        override fun onDownloadDocument(document: DocumentUIModel) = downloadDocument(document)
        override fun onNavigateToUrl(url: String?) = navigator.navigateToUrl(url)
    }

    private val manualUploadResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.getDocuments()
        }

    private val attachDocumentResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                RESULT_OK -> {
                    val document = result.data?.getSerializableExtra(DocumentValidationActivity.DOCUMENT) as DocumentUIModel
                    viewModel.verifyDocumentStatus(document)
                }

                DocumentValidationActivity.AUTENTIX_ERROR -> {
                    val document = result.data?.getSerializableExtra(DocumentValidationActivity.DOCUMENT) as DocumentUIModel
                    showDoubleActionGeneralDialog(
                        title = getString(R.string.validatingDocumentErrorTitle),
                        content = getString(R.string.validatingDocumentErrorMessage),
                        positiveText = getString(R.string.close),
                        negativeAction = { navigator.navigateToManualUpload(document, manualUploadResult) },
                        negativeText = getString(R.string.validatingDocumentErrorButton),
                    )
                }
            }
        }

    private val identityTypeSelectorResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    val selection = result.data?.getStringExtra(DocumentsSelectorActivity.DOCUMENT_SELECTION) ?: STRING_EMPTY
                    val document = result.data?.getSerializableExtra(DocumentsSelectorActivity.DOCUMENT) as DocumentUIModel
                    navigator.navigateToValidateDocument(
                        document,
                        selection,
                        true,
                        attachDocumentResult
                    )
                }
            }
        }

    private val documentTypeSelectorResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            when (result.resultCode) {
                Activity.RESULT_OK -> {
                    val selection = result.data?.getStringExtra(DocumentsSelectorActivity.DOCUMENT_SELECTION) ?: STRING_EMPTY
                    val document = result.data?.getSerializableExtra(DocumentsSelectorActivity.DOCUMENT) as DocumentUIModel
                    navigator.navigateToValidateDocument(
                        document,
                        selection,
                        false,
                        attachDocumentResult
                    )
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMyDocumentsListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    registerEvent("click_back")
                    finish()
                }
            }
        )

        setupClickListeners()
        setupView()
        setUpObservers()

        viewModel.getDocuments()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setUpObservers() {
        viewModel.myDocuments.observe(
            this,
            EventObserver { myDocuments ->
                documentsAdapter.updateRequiredDocuments(myDocuments.requiredDocuments.sortedBy { it.status.ordinal })
                documentsAdapter.updateUploadedDocuments(myDocuments.uploadedDocuments.sortedBy { it.status.ordinal })
            }
        )

        viewModel.complianceFile.observe(
            this,
            EventObserver {
                navigator.navigateToDownloadDocument(it)
            }
        )

        viewModel.isLoading.observe(
            this,
            EventObserver { isLoading ->
                if (isLoading) {
                    binding.activityDocumentsListProgressBar.visible()
                } else {
                    binding.activityDocumentsListProgressBar.gone()
                }
            }
        )

        viewModel.documentValidated.observe(
            this,
            EventObserver {
                viewModel.getDocuments()
                showSingleActionInfoDialog(
                    getString(R.string.action_done_transactional_calculator),
                    getString(R.string.document_uploaded_ok_text)
                ) {}
            }
        )

        viewModel.launchManualUpload.observe(
            this,
            EventObserver { document ->
                showDoubleActionGeneralDialog(
                    title = getString(R.string.validatingDocumentErrorTitle),
                    content = getString(R.string.validatingDocumentErrorMessage),
                    positiveText = getString(R.string.close),
                    negativeAction = { navigator.navigateToManualUpload(document, manualUploadResult) },
                    negativeText = getString(R.string.validatingDocumentErrorButton),
                )
            }
        )

        viewModel.documentsError.observe(
            this,
            EventObserver {
                showGenericErrorView(binding.documentsErrorView.root)
            }
        )
    }

    private fun setupView() {
        setupToolbar()
        setupClickListeners()
        setupRecyclerViews()
        setupRefreshLayoutAction()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.activityDocumentsListToolbar)
        supportActionBar?.title = getString(R.string.my_documents_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupClickListeners() {
        with(binding) {
            sendMoneyButton.setOnClickListener {
                registerEvent("click_send_money")
                navigator.navigateToHome()
            }
        }
    }

    private fun setupRefreshLayoutAction() {
        with(binding.activityDocumentsListRefreshLayout) {
            setColorSchemeColors(
                ContextCompat.getColor(context, R.color.main_blue),
                ContextCompat.getColor(context, R.color.main_blue),
                ContextCompat.getColor(context, R.color.main_blue)
            )
            setOnRefreshListener {
                binding.activityDocumentsListRefreshLayout.isRefreshing = false
                viewModel.getDocuments()
            }
        }
    }

    private fun setupRecyclerViews() {
        documentsAdapter = MyDocumentsAdapter(
            this,
            mutableListOf(),
            mutableListOf(),
            documentsListener,
        )

        binding.documentList.apply {
            layoutManager = LinearLayoutManager(this@MyDocumentsActivity)
            adapter = documentsAdapter
            itemAnimator = DefaultItemAnimator()
        }
    }

    private fun uploadDocument(document: DocumentUIModel) {
        when (document) {
            is ComplianceDocUIModel.FullValidationUIModel -> navigator.navigateToSelectTypeOfDocument(
                document,
                documentTypeSelectorResult
            )
            is ComplianceDocUIModel -> {
                if (document.type == ComplianceDocUIModel.ComplianceType.ID_MISSING_OR_EXPIRED) {
                    navigator.navigateToSelectTypeOfDocument(document, documentTypeSelectorResult)
                } else {
                    navigator.navigateToManualUpload(document, manualUploadResult)
                }
            }
        }
    }

    private fun validateIdentity(fullValidationUIModel: ComplianceDocUIModel.FullValidationUIModel) {
        navigator.navigateToSelectTypeOfDocument(fullValidationUIModel, identityTypeSelectorResult)
    }

    private fun downloadDocument(document: DocumentUIModel) {
        viewModel.getAttachmentById(
            document.uid,
            if (document is ComplianceDocUIModel) document.subtype else "document"
        )
    }

    private fun registerEvent(eventAction: String) {
        trackEvent(
            UserActionEvent(
                ScreenCategory.DASHBOARD.value,
                eventAction,
                "",
                getHierarchy(""),
            ),
        )
    }
}
