package com.smallworldfs.moneytransferapp.modules.status.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by luismiguel on 4/10/17.
 */

public class Coordenates implements Parcelable{

    private String latitude;

    private String longitude;


    protected Coordenates(Parcel in) {
        latitude = in.readString();
        longitude = in.readString();
    }

    public static final Creator<Coordenates> CREATOR = new Creator<Coordenates>() {
        @Override
        public Coordenates createFromParcel(Parcel in) {
            return new Coordenates(in);
        }

        @Override
        public Coordenates[] newArray(int size) {
            return new Coordenates[size];
        }
    };

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(latitude);
        dest.writeString(longitude);
    }
}
