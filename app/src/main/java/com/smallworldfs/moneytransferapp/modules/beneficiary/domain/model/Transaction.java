package com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model;

import static com.smallworldfs.moneytransferapp.utils.ConstantsKt.STRING_EMPTY;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Pair;

import androidx.core.content.ContextCompat;

import com.google.gson.annotations.SerializedName;
import com.smallworldfs.moneytransferapp.R;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.TransactionItemValue;
import com.smallworldfs.moneytransferapp.utils.AmountFormatter;
import com.smallworldfs.moneytransferapp.utils.Constants;
import com.smallworldfs.moneytransferapp.utils.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Created by pedro del castillo on 12/9/17.
 */

public class Transaction implements Parcelable {
    private static final String TAG = Transaction.class.getSimpleName();

    private String id;

    private String mtn;

    private String status;

    private String paid;

    @SerializedName("client_id")
    private String clientId;

    @SerializedName("sender_country")
    private String senderCountry;

    @SerializedName("original_id")
    private String originalId;

    @SerializedName("client_relation_id")
    private String clientRelationId;

    @SerializedName("original_relation_id")
    private String originalRelationId;

    private String totalSale;

    @SerializedName("promotion_code")
    private String promotionCode;

    @SerializedName("promotion_amount")
    private String promotionAmount;

    @SerializedName("change_date")
    private String changeDate;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("paid_date")
    private String paidDate;

    @SerializedName("updated_at")
    private String updatedAt;

    private String paymentType;

    private String paymentMethod;

    private String pspReference;

    @SerializedName("mtn_program")
    private String mtnProgram;

    @SerializedName("routing_number")
    private String routingNumber;

    @SerializedName("account_number")
    private String accountNumber;

    @SerializedName("account_type")
    private String accountType;

    private String ip;

    private String iban;

    private String bic;

    private String paymentUrl;

    private String confirmOnline;

    private String acknowledge;

    private String ioConfCode;

    private String ioInstName;

    private String ioText;

    @SerializedName("one_sweep_send")
    private String oneSweepSend;

    @SerializedName("client_name")
    private String clientName;

    private String timestamp;

    @SerializedName("extra_cost")
    private String extraCost;

    private String ec;

    private String issuer;

    @SerializedName("payout_principal")
    private String payoutPrincipal;

    @SerializedName("beneficiary_first_name")
    private String beneficiaryFirstName;

    @SerializedName("beneficiary_first_last_name")
    private String beneficiaryFirstLastName;

    @SerializedName("beneficiary_country")
    private String beneficiaryCountry;

    @SerializedName("payout_country")
    private String payoutCountry;

    @SerializedName("payout_currencie")
    private String payoutCurrency;

    @SerializedName("delivery_method")
    private String deliveryMethod;

    @SerializedName("delivery_type")
    private String deliveryType;

    @SerializedName("bank_titular")
    private String bankTitular;

    @SerializedName("bank_name")
    private String bankName;

    @SerializedName("bankName")
    private String bankName2;

    @SerializedName("fixed_fee")
    private String fixedFee;

    @SerializedName("variable_fee")
    private String variableFee;

    @SerializedName("sending_currency")
    private String sendingCurrency;

    @SerializedName("request_date")
    private String requestDate;

    @SerializedName("cancel_time")
    private String cancelTime;

    @SerializedName("beneficiary_city")
    private String beneficiaryCity;

    @SerializedName("beneficiary_bank_name")
    private String beneficiaryBankName;


    @SerializedName("beneficiary_bank_account_number")
    private String beneficiaryAccountNumber;

    @SerializedName("beneficiary_address")
    private String beneficiaryAddress;

    @SerializedName("beneficiary_zip")
    private String beneficiaryZip;

    @SerializedName("beneficiary_mobile_phone_number")
    private String beneficiaryMobilePhoneNumber;

    @SerializedName("beneficiary_bank_account_type")
    private String bankAccountType;

    @SerializedName("payer_name")
    private String payerName;

    @SerializedName("promotion_name")
    private String promotionName;

    @SerializedName("status_msg")
    private String statusMsg;

    @SerializedName("bank_num")
    private String bankNumber;

    @SerializedName("bank_iban")
    private String bankIban;

    private String translatedPaymentMethod;

    private String translatedDeliveryMethod;

    @SerializedName("beneficiary_id")
    private String beneficiaryId;

    private double rate;

    @SerializedName("tax_amount")
    private String taxAmount;

    @SerializedName("tax_code")
    private String taxCode;

    private boolean offline;

    private boolean boleto;

    private boolean canCanceled;

    private boolean isChallenge;

    @SerializedName("delivery_information")
    private ArrayList<TransactionItemValue> deliveryInformation;

    @SerializedName("transaction_information")
    private ArrayList<TransactionItemValue> transactionInformation;

    @SerializedName("transaction_taxes")
    private TransactionItemValue transactionTaxes;

