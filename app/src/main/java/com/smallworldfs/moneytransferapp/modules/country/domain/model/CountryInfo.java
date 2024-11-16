package com.smallworldfs.moneytransferapp.modules.country.domain.model;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by luismiguel on 26/6/17.
 */

public class CountryInfo {

    private String documentId;

    private ArrayList<String> sender;

    private boolean register;

    private boolean login;

    private String trustpilot;

    private ArrayList<Bank> banks;

    @SerializedName("webservice")
    private WebService webService;

    private TreeMap<String, String> currencies;

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public ArrayList<String> getSender() {
        return sender;
    }

    public void setSender(ArrayList<String> sender) {
        this.sender = sender;
    }

    public boolean isRegister() {
        return register;
    }

    public void setRegister(boolean register) {
        this.register = register;
    }

    public boolean isLogin() {
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public String getTrustpilot() {
        return trustpilot;
    }

    public void setTrustpilot(String trustpilot) {
        this.trustpilot = trustpilot;
    }

    public ArrayList<Bank> getBanks() {
        return banks;
    }

    public void setBanks(ArrayList<Bank> banks) {
        this.banks = banks;
    }

    public WebService getWebService() {
        return webService;
    }

    public void setWebService(WebService webService) {
        this.webService = webService;
    }

    public TreeMap<String, String> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(TreeMap<String, String> currencies) {
        this.currencies = currencies;
    }
}
