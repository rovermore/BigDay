package com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.fragments.common

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.loadCircularImage
import com.smallworldfs.moneytransferapp.databinding.AdapterBeneficiaryListAllItemBinding
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.model.BeneficiaryUIModel
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.INT_ONE
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.gone
import com.smallworldfs.moneytransferapp.utils.visible
import java.util.Locale
import java.util.regex.Pattern

class BeneficiaryListAdapter(
    beneficiaryList: List<BeneficiaryUIModel>,
    private val listener: BeneficiaryListListener
) : RecyclerView.Adapter<BeneficiaryListAdapter.BeneficiaryItemHolder>() {

    private var itemList = beneficiaryList.toMutableList()

    var isLastPage = false
    var isLoading = false
    var isFooterAdded = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BeneficiaryItemHolder {
        val binding = AdapterBeneficiaryListAllItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BeneficiaryItemHolder(binding)
    }

    override fun onBindViewHolder(holder: BeneficiaryItemHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item, position, listener)
    }

    override fun getItemCount() = itemList.size

    fun add(item: BeneficiaryUIModel) {
        itemList.add(item)
        notifyItemInserted(itemList.size - INT_ONE)
    }

    fun replaceAll(items: List<BeneficiaryUIModel>) {
        itemList = items.toMutableList()
        notifyDataSetChanged()
    }

    fun addLoadingFooter() {
        isFooterAdded = true
    }

    fun removeFooter() {
        if (isFooterAdded) {
            isFooterAdded = false
            itemList.removeAt(itemList.size - INT_ONE)
            notifyItemRemoved(itemList.size - INT_ONE)
        }
    }

    class BeneficiaryItemHolder(private val binding: AdapterBeneficiaryListAllItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: BeneficiaryUIModel, position: Int, listener: BeneficiaryListListener) {
            with(binding) {
                val nameOrNickName = item.nameOrNickName
                val beneficiaryFirstLetter = if (nameOrNickName.isNotEmpty()) {
                    nameOrNickName.substring(INT_ZERO, INT_ONE).uppercase(Locale.getDefault())
                } else {
                    STRING_EMPTY
                }

                val beneficiarySecondLetter = getSecondCapitalizedLetterFor(item)

                beneficiaryListAllUserNameLetterText.text = beneficiaryFirstLetter
                beneficiaryListAllUserNameLetterText2.text = beneficiarySecondLetter

                if (item.payoutCountry.iso3.isNotEmpty()) {
                    beneficiaryListAllCountryFlag.loadCircularImage(
                        binding.containerView.context,
                        R.drawable.placeholder_country_adapter,
                        Constants.COUNTRY.FLAG_IMAGE_ASSETS + item.payoutCountry.iso3 + Constants.COUNTRY.FLAG_IMAGE_EXTENSION
                    )
                }

                if (nameOrNickName.isNotEmpty()) {
                    beneficiaryListAllBeneficiaryNameText.text = nameOrNickName
                } else {
                    beneficiaryListAllBeneficiaryNameText.text = STRING_EMPTY
                }

                if (item.deliveryMethod.name.isNotEmpty()) {
                    beneficiaryListAllBeneficiaryOperationText.text = item.deliveryMethod.name
                } else {
                    beneficiaryListAllBeneficiaryOperationText.text = STRING_EMPTY
                }

                if (item.bankName.isNotEmpty()) {
                    beneficiaryListAllBankName.text = item.bankName
                } else {
                    beneficiaryListAllBankName.text = STRING_EMPTY
                }

                if (item.isNew) {
                    beneficiaryListAllNewLabel.visible()
                } else {
                    beneficiaryListAllNewLabel.gone()
                }

                binding.containerView.setOnClickListener {
                    listener.onItemClick(item, position)
                }
            }
        }

        private fun getSecondCapitalizedLetterFor(beneficiary: BeneficiaryUIModel): String {
            var beneficiarySecondLetter = STRING_EMPTY
            if (beneficiary.alias.isNotEmpty()) {
                val matcher = Pattern.compile("\\s([A-Za-z-0-9]+)").matcher(beneficiary.nameOrNickName)
                if (matcher.find()) {
                    beneficiarySecondLetter = matcher.group(INT_ONE)?.substring(INT_ZERO, INT_ONE)?.uppercase(Locale.getDefault()) ?: STRING_EMPTY
                }
            } else {
                beneficiarySecondLetter = if (beneficiary.surname.isNotEmpty()) beneficiary.surname.substring(INT_ZERO, INT_ONE).uppercase(Locale.getDefault()) else STRING_EMPTY
            }
            return beneficiarySecondLetter
        }
    }
}
