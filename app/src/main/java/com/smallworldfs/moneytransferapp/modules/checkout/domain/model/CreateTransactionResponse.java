package com.smallworldfs.moneytransferapp.modules.checkout.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction;

import java.util.ArrayList;

/**
 * Created by luismiguel on 3/10/17.
 */

public class CreateTransactionResponse implements Parcelable {

    private String msg;

    private Transaction transaction;

    private ArrayList<TransactionErrors> errors;

    @SerializedName("riskified_response")
    private RiskifiedResponse riskifiedResponse;

    private TransactionCheckOutDialogSummary summary;

    public CreateTransactionResponse() {
        this.msg = null;
        this.transaction = null;
        this.errors =  null;
    }

    protected CreateTransactionResponse(Parcel in) {
        msg = in.readString();
        transaction = in.readParcelable(Transaction.class.getClassLoader());
        errors = in.createTypedArrayList(TransactionErrors.CREATOR);
        riskifiedResponse = in.readParcelable(RiskifiedResponse.class.getClassLoader());
        summary = in.readParcelable(TransactionCheckOutDialogSummary.class.getClassLoader());
    }

    public static final Creator<CreateTransactionResponse> CREATOR = new Creator<CreateTransactionResponse>() {
        @Override
        public CreateTransactionResponse createFromParcel(Parcel in) {
            return new CreateTransactionResponse(in);
        }

        @Override
        public CreateTransactionResponse[] newArray(int size) {
            return new CreateTransactionResponse[size];
        }
    };

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public ArrayList<TransactionErrors> getErrors() {
        return errors;
    }

    public void setErrors(ArrayList<TransactionErrors> errors) {
        this.errors = errors;
    }

    public RiskifiedResponse getRiskifiedResponse() {
        return riskifiedResponse;
    }

    public void setRiskifiedResponse(RiskifiedResponse riskifiedResponse) {
        this.riskifiedResponse = riskifiedResponse;
    }

    public TransactionCheckOutDialogSummary getSummary() {
        return summary;
    }

    public void setSummary(TransactionCheckOutDialogSummary summary) {
        this.summary = summary;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(msg);
        dest.writeParcelable(transaction, flags);
        dest.writeTypedList(errors);
        dest.writeParcelable(riskifiedResponse, flags);
        dest.writeParcelable(summary, flags);
    }
}
