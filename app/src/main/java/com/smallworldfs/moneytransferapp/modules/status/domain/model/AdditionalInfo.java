package com.smallworldfs.moneytransferapp.modules.status.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by luismiguel on 27/10/17.
 */

public class AdditionalInfo {

    private String msg;

    @SerializedName("data")
    private ArrayList<TipInfo> tips;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<TipInfo> getTips() {
        return tips;
    }

    public void setTips(ArrayList<TipInfo> tips) {
        this.tips = tips;
    }
}
