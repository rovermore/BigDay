package com.smallworldfs.moneytransferapp.presentation.softregister.profile

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventProperty
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.DialogExt
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.EventObserver
import com.smallworldfs.moneytransferapp.databinding.FragmentRegisterProfileBinding
import com.smallworldfs.moneytransferapp.domain.migrated.form.repository.Source
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericFragment
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.common.countries.AddressUIModel
import com.smallworldfs.moneytransferapp.presentation.form.selector.FormSelectorActivity
import com.smallworldfs.moneytransferapp.presentation.form.selector.FormSelectorItem
import com.smallworldfs.moneytransferapp.presentation.form.selector.FormSelectorState
import com.smallworldfs.moneytransferapp.presentation.softregister.SignupViewModel
import com.smallworldfs.moneytransferapp.utils.INPUT_STATE
import com.smallworldfs.moneytransferapp.utils.KeyboardHandler
import com.smallworldfs.moneytransferapp.utils.RESULT_ITEM
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.visible
import com.smallworldfs.moneytransferapp.utils.widget.AddressSearchView
import dagger.hilt.android.AndroidEntryPoint
import java.text.DateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class RegisterProfileFragment : GenericFragment() {

    private val viewModelActivity: SignupViewModel by activityViewModels()

    @Inject
    lateinit var keyboardHandler: KeyboardHandler

    private var _binding: FragmentRegisterProfileBinding? = null
    private val binding get() = _binding!!

    private val startStateSelector =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val result = it.data?.getSerializableExtra(RESULT_ITEM) as FormSelectorItem
                binding.addressSearchView.setItemSelectedForm(
                    result.copy(
                        requestCode = AddressSearchView.SHOW_FORM_SELECT_STATE,
                    ),
                )
            }
        }
    private val startCitySelector =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val result = it.data?.getSerializableExtra(RESULT_ITEM) as FormSelectorItem
                binding.addressSearchView.setItemSelectedForm(
                    result.copy(
                        requestCode = AddressSearchView.SHOW_FORM_SELECT_CITY,
                    ),
                )
            }
        }
    private val startStreetTypeSelector =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val result = it.data?.getSerializableExtra(RESULT_ITEM) as FormSelectorItem
                binding.addressSearchView.setItemSelectedForm(
                    result.copy(
                        requestCode = AddressSearchView.SHOW_FORM_SELECT_STREET_TYPE,
                    ),
                )
            }
        }

    private var addressType = "auto"

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRegisterProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupObservers()
    }

    override fun onResume() {
        super.onResume()
        trackScreen()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupObservers() {
        viewModelActivity.searchAddressList.observe(
            viewLifecycleOwner,
            EventObserver {
                binding.addressSearchView.setAddressSuggestionsData(it) { addressSuggestionsShown() }
            },
        )

        viewModelActivity.addressForm.observe(
            viewLifecycleOwner,
            EventObserver {
                binding.addressSearchView.setFormAddressData(it)
                if (areFieldsPending(it)) {
                    binding.addressSearchView.showForm()
                    addressType = "semi"
                }
                setButtonClickable(true)
            },
        )

        viewModelActivity.registerUserError.observe(
            viewLifecycleOwner,
            EventObserver {
                showError(it)
                registerEvent(eventAction = "formKo", origin = viewModelActivity.selectedCountry.value?.peekContent()?.iso3 ?: STRING_EMPTY)
            },
        )
    }

    private fun areFieldsPending(fields: List<Field>): Boolean {
        return fields.firstOrNull { !it.isHidden }?.let { true } ?: false
    }

    private fun setupView() {
        setButtonClickable(false)

        binding.lastNameInput.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus)
                keyboardHandler.hideKeyboard(activity as AppCompatActivity)
        }

        binding.firstNameInput.onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
            if (!hasFocus)
                keyboardHandler.hideKeyboard(activity as AppCompatActivity)
        }

        with(binding.birthdateInputLayout) {
            onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus)
                    keyboardHandler.hideKeyboard(activity as AppCompatActivity)
            }
            setOnClickListener {
                showDateDialog()
            }
        }

        with(binding.birthdateInput) {
            showSoftInputOnFocus = false
            onFocusChangeListener = View.OnFocusChangeListener { _, hasFocus ->
                if (hasFocus)
                    keyboardHandler.hideKeyboard(activity as AppCompatActivity)
            }
            setOnClickListener {
                showDateDialog()
            }
        }

        binding.addressSearchView.setupView(
            requireActivity() as AppCompatActivity,
            object : AddressSearchView.AddressEventListener {
                override fun onSearchAddress(address: String) {
                    viewModelActivity.searchAddress(address)
                }

                override fun onAddressClicked(address: AddressUIModel) {
                    if (address.type == "ADDRESS") {
                        viewModelActivity.getAddressById(address.id)
                        addressType = "auto"
                    } else
                        viewModelActivity.searchAddressByParentId(address.id)
                }

                override fun onEnterAddressManuallyClicked() {
                    viewModelActivity.getAddressForm()
                    addressType = "manual"
                }

                override fun onAddressFieldClicked(field: Field, requestCode: Int, auxValue: String) {
                    val listToShow: MutableList<FormSelectorItem> = mutableListOf()
                    field.data?.forEach {
                        listToShow.add(FormSelectorItem(it.firstKey(), it.getValue(it.firstKey())))
                    }.let { listToShow.sortBy { it.value } }
                    val apiRef = field.refApi?.url?.let { url ->
                        field.refApi?.params?.let { params ->
                            Source.API(url, params, auxValue)
                        }
                    }
                    val state = FormSelectorState(field.title, listToShow, true, apiRef)
                    val intent = Intent(activity, FormSelectorActivity::class.java)
                    intent.putExtra(INPUT_STATE, state)
                    when (requestCode) {
                        AddressSearchView.REQUEST_CODE_STATE -> {
                            startStateSelector.launch(intent)
                        }
                        AddressSearchView.REQUEST_CODE_CITY -> {
                            startCitySelector.launch(intent)
                        }
                        AddressSearchView.REQUEST_CODE_STREET_TYPE -> {
                            startStreetTypeSelector.launch(intent)
                        }
                    }
                }
            },
        )

        binding.signUpButton.setOnClickListener {
            if (it.isEnabled) {
                registerUser()
                registerEvent(eventAction = "click_start", origin = viewModelActivity.selectedCountry.value?.peekContent()?.iso3 ?: STRING_EMPTY)
            }
        }
    }

    private fun registerUser() {
        val formList = binding.addressSearchView.formAdapter.getFormList()
        formList.firstOrNull { it.name == "city" }?.value?.let { city ->
            formList.firstOrNull { it.name == "zip" }?.value?.let { zip ->
                viewModelActivity.registerUser(
                    binding.firstNameInput.text.toString(),
                    binding.lastNameInput.text.toString(),
                    binding.birthdateInput.text.toString(),
                    city,
                    formList.firstOrNull { it.name == "streetType" }.let { streetType ->
                        streetType?.data?.firstOrNull { it.containsValue(streetType.value) }.let { it?.firstKey() }
                            ?: STRING_EMPTY
                    },
                    formList.firstOrNull { it.name == "streetName" }?.value ?: STRING_EMPTY,
                    formList.firstOrNull { it.name == "streetNumber" }?.value ?: STRING_EMPTY,
                    formList.firstOrNull { it.name == "buildingName" }?.value ?: STRING_EMPTY,
                    zip,
                    formList.firstOrNull { it.name == "state" }?.let { getState(it) } ?: STRING_EMPTY,
                    formList.firstOrNull { it.name == "address" }?.value ?: STRING_EMPTY,
                    formList.firstOrNull { it.name == "signature" }?.value ?: STRING_EMPTY,
                    addressType
                )
            }
        }
    }

    private fun getState(stateField: Field): String {
        val value = stateField.value
        val data = stateField.data
        if (data == null || value == null) return STRING_EMPTY

        return data.firstOrNull { it.containsKey(value) || it.containsValue(value) }?.firstKey() ?: STRING_EMPTY
    }

    private fun showDateDialog() {
        val maxDate = Calendar.getInstance().apply {
            add(Calendar.YEAR, -18)
        }
        DialogExt().showDateDialog(
            requireContext(),
            maxDate = maxDate,
        ) { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance(Locale.getDefault())
            calendar.set(year, month, dayOfMonth)
            val date = calendar.time
            binding.birthdateInput.setText(DateFormat.getDateInstance().format(date))
        }
    }

    private fun addressSuggestionsShown() {
        binding.profileScrollView.post {
            binding.profileScrollView.smoothScrollTo(0, binding.addressSearchView.bottom, 500)
        }
    }

    private fun setButtonClickable(isClickable: Boolean) {
        binding.signUpButton.isEnabled = isClickable
    }

    private fun showError(errorType: ErrorType) {
        when (errorType) {
            is ErrorType.EntityValidationError -> {
                binding.addressSearchView.setFormErrors(errorType.fields)
                setFieldsErrors(errorType.fields)
            }
            is ErrorType.DataNotFound -> {
                binding.addressSearchView.setAddressSuggestionsData(emptyList()) { addressSuggestionsShown() }
            }
            else ->
                viewModelActivity.showErrorGenericErrorView(errorType)
        }
    }

    private fun setFieldsErrors(fields: List<ErrorType.FieldError>) {
        fields.forEach {
            when (it.field) {
                "fullFirstName" -> binding.firstNameErrorText.apply {
                    text = it.error.first()
                    visible()
                }
                "fullLastName" -> binding.lastNameErrorText.apply {
                    text = it.error.first()
                    visible()
                }
                "dateOfBirth" -> binding.dateOfBirthErrorText.apply {
                    text = it.error.first()
                    visible()
                }
            }
        }
    }

    private fun trackScreen() {
        super.trackScreen(ScreenName.PROFILE_SCREEN.value)
        super.trackScreenBraze(
            ScreenName.PROFILE_SCREEN.value,
            mapOf(
                BrazeEventProperty.REGISTER_COUNTRY.value to (viewModelActivity.selectedCountry.value?.peekContent()?.iso3 ?: STRING_EMPTY)
            )
        )
    }

    private fun registerEvent(eventAction: String, eventLabel: String = STRING_EMPTY, formType: String = STRING_EMPTY, origin: String = STRING_EMPTY) {
        trackEvent(
            UserActionEvent(
                ScreenCategory.ACCESS.value,
                eventAction,
                eventLabel,
                getHierarchy(""),
                formType,
                STRING_EMPTY,
                STRING_EMPTY,
                origin,
                STRING_EMPTY,
            ),
        )
    }
}
