package com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.smallworldfs.moneytransferapp.presentation.account.beneficiary.list.model.BeneficiaryUIModel;

import java.io.Serializable;
import java.util.TreeMap;

/**
 * Created by luismiguel on 20/6/17
 */
public class Beneficiary implements Parcelable, Serializable {

    private String id;
    private String clientId;
    private String clientRelationId;
    private String alias;
    private String name;
    private String surname;
    private String email;
    private String mobile;
    private String city;
    private String country;
    private String address;
    private String zip;
    private String state;
    private TreeMap<String, String> deliveryMethod;
    private TreeMap<String, String> payoutCountry;
    private TreeMap<String, String> payoutCurrency;
    private String bankName;
    private TreeMap<String, String> bankAccountType;
    private String bankAccountNumber;
    private String document;
    private String numberDocument;
    private String countryDocument;
    private String currentRepresentativeCode;
    private String currentLocationCode;
    private String beneficiaryType;

    private int isNew = 0;

    public Beneficiary(){
        this.id = "";
        this.clientId = "";
    }

    public Beneficiary(Beneficiary beneficiary){
        this.id = beneficiary.getId();
        this.clientId = beneficiary.getClientId();
        this.clientRelationId = beneficiary.getClientRelationId();
        this.alias = beneficiary.getAlias();
        this.name = beneficiary.getName();
        this.surname = beneficiary.getSurname();
        this.email = beneficiary.getEmail();
        this.mobile = beneficiary.getMobile();
        this.city = beneficiary.getCity();
        this.country = beneficiary.getCountry();
        this.address = beneficiary.getAddress();
        this.zip = beneficiary.getZip();
        this.state = beneficiary.getState();
        this.deliveryMethod = beneficiary.getDeliveryMethod();
        this.payoutCountry = beneficiary.getPayoutCountry();
        this.payoutCurrency = beneficiary.getPayoutCurrency();
        this.bankName = beneficiary.getBankName();
        this.bankAccountNumber = beneficiary.getBankAccountNumber();
        this.document = beneficiary.getDocument();
        this.numberDocument = beneficiary.getNumberDocument();
        this.countryDocument = beneficiary.getCountryDocument();
        this.currentLocationCode = beneficiary.getCurrentLocationCode();
        this.currentRepresentativeCode = beneficiary.getCurrentRepresentativeCode();
        this.beneficiaryType = beneficiary.getBeneficiaryType();

        this.isNew = beneficiary.getIsNew();
    }

    public Beneficiary(BeneficiaryUIModel beneficiary){

        TreeMap<String, String> deliveryMethod = new TreeMap<>();
        deliveryMethod.put(beneficiary.getDeliveryMethod().getType(), beneficiary.getDeliveryMethod().getName());

        TreeMap<String, String> payoutCountry = new TreeMap<>();
        payoutCountry.put(beneficiary.getPayoutCountry().getIso3(), beneficiary.getDeliveryMethod().getName());

        TreeMap<String, String> payoutCurrency = new TreeMap<>();
        payoutCurrency.put(beneficiary.getPayoutCurrency(), beneficiary.getPayoutCurrency());

        this.id = beneficiary.getId();
        this.clientId = beneficiary.getClientId();
        this.clientRelationId = beneficiary.getClientRelationId();
        this.alias = beneficiary.getAlias();
        this.name = beneficiary.getName();
        this.surname = beneficiary.getSurname();
        this.email = beneficiary.getEmail();
        this.mobile = beneficiary.getMobile();
        this.city = beneficiary.getCity();
        this.country = beneficiary.getCountry();
        this.address = beneficiary.getAddress();
        this.zip = beneficiary.getZip();
        this.state = beneficiary.getState();
        this.deliveryMethod = deliveryMethod;
        this.payoutCountry = payoutCountry;
        this.payoutCurrency = payoutCurrency;
        this.bankName = beneficiary.getBankName();
        this.bankAccountNumber = beneficiary.getBankAccountNumber();
        this.document = beneficiary.getDocument();
        this.numberDocument = beneficiary.getNumberDocument();
        this.countryDocument = beneficiary.getCountryDocument();
        this.currentLocationCode = beneficiary.getCurrentLocationCode();
        this.currentRepresentativeCode = beneficiary.getCurrentRepresentativeCode();
        this.beneficiaryType = beneficiary.getBeneficiaryType();
        this.isNew = beneficiary.isNew() ? 1 : 0;
    }

    protected Beneficiary(Parcel in) {
        deliveryMethod = new TreeMap<>();
        payoutCountry = new TreeMap<>();
        payoutCurrency = new TreeMap<>();
        bankAccountType = new TreeMap<>();

        id = in.readString();
        clientId = in.readString();
        clientRelationId = in.readString();
        alias = in.readString();
        name = in.readString();
        surname = in.readString();
        email = in.readString();
        mobile = in.readString();
        city = in.readString();
        country = in.readString();
        address = in.readString();
        zip = in.readString();
        state = in.readString();

        int count = in.readInt();
        for (int i = 0; i < count; i++) {
            deliveryMethod.put(in.readString(), in.readString());
        }

        count = in.readInt();
        for (int i = 0; i < count; i++) {
            payoutCountry.put(in.readString(), in.readString());
        }

        count = in.readInt();
        for (int i = 0; i < count; i++) {
            payoutCurrency.put(in.readString(), in.readString());
        }

        bankName = in.readString();

        count = in.readInt();
        for (int i = 0; i < count; i++) {
            bankAccountType.put(in.readString(), in.readString());
        }

        bankAccountNumber = in.readString();
        document = in.readString();
        numberDocument = in.readString();
        countryDocument = in.readString();
        currentRepresentativeCode = in.readString();
        currentLocationCode = in.readString();

        isNew = in.readInt();
        beneficiaryType = in.readString();
    }

