package com.smallworldfs.moneytransferapp.modules.country.presentation.presenter;

import androidx.core.util.Pair;
import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.BasePresenter;
import java.util.ArrayList;

/**
 * Created by luismiguel on 14/6/17
 */
public interface SelectCountryDialogPresenter extends BasePresenter {

    interface View {
        void fillCountriesInDialog(ArrayList<Pair<String, String>> listData);
        void configureView();
        void showSearchEmptyView();
        void hideSearchEmptyView();
        void onFilterApplied(ArrayList<Pair<String, String>> mListCountries);
        void clearEditText();
        void hideLoadingView();
        void closeWindow();
    }
}
