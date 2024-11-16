package com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity;

import static com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.activity.LocationMapActivity.PAYOUT_LOCATIONS_EXTRA;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenName;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Payout;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter.PayoutLocationSelectorPresenter;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter.implementation.PayoutLocationSelectorPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.transactional.presentation.ui.adapter.PayoutLocationAdapter;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.ConstantsKt;
import com.smallworldfs.moneytransferapp.utils.RecyclerViewPaginationManager;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.hilt.android.AndroidEntryPoint;

/**
 * Created by luismiguel on 25/8/17
 */
@AndroidEntryPoint
public class PayoutLocationSelectorActivity extends GenericActivity implements PayoutLocationSelectorPresenter.View {

    //public static final String ACTIVITY_DATA_KEY = "ACTIVITY_DATA_KEY";
    public static final String ACTIVITY_TITLE = "ACTIVITY_TITLE";
    public static final String STEP_ID_EXTRA = "STEP_ID_EXTRA";
    public static final String ITEM_SELECTED_EXTRA = "ITEM_SELECTED_EXTRA";
    public static final String NAME_SELECTED_EXTRA = "NAME_SELECTED_EXTRA";
    public static final String TAX_SELECTED_EXTRA = "TAX_SELECTED_EXTRA";
    public static final String RESULT_DATA = "RESULT";
    public static final String LOCATION_TEXT = "LOCATION_TEXT";
    public static final String PAYOUT_TYPE = "PAYOUT_TYPE";