    public static final Creator<Beneficiary> CREATOR = new Creator<Beneficiary>() {
        @Override
        public Beneficiary createFromParcel(Parcel in) {
            return new Beneficiary(in);
        }

        @Override
        public Beneficiary[] newArray(int size) {
            return new Beneficiary[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientRelationId() {
        return clientRelationId;
    }

    public String getAlias() {
        return alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getZip() {
        return zip;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public TreeMap<String, String> getDeliveryMethod() {
        return deliveryMethod;
    }

    public void setDeliveryMethod(TreeMap<String, String> deliveryMethod) {
        this.deliveryMethod = deliveryMethod;
    }

    public TreeMap<String, String> getPayoutCountry() {
        return payoutCountry;
    }

    public TreeMap<String, String> getPayoutCurrency() {
        return payoutCurrency;
    }

    public void setPayoutCurrency(TreeMap<String, String> payoutCurrency) {
        this.payoutCurrency = payoutCurrency;
    }

    public String getBankName() {
        return bankName;
    }

    public TreeMap<String, String> getBankAccountType() {
        return bankAccountType;
    }

    public String getBankAccountNumber() {
        return bankAccountNumber;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getNumberDocument() {
        return numberDocument;
    }

    public String getCountryDocument() {
        return countryDocument;
    }

    public int getIsNew() {
        return isNew;
    }

    public String getBeneficiaryType() {
        return beneficiaryType == null ? "" : beneficiaryType;
    }

    public void setBeneficiaryType(String beneficiaryType) {
        this.beneficiaryType = beneficiaryType;
    }

    public String getNameOrNickName(){
        String nameOrNickname = "";
        if (alias != null && alias.length() > 0){
            nameOrNickname = alias;
        }else{
            nameOrNickname = getFullNameWithSurname();
        }
        return nameOrNickname;
    }

    public String getFullNameWithSurname(){
        String fullname = "";
        if (name != null && name.length() > 0){
            fullname = name;
        }
        if (surname != null && surname.length() > 0) {
            fullname = fullname + " " + surname;
        }
        return fullname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(clientId);
        dest.writeString(clientRelationId);
        dest.writeString(alias);
        dest.writeString(name);
        dest.writeString(surname);
        dest.writeString(email);
        dest.writeString(mobile);
        dest.writeString(city);
        dest.writeString(country);
        dest.writeString(address);
        dest.writeString(zip);
        dest.writeString(state);

        if(deliveryMethod != null) {
            dest.writeInt(deliveryMethod.size());
            for (String s : deliveryMethod.keySet()) {
                dest.writeString(s);
                dest.writeString(deliveryMethod.get(s));
            }
        }else{
            dest.writeInt(0);
        }

        if(payoutCountry != null) {
            dest.writeInt(payoutCountry.size());
            for (String s : payoutCountry.keySet()) {
                dest.writeString(s);
                dest.writeString(payoutCountry.get(s));
            }
        }else{
            dest.writeInt(0);
        }

        if(payoutCurrency != null) {
            dest.writeInt(payoutCurrency.size());
            for (String s : payoutCurrency.keySet()) {
                dest.writeString(s);
                dest.writeString(payoutCurrency.get(s));
            }
        }else{
            dest.writeInt(0);
        }

        dest.writeString(bankName);

        if(bankAccountType != null) {
            dest.writeInt(bankAccountType.size());
            for (String s : bankAccountType.keySet()) {
                dest.writeString(s);
                dest.writeString(bankAccountType.get(s));
            }
        }else{
            dest.writeInt(0);
        }

        dest.writeString(bankAccountNumber);
        dest.writeString(document);
        dest.writeString(numberDocument);
        dest.writeString(countryDocument);
        dest.writeString(currentRepresentativeCode);
        dest.writeString(currentLocationCode);

        dest.writeInt(isNew);
        dest.writeString(beneficiaryType);
    }

    public String getCurrentRepresentativeCode() {
        if (currentRepresentativeCode == null){
            return  "";
        }else{
            return currentRepresentativeCode;
        }
    }

    public void setCurrentRepresentativeCode(String currentRepresentativeCode) {
        this.currentRepresentativeCode = currentRepresentativeCode;
    }

    public String getCurrentLocationCode() {
        if (currentLocationCode != null){
            return "";
        }else{
            return currentLocationCode;
        }
    }

    public void setCurrentLocationCode(String currentLocationCode) {
        this.currentLocationCode = currentLocationCode;
    }
}
