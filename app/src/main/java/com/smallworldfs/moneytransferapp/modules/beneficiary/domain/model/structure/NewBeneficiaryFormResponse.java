package com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.structure;

import com.google.gson.annotations.SerializedName;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.FormData;

/**
 * Created by luismiguel on 18/7/17.
 */

public class NewBeneficiaryFormResponse {

    private String msg;

    @SerializedName("data")
    private FormData form;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public FormData getForm() {
        return form;
    }

    public void setForm(FormData form) {
        this.form = form;
    }
}
