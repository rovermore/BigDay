package com.smallworldfs.moneytransferapp.modules.status.domain.model;

import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.FormData;

/**
 * Created by luismiguel on 5/10/17.
 */

public class PaymentMethodsResponse {

    private String msg;

    private FormData inputs;

    public PaymentMethodsResponse(String msg, FormData inputs) {
        this.msg = msg;
        this.inputs = inputs;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public FormData getInputs() {
        return inputs;
    }

    public void setInputs(FormData inputs) {
        this.inputs = inputs;
    }
}
