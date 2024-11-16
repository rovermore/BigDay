package com.smallworldfs.moneytransferapp.modules.c2b.domain.interactors;

import com.smallworldfs.moneytransferapp.modules.c2b.domain.model.C2BResponse;

public interface C2BInteractor {

    interface Callback {
        void onSuccessful(C2BResponse c2BResponse);

        void onError();
    }
}
