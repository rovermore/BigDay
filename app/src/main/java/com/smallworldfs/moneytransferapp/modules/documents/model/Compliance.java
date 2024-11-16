package com.smallworldfs.moneytransferapp.modules.documents.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by pedro on 20/9/17.
 */

public class Compliance implements Parcelable {

    private String id;

    private String mtn;

    @SerializedName("compliance_type")
    private String complianceType;

    private String title;

    private String description;


    public Compliance(){

    }

    protected Compliance(Parcel in) {

        this.id = in.readString();
        this.mtn = in.readString();
        this.complianceType = in.readString();
        this.title = in.readString();
        this.description = in.readString();
    }

    public static final Creator<Compliance> CREATOR = new Creator<Compliance>() {
        @Override
        public Compliance createFromParcel(Parcel in) {
            return new Compliance(in);
        }

        @Override
        public Compliance[] newArray(int size) {
            return new Compliance[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(mtn);
        dest.writeString(complianceType);
        dest.writeString(title);
        dest.writeString(description);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMtn() {
        return mtn;
    }

    public void setMtn(String mtn) {
        this.mtn = mtn;
    }

    public String getComplianceType() {
        return complianceType;
    }

    public void setComplianceType(String complianceType) {
        this.complianceType = complianceType;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
