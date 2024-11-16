package com.smallworldfs.moneytransferapp.modules.checkout.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by luismiguel on 26/9/17.
 */

public class TransactionSummary implements Parcelable{

    private String name;

    private String firstLastName;

    private String country;

    private String currencyType;

    private String deliveryMethod;

    private String transactionDate;

    private double totalpayout;

    private String currency;

    private Double promotionAmount;

    private String promotionName;

    public TransactionSummary(String name, String firstLastName, String country, String currencyType, String deliveryMethod, String transactionDate, double totalpayout, String currency, Double promotionAmount, String promotionName) {
        this.name = name;
        this.firstLastName = firstLastName;
        this.country = country;
        this.currencyType = currencyType;
        this.deliveryMethod = deliveryMethod;
        this.transactionDate = transactionDate;
        this.totalpayout = totalpayout;
        this.currency = currency;
        this.promotionAmount = promotionAmount;
        this.promotionName = promotionName;
    }

    protected TransactionSummary(Parcel in) {
        name = in.readString();
        firstLastName = in.readString();
        country = in.readString();
        currencyType = in.readString();
        deliveryMethod = in.readString();
        transactionDate = in.readString();
        totalpayout = in.readDouble();
        currency = in.readString();
        promotionName = in.readString();
    }

    public TransactionSummary() {}

    public static final Creator<TransactionSummary> CREATOR = new Creator<TransactionSummary>() {
        @Override
        public TransactionSummary createFromParcel(Parcel in) {
            return new TransactionSummary(in);
        }

        @Override
        public TransactionSummary[] newArray(int size) {
            return new TransactionSummary[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getFirstLastName() {
        return firstLastName;
    }

    public void setFirstLastName(String firstLastName) {
        this.firstLastName = firstLastName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCurrencyType() {
        return currencyType;
    }

    public void setCurrencyType(String currencyType) {
        this.currencyType = currencyType;
    }

    public String getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public String getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(String transactionDate) {
        this.transactionDate = transactionDate;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public double getTotalPayout() {
        return totalpayout;
    }

    public void setTotalPayout(double amount) {
        this.totalpayout = amount;
    }

    public Double getPromotionAmount() {
        return promotionAmount;
    }

    public void setPromotionAmount(Double promotionAmount) {
        this.promotionAmount = promotionAmount;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(firstLastName);
        dest.writeString(country);
        dest.writeString(currencyType);
        dest.writeString(deliveryMethod);
        dest.writeString(transactionDate);
        dest.writeDouble(totalpayout);
        dest.writeString(currency);
        dest.writeString(promotionName);
    }
}
