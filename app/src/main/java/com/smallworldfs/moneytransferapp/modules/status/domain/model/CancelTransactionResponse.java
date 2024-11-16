package com.smallworldfs.moneytransferapp.modules.status.domain.model;

/**
 * Created by luismiguel on 10/10/17.
 */

public class CancelTransactionResponse {

    private String msg;

    private String text;

    private boolean result;

    public CancelTransactionResponse(String msg, String text) {
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

    public boolean getResult() {
        return result;
    }

    public void setResult(boolean result) {
        this.result = result;
    }
}
