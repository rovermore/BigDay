package com.smallworldfs.moneytransferapp.modules.customization.domain.repository;

import androidx.core.util.Pair;

/**
 * Created by luismiguel on 17/5/17.
 */

public class AppCustomizationRepository {

    public static AppCustomizationRepository sInstance = null;

    // Country Prefs
    private Pair<String, String> mCountrySelected;
    private Pair<String, String> mPayoutCountrySelected;
    private Pair<String, String> mCountryCodeSelected;

    public static AppCustomizationRepository getInstance() {
        if (sInstance == null) {
            sInstance = new AppCustomizationRepository();
        }
        return sInstance;
    }


    // Getters and Setters methods

    public Pair<String, String> getCountrySelected() {
        return this.mCountrySelected;
    }

    public Pair<String, String> getPayoutCountrySelected() {
        return this.mPayoutCountrySelected;
    }

    public Pair<String, String> getCountryCodeSelected() {
        return mCountryCodeSelected;
    }


    /**
     * Memory Send From Country
     * @param countrySelected
     */

    public void setCountrySelected(Pair<String, String> countrySelected) {
        this.mCountrySelected = countrySelected;
    }

    /**
     * Memory Send To Country
     * @param payoutCountrySelected
     */

    public void setPayoutCountrySelected(Pair<String, String> payoutCountrySelected) {
        this.mPayoutCountrySelected = payoutCountrySelected;
    }

    /**
     * Country Code Selected, at first time equals to Send From Country
     * @param mCountryCodeSelected
     */

    public void setCountryCodeSelected(Pair<String, String> mCountryCodeSelected) {
        this.mCountryCodeSelected = mCountryCodeSelected;
    }
}
