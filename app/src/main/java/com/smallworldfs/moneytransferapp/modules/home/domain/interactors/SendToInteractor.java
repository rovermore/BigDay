package com.smallworldfs.moneytransferapp.modules.home.domain.interactors;

import androidx.core.util.Pair;

import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.Interactor;

public interface SendToInteractor extends Interactor {
    interface Callback {
        void onHeaderInfoReady(Pair<String, String> mPayoutCountry, Pair<String, String> mBaseCountryOrigin, Pair<String, String> mCountries);

        void onRetrievingHeaderInfoError();
    }
}
