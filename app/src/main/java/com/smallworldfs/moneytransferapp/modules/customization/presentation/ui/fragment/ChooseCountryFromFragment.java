package com.smallworldfs.moneytransferapp.modules.customization.presentation.ui.fragment;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.modules.customization.presentation.presenter.ChooseCountryFromPresenter;
import com.smallworldfs.moneytransferapp.modules.customization.presentation.presenter.implementation.ChooseCountryFromImpl;
import com.smallworldfs.moneytransferapp.modules.customization.presentation.ui.adapter.CountryListAdapter;
import com.smallworldfs.moneytransferapp.utils.KeyboardUtils;
import com.smallworldfs.moneytransferapp.utils.widget.DismissibleEditText;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import rx.android.schedulers.AndroidSchedulers;

public class ChooseCountryFromFragment extends Fragment implements ChooseCountryFromPresenter.View, TextWatcher {

    private ChooseCountryFromImpl mPresenter;
    private Unbinder mUnbinder;
    private CountryListAdapter mAdapter;


    @BindView(R.id.toolbar)
    public Toolbar mToolbar;
    @BindView(R.id.recyclerview)
    public RecyclerView mRecyclerView;
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

    public static class EventRefreshCountry {
        public Pair<String, String> country;
        public EventRefreshCountry(Pair<String, String> country) {
            this.country = country;
        }
    }


    CountrySelectedListener countrySelectedListener;

    public interface CountrySelectedListener {
        void onCountrySelected(Pair<String, String> country);
    }

    @Override
    public void onAttach(@NonNull @NotNull Activity activity) {
        super.onAttach(activity);
        try {
            countrySelectedListener = (CountrySelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.getClass().toString() + " must implement CountrySelectedListener");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_free_user_country_from, container, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new ChooseCountryFromImpl(AndroidSchedulers.mainThread(), getActivity(), this);
        mPresenter.create();
        Typeface type = ResourcesCompat.getFont(requireActivity(), R.font.nunito_semi_bold);
        mSearchEditText.setTypeface(type);
        configureView();
    }

    @Override
    public void configureView() {

        mToolbar.findViewById(R.id.left_title).setVisibility(View.VISIBLE);
        mToolbar.findViewById(R.id.genericToolbarTitle).setVisibility(View.GONE);

        ((StyledTextView) mToolbar.findViewById(R.id.left_title)).setTextColor(getResources().getColor(R.color.white));
        ((StyledTextView) mToolbar.findViewById(R.id.left_title)).setText(getString(R.string.send_money_from));
        mToolbar.setBackgroundColor(getResources().getColor(R.color.main_blue));

        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_toolbar_white));
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        // Configure search container and listeners
        Typeface type = ResourcesCompat.getFont(requireActivity(), R.font.nunito_semi_bold);
        mSearchTextTitle.setText(getString(R.string.title_search_empty_view));

        mSearchEditText.setTypeface(type);

        // Search Edit TextWatcher Listener
        mSearchEditText.addTextChangedListener(this);

        // Set visible loading view
        mLoadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mPresenter != null) {
            mPresenter.resume();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mPresenter != null) {
            mPresenter.pause();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        KeyboardUtils.hideKeyboard(getActivity());
        mSearchEditText.removeTextChangedListener(this);
        if (mPresenter != null)
            mPresenter.destroy();
        if (mUnbinder != null)
            mUnbinder.unbind();
    }

    @Override
    public void fillCountriesInDialog(ArrayList<Pair<String, String>> listData) {

        Typeface type = ResourcesCompat.getFont(requireActivity(), R.font.nunito_semi_bold);
        mSearchEditText.setTypeface(type);

        // Search Edit TextWatcher Listener
        mSearchEditText.addTextChangedListener(this);

        Context context = getActivity();//You should not use getContext() if API < 23 : https://issuetracker.google.com/issues/37060988
        LinearLayoutManager llManager = new LinearLayoutManager(context);
        mAdapter = new CountryListAdapter();
        mAdapter.setCountryClickListener(new CountryListAdapter.OnCountrySelectedListener() {
            @Override
            public void onCountrySelected(Pair<String, String> country) {
                mPresenter.onCountrySelected(country);
                countrySelectedListener.onCountrySelected(country);
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(llManager);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.updateData(listData, true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

    @Override
    public void onTextChanged(CharSequence text, int start, int before, int count) {
        mSearchIcon.setImageDrawable(TextUtils.isEmpty(text) ? getResources().getDrawable(R.drawable.icn_search_icon) :
                getResources().getDrawable(R.drawable.icn_close_search));
        mSearchIcon.setClickable(!TextUtils.isEmpty(text));
        mPresenter.performSearch(text.toString());
    }

    @Override
    public void afterTextChanged(Editable s) { }

    @Override
    public void showSearchEmptyView() { mEmptyView.setVisibility(View.VISIBLE); }

    @Override
    public void hideSearchEmptyView() { mEmptyView.setVisibility(View.GONE); }

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
        if (mAdapter != null) mAdapter.updateData(listCountries, true);
    }

    @Override
    public void close() {
        getFragmentManager().popBackStack();
    }
}