    protected Transaction(Parcel in) {
        id = in.readString();
        mtn = in.readString();
        status = in.readString();
        paid = in.readString();
        clientId = in.readString();
        senderCountry = in.readString();
        originalId = in.readString();
        clientRelationId = in.readString();
        originalRelationId = in.readString();
        totalSale = in.readString();
        promotionCode = in.readString();
        promotionAmount = in.readString();
        changeDate = in.readString();
        createdAt = in.readString();
        paidDate = in.readString();
        updatedAt = in.readString();
        paymentType = in.readString();
        paymentMethod = in.readString();
        pspReference = in.readString();
        mtnProgram = in.readString();
        routingNumber = in.readString();
        accountNumber = in.readString();
        accountType = in.readString();
        ip = in.readString();
        iban = in.readString();
        bic = in.readString();
        paymentUrl = in.readString();
        confirmOnline = in.readString();
        acknowledge = in.readString();
        ioConfCode = in.readString();
        ioInstName = in.readString();
        ioText = in.readString();
        oneSweepSend = in.readString();
        clientName = in.readString();
        timestamp = in.readString();
        extraCost = in.readString();
        ec = in.readString();
        issuer = in.readString();
        payoutPrincipal = in.readString();
        beneficiaryFirstName = in.readString();
        beneficiaryFirstLastName = in.readString();
        beneficiaryCountry = in.readString();
        payoutCountry = in.readString();
        payoutCurrency = in.readString();
        deliveryMethod = in.readString();
        deliveryType = in.readString();
        bankTitular = in.readString();
        bankName = in.readString();
        bankName2 = in.readString();
        fixedFee = in.readString();
        variableFee = in.readString();
        sendingCurrency = in.readString();
        requestDate = in.readString();
        cancelTime = in.readString();
        beneficiaryCity = in.readString();
        beneficiaryBankName = in.readString();
        beneficiaryAccountNumber = in.readString();
        beneficiaryAddress = in.readString();
        beneficiaryZip = in.readString();
        beneficiaryMobilePhoneNumber = in.readString();
        bankAccountType = in.readString();
        payerName = in.readString();
        promotionName = in.readString();
        statusMsg = in.readString();
        bankNumber = in.readString();
        bankIban = in.readString();
        translatedPaymentMethod = in.readString();
        translatedDeliveryMethod = in.readString();
        beneficiaryId = in.readString();
        rate = in.readDouble();
        taxAmount = in.readString();
        taxCode = in.readString();
        offline = in.readByte() != 0x00;
        boleto = in.readByte() != 0x00;
        canCanceled = in.readByte() != 0x00;
        isChallenge = in.readByte() != 0x00;
        if (in.readByte() == 0x01) {
            deliveryInformation = new ArrayList<TransactionItemValue>();
            in.readList(deliveryInformation, TransactionItemValue.class.getClassLoader());
        } else {
            deliveryInformation = null;
        }
        if (in.readByte() == 0x01) {
            transactionInformation = new ArrayList<TransactionItemValue>();
            in.readList(transactionInformation, TransactionItemValue.class.getClassLoader());
        } else {
            transactionInformation = null;
        }
        transactionTaxes = (TransactionItemValue) in.readValue(TransactionItemValue.class.getClassLoader());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(mtn);
        dest.writeString(status);
        dest.writeString(paid);
        dest.writeString(clientId);
        dest.writeString(senderCountry);
        dest.writeString(originalId);
        dest.writeString(clientRelationId);
        dest.writeString(originalRelationId);
        dest.writeString(totalSale);
        dest.writeString(promotionCode);
        dest.writeString(promotionAmount);
        dest.writeString(changeDate);
        dest.writeString(createdAt);
        dest.writeString(paidDate);
        dest.writeString(updatedAt);
        dest.writeString(paymentType);
        dest.writeString(paymentMethod);
        dest.writeString(pspReference);
        dest.writeString(mtnProgram);
        dest.writeString(routingNumber);
        dest.writeString(accountNumber);
        dest.writeString(accountType);
        dest.writeString(ip);
        dest.writeString(iban);
        dest.writeString(bic);
        dest.writeString(paymentUrl);
        dest.writeString(confirmOnline);
        dest.writeString(acknowledge);
        dest.writeString(ioConfCode);
        dest.writeString(ioInstName);
        dest.writeString(ioText);
        dest.writeString(oneSweepSend);
        dest.writeString(clientName);
        dest.writeString(timestamp);
        dest.writeString(extraCost);
        dest.writeString(ec);
        dest.writeString(issuer);
        dest.writeString(payoutPrincipal);
        dest.writeString(beneficiaryFirstName);
        dest.writeString(beneficiaryFirstLastName);
        dest.writeString(beneficiaryCountry);
        dest.writeString(payoutCountry);
        dest.writeString(payoutCurrency);
        dest.writeString(deliveryMethod);
        dest.writeString(deliveryType);
        dest.writeString(bankTitular);
        dest.writeString(bankName);
        dest.writeString(bankName2);
        dest.writeString(fixedFee);
        dest.writeString(variableFee);
        dest.writeString(sendingCurrency);
        dest.writeString(requestDate);
        dest.writeString(cancelTime);
        dest.writeString(beneficiaryCity);
        dest.writeString(beneficiaryBankName);
        dest.writeString(beneficiaryAccountNumber);
        dest.writeString(beneficiaryAddress);
        dest.writeString(beneficiaryZip);
        dest.writeString(beneficiaryMobilePhoneNumber);
        dest.writeString(bankAccountType);
        dest.writeString(payerName);
        dest.writeString(promotionName);
        dest.writeString(statusMsg);
        dest.writeString(bankNumber);
        dest.writeString(bankIban);
        dest.writeString(translatedPaymentMethod);
        dest.writeString(translatedDeliveryMethod);
        dest.writeString(beneficiaryId);
        dest.writeDouble(rate);
        dest.writeString(taxAmount);
        dest.writeString(taxCode);
        dest.writeByte((byte) (offline ? 0x01 : 0x00));
        dest.writeByte((byte) (boleto ? 0x01 : 0x00));
        dest.writeByte((byte) (canCanceled ? 0x01 : 0x00));
        dest.writeByte((byte) (isChallenge ? 0x01 : 0x00));
        if (deliveryInformation == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(deliveryInformation);
        }
        if (transactionInformation == null) {
            dest.writeByte((byte) (0x00));
        } else {
            dest.writeByte((byte) (0x01));
            dest.writeList(transactionInformation);
        }
        dest.writeValue(transactionTaxes);
    }

