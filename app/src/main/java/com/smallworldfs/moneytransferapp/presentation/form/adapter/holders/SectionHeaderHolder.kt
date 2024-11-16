package com.smallworldfs.moneytransferapp.presentation.form.adapter.holders

import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.content.ContextCompat
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.databinding.FormSectionHeaderBinding
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.presentation.form.adapter.HolderViewModel
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.forms.SubType

class SectionHeaderHolder(binding: FormSectionHeaderBinding, holderViewModel: HolderViewModel) : BaseHolder<FormSectionHeaderBinding>(binding, holderViewModel) {

    override fun initialize(field: Field, lastField: Boolean) {
        super.initialize(field, lastField)

        binding.formSectionHeaderImage.visibility = View.GONE

        val drawable: Drawable? = when (field.subtype) {
            SubType.SECTION_HEADER_PROFILE -> ContextCompat.getDrawable(binding.formSectionHeaderImage.context, R.drawable.hardregister_icn_profile)
            SubType.SECTION_HEADER_ADDRESS -> ContextCompat.getDrawable(binding.formSectionHeaderImage.context, R.drawable.hardregister_icn_address)
            else -> null
        }

        drawable?.let {
            binding.formSectionHeaderImage.visibility = View.VISIBLE
            binding.formSectionHeaderImageView.setImageDrawable(it)
        }

        binding.formSectionHeaderText.text = field.value ?: STRING_EMPTY
        binding.root.contentDescription = field.name
    }
}
