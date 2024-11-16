package com.smallworldfs.moneytransferapp.modules.status.domain.model;

import com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.Transaction;

public class TransactionDetailResponse {

    private String msg;

    private Transaction transaction;

    private String cancellationMessage;

    public String getCancellationMessage() {
        return cancellationMessage;
    }

    public void setCancellationMessage(String cancellationMessage) {
        this.cancellationMessage = cancellationMessage;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public void setTransaction(Transaction transaction) {
        this.transaction = transaction;
    }
}
