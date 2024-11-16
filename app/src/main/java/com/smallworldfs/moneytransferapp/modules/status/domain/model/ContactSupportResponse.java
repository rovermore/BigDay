package com.smallworldfs.moneytransferapp.modules.status.domain.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by luismiguel on 4/10/17.
 */

public class ContactSupportResponse {

    private String msg;

    @SerializedName("data")
    private ContactSupportInfo contactSupportInfo;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ContactSupportInfo getContactSupportInfo() {
        return contactSupportInfo;
    }

    public void setContactSupportInfo(ContactSupportInfo contactSupportInfo) {
        this.contactSupportInfo = contactSupportInfo;
    }
}
