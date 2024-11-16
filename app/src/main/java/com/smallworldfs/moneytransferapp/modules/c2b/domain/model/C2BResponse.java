
package com.smallworldfs.moneytransferapp.modules.c2b.domain.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class C2BResponse {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("types")
    @Expose
    private List<Type> types = null;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<Type> getTypes() {
        return types;
    }

    public void setTypes(List<Type> types) {
        this.types = types;
    }

}
