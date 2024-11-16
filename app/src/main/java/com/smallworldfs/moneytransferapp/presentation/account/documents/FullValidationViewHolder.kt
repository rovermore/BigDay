package com.smallworldfs.moneytransferapp.presentation.account.documents

import androidx.recyclerview.widget.RecyclerView
import com.smallworldfs.moneytransferapp.databinding.UploadDocumentDialogBinding
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.OnDocumentActionClickListener
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.ComplianceDocUIModel

class FullValidationViewHolder(private val binding: UploadDocumentDialogBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: ComplianceDocUIModel.FullValidationUIModel, listener: OnDocumentActionClickListener) {
        with(binding) {
            uploadIdAndSelfieButton.setOnClickListener {
                listener.onValidateIdentity(item)
            }
            actionButton.setOnClickListener {
                listener.onUploadDocument(item)
            }
        }
    }
}
