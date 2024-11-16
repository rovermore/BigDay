package com.smallworldfs.moneytransferapp.modules.checkout.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by luismiguel on 26/9/17.
 */

public class TransactionDetails implements Parcelable {

    private double subtotal;

    private String fee;

    private String currencyOrigin;

    private String footer;

    private double totalToPay;

    @SerializedName("delivery_information")
    private ArrayList<TransactionItemValue> deliveryInformation;

    @SerializedName("transaction_information")
    private ArrayList<TransactionItemValue> transactionInformation;

    @SerializedName("transaction_taxes")
    private ArrayList<TransactionItemValue> transactionTaxes;

    public TransactionDetails(double subtotal, String fee, String currencyOrigin, String footer, double totalToPay, ArrayList<TransactionItemValue> deliveryInformation, ArrayList<TransactionItemValue> transactionInformation, ArrayList<TransactionItemValue> transactionTaxes) {
        this.subtotal = subtotal;
        this.fee = fee;
        this.currencyOrigin = currencyOrigin;
        this.footer = footer;
        this.totalToPay = totalToPay;
        this.deliveryInformation = deliveryInformation;
        this.transactionInformation = transactionInformation;
        this.transactionTaxes = transactionTaxes;
    }

    protected TransactionDetails(Parcel in) {
        subtotal = in.readDouble();
        fee = in.readString();
        currencyOrigin = in.readString();
        totalToPay = in.readDouble();
        footer = in.readString();
    }

    public TransactionDetails() {}

    public static final Creator<TransactionDetails> CREATOR = new Creator<TransactionDetails>() {
        @Override
        public TransactionDetails createFromParcel(Parcel in) {
            return new TransactionDetails(in);
        }

        @Override
        public TransactionDetails[] newArray(int size) {
            return new TransactionDetails[size];
        }
    };

    public double getSubtotal() {
        return subtotal;
    }

    public void setSubtotal(double subtotal) {
        this.subtotal = subtotal;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getCurrencyOrigin() {
        return currencyOrigin;
    }

    public void setCurrencyOrigin(String currencyOrigin) {
        this.currencyOrigin = currencyOrigin;
    }

    public double getTotalToPay() {
        return totalToPay;
    }

    public void setTotalToPay(double totalToPay) {
        this.totalToPay = totalToPay;
    }

    public ArrayList<TransactionItemValue> getDeliveryInformation() {
        return deliveryInformation;
    }

    public void setDeliveryInformation(ArrayList<TransactionItemValue> deliveryInformation) {
        this.deliveryInformation = deliveryInformation;
    }

    public ArrayList<TransactionItemValue> getTransactionInformation() {
        return transactionInformation;
    }

    public void setTransactionInformation(ArrayList<TransactionItemValue> transactionInformation) {
        this.transactionInformation = transactionInformation;
    }

    public ArrayList<TransactionItemValue> getTransactionTaxes() {
        return transactionTaxes;
    }

    public void setTransactionTaxes(ArrayList<TransactionItemValue> transactionTaxes) {
        this.transactionTaxes = transactionTaxes;
    }

    public String getFooter() {
        return footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(subtotal);
        dest.writeString(fee);
        dest.writeString(currencyOrigin);
        dest.writeDouble(totalToPay);
        dest.writeString(footer);
    }
}
