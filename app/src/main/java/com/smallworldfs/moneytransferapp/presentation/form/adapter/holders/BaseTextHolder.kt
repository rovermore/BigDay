package com.smallworldfs.moneytransferapp.presentation.form.adapter.holders

import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import com.smallworldfs.moneytransferapp.databinding.FormTextHolderBinding
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.presentation.form.adapter.HolderViewModel
import com.smallworldfs.moneytransferapp.utils.forms.SubType

abstract class BaseTextHolder(binding: FormTextHolderBinding, holderViewModel: HolderViewModel) :
    BaseHolder<FormTextHolderBinding>(binding, holderViewModel) {

    override fun initialize(field: Field, lastField: Boolean) {
        super.initialize(field, lastField)
        binding.inputEdit.imeOptions = if (lastField) {
            EditorInfo.IME_ACTION_DONE
        } else {
            EditorInfo.IME_ACTION_NEXT
        }
        binding.inputEdit.setOnEditorActionListener { view, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE && view.hasFocus()) {
                view.clearFocus()
            }
            false
        }
        binding.inputEdit.setOnFocusChangeListener { _, hasFocus ->
            if (hasFocus && binding.inputEdit.text != null) {
                binding.inputEdit.text?.let {
                    binding.inputEdit.setSelection(it.length)
                }
                field.errorMessage = ""
                binding.input.error = null
                binding.input.isErrorEnabled = false
            }
        }
        if (field.errorMessage != null && field.errorMessage.isNotEmpty()) {
            if (field.subtype == SubType.STRONG_PASSWORD && field.errorMessage == " ") {
                field.keyValue = "invalid"
            }
            binding.input.error = field.errorMessage
            binding.input.isErrorEnabled = true
        } else {
            binding.input.error = null
            binding.input.isErrorEnabled = false
        }

        binding.inputEdit.addTextChangedListener {
            this.field.value = it.toString()
        }
    }
}
