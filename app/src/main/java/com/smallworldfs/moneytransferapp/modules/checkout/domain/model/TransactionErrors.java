package com.smallworldfs.moneytransferapp.modules.checkout.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by luismiguel on 3/10/17.
 */

public class TransactionErrors implements Parcelable {

    private boolean blocking;

    private String title;

    private String description;

    private String type;

    private String subtype;

    protected TransactionErrors(Parcel in) {
        blocking = in.readByte() != 0;
        title = in.readString();
        description = in.readString();
        type = in.readString();
        subtype = in.readString();
    }

    public TransactionErrors(String title, String description) {
        this.title = title;
        this.description = description;
    }

    public static final Creator<TransactionErrors> CREATOR = new Creator<TransactionErrors>() {
        @Override
        public TransactionErrors createFromParcel(Parcel in) {
            return new TransactionErrors(in);
        }

        @Override
        public TransactionErrors[] newArray(int size) {
            return new TransactionErrors[size];
        }
    };

    public boolean isBlocking() {
        return blocking;
    }

    public void setBlocking(boolean blocking) {
        this.blocking = blocking;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte((byte) (blocking ? 1 : 0));
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(type);
        dest.writeString(subtype);
    }
}
