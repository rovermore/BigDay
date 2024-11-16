package com.smallworldfs.moneytransferapp.modules.calculator.presentation.ui.fragment;

import static com.smallworldfs.moneytransferapp.utils.Constants.CALCULATOR.CURRENCY_TYPE_PAYOUT_PRINCIPAL;
import static com.smallworldfs.moneytransferapp.utils.Constants.CALCULATOR.EUR_CURRENCY;
import static com.smallworldfs.moneytransferapp.utils.Constants.CALCULATOR.TOTALSALE;
import static com.smallworldfs.moneytransferapp.utils.Constants.COUNTRY.NDL_COUNTRY_VALUE;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.DialogExt;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.ImageViewExtKt;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.interactor.implementation.CalculatorInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.CalculatorData;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.Method;
import com.smallworldfs.moneytransferapp.modules.calculator.presentation.presenter.TransactionalCalculatorPresenter;
import com.smallworldfs.moneytransferapp.modules.calculator.presentation.presenter.impl.TransactionalCalculatorPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericFragment;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.adapter.CoinSelectorAdapter;
import com.smallworldfs.moneytransferapp.utils.AmountFormatter;
import com.smallworldfs.moneytransferapp.utils.AnimationUtils;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.ConstantsKt;
import com.smallworldfs.moneytransferapp.utils.KeyboardUtils;
import com.smallworldfs.moneytransferapp.utils.Log;
import com.smallworldfs.moneytransferapp.utils.Utils;
import com.smallworldfs.moneytransferapp.utils.widget.DismissibleEditText;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;

/**
 * Created by luismiguel on 11/7/17
 */
public class TransactionalCalculatorFragment extends GenericFragment implements TransactionalCalculatorPresenter.View, View.OnFocusChangeListener {

    private static final String TAG = "TransactionalCalculator";
    private static final String START_Y_EXTRA = "START_Y_EXTRA";
    private static final String SIDE_CLICK_EXTRA = "SIDE_CLICK_EXTRA";
    private static final String YOU_PAY_DEF = "YOU_PAY_DEF";
    private static final String BENEFICIARY_RECEIVE = "BENEFICIARY_RECEIVE";
    private static final String BENEFICIARY_NAME_EXTRA = "BENEFICIARY_NAME";

    private static final int DELAY_ANIMATION = 50;

