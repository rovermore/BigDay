package com.smallworldfs.moneytransferapp.modules.status.presentation.presenters;

import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.BasePresenter;

import java.util.ArrayList;

/**
 * Created by luismiguel on 4/10/17
 */
public interface SendEmailPresenter extends BasePresenter {
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
        void getBack();
    }
}
