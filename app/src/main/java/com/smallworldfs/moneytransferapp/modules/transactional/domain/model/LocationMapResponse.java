package com.smallworldfs.moneytransferapp.modules.transactional.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Locations;

import java.util.ArrayList;

public class LocationMapResponse implements Parcelable {

    String msg;

    private ArrayList<Locations> locations;

    protected LocationMapResponse(Parcel in) {
        msg = in.readString();
        locations = in.createTypedArrayList(Locations.CREATOR);
    }

    public static final Creator<Locations> CREATOR = new Creator<Locations>() {

        @Override
        public Locations createFromParcel(Parcel source) {
            return null;
        }

        @Override
        public Locations[] newArray(int size) {
            return new Locations[0];
        }
    };


    public ArrayList<Locations> getLocations() {
        return locations;
    }

    public void setLocations(ArrayList<Locations> locations) {
        this.locations = locations;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
