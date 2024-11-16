package com.smallworldfs.moneytransferapp.modules.onboard.presentation.presenter;

import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.BasePresenter;

/**
 * Created by luismiguel on 13/11/17.
 */

public interface WalkthroughPopupPresenter extends BasePresenter {

    public interface View {
        void configureView(String userName);
    }
}
