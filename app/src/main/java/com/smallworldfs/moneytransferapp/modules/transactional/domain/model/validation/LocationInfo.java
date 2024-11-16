package com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by luismiguel on 16/10/17.
 */

public class LocationInfo implements Parcelable{

    private String representativeCode;

    private String locationCode;

    public LocationInfo(String representativeCode, String locationCode) {
        this.representativeCode = representativeCode;
        this.locationCode = locationCode;
    }

    protected LocationInfo(Parcel in) {
        representativeCode = in.readString();
        locationCode = in.readString();
    }

    public static final Creator<LocationInfo> CREATOR = new Creator<LocationInfo>() {
        @Override
        public LocationInfo createFromParcel(Parcel in) {
            return new LocationInfo(in);
        }

        @Override
        public LocationInfo[] newArray(int size) {
            return new LocationInfo[size];
        }
    };

    public String getRepresentativeCode() {
        return representativeCode;
    }

    public void setRepresentativeCode(String representativeCode) {
        this.representativeCode = representativeCode;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(representativeCode);
        dest.writeString(locationCode);
    }
}
