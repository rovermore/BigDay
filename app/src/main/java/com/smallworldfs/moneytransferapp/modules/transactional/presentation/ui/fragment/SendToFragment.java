package com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.fragment;

import static com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity.PROMO_REQUEST_CODE;
import static com.smallworldfs.moneytransferapp.presentation.common.countries.SearchCountryActivity.SELECTED_COUNTRY_KEY;
import static com.smallworldfs.moneytransferapp.utils.Constants.CALCULATOR.CURRENCY_TYPE_PAYOUT_PRINCIPAL;
import static com.smallworldfs.moneytransferapp.utils.Constants.CALCULATOR.EUR_CURRENCY;
import static com.smallworldfs.moneytransferapp.utils.Constants.CALCULATOR.TOTALSALE;
import static com.smallworldfs.moneytransferapp.utils.Constants.COUNTRY.NDL_COUNTRY_VALUE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEvent;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventName;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventProperty;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventType;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.DialogExt;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.ImageViewExtKt;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.interactor.implementation.CalculatorInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.CalculatorData;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.Taxes;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericFragment;
import com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity;
import com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivityCountrySelectionCallback;
import com.smallworldfs.moneytransferapp.modules.home.presentation.ui.adapter.BeneficiaryHorizontalAdapter;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.CalculatorPromotion;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.Promotion;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter.SendToPresenter;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter.implementation.SendToPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.adapter.CoinSelectorAdapter;
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.model.BeneficiaryUIModel;
import com.smallworldfs.moneytransferapp.presentation.common.countries.CountryUIModel;
import com.smallworldfs.moneytransferapp.presentation.common.countries.SearchCountryActivity;
import com.smallworldfs.moneytransferapp.utils.AmountFormatter;
import com.smallworldfs.moneytransferapp.utils.AnimationUtils;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.KeyboardUtils;
import com.smallworldfs.moneytransferapp.utils.Log;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import org.greenrobot.eventbus.EventBus;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SendToFragment extends GenericFragment implements SendToPresenter.View, View.OnFocusChangeListener {

    public static final String ORIGIN_COUNTRY = "ORIGIN_COUNTRY";
    public static final String ORIGIN_COUNTRY_ISO = "ORIGIN_COUNTRY_ISO";
    public static final String DESTINATION_COUNTRY = "DESTINATION_COUNTRY";
    public static final String DESTINATION_COUNTRY_ISO = "DESTINATION_COUNTRY_ISO";
    public static final String LIMITED_USER_KEY = "LIMITED_USER_KEY";
    public static final String SCREEN_NAME = "SEND_TO_FRAGMENT";
    private static final String TAG = "SendToFragment";
    Unbinder mUnbinder;
    @BindView(R.id.root_view)
    RelativeLayout mRootView;
    @BindView(R.id.they_receive_edittext)
    EditText mTheyReceiveEditText;
    @BindView(R.id.you_pay_edittext)
    EditText mYouPayEditText;
    @BindView(R.id.coin_selector_container)
    CardView mCoinSelectorContainer;
    @BindView(R.id.expand_arrow)
    View mExpandoCoinArrow;
    @BindView(R.id.beneficiary_recyclerview)
    RecyclerView mBeneficiaryRecycler;
    @BindView(R.id.coin_selector_recyclerview)
    RecyclerView mCoinSelectorRecyclerView;
    @BindView(R.id.error_view)
    View mErrorView;
    @BindView(R.id.country_selector)
    View mCountrySelectorContainer;
    @BindView(R.id.beneficiary_recycler_container)
    View mBeneficiariesContainer;
    @BindView(R.id.loading_general_view)
    View mLoadingGeneralView;
    @BindView(R.id.coin_indicator)
    StyledTextView mCoinTextIndicator;
    @BindView(R.id.coin_beneficiary_receive_button)
    View mCoinButtonContainer;
    @BindView(R.id.rate_value_text)
    StyledTextView mRateValueText;
    @BindView(R.id.total_fee_text)
    StyledTextView mTotalFee;
    @BindView(R.id.total_iof_text)
    StyledTextView mTotalIof;
    @BindView(R.id.you_pay_label)
    StyledTextView mYouPayLabel;
    @BindView(R.id.taxes_container)
    RelativeLayout taxesContainer;
    @BindView(R.id.calculator_view)
    RelativeLayout mCalculatorView;
    @BindView(R.id.continue_button)
    View mContinueButton;
    @BindView(R.id.general_calculator_loading_view)
    RelativeLayout mCalculatorGeneralLoadingView;
    @BindView(R.id.you_pay_error_view)
    View mYouPayErrorView;
    @BindView(R.id.currency_origin_text)
    StyledTextView mCurrencySendingText;
    @BindView(R.id.promotional_code_text)
    StyledTextView mPromotionName;
    @BindView(R.id.promotion_arrow_button)
    View mPromotionArrowButton;
    @BindView(R.id.promotional_code_amount)
    StyledTextView mPromotionAmount;
    @BindView(R.id.select_promo_code_button)
    View mSelectPromoContainer;
    @BindView(R.id.calculator_error_view)
    View mCalculatorGeneralErrorView;
    @BindView(R.id.send_to_error_view)
    View mGlobalSendToErrorView;
    @BindView(R.id.beneficiary_receive_textview)
    StyledTextView mBeneficiaryReceiveTextView;
    @BindView(R.id.beneficiary_indicator_loading_view)
    View mBeneficiaryReceivesLoadingView;
    @BindView(R.id.you_pay_loading_view)
    View mYouPayContainerLoadingView;
    @BindView(R.id.country_image)
    ImageView mCountryFlag;
    @BindView(R.id.country_text)
    StyledTextView mCountryText;
    @Nullable
    @BindView(R.id.country_button_picker)
    View mCountryButtonPicker;
    @Nullable
    @BindView(R.id.country_selector_view)
    View mLimitedCountrySelector;
    @BindView(R.id.origin_country_text)
    StyledTextView mCountryOriginText;
    @BindView(R.id.destination_country_text)
    StyledTextView mDestinationCountryText;
    @BindView(R.id.origin_country_image)
    ImageView mOriginCountryImage;
    @BindView(R.id.destination_country_image)
    ImageView mDestinationCountryImage;
    @BindView(R.id.continue_button_layout)
    LinearLayout mContinueButtonLayout;
    boolean blockedFormatting = false;
    boolean isBeneficiaryAmountCalculated = false;
    boolean isYouPayAmountCalculated = false;
    HomeActivityCountrySelectionCallback homeActivityCountrySelectionCallback = new HomeActivityCountrySelectionCallback() {
        @Override
        public void originSelection(@NonNull CountryUIModel countryUIModel) {
            mCountryOriginText.setText(countryUIModel.getName());
            ImageViewExtKt.loadImage(
                    mOriginCountryImage,
                    countryUIModel.getLogo()
            );
        }

        @Override
        public void destinationSelection(@NonNull CountryUIModel countryUIModel) {
            mDestinationCountryText.setText(countryUIModel.getName());
            ImageViewExtKt.loadImage(
                    mDestinationCountryImage,
                    countryUIModel.getLogo()
            );
        }
    };
    private SendToPresenterImpl mPresenter;
    ActivityResultLauncher<Intent> searchCountry = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        // Here, no request code
                        Intent data = result.getData();
                        if (data != null) {
                            CountryUIModel country = data.getParcelableExtra(SELECTED_COUNTRY_KEY);
                            if (country != null) mPresenter.onSelectCountryPayOutFromPopupDialog(new Pair<>(country.getIso3(), country.getName()));
                        }
                    }
                }
            });
    /**
     * Edit Text Listeners
     */
    private final TextWatcher mBeneficiaryReceiveListener = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence text, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!blockedFormatting && !mPresenter.isBlockedBeneficiaryReceive()) {
                String twoDecimalsNumber = AmountFormatter.typedMoreThanTwoDecimals(editable.toString());
                String formattedNumber = AmountFormatter.formatStringNumber(twoDecimalsNumber);

                blockedFormatting = true;

                mTheyReceiveEditText.setText(formattedNumber);
                mTheyReceiveEditText.setSelection(mTheyReceiveEditText.getText().length());

                blockedFormatting = false;

                mPresenter.onAmmountReadyToCalculate(formattedNumber, CURRENCY_TYPE_PAYOUT_PRINCIPAL);
            } else {
                Log.d(TAG, "Blocked beneficiary, no filter");
            }
        }
    };
    TextWatcher mYouPayListener = new TextWatcher() {
        boolean blockedFormatting = false;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence text, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable editable) {
            if (!blockedFormatting && !mPresenter.isBlockedYouPay()) {
                String twoDecimalsNumber = AmountFormatter.typedMoreThanTwoDecimals(editable.toString());
                String formattedNumber = AmountFormatter.formatStringNumber(twoDecimalsNumber);
                blockedFormatting = true;

                mYouPayEditText.setText(formattedNumber);
                mYouPayEditText.setSelection(mYouPayEditText.getText().length());

                blockedFormatting = false;
                mPresenter.onAmmountReadyToCalculate(formattedNumber, TOTALSALE);
            } else {
                Log.d(TAG, "Blocked you pay, no filter");
            }
        }
    };
    private RecyclerView.OnItemTouchListener mListener = null;
    private BeneficiaryHorizontalAdapter mBeneficiaryAdapter;
    private CoinSelectorAdapter mCoinSelectorAdapter;
    private android.app.Dialog mProgressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_send_to, null, false);
        mUnbinder = ButterKnife.bind(this, view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPresenter = new SendToPresenterImpl(AndroidSchedulers.mainThread(), Schedulers.io(), getActivity(), this, getActivity());
        mPresenter.create();

    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.resume();
    }

    @Override
    public void onPause() {
        super.onPause();
        EventBus.getDefault().unregister(this);
        mPresenter.pause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        mPresenter.destroy();
        mUnbinder.unbind();
    }

    @Override
    public void configureView() {
        if (getArguments() != null && getArguments().getBoolean(LIMITED_USER_KEY)) {
            setLimitedCountrySelector();
            if (mPresenter.showWelcome()) {
                this.mProgressDialog = new DialogExt().showWelcomeTipDialog(
                        requireContext(),
                        this::navigateToSendFromScreen,
                        this::onGotItPressed,
                        getArguments().getString(ORIGIN_COUNTRY),
                        getArguments().getString(ORIGIN_COUNTRY_ISO),
                        getArguments().getString(DESTINATION_COUNTRY),
                        getArguments().getString(DESTINATION_COUNTRY_ISO)
                );
            }
        }
        //mMainCalculatorContainer.setOnTouchListener(outListener);
        Typeface type = ResourcesCompat.getFont(requireActivity(), R.font.nunito_black);
        mTheyReceiveEditText.setTypeface(type);
        mYouPayEditText.setTypeface(type);
        mBeneficiaryRecycler.setNestedScrollingEnabled(false);

        // Show calculator general loading view
        showTotalCalculatorLoadingView();

        // Header
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mBeneficiaryRecycler.setLayoutManager(mLinearLayoutManager);
        mBeneficiaryRecycler.addOnItemTouchListener(mListener);
        mBeneficiaryRecycler.setHasFixedSize(true);
        mBeneficiaryAdapter = new BeneficiaryHorizontalAdapter(getActivity());
        mBeneficiaryRecycler.setAdapter(mBeneficiaryAdapter);
        mBeneficiaryAdapter.setClickBeneficiaryListener(new BeneficiaryHorizontalAdapter.ClickBeneficiaryListener() {
            @Override
            public void onBeneficiaryClick(int position, BeneficiaryUIModel beneficiary) {
                mBeneficiaryAdapter.updateBeneficiarySelected(position);
                mPresenter.onSelectBeneficiaryInList(beneficiary);
            }

            @Override
            public void onNewBeneficiaryClick(BeneficiaryUIModel beneficiary) {
                TreeMap<String, String> properties = new TreeMap<>();
                properties.put(BrazeEventProperty.SCREEN_TYPE.getValue(), "Homepage");
                registerBrazeEvent(
                        BrazeEventName.BENEFICIARY_CREATION_STEP_1.getValue(),
                        properties
                );
                mPresenter.onNewBeneficiaryActionSelected();
            }
        });

        // Coin Selector
        LinearLayoutManager mCoinSelectorLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mCoinSelectorRecyclerView.setLayoutManager(mCoinSelectorLayoutManager);
        mCoinSelectorAdapter = new CoinSelectorAdapter();
        mCoinSelectorRecyclerView.setAdapter(mCoinSelectorAdapter);

        mCoinSelectorAdapter.setOnSelectCoinListener(currency -> {
            registerEvent("click_select_currency", currency.first, "", "");
            mPresenter.onSelectCurrencyInCalculator(currency);
            updateCurrencyTextIndicator(currency.first);
            onCoinBeneficiaryReceiveButtonClick();
            hideErrorView();
        });


        mTheyReceiveEditText.addTextChangedListener(mBeneficiaryReceiveListener);
        mYouPayEditText.addTextChangedListener(mYouPayListener);

        // Set default value
        mPresenter.setBlockedBeneficiaryEditText(true);
        mPresenter.setBlockedYouPayEditText(true);
        String initYouPayAmount = TextUtils.isEmpty(mPresenter.getCurrentAmountInScreen()) ?
                Constants.CALCULATOR.DEFAULT_AMOUNT : mPresenter.getCurrentAmountInScreen();
        String quantity = AmountFormatter.normalizeDoubleString(initYouPayAmount);
        mYouPayEditText.setText(quantity);

        mTheyReceiveEditText.setHint(AmountFormatter.formatStringNumber(getString(R.string.hint_beneficiary_edit_text)));
        mYouPayEditText.setHint(AmountFormatter.formatStringNumber(getString(R.string.hint_you_pay_edit_text)));

        mPresenter.setBlockedBeneficiaryEditText(false);
        mPresenter.setBlockedYouPayEditText(false);

        // Add focus change listener
        mTheyReceiveEditText.setOnFocusChangeListener(this);
        mYouPayEditText.setOnFocusChangeListener(this);
    }

    private void setLimitedCountrySelector() {
        mContinueButtonLayout.setVisibility(View.GONE);
        mCountryFlag.setVisibility(View.GONE);
        mCountryText.setVisibility(View.GONE);
        if (mCountryButtonPicker != null) mCountryButtonPicker.setVisibility(View.GONE);
        if (mLimitedCountrySelector != null) mLimitedCountrySelector.setVisibility(View.VISIBLE);
        if (getArguments() != null) {
            mCountryOriginText.setText(getArguments().getString(ORIGIN_COUNTRY));

            mDestinationCountryText.setText(getArguments().getString(DESTINATION_COUNTRY));

            ImageViewExtKt.loadImage(
                    mOriginCountryImage,
                    Constants.COUNTRY.FLAG_IMAGE_ASSETS + getArguments().getString(ORIGIN_COUNTRY_ISO) + Constants.COUNTRY.FLAG_IMAGE_EXTENSION
            );

            ImageViewExtKt.loadImage(
                    mDestinationCountryImage,
                    Constants.COUNTRY.FLAG_IMAGE_ASSETS + getArguments().getString(DESTINATION_COUNTRY_ISO) + Constants.COUNTRY.FLAG_IMAGE_EXTENSION);
        }
    }

    public void navigateToSendFromScreen() {
        mPresenter.navigateToCountrySelector();
    }

    public void onGotItPressed() {
        mPresenter.setWelcomeShown(true);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (!hasFocus) {
            if (v instanceof EditText)
                mPresenter.normalizeValue(v.getId(), ((EditText) v).getText().toString());

            hideKeyboard(v);
        }
    }

    public void updateCurrencyTextIndicator(String currencySelected) {
        mCoinTextIndicator.setVisibility(View.VISIBLE);
        mCoinTextIndicator.setText(currencySelected);
    }

    public void showSelectorCoinRecycler() {
        registerEvent("click_expand_currency", "", "", "");
        mCoinSelectorContainer.setVisibility(View.VISIBLE);
        AnimationUtils.rotateView(mExpandoCoinArrow, 300, 0);
    }

    public void hideSelectorCoinRecycler() {
        registerEvent("click_collapse_currency", "", "", "");
        mCoinSelectorContainer.setVisibility(View.GONE);
        AnimationUtils.rotateView(mExpandoCoinArrow, 300, 180);
    }

    public void resetBehavior() {
        if (mPresenter != null) {
            mPresenter.resetBehavior();
        }
    }

    private void enableDisablePromotionsButton(boolean enable) {
        mSelectPromoContainer.setClickable(enable);
        if (enable) {
            TypedValue outValue = new TypedValue();
            requireActivity().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            mSelectPromoContainer.setBackgroundResource(outValue.resourceId);
            mPromotionArrowButton.setVisibility(View.VISIBLE);
        } else {
            mSelectPromoContainer.setBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.white));
            mPromotionArrowButton.setVisibility(View.GONE);
        }
    }

    /**
     * Presenter View Callbacks
     */

    @Override
    public void showCountryBeneficiaryPopup(Pair<String, String> originCountry) {
        if (mCoinSelectorContainer.getVisibility() == View.VISIBLE)
            hideSelectorCoinRecycler();

        Intent intent = new Intent(getContext(), SearchCountryActivity.class);
        intent.putExtra(SearchCountryActivity.TITLE_KEY, getString(R.string.sending_to_title));
        intent.putExtra(SearchCountryActivity.TYPE, "SENDING_MONEY_TO");
        searchCountry.launch(intent);
    }


    //----------
    // HEADER
    //----------

    @Override
    public void configureBeneficiariesListHeader(ArrayList<BeneficiaryUIModel> listData, BeneficiaryUIModel beneficiarySelected) {
        // Add First Item Such Create Transaction
        if (mBeneficiaryAdapter != null) {
            mBeneficiaryAdapter.updateData(listData);
            mBeneficiaryAdapter.updateBeneficiarySelectedWithBeneficiary(beneficiarySelected);
            if (mPresenter.getBeneficiarySelected() == null)
                mPresenter.onSelectBeneficiaryInList(listData.get(0));
        }
    }

    @Override
    public void configureCountrySelectorHeader(Pair<String, String> country) {
        ImageViewExtKt.loadCircularImage(
                mCountryFlag,
                requireContext(),
                R.drawable.placeholder_country_adapter,
                Constants.COUNTRY.FLAG_IMAGE_ASSETS + country.first + Constants.COUNTRY.FLAG_IMAGE_EXTENSION
        );

        mPresenter.setmBeneficiaryCountry(country);
        mCountryText.setText(country.second);
    }

    @Override
    public void showListBeneficiaries() {
        mCountrySelectorContainer.setVisibility(View.GONE);
        mBeneficiariesContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showCountrySelector() {
        mBeneficiariesContainer.setVisibility(View.GONE);
        mCountrySelectorContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showGeneralLoadingView() {
        mLoadingGeneralView.setVisibility(View.VISIBLE);
    }


    @Override
    public void hideGeneralLoadingView() {
        mLoadingGeneralView.setVisibility(View.GONE);
    }

    @Override
    public void showErrorView(String text, String subtitle) {
        showErrorView(text, subtitle, mErrorView, true);
    }

    @Override
    public void hideErrorView() {
        hideErrorView(mErrorView);
    }

    @Override
    public void showTotalCalculatorLoadingView() {
        showBeneficiaryCalculatorLoadingView();
        showYouPayCalculatorLoadingView();

        mCoinTextIndicator.setVisibility(View.GONE);
        mExpandoCoinArrow.setVisibility(View.GONE);
    }

    @Override
    public void showBeneficiaryCalculatorLoadingView() {
        mBeneficiaryReceivesLoadingView.setVisibility(View.VISIBLE);
        isBeneficiaryAmountCalculated = false;
    }

    @Override
    public void hideBeneficiaryCalculatorLoadingView() {
        mBeneficiaryReceivesLoadingView.setVisibility(View.GONE);
        isBeneficiaryAmountCalculated = true;
    }

    @Override
    public void showYouPayCalculatorLoadingView() {
        mYouPayContainerLoadingView.setVisibility(View.VISIBLE);
        isYouPayAmountCalculated = false;
    }

    @Override
    public void hideYouPayCalculatorLoadingView() {
        mYouPayContainerLoadingView.setVisibility(View.INVISIBLE);
        isYouPayAmountCalculated = true;
    }

    private boolean shouldEnableContinueButton() {
        return isBeneficiaryAmountCalculated && isYouPayAmountCalculated;
    }

    @Override
    public void updateTheyReceiveEditText(String formattedAmount) {
        mTheyReceiveEditText.setText(formattedAmount);
    }

    @Override
    public void updateYouPayEditText(String formattedAmount) {
        mYouPayEditText.setText(formattedAmount);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void updateIofAmount(String formattedIofAmount, String sendingCurrency, Taxes taxes) {
        mTotalIof.setText(taxes.getTaxCode() + " (" + taxes.getPercentage() + "%" + ") " + formattedIofAmount + " " + sendingCurrency);
    }

    @Override
    public void updateBeneficiaryReceiveInfo(ArrayList<Pair<String, String>> payoutCurrencies, String currencySelected) {
        if (payoutCurrencies != null && payoutCurrencies.size() > 0) {

            updateCurrencyTextIndicator(currencySelected);

            mCoinSelectorAdapter.updateData(payoutCurrencies);
            mExpandoCoinArrow.setVisibility(View.VISIBLE);
        }

        // First disable view
        mCalculatorGeneralLoadingView.setVisibility(View.INVISIBLE);
    }

    @SuppressLint({"RestrictedApi", "SetTextI18n"})
    @Override
    public void updateBottomRateIndicators(double rate, double totalFee, Taxes taxes, String sendingCurrency) {
        mRateValueText.setText(String.format(SmallWorldApplication.getStr(R.string.rate_text_operation), AmountFormatter.formatDoubleRateNumber(rate)));
        mTotalFee.setText(String.format(SmallWorldApplication.getStr(R.string.total_fee_text_operation), AmountFormatter.formatDoubleAmountNumber(totalFee), sendingCurrency));
        mYouPayLabel.setText(getString(R.string.you_pay_calculator_label));
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mTotalFee.getLayoutParams();
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        mTotalFee.setLayoutParams(params);
        params = (RelativeLayout.LayoutParams) mRateValueText.getLayoutParams();
        params.addRule(RelativeLayout.CENTER_VERTICAL);
        mRateValueText.setLayoutParams(params);
        mPresenter.setTaxes(taxes);
        if (taxes != null && taxes.getTaxAmount() != null && !taxes.getTaxAmount().isEmpty() &&
                taxes.getTaxCode() != null && !taxes.getTaxCode().isEmpty()) {
            mTotalIof.setVisibility(View.VISIBLE);
            if (taxesContainer.getHeight() >= mTotalFee.getHeight() * 2.2) {
                params = (RelativeLayout.LayoutParams) mTotalFee.getLayoutParams();
                params.removeRule(RelativeLayout.CENTER_VERTICAL);
                mTotalFee.setLayoutParams(params);
                params = (RelativeLayout.LayoutParams) mRateValueText.getLayoutParams();
                params.removeRule(RelativeLayout.CENTER_VERTICAL);
                mRateValueText.setLayoutParams(params);
                mTotalIof.setText(taxes.getTaxCode() + " (" + taxes.getPercentage() + "%" + ") " + taxes.getFormatedTaxAmount() + " " + sendingCurrency);
            } else {
                String youPayLabel = mYouPayLabel.getText().toString().replace(")", "");
                youPayLabel += " " + (taxes.getTaxCode() + " " + taxes.getPercentage() + "%" + ")"); // + taxes.getTaxAmount() + " " + sendingCurrency);
                mYouPayLabel.setAutoSizeTextTypeUniformWithConfiguration(1, 18, 1, TypedValue.COMPLEX_UNIT_SP);
                mYouPayLabel.setText(youPayLabel);
            }
        } else {
            mTotalIof.setVisibility(View.GONE);
        }
        mCurrencySendingText.setText(sendingCurrency);
    }

    @Override
    public void hideGlobalErrorEmptyView() {
        mGlobalSendToErrorView.setVisibility(View.GONE);
    }

    @Override
    public void showGlobalErrorEmptyView() {
        mGlobalSendToErrorView.setVisibility(View.VISIBLE);
    }


    @Override
    public void showLocalCalculatorError() {
        // First disable view
        mCalculatorGeneralLoadingView.setVisibility(View.INVISIBLE);
        mCalculatorGeneralErrorView.setVisibility(View.VISIBLE);
        mContinueButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideLocalCalculatorError() {
        mCalculatorGeneralErrorView.setVisibility(View.GONE);
        mContinueButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void showYouPayCalculatorViewError() {
        mYouPayErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideYouPayCalculatorViewError() {
        mYouPayErrorView.setVisibility(View.GONE);
    }

    //--------------
    // PROMOTIONS
    //--------------

    @Override
    public void drawCalculatorPromotion(CalculatorPromotion calculatorPromotion) {
        mPromotionName.setText(calculatorPromotion.getPromotionName());
        mPromotionName.setTextColor(requireActivity().getResources().getColor(R.color.main_blue));
        mPromotionAmount.setText(calculatorPromotion.getStringPromotinoDescription(requireActivity(), mPresenter.getCurrentCurrencyCoin()));
        mSelectPromoContainer.setClickable(!calculatorPromotion.isAutoAsigned());
        enableDisablePromotionsButton(!calculatorPromotion.isAutoAsigned());
    }

    @Override
    public void drawPlaceHolderPromotions(boolean notifyAvailablePromotions) {
        mPromotionName.setText(getString(notifyAvailablePromotions ? R.string.promotion_text_available : R.string.promotion_text_placeholder));
        mPromotionName.setTextColor(requireActivity().getResources().getColor(notifyAvailablePromotions ? R.color.main_blue : R.color.default_text_color));
        mPromotionAmount.setText(getString(notifyAvailablePromotions ? R.string.check_it_out_promo_code : R.string.add_it_promo_code));
        enableDisablePromotionsButton(true);
    }

    @Override
    public void drawUserPromotion(Promotion promotion) {
        mPromotionName.setText(String.format(getString(R.string.promotion_text), promotion.getPromotionCode()));
        mPromotionName.setTextColor(requireActivity().getResources().getColor(R.color.main_blue));
        mPromotionAmount.setText(promotion.getStringPromotinoDescription(requireActivity(), mPresenter.getCurrentCurrencyCoin()));
        enableDisablePromotionsButton(true);
    }

    @Override
    public void resetPromotionsView() {
        mPromotionName.setText("");
        mPromotionArrowButton.setVisibility(View.INVISIBLE);
        mPromotionAmount.setText("");
        mSelectPromoContainer.setClickable(false);
    }

    @Override
    public void updateAmountsFromPassiveMode(String beneficiaryReceiveAmount, String youPayAmount, String currencyPayout) {
        mTheyReceiveEditText.setText(beneficiaryReceiveAmount);
        mYouPayEditText.setText(youPayAmount);
        mCoinTextIndicator.setText(currencyPayout);
    }

    @Override
    public void resetBeneficiaryAdapterValues() {
        if (mBeneficiaryAdapter != null) {
            mBeneficiaryAdapter.clearSelection();
        }
    }

    @Override
    public void changeReceivesLabel(String name) {
        if (!TextUtils.isEmpty(name)) {
            mBeneficiaryReceiveTextView.setText(String.format(getString(R.string.beneficiary_receive_name), name));
        } else {
            mBeneficiaryReceiveTextView.setText(getString(R.string.beneficiary_receive));
        }
    }

    @Override
    public void updateSelectedBeneficiary(BeneficiaryUIModel beneficiary) {
        mBeneficiaryAdapter.updateBeneficiarySelectedWithBeneficiary(beneficiary);
    }

    @Override
    public void showProgressDialog(boolean show, String title, String content, boolean showTitle) {
        if (show && this.mProgressDialog == null && getActivity() != null && !getActivity().isDestroyed() && !getActivity().isFinishing() && getContext() != null) {
            this.mProgressDialog = new DialogExt().showLoadingDialog(getContext(), title, content, showTitle);
        } else if (!show && this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
            this.mProgressDialog = null;
        }
    }

    @Override
    public void enableYouPayReceiveTextWatcher() {
        mPresenter.setBlockedYouPayEditText(false);
    }

    @Override
    public void disableYouPayReceiveTextWatcher() {
        mPresenter.setBlockedYouPayEditText(true);
    }

    /**
     * On Clicks
     */
    @OnClick(R.id.select_country_button)
    public void onClickSelectCountryPopup() {
        registerEvent("search_send_money_to", "", "search", ScreenName.SEND_MONEY_TO_SCREEN.getValue());
        mPresenter.onSelectCountryPopupClick();
    }

    @OnClick(R.id.coin_beneficiary_receive_button)
    public void onCoinBeneficiaryReceiveButtonClick() {
        KeyboardUtils.hideKeyboard(getActivity(), requireActivity().getCurrentFocus());
        if (mCoinSelectorAdapter != null && mCoinSelectorAdapter.getItemCount() > 0) {
            if (mCoinSelectorContainer.getVisibility() == View.VISIBLE) {
                hideSelectorCoinRecycler();
            } else {
                showSelectorCoinRecycler();
            }
        }
    }

    @OnClick(R.id.continue_button)
    public void onContinueButtonClick() {
        if (shouldEnableContinueButton()) {
            // If the user country is NDL check if the quantity is more tha 2000 thousand and the currency EUR
            CalculatorData calculatorData = CalculatorInteractorImpl.getInstance().getCalculatorData();

            registerEvent("click_continue", calculatorData.getSendingCurrency().first, "", "");

            HashMap<String, String> properties = new HashMap<>();
            properties.put(BrazeEventProperty.DESTINATION_COUNTRY.getValue(), calculatorData.getPayoutCountry().first);
            properties.put(BrazeEventProperty.DELIVERY_METHOD.getValue(), calculatorData.getDeliveryMethod().first);
            registerBrazeEvent(BrazeEventName.TRANSACTION_CREATION_STEP_1.getValue(), properties);


            // Amount (string)
            String amountString = calculatorData.getYouPay();

            // Amount double
            String DECIMAL_SEPARATOR = String.valueOf(new DecimalFormat().getDecimalFormatSymbols().getDecimalSeparator());
            String UNITS_SEPARATOR = String.valueOf(new DecimalFormat().getDecimalFormatSymbols().getGroupingSeparator());
            double amountDouble = Double.parseDouble(calculatorData.getYouPay().replace(UNITS_SEPARATOR, "").replace(DECIMAL_SEPARATOR, "."));

            if (calculatorData.getOriginCountry() != null && calculatorData.getOriginCountry().first.equals(NDL_COUNTRY_VALUE) && amountDouble > 2000 && calculatorData.getSendingCurrency() != null && calculatorData.getSendingCurrency().first.equals(EUR_CURRENCY)) {
                if (this.mProgressDialog == null && getActivity() != null && !getActivity().isDestroyed() && !getActivity().isFinishing() && getContext() != null) {
                    this.mProgressDialog = new DialogExt().showInfoDialog(getActivity(), getString(R.string.netherlands_warning_dialog_title), getString(R.string.netherlands_warning_dialog_message, amountString), () -> mPresenter.continueButtonClick(), this::hideWarningDialog, SmallWorldApplication.getStr(R.string.accept_text), SmallWorldApplication.getStr(R.string.cancel));
                }
            } else {
                mPresenter.continueButtonClick();
            }
        }
    }

    public void hideWarningDialog() {
        this.mProgressDialog.dismiss();
        this.mProgressDialog = null;
    }


    @OnClick(R.id.select_promo_code_button)
    public void onSelectPromoCodeClicked() {
        registerEvent("click_add_promotional_code", String.valueOf(PROMO_REQUEST_CODE), "", "");
        CalculatorData calculatorData = CalculatorInteractorImpl.getInstance().getCalculatorData();
        String payoutCountry = calculatorData.getPayoutCountry().first;
        mPresenter.onSelectPromoCode(PROMO_REQUEST_CODE, payoutCountry);
    }


    @OnClick(R.id.main_calculator_container)
    public void onRootViewClicked() {
        if ((requireActivity().getCurrentFocus() instanceof EditText)) {
            KeyboardUtils.hideKeyboard(getActivity(), mTheyReceiveEditText);
            KeyboardUtils.hideKeyboard(getActivity(), mYouPayEditText);
        }

        if (mTheyReceiveEditText.hasFocus()) {
            mTheyReceiveEditText.clearFocus();
        } else if (mYouPayEditText.hasFocus()) {
            mYouPayEditText.clearFocus();
        }
    }

    @OnClick(R.id.general_retry_button)
    public void onGeneralRetryButtonClick() {
        mPresenter.retryGeneralClick();
    }


    @OnClick(R.id.calculator_retry_button)
    public void onCalculatorRetryButtonClick() {
        mPresenter.retryCalculatorClick();
    }

    @OnClick(R.id.beneficiary_receive_container)
    public void changeOriginCountry() {
        HomeActivity activity = (HomeActivity) requireActivity();
        activity.changeOriginCountryFromSendToFragment(homeActivityCountrySelectionCallback);
    }

    @OnClick(R.id.you_pay_container)
    public void changeDestinationCountry() {
        HomeActivity activity = (HomeActivity) requireActivity();
        activity.changeDestinationCountryFromSendToFragment(homeActivityCountrySelectionCallback);
    }

    public void hideKeyboard(View view) {
        if (getActivity() != null) {
            InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void registerEvent(String eventAction, String eventLabel, String formType, String hierarchy) {
        trackEvent(new UserActionEvent(
                ScreenCategory.DASHBOARD.getValue(),
                eventAction,
                eventLabel,
                getHierarchy(hierarchy),
                formType,
                "",
                "",
                "",
                "",
                ""
        ));
    }

    private void registerBrazeEvent(String eventName, Map<String, String> eventProperties) {
        trackEvent(
                new BrazeEvent(eventName, eventProperties, BrazeEventType.ACTION)
        );
    }

    public void setListener(RecyclerView.OnItemTouchListener listener) {
        mListener = listener;
    }

}

