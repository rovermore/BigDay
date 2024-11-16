package com.smallworldfs.moneytransferapp.modules.transactional.presentation.presenter;

import com.smallworldfs.moneytransferapp.modules.common.presentation.presenter.BasePresenter;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Payout;

/**
 * Created by luismiguel on 28/8/17.
 */

public interface LocationMapPresenter extends BasePresenter {

    interface View {
        void configureView();
        void hideLoadingView();
        void showMessageError();
        void drawPoisInMap(Payout payout);
    }

}
