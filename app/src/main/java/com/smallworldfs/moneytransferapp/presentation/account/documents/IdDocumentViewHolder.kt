package com.smallworldfs.moneytransferapp.presentation.account.documents

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.parseFromTimestampToDate
import com.smallworldfs.moneytransferapp.databinding.DocumentsListItemBinding
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.DocumentButtonUIModel
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.DocumentUIModel
import com.smallworldfs.moneytransferapp.utils.visibleIf

class IdDocumentViewHolder(private val binding: DocumentsListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: DocumentUIModel) {
        with(binding) {
            val context = binding.root.context
            with(numberValue) {
                visibleIf(item.number.isNotEmpty())
                text = item.number
            }
            number.visibleIf(item.number.isNotEmpty())

            with(expirationDateValue) {
                visibleIf(item.expirationDate.isNotEmpty())
                text = item.expirationDate.parseFromTimestampToDate()
            }

            expirationDate.visibleIf(item.expirationDate.isNotEmpty())

            underReviewDescription.visibleIf(item.status == DocumentUIModel.DocumentStatus.UNDER_REVIEW)

            with(titleTextView) {
                visibleIf(item.name.isNotEmpty())
                text = item.name
            }

            val button = item.buttons.getOrNull(0) ?: DocumentButtonUIModel()

            with(buttonDocument) {
                visibleIf(button.text.isNotEmpty())
                text = button.text
                setTextColor(ContextCompat.getColor(context, button.color))
            }
        }
    }
}
