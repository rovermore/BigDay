package com.smallworldfs.moneytransferapp.presentation.account.documents.upload

import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.view.MenuItem
import android.widget.DatePicker
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.show
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showDateDialog
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.EventObserver
import com.smallworldfs.moneytransferapp.data.base.FileResolver
import com.smallworldfs.moneytransferapp.databinding.ActivityDocumentsFormBinding
import com.smallworldfs.moneytransferapp.domain.migrated.account.documents.model.ComplianceSubtype
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.ComplianceDocUIModel
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.DocumentIdUIModel
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.DocumentUIModel
import com.smallworldfs.moneytransferapp.presentation.account.documents.selector.DocumentsSelectorActivity
import com.smallworldfs.moneytransferapp.presentation.base.TakePictureContract
import com.smallworldfs.moneytransferapp.presentation.form.adapter.FormAdapter
import com.smallworldfs.moneytransferapp.presentation.form.selector.FormSelectorActivity
import com.smallworldfs.moneytransferapp.presentation.form.selector.FormSelectorItem
import com.smallworldfs.moneytransferapp.presentation.form.selector.FormSelectorState
import com.smallworldfs.moneytransferapp.utils.FIELD_NAME
import com.smallworldfs.moneytransferapp.utils.INPUT_STATE
import com.smallworldfs.moneytransferapp.utils.INT_ONE
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.RESULT_ITEM
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.STRING_SLASH
import com.smallworldfs.moneytransferapp.utils.forms.SubType
import com.smallworldfs.moneytransferapp.utils.forms.Type
import com.smallworldfs.moneytransferapp.utils.gone
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class UploadDocumentsActivity : GenericActivity() {

    private val viewModel: UploadDocumentsViewModel by viewModels()

    @Inject
    lateinit var formAdapter: FormAdapter

    @Inject
    lateinit var navigator: UploadDocumentsNavigator

    @Inject
    lateinit var fileResolver: FileResolver

    private var _binding: ActivityDocumentsFormBinding? = null
    private val binding get() = _binding!!

    private var frontCameraImageUri: Uri? = null
    private var backCameraImageUri: Uri? = null

    private val startFrontSelector =
        registerForActivityResult(
            ActivityResultContracts.GetContent(),
        ) { imageUri ->
            val field = formAdapter.getFormList().firstOrNull { it.name == ComplianceSubtype.FRONT.toString() }
            field?.let {
                it.value = fileResolver.getRealPathFromUri(imageUri!!)
                formAdapter.updateFormList(it, formAdapter.getFormList().indexOf(field))
            }
        }

    private val startBackSelector =
        registerForActivityResult(
            ActivityResultContracts.GetContent(),
        ) { imageUri ->
            val field = formAdapter.getFormList().firstOrNull { it.name == ComplianceSubtype.BACK.toString() }
            field?.let {
                it.value = fileResolver.getRealPathFromUri(imageUri!!)
                formAdapter.updateFormList(it, formAdapter.getFormList().indexOf(field))
            }
        }

    private val startCameraFront =
        registerForActivityResult(
            TakePictureContract(),
        ) { saved ->
            if (saved.first) {
                val field = formAdapter.getFormList().firstOrNull { it.name == ComplianceSubtype.FRONT.toString() }
                field?.let {
                    it.value = frontCameraImageUri?.let { it1 -> fileResolver.getRealPathFromUri(it1) }
                    formAdapter.updateFormList(it, formAdapter.getFormList().indexOf(field))
                }
            }
        }

    private val startCameraBack =
        registerForActivityResult(
            TakePictureContract(),
        ) { saved ->
            if (saved.first) {
                val field = formAdapter.getFormList().firstOrNull { it.name == ComplianceSubtype.BACK.toString() }
                field?.let {
                    it.value = backCameraImageUri?.let { it1 -> fileResolver.getRealPathFromUri(it1) }
                    formAdapter.updateFormList(it, formAdapter.getFormList().indexOf(field))
                }
            }
        }

    private val comboOwnSelector =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val fieldName = it.data?.getStringExtra(FIELD_NAME) as String
                val result = it.data?.getSerializableExtra(RESULT_ITEM) as FormSelectorItem
                val field = formAdapter.getFormList().firstOrNull { it.name == fieldName }
                field?.let {
                    it.value = result.value
                    formAdapter.updateFormList(it, formAdapter.getFormList().indexOf(field))
                }
            }
        }

    companion object {
        // Form values
        const val FRONT = "FRONT"
        const val TAX_CODE = "taxCode"
        const val BACK = "BACK"
        const val ISSUE_DATE = "issueDate"
        const val EXPIRATION_DATE = "expirationDate"
        const val DOCUMENT_NUMBER = "documentNumber"
        const val ISSUE_COUNTRY = "documentCountry"
        const val DOCUMENT_COUNTRY_FIELD = "documentCountry"
        const val NATIONALITY_COUNTRY_FIELD = "nationality"
        const val DOCUMENT_TYPE_FIELD = "documentType"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityDocumentsFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    registerEvent("click_back", STRING_EMPTY, STRING_EMPTY)
                    finish()
                }
            },
        )

        setupView()
        setUpObservers()

        viewModel.checkReadWritePermissions()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setUpObservers() {
        viewModel.permissionsGranted.observe(
            this,
            EventObserver { granted ->
                if (granted) {
                    val document = intent.extras!!.getSerializable(DocumentsSelectorActivity.DOCUMENT_SELECTION) as DocumentUIModel
                    val documentType = when (document) {
                        is ComplianceDocUIModel -> {
                            if (document.type == ComplianceDocUIModel.ComplianceType.ID_MISSING_OR_EXPIRED) {
                                document.documentTypeSelected
                            } else {
                                document.type
                            }
                        }

                        is DocumentIdUIModel -> {
                            document.toString()
                        }

                        else -> {
                            STRING_EMPTY
                        }
                    }
                    viewModel.getDocumentForm(document.uid, documentType.toString())
                }
            },
        )

        viewModel.form.observe(
            this,
            EventObserver {
                if (it.fields.size > INT_ZERO) formAdapter.setFormList(it.fields)
            },
        )

        viewModel.documentSaved.observe(
            this,
            EventObserver {
                finish()
            },
        )

        viewModel.onPermissionsError.observe(
            this,
            EventObserver {
                showProgressBar(false)
            },
        )

        viewModel.documentFormError.observe(
            this,
            EventObserver {
                showProgressBar(false)
            },
        )

        viewModel.saveDocumentError.observe(
            this,
            EventObserver {
                showProgressBar(false)
            },
        )

        viewModel.showProgressBar.observe(
            this,
            EventObserver {
                showProgressBar(it)
            },
        )
    }

    private fun setupView() {
        setSupportActionBar(binding.activityDocumentsFormToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setupTitle()
        setupTipsLink()

        formAdapter.initViewModel(this)

        with(binding.activityDocumentsFormRecyclerView) {
            layoutManager = LinearLayoutManager(this@UploadDocumentsActivity)
            adapter = formAdapter
            isNestedScrollingEnabled = false
        }
        binding.activityDocumentsFormSubmitButton.setOnClickListener {
            registerEvent("click_save", STRING_EMPTY, STRING_EMPTY)
            viewModel.saveDocument(
                (intent.extras!!.get(DocumentsSelectorActivity.DOCUMENT_SELECTION) as DocumentUIModel),
                formAdapter.getFormList(),
            )
        }

        formAdapter.getEvents().observe(this) { action ->
            action?.let {
                when (it) {
                    FRONT, BACK, TAX_CODE -> showUploadDocument(it)
                    else -> observeOtherFields(it)
                }
            }
        }
    }

    private fun showUploadDocument(action: String) {
        SourcePickerBottomSheet.newInstance(
            { navigateToTakeAPictureWithCamera(action) },
            { navigateToPickPicture(action) },
        ).show(this.supportFragmentManager, "source_picker")
    }

    private fun navigateToPickPicture(action: String) {
        when (action) {
            FRONT, TAX_CODE -> {
                registerEvent("click_attach_front_document", STRING_EMPTY, STRING_EMPTY)
                startFrontSelector.launch("image/*")
            }

            BACK -> {
                registerEvent("click_attach_back_document", STRING_EMPTY, STRING_EMPTY)
                startBackSelector.launch("image/*")
            }
        }
    }

    private fun navigateToTakeAPictureWithCamera(action: String) {
        when (action) {
            FRONT, TAX_CODE -> {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.TITLE, "Front Picture")
                values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
                frontCameraImageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                startCameraFront.launch(frontCameraImageUri)
            }

            BACK -> {
                val values = ContentValues()
                values.put(MediaStore.Images.Media.TITLE, "Back Picture")
                values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera")
                backCameraImageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
                startCameraBack.launch(backCameraImageUri)
            }
        }
    }

    private fun observeOtherFields(action: String) {
        val field = formAdapter.getFormList().firstOrNull { it.name == action }
        field?.let {
            when {
                it.type == Type.COMBO && it.subtype == SubType.OWN -> {
                    val listToShow: MutableList<FormSelectorItem> = mutableListOf()
                    field.data?.forEach {
                        listToShow.add(FormSelectorItem(it.firstKey(), it.getValue(it.firstKey())))
                    }
                    val state = FormSelectorState(
                        toolbarTitle = field.title,
                        listToShow = listToShow,
                        isVisibleSearchContainer = true,
                        fieldName = field.name
                    )
                    val i = Intent(this, FormSelectorActivity::class.java)
                    i.putExtra(INPUT_STATE, state)
                    if (it.name == DOCUMENT_COUNTRY_FIELD) {
                        registerEvent("click_issuing_country", STRING_EMPTY, STRING_EMPTY)
                    }
                    comboOwnSelector.launch(i)
                }
                it.type == Type.GROUP && it.subtype == SubType.GROUP_DATE -> {
                    registerEvent("click_expiration_date", STRING_EMPTY, STRING_EMPTY)
                    if (field.name == ISSUE_DATE) {
                        showDateDialog(maxDate = Date().time) { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                            // Modify field object and save it
                            it.value = dayOfMonth.toString() + STRING_SLASH + (month + INT_ONE) + STRING_SLASH + year
                            it.childs?.firstOrNull { child -> child.name.contains("day") }?.value = dayOfMonth.toString()
                            it.childs?.firstOrNull { child -> child.name.contains("month") }?.value = (month + INT_ONE).toString()
                            it.childs?.firstOrNull { child -> child.name.contains("year") }?.value = year.toString()

                            formAdapter.updateFormList(field, formAdapter.getFormList().indexOf(field))
                        }
                    } else {
                        showDateDialog(minDate = Date().time) { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                            // Modify field object and save it
                            it.value = dayOfMonth.toString() + STRING_SLASH + (month + INT_ONE) + STRING_SLASH + year
                            it.childs?.firstOrNull { child -> child.name.contains("day") }?.value = dayOfMonth.toString()
                            it.childs?.firstOrNull { child -> child.name.contains("month") }?.value = (month + INT_ONE).toString()
                            it.childs?.firstOrNull { child -> child.name.contains("year") }?.value = year.toString()

                            formAdapter.updateFormList(field, formAdapter.getFormList().indexOf(field))
                        }
                    }
                }
            }
        }
    }

    private fun setupTitle() {
        val document: ComplianceDocUIModel = intent.extras!!.get(DocumentsSelectorActivity.DOCUMENT_SELECTION) as ComplianceDocUIModel
        when (document.type) {
            ComplianceDocUIModel.ComplianceType.ID_MISSING_OR_EXPIRED -> {
                supportActionBar?.title = getString(R.string.upload_id)
                binding.titleForm.text = getString(R.string.title_document_capture_id)
            }

            ComplianceDocUIModel.ComplianceType.TAX_CODE_DOCUMENT -> {
                supportActionBar?.title = getString(R.string.upload_tax_code)
                binding.titleForm.text = getString(R.string.title_document_capture_tax_code)
            }

            else -> {
                supportActionBar?.title = getString(R.string.upload_compliance)
                binding.titleForm.text = getString(R.string.title_document_capture_compliance)
            }
        }
    }

    private fun setupTipsLink() {
        val document: DocumentUIModel = intent.extras!!.get(DocumentsSelectorActivity.DOCUMENT_SELECTION) as DocumentUIModel
        with(binding.tips) {
            val spannableText = SpannableString(getString(R.string.tips_link))
            spannableText.setSpan(UnderlineSpan(), 0, spannableText.length, 0)
            text = spannableText
            setOnClickListener { navigator.navigateToTipsActivity(document) }
        }
    }

    private fun showProgressBar(show: Boolean) {
        if (show) binding.activityDocumentsFormProgressBar.show()
        else binding.activityDocumentsFormProgressBar.gone()
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
        registerEvent("click_back", STRING_EMPTY, STRING_EMPTY)
        super.onBackPressed()
        this.overridePendingTransition(R.anim.from_left_to_right, R.anim.from_position_to_right)
    }

    private fun registerEvent(eventAction: String, eventLabel: String, formType: String) {
        trackEvent(
            UserActionEvent(
                ScreenCategory.TRANSFER.value,
                eventAction,
                eventLabel,
                getHierarchy(""),
                formType,
                "bank_deposit",
                "",
            ),
        )
    }
}
