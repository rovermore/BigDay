package com.smallworldfs.moneytransferapp.modules.flinks.domain.interactor;

import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.Interactor;

public interface FlinksInteractor extends Interactor {

    interface Callback {
        void onFlinksUrlReady(String url);
        void onFlinksUrlError();

        void onFlinksVerificationFinished();
        void onFlinksVerificationError();
    }

}
