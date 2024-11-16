package com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.utils

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.STRING_NEW_LINE
import kotlinx.android.synthetic.main.adapter_cash_pick_up_map_list_item.view.*

@SuppressLint("SetTextI18n")
class CashPickUpMapAdapter(itemsList: MutableList<CashPickUpMarkerPresentationModel>, val listener: OnItemClickListener) : RecyclerView.Adapter<CashPickUpMapAdapter.ViewHolder>() {

    var listItems = itemsList

    override fun getItemCount() = listItems.size

    class ViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.adapter_cash_pick_up_map_list_item, parent, false))

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = listItems[position]
        holder.itemView.adapterCashPickUpMapListItemTextViewTitle.text = (if (!item.locationName.isNullOrEmpty()) item.locationName + STRING_NEW_LINE else STRING_EMPTY) + if (!item.locationAddress.isNullOrEmpty()) item.locationAddress else STRING_EMPTY
        if (item.selected) {
            holder.itemView.adapterCashPickUpMapListItemConstraintLayout.setBackgroundResource(R.drawable.background_activity_cash_pick_up_map_list_item_selected)
        } else {
            holder.itemView.adapterCashPickUpMapListItemConstraintLayout.setBackgroundResource(R.drawable.background_activity_cash_pick_up_map_list_item)
        }

        holder.itemView.setOnClickListener {
            it.adapterCashPickUpMapListItemConstraintLayout.setBackgroundResource(R.drawable.background_activity_cash_pick_up_map_list_item_selected)
            listener.onItemClick(item)
        }
    }

    interface OnItemClickListener {
        fun onItemClick(item: CashPickUpMarkerPresentationModel)
    }

    fun updateItem(item: CashPickUpMarkerPresentationModel) {
        listItems.find { it == item }?.let {
            it.selected = item.selected
        }
        notifyDataSetChanged()
    }
}
