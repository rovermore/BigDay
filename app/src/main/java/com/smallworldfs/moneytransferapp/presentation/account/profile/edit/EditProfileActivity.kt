package com.smallworldfs.moneytransferapp.presentation.account.profile.edit

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showDateDialog
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.EventObserver
import com.smallworldfs.moneytransferapp.databinding.ActivityEditProfileBinding
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.presentation.account.profile.edit.EditProfileNavigator.Companion.REQUEST_CODE_BIRTH_COUNTRY
import com.smallworldfs.moneytransferapp.presentation.account.profile.edit.EditProfileNavigator.Companion.REQUEST_CODE_CITY
import com.smallworldfs.moneytransferapp.presentation.account.profile.edit.EditProfileNavigator.Companion.REQUEST_CODE_PREFIX
import com.smallworldfs.moneytransferapp.presentation.account.profile.edit.EditProfileNavigator.Companion.REQUEST_CODE_STATE
import com.smallworldfs.moneytransferapp.presentation.account.profile.edit.EditProfileNavigator.Companion.REQUEST_CODE_STREET_TYPE
import com.smallworldfs.moneytransferapp.presentation.form.adapter.FormAdapter
import com.smallworldfs.moneytransferapp.presentation.form.selector.FormSelectorItem
import com.smallworldfs.moneytransferapp.utils.INT_ONE
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.RESULT_ITEM
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.forms.SubType
import com.smallworldfs.moneytransferapp.utils.forms.Type
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class EditProfileActivity : GenericActivity() {

    /**
     * Attributes
     */

    private var _binding: ActivityEditProfileBinding? = null
    private val binding get() = _binding!!

    private val viewModel: EditProfileViewModel by viewModels()

    @Inject
    lateinit var formAdapter: FormAdapter

    @Inject
    lateinit var navigator: EditProfileNavigator

    companion object {
        const val SHOW_DATE_PICKER = "birthDateGroup"
        const val SHOW_FORM_SELECT_BIRTH_COUNTRY = "countryBirth"
        const val SHOW_FORM_SELECT_STATE = "state"
        const val SHOW_FORM_SELECT_CITY = "city"
        const val SHOW_FORM_SELECT_PHONE_PREFIX = SubType.GROUP_PHONE
        const val SHOW_FORM_SELECT_TEXT_PREFIX = SubType.GROUP_TEXT
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupForm()
        setupObservers()

        viewModel.requestEditForm()
    }

    private fun setupObservers() {
        viewModel.editProfileFormLiveData.observe(
            this,
            EventObserver { form ->
                form.fields?.let {
                    if (it.size > INT_ZERO) {
                        formAdapter.setFormList(it)
                    }
                }

                with(binding) {
                    editProfileProgressBar.visibility = View.GONE
                    editProfileRecyclerViewForm.visibility = View.VISIBLE
                    editProfileButtonSubmit.visibility = View.VISIBLE
                }
            }
        )

        viewModel.saveProfileFormLiveData.observe(
            this,
            EventObserver {
                registerEvent("formOk", "", "profile")
                navigator.navigateToProfileDetails()
            }
        )

        viewModel.errorSaveProfileFormLiveData.observe(
            this,
            EventObserver { error ->
                registerEvent("formKo", "validation_error_$error", "profile")
            }
        )
    }

    /**
     * On activity result
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_BIRTH_COUNTRY -> {
                    val result = data?.getSerializableExtra(RESULT_ITEM) as FormSelectorItem

                    val list = formAdapter.getFormList()
                    val field = list.firstOrNull { it.name == SHOW_FORM_SELECT_BIRTH_COUNTRY }
                    field?.let {
                        val position = list.indexOf(field)
                        it.value = result.value
                        it.keyValue = result.key
                        formAdapter.updateFormList(field, position)
                    }
                }
                REQUEST_CODE_STATE -> {
                    val result = data?.getSerializableExtra(RESULT_ITEM) as FormSelectorItem

                    val list = formAdapter.getFormList()
                    val field = list.firstOrNull { it.name == SHOW_FORM_SELECT_STATE }
                    val fieldCity = list.firstOrNull { it.name == SHOW_FORM_SELECT_CITY }
                    field?.let {
                        val positionField = list.indexOf(field)
                        it.value = result.value
                        it.keyValue = result.key
                        formAdapter.updateFormList(field, positionField)

                        // Update city field also
                        fieldCity?.let { city ->
                            val positionFieldCity = list.indexOf(city)
                            city.value = STRING_EMPTY
                            city.keyValue = STRING_EMPTY
                            formAdapter.updateFormList(city, positionFieldCity)
                        }
                    }
                }
                REQUEST_CODE_CITY -> {
                    val result = data?.getSerializableExtra(RESULT_ITEM) as FormSelectorItem

                    val list = formAdapter.getFormList()
                    val field = list.firstOrNull { it.name == SHOW_FORM_SELECT_CITY }
                    field?.let {
                        val position = list.indexOf(field)
                        it.value = result.value
                        it.keyValue = result.key
                        formAdapter.updateFormList(field, position)
                    }
                }
                REQUEST_CODE_PREFIX -> {
                    val result = data?.getSerializableExtra(RESULT_ITEM) as FormSelectorItem

                    val list = formAdapter.getFormList()
                    val field = list.firstOrNull { it.type == Type.GROUP && it.subtype == SubType.GROUP_PHONE }
                    field?.let {
                        val position = list.indexOf(field)
                        if (it.childs.size > INT_ONE) {
                            it.childs[INT_ZERO].value = result.key
                        } else {
                            it.value = result.key
                        }
                        formAdapter.updateFormList(field, position)
                    }
                }
                REQUEST_CODE_STREET_TYPE -> {
                    val result = data?.getSerializableExtra(RESULT_ITEM) as FormSelectorItem

                    val list = formAdapter.getFormList()
                    val field = list.firstOrNull { it.type == Type.GROUP && it.subtype == SubType.GROUP_TEXT }
                    field?.let {
                        val position = list.indexOf(field)
                        if (it.childs.size > INT_ONE) {
                            it.childs[INT_ZERO].value = result.value
                            it.childs[INT_ZERO].keyValue = result.key
                            it.childs[INT_ONE].value = STRING_EMPTY
                        } else {
                            it.value = result.value
                            it.keyValue = result.key
                        }
                        formAdapter.updateFormList(field, position)
                    }
                }
            }
        }
    }

    /**
     * Setup UI
     */
    private fun setupToolbar() {
        setSupportActionBar(binding.editProfileToolbar)
        supportActionBar?.title = getString(R.string.edit_profile_title)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupForm() {
        formAdapter.initViewModel(this)

        with(binding.editProfileRecyclerViewForm) {
            layoutManager = LinearLayoutManager(this@EditProfileActivity)
            adapter = formAdapter
            isNestedScrollingEnabled = false
        }

        binding.editProfileButtonSubmit.setOnClickListener {
            registerEvent("click_save")
            viewModel.onActionSaveData(formAdapter.getFormData())
        }

        formAdapter.getEvents().observe(this) { action ->
            when (action) {
                SHOW_DATE_PICKER -> {
                    showDateDialog { _: DatePicker?, year: Int, month: Int, dayOfMonth: Int ->
                        val list = formAdapter.getFormList()
                        val field = list.firstOrNull { it.name == SHOW_DATE_PICKER }
                        field?.let {
                            val position = list.indexOf(it)

                            it.value = "$dayOfMonth/${month + INT_ONE}/$year"
                            it.childs?.firstOrNull { child -> child.name == "dayDate" }?.value = dayOfMonth.toString()
                            it.childs?.firstOrNull { child -> child.name == "monthDate" }?.value = (month + INT_ONE).toString()
                            it.childs?.firstOrNull { child -> child.name == "yearDate" }?.value = year.toString()

                            formAdapter.updateFormList(it, position)
                        }
                    }
                }
                SHOW_FORM_SELECT_BIRTH_COUNTRY -> {
                    val list = formAdapter.getFormList()
                    val field = list.firstOrNull { it.name == SHOW_FORM_SELECT_BIRTH_COUNTRY }
                    field?.let {
                        viewModel.onActionNavigateToFormSelectItemFromBirthCountry(it, navigator)
                    }
                }
                SHOW_FORM_SELECT_STATE -> {
                    val list = formAdapter.getFormList()
                    val field = list.firstOrNull { it.name == SHOW_FORM_SELECT_STATE }
                    field?.let {
                        viewModel.onActionNavigateToFormSelectItemFromState(it, navigator)
                    }
                }
                SHOW_FORM_SELECT_CITY -> {
                    val list = formAdapter.getFormList()
                    val field = list.firstOrNull { it.name == SHOW_FORM_SELECT_CITY }
                    field?.let {
                        viewModel.onActionNavigateToFormSelectItemFromCity(list, it, navigator)
                    }
                }
                SHOW_FORM_SELECT_PHONE_PREFIX -> {
                    val list = formAdapter.getFormList()
                    val field = list.firstOrNull { it.type == Type.GROUP && it.subtype == SubType.GROUP_PHONE }
                    field?.let {
                        viewModel.onActionNavigateToFormSelectItemFromPhonePrefix(it, navigator)
                    }
                }
                SHOW_FORM_SELECT_TEXT_PREFIX -> {
                    val list = formAdapter.getFormList()
                    val field = list.firstOrNull { it.type == Type.GROUP && it.subtype == SubType.GROUP_TEXT }
                    field?.let {
                        viewModel.onActionNavigateToFormSelectItemFromStreetType(it, navigator)
                    }
                }
            }
        }
    }

    private fun registerEvent(eventAction: String, eventLabel: String = "", formType: String = "") {
        trackEvent(
            UserActionEvent(
                ScreenCategory.DASHBOARD.value,
                eventAction,
                eventLabel,
                getHierarchy(""),
                formType
            ),
        )
    }
}
