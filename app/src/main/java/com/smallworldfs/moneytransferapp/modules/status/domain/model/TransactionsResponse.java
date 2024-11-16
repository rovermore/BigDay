package com.smallworldfs.moneytransferapp.modules.status.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction;

import java.util.ArrayList;

/**
 * Created by luis on 29/9/17.
 */

public class TransactionsResponse implements Parcelable {

    private int total;

    private ArrayList<Transaction> transactions;

    private String cancellationMessage;

    public TransactionsResponse(int total, ArrayList<Transaction> transactions, String cancellationMessage) {
        this.total = total;
        this.transactions = transactions;
        this.cancellationMessage = cancellationMessage;
    }

    protected TransactionsResponse(Parcel in) {
        total = in.readInt();
        cancellationMessage = in.readString();
        transactions = in.createTypedArrayList(Transaction.CREATOR);
    }

    public static final Creator<TransactionsResponse> CREATOR = new Creator<TransactionsResponse>() {
        @Override
        public TransactionsResponse createFromParcel(Parcel in) {
            return new TransactionsResponse(in);
        }

        @Override
        public TransactionsResponse[] newArray(int size) {
            return new TransactionsResponse[size];
        }
    };

    public String getCancellationMessage() {
        return cancellationMessage;
    }

    public void setCancellationMessage(String cancellationMessage) {
        this.cancellationMessage = cancellationMessage;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(ArrayList<Transaction> transactions) {
        this.transactions = transactions;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(total);
        parcel.writeString(cancellationMessage);
        parcel.writeTypedList(transactions);
    }
}
