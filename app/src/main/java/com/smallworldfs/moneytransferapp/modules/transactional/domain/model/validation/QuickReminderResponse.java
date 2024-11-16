package com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by luismiguel on 17/11/17.
 */

public class QuickReminderResponse implements Parcelable {

    private String msg;

    private String title;

    @SerializedName("content")
    private ArrayList<QuickReminderMessage> messages;

    protected QuickReminderResponse(Parcel in) {
        msg = in.readString();
        title = in.readString();
        messages = in.createTypedArrayList(QuickReminderMessage.CREATOR);
    }

    public static final Creator<QuickReminderResponse> CREATOR = new Creator<QuickReminderResponse>() {
        @Override
        public QuickReminderResponse createFromParcel(Parcel in) {
            return new QuickReminderResponse(in);
        }

        @Override
        public QuickReminderResponse[] newArray(int size) {
            return new QuickReminderResponse[size];
        }
    };

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ArrayList<QuickReminderMessage> getMessages() {
        return messages;
    }

    public void setMessages(ArrayList<QuickReminderMessage> messages) {
        this.messages = messages;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(msg);
        dest.writeString(title);
        dest.writeTypedList(messages);
    }
}
