package com.smallworldfs.moneytransferapp.modules.transactional.domain.interactors;

import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.Interactor;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.LocationMapResponse;

public interface LocationMapInteractor extends Interactor {

    interface Callback {
        void onLocationsReceived(LocationMapResponse locationMapResponse);
        void onLocationsError();
    }
}
