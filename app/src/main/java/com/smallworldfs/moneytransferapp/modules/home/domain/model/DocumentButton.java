package com.smallworldfs.moneytransferapp.modules.home.domain.model;

public class DocumentButton {

    private String text;

    private String url;

    public DocumentButton(String text, String url) {
        this.text = text;
        this.url = url;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
