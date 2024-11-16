package com.smallworldfs.moneytransferapp.presentation.account.beneficiary.detail

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showDoubleActionGeneralDialog
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showSingleActionInfoDialog
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.EventObserver
import com.smallworldfs.moneytransferapp.databinding.ActivityBeneficiaryDetailBinding
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Beneficiary
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction
import com.smallworldfs.moneytransferapp.modules.beneficiary.presentation.ui.activity.EditBeneficiaryActivity
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.CreateTransactionResponse
import com.smallworldfs.moneytransferapp.modules.checkout.presentation.ui.fragment.CheckoutDialogFragment
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModel
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.INPUT_STATE
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BeneficiaryDetailActivity : GenericActivity() {

    private val viewModel: BeneficiaryDetailViewModel by viewModels()

    private var _binding: ActivityBeneficiaryDetailBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var navigator: BeneficiaryDetailNavigator

    private var mConfirmCheckoutDialogFragment: CheckoutDialogFragment? = null

    private val startEditBeneficiary =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) {
            when (it.resultCode) {
                Constants.RESULT_CODES.BENEFICIARY_UPDATED -> {
                    showSingleActionInfoDialog(getString(R.string.action_done_transactional_calculator), getString(R.string.beneficiary_updated_ok_text)) {}
                    val beneficiary = it.data?.extras?.getParcelable<Beneficiary>(EditBeneficiaryActivity.BENEFICIARY_EXTRA)
                    beneficiary?.let { beneficiary ->
                        viewModel.beneficiaryEdited(beneficiary)
                    }
                    this.setResult(Constants.RESULT_CODES.BENEFICIARY_UPDATED)
                }
                Constants.RESULT_CODES.BENEFICIARY_CREATED -> {
                    showSingleActionInfoDialog(getString(R.string.action_done_transactional_calculator), getString(R.string.new_beneficiary_created_ok_text)) {}
                }
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityBeneficiaryDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (mConfirmCheckoutDialogFragment != null && mConfirmCheckoutDialogFragment!!.isVisible)
                        closeDialogFragment()
                    else
                        finish()
                }
            },
        )

        val data = intent?.getSerializableExtra(INPUT_STATE) as BeneficiaryDetailState
        viewModel.setBeneficiary(data.beneficiary)

        setupView()
        setUpObservers()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setUpObservers() {
        viewModel.beneficiaryDeleted.observe(
            this,
            EventObserver {
                finish()
            },
        )

        viewModel.showDialogCheckout.observe(
            this,
            EventObserver {
                showDialogCheckout(it)
            },
        )

        viewModel.showEditBeneficiary.observe(
            this,
            EventObserver {
                navigateToEditBeneficiary(it)
            },
        )
    }

    private fun setupView() {
        setSupportActionBar(binding.activityBeneficiaryDetailToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = viewModel.beneficiary.nameOrNickName

        // Add tabs
        binding.activityBeneficiaryDetailTabLayout.addTab(binding.activityBeneficiaryDetailTabLayout.newTab().setText(getString(R.string.tab_beneficiary_detail_profile_title)))
        binding.activityBeneficiaryDetailTabLayout.addTab(binding.activityBeneficiaryDetailTabLayout.newTab().setText(getString(R.string.tab_beneficiary_detail_activity_title)))

        val adapter = BeneficiaryDetailFragmentAdapter(this, 2)
        with(binding.activityBeneficiaryDetailViewPager) {
            this.adapter = adapter
            offscreenPageLimit = 2
            TabLayoutMediator(binding.activityBeneficiaryDetailTabLayout, this) { _, _ -> }.attach()
            registerOnPageChangeCallback(
                object : ViewPager2.OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        updateSelection(position)
                    }
                },
            )
        }

        // Iterate over all tabs and set the custom view
        for (i in INT_ZERO until 2) {
            binding.activityBeneficiaryDetailTabLayout.getTabAt(i)?.customView = adapter.getTabView(i)
        }

        // Mark default screen selected
        updateSelection(INT_ZERO)
    }

    private fun updateSelection(position: Int) {
        for (i in INT_ZERO until 2) {
            if (i == position) {
                (binding.activityBeneficiaryDetailTabLayout.getTabAt(position)?.customView?.findViewById<View>(R.id.page_title) as StyledTextView).setTextColor(ContextCompat.getColor(this, R.color.white))
            } else {
                (binding.activityBeneficiaryDetailTabLayout.getTabAt(i)?.customView?.findViewById<View>(R.id.page_title) as StyledTextView).setTextColor(ContextCompat.getColor(this, R.color.white_transparency_60))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.beneficiary_detail_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }
            R.id.edit_beneficiary -> {
                viewModel.editBeneficiary()
                true
            }
            R.id.delete_beneficiary -> {
                showDoubleActionGeneralDialog(
                    getString(R.string.delete_beneficiary_dialog_title),
                    getString(R.string.delete_beneficiary_dialog_content),
                    getString(R.string.delete_text),
                    { viewModel.deleteBeneficiary() },
                    getString(R.string.cancel),
                    { },
                    positiveContentDescription = "delete_button",
                    negativeContentDescription = "cancel_button"
                )
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun navigateToEditBeneficiary(beneficiary: Beneficiary) {
        val i = Intent(this, EditBeneficiaryActivity::class.java)
        i.putExtra("BENEFICIARY_DETAIL", beneficiary as Parcelable)
        startEditBeneficiary.launch(i)
    }

    private fun showDialogCheckout(transactionCreated: CreateTransactionResponse) {
        binding.activityBeneficiaryDetailDialogFragment.visibility = View.VISIBLE
        mConfirmCheckoutDialogFragment = CheckoutDialogFragment.getInstance(Constants.DIALOG_CHECKOUT_STYLE.SUCCESS_STYLE, transactionCreated.errors, transactionCreated.transaction, transactionCreated.summary)
        mConfirmCheckoutDialogFragment?.setCloseListener(
            object : CheckoutDialogFragment.CloseListener {
                override fun checkErrorsNow() {}

                override fun closeAndRequestHelpEmail() {}

                override fun closeDialogAndShowTransactions(transaction: TransactionUIModel?) {}

                override fun closeDialogAndShowTransactions(transaction: Transaction) {
                    closeDialogFragment()
                    navigator.navigateToTransferDetails(transaction)
                }

                override fun closeDialog() {
                    closeDialogFragment()
                }

                override fun closeAndGoToPayNow(mtn: String) {
                    closeDialogFragment()
                    navigator.navigateToPayNowActivity(mtn)
                }
            },
        )
        mConfirmCheckoutDialogFragment?.let {
            supportFragmentManager.beginTransaction().replace(R.id.activityBeneficiaryDetailDialogFragment, it).commit()
        }
    }

    private fun closeDialogFragment() {
        if (mConfirmCheckoutDialogFragment != null) {
            supportFragmentManager.beginTransaction().remove(mConfirmCheckoutDialogFragment!!).commit()
            mConfirmCheckoutDialogFragment = null
            binding.activityBeneficiaryDetailDialogFragment.visibility = View.GONE
        }
    }
}
