package com.smallworldfs.moneytransferapp.modules.home.domain.model;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class ComplianceRuleModel {

    @SerializedName("id")
    private Integer id;

    @SerializedName("mtn")
    private String mtn;

    @SerializedName("compliance_type")
    private String complianceType;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("type")
    private String type;

    @SerializedName("doc")
    private String doc;

    @SerializedName("text")
    private String text;

    @SerializedName("title")
    private String title;

    @SerializedName("status")
    private String status;

    private boolean block;

    @SerializedName("upload")
    private boolean showUploadButton;

    private ArrayList<DocumentButton> buttons;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getMtn() {
        return mtn;
    }

    public void setMtn(String mtn) {
        this.mtn = mtn;
    }

    public String getComplianceType() {
        return complianceType;
    }

    public void setComplianceType(String complianceType) {
        this.complianceType = complianceType;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDoc() {
        return doc;
    }

    public void setDoc(String doc) {
        this.doc = doc;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {return status;}

    public void setStatus(String status) {this.status = status;}

    public boolean isBlock() { return block; }

    public void setBlock(boolean block) { this.block = block; }

    public boolean isShowUploadButton() { return showUploadButton; }

    public void setShowUploadButton(boolean showUploadButton) { this.showUploadButton = showUploadButton; }

    public ArrayList<DocumentButton> getButtons() { return buttons; }

    public void setButtons(ArrayList<DocumentButton> buttons) { this.buttons = buttons; }
}
