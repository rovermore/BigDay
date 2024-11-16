package com.smallworldfs.moneytransferapp.modules.notifications.domain.model;

/**
 * Created by pedro del castillo on 11/10/17
 */

public class NotificationGroup {

    private NotificationBlock account;

    private NotificationBlock status;

    private NotificationBlock sendto;

    public NotificationGroup(){

    }

    public NotificationBlock getAccount() {
        return account;
    }

    public void setAccount(NotificationBlock account) {
        this.account = account;
    }

    public NotificationBlock getStatus() {
        return status;
    }

    public void setStatus(NotificationBlock status) {
        this.status = status;
    }

    public NotificationBlock getSendto() {
        return sendto;
    }

    public void setSendto(NotificationBlock sendto) {
        this.sendto = sendto;
    }
}
