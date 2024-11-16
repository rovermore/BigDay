package com.smallworldfs.moneytransferapp.presentation.account.documents.verification

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smallworldfs.moneytransferapp.databinding.ComplianceListItemBinding
import com.smallworldfs.moneytransferapp.databinding.DocumentsListItemBinding
import com.smallworldfs.moneytransferapp.databinding.UploadDocumentDialogBinding
import com.smallworldfs.moneytransferapp.presentation.account.documents.ComplianceDocumentViewHolder
import com.smallworldfs.moneytransferapp.presentation.account.documents.FullValidationViewHolder
import com.smallworldfs.moneytransferapp.presentation.account.documents.IdDocumentViewHolder
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.OnDocumentActionClickListener
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.ComplianceDocUIModel
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.DocumentIdUIModel
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.DocumentUIModel

class VerificationAdapter(private val context: Context, documents: List<DocumentUIModel>, val listener: OnDocumentActionClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object TypeOfElement {
        private const val ID = 0
        private const val COMPLIANCE = 1
        private const val FULLVALIDATION = 2
    }

    private var documentItems = mutableListOf<DocumentUIModel>().apply { addAll(documents) }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder = when (viewType) {
        ID -> {
            val binding = DocumentsListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            IdDocumentViewHolder(binding)
        }

        FULLVALIDATION -> {
            val binding = UploadDocumentDialogBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            FullValidationViewHolder(binding)
        }

        else -> {
            val binding = ComplianceListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ComplianceDocumentViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ID -> {
                (holder as IdDocumentViewHolder).bind(
                    documentItems[position] as DocumentIdUIModel,
                )
            }

            FULLVALIDATION -> {
                (holder as FullValidationViewHolder).bind(
                    documentItems[position] as ComplianceDocUIModel.FullValidationUIModel,
                    listener,
                )
            }

            COMPLIANCE -> {
                (holder as ComplianceDocumentViewHolder).bind(
                    documentItems[position] as ComplianceDocUIModel,
                    listener,
                )
            }
        }
    }

    override fun getItemCount(): Int = documentItems.size

    override fun getItemViewType(position: Int) = when (documentItems[position]) {
        is ComplianceDocUIModel.FullValidationUIModel -> FULLVALIDATION
        is ComplianceDocUIModel -> COMPLIANCE
        else -> ID
    }

    fun updateDocuments(newListItems: List<DocumentUIModel>) {
        documentItems.clear()
        documentItems.addAll(newListItems)
        notifyDataSetChanged()
    }
}
