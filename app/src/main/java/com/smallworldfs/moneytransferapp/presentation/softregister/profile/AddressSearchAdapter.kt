package com.smallworldfs.moneytransferapp.presentation.softregister.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smallworldfs.moneytransferapp.databinding.AdapterListAddressBinding
import com.smallworldfs.moneytransferapp.presentation.common.countries.AddressUIModel
import com.smallworldfs.moneytransferapp.utils.gone
import com.smallworldfs.moneytransferapp.utils.visible

class AddressSearchAdapter constructor(private val context: Context) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val addressList = mutableListOf<AddressUIModel>()
    private var callback: ((AddressUIModel) -> (Unit))? = null
    private val RESULTS_LIMIT = 7

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        AddressViewHolder(
            AdapterListAddressBinding.inflate(
                LayoutInflater.from(context), null, false
            )
        )

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as AddressViewHolder).addressText.text = addressList[position].detail.text
            .plus(addressList[position].detail.description)

        holder.rootView.setOnClickListener { _ ->
            callback?.let { it.invoke(addressList[position]) }
        }
        if (addressList[position].type != "ADDRESS")
            holder.arrowImage.visible()
        else
            holder.arrowImage.gone()
    }

    override fun getItemCount(): Int {
        return if (addressList.size > RESULTS_LIMIT) RESULTS_LIMIT
        else addressList.size
    }

    fun setOnAddressSelectedCallback(callback: (AddressUIModel) -> (Unit)) {
        this.callback = callback
    }

    fun setData(data: List<AddressUIModel>) {
        addressList.clear()
        addressList.addAll(data)
        notifyDataSetChanged()
    }

    class AddressViewHolder(binding: AdapterListAddressBinding) : RecyclerView.ViewHolder(binding.root) {
        val addressText = binding.addressTextView
        val arrowImage = binding.arrowAddress
        val rootView = binding.rootView
    }
}
