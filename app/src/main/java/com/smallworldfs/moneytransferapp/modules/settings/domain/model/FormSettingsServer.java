package com.smallworldfs.moneytransferapp.modules.settings.domain.model;

import com.smallworldfs.moneytransferapp.modules.register.domain.model.Form;

/**
 * Created by luis on 22/5/18.
 */

public class FormSettingsServer {

    private String msg;

    private Form form;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Form getForm() {
        return form;
    }

    public void setForm(Form form) {
        this.form = form;
    }
}
