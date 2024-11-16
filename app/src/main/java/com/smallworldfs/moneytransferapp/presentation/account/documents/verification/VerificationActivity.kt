package com.smallworldfs.moneytransferapp.presentation.account.documents.verification

import android.app.Activity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showDoubleActionGeneralDialog
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.EventObserver
import com.smallworldfs.moneytransferapp.databinding.ActivityVerificationBinding
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.OnDocumentActionClickListener
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
class VerificationActivity : GenericActivity() {

    private val viewModel: VerificationViewModel by viewModels()

    @Inject
    lateinit var navigator: VerificationNavigator

    private var _binding: ActivityVerificationBinding? = null
    private val binding get() = _binding!!

    private lateinit var documentsAdapter: VerificationAdapter

    private val manualUploadResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            viewModel.requestDocuments()
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
        _binding = ActivityVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupClickListeners()
        setupToolbar()
        setupRefreshLayoutAction()
        setupRecyclerViews()
        setupObservers()

        viewModel.requestDocuments()
    }

    private fun setupClickListeners() {
        with(binding) {
            continueButton.setOnClickListener {
                setResult(Activity.RESULT_OK)
                finish()
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.verificationToolbar)
        supportActionBar?.title = getString(R.string.verification_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.verificationToolbar.navigationIcon = ContextCompat.getDrawable(this, R.drawable.ic_action_close_white)
    }

    private fun setupRefreshLayoutAction() {
        binding.verificationListRefreshLayout.apply { setColorSchemeColors(ContextCompat.getColor(context, R.color.main_blue), ContextCompat.getColor(context, R.color.main_blue), ContextCompat.getColor(context, R.color.main_blue)) }
        binding.verificationListRefreshLayout.setOnRefreshListener {
            binding.verificationListRefreshLayout.isRefreshing = false
            viewModel.requestDocuments()
        }
    }

    private fun setupRecyclerViews() {
        documentsAdapter = VerificationAdapter(
            this,
            mutableListOf(),
            object : OnDocumentActionClickListener {
                override fun onValidateIdentity(item: ComplianceDocUIModel.FullValidationUIModel) {
                    navigator.navigateToSelectTypeOfDocument(item, identityTypeSelectorResult)
                }
                override fun onDownloadDocument(item: DocumentUIModel) {
                    viewModel.onDownloadDocument(item)
                }
                override fun onUploadDocument(item: DocumentUIModel) {
                    when (item) {
                        is ComplianceDocUIModel.FullValidationUIModel -> {
                            navigator.navigateToSelectTypeOfDocument(item, documentTypeSelectorResult)
                        }
                        is ComplianceDocUIModel -> {
                            if (item.type == ComplianceDocUIModel.ComplianceType.ID_MISSING_OR_EXPIRED) {
                                navigator.navigateToSelectTypeOfDocument(item, documentTypeSelectorResult)
                            } else {
                                navigator.navigateToManualUpload(item, manualUploadResult)
                            }
                        }
                        else -> {}
                    }
                }
                override fun onNavigateToUrl(url: String?) {
                    url?.let {
                        navigator.navigateToUrl(url)
                    }
                }
            },
        )
        binding.verificationRecyclerViewRequiredDocuments.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = documentsAdapter
            itemAnimator = DefaultItemAnimator()
        }
    }

    private fun setupObservers() {
        viewModel.documents.observe(
            this,
            EventObserver {
                documentsAdapter.updateDocuments(it)
            }
        )

        viewModel.loading.observe(
            this,
            EventObserver { loading ->
                if (loading) {
                    binding.verificationListProgressBar.visible()
                    binding.verificationListNestedScrollView.gone()
                } else {
                    binding.verificationListProgressBar.gone()
                    binding.verificationListNestedScrollView.visible()
                }
            }
        )

        viewModel.download.observe(
            this,
            EventObserver {
                navigator.navigateToDownloadDocument(it)
            }
        )

        viewModel.documentValidated.observe(
            this,
            EventObserver {
                viewModel.requestDocuments()
                binding.verificationListTextViewRequiredDocuments.text = getString(R.string.document_uploaded_text)
                binding.continueButton.visible()
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
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                false
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.from_left_to_right, R.anim.from_position_to_right)
        setResult(Activity.RESULT_CANCELED)
        finish()
    }
}
