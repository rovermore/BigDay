package com.smallworldfs.moneytransferapp.presentation.account.documents.list

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.databinding.AdapterDocumentsListHeaderBinding
import com.smallworldfs.moneytransferapp.databinding.ComplianceListItemBinding
import com.smallworldfs.moneytransferapp.databinding.DocumentsListItemBinding
import com.smallworldfs.moneytransferapp.databinding.UploadDocumentDialogBinding
import com.smallworldfs.moneytransferapp.presentation.account.documents.ComplianceDocumentViewHolder
import com.smallworldfs.moneytransferapp.presentation.account.documents.FullValidationViewHolder
import com.smallworldfs.moneytransferapp.presentation.account.documents.IdDocumentViewHolder
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.ComplianceDocUIModel
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.DocumentUIModel
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.gone
import com.smallworldfs.moneytransferapp.utils.visible

class MyDocumentsAdapter(
    private val context: Context,
    requiredDocuments: List<DocumentUIModel>,
    uploadedDocuments: List<DocumentUIModel>,
    val listener: OnDocumentActionClickListener
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var requiredDocumentItems = mutableListOf<DocumentUIModel>().apply { addAll(requiredDocuments) }
    private var uploadedDocumentItems = mutableListOf<DocumentUIModel>().apply { addAll(uploadedDocuments) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        ID -> {
            val binding = DocumentsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            IdDocumentViewHolder(binding)
        }

        FULLVALIDATION -> {
            val binding = UploadDocumentDialogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            FullValidationViewHolder(binding)
        }

        COMPLIANCE -> {
            val binding = ComplianceListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ComplianceDocumentViewHolder(binding)
        }

        else -> {
            val binding = AdapterDocumentsListHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            HeaderViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ID -> {
                (holder as IdDocumentViewHolder).bind(
                    getDocumentItem(position),
                )
            }

            FULLVALIDATION -> {
                (holder as FullValidationViewHolder).bind(
                    getDocumentItem(position) as ComplianceDocUIModel.FullValidationUIModel,
                    listener,
                )
            }

            COMPLIANCE -> {
                (holder as ComplianceDocumentViewHolder).bind(
                    getDocumentItem(position) as ComplianceDocUIModel,
                    listener,
                )
            }

            HEADER -> {
                (holder as HeaderViewHolder).bind(
                    position,
                    context,
                    requiredDocumentItems.size,
                )
            }
        }
    }

    override fun getItemViewType(position: Int) = when (position) {
        0, (requiredDocumentItems.size + 1) -> HEADER
        in 1..requiredDocumentItems.size -> checkItem(requiredDocumentItems[position - 1])
        else -> checkItem(uploadedDocumentItems[position - requiredDocumentItems.size - 2])
    }

    private fun checkItem(item: DocumentUIModel) = when (item) {
        is ComplianceDocUIModel.FullValidationUIModel -> FULLVALIDATION
        is ComplianceDocUIModel -> COMPLIANCE
        else -> ID
    }

    private fun getDocumentItem(position: Int): DocumentUIModel =
        when (position) {
            in 1..requiredDocumentItems.size -> requiredDocumentItems[position - 1]
            else -> uploadedDocumentItems[position - requiredDocumentItems.size - 2]
        }

    override fun getItemCount() = requiredDocumentItems.size + uploadedDocumentItems.size + 2

    fun updateRequiredDocuments(newListItems: List<DocumentUIModel>) {
        requiredDocumentItems.clear()
        requiredDocumentItems.addAll(newListItems)
        notifyDataSetChanged()
    }

    fun updateUploadedDocuments(newListItems: List<DocumentUIModel>) {
        uploadedDocumentItems.clear()
        uploadedDocumentItems.addAll(newListItems)
        notifyDataSetChanged()
    }

    companion object TypeOfElement {
        private const val HEADER = 0
        private const val ID = 1
        private const val COMPLIANCE = 2
        private const val FULLVALIDATION = 3
    }

    class HeaderViewHolder(private val binding: AdapterDocumentsListHeaderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(position: Int, context: Context, requiredDocumentsCount: Int) {
            with(binding) {
                if (position == 0) {
                    headerTitle.text = context.getString(R.string.activityDocumentsListTextViewRequiredDocumentsText)

                    if (requiredDocumentsCount > INT_ZERO) {
                        bubbleIndicatorView.visible()
                        bubbleIndicatorTextView.text = requiredDocumentsCount.toString()
                        requiredDocumentsImage.gone()
                        requiredDocumentsText.gone()
                    } else {
                        requiredDocumentsImage.visible()
                        requiredDocumentsText.visible()
                    }
                } else {
                    separator.visible()
                    headerTitle.text = context.getString(R.string.activityDocumentsListTextViewUploadedDocumentsText)
                }
            }
        }
    }
}