    @BindView(R.id.main_container)
    RelativeLayout mMainContainer;
    @BindView(R.id.calculator_container)
    RelativeLayout mCalculatorContainer;
    @BindView(R.id.animation_container)
    RelativeLayout mAnimationContainer;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.country_flag_indicator)
    ImageView mCountryFlag;
    @BindView(R.id.you_pay_container)
    RelativeLayout mYouPayContainer;
    @BindView(R.id.they_receive_edittext)
    DismissibleEditText mTheyReceiveEditText;
    @BindView(R.id.you_pay_edittext)
    DismissibleEditText mYouPayEditText;
    @BindView(R.id.coin_indicator)
    StyledTextView mCoinIndicator;
    @BindView(R.id.coin_selector_container)
    ExpandableLayout mCoinSelectorContainer;
    @BindView(R.id.recyclerview)
    RecyclerView mCoinRecyclerview;
    @BindView(R.id.expand_arrow)
    View mExpandoCoinArrow;
    @BindView(R.id.you_pay_currency_text)
    StyledTextView mYouPayCurrency;
    @BindView(R.id.you_pay_info_container)
    View mYouPayInfoContainer;
    @BindView(R.id.you_pay_loading_view)
    View mYouPayLoadingView;
    @BindView(R.id.you_pay_error_view)
    View mYouPayErrorView;
    @BindView(R.id.done_button)
    StyledTextView mDoneButton;
    @BindView(R.id.beneficiary_receives_container)
    LinearLayout mBeneficiaryReceiveContainer;
    @BindView(R.id.beneficiary_receives_progressbar)
    ProgressBar mBeneficiaryReceiveProgressBar;
    @BindView(R.id.beneficiary_receive_title_text_view)
    TextView mBeneficiaryReceiveTextView;

    private Unbinder mUnbinder;
    private Handler mHandler = new Handler(Looper.getMainLooper());
    private EndClosAnimationListener mListener;
    private TransactionalCalculatorPresenterImpl mPresenter;
    private int mStartX = 0;
    private int mStartY = 0;
    private String mBeneficiaryReceive = "";
    @Nullable
    private String mBeneficiaryName;
    private CoinSelectorAdapter mCoinSelectorAdapter;
    private LinearLayoutManager mCoinSelectorLayoutManager;
    private int mSideClick = 0;
    private String mYouPay = "";
    private boolean mIsDoneEnabled = true;
    private CHANGED_FIELD mLastChangedField;
    private View.OnClickListener natigationToolbarClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            registerEvent("click_close", ConstantsKt.STRING_EMPTY);
            closeCalculator(true, Utils.isLowerThan21SDK());
        }
    };
    private String deliveryMethod = "";
    /**
     * Edit Text Listeners
     */

    private TextWatcher mYouPayListener = new TextWatcher() {
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
                if (editable.toString().equals(mYouPay)) {
                    mDoneButton.setText(R.string.action_done_transactional_calculator);
                    mIsDoneEnabled = true;
                } else {
                    mDoneButton.setText(R.string.recalculate);
                    mLastChangedField = CHANGED_FIELD.YOU_PAY;
                    mIsDoneEnabled = false;
                }
                mPresenter.onAmmountReadyToCalculate(formattedNumber, TOTALSALE);
            } else {
                Log.d(TAG, "Blocked you pay, no filter");
            }
        }
    };
    /**
     * Edit Text Listeners
     */

    private TextWatcher mBeneficiaryReceiveListener = new TextWatcher() {
        boolean blockedFormatting = false;

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
                if (editable.toString().equals(mBeneficiaryReceive)) {
                    mDoneButton.setText(R.string.action_done_transactional_calculator);
                    mIsDoneEnabled = true;
                } else {
                    mDoneButton.setText(R.string.recalculate);
                    mLastChangedField = CHANGED_FIELD.BENEFICIARY;
                    mIsDoneEnabled = false;
                }
                mPresenter.onAmmountReadyToCalculate(formattedNumber, CURRENCY_TYPE_PAYOUT_PRINCIPAL);


            } else {
                Log.d(TAG, "Blocked beneficiary, no filter");
            }
        }
    };

    public static TransactionalCalculatorFragment newInstance(int startY, int side, String youPay,
                                                              String beneficiaryRecieve,
                                                              @Nullable final String beneficiaryName,
                                                              String deliveryMethod) {
        TransactionalCalculatorFragment fragment = new TransactionalCalculatorFragment();
        fragment.deliveryMethod = deliveryMethod;
        Bundle args = new Bundle();

        args.putInt(START_Y_EXTRA, startY);
        args.putInt(SIDE_CLICK_EXTRA, side);
        args.putString(YOU_PAY_DEF, youPay);
        args.putString(BENEFICIARY_RECEIVE, beneficiaryRecieve);
        args.putString(BENEFICIARY_NAME_EXTRA, beneficiaryName);
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator_expanded, null, false);

        mUnbinder = ButterKnife.bind(this, view);
        mHandler = new Handler(Looper.getMainLooper());


        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (getArguments() != null) {
            mStartY = getArguments().getInt(START_Y_EXTRA);
            mSideClick = getArguments().getInt(SIDE_CLICK_EXTRA);
            mYouPay = getArguments().getString(YOU_PAY_DEF);
            mBeneficiaryReceive = getArguments().getString(BENEFICIARY_RECEIVE);
            mBeneficiaryName = getArguments().getString(BENEFICIARY_NAME_EXTRA);
        }

        mPresenter = new TransactionalCalculatorPresenterImpl(AndroidSchedulers.mainThread(), getActivity(), this);
        mPresenter.create();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void configureView() {
        Typeface type = ResourcesCompat.getFont(requireActivity(), R.font.nunito_black);

        mTheyReceiveEditText.setTypeface(type);
        mYouPayEditText.setTypeface(type);
        if (mBeneficiaryName != null) {
            mBeneficiaryReceiveTextView.setText(String.format(getString(R.string.beneficiary_receive_name), mBeneficiaryName));
        } else {
            mBeneficiaryReceiveTextView.setText(getString(R.string.beneficiary_receive));
        }
        mToolbar.setNavigationIcon(getActivity().getResources().getDrawable(R.drawable.ic_action_close_white));
        mToolbar.setTitle("");
        mToolbar.setNavigationOnClickListener(natigationToolbarClickListener);
    }

    @Override
    public void configureCalculator(String payoutCountryKey, String currentBeneficiaryAmount, String currentYouPayAmount, String sendingCurrency, String payoutCurrency, Method currentMethod) {
        ImageViewExtKt.loadCircularImage(
                mCountryFlag,
                requireContext(),
                R.drawable.placeholder_country_adapter,
                Constants.COUNTRY.FLAG_IMAGE_ASSETS + payoutCountryKey + Constants.COUNTRY.FLAG_IMAGE_EXTENSION
        );

        mTheyReceiveEditText.setText(AmountFormatter.normalizeDoubleString(AmountFormatter.formatStringNumber(currentBeneficiaryAmount)));
        mYouPayEditText.setText(AmountFormatter.normalizeDoubleString(AmountFormatter.formatStringNumber(currentYouPayAmount)));
        mCoinIndicator.setText(payoutCurrency);

        // Coin Beneficiary selector
        mCoinSelectorLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mCoinRecyclerview.setLayoutManager(mCoinSelectorLayoutManager);
        Context context = getActivity();//You should not use getContext() if API < 23 : https://issuetracker.google.com/issues/37060988
        mCoinSelectorAdapter = new CoinSelectorAdapter(ContextCompat.getDrawable(context, R.drawable.calculator_dark_blue_item_row));
        mCoinRecyclerview.setAdapter(mCoinSelectorAdapter);
        mYouPayCurrency.setText(sendingCurrency);

        ArrayList<Pair<String, String>> currencies = new ArrayList<>();
        if (currentMethod != null && currentMethod.getCurrencies() != null) {
            for (TreeMap<String, String> currency : currentMethod.getCurrencies()) {
                currencies.add(new Pair<>(currency.firstEntry().getKey(), currency.firstEntry().getValue()));
            }
        }

        mCoinSelectorAdapter.updateData(currencies);
        mCoinSelectorAdapter.setOnSelectCoinListener(new CoinSelectorAdapter.SelectCoinListener() {
            @Override
            public void onSelectCoinSelected(Pair<String, String> currency) {
                mPresenter.onSelectCurrencyInCalculator(currency);
                updateCurrencyTextIndicator(currency);
                collapseCoinSelector();
                registerEvent("click_select_currency", currency.first);
            }
        });

        mTheyReceiveEditText.addTextChangedListener(mBeneficiaryReceiveListener);
        mYouPayEditText.addTextChangedListener(mYouPayListener);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.destroy();
        }

        mUnbinder.unbind();
        mHandler = null;
    }

    @Override
    public void openCalculator(boolean disableAnimation) {
        openSoftKeyboard();
        if (!disableAnimation) {
            if (mHandler != null) {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            int startRadius = 0;
                            int endRadius = (int) Math.hypot(mMainContainer.getWidth(), mMainContainer.getHeight());
                            final Animator anim = ViewAnimationUtils.createCircularReveal(mAnimationContainer, mStartX, mStartY, startRadius, endRadius);
                            anim.addListener(new Animator.AnimatorListener() {
                                @Override
                                public void onAnimationStart(Animator animator) {
                                }

                                @Override
                                public void onAnimationEnd(Animator animator) {
                                    if (mYouPayContainer != null) {
                                        mYouPayContainer.animate().alpha(1f).setDuration(200).start();
                                    }
                                }

                                @Override
                                public void onAnimationCancel(Animator animator) {
                                }

                                @Override
                                public void onAnimationRepeat(Animator animator) {
                                }
                            });
                            mAnimationContainer.setVisibility(View.VISIBLE);
                            anim.start();
                        } catch (Exception e) {
                            Log.e(TAG, "openCalculator: Blame static mContext ? ", e);
                        }
                    }
                }, DELAY_ANIMATION);
            }
        } else {
            mAnimationContainer.setVisibility(View.VISIBLE);
        }
    }

    private void openSoftKeyboard() {
        if (mSideClick == 0) {
            mTheyReceiveEditText.requestFocus();
        } else {
            mYouPayEditText.requestFocus();
        }

        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    public void closeCalculator(boolean recoveryData, boolean disableAnimation) {

        if (recoveryData) {
            mPresenter.recoveryData();
            CalculatorInteractorImpl.getInstance().refreshCalculator();
        }

        hideSoftKeyboard();

        if (!disableAnimation) {


            int startRadius = Math.max(mMainContainer.getWidth(), mMainContainer.getHeight());
            int endRadius = 0;

            Animator anim = ViewAnimationUtils.createCircularReveal(mAnimationContainer, 0, mStartY, startRadius, endRadius);
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {

                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    animator.cancel();

                    if (mAnimationContainer != null) {
                        mAnimationContainer.setVisibility(View.GONE);
                        if (mListener != null) {
                            mListener.onAnimationEnd();
                            mListener = null;
                        }
                    }
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });

            anim.start();
        } else {
            if (mListener != null) {
                mListener.onAnimationEnd();
                mListener = null;
            }
        }
    }

    @Override
    public void enableDisableDoneButton(boolean enable) {
        mDoneButton.setTextColor(enable ? ContextCompat.getColor(getActivity(), R.color.blue_accent_color) : ContextCompat.getColor(getActivity(), R.color.dark_grey_text));
        mDoneButton.setClickable(enable);
    }

    @Override
    public void enableDoneMode() {
        mIsDoneEnabled = true;
        mDoneButton.setText(R.string.action_done_transactional_calculator);
    }

    private void hideSoftKeyboard() {
        KeyboardUtils.hideKeyboard(getActivity(), mTheyReceiveEditText);
        KeyboardUtils.hideKeyboard(getActivity(), mYouPayEditText);
    }

    public void setAnimationEndListener(EndClosAnimationListener listener) {
        this.mListener = listener;
    }

    public void updateCurrencyTextIndicator(Pair<String, String> currency) {
        mCoinIndicator.setText(currency.first);
    }

    public void expandCoinSelector() {
        hideSoftKeyboard();
        mCoinSelectorContainer.expand();
        AnimationUtils.rotateView(mExpandoCoinArrow, 300, 0);
    }

    public void collapseCoinSelector() {
        mCoinSelectorContainer.collapse();
        AnimationUtils.rotateView(mExpandoCoinArrow, 300, 180);
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {

    }

    /**
     * Calculator Callbacks
     */

    @Override
    public void updateYouPayEditText(String formattedAmount) {
        mYouPayEditText.setText(formattedAmount);
    }

    @Override
    public void updateTheyReceiveEditText(String formattedAmount) {
        mTheyReceiveEditText.setText(formattedAmount);
    }

    @Override
    public void showYouPayCalculatorLoadingView() {
        mYouPayLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideYouPayCalculatorViewError() {
        mYouPayErrorView.setVisibility(View.GONE);
    }

    @Override
    public void showYouPayCalculatorViewError() {
        mYouPayErrorView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showBeneficiaryLoadingView() {
        mBeneficiaryReceiveProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBeneficiaryLoadingView() {
        mBeneficiaryReceiveProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void showBeneficiaryContainer() {
        mBeneficiaryReceiveContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBeneficiaryContainer() {
        mBeneficiaryReceiveContainer.setVisibility(View.GONE);
    }

    @Override
    public void showYouPayInfoContainer() {
        mYouPayInfoContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideYouPayInfoContainer() {
        mYouPayInfoContainer.setVisibility(View.INVISIBLE);
    }

    @Override
    public void hideYouPayCalculatorLoadingView() {
        mYouPayLoadingView.setVisibility(View.GONE);
    }

    /**
     * OnCliks
     */

    @OnClick(R.id.expand_coins_button)
    public void onExpandSelectorCoins() {
        if (mCoinSelectorContainer.isExpanded()) {
            collapseCoinSelector();
            registerEvent("click_collapse_currency", ConstantsKt.STRING_EMPTY);
        } else {
            expandCoinSelector();
            registerEvent("click_expand_currency", ConstantsKt.STRING_EMPTY);
        }
    }

    @OnClick(R.id.main_container)
    public void onRootViewClicked() {
        mPresenter.onYouPayLostFocus(mYouPayEditText.getText().toString());
        mPresenter.onTheyReceivesLostFocus(mTheyReceiveEditText.getText().toString());

    }

    private android.app.Dialog mProgressDialog;

    @OnClick(R.id.done_button)
    public void onDoneButtonPressed() {
        if (mIsDoneEnabled) {
            registerEvent("click_done", ConstantsKt.STRING_EMPTY);
            mPresenter.onDoneClicked();
            closeCalculator(false, Utils.isLowerThan21SDK());
        } else {
            // If the user country is NDL check if the quantity is more tha 2000 thousand and the currency EUR
            CalculatorData calculatorData = CalculatorInteractorImpl.getInstance().getCalculatorData();

            // Amount (string)
            String amountString = calculatorData.getYouPay();

            // Amount double
            String DECIMAL_SEPARATOR = String.valueOf(new DecimalFormat().getDecimalFormatSymbols().getDecimalSeparator());
            String UNITS_SEPARATOR = String.valueOf(new DecimalFormat().getDecimalFormatSymbols().getGroupingSeparator());
            double amountDouble = Double.parseDouble(calculatorData.getYouPay().replace(UNITS_SEPARATOR, "").replace(DECIMAL_SEPARATOR, "."));

            if (calculatorData.getOriginCountry().first.equals(NDL_COUNTRY_VALUE) && amountDouble > 2000 && calculatorData.getSendingCurrency().first.equals(EUR_CURRENCY)) {
                if (this.mProgressDialog == null && getActivity() != null && !getActivity().isDestroyed() && !getActivity().isFinishing() && getContext() != null) {
                    this.mProgressDialog = new DialogExt().showInfoDialog(getActivity(), getString(R.string.netherlands_warning_dialog_title), getString(R.string.netherlands_warning_dialog_message, amountString), () -> mPresenter.onRecalculateClicked(mLastChangedField), this::hideWarningDialog, SmallWorldApplication.getStr(R.string.accept_text), SmallWorldApplication.getStr(R.string.cancel));
                }
            } else {
                mPresenter.onRecalculateClicked(mLastChangedField);
            }
        }
    }

    public void hideWarningDialog() {
        this.mProgressDialog.dismiss();
        this.mProgressDialog = null;
    }


    public enum CHANGED_FIELD {BENEFICIARY, YOU_PAY}

    public interface EndClosAnimationListener {
        void onAnimationEnd();
    }

    private void registerEvent(String eventAction, String eventLabel) {
        trackEvent(new UserActionEvent(
                ScreenCategory.TRANSFER.getValue(),
                eventAction,
                eventLabel,
                getHierarchy(""),
                "",
                deliveryMethod,
                "",
                "",
                "",
                ""
        ));
    }
}
