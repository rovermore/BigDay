package com.smallworldfs.moneytransferapp.presentation.account.documents.selector

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.EventObserver
import com.smallworldfs.moneytransferapp.databinding.ActivityDocumentsSelectorBinding
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.ComplianceDocUIModel
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.TypesOfDocumentUIModel
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DocumentsSelectorActivity : GenericActivity() {

    companion object {
        const val DOCUMENT = "document"
        const val DOCUMENT_SELECTION = "documentSelection"
    }

    private var _binding: ActivityDocumentsSelectorBinding? = null
    private val binding get() = _binding!!

    private val viewModel: DocumentsSelectorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDocumentsSelectorBinding.inflate(layoutInflater)

        setupObservers()
        setupToolbar()
        setupRecyclerView()

        viewModel.getDocumentTypes()

        setContentView(binding.root)
    }

    fun setupObservers() {
        viewModel.documentTypes.observe(
            this,
            EventObserver {
                showDocument(it)
            }
        )
    }

    private fun showDocument(data: List<TypesOfDocumentUIModel>) {
        if (data.size > INT_ZERO) {
            updateList(data)
        }
    }

    /**
     * Setup UI
     */
    private fun setupToolbar() {
        setSupportActionBar(binding.documentsSelectorToolbar)
        supportActionBar?.title = getString(R.string.add_document_activity_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupRecyclerView() {
        binding.documentsSelectorRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@DocumentsSelectorActivity)
            itemAnimator = DefaultItemAnimator()
            adapter = DocumentsSelectorAdapter(
                mutableListOf(),
                object : DocumentSelectorAdapterListener {
                    override fun onItemClick(data: String) {
                        registerEvent("click_document_type", "id|passport")
                        val document = intent.extras!!.getSerializable(DOCUMENT)

                        setResult(
                            Activity.RESULT_OK,
                            Intent()
                                .putExtra(DOCUMENT_SELECTION, data)
                                .putExtra(
                                    DOCUMENT,
                                    when (document) {
                                        is ComplianceDocUIModel.FullValidationUIModel -> document.copyFullValidation(documentTypeSelected = data)
                                        is ComplianceDocUIModel -> document.copyComplianceDoc(documentTypeSelected = data)
                                        else -> document
                                    }
                                )
                        )
                        finish()
                    }
                },
            )
        }
    }

    /**
     * Override pending transaction with custom animation
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onBackPressed() {
        registerEvent("click_back", STRING_EMPTY)
        setResult(Activity.RESULT_CANCELED)
        finish()
        this.overridePendingTransition(R.anim.from_left_to_right, R.anim.from_position_to_right)
    }

    /**
     * Update list of recycler view
     */
    private fun updateList(data: List<TypesOfDocumentUIModel>) {
        with(binding.documentsSelectorRecyclerView.adapter as DocumentsSelectorAdapter) {
            setNewData(data)
        }
    }

    private fun registerEvent(eventAction: String, eventLabel: String) {
        trackEvent(
            UserActionEvent(
                ScreenCategory.TRANSFER.value,
                eventAction,
                eventLabel,
                getHierarchy(""),
                "",
                "bank_deposit",
                "",
            ),
        )
    }
}
