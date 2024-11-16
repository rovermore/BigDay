package com.smallworldfs.moneytransferapp.presentation.form.adapter.holders

import android.text.InputType
import com.smallworldfs.moneytransferapp.databinding.FormTextHolderBinding
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.presentation.form.adapter.HolderViewModel

class EmailHolder(binding: FormTextHolderBinding, holderViewModel: HolderViewModel) :
    BaseTextHolder(binding, holderViewModel) {

    override fun initialize(field: Field, lastField: Boolean) {
        super.initialize(field, lastField)
        binding.input.hint = field.placeholder
        binding.inputEdit.setText(field.value)
        binding.inputEdit.contentDescription = field.name
        binding.inputEdit.inputType = InputType.TYPE_CLASS_TEXT or
            InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
    }
}
