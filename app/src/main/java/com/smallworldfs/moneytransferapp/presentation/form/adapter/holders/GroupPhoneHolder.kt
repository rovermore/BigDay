package com.smallworldfs.moneytransferapp.presentation.form.adapter.holders

import android.text.InputType
import android.text.TextUtils
import androidx.core.widget.addTextChangedListener
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.SmallWorldApplication
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.loadCircularImage
import com.smallworldfs.moneytransferapp.databinding.FormGroupPhoneBinding
import com.smallworldfs.moneytransferapp.modules.calculator.domain.interactor.implementation.CalculatorInteractorImpl
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.presentation.form.adapter.GenericButtonAction
import com.smallworldfs.moneytransferapp.presentation.form.adapter.HolderViewModel
import com.smallworldfs.moneytransferapp.presentation.form.adapter.extractCountryPhonePrefix
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.INT_ONE
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.forms.SubType
import java.util.TreeMap

class GroupPhoneHolder(binding: FormGroupPhoneBinding, private var genericButtonAction: GenericButtonAction, holderViewModel: HolderViewModel) : BaseHolder<FormGroupPhoneBinding>(binding, holderViewModel) {

    override fun initialize(field: Field, lastField: Boolean) {
        super.initialize(field, lastField)

        // If the group at least two fields
        if (field.childs != null && field.childs.size > INT_ONE) {

            // Get value of the country. e.g. "ESP"
            val selectedCountryValue = if (field.value != null) {
                getCountryById(field.data, field.value)?.value ?: field.childs[INT_ZERO].value
            } else {
                field.childs[INT_ZERO].value
            }
            if (TextUtils.isEmpty(selectedCountryValue)) {
                field.childs[INT_ZERO].value = CalculatorInteractorImpl.getInstance().payoutCountryKey
            }

            // Set listener
            binding.formGroupPhoneConstraintLayoutCodeSelector.setOnClickListener {
                genericButtonAction.onClick(adapterPosition, keyValue = SubType.GROUP_PHONE)
            }

            // Set image
            binding.formGroupPhoneImageViewCountryChip.loadCircularImage(
                itemView.context,
                R.drawable.placeholder_country_adapter,
                Constants.COUNTRY.FLAG_IMAGE_ASSETS + selectedCountryValue + Constants.COUNTRY.FLAG_IMAGE_EXTENSION
            )

            // Set prefix
            binding.formGroupPhoneTextViewPrefix.text = extractCountryPhonePrefix(field.childs[INT_ZERO].data, selectedCountryValue)

            // Set edit text
            binding.formGroupPhoneTextInputLayoutPhone.hint = "${field.placeholder} ${if (field.isRequired) STRING_EMPTY else SmallWorldApplication.getStr(R.string.optional_tag)}"
            binding.formGroupPhoneTextInputLayoutEditText.setText(field.childs[INT_ONE].value)
            binding.formGroupPhoneTextInputLayoutEditText.contentDescription = field.name
            binding.formGroupPhoneTextInputLayoutEditText.inputType = InputType.TYPE_CLASS_NUMBER
            binding.formGroupPhoneTextInputLayoutEditText.addTextChangedListener {
                this.field.childs?.let { list ->
                    list[INT_ONE].value = it.toString()
                }
            }
        }
    }

    private fun getCountryById(countries: ArrayList<TreeMap<String?, String>>, id: String): Map.Entry<String?, String>? {
        for (country in countries) {
            val countryData = country.firstEntry()
            if (countryData?.key != null && countryData.key == id) {
                return countryData
            }
        }
        return null
    }
}
