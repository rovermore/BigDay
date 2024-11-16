package com.smallworldfs.moneytransferapp.modules.c2b;


import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.modules.common.presentation.ui.GenericActivity;

public interface C2BContract {

    interface View {
        void configureView(Pair<String, String> country);

        void showGeneralLoadingView();

        void hideGeneralLoadingView();

        void showErrorView();

        void hideErrorView();

        void hideAllViews();

        void addButton(String beneficiaryType, String beneficiaryLabel, int numberOfButtons);
    }

    interface Presenter {
        void click(String beneficiaryType);
        void retryC2BClick();
        void create();
        void setView(C2BContract.View view, GenericActivity activity, Pair<String, String> country, boolean fromTransactional);
        void backToPreviousScreenWithResultOK();
        void destroy();
        void resume();
        void pause();
    }
}
