package com.smallworldfs.moneytransferapp.modules.transactional.domain.model.server;



import android.util.Pair;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.TreeMap;

/**
 * Created by luismiguel on 22/8/17.
 */

public class GenericKeyValueDropContent {

    private String msg;

    private ArrayList<TreeMap<String, String>> data;

    public ArrayList<TreeMap<String, String>> getData() {
        return data;
    }

    public void setData(ArrayList<TreeMap<String, String>> data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
