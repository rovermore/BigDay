package com.smallworldfs.moneytransferapp.modules.transactional.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by luismiguel on 7/8/17.
 */

public class KeyValueData implements Parcelable {

    private String key;

    private String value;

    public KeyValueData(String key, String value) {
        this.key = key;
        this.value = value;
    }

    protected KeyValueData(Parcel in) {
        key = in.readString();
        value = in.readString();
    }

    public static final Creator<KeyValueData> CREATOR = new Creator<KeyValueData>() {
        @Override
        public KeyValueData createFromParcel(Parcel in) {
            return new KeyValueData(in);
        }

        @Override
        public KeyValueData[] newArray(int size) {
            return new KeyValueData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(key);
        dest.writeString(value);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof KeyValueData)) return false;

        KeyValueData that = (KeyValueData) o;

        if (getKey() != null ? !getKey().equals(that.getKey()) : that.getKey() != null)
            return false;
        return getValue() != null ? getValue().equals(that.getValue()) : that.getValue() == null;

    }

    @Override
    public int hashCode() {
        int result = getKey() != null ? getKey().hashCode() : 0;
        result = 31 * result + (getValue() != null ? getValue().hashCode() : 0);
        return result;
    }
}
