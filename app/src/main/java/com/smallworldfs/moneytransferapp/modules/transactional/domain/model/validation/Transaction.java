package com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

/**
 * Created by luismiguel on 27/9/17.
 */

public class Transaction implements Parcelable{

    private String userId;

    private String userToken;

    private String beneficiaryId;

    private String mtn;

    @SerializedName("steps")
    private TransactionInfo transactionInfo;

    public Transaction(String userId, String userToken, String beneficiaryId, String mtn, TransactionInfo transactionInfo) {
        this.userId = userId;
        this.userToken = userToken;
        this.beneficiaryId = beneficiaryId;
        this.mtn = mtn;
        this.transactionInfo = transactionInfo;
    }

    protected Transaction(Parcel in) {
        userId = in.readString();
        userToken = in.readString();
        beneficiaryId = in.readString();
        mtn = in.readString();
        transactionInfo = in.readParcelable(TransactionInfo.class.getClassLoader());
    }

    public Transaction() {}

    public static final Creator<Transaction> CREATOR = new Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserToken() {
        return userToken;
    }

    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public String getMtn() {
        return mtn;
    }

    public void setMtn(String mtn) {
        this.mtn = mtn;
    }

    public TransactionInfo getTransactionInfo() {
        return transactionInfo;
    }

    public void setTransactionInfo(TransactionInfo transactionInfo) {
        this.transactionInfo = transactionInfo;
    }

    @Override
    public int describeContents() {
        return 0;
    }
   
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(userId);
        dest.writeString(userToken);
        dest.writeString(beneficiaryId);
        dest.writeString(mtn);
        dest.writeParcelable(transactionInfo, flags);
    }
}
