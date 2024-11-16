package com.smallworldfs.moneytransferapp.modules.mtn.presentation.ui.activity;

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
import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;
import com.smallworldfs.moneytransferapp.modules.customization.presentation.ui.adapter.CountryListAdapter;
import com.smallworldfs.moneytransferapp.modules.mtn.presentation.presenter.ChooseCountryPresenter;
import com.smallworldfs.moneytransferapp.modules.mtn.presentation.presenter.implementation.ChooseCountryPresenterImpl;
import com.smallworldfs.moneytransferapp.utils.widget.DismissibleEditText;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import dagger.hilt.android.AndroidEntryPoint;
import rx.android.schedulers.AndroidSchedulers;


/**
 * Created by pedro del castillo on 7/9/17.
 */
@AndroidEntryPoint
public class ChooseCountryActivity extends GenericActivity implements ChooseCountryPresenter.View, TextWatcher {

    private ChooseCountryPresenterImpl mPresenter;
    Unbinder mUnbinder;
    private CountryListAdapter mAdapter;

    @BindView(R.id.toolbar)
    public Toolbar mToolbar;
    @BindView(R.id.recyclerview)
    public RecyclerView mRecyclerView;
    @BindView(R.id.search_container)
    public RelativeLayout mSearchContainer;
    @BindView(R.id.search_edit_text)
    public DismissibleEditText mSearchEditText;
    @BindView(R.id.search_icon)
    ImageView mSearchIcon;
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

        mPresenter = new ChooseCountryPresenterImpl(AndroidSchedulers.mainThread(), this, this);
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

        if (mPresenter != null) {
            mPresenter.destroy();
        }

        mUnbinder.unbind();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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
        ((StyledTextView) mToolbar.findViewById(R.id.left_title)).setText(getString(R.string.choose_country_title));
        mToolbar.setBackgroundColor(getResources().getColor(R.color.main_blue));

        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_toolbar_white));
        setSupportActionBar(mToolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Configure search container and listeners
        Typeface type = ResourcesCompat.getFont(this, R.font.nunito_semi_bold);
        mSearchTextTitle.setText(getString(R.string.generic_title_empty_view_search_content));

        mSearchEditText.setTypeface(type);

        // Search Edit TextWatcher Listener
        mSearchEditText.addTextChangedListener(this);
    }

    @Override
    public void fillCountriesInDialog(ArrayList<Pair<String, String>> listData) {

        Typeface type = ResourcesCompat.getFont(this, R.font.nunito_semi_bold);
        mSearchEditText.setTypeface(type);

        // Search Edit TextWatcher Listener
        mSearchEditText.addTextChangedListener(this);

        LinearLayoutManager llManager = new LinearLayoutManager(this);
        mAdapter = new CountryListAdapter();

        mAdapter.setCountryClickListener(new CountryListAdapter.OnCountrySelectedListener() {
            @Override
            public void onCountrySelected(Pair<String, String> country) {
                /*
                if (mCountryCodeListener != null) {
                    mCountryCodeListener.onCountryCodeSelected(country);
                    dismiss();
                }
                */
                mPresenter.onCountryCodeSelected(country);
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(llManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.updateData(listData, true);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence text, int start, int before, int count) {

        mSearchIcon.setImageDrawable(TextUtils.isEmpty(text) ? getResources().getDrawable(R.drawable.icn_search_icon) :
                getResources().getDrawable(R.drawable.icn_close_search));
        mSearchIcon.setClickable(!TextUtils.isEmpty(text));

        mPresenter.performSearch(text.toString());
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
}
