package com.smallworldfs.moneytransferapp.modules.flinks.presentation.presenter;

import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.BasePresenter;

public interface FlinksPresenter extends BasePresenter {
    interface View {
        void configureView();
        void showHideLoadingView(boolean show);
        void showHideValidationView(boolean show);
        void startWebViewWithdUrl(String url);
        void showHideGeneralErrorView(boolean show);
        void closeActivity();
    }
}
