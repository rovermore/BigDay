package com.smallworldfs.moneytransferapp.modules.calculator.domain.model;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by luismiguel on 27/6/17.
 */

public class CurrenciesResponse {

    private String msg;

    private ArrayList<Method> methods;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<Method> getMethods() {
        return methods;
    }

    public void setMethods(ArrayList<Method> methods) {
        this.methods = methods;
    }
}
