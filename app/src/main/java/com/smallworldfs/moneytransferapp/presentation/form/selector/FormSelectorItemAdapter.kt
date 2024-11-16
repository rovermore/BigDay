package com.smallworldfs.moneytransferapp.presentation.form.selector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.loadCircularImage
import kotlinx.android.synthetic.main.adapter_list_form_selector_item.view.*

class FormSelectorItemAdapter(itemsList: List<FormSelectorItem>, val listener: OnItemClickListener) : RecyclerView.Adapter<FormSelectorItemAdapter.ViewHolder>() {

    var listItems = itemsList

    /**
     * Number of items that are in the list
     */
    override fun getItemCount() = listItems.size

    /**
     * Provide a reference to the views for each data item
     */
    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_list_form_selector_item, parent, false))
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItems[position]

        if (item.scaleType == null) {
            holder.view.adapterListFormSelectorItemImage.scaleType = ImageView.ScaleType.FIT_XY
        } else {
            holder.view.adapterListFormSelectorItemImage.scaleType = item.scaleType
        }

        if (item.drawable != null) {

            holder.view.adapterListFormSelectorItemImage.visibility = View.VISIBLE

            holder.view.adapterListFormSelectorItemImage.setImageDrawable(ContextCompat.getDrawable(holder.view.adapterListFormSelectorItemImage.context, item.drawable))
        } else {

            if (item.urlDrawable.isNullOrEmpty()) {

                holder.view.adapterListFormSelectorItemImage.visibility = View.GONE
            } else {

                holder.view.adapterListFormSelectorItemImage.loadCircularImage(
                    holder.itemView.context,
                    R.drawable.placeholder_country_adapter,
                    item.urlDrawable
                )
            }
        }

        holder.view.adapterListFormSelectorItemText.text = item.value

        holder.itemView.setOnClickListener { listener.onItemClick(item) }
    }

    /**
     * Set click listener
     */
    interface OnItemClickListener {
        fun onItemClick(item: FormSelectorItem)
    }
}
