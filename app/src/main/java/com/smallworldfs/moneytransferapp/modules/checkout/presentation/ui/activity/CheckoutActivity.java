package com.smallworldfs.moneytransferapp.modules.checkout.presentation.ui.activity;

import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity.CONTENT_FIELD_POSITION;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity.RESULT_DATA;
import static com.smallworldfs.moneytransferapp.utils.Constants.COUNTRY.US_COUNTRY_VALUE;
import static com.smallworldfs.moneytransferapp.utils.ConstantsKt.STRING_EMPTY;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.util.Pair;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEvent;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventName;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventProperty;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventType;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ECommerceEvent;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.DialogExt;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.ImageViewExtKt;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.Checkout;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.TransactionErrors;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.TransactionItemValue;
import com.smallworldfs.moneytransferapp.modules.checkout.presentation.navigator.CheckoutNavigator;
import com.smallworldfs.moneytransferapp.modules.checkout.presentation.presenter.CheckoutPresenter;
import com.smallworldfs.moneytransferapp.modules.checkout.presentation.presenter.implementation.CheckoutPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.checkout.presentation.ui.fragment.CheckoutDialogFragment;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;
import com.smallworldfs.moneytransferapp.modules.login.domain.repository.LoginRepository;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.KeyValueData;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.adapter.FormDataAdapter;
import com.smallworldfs.moneytransferapp.presentation.status.transaction.model.TransactionUIModel;
import com.smallworldfs.moneytransferapp.utils.AnimationUtils;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.Log;
import com.smallworldfs.moneytransferapp.utils.Utils;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.hilt.android.AndroidEntryPoint;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by luis on 14/9/17
 */
@AndroidEntryPoint
public class CheckoutActivity extends GenericActivity implements CheckoutPresenter.View {

    private static final String TAG = CheckoutActivity.class.getSimpleName();
    public static final String TRANSACTIONAL_DATA_EXTRA = "TRANSACTIONAL_DATA_EXTRA";
    public static final String CHECKOUT_DATA_EXTRA = "CHECKOUT_DATA_EXTRA";
    public static final String DELIVERY_METHOD = "DELIVERY_METHOD";
    public static final String PAYMENT_METHOD = "PAYMENT_METHOD";
    public static final String PAYER = "PAYER";
    public static final String PAYOUT_NAME = "PAYOUT_NAME";

    @Inject
    LoginRepository loginRepository;

