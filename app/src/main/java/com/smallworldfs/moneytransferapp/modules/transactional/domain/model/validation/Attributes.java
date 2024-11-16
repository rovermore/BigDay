package com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by pedro on 1/8/17.
 */

public class Attributes implements Parcelable {

    private String group;

    private String min;

    private String max;

    private String recommended;

    @SerializedName("alpha_num")
    private boolean alphaNum;

    @SerializedName("special_char")
    private boolean specialChar;

    @SerializedName("text_requirements")
    private String textRequirements;

    @SerializedName("uppercase")
    private boolean upperCase;

    public Attributes() {

    }

    protected Attributes(Parcel in) {
        group = in.readString();
        min = in.readString();
        max = in.readString();
        recommended = in.readString();
        alphaNum = in.readByte() != 0;
        specialChar = in.readByte() != 0;
        textRequirements = in.readString();
        upperCase = in.readByte() != 0;
    }

    public static final Creator<Attributes> CREATOR = new Creator<Attributes>() {
        @Override
        public Attributes createFromParcel(Parcel in) {
            return new Attributes(in);
        }

        @Override
        public Attributes[] newArray(int size) {
            return new Attributes[size];
        }
    };

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getRecommended() {
        return recommended;
    }

    public void setRecommended(String recommended) {
        this.recommended = recommended;
    }

    public boolean isAlphaNum() {
        return alphaNum;
    }

    public void setAlphaNum(boolean alphaNum) {
        this.alphaNum = alphaNum;
    }

    public boolean isSpecialChar() {
        return specialChar;
    }

    public void setSpecialChar(boolean specialChar) {
        this.specialChar = specialChar;
    }

    public String getTextRequirements() {
        return textRequirements;
    }

    public void setTextRequirements(String textRequirements) {
        this.textRequirements = textRequirements;
    }

    public boolean isUpperCase() {
        return upperCase;
    }

    public void setUpperCase(boolean upperCase) {
        this.upperCase = upperCase;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(group);
        dest.writeString(min);
        dest.writeString(max);
        dest.writeString(recommended);
        dest.writeByte((byte) (alphaNum ? 1 : 0));
        dest.writeByte((byte) (specialChar ? 1 : 0));
        dest.writeString(textRequirements);
        dest.writeByte((byte) (upperCase ? 1 : 0));
    }

    public boolean isInitialized() {
        return min != null || recommended != null || max != null;
    }
}
