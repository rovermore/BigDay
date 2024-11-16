package com.smallworldfs.moneytransferapp.modules.notifications.domain.model;

/**
 * Created by pedro del castillo on 11/10/17
 */

public class Topic {

    private String mTitle;

    private String mTopic;

    private String mDescription;

    public Topic(String title, String topic, String description){
        this.mTitle = title;
        this.mTopic = topic;
        this.mDescription = description;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public String getTopic() {
        return mTopic;
    }

    public void setTopic(String topic) {
        this.mTopic = topic;
    }

    public String getDescription() {
        return mDescription;
    }

    public void setDescription(String description) {
        this.mDescription = description;
    }

}
