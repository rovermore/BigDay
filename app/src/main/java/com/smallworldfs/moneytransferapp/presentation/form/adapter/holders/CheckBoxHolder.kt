package com.smallworldfs.moneytransferapp.presentation.form.adapter.holders

import com.smallworldfs.moneytransferapp.databinding.FormCheckBoxHolderBinding
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.presentation.form.adapter.HolderViewModel

class CheckBoxHolder(binding: FormCheckBoxHolderBinding, holderViewModel: HolderViewModel) :
    BaseHolder<FormCheckBoxHolderBinding>(binding, holderViewModel) {

    override fun initialize(field: Field, lastField: Boolean) {
        super.initialize(field, lastField)
        binding.checkbox.text = field.title
        binding.checkbox.contentDescription = field.name
        binding.checkbox.isChecked = field.value?.toBoolean() ?: false
        binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
            field.value = isChecked.toString()
        }
    }
}
