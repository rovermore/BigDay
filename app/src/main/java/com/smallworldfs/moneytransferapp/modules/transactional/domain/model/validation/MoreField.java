package com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation;

import com.google.gson.annotations.SerializedName;

/**
 * Created by luismiguel on 31/8/17.
 */

public class MoreField {

    @SerializedName("inputs")
    private FormData formData;

    public FormData getFormData() {
        return formData;
    }

    public void setFormData(FormData formData) {
        this.formData = formData;
    }
}
