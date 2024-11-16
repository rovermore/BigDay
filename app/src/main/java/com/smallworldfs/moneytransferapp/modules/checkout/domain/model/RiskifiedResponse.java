package com.smallworldfs.moneytransferapp.modules.checkout.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class RiskifiedResponse implements Parcelable {

    private Boolean success;

    private String code;

    private String msg;

    protected RiskifiedResponse(Parcel in) {
        success = in.readByte() == 1;
        code = in.readString();
        msg = in.readString();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeByte((byte) (success ? 1 : 0));
        parcel.writeString(code);
        parcel.writeString(msg);
    }

    public static final Creator<RiskifiedResponse> CREATOR = new Creator<RiskifiedResponse>() {
        @Override
        public RiskifiedResponse createFromParcel(Parcel in) {
            return new RiskifiedResponse(in);
        }

        @Override
        public RiskifiedResponse[] newArray(int size) {
            return new RiskifiedResponse[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
