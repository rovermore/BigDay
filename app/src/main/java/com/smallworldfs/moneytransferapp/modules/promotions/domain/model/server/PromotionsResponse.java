package com.smallworldfs.moneytransferapp.modules.promotions.domain.model.server;

import com.google.gson.annotations.SerializedName;
import com.smallworldfs.moneytransferapp.modules.promotions.domain.model.Promotion;

import java.util.ArrayList;

/**
 * Created by luismiguel on 30/6/17.
 */

public class PromotionsResponse {

    private String msg;

    @SerializedName("result")
    private ArrayList<Promotion> promotions;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<Promotion> getPromotions() {
        return promotions;
    }

    public void setPromotions(ArrayList<Promotion> promotions) {
        this.promotions = promotions;
    }
}
