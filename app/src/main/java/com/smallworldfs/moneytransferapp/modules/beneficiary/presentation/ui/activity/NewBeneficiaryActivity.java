package com.smallworldfs.moneytransferapp.modules.beneficiary.presentation.ui.activity;

import static com.smallworldfs.moneytransferapp.modules.c2b.presentation.ui.activity.C2BActivity.BENEFICIARY_TYPE;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity.CONTENT_FIELD_POSITION;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity.CONTENT_STEP_ID;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.GenericSelectDropContentActivity.RESULT_DATA;
import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.PayoutLocationSelectorActivity.STEP_ID_EXTRA;
import static com.smallworldfs.moneytransferapp.utils.Constants.COUNTRY.FLAG_IMAGE_ASSETS;
import static com.smallworldfs.moneytransferapp.utils.Constants.COUNTRY.FLAG_IMAGE_EXTENSION;

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
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.legacy.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEvent;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventName;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventProperty;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventType;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.DialogExt;
import com.smallworldfs.moneytransferapp.base.presentation.ui.extensions.ImageViewExtKt;
import com.smallworldfs.moneytransferapp.modules.beneficiary.presentation.presenter.NewBeneficiaryPresenter;
import com.smallworldfs.moneytransferapp.modules.beneficiary.presentation.presenter.implementation.NewBeneficiaryPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.calculator.domain.model.Method;
import com.smallworldfs.moneytransferapp.modules.common.domain.model.NewGenericError;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.GenericFormField;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.KeyValueData;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.structure.Step;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.structure.StepStatus;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.QuickReminderMessage;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.manager.ContentStepManager;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.adapter.FormDataAdapter;
import com.smallworldfs.moneytransferapp.presentation.transactional.cashpickup.map.utils.CashPickupResultModel;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.Log;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.hilt.android.AndroidEntryPoint;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

@AndroidEntryPoint
public class NewBeneficiaryActivity extends GenericActivity implements NewBeneficiaryPresenter.View {