    @SuppressWarnings("unused")
    public static final Parcelable.Creator<Transaction> CREATOR = new Parcelable.Creator<Transaction>() {
        @Override
        public Transaction createFromParcel(Parcel in) {
            return new Transaction(in);
        }

        @Override
        public Transaction[] newArray(int size) {
            return new Transaction[size];
        }
    };

    public enum SecondaryAction {
        EMPTY, AWAITING_BANK_TRANSFER, AWAITING_PAYMENT
    }

    public Transaction() {

        this.id = "";
        this.mtn = "";
        this.status = "";
        this.paid = "";
        this.clientId = "";
        this.senderCountry = "";
        this.originalId = "";
        this.clientRelationId = "";
        this.originalRelationId = "";
        this.totalSale = "";
        this.promotionCode = "";
        this.changeDate = "";
        this.createdAt = "";
        this.updatedAt = "";
        this.paymentType = "";
        this.paymentMethod = "";
        this.pspReference = "";
        this.mtnProgram = "";
        this.routingNumber = "";
        this.accountNumber = "";
        this.accountType = "";
        this.ip = "";
        this.iban = "";
        this.bic = "";
        this.paymentUrl = "";
        this.confirmOnline = "";
        this.acknowledge = "";
        this.ioConfCode = "";
        this.ioInstName = "";
        this.ioText = "";
        this.oneSweepSend = "";
        this.clientName = "";
        this.timestamp = "";
        this.extraCost = "";
        this.ec = "";
        this.issuer = "";
        this.payoutPrincipal = "";
        this.beneficiaryFirstName = "";
        this.beneficiaryFirstLastName = "";
        this.beneficiaryCountry = "";
        this.payoutCurrency = "";
        this.bankTitular = "";
        this.bankName = "";
        this.bankName2 = "";
        this.bankNumber = "";
        this.fixedFee = "";
        this.variableFee = "";
        this.sendingCurrency = "";
        this.beneficiaryCity = "";
        this.beneficiaryBankName = "";
        this.translatedPaymentMethod = "";
        this.translatedDeliveryMethod = "";
        this.beneficiaryId = "";
        this.taxAmount = "";
        this.taxCode = "";
        this.paidDate = "";
        this.requestDate = "";
        this.cancelTime = "";
        this.promotionAmount = "";
        this.offline = false;
        this.boleto = false;
        this.isChallenge = false;
        this.transactionTaxes = null;
        this.statusMsg = "";
    }

