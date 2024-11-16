package com.smallworldfs.moneytransferapp.modules.status.domain.model;

/**
 * Created by luismiguel on 9/10/17.
 */

public class PayNowUrlResponse {

    private String msg;

    private PaymentUrl paymentUrl;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public PaymentUrl getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(PaymentUrl paymentUrl) {
        this.paymentUrl = paymentUrl;
    }
}

