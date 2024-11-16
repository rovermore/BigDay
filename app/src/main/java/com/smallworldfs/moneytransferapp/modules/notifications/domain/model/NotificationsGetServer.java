package com.smallworldfs.moneytransferapp.modules.notifications.domain.model;

/**
 * Created by pedro del castillo on 7/9/17.
 */

public class NotificationsGetServer {

    private String msg;

    private NotificationGroup notifications;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public NotificationGroup getNotifications() {
        return notifications;
    }

    public void setNotifications(NotificationGroup notifications) {
        this.notifications = notifications;
    }
}
