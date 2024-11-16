package com.smallworldfs.moneytransferapp.modules.status.presentation.presenters;

import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.BasePresenter;

/**
 * Created by michel on 27/6/18.
 */

public interface LivezillaPresenter extends BasePresenter {
    public interface View {
        void configureView();

        void configureWebView();

        void showHideGeneralLoadingView(boolean show);

        void showHideContentLayout(boolean show);

        void showTopErrorView();
    }
}
