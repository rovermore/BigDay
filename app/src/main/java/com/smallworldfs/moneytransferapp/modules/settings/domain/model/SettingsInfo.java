package com.smallworldfs.moneytransferapp.modules.settings.domain.model;

/**
 * Created by pedro del castillo on 12/9/17.
 */

public class SettingsInfo {

    private String termsAndConditions;

    private String privacyPolicy;

    private String localPrivacyPolicy;

    private String customerAgreement;

    public String getTermsAndConditions() {
        return termsAndConditions;
    }

    public void setTermsAndConditions(String termsAndConditions) {
        this.termsAndConditions = termsAndConditions;
    }

    public String getPrivacyPolicy() {
        return privacyPolicy;
    }

    public void setPrivacyPolicy(String privacyPolicy) {
        this.privacyPolicy = privacyPolicy;
    }

    public String getLocalPrivacyPolicy() {
        return localPrivacyPolicy;
    }

    public void setLocalPrivacyPolicy(String localPrivacyPolicy) {
        this.localPrivacyPolicy = localPrivacyPolicy;
    }

    public String getCustomerAgreement() {
        return customerAgreement;
    }

    public void setCustomerAgreement(String customerAgreement) {
        this.customerAgreement = customerAgreement;
    }
}
