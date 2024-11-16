package com.smallworldfs.moneytransferapp.modules.status.domain.model;

/**
 * Created by luis on 6/10/17.
 */

public class ChangePaymentMethodResponse {

    private String msg;

    private String text;

    public ChangePaymentMethodResponse(String msg, String text) {
        this.msg = msg;
        this.text = text;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
