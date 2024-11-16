package com.smallworldfs.moneytransferapp.modules.beneficiary.presentation.ui.activity;

import static com.smallworldfs.moneytransferapp.modules.c2b.presentation.ui.activity.C2BActivity.FROM_TRANSACTIONAL;
import static com.smallworldfs.moneytransferapp.utils.ConstantsKt.STRING_EMPTY;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEvent;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventName;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventProperty;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.BrazeEventType;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.ScreenCategory;
import com.smallworldfs.moneytransferapp.base.presentation.ui.analytics.UserActionEvent;
import com.smallworldfs.moneytransferapp.modules.beneficiary.presentation.presenter.NewBeneficiaryStepCountryPresenter;
import com.smallworldfs.moneytransferapp.modules.beneficiary.presentation.presenter.implementation.NewBeneficiaryStepCountryPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.modules.customization.presentation.ui.adapter.CountryListAdapter;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.widget.DismissibleEditText;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.hilt.android.AndroidEntryPoint;
import rx.android.schedulers.AndroidSchedulers;

@AndroidEntryPoint
public class NewBeneficiaryStepCountryActivity extends GenericActivity implements NewBeneficiaryStepCountryPresenter.View, TextWatcher {

    private NewBeneficiaryStepCountryPresenterImpl mPresenter;
    Unbinder mUnbinder;
    private CountryListAdapter mAdapter;
    private long lastTimeEventSend = 0L;

    private final int ONE_SECOND = 1000;

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;
    @BindView(R.id.recyclerview)
    public RecyclerView mRecyclerView;
    @BindView(R.id.search_container)
    public RelativeLayout mSearchContainer;
    @BindView(R.id.search_edit_text)
    public DismissibleEditText mSearchEditText;
    @BindView(R.id.search_icon)
    public ImageView mSearchIcon;
    @BindView(R.id.search_empty_view)
    public View mEmptyView;
    @BindView(R.id.error_view_title)
    public StyledTextView mSearchTextTitle;
    @BindView(R.id.general_loading_view)
    public View mLoadingView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_select_drop_content);

        mUnbinder = ButterKnife.bind(this);

        boolean fromTransactional = false;
        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().containsKey(FROM_TRANSACTIONAL)) {
                fromTransactional = getIntent().getBooleanExtra(FROM_TRANSACTIONAL, false);
            }
        }

        mPresenter = new NewBeneficiaryStepCountryPresenterImpl(AndroidSchedulers.mainThread(), this, this, this, fromTransactional);
        mPresenter.create();
    }

    private void registerBrazeEvent(String eventName, Map<String, String> eventProperties) {
        trackEvent(new BrazeEvent(eventName, eventProperties, BrazeEventType.ACTION));
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

        if (mPresenter != null) {
            mPresenter.destroy();
        }

        mUnbinder.unbind();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        registerEvent("click_back", "", "");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODES.NEW_BENEFICIARY && resultCode == RESULT_OK) {
            mPresenter.backToPreviousScreenWithResultOK();
        }
    }

    /**
     * Configure View Methods
     */
    @Override
    public void configureView() {

        mToolbar.findViewById(R.id.left_title).setVisibility(View.VISIBLE);
        mToolbar.findViewById(R.id.genericToolbarTitle).setVisibility(View.GONE);

        ((StyledTextView) mToolbar.findViewById(R.id.left_title)).setTextColor(getResources().getColor(R.color.white));
        ((StyledTextView) mToolbar.findViewById(R.id.left_title)).setText(getString(R.string.new_beneficiary_title));
        mToolbar.setBackgroundColor(getResources().getColor(R.color.main_blue));

        mToolbar.setNavigationIcon(ContextCompat.getDrawable(this, R.drawable.ic_back_toolbar_white));
        setSupportActionBar(mToolbar);

        Objects.requireNonNull(getSupportActionBar()).setDisplayShowHomeEnabled(true);

        // Configure search container and listeners
        Typeface type = ResourcesCompat.getFont(this, R.font.nunito_semi_bold);
        mSearchTextTitle.setText(getString(R.string.title_search_empty_view));

        mSearchEditText.setTypeface(type);

        // Search Edit TextWatcher Listener
        mSearchEditText.addTextChangedListener(this);

        // Set visible loading view
        mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void fillCountriesInDialog(ArrayList<Pair<String, String>> listData) {

        Typeface type = ResourcesCompat.getFont(this, R.font.nunito_semi_bold);
        mSearchEditText.setTypeface(type);

        // Search Edit TextWatcher Listener
        mSearchEditText.addTextChangedListener(this);

        LinearLayoutManager llManager = new LinearLayoutManager(this);
        mAdapter = new CountryListAdapter();
        mAdapter.setBlueSeparatorVisible(true);

        mAdapter.setCountryClickListener(country -> {
            registerEvent("click_beneficiary_country_list", country.second, "");
            TreeMap<String, String> properties = new TreeMap<>();
            properties.put(BrazeEventProperty.DESTINATION_COUNTRY.getValue(), country.first);
            registerBrazeEvent(
                    BrazeEventName.BENEFICIARY_CREATION_STEP_2.getValue(),
                    properties
            );
            mPresenter.onCountryCodeSelected(country);
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(llManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.updateData(listData, false);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence text, int start, int before, int count) {
        mSearchIcon.setImageDrawable(TextUtils.isEmpty(text) ? ContextCompat.getDrawable(mSearchIcon.getContext(), R.drawable.icn_search_icon) : ContextCompat.getDrawable(mSearchIcon.getContext(), R.drawable.icn_close_search));
        mSearchIcon.setClickable(!TextUtils.isEmpty(text));

        mPresenter.performSearch(text.toString());

        if (text.length() > 2 && (System.currentTimeMillis() - lastTimeEventSend) > ONE_SECOND) {
            registerEvent("search_beneficiary_country", text.toString(), "search");
            lastTimeEventSend = System.currentTimeMillis();
        }

        // If the text is empty don't show the blue line
        if (mAdapter != null) {
            mAdapter.setBlueSeparatorVisible(text.toString().equals(STRING_EMPTY));
        }
    }

    @Override
    public void afterTextChanged(Editable s) {

    }

    @Override
    public void showSearchEmptyView() {
        mEmptyView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSearchEmptyView() {
        mEmptyView.setVisibility(View.GONE);

    }

    @Override
    public void clearEditText() {
        mSearchEditText.setText("");
    }

    @Override
    public void hideLoadingView() {
        mLoadingView.setVisibility(View.GONE);
    }

    @OnClick(R.id.search_icon)
    public void onClearSearchTextClick() {
        mPresenter.onClearSearchClick();
    }

    @Override
    public void onFilterApplied(ArrayList<Pair<String, String>> listCountries) {
        if (mAdapter != null) {
            mAdapter.updateData(listCountries, true);
        }
    }

    private void registerEvent(String eventAction, String eventLabel, String formType) {
        trackEvent(new UserActionEvent(
                ScreenCategory.DASHBOARD.getValue(),
                eventAction,
                eventLabel,
                getHierarchy(""),
                formType,
                "",
                "",
                "",
                "",
                ""
        ));
    }
}
