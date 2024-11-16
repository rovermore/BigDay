package com.smallworldfs.moneytransferapp.modules.beneficiary.presentation.presenter;

import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.BasePresenter;

import java.util.ArrayList;

/**
 * Created by pedro del castillo on 9/9/17.
 */

public interface NewBeneficiaryStepCountryPresenter extends BasePresenter {
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
