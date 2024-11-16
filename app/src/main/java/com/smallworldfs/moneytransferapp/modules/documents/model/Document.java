package com.smallworldfs.moneytransferapp.modules.documents.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.TreeMap;

/**
 * Created by pedro on 20/9/17.
 */

public class Document implements Parcelable {

    private String id;

    private TreeMap<String, String> type;

    private String number;

    @SerializedName("issued_by")
    private TreeMap<String, String> issuedBy;

    @SerializedName("expiration_date")
    private String expirationDate;

    @SerializedName("issue_country")
    private TreeMap<String, String> issueCountry;

    private String issueDate;

    private String updatedAt;

    public Document(){

    }

    protected Document(Parcel in) {

        type = new TreeMap<>();
        issuedBy = new TreeMap<>();
        issueCountry = new TreeMap<>();

        this.id = in.readString();

        int count = in.readInt();
        for (int i = 0; i < count; i++) {
            type.put(in.readString(), in.readString());
        }

        this.number = in.readString();

        count = in.readInt();
        for (int i = 0; i < count; i++) {
            issuedBy.put(in.readString(), in.readString());
        }

        this.expirationDate = in.readString();

        count = in.readInt();
        for (int i = 0; i < count; i++) {
            issueCountry.put(in.readString(), in.readString());
        }

        this.issueDate = in.readString();
        this.updatedAt = in.readString();
    }

    public static final Parcelable.Creator<Document> CREATOR = new Parcelable.Creator<Document>() {
        @Override
        public Document createFromParcel(Parcel in) {
            return new Document(in);
        }

        @Override
        public Document[] newArray(int size) {
            return new Document[size];
        }
    };


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);

        if(type != null) {
            dest.writeInt(type.size());
            for (String s : type.keySet()) {
                dest.writeString(s);
                dest.writeString(type.get(s));
            }
        }else{
            dest.writeInt(0);
        }

        dest.writeString(number);

        if(issuedBy != null) {
            dest.writeInt(issuedBy.size());
            for (String s : issuedBy.keySet()) {
                dest.writeString(s);
                dest.writeString(issuedBy.get(s));
            }
        }else{
            dest.writeInt(0);
        }

        dest.writeString(expirationDate);

        if(issueCountry != null) {
            dest.writeInt(issueCountry.size());
            for (String s : issueCountry.keySet()) {
                dest.writeString(s);
                dest.writeString(issueCountry.get(s));
            }
        }else{
            dest.writeInt(0);
        }

        dest.writeString(issueDate);
        dest.writeString(updatedAt);
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public TreeMap<String, String> getType() {
        return type;
    }

    public void setType(TreeMap<String, String> type) {
        this.type = type;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public TreeMap<String, String> getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(TreeMap<String, String> issuedBy) {
        this.issuedBy = issuedBy;
    }

    public String getExpirationDate() {
        return expirationDate;
    }

    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    public TreeMap<String, String> getIssueCountry() {
        return issueCountry;
    }

    public void setIssueCountry(TreeMap<String, String> issueCountry) {
        this.issueCountry = issueCountry;
    }

    public String getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}
