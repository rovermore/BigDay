package com.smallworldfs.moneytransferapp.presentation.account.beneficiary.detail.fragments

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.databinding.BeneficiaryActivityBlockBinding
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction
import com.smallworldfs.moneytransferapp.utils.DOUBLE_ZERO
import com.smallworldfs.moneytransferapp.utils.Log
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class BeneficiaryTransactionsAdapter(
    private val context: Context,
    private val transactionList: List<Transaction>,
    private val onItemClicked: OnItemClicked
) : RecyclerView.Adapter<BeneficiaryTransactionsAdapter.ViewHolder>() {

    interface OnItemClicked {
        fun itemClicked(transaction: Transaction)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.beneficiary_activity_block, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (transactionList.isNotEmpty()) {
            holder.bindView(transactionList[position])
        }
    }

    override fun getItemCount(): Int {
        if (transactionList.isEmpty()) {
            return 0
        }
        return transactionList.size
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = BeneficiaryActivityBlockBinding.bind(view)
        fun bindView(transaction: Transaction) {
            val cal = Calendar.getInstance()
            var transferDay = STRING_EMPTY
            var transferMonth = STRING_EMPTY
            var transferYear = STRING_EMPTY
            try {
                cal.time = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(transaction.createdAt) ?: Date()
                transferDay = cal[Calendar.DAY_OF_MONTH].toString()
                transferMonth = SimpleDateFormat("MMM").format(cal.time).replace("\\.".toRegex(), STRING_EMPTY).toUpperCase(Locale.getDefault())
                transferYear = cal[Calendar.YEAR].toString()
            } catch (e: ParseException) {
                Log.e("STACK", "----------------------", e)
            }
            binding.transferDay.text = transferDay
            binding.transferMonth.text = transferMonth
            binding.transferYear.text = transferYear

            var transferReceived = transaction.payoutPrincipal
            if (transferReceived == null) {
                transferReceived = STRING_EMPTY
            }
            val transferReceivedAmount: Double = try {
                transferReceived.toDouble()
            } catch (e: Exception) {
                DOUBLE_ZERO
            }
            var transferReceivedPayText: String? = String.format("%.2f", transferReceivedAmount) + " " + transaction.payoutCurrency
            var transferPay = transaction.totalSale
            if (transferPay == null) {
                transferPay = STRING_EMPTY
            }
            val transferPayAmount: Double = try {
                transferPay.toDouble()
            } catch (e: Exception) {
                DOUBLE_ZERO
            }
            var transferPayText = String.format(context.getString(R.string.beneficiary_detail_activity_transaction_pay), transferPayAmount) + " " + transaction.sendingCurrency
            if (transaction.isOffline) {
                transferReceivedPayText = transaction.mtn
                transferPayText = context.getString(R.string.offline_transaction_action_label)
            }
            binding.transferReceived.text = transferReceivedPayText
            binding.transferPay.text = transferPayText

            binding.mainContainer.setOnClickListener {
                onItemClicked.itemClicked(transaction)
            }
        }
    }
}
