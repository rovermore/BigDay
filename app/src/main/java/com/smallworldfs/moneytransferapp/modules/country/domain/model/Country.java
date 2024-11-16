package com.smallworldfs.moneytransferapp.modules.country.domain.model;

import com.smallworldfs.moneytransferapp.utils.CountryUtils;

import java.util.TreeMap;

/**
 * Created by luismiguel on 17/5/17.
 */

public class Country {

    private String msg;

    private TreeMap<String, String> countries;

    public Country() {

    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public TreeMap<String, String> getSortedCountries() {
        return countries;
    }

    public TreeMap<String, String> getCountries() {
        return countries;
    }

    public void setCountries(TreeMap<String, String> countries) {
        this.countries = countries;
    }

}