    public Transaction buildInstance (
            String id,
            String mtn,
            String status,
            String paid,
            String clientId,
            String senderCountry,
            String originalId,
            String clientRelationId,
            String originalRelationId,
            String totalSale,
            String promotionCode,
            String changeDate,
            String createdAt,
            String updatedAt,
            String paymentType,
            String paymentMethod,
            String pspReference,
            String mtnProgram,
            String routingNumber,
            String accountNumber,
            String accountType,
            String ip,
            String iban,
            String bic,
            String paymentUrl,
            String confirmOnline,
            String acknowledge,
            String ioConfCode,
            String ioInstName,
            String ioText,
            String oneSweepSend,
            String clientName,
            String timestamp,
            String extraCost,
            String ec,
            String issuer,
            String payoutPrincipal,
            String beneficiaryFirstName,
            String beneficiaryFirstLastName,
            String beneficiaryCountry,
            String payoutCurrency,
            String bankTitular,
            String bankName,
            String bankName2,
            String bankNumber,
            String bankIban,
            String fixedFee,
            String variableFee,
            String sendingCurrency,
            String beneficiaryCity,
            String beneficiaryBankName,
            String translatedPaymentMethod,
            String translatedDeliveryMethod,
            String beneficiaryId,
            String taxAmount,
            String taxCode,
            String paidDate,
            String requestDate,
            String cancelTime,
            String promotionAmount,
            boolean offline,
            boolean boleto,
            boolean isChallenge,
            TransactionItemValue transactionTaxes,
            String statusMsg,
            String bankAccountType
    ) {

        this.id = id;
        this.mtn = mtn;
        this.status = status;
        this.paid = paid;
        this.clientId = clientId;
        this.senderCountry = senderCountry;
        this.originalId = originalId;
        this.clientRelationId = clientRelationId;
        this.originalRelationId = originalRelationId;
        this.totalSale = totalSale;
        this.promotionCode = promotionCode;
        this.changeDate = changeDate;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.paymentType = paymentType;
        this.paymentMethod = paymentMethod;
        this.pspReference = pspReference;
        this.mtnProgram = mtnProgram;
        this.routingNumber = routingNumber;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.ip = ip;
        this.iban = iban;
        this.bic = bic;
        this.paymentUrl = paymentUrl;
        this.confirmOnline = confirmOnline;
        this.acknowledge = acknowledge;
        this.ioConfCode = ioConfCode;
        this.ioInstName = ioInstName;
        this.ioText = ioText;
        this.oneSweepSend = oneSweepSend;
        this.clientName = clientName;
        this.timestamp = timestamp;
        this.extraCost = extraCost;
        this.ec = ec;
        this.issuer = issuer;
        this.payoutPrincipal = payoutPrincipal;
        this.beneficiaryFirstName = beneficiaryFirstName;
        this.beneficiaryFirstLastName = beneficiaryFirstLastName;
        this.beneficiaryCountry = beneficiaryCountry;
        this.payoutCurrency = payoutCurrency;
        this.bankTitular = bankTitular;
        this.bankName = bankName;
        this.bankName2 = bankName2;
        this.bankNumber = bankNumber;
        this.bankIban = bankIban;
        this.fixedFee = fixedFee;
        this.variableFee = variableFee;
        this.sendingCurrency = sendingCurrency;
        this.beneficiaryCity = beneficiaryCity;
        this.beneficiaryBankName = beneficiaryBankName;
        this.translatedPaymentMethod = translatedPaymentMethod;
        this.translatedDeliveryMethod = translatedDeliveryMethod;
        this.beneficiaryId = beneficiaryId;
        this.taxAmount = taxAmount;
        this.taxCode = taxCode;
        this.paidDate = paidDate;
        this.requestDate = requestDate;
        this.cancelTime = cancelTime;
        this.promotionAmount = promotionAmount;
        this.offline = offline;
        this.boleto = boleto;
        this.isChallenge = isChallenge;
        this.transactionTaxes = transactionTaxes;
        this.statusMsg = statusMsg;
        this.bankAccountType = bankAccountType;
        return this;
    }

    public boolean isChallenge() {
        return isChallenge;
    }

    public void setChallenge(boolean challenge) {
        isChallenge = challenge;
    }

    public String getPromotionAmount() {
        return promotionAmount;
    }

    public void setPromotionAmount(String promotionAmount) {
        this.promotionAmount = promotionAmount;
    }

    public TransactionItemValue getTransactionTaxes() {
        return transactionTaxes;
    }

    public void setTransactionTaxes(TransactionItemValue transactionTaxes) {
        this.transactionTaxes = transactionTaxes;
    }

    public String getCancelTime() {
        return cancelTime;
    }

    public void setCancelTime(String cancelTime) {
        this.cancelTime = cancelTime;
    }

