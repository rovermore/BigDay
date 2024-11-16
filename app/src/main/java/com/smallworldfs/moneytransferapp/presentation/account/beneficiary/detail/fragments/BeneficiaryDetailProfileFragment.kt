package com.smallworldfs.moneytransferapp.presentation.account.beneficiary.detail.fragments

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.activityViewModels
import com.smallworldfs.moneytransferapp.R
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEvent
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventName
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventProperty
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.loadCircularImage
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showLoadingDialog
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.showSingleActionErrorDialog
import com.smallworldfs.moneytransferapp.base.presentation.viewmodel.EventObserver
import com.smallworldfs.moneytransferapp.databinding.FragmentBeneficiaryDetailProfileBinding
import com.smallworldfs.moneytransferapp.modules.c2b.presentation.ui.activity.C2BActivity
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.CreateTransactionResponse
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericFragment
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.TransactionalActivity
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.detail.BeneficiaryDetailNavigator
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.detail.BeneficiaryDetailViewModel
import com.smallworldfs.moneytransferapp.utils.Constants
import com.smallworldfs.moneytransferapp.utils.INT_ONE
import com.smallworldfs.moneytransferapp.utils.INT_ZERO
import com.smallworldfs.moneytransferapp.utils.STRING_DASH
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_beneficiary_detail_profile.*
import java.util.Locale
import java.util.regex.Pattern
import javax.inject.Inject

@AndroidEntryPoint
class BeneficiaryDetailProfileFragment : GenericFragment() {

    private val viewModelActivity: BeneficiaryDetailViewModel by activityViewModels()

    @Inject
    lateinit var navigator: BeneficiaryDetailNavigator

    private var _binding: FragmentBeneficiaryDetailProfileBinding? = null
    private val binding get() = _binding!!

    private var mProgressDialog: Dialog? = null

    private var mErrorDialog: Dialog? = null

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
        _binding = FragmentBeneficiaryDetailProfileBinding.inflate(inflater, container, false)
        setupObservers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
        setupButtonListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null

