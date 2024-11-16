package com.smallworldfs.moneytransferapp.modules.country.domain.model;

import java.util.ArrayList;

public class CountryResponse {

    private String msg;

    private ArrayList<CountryModel> countries;

    public CountryResponse() {
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<CountryModel> getCountries() {
        return countries;
    }

    public void setCountries(ArrayList<CountryModel> countries) {
        this.countries = countries;
    }
}