    public String getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }

    public String getPaidDate() {
        return paidDate;
    }

    public void setPaidDate(String paidDate) {
        this.paidDate = paidDate;
    }

    public boolean isCanCanceled() {
        return canCanceled;
    }

    public void setCanCanceled(boolean canCanceled) {
        this.canCanceled = canCanceled;
    }

    public boolean isBoleto() {
        return boleto;
    }

    public void setBoleto(boolean boleto) {
        this.boleto = boleto;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMtn() {
        return mtn;
    }

    public void setMtn(String mtn) {
        this.mtn = mtn;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaid() {
        return paid;
    }

    public void setPaid(String paid) {
        this.paid = paid;
    }

    public String getClientId() {
        return clientId;
    }

    public String getSenderCountry() {
        return senderCountry;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setSenderCountry(String senderCountry) {
        this.senderCountry = senderCountry;
    }

    public String getOriginalId() {
        return originalId;
    }

    public void setOriginalId(String originalId) {
        this.originalId = originalId;
    }

    public String getClientRelationId() {
        return clientRelationId;
    }

    public void setClientRelationId(String clientRelationId) {
        this.clientRelationId = clientRelationId;
    }

    public String getOriginalRelationId() {
        return originalRelationId;
    }

    public void setOriginalRelationId(String originalRelationId) {
        this.originalRelationId = originalRelationId;
    }

    public String getTotalSale() {
        return totalSale;
    }

    public void setTotalSale(String totalSale) {
        this.totalSale = totalSale;
    }

    public String getPromotionCode() {
        return promotionCode;
    }

    public String getBeneficiaryBankName() {
        return beneficiaryBankName;
    }

    public void setBeneficiaryBankName(String beneficiaryBankName) {
        this.beneficiaryBankName = beneficiaryBankName;
    }

    public void setPromotionCode(String promotionCode) {
        this.promotionCode = promotionCode;
    }

    public String getChangeDate() {
        return changeDate;
    }

    public void setChangeDate(String changeDate) {
        this.changeDate = changeDate;
    }

    public String getBankIban() {
        return bankIban;
    }

    public void setBankIban(String bankIban) {
        this.bankIban = bankIban;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getTranslatedPaymentMethod() {
        return translatedPaymentMethod;
    }

    public void setTranslatedPaymentMethod(String translatedPaymentMethod) {
        this.translatedPaymentMethod = translatedPaymentMethod;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getBeneficiaryId() {
        return beneficiaryId;
    }

    public void setBeneficiaryId(String beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getPspReference() {
        return pspReference;
    }

    public void setPspReference(String pspReference) {
        this.pspReference = pspReference;
    }

    public String getMtnProgram() {
        return mtnProgram;
    }

    public void setMtnProgram(String mtnProgram) {
        this.mtnProgram = mtnProgram;
    }

    public String getRoutingNumber() {
        return routingNumber;
    }

    public void setRoutingNumber(String routingNumber) {
        this.routingNumber = routingNumber;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getBeneficiaryAccountNumber() {
        return beneficiaryAccountNumber;
    }

    public void setBeneficiaryAccountNumber(String beneficiaryAccountNumber) {
        this.beneficiaryAccountNumber = beneficiaryAccountNumber;
    }


    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getIban() {
        return iban;
    }

    public String getBankTitular() {
        return bankTitular;
    }

    public void setBankTitular(String bankTitular) {
        this.bankTitular = bankTitular;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName2(String bankName) {
        this.bankName2 = bankName;
    }

    public String getBankName2() {
        return bankName2;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setIban(String iban) {
        this.iban = iban;
    }

    public void setBankNumber(String bankNumber) {
        this.bankNumber = bankNumber;
    }

    public String getBankNumber() {
        return bankNumber;
    }

    public String getBic() {
        return bic;
    }

    public void setBic(String bic) {
        this.bic = bic;
    }

    public String getPaymentUrl() {
        return paymentUrl;
    }

    public void setPaymentUrl(String paymentUrl) {
        this.paymentUrl = paymentUrl;
    }

    public String getConfirmOnline() {
        return confirmOnline;
    }

    public void setConfirmOnline(String confirmOnline) {
        this.confirmOnline = confirmOnline;
    }

    public String getAcknowledge() {
        return acknowledge;
    }

    public void setAcknowledge(String acknowledge) {
        this.acknowledge = acknowledge;
    }

    public String getIoConfCode() {
        return ioConfCode;
    }

    public void setIoConfCode(String ioConfCode) {
        this.ioConfCode = ioConfCode;
    }

    public String getIoInstName() {
        return ioInstName;
    }

    public void setIoInstName(String ioInstName) {
        this.ioInstName = ioInstName;
    }

    public String getIoText() {
        return ioText;
    }

    public void setIoText(String ioText) {
        this.ioText = ioText;
    }

    public String getOneSweepSend() {
        return oneSweepSend;
    }

    public void setOneSweepSend(String oneSweepSend) {
        this.oneSweepSend = oneSweepSend;
    }

    public String getClientName() {
        return clientName;
    }

    public String getBeneficiaryCity() {
        return beneficiaryCity;
    }

    public void setBeneficiaryCity(String beneficiaryCity) {
        this.beneficiaryCity = beneficiaryCity;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getExtraCost() {
        return extraCost;
    }

    public void setExtraCost(String extraCost) {
        this.extraCost = extraCost;
    }

    public String getEc() {
        return ec;
    }

    public String getDeliveryMethod() {
        if (deliveryMethod == null) {
            deliveryMethod = deliveryType == null ? "" : deliveryType;
        }
        return deliveryMethod;
    }

    public void setDeliveryMethod(String deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public void setEc(String ec) {
        this.ec = ec;
    }

    public String getIssuer() {
        return issuer;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public String getPayoutPrincipal() {
        return payoutPrincipal;
    }

    public void setPayoutPrincipal(String payoutPrincipal) {
        this.payoutPrincipal = payoutPrincipal;
    }

    public String getSendingCurrency() {
        return sendingCurrency;
    }

    public void setSendingCurrency(String sendingCurrency) {
        this.sendingCurrency = sendingCurrency;
    }

    public String getBeneficiaryFirstName() {
        return beneficiaryFirstName;
    }

    public void setBeneficiaryFirstName(String beneficiaryFirstName) {
        this.beneficiaryFirstName = beneficiaryFirstName;
    }

    public String getBeneficiaryFirstLastName() {
        return beneficiaryFirstLastName;
    }

    public void setBeneficiaryFirstLastName(String beneficiaryFirstLastName) {
        this.beneficiaryFirstLastName = beneficiaryFirstLastName;
    }

    public String getBeneficiaryCountry() {
        if (beneficiaryCountry == null || beneficiaryCountry.isEmpty()) {
            beneficiaryCountry = payoutCountry == null ? "" : payoutCountry;
        }
        return beneficiaryCountry;
    }

    public void setBeneficiaryCountry(String beneficiaryCountry) {
        this.beneficiaryCountry = beneficiaryCountry;
    }

    public String getTranslatedDeliveryMethod() {
        return translatedDeliveryMethod;
    }

    public void setTranslatedDeliveryMethod(String translatedDeliveryMethod) {
        this.translatedDeliveryMethod = translatedDeliveryMethod;
    }

    public String getFixedFee() {
        return fixedFee;
    }

    public void setFixedFee(String fixedFee) {
        this.fixedFee = fixedFee;
    }

    public String getVariableFee() {
        return variableFee;
    }

    public void setVariableFee(String variableFee) {
        this.variableFee = variableFee;
    }

    public String getPayoutCurrency() {
        return payoutCurrency;
    }

    public String getBeneficiaryAddress() {
        return beneficiaryAddress;
    }

    public void setBeneficiaryAddress(String beneficiaryAddress) {
        this.beneficiaryAddress = beneficiaryAddress;
    }

    public String getBeneficiaryZip() {
        return beneficiaryZip;
    }

    public void setBeneficiaryZip(String beneficiaryZip) {
        this.beneficiaryZip = beneficiaryZip;
    }

    public String getBeneficiaryMobilePhoneNumber() {
        return beneficiaryMobilePhoneNumber;
    }

    public void setBeneficiaryMobilePhoneNumber(String beneficiaryMobilePhoneNumber) {
        this.beneficiaryMobilePhoneNumber = beneficiaryMobilePhoneNumber;
    }

    public String getBankAccountType() {
        return bankAccountType;
    }

    public void setBankAccountType(String bankAccountType) {
        this.bankAccountType = bankAccountType;
    }

    public String getPayerName() {
        return payerName;
    }

    public void setPayerName(String payerName) {
        this.payerName = payerName;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getStatusMsg() {
        return statusMsg;
    }

    public void setStatusMsg(String statusMsg) {
        this.statusMsg = statusMsg;
    }

    public void setPayoutCurrency(String payoutCurrency) {
        this.payoutCurrency = payoutCurrency;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getTaxAmount() {
        return taxAmount;
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

    public boolean isOffline() {
        return this.offline;
    }

    public void setOffline(boolean offline) {
        this.offline = offline;
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


    public String getTransactionsStringStatus(Context context) {
        switch (this.status) {
            case Constants.TRANSACTION_STATUS.ACKNOWLEDGE_PENDING:
            case Constants.TRANSACTION_STATUS.NEW:
                if (isTransactionPaid()) {
                    return context.getString(R.string.status_transaction_in_progress);
                } else {
                    if ((this.paymentType != null) && (this.paymentType.equalsIgnoreCase(Constants.PAYNMENT_METHODS.BANKWIRE))) {
                        return context.getString(R.string.status_transaction_awaiting_bank_transfer);
                    } else {
                        return context.getString(R.string.status_transaction_awaiting_payment);
                    }
                }
            case Constants.TRANSACTION_STATUS.UNDER_REVIEW:
                return context.getString(R.string.status_transaction_under_review);
            case Constants.TRANSACTION_STATUS.IN_PROGRESS:
            case Constants.TRANSACTION_STATUS.TO_SUBMIT:
                return context.getString(R.string.status_transaction_in_progress);
            case Constants.TRANSACTION_STATUS.VOID_PENDING:
            case Constants.TRANSACTION_STATUS.USER_CANCEL:
                return context.getString(R.string.status_transaction_cancellation_under_review);
            case Constants.TRANSACTION_STATUS.CLOSED_CANCELLED:
            case Constants.TRANSACTION_STATUS.CLOSED_COMPLIANCE:
            case Constants.TRANSACTION_STATUS.CLOSED_REFUSED:
            case Constants.TRANSACTION_STATUS.CLOSED_DECLINED:
                return context.getString(R.string.status_transaction_transaction_cancelled);
            case Constants.TRANSACTION_STATUS.CLOSED_PAID_OUT:
                if (getDeliveryMethod().equalsIgnoreCase(Constants.DELIVERY_METHODS.BANK_DEPOSIT)) {
                    return context.getString(R.string.status_transaction_completed);
                } else {
                    return context.getString(R.string.status_transaction_money_collected);
                }
                /*
                return context.getString(R.string.status_transaction_money_collected);
                if ((this.deliveryMethod != null) && (this.deliveryMethod.equalsIgnoreCase(Constants.DELIVERY_METHODS.CASH_PICKUP))) {
                    return context.getString(R.string.status_transaction_ready_for_pickup);
                } else {
                    return context.getString(R.string.status_transaction_completed);
                }*/
            default:
                return "";
        }
    }

    public Pair<String, String> formatTransactionDate() {
        if (!TextUtils.isEmpty(this.createdAt)) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat formatterProjection = new SimpleDateFormat("MMMM dd");

            try {
                Date date = formatter.parse(this.createdAt);
                String dateText = formatterProjection.format(date);
                String[] dateSplitted = dateText.split(" ");
                if ((dateSplitted != null) && (dateSplitted.length > 1)) {
                    if ((dateSplitted[0] != null) && (dateSplitted[0].length() > 2)) {
                        String month = dateSplitted[0].substring(0, 3).toUpperCase(Locale.getDefault());
                        String day = dateSplitted[1];
                        return new Pair<>(month, day);
                    }
                }
                return null;
            } catch (ParseException e) {
                Log.e(TAG, "----------------------", e);
            }
        }
        return null;
    }

    public SecondaryAction getSecondaryActionType(Context context) {
        if (TextUtils.isEmpty(this.status)) {
            return SecondaryAction.EMPTY;
        }

        if (this.status.equalsIgnoreCase(Constants.TRANSACTION_STATUS.ACKNOWLEDGE_PENDING) || this.status.equalsIgnoreCase(Constants.TRANSACTION_STATUS.NEW)) {
            if (isTransactionPaid()) {
                return SecondaryAction.EMPTY;
            } else {
                if (this.paymentType.equalsIgnoreCase(Constants.PAYNMENT_METHODS.BANKWIRE)) {
                    return SecondaryAction.AWAITING_BANK_TRANSFER;
                } else {
                    return SecondaryAction.AWAITING_PAYMENT;
                }
            }
        } else {
            return SecondaryAction.EMPTY;
        }
    }

    public boolean isTransactionProcessCancel() {
        return this.status.equalsIgnoreCase(Constants.TRANSACTION_STATUS.CLOSED_CANCELLED) ||
                this.status.equalsIgnoreCase(Constants.TRANSACTION_STATUS.CLOSED_COMPLIANCE) ||
                this.status.equalsIgnoreCase(Constants.TRANSACTION_STATUS.CLOSED_REFUSED) ||
                this.status.equalsIgnoreCase(Constants.TRANSACTION_STATUS.CLOSED_PAID_OUT) ||
                this.status.equalsIgnoreCase(Constants.TRANSACTION_STATUS.CLOSED_DECLINED) ||
                this.status.equalsIgnoreCase(Constants.TRANSACTION_STATUS.USER_CANCEL);
    }

    public Drawable getDrawableSecondaryActionButton(Context context) {
        SecondaryAction type = getSecondaryActionType(context);
        if (type == SecondaryAction.AWAITING_BANK_TRANSFER) {
            return ContextCompat.getDrawable(context, R.drawable.status_icn_details);
        }
        return null;
    }

    public Drawable getDrawablePopupActionButton(Context context) {
        SecondaryAction type = getSecondaryActionType(context);
        switch (type) {
            case AWAITING_BANK_TRANSFER:
                return ContextCompat.getDrawable(context, R.drawable.checkout_icn_transferdetails);
            default:
                return ContextCompat.getDrawable(context, R.drawable.ic_verified_user);

        }
    }


    public String getSecondaryActionButtonString(Context context) {
        if (isChallenge) {
            return STRING_EMPTY;
        } else {
            if (canCanceled) {
                return context.getString(R.string.cancel_transaction_transaction_status_button).toUpperCase(Locale.getDefault());
            } else {
                if (boleto) {
                    return context.getString(R.string.secondary_button_text_action_print_boleto);
                } else {
                    SecondaryAction type = getSecondaryActionType(context);
                    switch (type) {
                        case AWAITING_BANK_TRANSFER:
                            return context.getString(R.string.secondary_button_text_action_transfer_details_dialog);
                        case AWAITING_PAYMENT:
                            if (isTransactionPaid()) {
                                return null;
                            } else {
                                return context.getString(R.string.secondary_button_text_action_pay_now);
                            }
                        default:
                            return null;
                    }
                }
            }
        }
    }


    public Drawable getStatusIcon(Context context) {
        switch (this.status) {
            case Constants.TRANSACTION_STATUS.ACKNOWLEDGE_PENDING:
            case Constants.TRANSACTION_STATUS.NEW:
                if (isTransactionPaid()) {
                    return ContextCompat.getDrawable(context, R.drawable.status_icn_inprogress);
                } else {
                    return ContextCompat.getDrawable(context, R.drawable.status_icn_paymentpending);
                }
            case Constants.TRANSACTION_STATUS.UNDER_REVIEW:
                return ContextCompat.getDrawable(context, R.drawable.status_icn_inprogress);
            case Constants.TRANSACTION_STATUS.VOID_PENDING:
            case Constants.TRANSACTION_STATUS.CLOSED_CANCELLED:
            case Constants.TRANSACTION_STATUS.CLOSED_COMPLIANCE:
            case Constants.TRANSACTION_STATUS.CLOSED_REFUSED:
            case Constants.TRANSACTION_STATUS.CLOSED_DECLINED:
            case Constants.TRANSACTION_STATUS.USER_CANCEL:
                return ContextCompat.getDrawable(context, R.drawable.status_icn_cancelled);
            case Constants.TRANSACTION_STATUS.CLOSED_PAID_OUT:
                return ContextCompat.getDrawable(context, R.drawable.status_icn_complete);
            default:
                return null;
        }
    }

    public ArrayList<TransactionItemValue> getDeliveryInformation(Context context) {
        ArrayList<TransactionItemValue> list = new ArrayList<>();
        switch (getDeliveryMethod()) {
            case Constants.DELIVERY_METHODS.CASH_PICKUP:
                list.add(new TransactionItemValue(context.getString(R.string.status_transfer_show_more_city), TextUtils.isEmpty(this.beneficiaryCity) ? "-" : this.beneficiaryCity));
                list.add(new TransactionItemValue(context.getString(R.string.status_transfer_show_more_country), TextUtils.isEmpty(getBeneficiaryCountry()) ? "-" : getBeneficiaryCountry()));
                break;
            case Constants.DELIVERY_METHODS.BANK_DEPOSIT:
                list.add(new TransactionItemValue(context.getString(R.string.status_transfer_show_more_country), TextUtils.isEmpty(getBeneficiaryCountry()) ? "-" : getBeneficiaryCountry()));
                list.add(new TransactionItemValue(context.getString(R.string.status_transfer_show_more_bank), TextUtils.isEmpty(this.beneficiaryBankName) ? "-" : this.beneficiaryBankName));
                list.add(new TransactionItemValue(context.getString(R.string.status_transfer_show_more_account_number), TextUtils.isEmpty(this.beneficiaryAccountNumber) ? "-" : this.beneficiaryAccountNumber));
                break;
            case Constants.DELIVERY_METHODS.PHYSICAL_DELIVERY:
                list.add(new TransactionItemValue(context.getString(R.string.status_transfer_show_more_address), TextUtils.isEmpty(this.beneficiaryAddress) ? "-" : this.beneficiaryAddress));
                list.add(new TransactionItemValue(context.getString(R.string.status_transfer_show_more_postal_code), TextUtils.isEmpty(this.beneficiaryZip) ? "-" : this.beneficiaryZip));
                list.add(new TransactionItemValue(context.getString(R.string.status_transfer_show_more_city), TextUtils.isEmpty(this.beneficiaryCity) ? "-" : this.beneficiaryCity));
                list.add(new TransactionItemValue(context.getString(R.string.status_transfer_show_more_country), TextUtils.isEmpty(getBeneficiaryCountry()) ? "-" : getBeneficiaryCountry()));
                break;
            case Constants.DELIVERY_METHODS.TOP_UP:
            case Constants.DELIVERY_METHODS.MOBILE_WALLET:
                list.add(new TransactionItemValue(context.getString(R.string.status_transfer_show_more_country), TextUtils.isEmpty(getBeneficiaryCountry()) ? "-" : getBeneficiaryCountry()));
                list.add(new TransactionItemValue(context.getString(R.string.status_transfer_show_more_mobile), TextUtils.isEmpty(this.beneficiaryMobilePhoneNumber) ? "-" : this.beneficiaryMobilePhoneNumber));
                break;
            case Constants.DELIVERY_METHODS.CASH_CARD_RELOAD:
                list.add(new TransactionItemValue(context.getString(R.string.status_transfer_show_more_country), TextUtils.isEmpty(getBeneficiaryCountry()) ? "-" : getBeneficiaryCountry()));
                list.add(new TransactionItemValue(context.getString(R.string.status_transfer_show_more_card_type), TextUtils.isEmpty(this.bankAccountType) ? "-" : this.bankAccountType));
                list.add(new TransactionItemValue(context.getString(R.string.status_transfer_show_more_account_number), TextUtils.isEmpty(this.beneficiaryAccountNumber) ? "-" : this.beneficiaryAccountNumber));
                break;
            default:
                break;
        }
        return list;
    }

    public ArrayList<TransactionItemValue> getTransactionInformation(Context context) {
        ArrayList<TransactionItemValue> list = new ArrayList<>();
        list.add(new TransactionItemValue(context.getString(R.string.status_transfer_show_more_mtn_code), TextUtils.isEmpty(this.mtn) ? "-" : this.mtn));
        list.add(new TransactionItemValue(context.getString(R.string.status_transfer_show_more_payer), TextUtils.isEmpty(this.payerName) ? "-" : this.payerName));
        list.add(new TransactionItemValue(context.getString(R.string.status_transfer_show_more_rate), AmountFormatter.normalizeDoubleString(String.valueOf(rate))));
        list.add(new TransactionItemValue(context.getString(R.string.status_transfer_show_more_payment_method), TextUtils.isEmpty(this.translatedPaymentMethod) ? "-" : this.translatedPaymentMethod));
        list.add(new TransactionItemValue(context.getString(R.string.status_transfer_show_more_promotion_applied), TextUtils.isEmpty(this.promotionName) ? "-" : this.promotionName));


        return list;
    }

    public boolean isPossibleToProceedWithPamentMethodChange(Context context) {
        if (getSecondaryActionType(context) == SecondaryAction.AWAITING_PAYMENT) {
            if (this.getPaymentType().equalsIgnoreCase(Constants.PAYNMENT_METHODS.ONLINEPAYMENT) ||
                    this.getPaymentType().equalsIgnoreCase(Constants.PAYNMENT_METHODS.SOFORT) ||
                    this.getPaymentType().equalsIgnoreCase(Constants.PAYNMENT_METHODS.WORLDPAY)) {
                return true;
            }
        }
        return false;
    }

    public boolean isNewTransaction() {
        return this.status.equalsIgnoreCase(Constants.TRANSACTION_STATUS.NEW) || this.status.equalsIgnoreCase(Constants.TRANSACTION_STATUS.ACKNOWLEDGE_PENDING);
    }

    public boolean isTransactionPaid() {
        return this.paid.equalsIgnoreCase("1");
    }
}
