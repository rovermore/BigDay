package com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by luismiguel on 28/7/17
 */
public class FormData implements Parcelable{

    @SerializedName("fields")
    private ArrayList<Field> fields;

    public FormData() { }

    protected FormData(Parcel in) {
        fields = in.createTypedArrayList(Field.CREATOR);
    }

    public static final Creator<FormData> CREATOR = new Creator<FormData>() {
        @Override
        public FormData createFromParcel(Parcel in) {
            return new FormData(in);
        }

        @Override
        public FormData[] newArray(int size) {
            return new FormData[size];
        }
    };

    public ArrayList<Field> getFields() {
        return fields;
    }

    public void setFields(ArrayList<Field> fields) {
        this.fields = fields;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(fields);
    }
}
