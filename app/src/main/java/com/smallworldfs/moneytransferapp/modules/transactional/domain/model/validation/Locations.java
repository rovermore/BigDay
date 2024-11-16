package com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by luismiguel on 24/8/17.
 */

public class Locations implements Parcelable{

    @SerializedName("id")
    private String id;
    @SerializedName("location_id")
    private String location_id;
    @SerializedName("locationCode")
    private String locationCode;
    @SerializedName("representative_id")
    private String representative_id;
    @SerializedName("representativeCode")
    private String representativeCode;
    @SerializedName("locationName")
    private String locationName;
    @SerializedName("locationCountry")
    private String locationCountry;
    @SerializedName("locationState")
    private String locationState;
    @SerializedName("postalcode")
    private String postalcode;
    @SerializedName("locationCity")
    private String locationCity;
    @SerializedName("locationAddress")
    private String locationAddress;
    @SerializedName("locationPhone")
    private String locationPhone;
    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;


    protected Locations(Parcel in) {
        id = in.readString();
        location_id = in.readString();
        locationCode = in.readString();
        representative_id = in.readString();
        representativeCode = in.readString();
        locationName = in.readString();
        locationCountry = in.readString();
        locationState = in.readString();
        postalcode = in.readString();
        locationCity = in.readString();
        locationAddress = in.readString();
        locationPhone = in.readString();
        latitude = in.readString();
        longitude = in.readString();
    }

    public static final Creator<Locations> CREATOR = new Creator<Locations>() {
        @Override
        public Locations createFromParcel(Parcel in) {
            return new Locations(in);
        }

        @Override
        public Locations[] newArray(int size) {
            return new Locations[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation_id() {
        return location_id;
    }

    public void setLocation_id(String location_id) {
        this.location_id = location_id;
    }

    public String getLocationCode() {
        return locationCode;
    }

    public void setLocationCode(String locationCode) {
        this.locationCode = locationCode;
    }

    public String getRepresentative_id() {
        return representative_id;
    }

    public void setRepresentative_id(String representative_id) {
        this.representative_id = representative_id;
    }

    public String getRepresentativeCode() {
        return representativeCode;
    }

    public void setRepresentativeCode(String representativeCode) {
        this.representativeCode = representativeCode;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getLocationCountry() {
        return locationCountry;
    }

    public void setLocationCountry(String locationCountry) {
        this.locationCountry = locationCountry;
    }

    public String getLocationState() {
        return locationState;
    }

    public void setLocationState(String locationState) {
        this.locationState = locationState;
    }

    public String getPostalcode() {
        return postalcode;
    }

    public void setPostalcode(String postalcode) {
        this.postalcode = postalcode;
    }

    public String getLocationCity() {
        return locationCity;
    }

    public void setLocationCity(String locationCity) {
        this.locationCity = locationCity;
    }

    public String getLocationAddress() {
        return locationAddress;
    }

    public void setLocationAddress(String locationAddress) {
        this.locationAddress = locationAddress;
    }

    public String getLocationPhone() {
        return locationPhone;
    }

    public void setLocationPhone(String locationPhone) {
        this.locationPhone = locationPhone;
    }

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
        dest.writeString(id);
        dest.writeString(location_id);
        dest.writeString(locationCode);
        dest.writeString(representative_id);
        dest.writeString(representativeCode);
        dest.writeString(locationName);
        dest.writeString(locationCountry);
        dest.writeString(locationState);
        dest.writeString(postalcode);
        dest.writeString(locationCity);
        dest.writeString(locationAddress);
        dest.writeString(locationPhone);
        dest.writeString(latitude);
        dest.writeString(longitude);
    }
}
