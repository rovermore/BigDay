package com.smallworldfs.moneytransferapp.modules.mtn.presentation.presenter;

import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.BasePresenter;

import java.util.ArrayList;

/**
 * Created by pedro del castillo on 9/9/17.
 */

public interface ChooseCountryPresenter extends BasePresenter {
    interface View {
        void configureView();
        void fillCountriesInDialog(ArrayList<Pair<String, String>> listData);
        void showSearchEmptyView();
        void hideSearchEmptyView();
        void onFilterApplied(ArrayList<Pair<String, String>> mListCountries);
        void clearEditText();
        void hideLoadingView();
    }
}
