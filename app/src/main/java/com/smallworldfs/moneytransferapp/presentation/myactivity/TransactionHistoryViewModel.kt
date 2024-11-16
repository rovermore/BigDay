package com.smallworldfs.moneytransferapp.presentation.myactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.smallworldfs.moneytransferapp.domain.migrated.base.Success
import com.smallworldfs.moneytransferapp.domain.migrated.transactions.usecase.GetTransactionsUseCase
import com.smallworldfs.moneytransferapp.presentation.myactivity.model.MonthlyTransactionHistoryDetail
import com.smallworldfs.moneytransferapp.presentation.myactivity.model.TransactionHistoryBarUIModel
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModel
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModelMapper
import com.smallworldfs.moneytransferapp.utils.DOUBLE_ZERO
import com.smallworldfs.moneytransferapp.utils.INT_ONE
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import kotlin.collections.HashMap

@HiltViewModel
class TransactionHistoryViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val transactionUIModelMapper: TransactionUIModelMapper
) : ViewModel() {

    companion object {
        const val MONTH_FORMAT = "MMM"
        const val MONTH_FULL_NAME_FORMAT = "MMMM"
        const val NUMBER_OF_TABS = 6
    }

    private val _monthlyTransactions: MutableStateFlow<HashMap<String, ArrayList<TransactionUIModel>>> = MutableStateFlow(HashMap())
    val monthlyTransactions get() = _monthlyTransactions.asStateFlow()

    private var _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _transactionHistory: MutableStateFlow<TransactionHistoryBarUIModel> = MutableStateFlow(TransactionHistoryBarUIModel())
    val transactionHistory get() = _transactionHistory.asStateFlow()

    private val _getTransactionsError = MutableStateFlow(false)
    val getTransactionsError get() = _getTransactionsError.asStateFlow()

    init {
        getTransactions()
    }

    fun getTransactions() {
        _loading.value = true
        _getTransactionsError.value = false
        viewModelScope.launch(Dispatchers.IO) {
            when (val result = getTransactionsUseCase.getUserTransactions()) {
                is Success -> processTransactions(transactionUIModelMapper.map(result.value.cancellationMessage, result.value.transactions))
                else -> {
                    _getTransactionsError.value = true
                    _loading.value = false
                }
            }
        }
    }

    private fun processTransactions(transactions: List<TransactionUIModel>) {
        _loading.value = false
        if (transactions.size > INT_ZERO) {
            val transactionsMap = LinkedHashMap<String, ArrayList<TransactionUIModel>>()
            val monthFormat = SimpleDateFormat(MONTH_FORMAT)

            // Create tree map keys
            val cal = Calendar.getInstance()
            cal.time = Date()
            cal.add(Calendar.MONTH, -(NUMBER_OF_TABS - INT_ONE))
            val month1Key = monthFormat.format(cal.time)
            transactionsMap[month1Key] = ArrayList()
            cal.add(Calendar.MONTH, INT_ONE)
            val month2Key = monthFormat.format(cal.time)
            transactionsMap[month2Key] = ArrayList()
            cal.add(Calendar.MONTH, INT_ONE)
            val month3Key = monthFormat.format(cal.time)
            transactionsMap[month3Key] = ArrayList()
            cal.add(Calendar.MONTH, INT_ONE)
            val month4Key = monthFormat.format(cal.time)
            transactionsMap[month4Key] = ArrayList()
            cal.add(Calendar.MONTH, INT_ONE)
            val month5Key = monthFormat.format(cal.time)
            transactionsMap[month5Key] = ArrayList()
            cal.add(Calendar.MONTH, INT_ONE)
            val month6Key = monthFormat.format(cal.time)
            transactionsMap[month6Key] = ArrayList()

            // Save the transactions in the tree map values
            transactions.forEach { transaction ->
                val monthKey = monthFormat.format(transaction.createdAt.time)

                transactionsMap[monthKey]?.add(transaction)
            }
            calculateMonthStats(transactionsMap)

            _monthlyTransactions.value = transactionsMap
        } else {
            _monthlyTransactions.value = HashMap()
        }
    }

    private fun calculateMonthStats(transactionsMap: Map<String, ArrayList<TransactionUIModel>>) {
        val transactionsHistoryMap = LinkedHashMap<String, MonthlyTransactionHistoryDetail>()

        var allMonthsMax = DOUBLE_ZERO
        val monthTotals = ArrayList<Double>()
        transactionsMap.forEach { entry ->
            var totalValue = DOUBLE_ZERO
            entry.value.let { transactionList ->
                transactionList.forEach { transaction ->
                    if (transaction.totalSale.isNotEmpty()) {
                        totalValue += transaction.totalSale.toDouble()
                    }
                }
            }
            if (totalValue > allMonthsMax) {
                allMonthsMax = totalValue
            }
            monthTotals.add(totalValue)
            val date = SimpleDateFormat(MONTH_FORMAT).parse(entry.key)
            val fullMonthName = SimpleDateFormat(MONTH_FULL_NAME_FORMAT).format(date)
            transactionsHistoryMap[entry.key] = MonthlyTransactionHistoryDetail(entry.key, totalValue, entry.value.size, fullMonthName, if (entry.value.size > 0) entry.value[0].sendingCurrency else STRING_EMPTY)
        }
        _transactionHistory.value = TransactionHistoryBarUIModel(transactionsHistoryMap, allMonthsMax)
    }
}
