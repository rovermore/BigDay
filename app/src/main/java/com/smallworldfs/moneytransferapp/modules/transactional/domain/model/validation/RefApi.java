package com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by luismiguel on 22/8/17.
 */

public class RefApi implements Parcelable{

    private ArrayList<String> params;

    private String url;

    public RefApi(){

    }

    protected RefApi(Parcel in) {
        params = in.createStringArrayList();
        url = in.readString();
    }

    public RefApi(ArrayList<String> params, String url){
        this.params = params;
        this.url = url;
    }

    public static final Creator<RefApi> CREATOR = new Creator<RefApi>() {
        @Override
        public RefApi createFromParcel(Parcel in) {
            return new RefApi(in);
        }

        @Override
        public RefApi[] newArray(int size) {
            return new RefApi[size];
        }
    };

    public ArrayList<String> getParams() {
        return params;
    }

    public void setParams(ArrayList<String> params) {
        this.params = params;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(params);
        dest.writeString(url);
    }
}
