package com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by luismiguel on 16/10/17.
 */

public class PurposeInfo implements Parcelable{

    @SerializedName("mtn_purpose")
    private String mtnPurpose;

    private String clientRelation;

    public PurposeInfo(String mtnPurpose, String clientRelation) {
        this.mtnPurpose = mtnPurpose;
        this.clientRelation = clientRelation;
    }

    protected PurposeInfo(Parcel in) {
        mtnPurpose = in.readString();
        clientRelation = in.readString();
    }

    public static final Creator<PurposeInfo> CREATOR = new Creator<PurposeInfo>() {
        @Override
        public PurposeInfo createFromParcel(Parcel in) {
            return new PurposeInfo(in);
        }

        @Override
        public PurposeInfo[] newArray(int size) {
            return new PurposeInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    public String getMtnPurpose() {
        return mtnPurpose;
    }

    public void setMtnPurpose(String mtnPurpose) {
        this.mtnPurpose = mtnPurpose;
    }

    public String getClientRelation() {
        return clientRelation;
    }

    public void setClientRelation(String clientRelation) {
        this.clientRelation = clientRelation;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mtnPurpose);
        dest.writeString(clientRelation);
    }
}
