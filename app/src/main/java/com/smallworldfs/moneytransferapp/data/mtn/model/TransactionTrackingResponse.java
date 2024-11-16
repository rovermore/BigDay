
package com.smallworldfs.moneytransferapp.data.mtn.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TransactionTrackingResponse {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("original_status")
    @Expose
    private String originalStatus;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("status_msg")
    @Expose
    private String statusMsg;
    @SerializedName("mtn")
    @Expose
    private String mtn;
    @SerializedName("country")
    @Expose
    private String country;
    @SerializedName("steps")
    @Expose
    private List<Status> statuses = null;

    public TransactionTrackingResponse() {}

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getOriginalStatus() {
        return originalStatus;
    }

    public void setOriginalStatus(String originalStatus) {
        this.originalStatus = originalStatus;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public String getMtn() {
        return mtn;
    }

    public void setMtn(String mtn) {
        this.mtn = mtn;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public List<Status> getStatuses() {
        return statuses;
    }

    public void setStatuses(List<Status> statuses) {
        this.statuses = statuses;
    }

}
