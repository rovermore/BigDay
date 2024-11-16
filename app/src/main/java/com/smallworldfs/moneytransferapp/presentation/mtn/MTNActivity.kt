package com.smallworldfs.moneytransferapp.presentation.mtn

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.EventObserver
import com.smallworldfs.moneytransferapp.databinding.ActivityMtnBinding
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity
import com.smallworldfs.moneytransferapp.presentation.base.ErrorType
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel
import com.smallworldfs.moneytransferapp.presentation.common.countries.SearchCountryActivity
import com.smallworldfs.moneytransferapp.presentation.mtn.model.MtnQrErrorUIModel
import com.smallworldfs.moneytransferapp.presentation.mtn.model.MtnStatusUIModel
import com.smallworldfs.moneytransferapp.utils.gone
import com.smallworldfs.moneytransferapp.utils.visible
import com.smallworldfs.moneytransferapp.utils.widget.RegionPickerUIModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MTNActivity : GenericActivity() {

    private val viewModel: MTNViewModel by viewModels()

    private var _binding: ActivityMtnBinding? = null
    private val binding get() = _binding!!

    lateinit var transactionView: TransactionFlow

    private val startSearch =
        registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) {
            if (it.resultCode == Activity.RESULT_OK) {
                val selectedCountry = it.data!!.getParcelableExtra<CountryUIModel>(SearchCountryActivity.SELECTED_COUNTRY_KEY)!!
                binding.swCountryPicker.setRegion(RegionPickerUIModel(selectedCountry.iso3, selectedCountry.name, selectedCountry.logo))
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityMtnBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupObservers()
        setupView(intent.extras?.containsKey("url") ?: false)
        viewModel.getCountries()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun setupObservers() {
        viewModel.mtnStatus.observe(
            this,
            EventObserver {
                setMtnStatus(it)
            },
        )

        viewModel.error.observe(
            this,
            EventObserver {
                setError(it)
            },
        )

        viewModel.qrError.observe(
            this,
            EventObserver {
                setQRError(it)
            },
        )

        viewModel.loading.observe(
            this,
            EventObserver { isLoading ->
                if (isLoading) binding.loadingView.visible()
                else binding.loadingView.gone()
            },
        )
    }

    private fun setupView(isQrCode: Boolean) {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.let {
            it.setDisplayShowHomeEnabled(true)
            it.setDisplayShowTitleEnabled(true)
            it.setDisplayHomeAsUpEnabled(true)
        }
        with(binding) {
            with(toolbar) {
                setContentInsetsAbsolute(0, contentInsetStartWithNavigation)
                title = if (isQrCode) getString(R.string.track_transaction_title_qr) else getString(R.string.track_transaction_title_mtn)
            }

            if (isQrCode && !intent.extras!!.getString("url").isNullOrEmpty()) {
                viewModel.trackQrTransaction(intent.extras!!.getString("url")!!)
            }

            mtnCode.editText?.let {
                with(it) {
                    inputType = InputType.TYPE_CLASS_NUMBER
                    isFocusableInTouchMode = true
                    isCursorVisible = true
                }
            }

            binding.swCountryPicker.setHint(resources.getString(R.string.mtn_track_number_title_description))
            binding.swCountryPicker.setButtonClickListener {
                pickCountry()
            }
            binding.swCountryPicker.setButtonClickListener {
                pickCountry()
            }
            checkButton.setOnClickListener {
                binding.mtnCode.error = null
                if (!binding.mtnCode.editText?.text?.toString().isNullOrEmpty()) {
                    binding.swCountryPicker.getCountry()?.id?.let { country ->
                        viewModel.getMTNStatus(binding.mtnCode.editText!!.text!!.toString(), country)
                    }
                } else {
                    binding.mtnCode.error = getString(R.string.empty_field)
                    binding.includeFlowContainer.gone()
                    if (::transactionView.isInitialized) {
                        transactionView.cleanError()
                    }
                }
            }
        }
    }

    private fun pickCountry() {
        val intent = Intent(this, SearchCountryActivity::class.java).apply {
            putExtra(
                SearchCountryActivity.TITLE_KEY,
                resources.getString(
                    R.string.sending_from_search_title,
                ),
            )
            putExtra(SearchCountryActivity.TITLE_KEY, resources.getString(R.string.send_money_to))
            val countries = ArrayList(viewModel.countries)
            putParcelableArrayListExtra(SearchCountryActivity.COUNTRIES_KEY, countries)
        }
        startSearch.launch(intent)
    }

    private fun setMtnStatus(mtn: MtnStatusUIModel) {
        binding.flowContainerMtn.visible()
        transactionView = TransactionFlow(binding.flowContainerMtn.root, this)

        if (mtn.isQrMode) {
            setDataFromQr(mtn.country, mtn.mtn)

            binding.includeFlowContainer.visible()
            TransactionFlow(binding.includeFlowContainer.root, this)
        }

        transactionView.addSteps(mtn.statusList)
    }

    private fun setError(error: ErrorType) {
        binding.includeFlowContainer.visible()
        transactionView = TransactionFlow(binding.flowContainerMtn.root, this)
        transactionView.addError(error.message.ifEmpty { getString(R.string.transaction_tracker_mtn_error) })
    }

    private fun setQRError(mtnQrErrorUIModel: MtnQrErrorUIModel) {
        binding.includeFlowContainer.visible()
        transactionView = TransactionFlow(binding.flowContainerMtn.root, this)
        transactionView.addError(mtnQrErrorUIModel.error.message.ifEmpty { getString(R.string.transaction_tracker_mtn_error) })

        setDataFromQr(mtnQrErrorUIModel.country, mtnQrErrorUIModel.mtn)
    }

    private fun setDataFromQr(country: String, mtn: String) {
        val countrySelected = viewModel.countries.first { it.iso3 == country }

        binding.swCountryPicker.setRegion(
            RegionPickerUIModel(
                countrySelected.iso3,
                countrySelected.name,
                countrySelected.logo
            )
        )

        binding.mtnCode.editText?.setText(mtn)
    }
}
