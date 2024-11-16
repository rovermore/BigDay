package com.smallworldfs.moneytransferapp.modules.transactional.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field;

/**
 * Created by luismiguel on 20/7/17.
 */

public class GenericFormField implements Parcelable{
    protected GenericFormField(Parcel in) {
    }

    public static final Creator<GenericFormField> CREATOR = new Creator<GenericFormField>() {
        @Override
        public GenericFormField createFromParcel(Parcel in) {
            return new GenericFormField(in);
        }

        @Override
        public GenericFormField[] newArray(int size) {
            return new GenericFormField[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    protected GenericFormField(){

    }
}
