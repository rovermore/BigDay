package com.smallworldfs.moneytransferapp.presentation.form.selector

import android.R
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.smallworldfs.moneytransferapp.base.presentation.ui.BaseStateAppCompatActivity
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.setTextIfHasChanged
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.BaseExtraData
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.Error
import com.smallworldfs.moneytransferapp.databinding.ActivityFormSelectorBinding
import com.smallworldfs.moneytransferapp.utils.PROCESS_CATEGORY
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_form_selector.*

@AndroidEntryPoint
class FormSelectorActivity : BaseStateAppCompatActivity<FormSelectorState, FormSelectorViewModel, FormSelectorNavigator, ActivityFormSelectorBinding>(FormSelectorViewModel::class.java) {

    companion object {
        private const val ONE_SECOND = 1000L
    }

    /**
     * Attributes
     */
    override fun setInputState(intent: Intent?): FormSelectorState? = intent?.getSerializableExtra("inputState") as FormSelectorState?

    override val bindingInflater: (LayoutInflater) -> ActivityFormSelectorBinding = ActivityFormSelectorBinding::inflate

    private lateinit var vm: FormSelectorViewModel

    private lateinit var mAdapter: FormSelectorItemAdapter

    private var screenNameEvent: String = STRING_EMPTY
    private var checkoutStep: String = STRING_EMPTY
    private var processCategory: String = STRING_EMPTY

    private var lastTimeEventSend = 0L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        onBackPressedDispatcher.addCallback(
            this,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    registerEvent("click_back", "")
                    finish()
                }
            }
        )
    }

    /**
     * When the view model is initialized
     */
    override fun onViewModelInitialized(viewModel: FormSelectorViewModel) {
        vm = viewModel

        // Setup UI
        setupToolbar()
        setupEditTextListener()
        setupRecyclerView()
        (intent?.getSerializableExtra("inputState") as FormSelectorState?)?.let {
            trackScreen(it.screenName)
            when (it.screenName) {
                ScreenName.STATE_OR_REGION_SCREEN.value -> screenNameEvent = "state_region"
                ScreenName.CITY_SCREEN.value -> screenNameEvent = "city"
                ScreenName.CHOOSE_PAYMENT_NETWORK_SCREEN.value -> {
                    screenNameEvent = "payment_network"
                    checkoutStep = "1_choosePaymentNetwork"
                }
                ScreenName.SEARCH_OFFICE_LOCATION_SCREEN.value -> screenNameEvent = "office_location"
            }
        }
        processCategory = intent?.extras?.getString(PROCESS_CATEGORY, "") ?: ""
    }

    override fun onStateNormal(data: FormSelectorState) {

        activityFormSelectorSearchContainer.visibility = if (data.isVisibleSearchContainer) View.VISIBLE else View.GONE

        supportActionBar?.title = data.toolbarTitle

        updateList(data.filteredList)

        activityFormSelectorSearchEditText.setTextIfHasChanged(data.searchText)
    }

    override fun onStateAlternative(data: BaseExtraData) {}

    override fun onStateError(error: Throwable) {
        if (error is FormSelectorFieldThrowable) {
            showGenericError(error.title, error.body, Error.FIELD_ERROR)
        }
    }

    /**
     * Setup UI
     */
    private fun setupToolbar() {
        setSupportActionBar(activityFormSelectorToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun setupEditTextListener() {
        activityFormSelectorSearchEditText.addTextChangedListener(
            object : TextWatcher {
                override fun afterTextChanged(p0: Editable?) {}
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}
                override fun onTextChanged(s: CharSequence, p1: Int, p2: Int, p3: Int) {
                    if (s.length > 2 && (System.currentTimeMillis() - lastTimeEventSend) > ONE_SECOND) {
                        registerEvent(
                            ScreenCategory.DASHBOARD.value,
                            "search_".plus(screenNameEvent),
                            s.toString(),
                            "search"
                        )
                        lastTimeEventSend = System.currentTimeMillis()
                    }
                    vm.onActionChangeSearchText(s.toString())
                }
            },
        )
    }

    private fun setupRecyclerView() {
        mAdapter = FormSelectorItemAdapter(
            mutableListOf(),
            object : FormSelectorItemAdapter.OnItemClickListener {
                override fun onItemClick(item: FormSelectorItem) {
                    vm.onActionSelectItem(item)
                    registerEvent(ScreenCategory.DASHBOARD.value, "click_".plus(screenNameEvent).plus("_list"), item.key)
                }
            },
        )
        activityFormSelectorRecyclerView.apply {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@FormSelectorActivity)
            adapter = mAdapter
            itemAnimator = DefaultItemAnimator()
        }
    }

    /**
     * Update list of recycler view
     */
    private fun updateList(list: List<FormSelectorItem>?) {
        mAdapter.listItems = list ?: mutableListOf()
        mAdapter.notifyDataSetChanged()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    private fun registerEvent(screenCategory: String, eventAction: String = "", eventLabel: String = "", formType: String = "", processCategory: String = this.processCategory, checkoutStep: String = this.checkoutStep) {
        trackEvent(
            UserActionEvent(
                screenCategory,
                eventAction,
                eventLabel,
                getHierarchy((intent?.getSerializableExtra("inputState") as FormSelectorState?)?.screenName ?: ""),
                formType,
                processCategory,
                checkoutStep,
            ),
        )
    }
}
