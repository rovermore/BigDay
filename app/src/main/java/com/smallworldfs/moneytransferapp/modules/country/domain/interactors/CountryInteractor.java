package com.smallworldfs.moneytransferapp.modules.country.domain.interactors;

import com.smallworldfs.moneytransferapp.modules.common.domain.interactors.Interactor;
import com.smallworldfs.moneytransferapp.modules.country.domain.model.Country;
import com.smallworldfs.moneytransferapp.modules.country.domain.model.CountryResponse;

import java.util.TreeMap;

public interface CountryInteractor extends Interactor {

    interface Callback {
        void onOriginCountriesReceived(Country countries);

        void onMostPopularCountriesReceived(CountryResponse countries);

        void onMostPopularErrorCountries();

        void onPayoutCountriesReceived(Country countries);

        void onPayoutCountriesReceivedNotSorted(CountryResponse countries);

        void onPayoutCountriesError();
    }

    interface CountryCodesCallback {
        void onCountryCodesReady(TreeMap<String, String> countries);
    }
}
