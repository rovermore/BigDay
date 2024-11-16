package com.smallworldfs.moneytransferapp.presentation.form.adapter.holders

import com.smallworldfs.moneytransferapp.databinding.FormButtonHolderBinding
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.presentation.form.adapter.GenericButtonAction
import com.smallworldfs.moneytransferapp.presentation.form.adapter.HolderViewModel

class ButtonHolder(
    binding: FormButtonHolderBinding,
    var genericButtonAction: GenericButtonAction,
    holderViewModel: HolderViewModel
) :
    BaseHolder<FormButtonHolderBinding>(binding, holderViewModel) {

    override fun initialize(field: Field, lastField: Boolean) {
        super.initialize(field, lastField)
        binding.button.text = field.value
        binding.button.contentDescription = field.name
        binding.button.setOnClickListener {
            genericButtonAction.onClick(adapterPosition)
        }
    }
}
