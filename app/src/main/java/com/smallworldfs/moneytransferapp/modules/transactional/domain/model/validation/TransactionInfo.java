package com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by luismiguel on 16/10/17.
 */

public class TransactionInfo implements Parcelable{

    @SerializedName("step2")
    private PurposeInfo purposeInfo;

    @SerializedName("step3")
    private LocationInfo locationInfo;

    public TransactionInfo(PurposeInfo purposeInfo, LocationInfo locationInfo) {
        this.purposeInfo = purposeInfo;
        this.locationInfo = locationInfo;
    }

    protected TransactionInfo(Parcel in) {
        purposeInfo = in.readParcelable(PurposeInfo.class.getClassLoader());
        locationInfo = in.readParcelable(LocationInfo.class.getClassLoader());
    }

    public static final Creator<TransactionInfo> CREATOR = new Creator<TransactionInfo>() {
        @Override
        public TransactionInfo createFromParcel(Parcel in) {
            return new TransactionInfo(in);
        }

        @Override
        public TransactionInfo[] newArray(int size) {
            return new TransactionInfo[size];
        }
    };

    public LocationInfo getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(LocationInfo locationInfo) {
        this.locationInfo = locationInfo;
    }

    public PurposeInfo getPurposeInfo() {
        return purposeInfo;
    }

    public void setPurposeInfo(PurposeInfo purposeInfo) {
        this.purposeInfo = purposeInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(purposeInfo, flags);
        dest.writeParcelable(locationInfo, flags);
    }
}
