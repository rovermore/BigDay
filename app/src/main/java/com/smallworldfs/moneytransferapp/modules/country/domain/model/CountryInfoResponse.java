package com.smallworldfs.moneytransferapp.modules.country.domain.model;

/**
 * Created by luismiguel on 26/6/17.
 */

public class CountryInfoResponse {

    private String msg;

    private CountryInfo country;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public CountryInfo getCountry() {
        return country;
    }

    public void setCountry(CountryInfo country) {
        this.country = country;
    }
}
