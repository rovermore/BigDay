package com.smallworldfs.moneytransferapp.modules.calculator.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.smallworldfs.moneytransferapp.utils.AmountFormatter;

public class Taxes implements Parcelable {
    @SerializedName("direction")
    @Expose
    private String direction;
    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("percentage")
    @Expose
    private String percentage;
    @SerializedName("sequenceId")
    @Expose
    private Integer sequenceId;
    @SerializedName("taxAmount")
    @Expose
    private String taxAmount;
    @SerializedName("taxCode")
    @Expose
    private String taxCode;

    protected Taxes(Parcel in) {
        direction = in.readString();
        id = in.readInt();
        percentage = in.readString();
        sequenceId = in.readInt();
        taxAmount = in.readString();
        taxCode = in.readString();
    }

    public static final Creator<Taxes> CREATOR = new Creator<Taxes>() {
        @Override
        public Taxes createFromParcel(Parcel in) {
            return new Taxes(in);
        }

        @Override
        public Taxes[] newArray(int size) {
            return new Taxes[size];
        }
    };

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public Integer getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(Integer sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String getTaxAmount() {
        return taxAmount;
    }

    public String getFormatedTaxAmount() {
        return AmountFormatter.formatDoubleAmountNumber(Double.parseDouble(getTaxAmount()));
    }

    public void setTaxAmount(String taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getTaxCode() {
        return taxCode;
    }

    public void setTaxCode(String taxCode) {
        this.taxCode = taxCode;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(direction);
        dest.writeInt(id);
        dest.writeString(percentage);
        dest.writeInt(sequenceId);
        dest.writeString(taxAmount);
        dest.writeString(taxCode);
    }
}
