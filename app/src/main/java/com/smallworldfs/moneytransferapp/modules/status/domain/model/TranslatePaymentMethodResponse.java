package com.smallworldfs.moneytransferapp.modules.status.domain.model;

import java.util.HashMap;

/**
 * Created by luismiguel on 9/10/17.
 */

public class TranslatePaymentMethodResponse {

    private String msg;

    private HashMap<String, String> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public HashMap<String, String> getData() {
        return data;
    }

    public void setData(HashMap<String, String> data) {
        this.data = data;
    }
}
