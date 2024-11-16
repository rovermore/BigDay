package com.smallworldfs.moneytransferapp.utils.widget

import android.content.Context
import android.text.Editable
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.smallworldfs.moneytransferapp.databinding.AddressSearchBinding
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.presentation.account.profile.edit.EditProfileActivity
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.common.countries.AddressUIModel
import com.smallworldfs.moneytransferapp.presentation.form.adapter.FormAdapter
import com.smallworldfs.moneytransferapp.presentation.form.selector.FormSelectorItem
import com.smallworldfs.moneytransferapp.presentation.softregister.profile.AddressSearchAdapter
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.TextChangeCallback
import com.smallworldfs.moneytransferapp.utils.gone
import com.smallworldfs.moneytransferapp.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddressSearchView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

    var binding: AddressSearchBinding? = null

    companion object {
        const val REQUEST_CODE_STATE = 2022
        const val REQUEST_CODE_CITY = 2023
        const val REQUEST_CODE_STREET_TYPE = 2024
        const val SHOW_FORM_SELECT_STATE = "state"
        const val SHOW_FORM_SELECT_CITY = "city"
        const val SHOW_FORM_SELECT_STREET_TYPE = "streetType"
    }

    @Inject
    lateinit var formAdapter: FormAdapter

    private val addressAdapter = AddressSearchAdapter(context)

    init {
        binding = AddressSearchBinding.inflate(
            LayoutInflater.from(context),
            this,
            true,
        )
    }

    fun setupView(
        activity: AppCompatActivity,
        addressEventListener: AddressEventListener
    ) {
        formAdapter.initViewModel(activity)

        binding?.enterAddressManually?.setOnClickListener {
            addressEventListener.onEnterAddressManuallyClicked()
            showForm()
        }

        binding?.addressEditText?.apply {
            addTextChangedListener(
                object : TextChangeCallback {
                    override fun afterTextChanged(editable: Editable) {
                        if (editable.isEmpty()) {
                            binding?.addressSuggestionRecycler?.gone()
                        } else {
                            binding?.addressSuggestionRecycler?.visible()
                            if (editable.length % 3 == 0) {
                                addressEventListener.onSearchAddress(editable.toString())
                            }
                        }
                    }
                },
            )
        }

        binding?.addressSuggestionRecycler?.apply {
            adapter = addressAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = true
        }

        binding?.formAddressRecycler?.apply {
            adapter = formAdapter
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            isNestedScrollingEnabled = false
        }

        addressAdapter.setOnAddressSelectedCallback {
            addressEventListener.onAddressClicked(it)
        }

        formAdapter.getEvents().observe(activity) { action ->
            val list = formAdapter.getFormList()
            when (action) {
                SHOW_FORM_SELECT_STATE -> {
                    list.firstOrNull { it.name == EditProfileActivity.SHOW_FORM_SELECT_STATE }
                        ?.let {
                            addressEventListener.onAddressFieldClicked(it, REQUEST_CODE_STATE)
                        }
                }
                SHOW_FORM_SELECT_CITY -> {
                    var state = STRING_EMPTY
                    list.firstOrNull { it.name == EditProfileActivity.SHOW_FORM_SELECT_STATE }
                        ?.let {
                            it.keyValue?.let { state = it }
                        }
                    list.firstOrNull { it.name == SHOW_FORM_SELECT_CITY }?.let { cityField ->
                        addressEventListener.onAddressFieldClicked(cityField, REQUEST_CODE_CITY, state)
                    }
                }
                SHOW_FORM_SELECT_STREET_TYPE -> {
                    list.firstOrNull { it.name == SHOW_FORM_SELECT_STREET_TYPE }?.let {
                        addressEventListener.onAddressFieldClicked(it, REQUEST_CODE_STREET_TYPE)
                    }
                }
            }
        }
    }

    fun setAddressSuggestionsData(
        addressSuggestions: List<AddressUIModel>,
        addressSuggestionsShown: () -> Unit
    ) {
        addressAdapter.setData(addressSuggestions)
        addressSuggestionsShown.invoke()
    }

    fun setFormAddressData(fields: List<Field>) {
        formAdapter.setFormList(fields)
        fields.firstOrNull { it.name == "whitebox_address_label" }
            ?.let { it.value?.let { value -> setSummaryAddress(value) } }
    }

    fun setFormErrors(errors: List<ErrorType.FieldError>) {
        formAdapter.setErrors(errors)
    }

    private fun setSummaryAddress(selectedAddress: String) {
        binding?.apply {
            showSummary()
            addressSummaryTextView.text = selectedAddress
            editAddressButton.setOnClickListener {
                showMainView()
                binding?.addressEditText?.text?.clear()
            }
        }
    }

    private fun showSummary() {
        binding?.apply {
            enterAddressManually.gone()
            addressInput.gone()
            formAddressRecycler.gone()
            addressSuggestionRecycler.gone()
            addressSummaryContainer.visible()
        }
    }

    fun showForm() {
        binding?.apply {
            enterAddressManually.gone()
            addressInput.gone()
            formAddressRecycler.visible()
        }
    }

    private fun showMainView() {
        binding?.apply {
            enterAddressManually.visible()
            addressInput.visible()
            formAddressRecycler.gone()
            addressSummaryContainer.gone()
            addressSuggestionRecycler.gone()
        }
    }

    fun setItemSelectedForm(itemSelected: FormSelectorItem) {
        val list = formAdapter.getFormList()
        val field = list.firstOrNull { it.name == itemSelected.requestCode }
        field?.let {
            val position = list.indexOf(field)
            it.value = itemSelected.value
            it.keyValue = itemSelected.key
            formAdapter.updateFormList(field, position)
        }
    }

    interface AddressEventListener {
        fun onSearchAddress(address: String)
        fun onAddressClicked(address: AddressUIModel)
        fun onEnterAddressManuallyClicked()
        fun onAddressFieldClicked(field: Field, requestCode: Int, auxValue: String = STRING_EMPTY)
    }
}
