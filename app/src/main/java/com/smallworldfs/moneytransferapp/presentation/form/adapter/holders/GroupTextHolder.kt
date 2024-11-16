package com.smallworldfs.moneytransferapp.presentation.form.adapter.holders

import android.text.InputType
import android.view.View
import androidx.core.widget.addTextChangedListener
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.SmallWorldApplication
import com.smallworldfs.moneytransferapp.databinding.FormGroupTextBinding
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.presentation.form.adapter.GenericButtonAction
import com.smallworldfs.moneytransferapp.presentation.form.adapter.HolderViewModel
import com.smallworldfs.moneytransferapp.utils.INT_ONE
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.forms.SubType
import java.util.TreeMap

class GroupTextHolder(binding: FormGroupTextBinding, private var genericButtonAction: GenericButtonAction, holderViewModel: HolderViewModel) : BaseHolder<FormGroupTextBinding>(binding, holderViewModel) {

    override fun initialize(field: Field, lastField: Boolean) {
        super.initialize(field, lastField)

        // If the group at least two fields
        if (field.childs != null && field.childs.size > INT_ONE) {

            // If there are options to choose
            if (field.childs[INT_ZERO].data.size > INT_ZERO) {

                // Set listener
                binding.formGroupTextConstraintLayoutCodeSelector.setOnClickListener {
                    genericButtonAction.onClick(adapterPosition, keyValue = SubType.GROUP_TEXT)
                }

                // Set street type
                if (!field.childs[INT_ZERO].value.isNullOrEmpty()) {
                    binding.formGroupTextTextViewSelectableIndicator.text = findValueFromKey(field.childs[INT_ZERO].data, field.childs[INT_ZERO].value)
                } else {
                    binding.formGroupTextTextViewSelectableIndicator.text = field.childs[INT_ZERO].data[INT_ZERO]?.firstEntry()?.value ?: STRING_EMPTY
                }

                // If there isn't value for street type: hide it
                if (binding.formGroupTextTextViewSelectableIndicator.text.isNullOrEmpty()) {
                    binding.formGroupTextConstraintLayoutCodeSelector.visibility = View.GONE
                }
            } else {
                binding.formGroupTextConstraintLayoutCodeSelector.visibility = View.GONE
            }

            // Set edit text
            binding.formGroupTextTextInputLayoutAddress.hint = "${field.placeholder} ${if (field.isRequired) STRING_EMPTY else SmallWorldApplication.getStr(R.string.optional_tag)}"
            binding.formGroupTextTextInputLayoutEditText.setText(field.childs[INT_ONE].value)
            binding.formGroupTextTextInputLayoutEditText.contentDescription = field.name
            binding.formGroupTextTextInputLayoutEditText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
            binding.formGroupTextTextInputLayoutEditText.addTextChangedListener {
                field.childs?.let { list ->
                    list[INT_ONE].value = it.toString()
                }
            }
        }
    }

    /**
     * Check if the key give as a param exist in the list, then return the value, if not: return the key
     */
    private fun findValueFromKey(data: ArrayList<TreeMap<String, String>>, key: String = STRING_EMPTY): String {
        val entry = data.firstOrNull { it.firstKey() == key }
        return if (entry != null) entry[key] ?: key else key
    }
}
