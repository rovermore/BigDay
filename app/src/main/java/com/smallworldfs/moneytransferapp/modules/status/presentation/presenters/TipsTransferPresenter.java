package com.smallworldfs.moneytransferapp.modules.status.presentation.presenters;

import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.BasePresenter;
import com.smallworldfs.moneytransferapp.modules.status.domain.model.TipInfo;

import java.util.ArrayList;

/**
 * Created by luismiguel on 27/10/17.
 */

public interface TipsTransferPresenter extends BasePresenter {

    interface View {
        void configureView();
        void showHideLoadingView(boolean show);
        void drawAdditionalTips(ArrayList<TipInfo> listTip);
    }
}
