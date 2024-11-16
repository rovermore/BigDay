package com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayoutMediator
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventName
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventProperty
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventType
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showSingleActionInfoDialog
import com.smallworldfs.moneytransferapp.databinding.ActivityBeneficiaryListBinding
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.Constants.RESULT_CODES
import com.smallworldfs.moneytransferapp.utils.INT_ONE
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class BeneficiaryListActivity : GenericActivity() {

    private var _binding: ActivityBeneficiaryListBinding? = null
    private val binding get() = _binding!!

    @Inject
    lateinit var navigator: BeneficiaryListNavigator

    private lateinit var mAdapter: BeneficiaryListFragmentAdapter

    companion object {
        const val NUMBER_OF_TABS = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityBeneficiaryListBinding.inflate(layoutInflater)

        setContentView(binding.root)

        setupToolbar()
        setupListeners()
        setupView()

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    registerEvent("click_back")
                    overridePendingTransition(R.anim.from_left_to_right, R.anim.from_position_to_right)
                    finish()
                }
            },
        )
    }

    private fun setupView() {
        with(binding) {
            for (i in INT_ZERO until NUMBER_OF_TABS) {
                beneficiaryListTabLayout.addTab(beneficiaryListTabLayout.newTab())
            }

            mAdapter = BeneficiaryListFragmentAdapter(this@BeneficiaryListActivity, NUMBER_OF_TABS)

            beneficiaryListViewPager.adapter = mAdapter
            beneficiaryListViewPager.offscreenPageLimit = 2

            // Attach
            TabLayoutMediator(beneficiaryListTabLayout, beneficiaryListViewPager) { _, _ -> }.attach()

            beneficiaryListViewPager.registerOnPageChangeCallback(
                object : OnPageChangeCallback() {
                    override fun onPageSelected(position: Int) {
                        super.onPageSelected(position)
                        if (position == INT_ZERO) {
                            registerEvent("click_view_all")
                        } else if (position == INT_ONE) {
                            registerEvent("click_view_delivery_method")
                        }
                        updateSelection(position)
                    }
                },
            )

            for (i in INT_ZERO until NUMBER_OF_TABS) {
                beneficiaryListTabLayout.getTabAt(i)?.customView = mAdapter.getTabView(i)
            }
        }

        updateSelection(INT_ZERO)
    }

    private fun updateSelection(position: Int) {
        for (i in INT_ZERO until NUMBER_OF_TABS) {
            if (i == position) {
                (binding.beneficiaryListTabLayout.getTabAt(position)?.customView?.findViewById<View>(R.id.page_title) as StyledTextView)
                    .setTextColor(ContextCompat.getColor(this, R.color.white))
            } else {
                (binding.beneficiaryListTabLayout.getTabAt(i)?.customView?.findViewById<View>(R.id.page_title) as StyledTextView)
                    .setTextColor(ContextCompat.getColor(this, R.color.white_transparency_60))
            }
        }
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.beneficiaryListToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupListeners() {
        binding.beneficiaryListFloatingActionButton.setOnClickListener {
            registerEvent("click_add_beneficiary")
            trackEvent(
                BrazeEvent(
                    BrazeEventName.BENEFICIARY_CREATION_STEP_1.value,
                    HashMap<String, String>().apply {
                        put(
                            BrazeEventProperty.SCREEN_TYPE.value,
                            if (binding.beneficiaryListViewPager.currentItem == 0) "ALL" else "Delivery Method",
                        )
                    },
                    BrazeEventType.ACTION,
                ),
            )
            navigator.navigateToNewBeneficiaryStepCountry()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when {
            requestCode == Constants.REQUEST_CODES.NEW_BENEFICIARY && resultCode == Activity.RESULT_OK -> {
                showSingleActionInfoDialog(getString(R.string.action_done_transactional_calculator), getString(R.string.new_beneficiary_created_ok_text)) {}
            }

            requestCode == Constants.REQUEST_CODES.BENEFICIARY_DETAIL && resultCode == RESULT_CODES.BENEFICIARY_DELETED -> {
                showSingleActionInfoDialog(getString(R.string.action_done_transactional_calculator), getString(R.string.beneficiary_deleted_ok_text)) {}
            }

            else -> {}
        }

        setupView()
        setResult(resultCode)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun registerEvent(eventAction: String) {
        trackEvent(
            UserActionEvent(
                ScreenCategory.DASHBOARD.value,
                eventAction,
                "",
                getHierarchy(""),
            ),
        )
    }
}
