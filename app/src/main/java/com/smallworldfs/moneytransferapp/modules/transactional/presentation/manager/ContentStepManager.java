package com.smallworldfs.moneytransferapp.modules.transactional.presentation.manager;

import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.TransactionalActivity.CONTENT_APPEND_TAG;
import static com.smallworldfs.moneytransferapp.utils.Constants.CONFIGURATION.REPRESENTATIVE_CODE_LOCATION_SEPARATOR;
import static com.smallworldfs.moneytransferapp.utils.ConstantsKt.STRING_EMPTY;
import static com.smallworldfs.moneytransferapp.utils.ConstantsKt.STRING_NEW_LINE;
import static com.smallworldfs.moneytransferapp.utils.ConstantsKt.STRING_SPACE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.RecyclerView;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.databinding.TransactionalStepLocationListCashPickUpNewLayoutBinding;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.interactor.implementation.CalculatorInteractorImpl;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.Method;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.GenericFormField;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.structure.Step;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter.implementation.TransactionalPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.TransactionalActivity;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.adapter.DeliveryMethodAdapter;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.adapter.FormDataAdapter;
import com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.utils.CashPickupResultModel;
import com.smallworldfs.moneytransferapp.utils.AmountFormatter;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.KeyboardUtils;
import com.smallworldfs.moneytransferapp.utils.Log;
import com.smallworldfs.moneytransferapp.utils.Utils;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;

public class ContentStepManager {
    private static final String TAG = ContentStepManager.class.getSimpleName();

    private final Context mContext;
    private final Activity mActivity;
    private final TransactionalPresenterImpl mPresenter;
    private Field mLocationField;
    private final boolean mHideSendButton;
    private RecyclerView.Adapter<RecyclerView.ViewHolder> mDeliveryMethodAdapter;

    private NestedScrollView mMainNestedScrollView;
    private LinearLayout mMainContainer;
    private boolean mExtraPaddingAdded = false;
    private boolean mIsKeyboardShown = false;
    private boolean mKeyboardWillHide = false;

    private final Handler mShouldHideKeyboardHandler;
    private Runnable mShouldHideKeyboardRunnable = null;
    private View locationView = null;

    public ContentStepManager(Context context, Activity activity, TransactionalPresenterImpl presenter) {
        this.mContext = context;
        this.mActivity = activity;
        this.mPresenter = presenter;
        this.mHideSendButton = false;
        mShouldHideKeyboardHandler = new Handler(Looper.getMainLooper());
    }

    public View getLocationView() {
        return locationView;
    }


    //// //// //// //// //// //// //// ////
    // STEP 2
    //// //// //// //// //// //// //// ////


    /**
     * Inflate Delivery Method Content
     */
    public View inflateDeliveryMethod(final Step step, ArrayList<? extends GenericFormField> data, boolean shouldShowChangeDeliveryMethod) {
        @SuppressLint("InflateParams") View stepLayout = LayoutInflater.from(mContext).inflate(R.layout.transactional_delivery_step_content_layout, null, false);
        stepLayout.setTag(CONTENT_APPEND_TAG + step.getStepId());

        RecyclerView recyclerView = stepLayout.findViewById(R.id.recyclerView);
        DeliveryMethodAdapter adapter = new DeliveryMethodAdapter(mContext, data, shouldShowChangeDeliveryMethod);
        adapter.setDeliveryMethodListener(method -> mPresenter.onSelectDeliveryMethodClick(step, method));
        recyclerView.setAdapter(adapter);
        mDeliveryMethodAdapter = adapter;
        return stepLayout;
    }


