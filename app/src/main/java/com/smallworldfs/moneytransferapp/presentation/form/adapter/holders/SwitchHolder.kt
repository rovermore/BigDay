package com.smallworldfs.moneytransferapp.presentation.form.adapter.holders

import com.smallworldfs.moneytransferapp.databinding.FormSwitchHolderBinding
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.presentation.form.adapter.HolderViewModel

class SwitchHolder(binding: FormSwitchHolderBinding, holderViewModel: HolderViewModel) :
    BaseHolder<FormSwitchHolderBinding>(binding, holderViewModel) {

    override fun initialize(field: Field, lastField: Boolean) {
        super.initialize(field, lastField)
        binding.switchText.text = field.title
        binding.switchButton.contentDescription = field.name
        binding.switchButton.isChecked = field.value?.toBoolean() ?: false
        binding.switchButton.setOnCheckedChangeListener { _, isChecked ->
            field.value = isChecked.toString()
        }
    }
}