    public static final String CONTENT_APPEND_TAG = "content_";
    public static final String EXTRA_KEY = "EXTRA_KEY";
    public static final String EXTRA_VALUE = "EXTRA_VALUE";
    private static final String GENERAL_ERROR_STRUCTURE_VIEW = "GENERAL_ERROR";
    private static final int REQUEST_CONTACTS_PERMISSION_CODE = 200;
    Unbinder mUnbinder;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.genericToolbarTitle)
    TextView mToolbarTitle;
    @BindView(R.id.main_content)
    LinearLayout mMainTransactionalContent;
    @BindView(R.id.loading_view)
    View mLoadingView;
    @BindView(R.id.relative_container)
    RelativeLayout mRelativeContainer;
    @BindView(R.id.main_nested_scroll_view)
    NestedScrollView mMainNestedScrollView;
    @BindView(R.id.country_image)
    ImageView mCountryImage;
    @BindView(R.id.country_text)
    TextView mCountryText;
    @BindView(R.id.clear_focus)
    LinearLayout mClearFocus;
    private NewBeneficiaryPresenterImpl mPresenter;
    private HashMap<Step, View> mBlockStepMap;
    private ContentStepManager mContentStepManager;
    private Dialog mProgressDialog;
    private Handler mHandler;
    private Pair<String, String> mCountry;
    private String beneficiaryType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_beneficiary);
        mUnbinder = ButterKnife.bind(this);

        if (getIntent().getExtras() != null) {
            mCountry = new Pair<>(getIntent().getExtras().getString(EXTRA_KEY, ""), getIntent().getExtras().getString(EXTRA_VALUE, ""));
            beneficiaryType = getIntent().getStringExtra(BENEFICIARY_TYPE);
        }

        mHandler = new Handler(Looper.getMainLooper());

        mPresenter = new NewBeneficiaryPresenterImpl(AndroidSchedulers.mainThread(), Schedulers.io(), this, this, this, mCountry, beneficiaryType);
        mPresenter.create();

        mContentStepManager = new ContentStepManager(this, this, mPresenter);
        mContentStepManager.setMainNestedScrollView(mMainNestedScrollView);
        mContentStepManager.setMainContainer(mMainTransactionalContent);
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
        mClearFocus.requestFocus();
        if (mPresenter != null) {
            mPresenter.pause();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.destroy();
        }

        mUnbinder.unbind();
    }

    /**
     * Configure View Methods
     */
    @Override
    public void configureView() {

        mToolbar.findViewById(R.id.left_title).setVisibility(View.VISIBLE);
        mToolbar.findViewById(R.id.genericToolbarTitle).setVisibility(View.GONE);

        ((StyledTextView) mToolbar.findViewById(R.id.left_title)).setText(getString(R.string.new_beneficiary_title));

        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_back_toolbar_white));
        setSupportActionBar(mToolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);

        // Init Views List
        mBlockStepMap = new HashMap<>();
    }

    /*
     * TRANSACTIONAL CALLBACKS
     */

    @Override
    public void hideGeneralLoadingView() {
        if (mLoadingView != null) {
            mLoadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onStructureError() {
        View errorView = LayoutInflater.from(this).inflate(R.layout.transactional_step_error_view, null, false);
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
        if (step.getStepType().equals(Constants.STEP_TYPE.DELIVERY_METHOD)) {
            trackScreen(ScreenName.SEND_MONEY_CONFIGURATION_SCREEN.getValue());
        }
        if (mBlockStepMap.size() != 0 && idStepAdded(step) && !step.getStepType().equals(Constants.STEP_TYPE.DELIVERY_METHOD)) {
            View stepView = getStepViewAdded(step);
            if (stepView != null) {
                ((StyledTextView) stepView.findViewById(R.id.title)).setText(step.getName());
                stepView.findViewById(R.id.sub_line).setVisibility(isLastPosition ? View.INVISIBLE : View.VISIBLE);
            }
        } else if (!idStepAdded(step)) {

            LinearLayout blockStepContent = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.transactional_step_layout, null, false);
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
     * Draw content inside Delivery Method Step
     */
    @Override
    public void drawContentStep(final Step step, ArrayList<? extends GenericFormField> data, String currentYouPayAmount) {
        LinearLayout blockStepContent = (LinearLayout) mBlockStepMap.get(step);

        if (blockStepContent != null && blockStepContent.findViewWithTag(CONTENT_APPEND_TAG + step.getStepId()) == null) {

            View stepLayout = null;
            switch (step.getStepType()) {
                case Constants.STEP_TYPE.DELIVERY_METHOD: {
                    stepLayout = mContentStepManager.inflateDeliveryMethod(step, data, false);
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
                StyledTextView sendButton = view.findViewById(R.id.sendButton);
                sendButton.setText(getString(R.string.action_done_transactional_calculator));
                sendButton.setEnabled(true);
                sendButton.setAlpha(1f);
            } else {
                TreeMap<String, String> properties = new TreeMap<>();
                properties.put(BrazeEventProperty.DESTINATION_COUNTRY.getValue(), mCountry.first);
                properties.put(BrazeEventProperty.BENEFICIARY_TYPE.getValue(), beneficiaryType);
                properties.put(BrazeEventProperty.DELIVERY_METHOD.getValue(), mContentStepManager.getSelectedDeliveryMethod());
                trackEvent(
                        new BrazeEvent(BrazeEventName.BENEFICIARY_CREATION_STEP_4.getValue(), properties, BrazeEventType.ACTION)
                );
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

            StyledTextView sendButton = view.findViewById(R.id.sendButton);

            switch (step.getStepType()) {
                case Constants.STEP_TYPE.BENEFICIARY_FORM:
                case Constants.STEP_TYPE.FORM:
                    sendButton.setText(R.string.validating_step_text_button);
                    // Disable button
                    sendButton.setEnabled(false);
                    sendButton.setAlpha(.80f);
                    break;
                case Constants.STEP_TYPE.LOCATION_LIST:
                    // Disable button
                    sendButton.setEnabled(false);
                    sendButton.setAlpha(.80f);
                    break;
            }
        }
    }

    @Override
    public void hideValidatingLoadingStepView(Step step) {
        View view = mBlockStepMap.get(step);
        if (view != null) {
            view.findViewById(R.id.step_loading).setVisibility(View.INVISIBLE);
            view.findViewById(R.id.tick_indicator).setVisibility(View.VISIBLE);
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

                sendButton.setText(R.string.done_action);
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

    @Override
    public void notifyGlobalChanges(Step step) {
        switch (step.getStepType()) {
            case Constants.STEP_TYPE.BENEFICIARY_FORM:
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
                        Log.e("STACK", "----------------------", e);
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
        // not aplicate
    }

    @Override
    public void setDeliveryMethodAutoSelectedInAdapter(Step step, Method method) {
        // not aplicate
    }

    @Override
    public void showQuickReminderPopup(String title, ArrayList<QuickReminderMessage> messages) {
        // not aplicate
    }

    @Override
    public void configureStaticCalculator(String payoutCountryKey, String currentBeneficiaryAmount, String payoutCurrency) {
        // not aplicate
    }

    @Override
    public void showCalculator(int side) {
        // not aplicate
    }

    @Override
    public void closeCalculator() {
        // not aplicate
    }

    @Override
    public void showCalculatorLoadingView(boolean show) {
        // not aplicate
    }

    @Override
    public void showYouPayCalculated(String youPayAmount, String youPayCurrency) {
        // not aplicate
    }

    @Override
    public void showPayoutBottomCalculator(String payoutPrincipal, String payoutPrincipalCurrency, String principal, String principalCurrency) {
        // not aplicate
    }

    @Override
    public void trackBeginCheckout() {

    }

    @Override
    public void onDeliveryMethodSelected(String deliveryMethod) {

    }

    @Override
    public void onFieldClicked(Field field) {

    }

    @Override
    public void onStepCompleted(Step validatedStep) {

    }

    @Override
    public void onStepError(Step step, String error) {

    }

    @Override
    public void onChoosePickUpLocation(CashPickupResultModel cashPickupResultModel) {

    }

    @Override
    public void onClickMore() {

    }

    @Override
    public void onChangeClicked() {

    }

    @Override
    public void registerAddToCartEvent() {

    }

    @Override
    public String getBeneficiaryCountry() {
        return mCountry.first != null ? mCountry.first : "";
    }

    @Override
    public void finishWithSuccess(String beneficiaryName) {
        HashMap<String, String> properties = new HashMap<>();
        properties.put(BrazeEventProperty.DESTINATION_COUNTRY.getValue(), mCountry.first);
        properties.put(BrazeEventProperty.BENEFICIARY_TYPE.getValue(), beneficiaryType);
        properties.put(BrazeEventProperty.DELIVERY_METHOD.getValue(), mContentStepManager.getSelectedDeliveryMethod());
        properties.put(BrazeEventProperty.BENEFICIARY_FULL_NAME.getValue(), beneficiaryName);

        registerBrazeEvent(BrazeEventName.BENEFICIARY_CREATED.getValue(), properties);

        setResult(Activity.RESULT_OK);
        finish();
    }

    @Override
    public void trackBrazeError(NewGenericError.ErrorType error, String beneficiaryName) {
        HashMap<String, String> properties = new HashMap<>();
        properties.put(BrazeEventProperty.DESTINATION_COUNTRY.getValue(), mCountry.first);
        properties.put(BrazeEventProperty.BENEFICIARY_TYPE.getValue(), beneficiaryType);
        properties.put(BrazeEventProperty.DELIVERY_METHOD.getValue(), mContentStepManager.getSelectedDeliveryMethod());
        properties.put(BrazeEventProperty.BENEFICIARY_FULL_NAME.getValue(), beneficiaryName);
        properties.put(BrazeEventProperty.ERROR_REASON.getValue(), error.name());

        registerBrazeEvent(BrazeEventName.BENEFICIARY_KO.getValue(), properties);
    }

    @Override
    public void checkAuthenticatedUser(Step step, User user) {
        // not aplicate
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

    @SuppressWarnings({"deprecation", "ToArrayCallWithZeroLengthArrayArgument"})
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


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CONTACTS_PERMISSION_CODE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                // permission was granted, yay! Do the
                // contacts-related task you need to do.
                mPresenter.permissionsGranted();
            } else {

                // permission denied, boo! Disable the
                // functionality that depends on this permission.
                mPresenter.permissionsDenied();
            }
        }
    }


    // On Activity Result

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Constants.REQUEST_CODES.GENERIC_DROP_SELECTOR_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {

                KeyValueData keyValueSelected = data.getParcelableExtra(RESULT_DATA);
                String stepId = data.getStringExtra(CONTENT_STEP_ID);
                int positionField = data.getIntExtra(CONTENT_FIELD_POSITION, -1);

                mPresenter.onDropContentSelected(keyValueSelected, stepId, positionField);
            }
        } else if (requestCode == Constants.REQUEST_CODES.INFO_CONTACTS_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                mPresenter.onContactSelected(data.getData());
            }
        } else if (requestCode == Constants.REQUEST_CODES.SELECT_LOCATION_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {
                mPresenter.onPayoutLocationSelected((Field) Objects.requireNonNull(data.getExtras()).getParcelable(RESULT_DATA), data.getExtras().getString(STEP_ID_EXTRA, ""));
            }
        }
    }

    @Override
    public void configureCountryView(Pair<String, String> country) {

        if (country != null) {
            ImageViewExtKt.loadCircularImage(
                    mCountryImage,
                    this,
                    R.drawable.placeholder_country_adapter,
                    FLAG_IMAGE_ASSETS + country.first + FLAG_IMAGE_EXTENSION
            );

            mCountryText.setText(country.second);
        }
    }

    @Override
    public void registerBrazeEvent(String eventName, HashMap<String, String> eventProperties) {
        trackEvent(
                new BrazeEvent(eventName, eventProperties, BrazeEventType.ACTION)
        );

    }
}
