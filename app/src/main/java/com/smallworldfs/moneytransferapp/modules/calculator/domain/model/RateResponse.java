package com.smallworldfs.moneytransferapp.modules.calculator.domain.model;

/**
 * Created by luismiguel on 28/6/17.
 */

public class RateResponse {

    private String msg;

    private RateValues result;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public RateValues getResult() {
        return result;
    }

    public void setResult(RateValues result) {
        this.result = result;
    }

}
