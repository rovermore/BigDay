package com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity;

import static com.smallworldfs.moneytransferapp.modules.c2b.presentation.ui.activity.C2BActivity.BENEFICIARY_TYPE;
import static com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity.CHECKOUT_DATA;
import static com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity.SHOW_CHECKOUT_DIALOG_EXTRA;
import static com.smallworldfs.moneytransferapp.modules.home.presentation.ui.activity.HomeActivity.TRANSACTION_DATA;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity.CONTENT_FIELD_POSITION;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity.CONTENT_STEP_ID;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity.RESULT_DATA;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.PayoutLocationSelectorActivity.STEP_ID_EXTRA;
import static com.smallworldfs.moneytransferapp.utils.Constants.COUNTRY.ESP_COUNTRY_VALUE;
import static com.smallworldfs.moneytransferapp.utils.Constants.DELIVERY_METHODS.CASH_PICKUP;
import static com.smallworldfs.moneytransferapp.utils.Constants.REQUEST_CODES.REQUEST_DOCUMENT_TYPE;
import static com.smallworldfs.moneytransferapp.utils.Constants.REQUEST_CODES.REQUEST_VALIDATE_ID;
import static com.smallworldfs.moneytransferapp.utils.ConstantsKt.RESULT_ITEM;
import static com.smallworldfs.moneytransferapp.utils.ConstantsKt.STRING_EMPTY;

import android.Manifest;
import android.animation.LayoutTransition;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.FragmentManager;
import androidx.legacy.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEvent;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventName;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventProperty;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventType;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ECommerceEvent;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.EcommerceCheckoutInfo;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.DialogExt;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.DialogExtKt;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.ImageViewExtKt;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.interactor.implementation.CalculatorInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.Method;
import com.smallworldfs.moneytransferapp.modules.calculator.presentation.ui.fragment.TransactionalCalculatorFragment;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.Checkout;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.CreateTransactionResponse;
import com.smallworldfs.moneytransferapp.modules.checkout.presentation.navigator.CheckoutNavigator;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericDialog;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.GenericFormField;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.KeyValueData;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.structure.Step;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.structure.StepStatus;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.QuickReminderMessage;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.manager.ContentStepManager;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter.TransactionalPresenter;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter.implementation.TransactionalPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.adapter.DeliveryMethodAdapter;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.adapter.FormDataAdapter;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.fragment.QuickReminderPopupFragment;
import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.model.BeneficiaryUIModel;
import com.smallworldfs.moneytransferapp.presentation.account.documents.list.model.ComplianceDocUIModel;
import com.smallworldfs.moneytransferapp.presentation.account.documents.selector.DocumentsSelectorActivity;
import com.smallworldfs.moneytransferapp.presentation.autentix.DocumentValidationActivity;
import com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.utils.CashPickupResultModel;
import com.smallworldfs.moneytransferapp.utils.AmountFormatter;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.ConstantsKt;
import com.smallworldfs.moneytransferapp.utils.Log;
import com.smallworldfs.moneytransferapp.utils.SamsungMemLeak;
import com.smallworldfs.moneytransferapp.utils.Utils;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import net.cachapa.expandablelayout.ExpandableLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.hilt.android.AndroidEntryPoint;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@AndroidEntryPoint
public class TransactionalActivity extends GenericActivity implements TransactionalPresenter.View {

    private static final String TAG = TransactionalActivity.class.getSimpleName();

    public static final String BENEFICIARY_EXTRA = "BENEFICIARY_EXTRA";
    public static final String YOU_PAY_EXTRA = "YOU_PAY";
    public static final String CONTENT_APPEND_TAG = "content_";
    private static final String GENERAL_ERROR_STRUCTURE_VIEW = "GENERAL_ERROR";
    private static final int REQUEST_CONTACTS_PERMISSION_CODE = 200;

