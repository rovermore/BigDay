package com.smallworldfs.moneytransferapp.modules.login.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.QuickReminderMessage;

import java.util.ArrayList;

/**
 * Created by luis on 21/5/18.
 */

public class Gdpr implements Parcelable {

    @SerializedName("content")
    private ArrayList<QuickReminderMessage> listGdprMessages;

    private String msg;

    private String title;

    private String type;

    @SerializedName("button_ok_title")
    private String buttonOkTitle;

    @SerializedName("button_cancel_title")
    private String buttonCancelTitle;

    public Gdpr() {}

    protected Gdpr(Parcel in) {
        listGdprMessages = in.createTypedArrayList(QuickReminderMessage.CREATOR);
        msg = in.readString();
        title = in.readString();
        type = in.readString();
        buttonOkTitle = in.readString();
        buttonCancelTitle = in.readString();
    }

    public static final Creator<Gdpr> CREATOR = new Creator<Gdpr>() {
        @Override
        public Gdpr createFromParcel(Parcel in) {
            return new Gdpr(in);
        }

        @Override
        public Gdpr[] newArray(int size) {
            return new Gdpr[size];
        }
    };

    public ArrayList<QuickReminderMessage> getListGdprMessages() {
        return listGdprMessages;
    }

    public void setListGdprMessages(ArrayList<QuickReminderMessage> listGdprMessages) {
        this.listGdprMessages = listGdprMessages;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getButtonOkTitle() {
        return buttonOkTitle;
    }

    public void setButtonOkTitle(String buttonOkTitle) {
        this.buttonOkTitle = buttonOkTitle;
    }

    public String getButtonCancelTitle() {
        return buttonCancelTitle;
    }

    public void setButtonCancelTitle(String buttonCancelTitle) {
        this.buttonCancelTitle = buttonCancelTitle;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeTypedList(listGdprMessages);
        parcel.writeString(msg);
        parcel.writeString(title);
        parcel.writeString(type);
        parcel.writeString(buttonOkTitle);
        parcel.writeString(buttonCancelTitle);
    }
}
