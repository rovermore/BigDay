package com.smallworldfs.moneytransferapp.presentation.account.documents.selector

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smallworldfs.moneytransferapp.databinding.AdapterGenericDropContentBinding
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.TypesOfDocumentUIModel

class DocumentsSelectorAdapter(
    private var itemsList: List<TypesOfDocumentUIModel>,
    val listener: DocumentSelectorAdapterListener
) : RecyclerView.Adapter<DocumentsSelectorAdapter.DocumentSelectorItemHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DocumentSelectorItemHolder {
        val binding = AdapterGenericDropContentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DocumentSelectorItemHolder(binding)
    }

    override fun getItemCount() = itemsList.size

    override fun onBindViewHolder(holder: DocumentSelectorItemHolder, position: Int) {
        holder.bind(itemsList, position, listener)
    }

    fun setNewData(newData: List<TypesOfDocumentUIModel>) {
        itemsList = newData
        notifyDataSetChanged()
    }

    class DocumentSelectorItemHolder(private val binding: AdapterGenericDropContentBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: List<TypesOfDocumentUIModel>, position: Int, listener: DocumentSelectorAdapterListener) {
            with(binding) {
                text.text = data[position].name
                root.setOnClickListener {
                    listener.onItemClick(data[position].id)
                }
            }
        }
    }
}