    Unbinder mUnbinder;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.country_flag_indicator)
    ImageView mCountryFlag;
    @BindView(R.id.beneficiary_receive_amount)
    StyledTextView mBeneficiaryReceiveAmountText;
    @BindView(R.id.you_pay_amount)
    StyledTextView mYoupayAmountText;
    @BindView(R.id.sending_currency)
    StyledTextView mSendingCurrency;
    @BindView(R.id.beneficiary_currency)
    StyledTextView mBeneficiaryCurrency;
    @BindView(R.id.content_calculator_frame)
    FrameLayout mContentFrame;
    @BindView(R.id.beneficiary_receive_container)
    RelativeLayout mBeneficiaryContainer;
    @BindView(R.id.main_content)
    LinearLayout mMainTransactionalContent;
    @BindView(R.id.loading_view)
    View mLoadingView;
    @BindView(R.id.relative_container)
    RelativeLayout mRelativeContainer;
    @BindView(R.id.loading_calculator_you_pay_view)
    View mLoadingCalculatorYouPayView;
    @BindView(R.id.loading_calculator_beneficiary_view)
    View mLoadingCalculatorBeneficiaryView;
    @BindView(R.id.main_nested_scroll_view)
    NestedScrollView mMainNestedScrollView;
    @BindView(R.id.calculator_error_view)
    View mCalculatorErrorView;
    @BindView(R.id.calculator_error_text)
    StyledTextView mCalculatorErrorText;
    @BindView(R.id.content_frame)
    FrameLayout mPopupContentFrame;
    @BindView(R.id.calculator_main_container)
    ConstraintLayout calculatorMainContainer;
    @BindView(R.id.triangle_image)
    ImageView triangleImage;
    @BindView(R.id.beneficiary_receive_title_text_view)
    TextView mBeneficiaryReceiveTitleTextView;

    private TransactionalPresenterImpl mPresenter;
    private FragmentManager mFragmentManager;
    private TransactionalCalculatorFragment mCalculatorFragment;
    private HashMap<Step, View> mBlockStepMap;
    private BeneficiaryUIModel mBeneficiarySelected;
    private ContentStepManager mContentStepManager;
    private Dialog mProgressDialog;
    private Handler mHandler;
    private QuickReminderPopupFragment mQuickReminderPopupFragment;
    private String deliveryMethod = ConstantsKt.STRING_EMPTY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transactional);
        mUnbinder = ButterKnife.bind(this);

        mFragmentManager = getSupportFragmentManager();
        mHandler = new Handler(Looper.getMainLooper());
        String youPay = null;
        String beneficiaryType = "";
        if (getIntent().getExtras() != null) {
            mBeneficiarySelected = getIntent().getParcelableExtra(BENEFICIARY_EXTRA);
            youPay = getIntent().getStringExtra(YOU_PAY_EXTRA);
            beneficiaryType = getIntent().getStringExtra(BENEFICIARY_TYPE);
        }

        mPresenter = new TransactionalPresenterImpl(AndroidSchedulers.mainThread(), Schedulers.io(), this, this, this, mBeneficiarySelected, youPay, beneficiaryType);
        mPresenter.create();

        mContentStepManager = new ContentStepManager(this, this, mPresenter);
        mContentStepManager.setMainNestedScrollView(mMainNestedScrollView);
        mContentStepManager.setMainContainer(mMainTransactionalContent);

        calculatorMainContainer.getViewTreeObserver().addOnGlobalLayoutListener(() -> {
            if (calculatorMainContainer != null) {
                setHeightTriangleImageView(calculatorMainContainer.getHeight());
            }
        });
    }

    private void setHeightTriangleImageView(int height) {
        android.view.ViewGroup.LayoutParams params = triangleImage.getLayoutParams();
        params.height = height;
        triangleImage.setLayoutParams(params);

    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.pause();
    }


    @Override
    protected void onResume() {
        super.onResume();
        mPresenter.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mHandler = null;
        mQuickReminderPopupFragment = null;
        mPresenter.destroy();
        mUnbinder.unbind();
        SamsungMemLeak.onDestroy(getApplicationContext());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        registerEvent("click_back", "", "", deliveryMethod);
        if (mCalculatorFragment != null) {
            closeCalculator();
        } else {
            if (mQuickReminderPopupFragment == null) {
                if (mPresenter.goToLoseInformation()) {
                    GenericDialog dialog = new DialogExt().showInfoDoubleActionGeneralDialog(this, getString(R.string.transaction_in_progress_dialog_title),
                            getString(R.string.transaction_in_progress_dialog_content),
                            getString(R.string.accept_text),
                            () -> {
                                HashMap<String, String> properties = new HashMap<>();
                                properties.put(BrazeEventProperty.DESTINATION_COUNTRY.getValue(), mBeneficiarySelected.getPayoutCountry().getIso3());
                                properties.put(BrazeEventProperty.DELIVERY_METHOD.getValue(), deliveryMethod);
                                registerBrazeEvent(BrazeEventName.TRANSACTION_CANCELLED.getValue(), properties);
                                mPresenter.exitTransaction();
                                finish();
                                registerEvent("click_accept", "", "", deliveryMethod);
                            },
                            getString(R.string.cancel),
                            () -> {
                                registerEvent("click_cancel", "", "", deliveryMethod);
                            });
                    dialog.trackDialog(ScreenName.MODAL_DISCARD_TRANSFER.getValue());
                } else {
                    super.onBackPressed();
                }
            }
        }
    }

    @Override
    public void configureView() {
        // Init Views List
        mBlockStepMap = new HashMap<>();

        setSupportActionBar(mToolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("");
    }

    @Override
    public void configureStaticCalculator(String payoutCountry, String beneficiaryReceiveAmount, String beneficiaryCurrency) {
        ImageViewExtKt.loadCircularImage(
                mCountryFlag,
                this,
                R.drawable.placeholder_country_adapter,
                Constants.COUNTRY.FLAG_IMAGE_ASSETS + payoutCountry + Constants.COUNTRY.FLAG_IMAGE_EXTENSION
        );

        final String beneficiaryName = getBeneficiaryName();
        if (beneficiaryName != null) {
            mBeneficiaryReceiveTitleTextView.setText(String.format(getString(R.string.beneficiary_receive_name), beneficiaryName));
        } else {
            mBeneficiaryReceiveTitleTextView.setText(getString(R.string.beneficiary_receive));
        }
        String quantity = AmountFormatter.normalizeDoubleString(AmountFormatter.formatStringNumber(beneficiaryReceiveAmount));
        mBeneficiaryReceiveAmountText.setText(quantity);
        mBeneficiaryCurrency.setText(beneficiaryCurrency);
    }


    /*
     * CALCULATOR CALLBACKS
     */

    @Override
    public void showCalculator(int side) {
        trackScreen(ScreenName.CALCULATOR_SCREEN.getValue());
        int startY = mToolbar.getHeight() + mBeneficiaryContainer.getHeight() / 2;
        mCalculatorFragment = TransactionalCalculatorFragment.newInstance(startY, side,
                mYoupayAmountText.getText().toString(), mBeneficiaryReceiveAmountText.getText().toString(),
                getBeneficiaryName(), deliveryMethod);
        mFragmentManager.beginTransaction()
                .replace(R.id.content_calculator_frame, mCalculatorFragment)
                .addToBackStack(null)
                .commit();

        mCalculatorFragment.setAnimationEndListener(() -> {
            mFragmentManager.popBackStack();
            mCalculatorFragment = null;
            mPresenter.setShowingCalculator(false);
            if (CalculatorInteractorImpl.getInstance().getCalculatorStatus() == CalculatorInteractorImpl.CalculatorStatus.OK) {
                showCalculatorLoadingView(false);
            }
        });
        showCalculatorLoadingView(true);
    }

    @Nullable
    private String getBeneficiaryName() {
        if (mBeneficiarySelected != null) {
            return mBeneficiarySelected.getName();
        } else {
            return null;
        }
    }

    @Override
    public void closeCalculator() {
        if (mCalculatorFragment != null) {
            mCalculatorFragment.closeCalculator(true, Utils.isLowerThan21SDK());
        }
    }

    public static class EventRefreshAmounts {
    }

    @SuppressWarnings({"unused", "RedundantSuppression"})
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(EventRefreshAmounts event) {
        showGeneralLoadingView();
        if (mPresenter != null) {
            mPresenter.onNeedToRequestAgainDataStep(0);
        }
    }

    private void closeQuickReminderPopup() {
        if (this.mQuickReminderPopupFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(mQuickReminderPopupFragment);
            this.mQuickReminderPopupFragment = null;
        }

        mPopupContentFrame.setVisibility(View.GONE);
    }

    @Override
    public void showCalculatorLoadingView(boolean show) {
        mLoadingCalculatorYouPayView.setVisibility(show ? View.VISIBLE : View.GONE);
        mLoadingCalculatorBeneficiaryView.setVisibility(show ? View.VISIBLE : View.GONE); //TO-DO Refactor loadings
    }

    @Override
    public void showYouPayCalculated(String youPayAmount, String youPayCurrency) {
        mYoupayAmountText.setText(AmountFormatter.normalizeDoubleString(AmountFormatter.formatStringNumber(youPayAmount)));
        mSendingCurrency.setText(youPayCurrency);
    }

    @Override
    public void showPayoutBottomCalculator(String payoutPrincipal, String payoutPrincipalCurrency, String principal, String principalCurrency) {
        if (mContentStepManager != null) {
            View locationView = mContentStepManager.getLocationView();
            if (locationView != null) {
                ((StyledTextView) locationView.findViewById(R.id.payout_principal_value)).setText(payoutPrincipal + " " + payoutPrincipalCurrency);
                ((StyledTextView) locationView.findViewById(R.id.principal_value)).setText(principal + " " + principalCurrency);
            }
        }
    }

    /*
     * TRANSACTIONAL CALLBACKS
     */

    @Override
    public void hideGeneralLoadingView() {
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void onStructureError() {
        View errorView = LayoutInflater.from(this).inflate(R.layout.transactional_step_error_view, mRelativeContainer, false);
        if (errorView != null) {
            RelativeLayout.LayoutParams rlp = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.MATCH_PARENT,
                    RelativeLayout.LayoutParams.MATCH_PARENT);
            rlp.addRule(RelativeLayout.CENTER_IN_PARENT);
            errorView.setLayoutParams(rlp);

            errorView.setTag(GENERAL_ERROR_STRUCTURE_VIEW);

            errorView.findViewById(R.id.retry_step_button).setOnClickListener(v -> mPresenter.retryGeneralStructureClick());
            mRelativeContainer.addView(errorView);
        }
    }

    @Override
    public void hideGeneralStructureErrorView() {
        View errorView = mRelativeContainer.findViewWithTag(GENERAL_ERROR_STRUCTURE_VIEW);
        if (errorView != null) {
            ((RelativeLayout) errorView.getParent()).removeView(errorView);
        }
    }

    @Override
    public void showGeneralLoadingView() {
        mLoadingView.setVisibility(View.VISIBLE);
    }


    @Override
    public void appendStep(Step step, int position, boolean isLastPosition) {
        inflateStep(step, position, isLastPosition);
    }

    private boolean idStepAdded(Step step) {
        for (HashMap.Entry<Step, View> entry : mBlockStepMap.entrySet()) {
            if (entry.getKey().getStepId().equals(step.getStepId())) {
                return true;
            }
        }
        return false;
    }

    private View getStepViewAdded(Step step) {
        View blockedViewAdded = null;
        for (HashMap.Entry<Step, View> entry : mBlockStepMap.entrySet()) {
            if (entry.getKey().getStepId().equals(step.getStepId())) {
                blockedViewAdded = entry.getValue();
                entry.getKey().updateStep(step);
                break;
            }
        }
        return blockedViewAdded;
    }


    private void inflateStep(final Step step, int position, boolean isLastPosition) {

        if (mBlockStepMap.size() != 0 && idStepAdded(step) && !step.getStepType().equals(Constants.STEP_TYPE.DELIVERY_METHOD)) {
            View stepView = getStepViewAdded(step);
            if (stepView != null) {
                ((StyledTextView) stepView.findViewById(R.id.title)).setText(step.getName());
                stepView.findViewById(R.id.sub_line).setVisibility(isLastPosition ? View.INVISIBLE : View.VISIBLE);
            }
        } else if (!idStepAdded(step)) {

            LinearLayout blockStepContent = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.transactional_step_layout, mMainTransactionalContent, false);
            if (isLastPosition) {
                blockStepContent.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            }
            if (blockStepContent != null) {
                blockStepContent.findViewById(R.id.pre_title).setVisibility(View.INVISIBLE);
                ((StyledTextView) blockStepContent.findViewById(R.id.title)).setText(step.getName());

                if (position == 0) {
                    // Disable First Line
                    ((ExpandableLayout) blockStepContent.findViewById(R.id.expandable_layout)).expand(true);
                    blockStepContent.findViewById(R.id.pre_line).setVisibility(View.INVISIBLE);
                }

                if (isLastPosition) {
                    blockStepContent.findViewById(R.id.sub_line).setVisibility(View.INVISIBLE);
                }

                blockStepContent.findViewById(R.id.step_indicator).setVisibility(View.VISIBLE);
                ((StyledTextView) blockStepContent.findViewById(R.id.step_indicator)).setText(String.valueOf(position + 1));

                if (position != 0) {
                    blockStepContent.findViewById(R.id.disable_view).setVisibility(View.VISIBLE);
                    ((StyledTextView) blockStepContent.findViewById(R.id.step_indicator)).setTextColor(getResources().getColor(R.color.blue_color_control));
                } else {
                    // Set step number
                    (blockStepContent.findViewById(R.id.circle_indicator_2)).setBackground(ContextCompat.getDrawable(this, R.drawable.blue_light_circle_indicator_selected));
                    ((StyledTextView) blockStepContent.findViewById(R.id.step_indicator)).setTextColor(getResources().getColor(R.color.white));
                }

                View.OnClickListener headerClickListener = v -> {
                    // Toggle Expandable layout
                    mPresenter.stepClicked(step);
                };

                blockStepContent.findViewById(R.id.content_header).setOnClickListener(headerClickListener);

                // Track view in list
                blockStepContent.setTag(step.getStepId());
                mBlockStepMap.put(step, blockStepContent);

                // Flag to control soft animation and append new step
                if (step.isNewStep()) {
                    mMainTransactionalContent.setLayoutTransition(new LayoutTransition());
                }

                mMainTransactionalContent.addView(blockStepContent);

                if (step.isNewStep()) {
                    mMainTransactionalContent.setLayoutTransition(null);
                    step.setNewStep(false);
                    mPresenter.resume();
                }
            }
        }
    }

    @Override
    public void deleteStep(Step step) {
        View stepView = mBlockStepMap.get(step);
        if (stepView != null) {
            mBlockStepMap.remove(step);
            mMainTransactionalContent.setLayoutTransition(new LayoutTransition());
            mMainTransactionalContent.removeView(stepView);
            mMainTransactionalContent.setLayoutTransition(null);
        }
    }

    @Override
    public void resetContentStep(Step step) {
        View view = mBlockStepMap.get(step);
        if (view != null) {
            View stepView = view.findViewWithTag(CONTENT_APPEND_TAG + step.getStepId());
            if (stepView != null) {
                stepView.setVisibility(View.INVISIBLE);
                ((ExpandableLayout) stepView.getParent()).removeView(stepView);
            }
        }
    }


    /**
     * Draw content inside Delivery Method Status
     */
    @Override
    public void drawContentStep(final Step step, ArrayList<? extends GenericFormField> data, String currentYouPayAmount) {
        LinearLayout blockStepContent = (LinearLayout) mBlockStepMap.get(step);

        if (blockStepContent != null && blockStepContent.findViewWithTag(CONTENT_APPEND_TAG + step.getStepId()) == null) {

            View stepLayout = null;
            switch (step.getStepType()) {
                case Constants.STEP_TYPE.DELIVERY_METHOD: {
                    stepLayout = mContentStepManager.inflateDeliveryMethod(step, data, mPresenter.isBeneficiaryPresent());
                    break;
                }
                case Constants.STEP_TYPE.FORM: {
                    stepLayout = mContentStepManager.inflateGenericForm(step, data, blockStepContent);
                    break;
                }
                case Constants.STEP_TYPE.BENEFICIARY_FORM: {
                    stepLayout = mContentStepManager.inflateBeneficiaryForm(step, data, blockStepContent);
                    break;
                }
                case Constants.STEP_TYPE.LOCATION_LIST:
                    stepLayout = mContentStepManager.inflateLocationList(step, mBlockStepMap.get(step), currentYouPayAmount);
                    break;
            }

            if (stepLayout != null) {
                ((ExpandableLayout) blockStepContent.findViewById(R.id.expandable_layout)).addView(stepLayout);
            }
        }
    }

    @Override
    public void drawContentStep(Step step, CashPickupResultModel cashPickupResultModel) {
        LinearLayout blockStepContent = (LinearLayout) mBlockStepMap.get(step);

        if (blockStepContent != null && blockStepContent.findViewWithTag(CONTENT_APPEND_TAG + step.getStepId()) == null) {
            if (cashPickupResultModel != null && cashPickupResultModel.getFee() != null && cashPickupResultModel.getFee() == 0 && cashPickupResultModel.getRate() != null) {
                try {
                    double youPay = Double.parseDouble(mYoupayAmountText.getText().toString().replace(",", "."));
                    double rate = Double.parseDouble(cashPickupResultModel.getRate().replace(",", "."));
                    double total = (youPay * rate);

                    Locale locale = this.getResources().getConfiguration().locale;
                    NumberFormat format = NumberFormat.getInstance(locale);

                    step.setStepSelectedItem(cashPickupResultModel.getRepresentativeName());

                    mBeneficiaryReceiveAmountText.setText(format.format(total));
                } catch (NumberFormatException e) {
                    Log.e(TAG, "Parse Amount Error:----------------------------------------------------------", e);
                }
            } else {
                mBeneficiaryReceiveAmountText.setText(CalculatorInteractorImpl.getInstance().getCalculatorData().getAmount());
            }

            View stepLayout = mContentStepManager.inflatePickUpLocationStep(cashPickupResultModel, step);
            if (stepLayout != null) {
                ((ExpandableLayout) blockStepContent.findViewById(R.id.expandable_layout)).addView(stepLayout);
            }
        }
    }

    @Override
    public void completeStep(Step step) {
        View view = mBlockStepMap.get(step);
        if (view != null) {
            (view.findViewById(R.id.circle_indicator_2)).setBackground(ContextCompat.getDrawable(this, R.drawable.blue_circle_indicator));
            (view.findViewById(R.id.step_indicator)).setVisibility(View.INVISIBLE);
            (view.findViewById(R.id.tick_indicator)).setVisibility(View.VISIBLE);
            (view.findViewById(R.id.step_loading)).setVisibility(View.GONE);
            (view.findViewById(R.id.pre_title)).setVisibility(View.VISIBLE);
            ((StyledTextView) view.findViewById(R.id.pre_title)).setText(step.getName());

            if (!step.getStepType().equalsIgnoreCase(Constants.STEP_TYPE.DELIVERY_METHOD)) {
                if (step.getStepType().equals(Constants.STEP_TYPE.LOCATION_LIST) && step.getStepSubType() != null && step.getStepSubType().equals(CASH_PICKUP)) {
                    Button sendButton = view.findViewById(R.id.transactionalStepLocationListCashPickUpLayoutMaterialButtonDone);
                    if (!step.getStepId().equalsIgnoreCase(Constants.STEP_TAGS.STEP_1) && !step.getStepId().equalsIgnoreCase(Constants.STEP_TAGS.STEP_2)) {
                        sendButton.setText(R.string.proceed_action);
                    } else {
                        sendButton.setText(R.string.done_action);
                    }
                    sendButton.setEnabled(true);
                    sendButton.setAlpha(1f);
                } else {
                    StyledTextView sendButton = view.findViewById(R.id.sendButton);
                    if (!step.getStepId().equalsIgnoreCase(Constants.STEP_TAGS.STEP_1) && !step.getStepId().equalsIgnoreCase(Constants.STEP_TAGS.STEP_2)) {
                        sendButton.setText(R.string.proceed_action);
                    } else {
                        sendButton.setText(R.string.done_action);
                    }
                    sendButton.setEnabled(true);
                    sendButton.setAlpha(1f);
                }
            }

            ((StyledTextView) view.findViewById(R.id.title)).setText(TextUtils.isEmpty(step.getStepSelectedItem()) ? step.getName() : step.getStepSelectedItem());
        }
    }

    @Override
    public void showValidatingLoadingStepView(Step step) {
        View view = mBlockStepMap.get(step);
        if (view != null) {
            view.findViewById(R.id.step_loading).setVisibility(View.VISIBLE);
            view.findViewById(R.id.step_indicator).setVisibility(View.GONE);
            view.findViewById(R.id.tick_indicator).setVisibility(View.VISIBLE);
            view.findViewById(R.id.circle_indicator_2).setBackground(ContextCompat.getDrawable(this, R.drawable.blue_light_circle_indicator_selected));


            switch (step.getStepType()) {
                case Constants.STEP_TYPE.BENEFICIARY_FORM:
                case Constants.STEP_TYPE.FORM:
                case Constants.STEP_TYPE.LOCATION_LIST:
                    if (mBeneficiarySelected != null && !mBeneficiarySelected.getDeliveryMethod().getType().isEmpty() && mBeneficiarySelected.getDeliveryMethod().getType().equals(CASH_PICKUP)) {
                        Button button = view.findViewById(R.id.transactionalStepLocationListCashPickUpLayoutMaterialButtonDone);
                        if (button != null) {
                            button.setText(R.string.validating_step_text_button);
                            button.setEnabled(false);
                            button.setAlpha(.80f);
                        }
                    } else {
                        StyledTextView sendButton = view.findViewById(R.id.sendButton);
                        if (sendButton != null) {
                            sendButton.setText(R.string.validating_step_text_button);
                            sendButton.setEnabled(false);
                            sendButton.setAlpha(.80f);
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void hideValidatingLoadingStepView(Step step) {
        View view = mBlockStepMap.get(step);
        if (view != null) {
            view.findViewById(R.id.step_loading).setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void markCurrentStepEditing(Step step) {
        View view = mBlockStepMap.get(step);
        if (view != null) {
            (view.findViewById(R.id.circle_indicator_2)).setBackground(ContextCompat.getDrawable(this, R.drawable.blue_light_circle_indicator_selected));
            ((StyledTextView) view.findViewById(R.id.step_indicator)).setTextColor(getResources().getColor(R.color.white));
        }
    }

    @Override
    public void collapseAllSteps(Step step) {
        for (HashMap.Entry<Step, View> entry : mBlockStepMap.entrySet()) {
            if (entry.getValue().getTag() != null && !entry.getValue().getTag().equals(step.getStepId())) {
                ((ExpandableLayout) entry.getValue().findViewById(R.id.expandable_layout)).setExpanded(false, true);
            }
        }
    }

    @Override
    public void openStep(Step step) {
        View view = mBlockStepMap.get(step);
        if (view != null) {
            if (!((ExpandableLayout) view.findViewById(R.id.expandable_layout)).isExpanded()) {
                ((ExpandableLayout) view.findViewById(R.id.expandable_layout)).toggle(true);
                if (step.getStepType().equalsIgnoreCase(Constants.STEP_TYPE.BENEFICIARY_FORM) && step.getFormData().getFields().get(2).getValue() == null)
                    registerEcommerceEvent(FirebaseAnalytics.Event.VIEW_ITEM);
            }
        }
    }

    @Override
    public void restoreStepIndicator(Step step) {
        View view = mBlockStepMap.get(step);
        if (view != null) {
            view.findViewById(R.id.step_loading).setVisibility(View.GONE);
            view.findViewById(R.id.circle_indicator_2).setBackground(ContextCompat.getDrawable(this, R.drawable.blue_circle_indicator));
            (view.findViewById(R.id.step_indicator)).setVisibility(View.VISIBLE);
            (view.findViewById(R.id.tick_indicator)).setVisibility(View.GONE);
            ((StyledTextView) view.findViewById(R.id.step_indicator)).setTextColor(getResources().getColor(R.color.blue_color_control));

            StyledTextView sendButton = view.findViewById(R.id.sendButton);

            if (!step.getStepType().equalsIgnoreCase(Constants.STEP_TYPE.DELIVERY_METHOD) &&
                    sendButton != null) {

                if (!step.getStepId().equalsIgnoreCase(Constants.STEP_TAGS.STEP_1) &&
                        !step.getStepId().equalsIgnoreCase(Constants.STEP_TAGS.STEP_2)) {
                    sendButton.setText(R.string.proceed_action);
                } else {
                    sendButton.setText(R.string.done_action);
                }
                sendButton.setEnabled(true);
                sendButton.setAlpha(1f);
            }
        }
    }

    @Override
    public void updateLocationPayoutContentStep(Field field, Step step, String sendingCurrency) {
        View stepView = mBlockStepMap.get(step);
        if (stepView != null) {
            mContentStepManager.updateLocationList(field, stepView, sendingCurrency, step);
        }
    }


    @Override
    public void closeStep(Step step) {
        View view = mBlockStepMap.get(step);
        if (view != null) {
            if (((ExpandableLayout) view.findViewById(R.id.expandable_layout)).isExpanded()) {
                ((ExpandableLayout) view.findViewById(R.id.expandable_layout)).toggle(true);
            }
        }
    }

    @Override
    public void disableStep(Step step) {
        View view = mBlockStepMap.get(step);
        if (view != null) {
            view.findViewById(R.id.disable_view).setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void enableStep(Step step) {
        View view = mBlockStepMap.get(step);
        if (view != null) {
            view.findViewById(R.id.disable_view).setVisibility(View.GONE);
        }
    }


    @Override
    public void togleStep(Step step) {
        View view = mBlockStepMap.get(step);
        if (view != null) {
            ((ExpandableLayout) view.findViewById(R.id.expandable_layout)).toggle(true);
            if (((ExpandableLayout) view.findViewById(R.id.expandable_layout)).isExpanded()) {
                registerEvent("click_expand_step", step.getStepId(), "", deliveryMethod);
                if (step.getStepType().equalsIgnoreCase(Constants.STEP_TYPE.BENEFICIARY_FORM))
                    registerEcommerceEvent(FirebaseAnalytics.Event.VIEW_ITEM);
            } else
                registerEvent("click_collapse_step", step.getStepId(), "", deliveryMethod);
        }
    }

    @Override
    public void drawStepErrorView(final Step step) {
        LinearLayout view = (LinearLayout) mBlockStepMap.get(step);
        if (view != null) {
            ((StyledTextView) view.findViewById(R.id.title)).setText(getString(R.string.an_error_has_occurred));
            ((StyledTextView) view.findViewById(R.id.title)).setTextColor(getResources().getColor(R.color.colorRedError));
            view.findViewById(R.id.circle_indicator_2).setBackground(ContextCompat.getDrawable(this, R.drawable.error_circle_indicator_selected));
            ((StyledTextView) view.findViewById(R.id.step_indicator)).setTextColor(getResources().getColor(R.color.white));
            view.findViewById(R.id.step_indicator).setVisibility(View.VISIBLE);
            (view.findViewById(R.id.pre_line)).setBackgroundColor(getResources().getColor(R.color.colorRedError));
            (view.findViewById(R.id.sub_line)).setBackgroundColor(getResources().getColor(R.color.colorRedError));
        }
    }

    @Override
    public void hideStepErrorView(Step step) {
        LinearLayout view = (LinearLayout) mBlockStepMap.get(step);
        if (view != null) {
            ((StyledTextView) view.findViewById(R.id.title)).setText(TextUtils.isEmpty(step.getStepSelectedItem()) ? step.getName() : step.getStepSelectedItem());
            ((StyledTextView) view.findViewById(R.id.title)).setTextColor(getResources().getColor(R.color.black));
            view.findViewById(R.id.circle_indicator_2).setBackground(ContextCompat.getDrawable(this, R.drawable.blue_light_circle_indicator_selected));
            (view.findViewById(R.id.pre_line)).setBackgroundColor(getResources().getColor(R.color.blue_color_control));
            (view.findViewById(R.id.sub_line)).setBackgroundColor(getResources().getColor(R.color.blue_color_control));
        }
    }

    @Override
    public void updateNewLastStep(Step step) {
        LinearLayout view = (LinearLayout) mBlockStepMap.get(step);
        if (view != null) {
            view.findViewById(R.id.sub_line).setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void showDateRangeSelector(final Field field, final int position, final String stepId, String type, String value) {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(this, R.style.MyDialogTheme, (view, year1, month1, dayOfMonth) -> mPresenter.onDateSelected(position, dayOfMonth, month1 + 1, year1, stepId), year, month, day);

        datePickerDialog.show();

    }

    @Override
    public void resetStepViewStatus() {
        for (Map.Entry<Step, View> entry : mBlockStepMap.entrySet()) {
            entry.getKey().setStatus(StepStatus.EMPTY);
            hideStepErrorView(entry.getKey());
        }
    }

    //TODO uncomment when selfie validation available (user.getAuthenticated() <-> user.getBankwired())
    @Override
    public void checkAuthenticatedUser(final Step step, final User user) {
        CalculatorInteractorImpl calculatorInteractor = CalculatorInteractorImpl.getInstance();
        boolean shownUploadDialog = calculatorInteractor.isUploadDialogShown();
        if (!user.getAuthenticated() && !shownUploadDialog && Objects.equals(user.getCountry().firstKey(), ESP_COUNTRY_VALUE)) {
            calculatorInteractor.setUploadDialogShown(true);
            DialogExtKt.showUploadDocumentDialog(
                    this,
                    this::navigateToSelectDocumentType,
                    () -> CheckoutNavigator.navigateToVerification(this)
            );
        } else {
            trackBeginCheckout();
            mPresenter.goToCheckout(step.getStepSelectedItem());
        }
    }

    private void navigateToSelectDocumentType() {
        ComplianceDocUIModel doc = new ComplianceDocUIModel.FullValidationUIModel();
        Intent i = new Intent(this, DocumentsSelectorActivity.class);
        i.putExtra(DocumentsSelectorActivity.DOCUMENT, doc);
        overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left);
        this.startActivityForResult(i, REQUEST_DOCUMENT_TYPE);
    }

    private void navigateToValidateUserId(String documentType, ComplianceDocUIModel documentUIModel) {
        Intent intent = new Intent(this, DocumentValidationActivity.class);
        intent.putExtra(DocumentValidationActivity.FACE_COMPARE, true);
        intent.putExtra(DocumentValidationActivity.DOCUMENT_TYPE, documentType);
        intent.putExtra(DocumentValidationActivity.DOCUMENT, documentUIModel);
        this.overridePendingTransition(R.anim.from_right_to_left, R.anim.from_position_to_left);
        startActivityForResult(intent, REQUEST_VALIDATE_ID);
    }

    @Override
    public void notifyGlobalChanges(final Step step) {
        switch (step.getStepType()) {
            case Constants.STEP_TYPE.BENEFICIARY_FORM:
                updateBeneficiaryTitle(step, getString(R.string.an_error_has_occurred)); //  getString(R.string.an_error_has_occurred_time_out));
            case Constants.STEP_TYPE.FORM:
                LinearLayout view = (LinearLayout) mBlockStepMap.get(step);
                final RecyclerView recyclerView = Objects.requireNonNull(view).findViewById(R.id.recyclerView);
                if (recyclerView != null) {
                    recyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

                        @Override
                        public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                            recyclerView.removeOnLayoutChangeListener(this);
                            ((FormDataAdapter) Objects.requireNonNull(recyclerView.getAdapter())).setValidatedListeners(true);
                        }
                    });

                    ((FormDataAdapter) Objects.requireNonNull(recyclerView.getAdapter())).notifyGlobalChanges();
                }

                break;
            default:
                break;
        }
    }

    private void updateBeneficiaryTitle(Step step, String title) {
        View view = mBlockStepMap.get(step);
        if (view != null) {
            if (step.getStatus() != null && step.getStatus().equals(StepStatus.ERROR)) {
                ((StyledTextView) view.findViewById(R.id.title)).setText(title);
            } else {
                ((StyledTextView) view.findViewById(R.id.title)).setText(step.getName());
            }
        }
    }


    @Override
    public void enableDisableEditTextListeners(Step step, final boolean enable) {
        switch (step.getStepType()) {
            case Constants.STEP_TYPE.BENEFICIARY_FORM:
            case Constants.STEP_TYPE.FORM: {
                LinearLayout view = (LinearLayout) mBlockStepMap.get(step);
                final RecyclerView recyclerView = Objects.requireNonNull(view).findViewById(R.id.recyclerView);
                if (!enable) {
                    ((FormDataAdapter) Objects.requireNonNull(recyclerView.getAdapter())).setValidatedListeners(false);
                } else {
                    try {
                        mHandler.postDelayed(() -> ((FormDataAdapter) Objects.requireNonNull(recyclerView.getAdapter())).setValidatedListeners(true), 100);
                    } catch (Exception e) {
                        Log.e(TAG, "enableDisableEditTextListeners-------------------------", e);
                    }
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void showProgressDialog(boolean show) {
        if (show && this.mProgressDialog == null) {
            this.mProgressDialog =
                    new DialogExt().showLoadingDialog(this,
                            getString(R.string.progress_dialog_transactional_title),
                            getString(R.string.progress_dialog_transactional_content),
                            true);

        } else if (!show && this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
            this.mProgressDialog = null;
        }
    }

    @Override
    public void showHideCalculatorErroView(boolean show, String errorText) {
        if (!TextUtils.isEmpty(errorText)) {
            mCalculatorErrorText.setText(errorText);
        }
        mCalculatorErrorView.setVisibility(show && mCalculatorFragment == null ? View.VISIBLE : View.GONE);
    }

    @Override
    public void setDeliveryMethodAutoSelectedInAdapter(Step step, Method method) {
        if (step != null) {
            View stepView = mBlockStepMap.get(step);
            if (stepView != null) {
                RecyclerView recyclerDeliveryMethod = stepView.findViewById(R.id.recyclerView);
                if (recyclerDeliveryMethod != null) {
                    ((DeliveryMethodAdapter) Objects.requireNonNull(recyclerDeliveryMethod.getAdapter())).setMethodSelected(method);
                }
            }
        }
    }

    @Override
    public void showQuickReminderPopup(String title, ArrayList<QuickReminderMessage> messages) {
        mPopupContentFrame.setVisibility(View.VISIBLE);
        mQuickReminderPopupFragment = QuickReminderPopupFragment.getInstance(messages, title);
        mQuickReminderPopupFragment.setOnContinueListener(this::closeQuickReminderPopup);

        try {
            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mQuickReminderPopupFragment).commit();
        } catch (Exception ignore) {
        }

    }


    @Override
    public void notifyAddedRemoveFields(Step step, int position, int count, boolean add) {
        switch (step.getStepType()) {
            case Constants.STEP_TYPE.BENEFICIARY_FORM:
            case Constants.STEP_TYPE.FORM: {
                LinearLayout view = (LinearLayout) mBlockStepMap.get(step);
                final RecyclerView recyclerView = Objects.requireNonNull(view).findViewById(R.id.recyclerView);
                ((FormDataAdapter) Objects.requireNonNull(recyclerView.getAdapter())).notifyDataSetChanged();
                break;
            }
            default:
                break;
        }
    }

    @SuppressWarnings({"ToArrayCallWithZeroLengthArrayArgument", "deprecation"})
    @Override
    public void checkAndRequestPermissions() {
        List<String> listPermissionsNeeded = new ArrayList<>();
        int contactsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        if (contactsPermission != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_CONTACTS_PERMISSION_CODE);
        } else {
            mPresenter.permissionsGranted();
        }
        registerEvent("click_add_beneficiary_from_contacts", "", "", deliveryMethod);
    }

    @Override
    public void showStepEmptyView(Step step, String deliveryMethod) {
        LinearLayout stepView = (LinearLayout) mBlockStepMap.get(step);
        if (stepView != null) {
            mContentStepManager.inflateEmptyView(step, stepView, deliveryMethod);
        }
    }


    @Override
    public void updateComboGroupValueData(ArrayList<KeyValueData> keyValuesSelected, Step step, int position) {
        LinearLayout stepView = (LinearLayout) mBlockStepMap.get(step);
        if (stepView != null) {
            switch (step.getStepType()) {
                case Constants.STEP_TYPE.BENEFICIARY_FORM:
                case Constants.STEP_TYPE.FORM:
                    final RecyclerView recyclerView = stepView.findViewById(R.id.recyclerView);
                    if (recyclerView != null) {
                        ((FormDataAdapter) Objects.requireNonNull(recyclerView.getAdapter())).updateField(keyValuesSelected, position);
                        ((FormDataAdapter) recyclerView.getAdapter()).notifySpecificPositionChanges(position);

                        try {
                            mHandler.postDelayed(() -> ((FormDataAdapter) recyclerView.getAdapter()).setValidatedListeners(true), 100);
                        } catch (Exception e) {
                            Log.e("STACK", "----------------------", e);
                        }
                    }
                    break;
            }
        }
    }

    /*
     * ON CLICKS
     */

    @OnClick({R.id.beneficiary_receive_container, R.id.you_pay_container, R.id.calculator_error_view})
    public void onClickCalculator(View view) {
        int side;
        if (view.getId() == R.id.calculator_error_view) {
            side = 0;
        } else {
            side = view.getId() == R.id.beneficiary_receive_container ? 0 : 1;
        }
        mPresenter.clickCalculator(side);
        registerEvent("click_calculator", "", "", deliveryMethod);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CONTACTS_PERMISSION_CODE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted, yay! Do the contacts-related task you need to do.
                mPresenter.permissionsGranted();
            } else {
                // Permission denied, boo! Disable the functionality that depends on this permission.
                mPresenter.permissionsDenied();
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_VALIDATE_ID) {
            CheckoutNavigator.navigateToVerification(this);
        }
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.REQUEST_CODES.GENERIC_DROP_SELECTOR_REQUEST_CODE:
                    KeyValueData keyValueSelected = data.getParcelableExtra(RESULT_DATA);
                    String stepId = data.getStringExtra(CONTENT_STEP_ID);
                    int positionField = data.getIntExtra(CONTENT_FIELD_POSITION, -1);
                    mPresenter.onDropContentSelected(keyValueSelected, stepId, positionField);
                    break;

                case Constants.REQUEST_CODES.INFO_CONTACTS_REQUEST_CODE:
                    mPresenter.onContactSelected(data.getData());
                    break;

                case Constants.REQUEST_CODES.SELECT_LOCATION_REQUEST_CODE:
                    mPresenter.onPayoutLocationSelected((Field) Objects.requireNonNull(data.getExtras()).getParcelable(RESULT_DATA), data.getExtras().getString(STEP_ID_EXTRA, ""));
                    break;

                case Constants.REQUEST_CODES.CHECKOUT_REQUEST_CODE:
                    CreateTransactionResponse transactionResponse = data.getParcelableExtra(TRANSACTION_DATA);
                    boolean showCheckoutDialogExtra = data.getBooleanExtra(SHOW_CHECKOUT_DIALOG_EXTRA, false);
                    Checkout checkout = data.getParcelableExtra(CHECKOUT_DATA);
                    mPresenter.onTransactionFinished(transactionResponse, showCheckoutDialogExtra, checkout);
                    break;

                case REQUEST_DOCUMENT_TYPE:
                    if (data != null) {
                        ComplianceDocUIModel document = (ComplianceDocUIModel) data.getSerializableExtra(DocumentsSelectorActivity.DOCUMENT);
                        String selection = data.getStringExtra(DocumentsSelectorActivity.DOCUMENT_SELECTION);
                        navigateToValidateUserId(selection, document);
                    }
                    break;

                case Constants.REQUEST_CODES.CASH_PICK_UP:
                    mPresenter.onPickUpLocationSelected((CashPickupResultModel) data.getSerializableExtra(RESULT_ITEM));
                    break;
            }

        } else if (resultCode == Activity.RESULT_CANCELED) {
            if (requestCode == Constants.REQUEST_CODES.CHECKOUT_REQUEST_CODE) {
                if (data != null && data.getDataString() != null) {
                    showBannerErrorView(data.getDataString());
                }
            }
        }
    }

    private void showBannerErrorView(String error) {
        new DialogExt().showSingleActionErrorDialog(
                this,
                getString(R.string.generic_error_view_text),
                error,
                null,
                null);
    }

    public void hideComplianceDialog() {
        this.mProgressDialog.dismiss();
        this.mProgressDialog = null;
        finish();
    }

    @Override
    public void trackBeginCheckout() {
        registerCheckoutEvent("checkout_1");
        registerEcommerceEvent(FirebaseAnalytics.Event.BEGIN_CHECKOUT);
    }

    private void registerEcommerceEvent(String eventType) {
        EcommerceCheckoutInfo checkout = mPresenter.getEventInfo();
        trackEvent(new ECommerceEvent(
                STRING_EMPTY,
                checkout.getOriginCountry(),
                checkout.getDestinationCountry(),
                checkout.getDeliveryMethod(),
                checkout.getItemPrice(),
                checkout.getItemCurrency(),
                checkout.getDestinationCurrency(),
                eventType,
                getHierarchy(""),
                "",
                "",
                "",
                ""
        ));
    }

    private void registerCheckoutEvent(String eventType) {
        EcommerceCheckoutInfo checkout = mPresenter.getEventInfo();
        trackEvent(new ECommerceEvent(
                STRING_EMPTY,
                checkout.getOriginCountry(),
                checkout.getDestinationCountry(),
                checkout.getDeliveryMethod(),
                checkout.getItemPrice(),
                checkout.getItemCurrency(),
                checkout.getDestinationCurrency(),
                eventType,
                getHierarchy(""),
                "1",
                checkout.getCoupon(),
                checkout.getDeliveryMethod(),
                mPresenter.payoutName
        ));
    }

    private void registerEvent(String eventAction, String eventLabel, String formType, String processCategory) {
        trackEvent(
                new UserActionEvent(
                        ScreenCategory.TRANSFER.getValue(),
                        eventAction,
                        eventLabel,
                        getHierarchy(""),
                        formType,
                        deliveryMethod,
                        processCategory,
                        "",
                        "",
                        ""
                )
        );
    }

    @Override
    public void onDeliveryMethodSelected(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
        registerEvent("click_select_delivery_method_step1", deliveryMethod, "", deliveryMethod);
    }

    @Override
    public void onFieldClicked(Field field) {
        registerEvent(field.getName(), "", "", deliveryMethod);
    }

    @Override
    public void onStepCompleted(Step validatedStep) {
        String eventAction = "formOk_" + validatedStep.getStepType();
        registerEvent(eventAction, "", "checkout", deliveryMethod);
    }

    @Override
    public void onStepError(Step step, String error) {
        String eventAction = "formKo_" + step.getStepType();
        registerEvent(eventAction, "error_" + error, "checkout", deliveryMethod);
    }

    @Override
    public void onChoosePickUpLocation(CashPickupResultModel cashPickupResultModel) {
        String eventLabel = "";
        String eventAction = "click_choose_pick_up_location";
        if (cashPickupResultModel != null) {
            eventAction = "click_pick_up_location";
            eventLabel = cashPickupResultModel.getRepresentativeName();
        }
        registerEvent(eventAction, eventLabel, "checkout", "pick_up");
    }

    @Override
    public void onClickMore() {
        registerEvent("click_more", "", "checkout", deliveryMethod);
    }

    @Override
    public void onChangeClicked() {
        registerEvent("click_change", "edit_beneficiary_location", "checkout", deliveryMethod);
    }

    @Override
    public void registerAddToCartEvent() {
        registerEcommerceEvent(FirebaseAnalytics.Event.ADD_TO_CART);
    }

    @Override
    public void registerBrazeEvent(String eventName, HashMap<String, String> eventProperties) {
        trackEvent(
                new BrazeEvent(eventName, eventProperties, BrazeEventType.ACTION)
        );
    }

    public void registerTransaction2ndStepBrazeEvent() {
        HashMap<String, String> properties = new HashMap<>();
        properties.put(BrazeEventProperty.DESTINATION_COUNTRY.getValue(), mBeneficiarySelected.getPayoutCountry().getIso3());
        properties.put(BrazeEventProperty.DELIVERY_METHOD.getValue(), deliveryMethod);
        registerBrazeEvent(BrazeEventName.TRANSACTION_CREATION_STEP_2.getValue(), properties);
    }
}
