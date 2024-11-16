package com.smallworldfs.moneytransferapp.modules.calculator.domain.model;

import android.os.Parcel;

import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.GenericFormField;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by luismiguel on 27/6/17.
 */

public class Method extends GenericFormField {

    private TreeMap<String, String> method;

    private ArrayList<TreeMap<String, String>> currencies;

    protected Method(Parcel in) {
        super(in);
    }

    public Method(TreeMap<String, String> deliveryMethod) {
        this.method = deliveryMethod;
    }

    public TreeMap<String, String> getMethod() {
        return method;
    }

    public void setMethod(TreeMap<String, String> method) {
        this.method = method;
    }

    public ArrayList<TreeMap<String, String>> getCurrencies() {
        return currencies;
    }

    public void setCurrencies(ArrayList<TreeMap<String, String>> currencies) {
        this.currencies = currencies;
    }
}