        // Remove dialogs
        mProgressDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
        mErrorDialog?.let {
            if (it.isShowing) {
                it.dismiss()
            }
        }
    }

    private fun setupObservers() {
        viewModelActivity.calculatorUpdated.observe(
            viewLifecycleOwner,
            EventObserver { beneficiary ->
                showLoadingDialog(false)
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
                showErrorDialog(true)
            },
        )

        viewModelActivity.refreshBeneficiary.observe(
            viewLifecycleOwner,
            EventObserver {
                setupView()
            },
        )
    }

    private fun setupView() {
        binding.fragmentBeneficiaryDetailProfileMainContainer.removeAllViews()
        drawProfile()
        configurePersonalInformationBlock()
        configureAddressBlock()
        configureTransactionInfoBlock()
        configureBankDetailsBlock()
        configureAdditionalInfoBlock()
        showErrorDialog(false)
        showLoadingDialog(false)
    }

    private fun drawProfile() {
        val beneficiaryFirstLetter = if (viewModelActivity.beneficiary.fullNameWithSurname.isNotEmpty()) viewModelActivity.beneficiary.fullNameWithSurname.substring(INT_ZERO, INT_ONE).toUpperCase(
            Locale.getDefault()
        ) else STRING_EMPTY
        val beneficiarySecondLetter = getSecondCapitalizedLetterFor()
        if (beneficiaryFirstLetter.isNotEmpty()) {
            binding.userNameLetterText.text = beneficiaryFirstLetter
        } else {
            binding.userNameLetterText.visibility = View.GONE
        }
        if (beneficiarySecondLetter.isNotEmpty()) {
            binding.userNameLetterText2.text = beneficiarySecondLetter
        } else {
            binding.userNameLetterText2.visibility = View.GONE
        }
        if (viewModelActivity.beneficiary.payoutCountry != null && viewModelActivity.beneficiary.payoutCountry.iso3.isNotEmpty()) {
            binding.countryFlag.loadCircularImage(
                requireContext(),
                R.drawable.placeholder_country_adapter,
                Constants.COUNTRY.FLAG_IMAGE_ASSETS + viewModelActivity.beneficiary.payoutCountry.iso3 + Constants.COUNTRY.FLAG_IMAGE_EXTENSION
            )
        }
        if (viewModelActivity.beneficiary.name.isNotEmpty()) {
            binding.beneficiaryNameText.text = viewModelActivity.beneficiary.fullNameWithSurname
        }
        if (viewModelActivity.beneficiary.deliveryMethod.name.isNotEmpty())
            binding.beneficiaryOperationText.text = viewModelActivity.beneficiary.deliveryMethod.name
    }

    private fun getSecondCapitalizedLetterFor(): String {
        if (viewModelActivity.beneficiary.fullNameWithSurname.isNotEmpty()) {
            val matcher = Pattern.compile("\\s([A-Za-z-0-9]+)").matcher(viewModelActivity.beneficiary.fullNameWithSurname)
            if (matcher.find()) {
                return matcher.group(INT_ONE)?.substring(INT_ZERO, INT_ONE)?.toUpperCase(Locale.getDefault()) ?: STRING_EMPTY
            }
        }
        return STRING_EMPTY
    }

    private fun configurePersonalInformationBlock() {
        val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val blockView = inflater.inflate(R.layout.profile_layout_block, null) as RelativeLayout
        val blockTitle = blockView.findViewById<TextView>(R.id.blockTitle)
        blockTitle.text = getString(R.string.beneficiary_profile_personal_info_block_title)
        val mainContainer = blockView.findViewById<LinearLayout>(R.id.mainContainer)
        val topBackground = blockView.findViewById<View>(R.id.top_background)
        topBackground.visibility = View.GONE

        var genericField: LinearLayout? = createGenericFieldLayout(inflater, getString(R.string.beneficiary_profile_nickname_title), viewModelActivity.beneficiary.alias)
        if (genericField != null) {
            mainContainer.addView(genericField)
        }
        genericField = createGenericFieldLayout(inflater, getString(R.string.beneficiary_profile_first_name_title), viewModelActivity.beneficiary.name)
        if (genericField != null) {
            mainContainer.addView(genericField)
        }
        genericField = createGenericFieldLayout(inflater, getString(R.string.beneficiary_profile_last_name_title), viewModelActivity.beneficiary.surname)
        if (genericField != null) {
            mainContainer.addView(genericField)
        }
        genericField = createGenericFieldLayout(inflater, getString(R.string.beneficiary_profile_email_address_title), viewModelActivity.beneficiary.email)
        if (genericField != null) {
            mainContainer.addView(genericField)
        }
        genericField = createGenericFieldLayout(inflater, getString(R.string.beneficiary_profile_mobile_phone_title), viewModelActivity.beneficiary.mobile)
        if (genericField != null) {
            mainContainer.addView(genericField)
        }
        fragmentBeneficiaryDetailProfileMainContainer.addView(blockView)
    }

    private fun configureAddressBlock() {
        val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val blockView = inflater.inflate(R.layout.profile_layout_block, null) as RelativeLayout
        val blockTitle = blockView.findViewById<TextView>(R.id.blockTitle)
        blockTitle.text = getString(R.string.beneficiary_profile_address_block_title)
        val mainContainer = blockView.findViewById<LinearLayout>(R.id.mainContainer)
        val topBackground = blockView.findViewById<View>(R.id.top_background)
        topBackground.visibility = View.GONE

        // Fields
        var genericField: LinearLayout? = createGenericFieldLayout(inflater, getString(R.string.beneficiary_profile_city_title), viewModelActivity.beneficiary.city)
        if (genericField != null) {
            mainContainer.addView(genericField)
        }
        val countryKey = viewModelActivity.beneficiary.payoutCountry.iso3
        val countryUrl = Constants.COUNTRY.FLAG_IMAGE_ASSETS + countryKey + Constants.COUNTRY.FLAG_IMAGE_EXTENSION
        genericField = createGenericFieldLayout(inflater, getString(R.string.beneficiary_profile_country_title), if (viewModelActivity.beneficiary.country.isNullOrEmpty()) STRING_DASH else viewModelActivity.beneficiary.country, countryUrl)
        if (genericField != null) {
            mainContainer.addView(genericField)
        }
        genericField = createGenericFieldLayout(inflater, getString(R.string.beneficiary_profile_address_title), viewModelActivity.beneficiary.address)
        if (genericField != null) {
            mainContainer.addView(genericField)
        }
        genericField = createGenericFieldLayout(inflater, getString(R.string.beneficiary_profile_postal_code_title), viewModelActivity.beneficiary.zip)
        if (genericField != null) {
            mainContainer.addView(genericField)
        }
        genericField = createGenericFieldLayout(inflater, getString(R.string.beneficiary_profile_state_province_region_title), viewModelActivity.beneficiary.state)
        if (genericField != null) {
            mainContainer.addView(genericField)
        }
        fragmentBeneficiaryDetailProfileMainContainer.addView(blockView)
    }

    private fun configureTransactionInfoBlock() {
        val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val blockView = inflater.inflate(R.layout.profile_layout_block, null) as RelativeLayout
        val blockTitle = blockView.findViewById<TextView>(R.id.blockTitle)
        blockTitle.text = getString(R.string.beneficiary_profile_transaction_info_block_title)
        val mainContainer = blockView.findViewById<LinearLayout>(R.id.mainContainer)
        val topBackground = blockView.findViewById<View>(R.id.top_background)
        topBackground.visibility = View.GONE

        // Fields
        var deliveryMethod: String? = STRING_EMPTY
        if (viewModelActivity.beneficiary.deliveryMethod.name.isNotEmpty()) {
            deliveryMethod = viewModelActivity.beneficiary.deliveryMethod.name
        }
        var genericField: LinearLayout? = createGenericFieldLayout(inflater, getString(R.string.beneficiary_profile_delivery_method_title), deliveryMethod)
        if (genericField != null) {
            mainContainer.addView(genericField)
        }
        var payoutCurrency: String? = STRING_EMPTY
        if (viewModelActivity.beneficiary.payoutCurrency.isNotEmpty()) {
            payoutCurrency = viewModelActivity.beneficiary.payoutCurrency
        }
        genericField = createGenericFieldLayout(inflater, getString(R.string.beneficiary_profile_currency_title), payoutCurrency)
        if (genericField != null) {
            mainContainer.addView(genericField)
        }
        fragmentBeneficiaryDetailProfileMainContainer.addView(blockView)
    }

    private fun configureBankDetailsBlock() {
        val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val blockView = inflater.inflate(R.layout.profile_layout_block, null) as RelativeLayout
        val blockTitle = blockView.findViewById<TextView>(R.id.blockTitle)
        blockTitle.text = getString(R.string.beneficiary_profile_bank_details_block_title)
        val mainContainer = blockView.findViewById<LinearLayout>(R.id.mainContainer)
        val topBackground = blockView.findViewById<View>(R.id.top_background)
        topBackground.visibility = View.GONE

        // Fields
        var genericField: LinearLayout? = createGenericFieldLayout(inflater, getString(R.string.beneficiary_profile_bank_title), viewModelActivity.beneficiary.bankName)
        if (genericField != null) {
            mainContainer.addView(genericField)
        }
        var bankAccountType: String? = STRING_DASH
        if (viewModelActivity.beneficiary.bankAccountType.isNotEmpty()) {
            bankAccountType = viewModelActivity.beneficiary.bankAccountType
        }
        genericField = createGenericFieldLayout(inflater, getString(R.string.beneficiary_profile_account_type_title), bankAccountType)
        if (genericField != null) {
            mainContainer.addView(genericField)
        }
        genericField = createGenericFieldLayout(inflater, getString(R.string.beneficiary_profile_account_number_title), viewModelActivity.beneficiary.bankAccountNumber)
        if (genericField != null) {
            mainContainer.addView(genericField)
        }
        if (viewModelActivity.beneficiary.bankName.isNotEmpty() || viewModelActivity.beneficiary.bankAccountNumber.isNotEmpty()) {
            fragmentBeneficiaryDetailProfileMainContainer.addView(blockView)
        }
    }

    private fun configureAdditionalInfoBlock() {
        val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val blockView = inflater.inflate(R.layout.profile_layout_block, null) as RelativeLayout
        val blockTitle = blockView.findViewById<TextView>(R.id.blockTitle)
        blockTitle.text = getString(R.string.beneficiary_profile_additional_info_block_title)
        val mainContainer = blockView.findViewById<LinearLayout>(R.id.mainContainer)
        val topBackground = blockView.findViewById<View>(R.id.top_background)
        topBackground.visibility = View.GONE

        // Fields
        var genericField: LinearLayout? = createGenericFieldLayout(inflater, getString(R.string.beneficiary_profile_document_type_title), viewModelActivity.beneficiary.document)
        if (genericField != null) {
            mainContainer.addView(genericField)
        }
        genericField = createGenericFieldLayout(inflater, getString(R.string.beneficiary_profile_id_number_title), viewModelActivity.beneficiary.numberDocument)
        if (genericField != null) {
            mainContainer.addView(genericField)
        }
        genericField = createGenericFieldLayout(inflater, getString(R.string.beneficiary_profile_issuing_country_title), viewModelActivity.beneficiary.countryDocument)
        if (genericField != null) {
            mainContainer.addView(genericField)
        }
        fragmentBeneficiaryDetailProfileMainContainer.addView(blockView)
    }

    private fun createGenericFieldLayout(inflater: LayoutInflater, preTitle: String? = STRING_EMPTY, title: String? = STRING_EMPTY): LinearLayout? {
        return createGenericFieldLayout(inflater, if (preTitle.isNullOrEmpty()) STRING_EMPTY else preTitle, if (title.isNullOrEmpty()) STRING_DASH else title, null)
    }

    private fun createGenericFieldLayout(inflater: LayoutInflater, preTitle: String, title: String, countryUrl: String?): LinearLayout {
        val genericField = inflater.inflate(R.layout.profile_layout_generic_field, null) as LinearLayout
        genericField.findViewById<TextView>(R.id.pre_title).text = preTitle
        genericField.findViewById<TextView>(R.id.title).text = title
        if (countryUrl != null) {
            val image = genericField.findViewById<ImageView>(R.id.image)
            image.loadCircularImage(
                requireContext(),
                R.drawable.placeholder_country_adapter,
                countryUrl
            )
        }
        return genericField
    }

    /**
     * Setup UI
     */
    private fun setupButtonListeners() {
        binding.sendMoneyButton.setOnClickListener {
            showLoadingDialog(true)
            viewModelActivity.updateCalculatorWithBeneficiary()
        }
    }

    private fun showErrorDialog(show: Boolean) {
        if (show) {
            showLoadingDialog(false)
            mErrorDialog = (activity as AppCompatActivity).showSingleActionErrorDialog(getString(R.string.generic_title_error), getString(R.string.generic_subtitle_error), null)
        } else {
            mErrorDialog?.let {
                if (it.isShowing) {
                    it.dismiss()
                }
            }
        }
    }

    private fun showLoadingDialog(show: Boolean) {
        if (show) {
            showErrorDialog(false)
            if (mProgressDialog == null && activity != null && context != null) {
                mProgressDialog = (activity as AppCompatActivity).showLoadingDialog(getString(R.string.progress_dialog_transactional_content), getString(R.string.loading_text), true)
            }
        } else {
            mProgressDialog?.let {
                if (it.isShowing) {
                    it.dismiss()
                }
            }
        }
    }
}
