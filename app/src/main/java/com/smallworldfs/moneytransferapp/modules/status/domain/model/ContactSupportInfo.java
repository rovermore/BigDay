package com.smallworldfs.moneytransferapp.modules.status.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by luismiguel on 4/10/17
 */
public class ContactSupportInfo implements Parcelable {

    private String livezilla;
    private String phone;
    private String address;
    private Coordenates coordenates;
    private String email;
    private ArrayList<String> mailing;
    private String emailPrivate;
    private String emailAgent;
    private String emailCareer;
    private String emailBusiness;
    private String emailCountryError;

    public ContactSupportInfo() {
    }

    private ContactSupportInfo(Parcel in) {
        livezilla = in.readString();
        phone = in.readString();
        address = in.readString();
        coordenates = in.readParcelable(Coordenates.class.getClassLoader());
        email = in.readString();
        mailing = in.createStringArrayList();
        emailPrivate = in.readString();
        emailAgent = in.readString();
        emailCareer = in.readString();
        emailBusiness = in.readString();
        emailCountryError = in.readString();
    }

    public static final Creator<ContactSupportInfo> CREATOR = new Creator<ContactSupportInfo>() {
        @Override
        public ContactSupportInfo createFromParcel(Parcel in) {
            return new ContactSupportInfo(in);
        }

        @Override
        public ContactSupportInfo[] newArray(int size) {
            return new ContactSupportInfo[size];
        }
    };

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(livezilla);
        dest.writeString(phone);
        dest.writeString(address);
        dest.writeParcelable(coordenates, flags);
        dest.writeString(email);
        dest.writeStringList(mailing);
        dest.writeString(emailPrivate);
        dest.writeString(emailAgent);
        dest.writeString(emailCareer);
        dest.writeString(emailBusiness);
        dest.writeString(emailCountryError);
    }

    public String getLivezilla() {
        return livezilla;
    }

    public void setLivezilla(String livezilla) {
        this.livezilla = livezilla;
    }

    public ArrayList<String> getMailing() {
        return this.mailing;
    }

    public void setMailing(ArrayList<String> mailing) {
        this.mailing = mailing;
    }

    public String getEmailPrivate() {
        return emailPrivate;
    }

    public void setEmailPrivate(String emailPrivate) {
        this.emailPrivate = emailPrivate;
    }

    public String getEmailAgent() {
        return emailAgent;
    }

    public void setEmailAgent(String emailAgent) {
        this.emailAgent = emailAgent;
    }

    public String getEmailCareer() {
        return emailCareer;
    }

    public void setEmailCareer(String emailCareer) {
        this.emailCareer = emailCareer;
    }

    public String getEmailBusiness() {
        return emailBusiness;
    }

    public void setEmailBusiness(String emailBusiness) {
        this.emailBusiness = emailBusiness;
    }

    public String getEmailCountryError() {
        return emailCountryError;
    }

    public void setEmailCountryError(String emailCountryError) {
        this.emailCountryError = emailCountryError;
    }

    public Coordenates getCoordenates() {
        return coordenates;
    }

}