    Unbinder mUnbinder;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.main_nested_scroll_view)
    NestedScrollView mMainNestedScrollView;
    @BindView(R.id.mainContainer)
    RelativeLayout mMainContainer;
    @BindView(R.id.general_loading_view)
    View mGeneralLoadingView;
    @BindView(R.id.top_error_view)
    View mGlobalErrorView;
    @BindView(R.id.error_view)
    View mTopErrorView;
    @BindView(R.id.confirm_button)
    StyledTextView mConfirmButton;
    @BindView(R.id.expandable_layout)
    ExpandableLayout mExpandableLayout;
    @BindView(R.id.expand_collapse_arrow)
    ImageView mExpandCollapseArrow;
    @BindView(R.id.more_button_text)
    StyledTextView mMoreButtonText;
    @BindView(R.id.top_cardview_header)
    CardView mHeaderCardView;
    @BindView(R.id.transaction_info_cardview)
    CardView mTransactionInfoCardView;
    @BindView(R.id.user_name_letter_text)
    StyledTextView mBeneficiaryFirstLetter;
    @BindView(R.id.user_name_letter_text_2)
    StyledTextView mBeneficiarySecondLetter;
    @BindView(R.id.country_flag)
    ImageView mCountryBeneficiaryFlag;
    @BindView(R.id.user_name_textview)
    StyledTextView mBeneficiaryUserName;
    @BindView(R.id.checkout_amount_textview)
    StyledTextView mBeneficiaryAmount;
    @BindView(R.id.delivery_method_textview)
    StyledTextView mDeliverMethodTextView;
    @BindView(R.id.subtotal_textview)
    StyledTextView mSubtotalTextView;
    @BindView(R.id.subtotal_textview_key)
    StyledTextView mSubtotalTextViewKey;
    @BindView(R.id.content_checkout_text_footer)
    StyledTextView mContentCheckoutTextFooter;
    @BindView(R.id.transfer_fee_textview_key)
    StyledTextView mTransferFeeTextViewKey;
    @BindView(R.id.total_pay_textview_title)
    StyledTextView mTotalToPayTextViewKey;
    @BindView(R.id.fee_textview)
    StyledTextView mFeeTextView;
    @BindView(R.id.total_pay_textview)
    StyledTextView mTotalPayTextView;
    @BindView(R.id.delivery_method_append_container)
    LinearLayout mAppendViewsDeliverMethodInfo;
    @BindView(R.id.transaction_append_container)
    LinearLayout mAppendViewsTransactionInfo;
    @BindView(R.id.number_day_transaction)
    StyledTextView mNumberDayTransaction;
    @BindView(R.id.number_month_transaction)
    StyledTextView mNumberMonthTransaction;
    @BindView(R.id.discount_label)
    StyledTextView mDiscountLabel;
    @BindView(R.id.discount_textview)
    StyledTextView mDiscountTextview;
    @BindView(R.id.form_recyclerview)
    RecyclerView mFormRecyclerView;
    @BindView(R.id.content_frame)
    FrameLayout mContentFrame;

    @BindView(R.id.taxes_label_layout)
    LinearLayout taxesLabelLayout;
    @BindView(R.id.taxes_values_layout)
    LinearLayout taxesValuesLayout;

    private Context mContext;
    private CheckoutPresenterImpl mPresenter;
    private boolean mToolbarElevated;
    private Checkout mCheckoutData;
    private FormDataAdapter mAdapter;
    private Handler mHandler;
    private Dialog mProgressDialog;
    private ArrayList<KeyValueData> mTransactionalData;
    private CheckoutDialogFragment mErrorDialogFragment;
    private boolean mExtraPaddingAdded = false;
    private boolean mIsKeyboardShown = false;
    private boolean mKeyboardWillHide = false;
    private String deliveryMethod = "";

    private String paymentMethod = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        mUnbinder = ButterKnife.bind(this);
        mHandler = new Handler(Looper.getMainLooper());

        if (getIntent().getExtras() != null) {
            mTransactionalData = getIntent().getParcelableArrayListExtra(TRANSACTIONAL_DATA_EXTRA);
            mCheckoutData = getIntent().getParcelableExtra(CHECKOUT_DATA_EXTRA);
            deliveryMethod = getIntent().getStringExtra(DELIVERY_METHOD);
            paymentMethod = getIntent().getStringExtra(PAYMENT_METHOD);
        }

        mContext = this;

        mPresenter = new CheckoutPresenterImpl(AndroidSchedulers.mainThread(), Schedulers.io(), this, this, this, mTransactionalData, mCheckoutData);
        mPresenter.create();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.resume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }

        mHandler = null;
        mPresenter.destroy();
        mUnbinder.unbind();
    }

    @Override
    public void onBackPressed() {
        if (this.mErrorDialogFragment == null) {
            super.onBackPressed();
            CheckoutActivity.this.overridePendingTransition(R.anim.from_left_to_right, R.anim.from_position_to_right);
        }
    }

    @Override
    public void configureView() {

        mToolbar.findViewById(R.id.left_title).setVisibility(View.VISIBLE);
        mToolbar.findViewById(R.id.genericToolbarTitle).setVisibility(View.GONE);

        showHideLoadingView(true);
        mTopErrorView.findViewById(R.id.status_bar_padding).setVisibility(View.GONE);

        ((StyledTextView) mToolbar.findViewById(R.id.left_title)).setText(getString(R.string.checkout_name_activity));
        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_back_toolbar_white));

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mToolbar.setElevation(0.0f);
        }

        mToolbarElevated = false;
        mMainNestedScrollView.getViewTreeObserver().addOnScrollChangedListener(new ViewTreeObserver.OnScrollChangedListener() {
            @Override
            public void onScrollChanged() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    int scrollY = mMainNestedScrollView.getScrollY();
                    if (scrollY > 0) {
                        if (!mToolbarElevated) {
                            mToolbar.setElevation(8.0f);
                        }
                        mToolbarElevated = true;
                    } else {
                        if (mToolbarElevated) {
                            mToolbar.setElevation(0.0f);
                        }
                        mToolbarElevated = false;
                    }
                }
            }
        });
    }

    @Override
    public void showHideLoadingView(boolean show) {
        mGeneralLoadingView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void fillDataHeaderInfo(String beneficiaryName, String formattedAmountAndCurrency, String beneficiaryCountry, String beneficiaryFirstLetter, String beneficiarySecondLetter, String deliveryMethod, Pair<String, String> monthDayPair) {

        mBeneficiaryUserName.setText(beneficiaryName);
        mBeneficiaryAmount.setText(formattedAmountAndCurrency);
        ImageViewExtKt.loadCircularImage(
                mCountryBeneficiaryFlag,
                this,
                R.drawable.placeholder_country_adapter,
                Constants.COUNTRY.FLAG_IMAGE_ASSETS + beneficiaryCountry + Constants.COUNTRY.FLAG_IMAGE_EXTENSION
        );
        mBeneficiaryFirstLetter.setText(beneficiaryFirstLetter);
        mBeneficiarySecondLetter.setText(beneficiarySecondLetter);
        mDeliverMethodTextView.setText(deliveryMethod);

        if (monthDayPair != null) {
            mNumberMonthTransaction.setText(monthDayPair.first);
            mNumberDayTransaction.setText(monthDayPair.second);
        }

        mHeaderCardView.setVisibility(View.VISIBLE);
    }

    @Override
    public void fillDataTransactionInfo(String subtotal, String feeFormatted, String totalToPay, ArrayList<TransactionItemValue> deliveryInformation, ArrayList<TransactionItemValue> transactionInformation, ArrayList<TransactionItemValue> transactionTaxes, String promotionAmount, String footer) {

        // If the user country is USA change the string names to CFPB terminology
        User user = loginRepository.getUser();
        if (user != null && user.getCountry().firstKey().equals(US_COUNTRY_VALUE)) {
            mSubtotalTextViewKey.setText(getString(R.string.subtotal_usa));
            mTransferFeeTextViewKey.setText(getString(R.string.fee_text_usa));
            mTotalToPayTextViewKey.setText(getString(R.string.total_to_pay_usa));
        }

        mSubtotalTextView.setText(subtotal);
        mFeeTextView.setText(feeFormatted);

        // Inflate transaction taxes
        if (transactionTaxes != null && transactionTaxes.size() > 0) {
            StyledTextView taxTitle, taxValue;
            for (TransactionItemValue item : transactionTaxes) {
                taxTitle = new StyledTextView(this);
                taxValue = new StyledTextView(this);

                taxTitle.setPaddingRelative(0, 0, 0, 0);
                taxValue.setPaddingRelative(0, 0, 0, 0);

                taxTitle.setEllipsize(TextUtils.TruncateAt.END);
                taxValue.setEllipsize(TextUtils.TruncateAt.END);

                taxTitle.setMaxLines(1);
                taxValue.setMaxLines(1);

                taxTitle.setText(item.getTitle());
                taxValue.setText(item.getValue());

                taxTitle.setIncludeFontPadding(false);
                taxValue.setIncludeFontPadding(false);

                taxTitle.setTextSize(13);
                taxValue.setTextSize(13);

                taxTitle.setTextColor(ContextCompat.getColor(this, R.color.default_text_color));
                taxValue.setTextColor(ContextCompat.getColor(this, R.color.default_text_color));

                taxTitle.setTypeface(taxTitle.getTypeface(), Typeface.BOLD);
                taxValue.setTypeface(taxValue.getTypeface(), Typeface.BOLD);

                taxTitle.setGravity(Gravity.END);
                taxValue.setGravity(Gravity.END);

                taxesLabelLayout.addView(taxTitle);
                taxesValuesLayout.addView(taxValue);
            }
        }

        mTotalPayTextView.setText(totalToPay);

        if (user != null && user.getCountry().firstKey().equals(US_COUNTRY_VALUE)) {
            mDiscountLabel.setText(R.string.discount_label_usa);
        } else {
            mDiscountLabel.setText(R.string.discount_label);
        }

        if (TextUtils.isEmpty(promotionAmount)) {
            // Disable row promotion
            mDiscountLabel.setVisibility(View.GONE);
            mDiscountTextview.setVisibility(View.GONE);
        } else {
            mDiscountTextview.setText(promotionAmount);
        }


        // Inflate deliveryInformation
        if (deliveryInformation != null && deliveryInformation.size() > 0) {
            int position = 0;
            for (TransactionItemValue item : deliveryInformation) {
                LinearLayout transactionItemDetail = (LinearLayout) getLayoutInflater().inflate(R.layout.checkout_item_transaction_details_layout, null, true);
                ((StyledTextView) transactionItemDetail.findViewById(R.id.title)).setText(item.getTitle());
                ((StyledTextView) transactionItemDetail.findViewById(R.id.value)).setText(item.getValue());

                if (position % 2 == 0) {
                    transactionItemDetail.findViewById(R.id.top_item_container).setBackgroundColor(ContextCompat.getColor(this, R.color.default_grey_light_background));
                } else {
                    transactionItemDetail.findViewById(R.id.top_item_container).setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                }

                mAppendViewsDeliverMethodInfo.addView(transactionItemDetail);
                position++;
            }
        }

        // Inflate transaction Info
        if (deliveryInformation != null && transactionInformation.size() > 0) {
            int position = 0;
            for (TransactionItemValue item : transactionInformation) {
                LinearLayout transactionItemDetail = (LinearLayout) getLayoutInflater().inflate(R.layout.checkout_item_transaction_details_layout, null, true);
                ((StyledTextView) transactionItemDetail.findViewById(R.id.title)).setText(item.getTitle());
                ((StyledTextView) transactionItemDetail.findViewById(R.id.value)).setText(item.getValue());

                if (position % 2 == 0) {
                    transactionItemDetail.findViewById(R.id.top_item_container).setBackgroundColor(ContextCompat.getColor(this, R.color.default_grey_light_background));
                } else {
                    transactionItemDetail.findViewById(R.id.top_item_container).setBackgroundColor(ContextCompat.getColor(this, R.color.white));
                }

                mAppendViewsTransactionInfo.addView(transactionItemDetail);
                position++;
            }
        }

        // Show footer if it comes
        if (footer != null && !footer.isEmpty()) {
            mContentCheckoutTextFooter.setVisibility(View.VISIBLE);
            mContentCheckoutTextFooter.setText(footer);
        } else {
            mContentCheckoutTextFooter.setVisibility(View.GONE);
        }

        mTransactionInfoCardView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showHideTotalErrorView(boolean show) {
        mGlobalErrorView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void configureForm(ArrayList<Field> fields) {
        mAdapter = new FormDataAdapter(this, fields);
        mFormRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mFormRecyclerView.setAdapter(mAdapter);
        mFormRecyclerView.setNestedScrollingEnabled(true);

        mAdapter.setFormClickListener(new FormDataAdapter.FormClickListener() {
            @Override
            public void onAttachFileButtonClick(Field field, int position) {

            }

            @Override
            public void onPhoneCountrySelectorClick(Field field, int position) {
                mPresenter.onCountryPhoneClickFormEvent(field, position);
            }

            @Override

            public void onTextSelectableGroupClick(Field field, int position) {
                mPresenter.onCountryPhoneClickFormEvent(field, position);
            }

            @Override
            public void onDateSelectorClick(Field field, int position) {
                mPresenter.showRangeDateSelector(field, position, field.getType(), "");
            }

            @Override
            public void onComboApiClick(Field field, int position) {
                mPresenter.onComboApiDataSelected(field, position);
                checkClickedFieldForAnalytics(field);
            }

            @Override
            public void onComboOwnDataClick(Field field, int position) {
                mPresenter.onComboOwnDataSelected(field, position);
                if (field != null)
                    checkClickedFieldForAnalytics(field);
            }

            @Override
            public void onUploadFileButtonClick(Field field, int position) {
                mPresenter.onAttachSendFileButtonSelected(field, position);
            }

            @Override
            public void onRadioSwitchButtonClick(Field field, int position) {

            }
        });

        mAdapter.setFormErrorListener(new FormDataAdapter.FormErrorListener() {
            @Override
            public void onCheckBoxError(final int fieldPosition) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if ((mFormRecyclerView != null) && (mFormRecyclerView.getLayoutManager() != null)) {
                            View fieldView = mFormRecyclerView.getLayoutManager().findViewByPosition(fieldPosition);
                            if (fieldView != null) {

                                int containerTop = mFormRecyclerView.getTop();
                                int fieldTop = fieldView.getTop();
                                int offset = containerTop + fieldTop;
                                scroolToField(offset);
                            }
                        }
                    }
                }, 200);
            }

            @Override
            public void scrollToRelativeOffsetPosition(final int fieldPosition) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if ((mFormRecyclerView != null) && (mFormRecyclerView.getLayoutManager() != null)) {
                            View fieldView = mFormRecyclerView.getLayoutManager().findViewByPosition(fieldPosition);
                            if (fieldView != null) {

                                int containerTop = mFormRecyclerView.getTop();
                                int fieldTop = fieldView.getTop();
                                int offset = containerTop + fieldTop;
                                scroolToField(offset);
                            }
                        }
                    }
                }, 200);
            }
        });

        mAdapter.setFormEditTextListener(new FormDataAdapter.FormEditTextListener() {
            @Override
            public void onEditTextActionNext() {
            }

            @Override
            public void onEditTextActionDone() {
                removeScrollExtraPadding();
            }

            @Override
            public void onEditTextChangedFocus(boolean hasFocus) {
                if (hasFocus) {
                    mKeyboardWillHide = false;
                    if (!mIsKeyboardShown) {
                        mIsKeyboardShown = true;
                        //addScrollExtraPadding();
                    }
                } else {
                    mKeyboardWillHide = true;

                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mKeyboardWillHide) {
                                mIsKeyboardShown = false;
                                //removeScrollExtraPadding();
                            }
                        }
                    }, 250);
                }
            }
        });
    }

    private void scroolToField(final int topPosition) {

        mMainNestedScrollView.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mMainNestedScrollView != null) {
                    final int offset = topPosition - 400;
                    mMainNestedScrollView.fling(0);
                    mMainNestedScrollView.smoothScrollTo(0, offset);
                }

            }
        }, 50);
    }

    public void addScrollExtraPadding() {
        Handler mainHandler = new Handler(mContext.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                if (mContext != null) {
                    final int offsetPixels = (int) Utils.getDpInPixels(400f, mContext);
                    if (mMainContainer != null && !mExtraPaddingAdded) {
                        mMainContainer.setPadding(0, 0, 0, offsetPixels);
                        mExtraPaddingAdded = true;
                    }
                }
            }
        };
        mainHandler.post(myRunnable);
    }

    public void removeScrollExtraPadding() {
        Handler mainHandler = new Handler(mContext.getMainLooper());
        Runnable myRunnable = new Runnable() {
            @Override
            public void run() {
                if (mContext != null) {
                    final int offsetPixels = (int) Utils.getDpInPixels(0f, mContext);
                    if (mMainContainer != null && mExtraPaddingAdded) {
                        mMainContainer.setPadding(0, 0, 0, offsetPixels);
                        mExtraPaddingAdded = false;
                    }
                }
            }
        };
        mainHandler.post(myRunnable);
    }

    @Override
    public void updateComboGroupValueData(ArrayList<KeyValueData> values, int positionField) {
        if (mAdapter != null) {
            mAdapter.updateField(values, positionField);
            mAdapter.notifySpecificPositionChanges(positionField);
            trackEcommerceFieldValueSelected(mAdapter.getFieldByPosition(positionField));
            try {
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (mAdapter != null) {
                            mAdapter.setValidatedListeners(true);
                        }
                    }
                }, 100);
            } catch (Exception e) {
                Log.e("STACK", "----------------------", e);
            }
        }
    }

    private void trackEcommerceFieldValueSelected(Field field) {
        if (mCheckoutData != null) {
            switch (field.getName()) {
                case "sourceoffunds":
                    registerEvent("checkout_2", 2, field.getKeyValue());
                    registerEvent(FirebaseAnalytics.Event.ADD_SHIPPING_INFO, 2, field.getKeyValue());
                    break;
                case "paymentMethod":
                    registerEvent("checkout_3", 3, field.getKeyValue());
                    registerEvent(FirebaseAnalytics.Event.ADD_PAYMENT_INFO, 3, field.getKeyValue());
                    break;
            }
        }
    }

    private void registerEvent(String eventType, Integer funnelStep, String transferOption) {
        String deliveryMethod = mCheckoutData.getTransactionSummary().getDeliveryMethod() != null ? mCheckoutData.getTransactionSummary().getDeliveryMethod() : "";
        ECommerceEvent event = new ECommerceEvent(
                STRING_EMPTY,
                mPresenter.getUser().getCountry().firstKey() != null ? mPresenter.getUser().getCountry().firstKey() : "",
                mCheckoutData.getTransactionSummary().getCountry() != null ? mCheckoutData.getTransactionSummary().getCountry() : "",
                deliveryMethod,
                !String.valueOf(mCheckoutData.getTransactionDetails().getTotalToPay()).equals("") ? String.valueOf(mCheckoutData.getTransactionDetails().getTotalToPay()) : "",
                mCheckoutData.getTransactionDetails().getCurrencyOrigin() != null ? mCheckoutData.getTransactionDetails().getCurrencyOrigin() : "",
                mCheckoutData.getTransactionSummary().getCurrency() != null ? mCheckoutData.getTransactionSummary().getCurrency() : "",
                eventType,
                getHierarchy(""),
                funnelStep.toString(),
                mCheckoutData.getTransactionSummary().getPromotionName() != null ? mCheckoutData.getTransactionSummary().getPromotionName() : "",
                deliveryMethod,
                transferOption
        );
        trackEvent(event);
    }


    @Override
    public void showProgressDialog(boolean show, String title, String content, boolean showTitle) {
        if (show && this.mProgressDialog == null) {
            this.mProgressDialog =
                    new DialogExt().showLoadingDialog(this, title, content, showTitle);
        } else if (!show && this.mProgressDialog != null && this.mProgressDialog.isShowing()) {
            this.mProgressDialog.dismiss();
            this.mProgressDialog = null;
        }
    }

    @Override
    public void showTopErrorView() {
        showErrorView(getString(R.string.generic_title_top_error_view_empty_fields), getString(R.string.generic_subtitle_top_error_view_empty_fields), mTopErrorView);
        registerEvent("formKo", "checkout");
        registerTransactionKoBrazeEvent();
    }

    @Override
    public void showTopServerErrorView() {
        showErrorView(getString(R.string.generic_title_top_error_view), getString(R.string.generic_subtitle_top_error_view), mTopErrorView);
        registerEvent("formKo", "checkout");
        registerTransactionKoBrazeEvent();
    }

    @Override
    public void hideTopErrorView() {
        hideErrorView(mTopErrorView);
    }

    @Override
    public void styleConfirmButtonInRetryMode() {
        mConfirmButton.setText(getString(R.string.retry_confirm));
        mConfirmButton.setBackground(ContextCompat.getDrawable(this, R.drawable.orange_border_button));
        if (Build.VERSION.SDK_INT < 23) {
            mConfirmButton.setTextAppearance(this, R.style.orange_border_button_style);
        } else {
            mConfirmButton.setTextAppearance(R.style.orange_border_button_style);
        }
    }

    @Override
    public void enableDisableEditTextListeners(boolean enable) {
        if (mAdapter != null && mFormRecyclerView != null) {
            if (!enable) {
                mAdapter.setValidatedListeners(false);
            } else {
                try {
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mAdapter != null) {
                                mAdapter.setValidatedListeners(true);
                            }
                        }
                    }, 100);
                } catch (Exception e) {
                    Log.e("STACK", "----------------------", e);
                }
            }
        }
    }

    @Override
    public void notifyAddedRemoveFields(int position, int positionIncreased, boolean add) {
        if (mFormRecyclerView != null && mAdapter != null) {
            if (!add) {
                mAdapter.notifyItemDeleted(position);
            } else {
                mAdapter.notifyItemsAdded(position, positionIncreased);
            }
        }
    }

    @Override
    public void showAlertErrorView(String title, String description) {
        registerEvent("formKo", "checkout");
        registerTransactionKoBrazeEvent();
        new DialogExt().showSingleActionErrorDialog(this, title, description, null);
    }

    @Override
    public void checkBankWire() {
        Field field = mAdapter.getFieldByName("paymentMethod");
        if (field != null) {
            for (TreeMap<String, String> data : field.getData()) {
                if (data.containsKey(this.paymentMethod)) {
                    field.setValue(data.get(this.paymentMethod));
                    field.setKeyValue("BANKWIRE");
                    mPresenter.checkIfShouldRequestMoreFields(mAdapter.getFields().indexOf(field));
                }
            }
        }
    }

    @Override
    public void transactionSuccess() {
        Map<String, String> brazeEventProperties = new HashMap<>();
        Field paymentMethodField = mAdapter.getFieldByName("paymentMethod");

        brazeEventProperties.put(
                BrazeEventProperty.DESTINATION_COUNTRY.getValue(),
                mCheckoutData.getTransactionSummary().getCountry() != null ? mCheckoutData.getTransactionSummary().getCountry() : ""
        );
        brazeEventProperties.put(
                BrazeEventProperty.BENEFICIARY_FULL_NAME.getValue(),
                mCheckoutData.getTransaction().getBeneficiaryId() != null ? mCheckoutData.getTransaction().getBeneficiaryId() : ""
        );
        brazeEventProperties.put(
                BrazeEventProperty.PAYER.getValue(),
                getIntent().getStringExtra(PAYER) != null ? getIntent().getStringExtra(PAYER) : ""
        );
        brazeEventProperties.put(
                BrazeEventProperty.DELIVERY_METHOD.getValue(),
                mCheckoutData.getTransactionSummary().getDeliveryMethod() != null ? mCheckoutData.getTransactionSummary().getDeliveryMethod() : ""
        );
        brazeEventProperties.put(
                BrazeEventProperty.PAYMENT_METHOD.getValue(),
                paymentMethodField.getValue() != null ? paymentMethodField.getValue() : ""
        );

        registerBrazeEvent(BrazeEventName.TRANSACTION_CREATED.getValue(), brazeEventProperties);
    }

    @Override
    public void showCreateTransactionFragmentErrors(ArrayList<TransactionErrors> errors) {
        try {
            registerEvent("formKo", "checkout");
            registerTransactionKoBrazeEvent();

            mContentFrame.setVisibility(View.VISIBLE);

            mErrorDialogFragment = CheckoutDialogFragment.getInstance(Constants.DIALOG_CHECKOUT_STYLE.ERROR_STYLE, errors, null, null);
            mErrorDialogFragment.setCloseListener(new CheckoutDialogFragment.CloseListener() {
                @Override
                public void closeDialog() {
                    closeDialogFragment();
                }

                @Override
                public void checkErrorsNow() {
                    closeDialogFragment();
                    finish();
                    CheckoutActivity.this.overridePendingTransition(R.anim.from_left_to_right, R.anim.from_position_to_right);
                }

                @Override
                public void closeDialogAndShowTransactions(Transaction transaction) {
                    // Not aplicate
                }

                @Override
                public void closeDialogAndShowTransactions(TransactionUIModel transaction) {

                }

                @Override
                public void closeAndRequestHelpEmail() {
                    closeDialogFragment();
                    mPresenter.sendHelpEmail();
                }

                @Override
                public void closeAndGoToPayNow(String mtn) {
                    // Not aplicate
                }
            });

            getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, mErrorDialogFragment).commit();
        } catch (Exception e) {
            Log.e(TAG, "showCreateTransactionFragmentErrors: Blame static mContext ?", e);
        }
    }

    @Override
    public void notifyGlobalChanges() {
        if (mFormRecyclerView != null && mAdapter != null) {
            mFormRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {

                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                    mFormRecyclerView.removeOnLayoutChangeListener(this);
                    mAdapter.setValidatedListeners(true);
                }
            });

            mAdapter.notifyGlobalChanges();
        }
    }

    @OnClick(R.id.more_button_expand)
    public void onMoreButtonClick() {
        mExpandableLayout.toggle(true);
        String click = "";
        if (mExpandableLayout.isExpanded()) {
            AnimationUtils.rotateView(mExpandCollapseArrow, 300, 0);
            mMoreButtonText.setText(getString(R.string.less_text_button));
            click = "click_more";
        } else {
            AnimationUtils.rotateView(mExpandCollapseArrow, 300, 180);
            mMoreButtonText.setText(getString(R.string.more_text_button));
            click = "click_less";
        }
        registerEvent(click, "checkout");
    }

    @OnClick(R.id.button_text_retry)
    public void onRetryButtonClick() {
        mPresenter.onRetryButtonClick();
    }

    @OnClick(R.id.confirm_button)
    public void onConfirmButtonClick() {
        registerEvent("click_confirm", "");
        if (mCheckoutData != null) {

            trackEvent(new ECommerceEvent(
                    "",
                    mPresenter.getUser().getCountry().firstKey() != null ? mPresenter.getUser().getCountry().firstKey() : "",
                    mCheckoutData.getTransactionSummary().getCountry() != null ? mCheckoutData.getTransactionSummary().getCountry() : "",
                    mCheckoutData.getTransactionSummary().getDeliveryMethod() != null ? mCheckoutData.getTransactionSummary().getDeliveryMethod() : "",
                    !String.valueOf(mCheckoutData.getTransactionDetails().getTotalToPay()).equals("") ? String.valueOf(mCheckoutData.getTransactionDetails().getTotalToPay()) : "",
                    mCheckoutData.getTransactionDetails().getCurrencyOrigin() != null ? mCheckoutData.getTransactionDetails().getCurrencyOrigin() : "",
                    mCheckoutData.getTransactionSummary().getCurrency() != null ? mCheckoutData.getTransactionSummary().getCurrency() : "",
                    "checkout_4",
                    getHierarchy(""),
                    "4",
                    mCheckoutData.getTransactionSummary().getPromotionName() != null ? mCheckoutData.getTransactionSummary().getPromotionName() : "",
                    mCheckoutData.getTransactionSummary().getDeliveryMethod() != null ? mCheckoutData.getTransactionSummary().getDeliveryMethod() : "",
                    ""
            ));

        }
        mPresenter.onConfirmButtonClick();
    }


    private void closeDialogFragment() {
        if (this.mErrorDialogFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(mErrorDialogFragment);
            this.mErrorDialogFragment = null;

            mContentFrame.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODES.GENERIC_DROP_SELECTOR_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                KeyValueData keyValueSelected = data.getParcelableExtra(RESULT_DATA);
                int positionField = data.getIntExtra(CONTENT_FIELD_POSITION, -1);

                mPresenter.onDropContentSelected(keyValueSelected, positionField);
            }
        }
        if (requestCode == Constants.REQUEST_CODES.TRANSACTION_UPLOAD_DOCUMENTS_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_CANCELED) {
                finish();
            } else if (resultCode == Activity.RESULT_OK) {
                checkBankWire();
            }
        }
    }

    @Override
    public void showFlinksValidation() {
        CheckoutNavigator.navigateToFlinksValidation(this);
    }

    @Override
    public void showMessageEmailSent() {
        new DialogExt().showSingleActionInfoDialog(this, getString(R.string.action_done_transactional_calculator),
                getString(R.string.email_sent_successfully), null, null, "");
    }

    @Override
    public void sendCheckoutAnalytics(Checkout checkout) {

    }

    @Override
    public void onCheckoutSuccess() {
        registerEvent("formOk", "checkout");
    }

    @Override
    public void updateCheckoutData(Checkout checkoutData) {
        mCheckoutData = checkoutData;
    }

    @Override
    public void showDateRangeSelector(Field field, int position, String type, String value) {
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog =
                new DatePickerDialog(this, R.style.MyDialogTheme, (view, year1, month1, dayOfMonth) -> mPresenter.onDateSelected(position, dayOfMonth, month1 + 1, year1), year, month, day);

        datePickerDialog.show();
    }

    @Override
    public String getPaymentMethod() {
        return paymentMethod;
    }

    private void checkClickedFieldForAnalytics(Field field) {
        if (field != null && field.getName() != null) {
            String eventAction = "click_" + field.getName();
            registerEvent(eventAction, "");
        }
    }

    private void registerTransactionKoBrazeEvent() {
        Map<String, String> brazeEventProperties = new HashMap<>();
        Field paymentMethodField = mAdapter.getFieldByName("paymentMethod");

        brazeEventProperties.put(
                BrazeEventProperty.DESTINATION_COUNTRY.getValue(),
                mCheckoutData.getTransactionSummary().getCountry() != null ? mCheckoutData.getTransactionSummary().getCountry() : ""
        );
        brazeEventProperties.put(
                BrazeEventProperty.BENEFICIARY_FULL_NAME.getValue(),
                mCheckoutData.getTransaction().getBeneficiaryId() != null ? mCheckoutData.getTransaction().getBeneficiaryId() : ""
        );
        brazeEventProperties.put(
                BrazeEventProperty.PAYER.getValue(),
                getIntent().getStringExtra(PAYER) != null ? getIntent().getStringExtra(PAYER) : ""
        );
        brazeEventProperties.put(
                BrazeEventProperty.DELIVERY_METHOD.getValue(),
                mCheckoutData.getTransactionSummary().getDeliveryMethod() != null ? mCheckoutData.getTransactionSummary().getDeliveryMethod() : ""
        );
        brazeEventProperties.put(
                BrazeEventProperty.PAYMENT_METHOD.getValue(),
                paymentMethodField.getValue() != null ? paymentMethodField.getValue() : ""
        );

        brazeEventProperties.put(
                BrazeEventProperty.PAID_AMOUNT.getValue(),
                String.valueOf(mCheckoutData.getTransactionDetails().getTotalToPay())
        );

        registerBrazeEvent(BrazeEventName.TRANSACTION_KO.getValue(), brazeEventProperties);
    }

    private void registerEvent(String eventAction, String formType) {
        String checkoutStep = "";
        if (mCheckoutData != null)
            checkoutStep = mCheckoutData.getTransactionSummary().getDeliveryMethod();
        else
            checkoutStep = deliveryMethod;
        trackEvent(new UserActionEvent(
                ScreenCategory.TRANSFER.getValue(),
                eventAction,
                "",
                getHierarchy(""),
                "",
                formType,
                checkoutStep,
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
}
