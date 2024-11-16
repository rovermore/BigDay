package com.smallworldfs.moneytransferapp.modules.status.presentation.presenters;

import android.content.Intent;
import androidx.core.util.Pair;
import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.BasePresenter;
import java.util.ArrayList;

public interface SendEmailLimitedUserPresenter extends BasePresenter {
    int CODE_HOW_CAN_WE_HELP = 691;
    int CODE_STATE = 692;

    int FIELD_HOW = 0;
    int FIELD_COUNTRY = 1;
    int FIELD_STATE = 2;
    int FIELD_NAME = 3;
    int FIELD_EMAIL = 4;
    int FIELD_PHONE = 5;
    int FIELD_MAX = 6;

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
        void onEmailSent();

        //void setSendEmailEnabled(boolean enabled);

        void startActivityForResult(Intent intent, int requestCode);
        void showCountryFromSelector();
        void showCountryPhoneSelector();
        void updateHowCanHelpYou(String subject);
        void updateCountryFrom(Pair<String, String> country);
        void updateCountryPhone(Pair<String, String> country);
        void updateState(String state);
        void setStateEnabled(boolean b);
        String getName();
        String getEmail();
        String getPhone();
    }
}
