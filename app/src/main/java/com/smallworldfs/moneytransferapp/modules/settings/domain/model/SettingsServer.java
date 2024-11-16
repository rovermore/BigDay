package com.smallworldfs.moneytransferapp.modules.settings.domain.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pedro del castillo on 13/9/17.
 */

public class SettingsServer {

    private String msg;

    @SerializedName("data")
    private SettingsInfo settingsInfo;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public SettingsInfo getSettingsInfo() {
        return settingsInfo;
    }

    public void setSettingsInfo(SettingsInfo settingsInfo) {
        this.settingsInfo = settingsInfo;
    }
}
