package com.smallworldfs.moneytransferapp.modules.calculator.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.TreeMap;

/**
 * Created by pedro del castillo on 15/9/17.
 */

public class DeliveryMethodsResponse {

    private String msg;

    @SerializedName("data")
    private TreeMap<String, String> deliveryMethods;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public TreeMap<String, String> getDeliveryMethods() {
        return deliveryMethods;
    }

    public void setDeliveryMethods(TreeMap<String, String> deliveryMethods) {
        this.deliveryMethods = deliveryMethods;
    }
}
