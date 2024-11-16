package com.smallworldfs.moneytransferapp.modules.checkout.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Field;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Transaction;

import java.util.ArrayList;

/**
 * Created by luismigel on 26/9/17.
 */

public class Checkout implements Parcelable{

    @SerializedName("transaction_summary")
    private TransactionSummary transactionSummary;

    @SerializedName("transaction_details")
    private TransactionDetails transactionDetails;

    private ArrayList<Field> fields;

    private Transaction transaction;

    public Checkout(TransactionSummary transactionSummary, TransactionDetails transactionDetails, ArrayList<Field> fields, Transaction transaction) {
        this.transactionSummary = transactionSummary;
        this.transactionDetails = transactionDetails;
        this.fields = fields;
        this.transaction = transaction;
    }

    protected Checkout(Parcel in) {
        transactionSummary = in.readParcelable(TransactionSummary.class.getClassLoader());
        transactionDetails = in.readParcelable(TransactionDetails.class.getClassLoader());
        fields = in.createTypedArrayList(Field.CREATOR);
        transaction = in.readParcelable(Transaction.class.getClassLoader());
    }

    public static final Creator<Checkout> CREATOR = new Creator<Checkout>() {
        @Override
        public Checkout createFromParcel(Parcel in) {
            return new Checkout(in);
        }

        @Override
        public Checkout[] newArray(int size) {
            return new Checkout[size];
        }
    };

    public TransactionSummary getTransactionSummary() {
        return transactionSummary;
    }

    public void setTransactionSummary(TransactionSummary transactionSummary) {
        this.transactionSummary = transactionSummary;
    }

    public TransactionDetails getTransactionDetails() {
        return transactionDetails;
    }

    public void setTransactionDetails(TransactionDetails transactionDetails) {
        this.transactionDetails = transactionDetails;
    }

    public ArrayList<Field> getFields() {
        return fields;
    }

    public void setFields(ArrayList<Field> fields) {
        this.fields = fields;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(transactionSummary, flags);
        dest.writeParcelable(transactionDetails, flags);
        dest.writeTypedList(fields);
        dest.writeParcelable(transaction, flags);
    }
}
