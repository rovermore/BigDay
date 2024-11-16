package com.smallworldfs.moneytransferapp.presentation.form.adapter.holders

import android.text.InputType
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.SmallWorldApplication
import com.smallworldfs.moneytransferapp.databinding.FormComboHolderBinding
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.presentation.form.adapter.GenericButtonAction
import com.smallworldfs.moneytransferapp.presentation.form.adapter.HolderViewModel
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY

class GroupDateHolder(binding: FormComboHolderBinding, var genericButtonAction: GenericButtonAction, holderViewModel: HolderViewModel) : BaseHolder<FormComboHolderBinding>(binding, holderViewModel) {

    override fun initialize(field: Field, lastField: Boolean) {
        super.initialize(field, lastField)

        val valueToShow = if (!field.value.isNullOrEmpty()) {
            field.value
        } else {
            val dayDate = field.childs?.firstOrNull { child -> child.name == "dayDate" || child.name == "dayExpirationDate" }?.value
            val monthDate = field.childs?.firstOrNull { child -> child.name == "monthDate" || child.name == "monthExpirationDate" }?.value
            val yearDate = field.childs?.firstOrNull { child -> child.name == "yearDate" || child.name == "yearExpirationDate" }?.value

            val dayToShow = try {
                dayDate?.substring(0, 2)
            } catch (e: Exception) {
                dayDate
            }
            if (dayToShow.isNullOrEmpty() || monthDate.isNullOrEmpty() || yearDate.isNullOrEmpty()) {
                STRING_EMPTY
            } else {
                "$dayToShow/$monthDate/$yearDate"
            }
        }

        binding.inputEdit.setText(valueToShow)
        binding.inputEdit.contentDescription = field.name

        binding.input.hint = "${field.placeholder} ${if (field.isRequired) STRING_EMPTY else SmallWorldApplication.getStr(R.string.optional_tag)}"

        binding.inputEdit.inputType = InputType.TYPE_NULL

        // Check if there is an error in the field, if not, check the children, if not, hide errors in edit text
        var errorToShow = STRING_EMPTY
        if (field.childs != null && field.childs.size > INT_ZERO) {
            field.childs.asReversed().forEach { child ->
                if (!child.errorMessage.isNullOrEmpty()) {
                    errorToShow = child.errorMessage
                }
            }
        }
        if (!field.errorMessage.isNullOrEmpty()) {
            errorToShow = field.errorMessage
        }
        if (errorToShow.isNotEmpty()) {
            binding.input.error = errorToShow
            binding.input.isErrorEnabled = true
        } else {
            binding.input.error = null
            binding.input.isErrorEnabled = false
        }

        binding.button.setOnClickListener {
            genericButtonAction.onClick(adapterPosition)
        }
    }
}
