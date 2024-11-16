package com.smallworldfs.moneytransferapp.modules.status.presentation.presenters;

import android.content.Intent;
import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.BasePresenter;

import java.util.ArrayList;

public interface CallUsLimitedUserPresenter extends BasePresenter {

    int FIELD_NAME = 0;
    int FIELD_PHONE = 1;
    int FIELD_MAX = 2;

    interface View {
        void configureView();
        void showHideLoadingView(boolean show);
        void showHideGeneralErrorView(boolean show);
        void onFormErrors(ArrayList<Pair<Integer, String>> listErrors);
        void clearErrorIndicatorInForm();
        void showLoadingDialog(boolean show);
        void showTopErrorView();
        void hideTopErrorView();
        void showContentLayout();
        void onCallUsFinish();

        void startActivityForResult(Intent intent, int requestCode);
        void showCountryPhoneSelector();
        void updateCountryPhone(Pair<String, String> country);
        String getName();
        String getPhone();
        void showTextFieldErrors();
    }
}
