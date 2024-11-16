package com.smallworldfs.moneytransferapp.presentation.account.documents

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.databinding.ComplianceListItemBinding
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.OnDocumentActionClickListener
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.ComplianceDocUIModel
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.DocumentButtonUIModel
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.DocumentUIModel
import com.smallworldfs.moneytransferapp.utils.toHtml
import com.smallworldfs.moneytransferapp.utils.visibleIf

class ComplianceDocumentViewHolder(private val binding: ComplianceListItemBinding) : RecyclerView.ViewHolder(binding.root) {

    val MISSING = "MISSING"

    fun bind(item: ComplianceDocUIModel, listener: OnDocumentActionClickListener) {
        with(binding) {
            apply {
                val context = binding.root.context
                underReviewDescription.visibleIf(item.status == DocumentUIModel.DocumentStatus.UNDER_REVIEW)

                descriptionTextView.text =
                    if (item.mtn.isNotEmpty()) context.getString(R.string.documentsListAdapterDescriptionText, item.mtn, item.description.toHtml())
                    else item.description.toHtml()
                descriptionTextView.visibleIf(item.status.toString() == MISSING)
                separatorMainContent.visibleIf(item.status.toString() == MISSING)

                with(titleTextView) {
                    visibleIf(item.title.isNotEmpty())
                    isAllCaps = item.mtn.isEmpty()
                    text = item.title
                }
                separatorTitles.visibleIf(item.mtn.isNotEmpty())
                with(mtn) {
                    visibleIf(item.mtn.isNotEmpty() && item.status.toString() != MISSING)
                    text = item.mtn
                }

                separatorTitles.visibleIf(item.mtn.isNotEmpty() && item.status.toString() != MISSING)

                val leftButton = item.buttons.getOrNull(0) ?: DocumentButtonUIModel()
                val rightButton = item.buttons.getOrNull(1) ?: DocumentButtonUIModel()

                with(buttonLeft) {
                    visibleIf(leftButton.text.isNotEmpty())
                    text = leftButton.text
                    setTextColor(ContextCompat.getColor(context, leftButton.color))
                }

                separatorBottomButtons.visibleIf(rightButton.text.isNotEmpty())

                with(buttonRight) {
                    visibleIf(rightButton.text.isNotEmpty())
                    text = rightButton.text
                    setTextColor(ContextCompat.getColor(context, rightButton.color))
                }
            }

            if (item.status.toString() == MISSING) {
                if (item.block && !item.upload) {
                    buttonLeft.setOnClickListener {
                        listener.onNavigateToUrl(item.buttons[0].url)
                    }

                    buttonRight.setOnClickListener {
                        listener.onNavigateToUrl(item.buttons[1].url)
                    }
                } else if (item.doc.isEmpty()) {
                    buttonLeft.setOnClickListener {
                        listener.onUploadDocument(item)
                    }

                    buttonRight.setOnClickListener(null)
                } else {
                    buttonLeft.setOnClickListener {
                        listener.onDownloadDocument(item)
                    }

                    buttonRight.setOnClickListener {
                        listener.onUploadDocument(item)
                    }
                }
            }
        }
    }
}
