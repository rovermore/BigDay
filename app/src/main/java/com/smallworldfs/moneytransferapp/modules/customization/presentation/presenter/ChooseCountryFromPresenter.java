package com.smallworldfs.moneytransferapp.modules.customization.presentation.presenter;

import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.BasePresenter;

import java.util.ArrayList;

/**
 * Created by ddi-pc-52 on 12.03.18
 */
public interface ChooseCountryFromPresenter extends BasePresenter {

    interface View {
        void configureView();
        void fillCountriesInDialog(ArrayList<Pair<String, String>> listData);
        void showSearchEmptyView();
        void hideSearchEmptyView();
        void onFilterApplied(ArrayList<Pair<String, String>> mListCountries);
        void clearEditText();
        void hideLoadingView();
        void close();
    }
}