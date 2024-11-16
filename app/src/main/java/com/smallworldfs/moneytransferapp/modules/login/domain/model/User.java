package com.smallworldfs.moneytransferapp.modules.login.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import org.jetbrains.annotations.TestOnly;

import java.util.TreeMap;

/**
 * Created by luismiguel on 5/5/17
 */
public class User implements Parcelable {

    private String id;
    @SerializedName("client_id")
    private String clientId;
    private String name;
    @SerializedName("second_name")
    private String secondName;
    private String surname;
    @SerializedName("second_surname")
    private String secondSurname;
    private String email;
    private TreeMap<String, String> country;
    private TreeMap<String, String> originCountry;
    @SerializedName("birth_date")
    private String birthDate;
    private String phone;
    private String mobile;
    private String address;
    private String cp;
    private String city;
    private String status;
    private String ocupation;
    private String streetNumber;
    private String buildingName;
    @SerializedName("app_token")
    private String appToken;
    private TreeMap<String, String> mobilePhoneCountry;
    @SerializedName("user_token")
    private String userToken;
    private String kountsessid;
    private String receiveNewsletters;
    private String receiveStatusTrans;
    @SerializedName("finished_transactions")
    private String finishedTransactions;
    private String flinksState;
    private Gdpr gdpr;
    @SerializedName("freshchat_id")
    private String freshchatId;
    private boolean showEmailValidated;

    private boolean authenticated;


    public User() {
    }

    protected User(Parcel in) {
        country = new TreeMap<>();
        mobilePhoneCountry = new TreeMap<>();

        id = in.readString();
        clientId = in.readString();
        name = in.readString();
        secondName = in.readString();
        surname = in.readString();
        secondSurname = in.readString();
        email = in.readString();

        int count = in.readInt();
        for (int i = 0; i < count; i++) {
            country.put(in.readString(), in.readString());
        }

        count = in.readInt();
        for (int i = 0; i < count; i++) {
            originCountry.put(in.readString(), in.readString());
        }

        birthDate = in.readString();
        phone = in.readString();
        mobile = in.readString();
        address = in.readString();
        cp = in.readString();
        city = in.readString();
        status = in.readString();
        streetNumber = in.readString();
        buildingName = in.readString();
        appToken = in.readString();

        count = in.readInt();
        for (int i = 0; i < count; i++) {
            mobilePhoneCountry.put(in.readString(), in.readString());
        }

        userToken = in.readString();
        kountsessid = in.readString();
        flinksState = in.readString();

        freshchatId = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
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


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSecondName() {
        return secondName;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUserToken() {
        return userToken;
    }


    public void setUserToken(String userToken) {
        this.userToken = userToken;
    }

    public String getSurname() {
        return surname;
    }


    public String getSecondSurname() {
        return secondSurname;
    }


    public TreeMap<String, String> getCountry() {
        return country;
    }

    public void setCountry(TreeMap<String, String> country) {
        this.country = country;
    }

    public TreeMap<String, String> getDestinationCountry() {
        return originCountry;
    }

    public void setDestinationCountry(TreeMap<String, String> country) {
        this.originCountry = country;
    }

    public String getBirthDate() {
        return birthDate;
    }


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCp() {
        return cp;
    }


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public TreeMap<String, String> getMobilePhoneCountry() {
        return mobilePhoneCountry;
    }

    public void setMobilePhoneCountry(TreeMap<String, String> mobilePhoneCountry) {
        this.mobilePhoneCountry = mobilePhoneCountry;
    }

    public Gdpr getGdpr() {
        return gdpr;
    }

    public void setGdpr(Gdpr gdpr) {
        this.gdpr = gdpr;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStreetNumber() {
        return streetNumber;
    }


    public String getBuildingName() {
        return buildingName;
    }


    public String getAppToken() {
        return appToken;
    }

    @TestOnly
    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    public String getKountsessid() {
        return kountsessid;
    }

    public String getFinishedTransactions() {
        return finishedTransactions;
    }

    public String getFlinksState() {
        return flinksState;
    }

    public void setFlinksState(String flinksState) {
        this.flinksState = flinksState;
    }

    public boolean wantToReceiveNewsletters() {
        return false;
    }

    public boolean wantToReceiveStatusTrans() {
        return false;
    }

    public String getFreshchatId() {
        return freshchatId;
    }

    public void setFreshchatId(String freshchatId) {
        this.freshchatId = freshchatId;
    }


    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public void setSecondSurname(String secondSurname) {
        this.secondSurname = secondSurname;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getOcupation() {
        return ocupation;
    }

    public void setOcupation(String ocupation) {
        this.ocupation = ocupation;
    }

    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    public void setBuildingName(String buildingName) {
        this.buildingName = buildingName;
    }

    public void setKountsessid(String kountsessid) {
        this.kountsessid = kountsessid;
    }

    public String getReceiveNewsletters() {
        return receiveNewsletters;
    }

    public void setReceiveNewsletters(String receiveNewsletters) {
        this.receiveNewsletters = receiveNewsletters;
    }

    public String getReceiveStatusTrans() {
        return receiveStatusTrans;
    }

    public void setReceiveStatusTrans(String receiveStatusTrans) {
        this.receiveStatusTrans = receiveStatusTrans;
    }

    public void setFinishedTransactions(String finishedTransactions) {
        this.finishedTransactions = finishedTransactions;
    }

    public void setShowEmailValidated(boolean showEmailValidated) {
        this.showEmailValidated = showEmailValidated;
    }

    public boolean getShowEmailValidated() {
        return showEmailValidated;
    }

    public void setAuthenticated(boolean authenticated) {
        this.authenticated = authenticated;
    }

    public boolean getAuthenticated() { return authenticated; }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(clientId);
        dest.writeString(name);
        dest.writeString(secondName);
        dest.writeString(surname);
        dest.writeString(secondSurname);
        dest.writeString(email);

        if (country != null) {
            dest.writeInt(country.size());
            for (String s : country.keySet()) {
                dest.writeString(s);
                dest.writeString(country.get(s));
            }
        } else {
            dest.writeInt(0);
        }

        if (originCountry != null) {
            dest.writeInt(originCountry.size());
            for (String s : originCountry.keySet()) {
                dest.writeString(s);
                dest.writeString(originCountry.get(s));
            }
        } else {
            dest.writeInt(0);
        }

        dest.writeString(birthDate);
        dest.writeString(phone);
        dest.writeString(mobile);
        dest.writeString(address);
        dest.writeString(cp);
        dest.writeString(city);
        dest.writeString(status);
        dest.writeString(streetNumber);
        dest.writeString(buildingName);
        dest.writeString(appToken);

        if (mobilePhoneCountry != null) {
            dest.writeInt(mobilePhoneCountry.size());
            for (String s : mobilePhoneCountry.keySet()) {
                dest.writeString(s);
                dest.writeString(mobilePhoneCountry.get(s));
            }
        } else {
            dest.writeInt(0);
        }

        dest.writeString(flinksState);
        dest.writeString(userToken);
        dest.writeString(kountsessid);

        dest.writeString(freshchatId);
    }
}
