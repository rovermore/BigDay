package com.smallworldfs.moneytransferapp.presentation.common.countries

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.loadCircularImage
import com.smallworldfs.moneytransferapp.databinding.AdapterListCountryBinding

class StateSearchAdapter constructor(private val context: Context, private val separatorIndex: Int = -1) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val initialData = mutableListOf<StateUIModel>()
    private val filteredStates = mutableListOf<StateUIModel>()
    var selectedCountry: StateUIModel? = null
        private set
    private var callback: ((StateUIModel) -> (Unit))? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = StateViewHolder(AdapterListCountryBinding.inflate(LayoutInflater.from(context), null, false))

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        (holder as StateViewHolder).name.text = filteredStates[position].name

        holder.image.loadCircularImage(
            context,
            R.drawable.placeholder_country_adapter,
            filteredStates[position].logo
        )

        holder.rootView.setOnClickListener { view ->
            callback?.invoke(filteredStates[position])
        }

        holder.separator.visibility = if (position == separatorIndex) View.VISIBLE else View.GONE
    }

    override fun getItemCount() = filteredStates.size

    fun setOnStateSelectedCallback(callback: (StateUIModel) -> (Unit)) {
        this.callback = callback
    }

    fun setData(data: List<StateUIModel>) {
        initialData.clear()
        initialData.addAll(data)
        filteredStates.clear()
        filteredStates.addAll(data)
        notifyDataSetChanged()
    }

    fun filterByName(text: String) {
        filteredStates.clear()
        filteredStates.addAll(
            initialData.filter {
                it.name.toLowerCase().contains(text.toLowerCase())
            }
        )
        notifyDataSetChanged()
    }

    fun restoreInitialData() {
        filteredStates.clear()
        filteredStates.addAll(initialData)
        notifyDataSetChanged()
    }

    class StateViewHolder(binding: AdapterListCountryBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.countryText
        val image = binding.countryImage
        val rootView = binding.rootView
        val separator = binding.adapterListCountryBlueSeparator
    }
}
