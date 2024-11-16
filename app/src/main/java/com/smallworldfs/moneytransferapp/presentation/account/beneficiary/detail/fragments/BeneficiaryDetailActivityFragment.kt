package com.smallworldfs.moneytransferapp.presentation.account.beneficiary.detail.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventName
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventProperty
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showLoadingDialog
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.EventObserver
import com.smallworldfs.moneytransferapp.databinding.BeneficiaryActivityEmptyViewBinding
import com.smallworldfs.moneytransferapp.databinding.FragmentBeneficiaryDetailActivityBinding
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction
import com.smallworldfs.moneytransferapp.modules.c2b.presentation.ui.activity.C2BActivity
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.CreateTransactionResponse
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericFragment
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.TransactionalActivity
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.detail.BeneficiaryDetailNavigator
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.detail.BeneficiaryDetailViewModel
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.detail.model.BeneficiaryActivityUIModel
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import com.smallworldfs.moneytransferapp.utils.STRING_SPACE
import com.smallworldfs.moneytransferapp.utils.gone
import com.smallworldfs.moneytransferapp.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import javax.inject.Inject

@AndroidEntryPoint
class BeneficiaryDetailActivityFragment : GenericFragment() {

    private val viewModelActivity: BeneficiaryDetailViewModel by activityViewModels()

    @Inject
    lateinit var navigator: BeneficiaryDetailNavigator

    private var _binding: FragmentBeneficiaryDetailActivityBinding? = null
    private val binding get() = _binding!!

    private var _emptyViewBinding: BeneficiaryActivityEmptyViewBinding? = null
    private val emptyViewBinding get() = _emptyViewBinding!!

    private lateinit var progressDialog: Dialog

    private val startTransactionalActivity =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                it.data?.let { data ->
                    val transactionCreated = data.getParcelableExtra<CreateTransactionResponse>("TRANSACTION_DATA_EXTRA")
                    if (data.getBooleanExtra("SHOW_CHECKOUT_DIALOG_EXTRA", false))
                        viewModelActivity.showDialogCheckout(transactionCreated)
                }
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentBeneficiaryDetailActivityBinding.inflate(inflater, container, false)
        _emptyViewBinding = BeneficiaryActivityEmptyViewBinding.inflate(inflater, container, false)
        setupObservers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        viewModelActivity.getBeneficiaryActivity()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _emptyViewBinding = null
        // Remove dialogs
        showProgressDialog(false)
    }

    private fun setupObservers() {
        viewModelActivity.beneficiaryActivity.observe(
            viewLifecycleOwner,
            EventObserver {
                drawData(it)
            },
        )
        viewModelActivity.calculatorUpdated.observe(
            viewLifecycleOwner,
            EventObserver { beneficiary ->

                trackEvent(
                    BrazeEvent(
                        BrazeEventName.TRANSACTION_CREATION_STEP_1.value,
                        mapOf(
                            Pair(BrazeEventProperty.DESTINATION_COUNTRY.value, beneficiary.country),
                            Pair(BrazeEventProperty.DELIVERY_METHOD.value, beneficiary.deliveryMethod.type),
                        ),
                    ),
                )

                val intent = Intent(activity, TransactionalActivity::class.java)
                intent.putExtra(TransactionalActivity.BENEFICIARY_EXTRA, viewModelActivity.beneficiary as Parcelable)
                intent.putExtra(TransactionalActivity.YOU_PAY_EXTRA, STRING_EMPTY)
                intent.putExtra(C2BActivity.BENEFICIARY_TYPE, viewModelActivity.beneficiary.beneficiaryType)
                startTransactionalActivity.launch(intent)
            },
        )
        viewModelActivity.beneficiaryError.observe(
            viewLifecycleOwner,
            EventObserver {
                binding.errorView.root.visible()
            },
        )
        viewModelActivity.showProgressDialog.observe(
            viewLifecycleOwner,
            EventObserver {
                showProgressDialog(it)
            },
        )
    }

    private fun setupView() {
        binding.fragmentBeneficiaryDetailActivityProgressBar.gone()
        emptyViewBinding.root.gone()
        binding.errorView.root.gone()
        binding.swipeRefreshLayout.isEnabled = true
        setupRefreshLayoutAction()
        setupButtonListeners()
    }

    private fun drawData(data: BeneficiaryActivityUIModel) {
        if (data.transactions.isEmpty()) {
            emptyViewBinding.root.visible()
        } else {
            binding.swipeRefreshLayout.isEnabled = true

            // Transaction info
            data.transactionInfo.let {
                val cal = Calendar.getInstance()
                binding.totalPaidValue.text = String.format("%.2f", it.moneySent) + STRING_SPACE + data.transactions[0].sendingCurrency
                binding.transfersMadeValue.text = String.format("%.0f", it.transactionsCount)
                val endPeriodYear = cal[Calendar.YEAR]
                val endPeriodMonth: String = SimpleDateFormat("MMM").format(cal.time).replace("\\.".toRegex(), STRING_EMPTY).toUpperCase(
                    Locale.getDefault(),
                )
                cal.add(Calendar.MONTH, -5)
                val startPeriodYear = cal[Calendar.YEAR]
                val startPeriodMonth: String = SimpleDateFormat("MMM").format(cal.time).replace("\\.".toRegex(), STRING_EMPTY).toUpperCase(Locale.getDefault())
                binding.timePeriod.text = "$startPeriodMonth $startPeriodYear - $endPeriodMonth $endPeriodYear"
            }

            with(binding.transactionsRecycler) {
                layoutManager = LinearLayoutManager(context)
                setHasFixedSize(true)
                adapter = BeneficiaryTransactionsAdapter(
                    context, data.transactions,
                    object : BeneficiaryTransactionsAdapter.OnItemClicked {
                        override fun itemClicked(transaction: Transaction) {
                            try {
                                val mtn = java.lang.Long.valueOf(transaction.mtn)
                                navigator.navigateToTransactionStatusDetail(mtn, transaction)
                            } catch (_: Exception) { }
                        }
                    }
                )
            }
        }
    }

    /**
     * Setup UI
     */
    private fun setupRefreshLayoutAction() {
        binding.swipeRefreshLayout.setColorSchemeColors(
            ContextCompat.getColor(binding.swipeRefreshLayout.context, R.color.main_blue),
            ContextCompat.getColor(binding.swipeRefreshLayout.context, R.color.main_blue),
            ContextCompat.getColor(binding.swipeRefreshLayout.context, R.color.main_blue)
        )
        binding.swipeRefreshLayout.setOnRefreshListener {
            binding.swipeRefreshLayout.isRefreshing = false
            viewModelActivity.getBeneficiaryActivity()
        }
    }

    private fun setupButtonListeners() {
        emptyViewBinding.sendMoneyButton.setOnClickListener {
            viewModelActivity.updateCalculatorWithBeneficiary()
        }
        binding.errorView.buttonTextRetry.setOnClickListener {
            viewModelActivity.getBeneficiaryActivity()
        }
    }

    private fun showProgressDialog(show: Boolean) {
        if (!this::progressDialog.isInitialized)
            progressDialog = (activity as AppCompatActivity).showLoadingDialog(
                getString(R.string.progress_dialog_transactional_content),
                getString(R.string.loading_text),
                true
            )
        if (show)
            progressDialog.show()
        else
            progressDialog.dismiss()
    }
}
