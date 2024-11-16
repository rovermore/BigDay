package com.smallworldfs.moneytransferapp.modules.country.domain.model;

/**
 * Created by luismiguel on 26/6/17.
 */

public class Bank {

    private String bank;

    private String name;

    private String ccc;

    private String bic;

    private String depositBankId;

    private String depositBankBranchId;

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCcc() {
        return ccc;
    }

    public void setCcc(String ccc) {
        this.ccc = ccc;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getDepositBankId() {
        return depositBankId;
    }

    public void setDepositBankId(String depositBankId) {
        this.depositBankId = depositBankId;
    }

    public String getDepositBankBranchId() {
        return depositBankBranchId;
    }

    public void setDepositBankBranchId(String depositBankBranchId) {
        this.depositBankBranchId = depositBankBranchId;
    }
}
