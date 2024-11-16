package com.smallworldfs.moneytransferapp.modules.register.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by luismiguel on 12/9/17.
 */

public class Group implements Parcelable {

    private String id;

    private String title;

    protected Group(Parcel in) {
        id = in.readString();
        title = in.readString();
    }

    public static final Creator<Group> CREATOR = new Creator<Group>() {
        @Override
        public Group createFromParcel(Parcel in) {
            return new Group(in);
        }

        @Override
        public Group[] newArray(int size) {
            return new Group[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
    }
}
