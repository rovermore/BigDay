package com.smallworldfs.moneytransferapp.presentation.form.adapter.holders

import android.text.InputType
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.SmallWorldApplication
import com.smallworldfs.moneytransferapp.databinding.FormTextHolderBinding
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.presentation.form.adapter.HolderViewModel
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.forms.SubType.Companion.ALPHA_NUM
import com.smallworldfs.moneytransferapp.utils.forms.SubType.Companion.NUMERIC

class TextHolder(binding: FormTextHolderBinding, holderViewModel: HolderViewModel) : BaseTextHolder(binding, holderViewModel) {

    override fun initialize(field: Field, lastField: Boolean) {
        super.initialize(field, lastField)

        binding.inputEdit.setText(field.value)
        binding.inputEdit.contentDescription = field.name
        binding.input.hint = "${field.placeholder} ${if (field.isRequired) STRING_EMPTY else SmallWorldApplication.getStr(R.string.optional_tag)}"
        when (field.subtype) {
            ALPHA_NUM -> {
                binding.inputEdit.inputType = InputType.TYPE_CLASS_TEXT
            }
            NUMERIC -> {
                binding.inputEdit.inputType = InputType.TYPE_CLASS_NUMBER
            }
        }
    }
}