    public static final String BANK_DEPOSIT_SCREEN_TAG = "BANK_DEPOSIT_SCREEN_TAG";
    public static final String MOBILE_WALLET_SCREEN_TAG = "MOBILE_WALLET_SCREEN_TAG";

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerview)
    RecyclerView mRecyclerView;
    @BindView(R.id.change_button)
    View mChangeButton;
    @BindView(R.id.location_text)
    StyledTextView mLocationTextView;

    private Unbinder mUnbinder;
    private PayoutLocationSelectorPresenterImpl mPresenter;
    private String mTitle;
    private String mStepId;
    private String mItemSelectedId;
    private String mNameSelected;
    private double mTaxSelected;
    private String mLocationText;
    private String payoutType;
    private PayoutLocationAdapter mAdapter;
    public static ArrayList<Field> mData;
    private String screenName = ConstantsKt.STRING_EMPTY;
    private String processCategory = ConstantsKt.STRING_EMPTY;
    private String checkoutStep = ConstantsKt.STRING_EMPTY;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payout_selector);

        mUnbinder = ButterKnife.bind(this);
        if (getIntent().getExtras() != null) {
            mTitle = getIntent().getStringExtra(ACTIVITY_TITLE);
            mStepId = getIntent().getStringExtra(STEP_ID_EXTRA);
            mItemSelectedId = getIntent().getStringExtra(ITEM_SELECTED_EXTRA);
            mNameSelected = getIntent().getStringExtra(NAME_SELECTED_EXTRA);
            mTaxSelected = getIntent().getDoubleExtra(TAX_SELECTED_EXTRA, 0);
            mLocationText = getIntent().getStringExtra(LOCATION_TEXT);
            payoutType = getIntent().getExtras().getString(PAYOUT_TYPE, "");
            mPresenter = new PayoutLocationSelectorPresenterImpl(null, this, this);
            mPresenter.create();
            if (mData != null) {
                mPresenter.onPayouLocationsLoaded(mData);
            }
            if (payoutType.equalsIgnoreCase(Constants.DELIVERY_METHODS.BANK_DEPOSIT)) {
                screenName = ScreenName.BANK_DEPOSIT_PAYERS_SCREEN.getValue();
                processCategory = "bank_deposit";
                checkoutStep = "1_checkoutPayer";
            }
            if (payoutType.equalsIgnoreCase(Constants.DELIVERY_METHODS.MOBILE_WALLET)) {
                screenName = ScreenName.AVAILABLE_PAYERS_SCREEN.getValue();
                processCategory = "mobile_wallet";
                checkoutStep = "1_checkoutPayer";
            }
            if (payoutType.equalsIgnoreCase(Constants.DELIVERY_METHODS.CASH_CARD_APP_PHYSICAL)) {
                screenName = ScreenName.PICKUP_LOCATIONS_SCREEN.getValue();
                processCategory = "home_delivery";
                checkoutStep = "1_choosePickupLocation";
            }
            trackScreen(screenName);
        }
    }


    @Override
    public void onBackPressed() {
        cancelAction();
        PayoutLocationSelectorActivity.this.overridePendingTransition(R.anim.from_left_to_right, R.anim.from_position_to_right);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (payoutType.equals(Constants.DELIVERY_METHODS.BANK_DEPOSIT)) trackScreen(BANK_DEPOSIT_SCREEN_TAG);
        else if (payoutType.equals(Constants.DELIVERY_METHODS.MOBILE_WALLET)) trackScreen(MOBILE_WALLET_SCREEN_TAG);

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
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mPresenter != null) {
            mPresenter.destroy();
        }
        mUnbinder.unbind();
    }

    @Override
    public void configureView() {

        mToolbar.findViewById(R.id.left_title).setVisibility(View.VISIBLE);
        mToolbar.findViewById(R.id.genericToolbarTitle).setVisibility(View.GONE);
        ((StyledTextView) mToolbar.findViewById(R.id.left_title)).setText(mTitle);
        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_toolbar_white));
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mChangeButton.setVisibility(View.INVISIBLE);
        mLocationTextView.setText(mLocationText);
    }

    @Override
    public void configureDataInAdapter(ArrayList<Field> payoutLocations, String currency) {
        mAdapter = new PayoutLocationAdapter(this, mItemSelectedId, currency, mNameSelected,
                mTaxSelected);
        //mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemViewCacheSize(20);
        mRecyclerView.setDrawingCacheEnabled(true);
        mRecyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        final RecyclerViewPaginationManager<Field> paginationManager =
                new RecyclerViewPaginationManager<>(payoutLocations, 100, 20);
        paginationManager.attach(mRecyclerView, mAdapter);

        mAdapter.setPayoutListener(new PayoutLocationAdapter.PayoutListener() {
            @Override
            public void onSelectPayoutListener(Field field) {
                if (field != null) {
                    returnData(field);
                }
            }

            @Override
            public void onPayoutMapListener(Field field) {
                if (field != null && field.getPayout() != null){
                    showMapLocations(field.getPayout());
                }
            }
        });
    }


    private void cancelAction() {
        Intent returnIntent = new Intent();
        setResult(Activity.RESULT_CANCELED, returnIntent);
        finish();
    }

    private void returnData(Field field) {
        Intent returnIntent = new Intent();
        returnIntent.putExtra(RESULT_DATA, field);
        returnIntent.putExtra(STEP_ID_EXTRA, mStepId);
        registerEvent("click_payers_list", field.getPayout().getLocationName());
        setResult(Activity.RESULT_OK, returnIntent);
        finish();
        PayoutLocationSelectorActivity.this.overridePendingTransition(R.anim.from_left_to_right, R.anim.from_position_to_right);
    }

    private void showMapLocations(Payout payout) {
        Activity currentActivity = PayoutLocationSelectorActivity.this;
        Intent intent = new Intent(currentActivity, LocationMapActivity.class);
        intent.putExtra(PAYOUT_LOCATIONS_EXTRA, payout);
        intent.putExtra(ACTIVITY_TITLE, SmallWorldApplication.getStr(R.string.locations_maps_title_activity));

        currentActivity.startActivity(intent);
    }

    private void registerEvent(String eventAction, String eventLabel) {
        trackEvent(new UserActionEvent(
                ScreenCategory.TRANSFER.getValue(),
                eventAction,
                eventLabel,
                getHierarchy(screenName),
                "",
                processCategory,
                checkoutStep,
                "",
                "",
                ""
        ));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            registerEvent("click_back", "");
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
