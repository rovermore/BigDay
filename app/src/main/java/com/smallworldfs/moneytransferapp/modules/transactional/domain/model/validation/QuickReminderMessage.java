package com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by luismiguel on 17/11/17.
 */

public class QuickReminderMessage implements Parcelable {

    private String title;

    private String description;

    public QuickReminderMessage(String title, String description) {
        this.title = title;
        this.description = description;
    }

    protected QuickReminderMessage(Parcel in) {
        description = in.readString();
    }

    public static final Creator<QuickReminderMessage> CREATOR = new Creator<QuickReminderMessage>() {
        @Override
        public QuickReminderMessage createFromParcel(Parcel in) {
            return new QuickReminderMessage(in);
        }

        @Override
        public QuickReminderMessage[] newArray(int size) {
            return new QuickReminderMessage[size];
        }
    };

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(description);
    }
}