    /**
     * Inflate Generic Form Content
     */
    public View inflateGenericForm(final Step step, ArrayList<? extends GenericFormField> data, final View blockStepContent) {
        @SuppressLint("InflateParams") final View stepLayout = LayoutInflater.from(mContext).inflate(R.layout.transactional_form_step_content_layout, null, false);
        stepLayout.setTag(CONTENT_APPEND_TAG + step.getStepId());

        final RecyclerView recyclerView = stepLayout.findViewById(R.id.recyclerView);
        final FormDataAdapter adapter = new FormDataAdapter(mContext, data);
        recyclerView.setAdapter(adapter);
        recyclerView.setPadding(0, Utils.dpToPx(16), 0, 0);

        StyledTextView sendButton = stepLayout.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(view -> {
            KeyboardUtils.hideKeyboard(mContext, mActivity.getCurrentFocus());
            registerTransaction2ndStepBrazeEvent();
            // Data collector
            mPresenter.onSubmitFormClick(step);
        });

        adapter.setFormClickListener(new FormDataAdapter.FormClickListener() {
            @Override
            public void onAttachFileButtonClick(Field field, int position) {
            }

            @Override
            public void onPhoneCountrySelectorClick(Field field, int position) {
                mPresenter.onCountryPhoneClickFormEvent(field, position, step);
            }

            @Override
            public void onTextSelectableGroupClick(Field field, int position) {
                mPresenter.onCountryPhoneClickFormEvent(field, position, step);
            }

            @Override
            public void onDateSelectorClick(Field field, int position) {
                mPresenter.showRangeDateSelector(step.getStepId(), field, position, field.getType(), field.getValue());
            }

            @Override
            public void onComboApiClick(Field field, int position) {
                mPresenter.onComboApiDataSelected(field, position, step);
            }

            @Override
            public void onComboOwnDataClick(Field field, int position) {
                mPresenter.onComboOwnDataSelected(field, position, step);
            }

            @Override
            public void onUploadFileButtonClick(Field field, int position) {
                mPresenter.onAttachSendFileButtonSelected(field, position);
            }

            @Override
            public void onRadioSwitchButtonClick(Field field, int position) {
            }
        });

        adapter.setFormEditTextListener(new FormDataAdapter.FormEditTextListener() {
            @Override
            public void onEditTextActionNext() {
                mShouldHideKeyboardRunnable = () -> {
                };
                mShouldHideKeyboardHandler.postDelayed(mShouldHideKeyboardRunnable, 500);
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
                        addScrollExtraPadding();
                    }
                } else {
                    mKeyboardWillHide = true;

                    new Handler().postDelayed(() -> {
                        if (mKeyboardWillHide) {
                            mIsKeyboardShown = false;
                            removeScrollExtraPadding();
                        }
                    }, 250);
                }
            }
        });

        ((FormDataAdapter) recyclerView.getAdapter()).setFormErrorListener(new FormDataAdapter.FormErrorListener() {
            @Override
            public void onCheckBoxError(final int fieldPosition) {
                if ((recyclerView != null) && (recyclerView.getLayoutManager() != null)) {
                    new Handler().postDelayed(() -> {
                        View fieldView = recyclerView.getLayoutManager().findViewByPosition(fieldPosition);
                        if (fieldView != null) {
                            int containerTop = 0;
                            if (blockStepContent != null) {
                                containerTop = blockStepContent.getTop();
                            }
                            int fieldTop = fieldView.getTop();
                            int offset = containerTop + fieldTop;
                            scrollToField(offset);
                        }
                    }, 200);
                }
            }

            @Override
            public void scrollToRelativeOffsetPosition(final int fieldPosition) {

                if ((recyclerView != null) && (recyclerView.getLayoutManager() != null)) {
                    new Handler().postDelayed(() -> {
                        View fieldView = recyclerView.getLayoutManager().findViewByPosition(fieldPosition);
                        if (fieldView != null) {
                            int containerTop = 0;
                            if (blockStepContent != null) {
                                containerTop = blockStepContent.getTop();
                            }
                            int fieldTop = fieldView.getTop();
                            int offset = containerTop + fieldTop;
                            scrollToField(offset);
                        }
                    }, 200);
                }
            }
        });

        // Avoid wrong scroll
        recyclerView.setNestedScrollingEnabled(false);

        return stepLayout;
    }

    private void scrollToField(final int topPosition) {
        mMainNestedScrollView.postDelayed(() -> {
            mMainNestedScrollView.fling(0);
            mMainNestedScrollView.smoothScrollTo(0, topPosition);

            mShouldHideKeyboardRunnable = () -> {
            };
            mShouldHideKeyboardHandler.postDelayed(mShouldHideKeyboardRunnable, 500);
        }, 50);
    }

    /**
     * Inflate Beneficiary Form Content
     */
    public View inflateBeneficiaryForm(final Step step, ArrayList<? extends GenericFormField> data, final View blockStepContent) {
        @SuppressLint("InflateParams") final View stepLayout = LayoutInflater.from(mContext).inflate(R.layout.transactional_beneficiary_form_step_content_layout, null, false);
        stepLayout.setTag(CONTENT_APPEND_TAG + step.getStepId());

        try {
            for (GenericFormField f : data) {
                Field f2 = (Field) f;
                if (f2.getSubtype() != null && f2.getSubtype().equals(Constants.FIELD_SUBTYPES.PHONE)) {
                    Field phoneCountry = f2.getChilds().get(0);
                    if (phoneCountry.getValue() == null) {
                        String country = CalculatorInteractorImpl.getInstance().getPayoutCountryKey();
                        phoneCountry.setValue(country);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "inflateBeneficiaryForm:e:--------------------------------", e);
        }

        final RecyclerView recyclerView = stepLayout.findViewById(R.id.recyclerView);
        final FormDataAdapter adapter = new FormDataAdapter(mContext, data);
        recyclerView.setAdapter(adapter);

        StyledTextView sendButton = stepLayout.findViewById(R.id.sendButton);
        sendButton.setOnClickListener(view -> {
            KeyboardUtils.hideKeyboard(mContext, mActivity.getCurrentFocus());
            // Data collector
            registerTransaction2ndStepBrazeEvent();
            mPresenter.onSubmitFormClick(step);
            // Clear prev focus
            stepLayout.findViewById(R.id.clear_focus_view).requestFocus();

            mPresenter.registerAddToCartEcommerceEvent();
        });

        if (this.mHideSendButton) {
            sendButton.setVisibility(View.GONE);
        }

        StyledTextView readInfFromContacts = stepLayout.findViewById(R.id.addFromContactsButton);
        readInfFromContacts.setOnClickListener(view -> {
            // Data collector
            mPresenter.onRequestInfoFromContacts();
        });


        adapter.setFormClickListener(new FormDataAdapter.FormClickListener() {
            @Override
            public void onAttachFileButtonClick(Field field, int position) {
            }

            @Override
            public void onPhoneCountrySelectorClick(Field field, int position) {
                mPresenter.onCountryPhoneClickFormEvent(field, position, step);
            }

            @Override
            public void onTextSelectableGroupClick(Field field, int position) {
                mPresenter.onCountryPhoneClickFormEvent(field, position, step);
            }

            @Override
            public void onDateSelectorClick(Field field, int position) {
                mPresenter.showRangeDateSelector(step.getStepId(), field, position, field.getType(), field.getValue());
            }

            @Override
            public void onComboApiClick(Field field, int position) {
                mPresenter.onComboApiDataSelected(field, position, step);
            }

            @Override
            public void onComboOwnDataClick(Field field, int position) {
                mPresenter.onComboOwnDataSelected(field, position, step);
            }

            @Override
            public void onUploadFileButtonClick(Field field, int position) {
                mPresenter.onAttachSendFileButtonSelected(field, position);
            }

            @Override
            public void onRadioSwitchButtonClick(Field field, int position) {
            }
        });

        adapter.setFormEditTextListener(new FormDataAdapter.FormEditTextListener() {
            @Override
            public void onEditTextActionNext() {
                mShouldHideKeyboardRunnable = () -> {
                };
                mShouldHideKeyboardHandler.postDelayed(mShouldHideKeyboardRunnable, 500);
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
                        addScrollExtraPadding();
                    }
                } else {
                    mKeyboardWillHide = true;

                    new Handler().postDelayed(() -> {
                        if (mKeyboardWillHide) {
                            mIsKeyboardShown = false;
                            removeScrollExtraPadding();
                            KeyboardUtils.hideKeyboard(mContext, mActivity.getCurrentFocus());
                        }
                    }, 250);
                }
            }
        });

        ((FormDataAdapter) recyclerView.getAdapter()).setFormErrorListener(new FormDataAdapter.FormErrorListener() {
            @Override
            public void onCheckBoxError(final int fieldPosition) {
                if ((recyclerView != null) && (recyclerView.getLayoutManager() != null)) {
                    new Handler().postDelayed(() -> {
                        View fieldView = recyclerView.getLayoutManager().findViewByPosition(fieldPosition);
                        if (fieldView != null) {
                            int containerTop = 0;
                            if (blockStepContent != null) {
                                containerTop = blockStepContent.getTop();
                            }
                            int fieldTop = fieldView.getTop();
                            int offset = containerTop + fieldTop;
                            scrollToField(offset);
                        }
                    }, 200);
                }
            }

            @Override
            public void scrollToRelativeOffsetPosition(final int fieldPosition) {

                if ((recyclerView != null) && (recyclerView.getLayoutManager() != null)) {
                    new Handler().postDelayed(() -> {
                        View fieldView = recyclerView.getLayoutManager().findViewByPosition(fieldPosition);
                        if (fieldView != null) {
                            int containerTop = 0;
                            if (blockStepContent != null) {
                                containerTop = blockStepContent.getTop();
                            }
                            int fieldTop = fieldView.getTop();
                            int offset = containerTop + fieldTop;
                            scrollToField(offset);
                        }
                    }, 200);
                }
            }
        });

        // Avoid wrong scroll
        recyclerView.setNestedScrollingEnabled(false);

        return stepLayout;
    }

    public void addScrollExtraPadding() {
        Handler mainHandler = new Handler(mContext.getMainLooper());
        Runnable myRunnable = () -> {
            final int offsetPixels = (int) Utils.getDpInPixels(300f, mContext);
            if (mMainContainer != null && !mExtraPaddingAdded) {
                mMainContainer.setPadding(0, 0, 0, offsetPixels);
                mExtraPaddingAdded = true;
            }
        };
        mainHandler.post(myRunnable);
    }

    public void removeScrollExtraPadding() {
        Handler mainHandler = new Handler(mContext.getMainLooper());
        Runnable myRunnable = () -> {
            final int offsetPixels = (int) Utils.getDpInPixels(0f, mContext);
            if (mMainContainer != null && mExtraPaddingAdded) {
                mMainContainer.setPadding(0, 0, 0, offsetPixels);
                mExtraPaddingAdded = false;
            }
        };
        mainHandler.post(myRunnable);
    }


    //// //// //// //// //// //// //// ////
    // STEP 3
    //// //// //// //// //// //// //// ////
    public View inflateLocationList(Step step, View stepView, String currentYouPayAmount) {
        if (step != null && step.getFormData() != null && step.getFormData().getFields() != null && step.getFormData().getFields().size() > 0) {
            Field field = step.getFormData().getFields().get(0);
            if (field != null && stepView != null && field.getPayout() != null) {
                deletePrevContentIfExist(stepView);
                switch (field.getSubtype()) {
                    case Constants.FIELD_SUBTYPES.LOCATION_PICK_UP:
                        return inflateLocationListCashPickUpView(field, stepView, currentYouPayAmount, step.getFormData().getFields(), step);
                    case Constants.FIELD_SUBTYPES.LOCATION_BANK_DEPOSIT:
                        return inflateLocationListBankDepositView(field, stepView, currentYouPayAmount, step.getFormData().getFields(), step);
                    case Constants.FIELD_SUBTYPES.LOCATION_MOBILE_TOP_UP:
                        return inflateMobileTopUpLocationListView(field, currentYouPayAmount, step);
                    case Constants.FIELD_SUBTYPES.LOCATION_HOME_DELIVERY:
                        return inflateHomeDeliveryLocationListView(field, step.getFormData().getFields(), step);
                    case Constants.FIELD_SUBTYPES.LOCATION_CASH_CARD_RELOAD:
                        return inflateCashCardReloadLocationListView(field, step);
                    case Constants.FIELD_SUBTYPES.LOCATION_ON_CALL:
                        return inflateOnCallLocationListView(field, step);
                    case Constants.FIELD_SUBTYPES.LOCATION_MOBILE_WALLET:
                        return inflateMobileWalletLocationListView(field, step, step.getFormData().getFields());
                }
            }
        }
        return null;
    }


    /**
     * Inflate pick up location step
     */
    public View inflatePickUpLocationStep(CashPickupResultModel cashPickupResultModel, Step step) {
        // Get the UI layout
        TransactionalStepLocationListCashPickUpNewLayoutBinding binding = TransactionalStepLocationListCashPickUpNewLayoutBinding.inflate(LayoutInflater.from(mContext), null, false);

        // Set choose location listener
        binding.transactionalStepLocationListCashPickUpLayoutConstraintLayoutChooseLocation.setOnClickListener(v ->
                mPresenter.onActionChooseLocationForPickUp(false, cashPickupResultModel)
        );

        // If there is an object selected, draw it
        binding.transactionalStepLocationListCashPickUpLayoutConstraintLayoutLocationSelected.setVisibility(cashPickupResultModel != null ? View.VISIBLE : View.GONE);
        binding.transactionalStepLocationListCashPickUpLayoutTextViewFeeValue.setText(cashPickupResultModel != null ? cashPickupResultModel.getFee() + " " + CalculatorInteractorImpl.getInstance().getSendingCurrency() : STRING_EMPTY);
        binding.transactionalStepLocationListCashPickUpLayoutTextViewRateValue.setText(cashPickupResultModel != null ? cashPickupResultModel.getRate() : STRING_EMPTY);
        binding.transactionalStepLocationListCashPickUpLayoutTextViewDeliveryValue.setText(cashPickupResultModel != null ? cashPickupResultModel.getDeliveryTime() : STRING_EMPTY);
        binding.transactionalStepLocationListCashPickUpLayoutTextViewLocationSelected.setText(cashPickupResultModel != null ? cashPickupResultModel.getRepresentativeName() : STRING_EMPTY);
        binding.transactionalStepLocationListCashPickUpLayoutTextViewChooseLocation.setText(cashPickupResultModel != null ? (!cashPickupResultModel.getRepresentativeName().isEmpty() ? cashPickupResultModel.getRepresentativeName() + STRING_NEW_LINE : STRING_EMPTY) + (!cashPickupResultModel.getLocationAddress().isEmpty() ? cashPickupResultModel.getLocationAddress() : STRING_EMPTY) : mContext.getString(R.string.transactionalStepLocationListCashPickUpLayoutTextViewChooseLocationText));
        binding.transactionalStepLocationListCashPickUpLayoutTextViewChooseLocation.setTextColor(cashPickupResultModel != null ? ContextCompat.getColor(mContext, R.color.colorBlack) : ContextCompat.getColor(mContext, R.color.colorFieldHint));

        binding.transactionalStepLocationListCashPickUpLayoutMaterialButtonDone.setOnClickListener(v -> {
            registerTransaction2ndStepBrazeEvent();
            binding.transactionalStepLocationListCashPickUpLayoutTextViewChooseLocation.setTextColor(cashPickupResultModel != null ? ContextCompat.getColor(mContext, R.color.colorFieldHint) : ContextCompat.getColor(mContext, R.color.colorRedError));
            binding.transactionalStepLocationListCashPickUpLayoutTextViewRequiredField.setVisibility(cashPickupResultModel != null ? View.GONE : View.VISIBLE);
            if (cashPickupResultModel == null) {
                binding.transactionalStepLocationListCashPickUpLayoutImageViewLocation.setColorFilter(ContextCompat.getColor(mContext, R.color.colorRedError));
            } else {
                binding.transactionalStepLocationListCashPickUpLayoutImageViewLocation.clearColorFilter();
                mPresenter.onLocationSelected(step, cashPickupResultModel.getLocationCode(), cashPickupResultModel.getRepresentativeCode());
            }

        });

        step.setStepSelectedItem(cashPickupResultModel != null ? cashPickupResultModel.getRepresentativeName() : "");

        return binding.getRoot();
    }


    private void deletePrevContentIfExist(View stepView) {
        ExpandableLayout expandableLayout = stepView.findViewById(R.id.expandable_layout);
        if (expandableLayout != null) {
            if (expandableLayout.getChildCount() > 0) {
                expandableLayout.removeAllViews();
            }
        }
    }

    /**
     * Inflate Location Cash Pickup Layout
     */
    @SuppressLint("SetTextI18n")
    private View inflateLocationListCashPickUpView(final Field currentField, View stepView, String currency, final ArrayList<Field> fields, final Step step) {

        @SuppressLint("InflateParams") final View locationView = LayoutInflater.from(mContext).inflate(R.layout.transactional_step_location_list_cashpickup_layout, null, false);
        if (locationView != null) {
            mLocationField = currentField;
            String bankName = TextUtils.isEmpty(mLocationField.getPayout().getRepresentativeName()) ? "" : mLocationField.getPayout().getRepresentativeName();
            String feeIncluded = AmountFormatter.formatDoubleAmountNumber(mLocationField.getPayout().getFee());
            String rate = AmountFormatter.formatDoubleRateNumber(mLocationField.getPayout().getRate());
            int numLocations = mLocationField.getPayout().getCountLocations();
            String deliveryTime = mLocationField.getPayout().getDeliveryTime();
            String locationListSelected = mPresenter.getCitySelectedToShowInPayoutLocation();

            ((StyledTextView) stepView.findViewById(R.id.title)).setText(bankName);
            step.setStepSelectedItem(bankName);

            ((StyledTextView) stepView.findViewById(R.id.pre_title)).setText(step.getName());
            stepView.findViewById(R.id.pre_title).setVisibility(View.VISIBLE);


            // Inflate Content

            locationView.findViewById(R.id.main_location_view).setClickable(false);

            ((StyledTextView) locationView.findViewById(R.id.time_delivery)).setText(deliveryTime);
            ((StyledTextView) locationView.findViewById(R.id.locations_title)).setText((numLocations > 1) ? SmallWorldApplication.getStr(R.string.pick_up_location_plural) : SmallWorldApplication.getStr(R.string.pick_up_location_single));
            ((StyledTextView) locationView.findViewById(R.id.bank_name)).setText(bankName);
            ((StyledTextView) locationView.findViewById(R.id.fee_included)).setText(feeIncluded + " " + currency);
            ((StyledTextView) locationView.findViewById(R.id.rate_value)).setText(rate);
            ((StyledTextView) locationView.findViewById(R.id.num_locations)).setText(String.valueOf(numLocations));
            final String locationTextFormatted = String.format(SmallWorldApplication.getStr(R.string.text_location_selected), locationListSelected);
            ((StyledTextView) locationView.findViewById(R.id.location_text)).setText(locationTextFormatted);

            if (mLocationField.getPayout().getTaxes() != null && mLocationField.getPayout().getTaxes().getTaxCode() != null && !mLocationField.getPayout().getTaxes().getTaxCode().isEmpty() && mLocationField.getPayout().getTaxes().getTaxAmount() != null && !mLocationField.getPayout().getTaxes().getTaxAmount().isEmpty()) {
                (locationView.findViewById(R.id.iof_box)).setVisibility(View.VISIBLE);
                ((StyledTextView) locationView.findViewById(R.id.tax_iof_amount)).setText(mLocationField.getPayout().getTaxes().getFormatedTaxAmount() + " " + getSendingCurrency());
            } else {
                (locationView.findViewById(R.id.iof_box)).setVisibility(View.GONE);
            }

            locationView.setTag(mLocationField.getPayout().getLocationCode() + REPRESENTATIVE_CODE_LOCATION_SEPARATOR + mLocationField.getPayout().getRepresentativeCode());

            Drawable blueRightArrow = ContextCompat.getDrawable(mContext, R.drawable.onboarding_btn_more2);
            DrawableCompat.setTint(blueRightArrow, mContext.getResources().getColor(R.color.medium_blue_color));
            DrawableCompat.setTintMode(blueRightArrow, PorterDuff.Mode.SRC_IN);
            ((StyledTextView) locationView.findViewById(R.id.more_button)).setCompoundDrawablesWithIntrinsicBounds(null, null, blueRightArrow, null);
            ((StyledTextView) locationView.findViewById(R.id.more_button)).setCompoundDrawablePadding(Utils.dpToPx(6));

            locationView.findViewById(R.id.more_button).setOnClickListener(v ->
                    mPresenter.onLocationListMoreClick(fields,
                            step.getStepId(),
                            ((String) locationView.getTag()).split(REPRESENTATIVE_CODE_LOCATION_SEPARATOR)[0],
                            SmallWorldApplication.getStr(R.string.select_cash_pickup_payers_title),
                            locationTextFormatted,
                            mLocationField.getPayout().getRepresentativeName(),
                            Constants.DELIVERY_METHODS.CASH_PICKUP,
                            mLocationField.getPayout().getRate()));

            ((StyledTextView) locationView.findViewById(R.id.change_button)).setCompoundDrawablesWithIntrinsicBounds(null, null, blueRightArrow, null);
            ((StyledTextView) locationView.findViewById(R.id.change_button)).setCompoundDrawablePadding(Utils.dpToPx(6));


            locationView.findViewById(R.id.change_button).setOnClickListener(v ->
                    mPresenter.onChangeLocationClickInStep3(step));

            ((StyledTextView) locationView.findViewById(R.id.sendButton)).setText(mContext.getString(R.string.proceed_action));
            ((StyledTextView) locationView.findViewById(R.id.sendButton)).setContentDescription("proceed_button");
            locationView.findViewById(R.id.sendButton).setOnClickListener(v -> {
                String locationCode = ((String) locationView.getTag()).split(REPRESENTATIVE_CODE_LOCATION_SEPARATOR)[0];
                String representativeCode = ((String) locationView.getTag()).split(REPRESENTATIVE_CODE_LOCATION_SEPARATOR)[1];
                registerTransaction2ndStepBrazeEvent();
                mPresenter.onLocationSelected(step, locationCode, representativeCode);
            });

            locationView.findViewById(R.id.locations_button).setOnClickListener(v ->
                    mPresenter.onLocationMapButtonClick(mLocationField.getPayout()));
        }

        return locationView;
    }

    @SuppressLint("SetTextI18n")
    private View inflateLocationListBankDepositView(final Field currentField, View stepView, String currency, final ArrayList<Field> fields, final Step step) {
        @SuppressLint("InflateParams") final View locationView = LayoutInflater.from(mContext).inflate(R.layout.transactional_step_location_list_bankdeposit_layout, null, false);
        if (locationView != null) {

            mLocationField = currentField;
            String bankName = TextUtils.isEmpty(mLocationField.getPayout().getRepresentativeName()) ? "" : mLocationField.getPayout().getRepresentativeName();
            String feeIncluded = AmountFormatter.formatDoubleAmountNumber(mLocationField.getPayout().getFee());
            String rate = AmountFormatter.formatDoubleRateNumber(mLocationField.getPayout().getRate());
            String timeDelivery = mLocationField.getPayout().getDeliveryTime();

            String cellTitle = mLocationField.getPayout().getDiccType();
            String bankSelected = mPresenter.getBankSelectedToShowInPayoutLocation();
            Drawable iconCell = mLocationField.getPayout().getIconType(mContext, true);

            ((StyledTextView) stepView.findViewById(R.id.title)).setText(bankName);
            step.setStepSelectedItem(bankName);

            ((StyledTextView) stepView.findViewById(R.id.pre_title)).setText(step.getName());
            stepView.findViewById(R.id.pre_title).setVisibility(View.VISIBLE);


            // Inflate Content
            locationView.findViewById(R.id.main_location_view).setClickable(false);

            final String locationTextFormatted = String.format(SmallWorldApplication.getStr(R.string.selected_bank_header_text), bankSelected);

            ((StyledTextView) locationView.findViewById(R.id.location_text)).setText(locationTextFormatted);
            ((StyledTextView) locationView.findViewById(R.id.location_text)).setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);

            ((StyledTextView) locationView.findViewById(R.id.cell_title)).setText(cellTitle);
            ((StyledTextView) locationView.findViewById(R.id.bank_name)).setText(bankName);
            ((StyledTextView) locationView.findViewById(R.id.fee_included)).setText(feeIncluded + " " + currency);
            ((StyledTextView) locationView.findViewById(R.id.rate_value)).setText(rate);
            ((StyledTextView) locationView.findViewById(R.id.time_delivery)).setText(timeDelivery);

            ((StyledTextView) locationView.findViewById(R.id.cell_title)).setCompoundDrawablesWithIntrinsicBounds(iconCell, null, null, null);

            locationView.setTag(mLocationField.getPayout().getLocationCode() + REPRESENTATIVE_CODE_LOCATION_SEPARATOR + mLocationField.getPayout().getRepresentativeCode());
            Drawable blueRightArrow = ContextCompat.getDrawable(mContext, R.drawable.onboarding_btn_more2);

            if (mLocationField.getPayout().getTaxes() != null && mLocationField.getPayout().getTaxes().getTaxCode() != null &&
                    !mLocationField.getPayout().getTaxes().getTaxCode().isEmpty() && mLocationField.getPayout().getTaxes().getTaxAmount() != null &&
                    !mLocationField.getPayout().getTaxes().getTaxAmount().isEmpty()) {
                (locationView.findViewById(R.id.iof_box)).setVisibility(View.VISIBLE);
                ((StyledTextView) locationView.findViewById(R.id.tax_iof_amount)).setText(mLocationField.getPayout().getTaxes().getFormatedTaxAmount() +
                        " " + getSendingCurrency());
            } else {
                (locationView.findViewById(R.id.iof_box)).setVisibility(View.GONE);
            }

            if (fields != null && fields.size() > 1) {
                DrawableCompat.setTint(blueRightArrow, mContext.getResources().getColor(R.color.medium_blue_color));
                DrawableCompat.setTintMode(blueRightArrow, PorterDuff.Mode.SRC_IN);
                ((StyledTextView) locationView.findViewById(R.id.more_button)).setCompoundDrawablesWithIntrinsicBounds(null, null, blueRightArrow, null);
                ((StyledTextView) locationView.findViewById(R.id.more_button)).setCompoundDrawablePadding(Utils.dpToPx(6));
            } else {
                locationView.findViewById(R.id.more_button).setVisibility(View.GONE);
            }


            ((StyledTextView) locationView.findViewById(R.id.change_button)).setCompoundDrawablesWithIntrinsicBounds(null, null, blueRightArrow, null);
            ((StyledTextView) locationView.findViewById(R.id.change_button)).setCompoundDrawablePadding(Utils.dpToPx(6));


            locationView.findViewById(R.id.more_button).setOnClickListener(v -> {
                String locationCode = ((String) locationView.getTag()).split(REPRESENTATIVE_CODE_LOCATION_SEPARATOR)[0];
                mPresenter.onLocationListMoreClick(fields, step.getStepId(),
                        locationCode,
                        SmallWorldApplication.getStr(R.string.select_bank_deposit_payers_title),
                        locationTextFormatted,
                        mLocationField.getPayout().getRepresentativeName(),
                        Constants.DELIVERY_METHODS.BANK_DEPOSIT,
                        mLocationField.getPayout().getRate());
            });

            locationView.findViewById(R.id.change_button).setOnClickListener(v -> mPresenter.onChangeLocationClickInStep3(step));

            ((StyledTextView) locationView.findViewById(R.id.sendButton)).setText(mContext.getString(R.string.proceed_action));
            ((StyledTextView) locationView.findViewById(R.id.sendButton)).setContentDescription("proceed_button");
            locationView.findViewById(R.id.sendButton).setOnClickListener(v -> {
                // Manage representative and locationCode
                String locationCode = ((String) locationView.getTag()).split(REPRESENTATIVE_CODE_LOCATION_SEPARATOR)[0];
                String representativeCode = ((String) locationView.getTag()).split(REPRESENTATIVE_CODE_LOCATION_SEPARATOR)[1];
                registerTransaction2ndStepBrazeEvent();
                mPresenter.onLocationSelected(step, locationCode, representativeCode);
            });
        }

        return locationView;
    }


    @SuppressLint("SetTextI18n")
    private View inflateHomeDeliveryLocationListView(Field currentField, final ArrayList<Field> fields, final Step step) {
        @SuppressLint("InflateParams") final View locationView = LayoutInflater.from(mContext).inflate(R.layout.transactional_step_location_list_home_delivery_layout, null, false);

        if (locationView != null) {
            mLocationField = currentField;
            String bankName = TextUtils.isEmpty(mLocationField.getPayout().getRepresentativeName()) ? "" : mLocationField.getPayout().getRepresentativeName();
            String feeIncluded = AmountFormatter.formatDoubleAmountNumber(mLocationField.getPayout().getFee()) + " " + getSendingCurrency();
            String rate = AmountFormatter.formatDoubleRateNumber(mLocationField.getPayout().getRate());
            final String locationSelected = mPresenter.getCitySelectedToShowInPayoutLocation();
            String deliveryTime = mLocationField.getPayout().getDeliveryTime();

            ((StyledTextView) locationView.findViewById(R.id.time_delivery)).setText(deliveryTime);
            ((StyledTextView) locationView.findViewById(R.id.location_text)).setText(String.format(SmallWorldApplication.getStr(R.string.text_location_selected), locationSelected));
            ((StyledTextView) locationView.findViewById(R.id.fee_included)).setText(feeIncluded);
            ((StyledTextView) locationView.findViewById(R.id.rate_value)).setText(rate);
            ((StyledTextView) locationView.findViewById(R.id.bank_name)).setText(bankName);

            step.setStepSelectedItem(bankName);


            // More & Change Button
            Drawable blueRightArrow = ContextCompat.getDrawable(mContext, R.drawable.onboarding_btn_more2);
            DrawableCompat.setTint(blueRightArrow, mContext.getResources().getColor(R.color.medium_blue_color));
            DrawableCompat.setTintMode(blueRightArrow, PorterDuff.Mode.SRC_IN);

            ((StyledTextView) locationView.findViewById(R.id.change_button)).setCompoundDrawablesWithIntrinsicBounds(null, null, blueRightArrow, null);
            ((StyledTextView) locationView.findViewById(R.id.change_button)).setCompoundDrawablePadding(Utils.dpToPx(6));

            locationView.setTag(mLocationField.getPayout().getLocationCode() + REPRESENTATIVE_CODE_LOCATION_SEPARATOR + mLocationField.getPayout().getRepresentativeCode());

            if (mLocationField.getPayout().getTaxes() != null && mLocationField.getPayout().getTaxes().getTaxCode() != null && !mLocationField.getPayout().getTaxes().getTaxCode().isEmpty() && mLocationField.getPayout().getTaxes().getTaxAmount() != null && !mLocationField.getPayout().getTaxes().getTaxAmount().isEmpty()) {
                (locationView.findViewById(R.id.iof_box)).setVisibility(View.VISIBLE);
                ((StyledTextView) locationView.findViewById(R.id.tax_iof_amount)).setText(mLocationField.getPayout().getTaxes().getFormatedTaxAmount() + " " + getSendingCurrency());
            } else {
                (locationView.findViewById(R.id.iof_box)).setVisibility(View.GONE);
            }

            if (fields != null && fields.size() > 1) {
                ((StyledTextView) locationView.findViewById(R.id.more_button)).setCompoundDrawablesWithIntrinsicBounds(null, null, blueRightArrow, null);
                ((StyledTextView) locationView.findViewById(R.id.more_button)).setCompoundDrawablePadding(Utils.dpToPx(6));

                locationView.findViewById(R.id.more_button).setOnClickListener(v -> {
                    String locationCode = ((String) locationView.getTag()).split(REPRESENTATIVE_CODE_LOCATION_SEPARATOR)[0];
                    mPresenter.onLocationListMoreClick(fields,
                            step.getStepId(),
                            locationCode,
                            SmallWorldApplication.getStr(R.string.select_cash_pickup_payers_title),
                            locationSelected,
                            mLocationField.getPayout().getRepresentativeName(),
                            Constants.DELIVERY_METHODS.CASH_CARD_APP_PHYSICAL,
                            mLocationField.getPayout().getRate());
                });
            } else {
                locationView.findViewById(R.id.more_button).setVisibility(View.GONE);
            }


            locationView.findViewById(R.id.change_button).setOnClickListener(v -> mPresenter.onChangeLocationClickInStep3(step));

            ((StyledTextView) locationView.findViewById(R.id.sendButton)).setText(mContext.getString(R.string.proceed_action));
            ((StyledTextView) locationView.findViewById(R.id.sendButton)).setContentDescription("proceed_button");
            locationView.findViewById(R.id.sendButton).setOnClickListener(v -> {
                // Manage representative and locationCode
                String locationCode = ((String) locationView.getTag()).split(REPRESENTATIVE_CODE_LOCATION_SEPARATOR)[0];
                String representativeCode = ((String) locationView.getTag()).split(REPRESENTATIVE_CODE_LOCATION_SEPARATOR)[1];
                registerTransaction2ndStepBrazeEvent();
                mPresenter.onLocationSelected(step, locationCode, representativeCode);
            });


        }

        return locationView;
    }

    @SuppressLint("SetTextI18n")
    private View inflateCashCardReloadLocationListView(Field currentField, final Step step) {
        @SuppressLint("InflateParams") final View locationView = LayoutInflater.from(mContext).inflate(R.layout.transactional_step_location_list_cash_cash_reload_layout, null, false);

        if (locationView != null) {
            mLocationField = currentField;
            String bankName = TextUtils.isEmpty(mLocationField.getPayout().getRepresentativeName()) ? "" : mLocationField.getPayout().getRepresentativeName();
            String feeIncluded = AmountFormatter.formatDoubleAmountNumber(mLocationField.getPayout().getFee()) + getSendingCurrency();
            String rate = AmountFormatter.formatDoubleRateNumber(mLocationField.getPayout().getRate());
            String deliveryTime = mLocationField.getPayout().getDeliveryTime();

            ((StyledTextView) locationView.findViewById(R.id.time_delivery)).setText(deliveryTime);
            ((StyledTextView) locationView.findViewById(R.id.fee_included)).setText(feeIncluded);
            ((StyledTextView) locationView.findViewById(R.id.rate_value)).setText(rate);
            ((StyledTextView) locationView.findViewById(R.id.bank_name)).setText(bankName);

            step.setStepSelectedItem(bankName);

            locationView.setTag(mLocationField.getPayout().getLocationCode() + REPRESENTATIVE_CODE_LOCATION_SEPARATOR + mLocationField.getPayout().getRepresentativeCode());

            if (mLocationField.getPayout().getTaxes() != null && mLocationField.getPayout().getTaxes().getTaxCode() != null && !mLocationField.getPayout().getTaxes().getTaxCode().isEmpty() && mLocationField.getPayout().getTaxes().getTaxAmount() != null && !mLocationField.getPayout().getTaxes().getTaxAmount().isEmpty()) {
                (locationView.findViewById(R.id.iof_box)).setVisibility(View.VISIBLE);
                ((StyledTextView) locationView.findViewById(R.id.tax_iof_amount)).setText(mLocationField.getPayout().getTaxes().getFormatedTaxAmount() + " " + getSendingCurrency());
            } else {
                (locationView.findViewById(R.id.iof_box)).setVisibility(View.GONE);
            }

            ((StyledTextView) locationView.findViewById(R.id.sendButton)).setText(mContext.getString(R.string.proceed_action));
            ((StyledTextView) locationView.findViewById(R.id.sendButton)).setContentDescription("proceed_button");
            locationView.findViewById(R.id.sendButton).setOnClickListener(v -> {
                // Manage representative and locationCode
                String locationCode = ((String) locationView.getTag()).split(REPRESENTATIVE_CODE_LOCATION_SEPARATOR)[0];
                String representativeCode = ((String) locationView.getTag()).split(REPRESENTATIVE_CODE_LOCATION_SEPARATOR)[1];
                registerTransaction2ndStepBrazeEvent();
                mPresenter.onLocationSelected(step, locationCode, representativeCode);
            });
        }

        return locationView;
    }


    @SuppressLint("SetTextI18n")
    private View inflateMobileTopUpLocationListView(Field currentField, String currency, final Step step) {
        @SuppressLint("InflateParams") final View locationView = LayoutInflater.from(mContext).inflate(R.layout.transactional_step_location_list_mobile_top_up_layout, null, false);

        if (locationView != null) {
            mLocationField = currentField;
            String feeIncluded = AmountFormatter.formatDoubleAmountNumber(mLocationField.getPayout().getFee());
            String rate = AmountFormatter.formatDoubleRateNumber(mLocationField.getPayout().getRate()) + " " + getSendingCurrency();
            String payoutPrincipal = CalculatorInteractorImpl.getInstance().getCurrentBeneficiaryAmount() + " " + CalculatorInteractorImpl.getInstance().getPayoutCurrency();
            String principal = CalculatorInteractorImpl.getInstance().getCurrentYouPayAmount() + " " + getSendingCurrency();
            String deliveryTime = mLocationField.getPayout().getDeliveryTime();

            ((StyledTextView) locationView.findViewById(R.id.time_delivery)).setText(deliveryTime);
            ((StyledTextView) locationView.findViewById(R.id.fee_included_value)).setText(feeIncluded + " " + currency);
            ((StyledTextView) locationView.findViewById(R.id.rate_value)).setText(rate);
            ((StyledTextView) locationView.findViewById(R.id.payout_principal_value)).setText(payoutPrincipal);
            ((StyledTextView) locationView.findViewById(R.id.principal_value)).setText(principal);

            locationView.setTag(mLocationField.getPayout().getLocationCode() + REPRESENTATIVE_CODE_LOCATION_SEPARATOR + mLocationField.getPayout().getRepresentativeCode());

            if (mLocationField.getPayout().getTaxes() != null && mLocationField.getPayout().getTaxes().getTaxCode() != null && !mLocationField.getPayout().getTaxes().getTaxCode().isEmpty() && mLocationField.getPayout().getTaxes().getTaxAmount() != null && !mLocationField.getPayout().getTaxes().getTaxAmount().isEmpty()) {
                (locationView.findViewById(R.id.iof_box)).setVisibility(View.VISIBLE);
                ((StyledTextView) locationView.findViewById(R.id.tax_iof_amount)).setText(mLocationField.getPayout().getTaxes().getFormatedTaxAmount() + " " + getSendingCurrency());
            } else {
                (locationView.findViewById(R.id.iof_box)).setVisibility(View.GONE);
            }

            ((StyledTextView) locationView.findViewById(R.id.sendButton)).setText(mContext.getString(R.string.proceed_action));
            ((StyledTextView) locationView.findViewById(R.id.sendButton)).setContentDescription("proceed_button");
            locationView.findViewById(R.id.sendButton).setOnClickListener(v -> {
                // Manage representative and locationCode
                String locationCode = ((String) locationView.getTag()).split(REPRESENTATIVE_CODE_LOCATION_SEPARATOR)[0];
                String representativeCode = ((String) locationView.getTag()).split(REPRESENTATIVE_CODE_LOCATION_SEPARATOR)[1];
                registerTransaction2ndStepBrazeEvent();
                mPresenter.onLocationSelected(step, locationCode, representativeCode);
            });

        }
        this.locationView = locationView;
        return locationView;
    }

    @SuppressLint("SetTextI18n")
    private View inflateOnCallLocationListView(final Field currentField, final Step step) {
        @SuppressLint("InflateParams") final View locationView = LayoutInflater.from(mContext).inflate(R.layout.transactional_step_location_list_on_call_layout, null, false);

        if (locationView != null) {
            mLocationField = currentField;
            String feeIncluded = AmountFormatter.formatDoubleAmountNumber(mLocationField.getPayout().getFee());
            String rate = AmountFormatter.formatDoubleRateNumber(mLocationField.getPayout().getRate());
            ((StyledTextView) locationView.findViewById(R.id.fee_included)).setText(feeIncluded + " " + getSendingCurrency());
            ((StyledTextView) locationView.findViewById(R.id.rate_value)).setText(rate);
            String deliveryTime = mLocationField.getPayout().getDeliveryTime();
            String bankName = TextUtils.isEmpty(mLocationField.getPayout().getRepresentativeName()) ? "" : mLocationField.getPayout().getRepresentativeName();
            int numLocations = mLocationField.getPayout().getCountLocations();

            ((StyledTextView) locationView.findViewById(R.id.time_delivery)).setText(deliveryTime);
            ((StyledTextView) locationView.findViewById(R.id.locations_title)).setText(bankName);
            ((StyledTextView) locationView.findViewById(R.id.num_locations)).setVisibility(View.GONE);
            ((StyledTextView) locationView.findViewById(R.id.arrow_Back)).setVisibility(View.GONE);

            if (numLocations > 0) {
                final String locationTextFormattedPart1 = String.format(SmallWorldApplication.getStr(R.string.text_location_anywhere_pickup_part_1));
                final String locationTextFormattedPart2 = String.format(SmallWorldApplication.getStr(R.string.text_location_anywhere_pickup_part_2));
                final String locationTextFormattedNumberLocation = String.format(STRING_SPACE + numLocations + STRING_SPACE);
                ((StyledTextView) locationView.findViewById(R.id.location_text)).setText(locationTextFormattedPart1 + locationTextFormattedNumberLocation + locationTextFormattedPart2);
            } else {
                ((StyledTextView) locationView.findViewById(R.id.location_text)).setText(String.format(SmallWorldApplication.getStr(R.string.text_location_anywhere_pickup_one_location)));

            }
            step.setStepSelectedItem(SmallWorldApplication.getStr(R.string.payout_details));

            locationView.setTag(mLocationField.getPayout().getLocationCode() + REPRESENTATIVE_CODE_LOCATION_SEPARATOR + mLocationField.getPayout().getRepresentativeCode());

            if (mLocationField.getPayout().getTaxes() != null && mLocationField.getPayout().getTaxes().getTaxCode() != null &&
                    !mLocationField.getPayout().getTaxes().getTaxCode().isEmpty() && mLocationField.getPayout().getTaxes().getTaxAmount() != null &&
                    !mLocationField.getPayout().getTaxes().getTaxAmount().isEmpty()) {
                (locationView.findViewById(R.id.iof_box)).setVisibility(View.VISIBLE);
                ((StyledTextView) locationView.findViewById(R.id.tax_iof_amount)).setText(mLocationField.getPayout().getTaxes().getFormatedTaxAmount() + " " + getSendingCurrency());
            } else {
                (locationView.findViewById(R.id.iof_box)).setVisibility(View.GONE);
            }

            Drawable blueRightArrow = ContextCompat.getDrawable(mContext, R.drawable.onboarding_btn_more2);
            DrawableCompat.setTint(blueRightArrow, mContext.getResources().getColor(R.color.medium_blue_color));
            DrawableCompat.setTintMode(blueRightArrow, PorterDuff.Mode.SRC_IN);
            ((StyledTextView) locationView.findViewById(R.id.change_button)).setCompoundDrawablesWithIntrinsicBounds(null, null, blueRightArrow, null);
            ((StyledTextView) locationView.findViewById(R.id.change_button)).setCompoundDrawablePadding(Utils.dpToPx(6));

            ((StyledTextView) locationView.findViewById(R.id.sendButton)).setText(mContext.getString(R.string.proceed_action));
            ((StyledTextView) locationView.findViewById(R.id.sendButton)).setContentDescription("proceed_button");
            locationView.findViewById(R.id.sendButton).setOnClickListener(v -> {
                // Manage representative and locationCode
                String locationCode = ((String) locationView.getTag()).split(REPRESENTATIVE_CODE_LOCATION_SEPARATOR)[0];
                String representativeCode = ((String) locationView.getTag()).split(REPRESENTATIVE_CODE_LOCATION_SEPARATOR)[1];
                registerTransaction2ndStepBrazeEvent();
                mPresenter.onLocationSelected(step, locationCode, representativeCode);
            });

            //locationView.findViewById(R.id.locations_button).setOnClickListener(v -> mPresenter.onLocationMapButtonClick(mLocationField.getPayout()));
            ((LinearLayout) locationView.findViewById(R.id.payout_cashpickup_container)).setClickable(false);
            ((RelativeLayout) locationView.findViewById(R.id.locations_button)).setClickable(false);
            ((RelativeLayout) locationView.findViewById(R.id.location_text_container)).setOnClickListener(v ->
                    mPresenter.onActionChooseLocationForPickUp(true, null)
            );

        }

        return locationView;

    }

    @SuppressLint("SetTextI18n")
    private View inflateMobileWalletLocationListView(Field currentField, final Step step, final ArrayList<Field> fields) {
        @SuppressLint("InflateParams") final View locationView = LayoutInflater.from(mContext).inflate(R.layout.transactional_step_location_list_mobile_wallet_layout, null, false);


        if (locationView != null) {
            mLocationField = currentField;
            String feeIncluded = AmountFormatter.formatDoubleAmountNumber(mLocationField.getPayout().getFee());
            String rate = AmountFormatter.formatDoubleRateNumber(mLocationField.getPayout().getRate());
            String bankName = mLocationField.getPayout().getRepresentativeName();
            String deliveryTime = mLocationField.getPayout().getDeliveryTime();

            ((StyledTextView) locationView.findViewById(R.id.time_delivery)).setText(deliveryTime);
            ((StyledTextView) locationView.findViewById(R.id.fee_included)).setText(feeIncluded + " " + getSendingCurrency());
            ((StyledTextView) locationView.findViewById(R.id.rate_value)).setText(rate);
            ((StyledTextView) locationView.findViewById(R.id.bank_name)).setText(bankName);


            step.setStepSelectedItem(SmallWorldApplication.getStr(R.string.payout_details));
            final String locationTextFormatted = String.format(SmallWorldApplication.getStr(R.string.selected_bank_header_text), bankName);

            locationView.setTag(mLocationField.getPayout().getLocationCode() + REPRESENTATIVE_CODE_LOCATION_SEPARATOR + mLocationField.getPayout().getRepresentativeCode());
            Drawable blueRightArrow = ContextCompat.getDrawable(mContext, R.drawable.onboarding_btn_more2);
            DrawableCompat.setTint(blueRightArrow, mContext.getResources().getColor(R.color.medium_blue_color));
            DrawableCompat.setTintMode(blueRightArrow, PorterDuff.Mode.SRC_IN);
            ((StyledTextView) locationView.findViewById(R.id.more_button)).setCompoundDrawablesWithIntrinsicBounds(null, null, blueRightArrow, null);
            ((StyledTextView) locationView.findViewById(R.id.more_button)).setCompoundDrawablePadding(Utils.dpToPx(6));

            if (mLocationField.getPayout().getTaxes() != null && mLocationField.getPayout().getTaxes().getTaxCode() != null &&
                    !mLocationField.getPayout().getTaxes().getTaxCode().isEmpty() && mLocationField.getPayout().getTaxes().getTaxAmount() != null &&
                    !mLocationField.getPayout().getTaxes().getTaxAmount().isEmpty()) {
                (locationView.findViewById(R.id.iof_box)).setVisibility(View.VISIBLE);
                ((StyledTextView) locationView.findViewById(R.id.tax_iof_amount)).setText(mLocationField.getPayout().getTaxes().getFormatedTaxAmount() + " " + getSendingCurrency());
            } else {
                (locationView.findViewById(R.id.iof_box)).setVisibility(View.GONE);
            }

            locationView.findViewById(R.id.more_button).setOnClickListener(v -> mPresenter.onLocationListMoreClick(fields,
                    step.getStepId(),
                    String.valueOf(locationView.getTag()).split(REPRESENTATIVE_CODE_LOCATION_SEPARATOR)[0],
                    SmallWorldApplication.getStr(R.string.select_available_payers_title),
                    locationTextFormatted,
                    mLocationField.getPayout().getRepresentativeName(),
                    Constants.DELIVERY_METHODS.MOBILE_WALLET,
                    mLocationField.getPayout().getRate()));

            ((StyledTextView) locationView.findViewById(R.id.sendButton)).setText(mContext.getString(R.string.proceed_action));
            ((StyledTextView) locationView.findViewById(R.id.sendButton)).setContentDescription("proceed_button");
            locationView.findViewById(R.id.sendButton).setOnClickListener(v -> {
                // Manage representative and locationCode
                String locationCode = ((String) locationView.getTag()).split(REPRESENTATIVE_CODE_LOCATION_SEPARATOR)[0];
                String representativeCode = ((String) locationView.getTag()).split(REPRESENTATIVE_CODE_LOCATION_SEPARATOR)[1];
                registerTransaction2ndStepBrazeEvent();
                mPresenter.onLocationSelected(step, locationCode, representativeCode);
            });
        }

        return locationView;

    }


    /**
     * Decides what kind of view has to update
     */
    public final void updateLocationList(Field currentField, View stepView, String currency, Step step) {
        if (currentField != null && stepView != null && mLocationField.getPayout() != null) {
            mLocationField = currentField;
            switch (mLocationField.getSubtype()) {
                case Constants.FIELD_SUBTYPES.LOCATION_PICK_UP:
                    updateLocationListCashPickupView(mLocationField, stepView, currency, step);
                    break;
                case Constants.FIELD_SUBTYPES.LOCATION_BANK_DEPOSIT:
                    updateLocationBankDepositView(mLocationField, stepView, currency, step);
                    break;
                case Constants.FIELD_SUBTYPES.LOCATION_HOME_DELIVERY:
                    updateLocationHomeDelivery(mLocationField, stepView, step);
                    break;
                case Constants.FIELD_SUBTYPES.LOCATION_MOBILE_WALLET:
                    updateLocationListMobileWalletView(mLocationField, stepView, step);
                    break;
            }

            // Recalculate
            if (mLocationField.getPayout() != null) {
                String repCode = Integer.toString(mLocationField.getPayout().getRepresentativeCode());
                CalculatorInteractorImpl.getInstance().updateCalculatorWithRepresentativeCode(repCode);
            }
        }
    }


    /**
     * Update location list cash pickup cell
     */
    @SuppressLint("SetTextI18n")
    private void updateLocationListCashPickupView(Field currentField, View locationView, String currency, Step step) {
        if (locationView != null) {
            mLocationField = currentField;
            String bankName = TextUtils.isEmpty(mLocationField.getPayout().getRepresentativeName()) ? "" : mLocationField.getPayout().getRepresentativeName();
            String feeIncluded = AmountFormatter.convertServerValueToLocalized(String.valueOf(mLocationField.getPayout().getFee()));
            String rate = AmountFormatter.convertServerValueToLocalized(String.valueOf(mLocationField.getPayout().getRate()));
            int numLocations = mLocationField.getPayout().getCountLocations();

            step.setStepSelectedItem(bankName);
            ((StyledTextView) locationView.findViewById(R.id.title)).setText(bankName);


            // Inflate Content
            locationView.findViewById(R.id.main_location_view).setTag(mLocationField.getPayout().getLocationCode() + REPRESENTATIVE_CODE_LOCATION_SEPARATOR + mLocationField.getPayout().getRepresentativeCode());

            ((StyledTextView) locationView.findViewById(R.id.bank_name)).setText(bankName);
            ((StyledTextView) locationView.findViewById(R.id.fee_included)).setText(feeIncluded + " " + currency);
            ((StyledTextView) locationView.findViewById(R.id.rate_value)).setText(rate);
            ((StyledTextView) locationView.findViewById(R.id.num_locations)).setText(String.valueOf(numLocations));
        }
    }

    /**
     * Update locaiton list bank deposit cell
     */
    @SuppressLint("SetTextI18n")
    private void updateLocationBankDepositView(Field currentField, View locationView, String currency, Step step) {
        if (locationView != null) {
            mLocationField = currentField;
            String bankName = TextUtils.isEmpty(mLocationField.getPayout().getRepresentativeName()) ? "" : mLocationField.getPayout().getRepresentativeName();
            String feeIncluded = AmountFormatter.convertServerValueToLocalized(String.valueOf(mLocationField.getPayout().getFee()));
            String rate = AmountFormatter.convertServerValueToLocalized(String.valueOf(mLocationField.getPayout().getRate()));
            String timeDelivery = mLocationField.getPayout().getDiccDeliveryTime();
            String cellTitle = mLocationField.getPayout().getDiccType();
            Drawable iconCell = mLocationField.getPayout().getIconType(mContext, true);

            step.setStepSelectedItem(bankName);

            ((StyledTextView) locationView.findViewById(R.id.title)).setText(bankName);

            // Inflate Content
            locationView.findViewById(R.id.main_location_view).setTag(mLocationField.getPayout().getLocationCode() + REPRESENTATIVE_CODE_LOCATION_SEPARATOR + mLocationField.getPayout().getRepresentativeCode());

            ((StyledTextView) locationView.findViewById(R.id.cell_title)).setText(cellTitle);
            ((StyledTextView) locationView.findViewById(R.id.bank_name)).setText(bankName);
            ((StyledTextView) locationView.findViewById(R.id.fee_included)).setText(feeIncluded + " " + currency);
            ((StyledTextView) locationView.findViewById(R.id.rate_value)).setText(rate);
            ((StyledTextView) locationView.findViewById(R.id.time_delivery)).setText(timeDelivery);

            ((StyledTextView) locationView.findViewById(R.id.cell_title)).setCompoundDrawablesWithIntrinsicBounds(iconCell, null, null, null);
        }
    }


    /**
     * Update location list home delivery
     */
    private void updateLocationHomeDelivery(Field currentField, View locationView, Step step) {
        try {
            if (locationView != null) {
                mLocationField = currentField;
                String bankName = TextUtils.isEmpty(mLocationField.getPayout().getRepresentativeName()) ? "" : mLocationField.getPayout().getRepresentativeName();
                String feeIncluded = AmountFormatter.convertServerValueToLocalized(String.valueOf(mLocationField.getPayout().getFee())) + " " + getSendingCurrency();
                String rate = AmountFormatter.formatDoubleAmountNumber(mLocationField.getPayout().getRate());

                if (locationView.findViewById(R.id.fee_included_value) != null)
                    ((StyledTextView) locationView.findViewById(R.id.fee_included_value)).setText(feeIncluded);
                if (locationView.findViewById(R.id.rate_value) != null)
                    ((StyledTextView) locationView.findViewById(R.id.rate_value)).setText(rate);
                if (locationView.findViewById(R.id.bank_name) != null)
                    ((StyledTextView) locationView.findViewById(R.id.bank_name)).setText(bankName);

                step.setStepSelectedItem(bankName);

                ((StyledTextView) locationView.findViewById(R.id.title)).setText(bankName);

                // Inflate Content
                locationView.findViewById(R.id.main_location_view).setTag(mLocationField.getPayout().getLocationCode() + REPRESENTATIVE_CODE_LOCATION_SEPARATOR + mLocationField.getPayout().getRepresentativeCode());
            }
        } catch (Exception e) {
            Log.e(TAG, "updateLocationHomeDelivery:e:------------------------------------------", e);
        }
    }

    /**
     * Update location list mobile wallet cell
     */
    @SuppressLint("SetTextI18n")
    private void updateLocationListMobileWalletView(Field currentField, View locationView, Step step) {
        if (locationView != null) {
            mLocationField = currentField;
            String feeIncluded = AmountFormatter.formatDoubleAmountNumber(mLocationField.getPayout().getFee());
            String rate = AmountFormatter.formatDoubleRateNumber(mLocationField.getPayout().getRate());
            String bankName = mLocationField.getPayout().getRepresentativeName();
            String deliveryTime = mLocationField.getPayout().getDeliveryTime();

            step.setStepSelectedItem(bankName);
            ((StyledTextView) locationView.findViewById(R.id.title)).setText(bankName);


            // Inflate Content
            locationView.findViewById(R.id.main_location_view).setTag(mLocationField.getPayout().getLocationCode() + REPRESENTATIVE_CODE_LOCATION_SEPARATOR + mLocationField.getPayout().getRepresentativeCode());

            ((StyledTextView) locationView.findViewById(R.id.time_delivery)).setText(deliveryTime);
            ((StyledTextView) locationView.findViewById(R.id.fee_included)).setText(feeIncluded + " " + getSendingCurrency());
            ((StyledTextView) locationView.findViewById(R.id.rate_value)).setText(rate);
            ((StyledTextView) locationView.findViewById(R.id.bank_name)).setText(bankName);
        }
    }

    public void inflateEmptyView(Step step, View stepView, String deliveryMethod) {
        View emptyView = null;
        deletePrevContentIfExist(stepView);
        if (Constants.STEP_TYPE.LOCATION_LIST.equals(step.getStepType())) {
            emptyView = processEmptyViewLocationList(step, deliveryMethod);
        }
        if (emptyView != null) {
            ((ExpandableLayout) stepView.findViewById(R.id.expandable_layout)).addView(emptyView);
        }
    }

    /**
     * Process generic list location cashpickup emptyview/errorview
     */
    private View processEmptyViewLocationList(final Step step, String deliveryMethod) {
        @SuppressLint("InflateParams") View emptyView = LayoutInflater.from(mContext).inflate(R.layout.transactional_step_location_list_error_view, null, false);
        switch (deliveryMethod) {
            case Constants.DELIVERY_METHODS.CASH_PICKUP:
            case Constants.DELIVERY_METHODS.PHYSICAL_DELIVERY: {
                String locationListSelected = mPresenter.getCitySelectedToShowInPayoutLocation();

                ((StyledTextView) emptyView.findViewById(R.id.emtpy_text_title)).setText(SmallWorldApplication.getStr(R.string.empty_view_text_location_list_city_deposit_title));
                ((StyledTextView) emptyView.findViewById(R.id.emtpy_text_subtitle)).setText(SmallWorldApplication.getStr(R.string.empty_view_text_location_list_location_deposit_subtitle));
                emptyView.findViewById(R.id.empty_view_button).setVisibility(View.GONE);
                ((StyledTextView) emptyView.findViewById(R.id.location_text)).setText(String.format(SmallWorldApplication.getStr(R.string.text_location_selected), locationListSelected));

                ((StyledTextView) emptyView.findViewById(R.id.change_button)).setTextColor(mContext.getResources().getColor(R.color.colorRedError));

                Drawable blueRightArrow = ContextCompat.getDrawable(mContext, R.drawable.onboarding_btn_more2);
                DrawableCompat.setTint(blueRightArrow, mContext.getResources().getColor(R.color.colorRedError));
                DrawableCompat.setTintMode(blueRightArrow, PorterDuff.Mode.SRC_IN);

                ((StyledTextView) emptyView.findViewById(R.id.change_button)).setCompoundDrawablesWithIntrinsicBounds(null, null, blueRightArrow, null);
                ((StyledTextView) emptyView.findViewById(R.id.change_button)).setCompoundDrawablePadding(Utils.dpToPx(6));

                emptyView.findViewById(R.id.change_button).setOnClickListener(v -> mPresenter.onChangeLocationClickInStep3(step));
            }
            break;
            case Constants.DELIVERY_METHODS.BANK_DEPOSIT:
            case Constants.DELIVERY_METHODS.CASH_CARD_RELOAD: {

                String bankSelected = mPresenter.getBankSelectedToShowInPayoutLocation();

                ((StyledTextView) emptyView.findViewById(R.id.emtpy_text_title)).setText(SmallWorldApplication.getStr(R.string.empty_view_text_location_list_bank_deposit_title));
                ((StyledTextView) emptyView.findViewById(R.id.emtpy_text_subtitle)).setText(SmallWorldApplication.getStr(R.string.empty_view_text_location_list_bank_deposit_subtitle));
                ((StyledTextView) emptyView.findViewById(R.id.button_text_retry)).setText(R.string.edit_beneficiary_data);

                if (deliveryMethod.equalsIgnoreCase(Constants.DELIVERY_METHODS.CASH_CARD_RELOAD)) {
                    (emptyView.findViewById(R.id.location_text_container)).setVisibility(View.GONE);
                } else {
                    ((StyledTextView) emptyView.findViewById(R.id.location_text)).setText(String.format(SmallWorldApplication.getStr(R.string.selected_bank_header_text), bankSelected));
                }

                emptyView.findViewById(R.id.change_button).setVisibility(View.INVISIBLE);
                emptyView.findViewById(R.id.button_text_retry).setOnClickListener(v -> mPresenter.onChangeLocationClickInStep3(step));
                break;
            }
            default: {
                (emptyView.findViewById(R.id.location_text_container)).setVisibility(View.GONE);
                ((StyledTextView) emptyView.findViewById(R.id.emtpy_text_title)).setText(SmallWorldApplication.getStr(R.string.no_data_generic_available));
                ((StyledTextView) emptyView.findViewById(R.id.emtpy_text_subtitle)).setText(SmallWorldApplication.getStr(R.string.change_info_in_beneficiary_step));
                ((StyledTextView) emptyView.findViewById(R.id.button_text_retry)).setText(R.string.change);
                emptyView.findViewById(R.id.change_button).setVisibility(View.INVISIBLE);
                emptyView.findViewById(R.id.button_text_retry).setOnClickListener(v -> mPresenter.onChangeLocationClickInStep3(step));

                break;
            }


        }
        return emptyView;
    }

    public void setSelectedDeliveryMethod(Method deliveryMethod) {
        if (mDeliveryMethodAdapter != null) {
            ((DeliveryMethodAdapter) mDeliveryMethodAdapter).setSelectedDeliveryMethod(deliveryMethod);
        }
    }

    public String getSelectedDeliveryMethod() {
        if (mDeliveryMethodAdapter != null) {
            return ((DeliveryMethodAdapter) mDeliveryMethodAdapter).getSelectedDeliveryMethod();
        } else return "";
    }


    private String getSendingCurrency() {
        return CalculatorInteractorImpl.getInstance().getSendingCurrency();
    }

    public void setMainNestedScrollView(NestedScrollView mainNestedScrollView) {
        this.mMainNestedScrollView = mainNestedScrollView;
    }

    public LinearLayout getMainContainer() {
        return mMainContainer;
    }

    public void setMainContainer(LinearLayout mainContainer) {
        this.mMainContainer = mainContainer;
    }

    private void registerTransaction2ndStepBrazeEvent() {
        if (mActivity instanceof TransactionalActivity) {
            ((TransactionalActivity) mActivity).registerTransaction2ndStepBrazeEvent();
        }
    }
}

