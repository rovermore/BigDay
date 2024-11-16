package com.smallworldfs.moneytransferapp.modules.country.presentation.ui.fragment;

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
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.util.Pair;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericDialogFragment;
import com.smallworldfs.moneytransferapp.modules.country.presentation.presenter.SelectCountryDialogPresenter;
import com.smallworldfs.moneytransferapp.modules.country.presentation.presenter.implementation.SelectCountryCodeDialogPresenterImpl;
import com.smallworldfs.moneytransferapp.modules.customization.presentation.ui.adapter.CountryListAdapter;
import com.smallworldfs.moneytransferapp.utils.widget.DismissibleEditText;
import com.smallworldfs.moneytransferapp.utils.widget.StyledTextView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by luismiguel on 14/6/17
 */
public class SelectCodeCountryDialogFragment extends GenericDialogFragment implements SelectCountryDialogPresenter.View, TextWatcher {

    Unbinder mUnbinder;
    private SelectCountryCodeDialogPresenterImpl mPresenter;
    private CountryListAdapter mAdapter;
    private CountrySelectedInterface mCountryCodeListener;

    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;
    @BindView(R.id.root_view)
    LinearLayout mRootView;
    @BindView(R.id.search_edit_text)
    DismissibleEditText mSearchEditText;
    @BindView(R.id.search_icon)
    ImageView mSearchIcon;
    @BindView(R.id.search_empty_view)
    View mEmptyView;
    @BindView(R.id.loading_view)
    View mLoadingView;

    public interface CountrySelectedInterface {
        void onCountryCodeSelected(Pair<String, String> selectedCountry);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof CountrySelectedInterface) {
            mCountryCodeListener = (CountrySelectedInterface) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment_select_country_code, null, false);
        mUnbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mPresenter = new SelectCountryCodeDialogPresenterImpl(null, getContext(), this, mCountryCodeListener);
        mPresenter.create();
    }

    @Override
    public void onStart() {
        super.onStart();
        //getDialog().getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        mRootView.animate().alpha(1).start();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null)
            mPresenter.destroy();
        if (mUnbinder != null)
            mUnbinder.unbind();
    }

    @Override
    public void configureView() {

        mToolbar.findViewById(R.id.left_title).setVisibility(View.VISIBLE);
        mToolbar.findViewById(R.id.genericToolbarTitle).setVisibility(View.GONE);

        ((StyledTextView) mToolbar.findViewById(R.id.left_title)).setTextColor(getResources().getColor(R.color.white));
        ((StyledTextView) mToolbar.findViewById(R.id.left_title)).setText(getString(R.string.country_calling_code));
        //mToolbar.setBackgroundColor(getResources().getColor(R.color.main_blue));

        mToolbar.setNavigationIcon(getResources().getDrawable(R.drawable.ic_back_toolbar_white));
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeWindow();
            }
        });

        Typeface type = ResourcesCompat.getFont(requireActivity(), R.font.nunito_semi_bold);
        mSearchEditText.setTypeface(type);

        // Search Edit TextWatcher Listener
        mSearchEditText.addTextChangedListener(this);

        LinearLayoutManager llManager = new LinearLayoutManager(getActivity());
        mAdapter = new CountryListAdapter();

        mAdapter.setCountryClickListener(new CountryListAdapter.OnCountrySelectedListener() {
            @Override
            public void onCountrySelected(Pair<String, String> country) {
                mPresenter.onCountrySelected(country);
            }
        });

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(llManager);
        mRecyclerView.setAdapter(mAdapter);

        mPresenter.getData();
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
    public void onFilterApplied(ArrayList<Pair<String, String>> listCountries) {
        if (mAdapter != null) mAdapter.updateData(listCountries, true);
    }

    @Override
    public void clearEditText() {
        mSearchEditText.setText("");
    }

    @Override
    public void hideLoadingView() {
        mLoadingView.setVisibility(View.GONE);
    }

    @Override
    public void fillCountriesInDialog(ArrayList<Pair<String, String>> listData) {
        mAdapter.updateData(listData, true);
    }

    @Override
    public void onTextChanged(CharSequence text, int start, int before, int count) {
        mSearchIcon.setImageDrawable(TextUtils.isEmpty(text)
                ? getResources().getDrawable(R.drawable.icn_search_icon)
                : getResources().getDrawable(R.drawable.icn_close_search));
        mSearchIcon.setClickable(!TextUtils.isEmpty(text));
        mPresenter.performSearch(text.toString());
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void afterTextChanged(Editable s) {
    }

    @OnClick(R.id.search_icon)
    public void onClearSearchTextClick() {
        mPresenter.onClearSearchClick();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (getActivity() != null) getActivity().onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void closeWindow() {
        if (getFragmentManager() != null)
            getFragmentManager().popBackStack();
    }

    public void setOnCountrySelectedListener(@NotNull CountrySelectedInterface countryPrefixSelectedListener) {
        this.mCountryCodeListener = countryPrefixSelectedListener;
    }
}
