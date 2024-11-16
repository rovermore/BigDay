package com.smallworldfs.moneytransferapp.presentation.form.adapter.holders

import android.annotation.SuppressLint
import androidx.core.content.ContextCompat
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.SmallWorldApplication
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.loadImage
import com.smallworldfs.moneytransferapp.databinding.FormFileDocumentBinding
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field
import com.smallworldfs.moneytransferapp.presentation.form.adapter.GenericButtonAction
import com.smallworldfs.moneytransferapp.presentation.form.adapter.HolderViewModel
import com.smallworldfs.moneytransferapp.utils.INT_ONE
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import kotlinx.android.synthetic.main.dialog_animation_layout.view.*
import java.util.Locale

class FileDocumentHolder(binding: FormFileDocumentBinding, var genericButtonAction: GenericButtonAction, holderViewModel: HolderViewModel) : BaseHolder<FormFileDocumentBinding>(binding, holderViewModel) {

    @SuppressLint("SetTextI18n")
    override fun initialize(field: Field, lastField: Boolean) {

        super.initialize(field, lastField)

        binding.root.contentDescription = field.name

        // Set title
        if (field.title.isNotEmpty()) {
            binding.formFileDocumentTitle.text = field.title
        } else {
            binding.formFileDocumentTitle.text = field.name.substring(INT_ZERO, INT_ONE).toUpperCase(
                Locale.getDefault(),
            ) + field.name.substring(INT_ONE, field.name.length)
        }

        // Set subtitle and image
        when {
            !field.value.isNullOrEmpty() && field.errorMessage.isNullOrEmpty() -> {
                binding.formFileDocumentSubtitle.text = SmallWorldApplication.getStr(R.string.image_loaded_successfully)
                binding.formFileDocumentSubtitle.setTextColor(ContextCompat.getColor(binding.formFileDocumentSubtitle.context, R.color.documentUploadedGreen))
                binding.formFileDocumentImageView.loadImage(R.drawable.icn_upload_ok)
            }
            !field.errorMessage.isNullOrEmpty() -> {
                binding.formFileDocumentSubtitle.text = field.placeholder
                binding.formFileDocumentSubtitle.setTextColor(ContextCompat.getColor(binding.formFileDocumentSubtitle.context, R.color.colorRedError))
                binding.formFileDocumentImageView.loadImage(R.drawable.hardregister_icn_attachdocfail)
            }
            else -> {
                binding.formFileDocumentSubtitle.text = field.placeholder
                binding.formFileDocumentSubtitle.setTextColor(ContextCompat.getColor(binding.formFileDocumentSubtitle.context, R.color.blue_background_welcome))
                binding.formFileDocumentImageView.loadImage(R.drawable.icn_upload_new)
            }
        }

        binding.formFileDocumentConstraintLayout.contentDescription = if (field.name == "FRONT") "front_upload_button" else "back_upload_button"

        // Set listener
        binding.formFileDocumentConstraintLayout.setOnClickListener {
            genericButtonAction.onClick(adapterPosition)
        }
    }
}
