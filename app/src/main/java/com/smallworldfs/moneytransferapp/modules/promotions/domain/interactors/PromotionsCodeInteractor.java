package com.smallworldfs.moneytransferapp.modules.promotions.domain.interactors;

import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.Interactor;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.Promotion;

/**
 * Created by luis on 29/6/17.
 */

public interface PromotionsCodeInteractor extends Interactor {

    interface Callback {
        void onUserChangeSelectedPromotion();
        void onPromotionInvalid();
        void onPromotionCheckedSusccessfull();
    }

}
