package com.smallworldfs.moneytransferapp.presentation.form.adapter.holders

import android.text.Html
import android.text.method.LinkMovementMethod
import androidx.core.text.HtmlCompat
import com.smallworldfs.moneytransferapp.databinding.FormWhiteBoxHolderBinding
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.presentation.form.adapter.HolderViewModel
import com.smallworldfs.moneytransferapp.utils.forms.SubType

class WhiteBoxHolder(binding: FormWhiteBoxHolderBinding, holderViewModel: HolderViewModel) : BaseHolder<FormWhiteBoxHolderBinding>(binding, holderViewModel) {

    override fun initialize(field: Field, lastField: Boolean) {
        super.initialize(field, lastField)
        if (field.value != null && field.value.isNotEmpty()) {
            if (field.subtype == SubType.RICH_TEXT) {
                binding.text.isClickable = true
                binding.text.contentDescription = field.name
                binding.text.movementMethod = LinkMovementMethod.getInstance()
                binding.text.text = HtmlCompat.fromHtml(field.value, Html.FROM_HTML_MODE_COMPACT)
            } else {
                binding.text.text = field.value
            }
        }
    }
}
