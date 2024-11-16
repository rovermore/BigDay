package com.smallworldfs.moneytransferapp.modules.status.domain.model;

import static com.smallworldfs.moneytransferapp.utils.ConstantsKt.STRING_EMPTY;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.TreeMap;

/**
 * Created by luismiguel on 5/10/17.
 */

public class Bank implements Parcelable{

    private String bank;

    private String name;

    private String ccc;

    private String iban;

    private String bic;

    private String depositBankId;

    private String depositBankBranchId;

    public Bank() {
        bank = STRING_EMPTY;
        name = STRING_EMPTY;
        ccc = STRING_EMPTY;
        iban = STRING_EMPTY;
        bic = STRING_EMPTY;
        depositBankId = STRING_EMPTY;
        depositBankBranchId = STRING_EMPTY;
    }


    public Bank(TreeMap<String, String> map) {
        this.bank = map.get("bank");
        this.name = map.get("name");
        this.ccc = map.get("ccc");
        this.iban = map.get("iban");
        this.bic = map.get("bic");
        this.depositBankId = map.get("depositBankId");
        this.depositBankBranchId = map.get("depositBankBranchId");
    }

    protected Bank(Parcel in) {
        bank = in.readString();
        name = in.readString();
        ccc = in.readString();
        iban = in.readString();
        bic = in.readString();
        depositBankId = in.readString();
        depositBankBranchId = in.readString();
    }

    public static final Creator<Bank> CREATOR = new Creator<Bank>() {
        @Override
        public Bank createFromParcel(Parcel in) {
            return new Bank(in);
        }

        @Override
        public Bank[] newArray(int size) {
            return new Bank[size];
        }
    };

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

    public String getIban() {
        return iban;
    }

    public void setIban(String iban) {
        this.iban = iban;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(bank);
        dest.writeString(name);
        dest.writeString(ccc);
        dest.writeString(iban);
        dest.writeString(bic);
        dest.writeString(depositBankId);
        dest.writeString(depositBankBranchId);
    }
}
