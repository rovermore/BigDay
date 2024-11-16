package com.smallworldfs.moneytransferapp.modules.login.domain.model;

import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;

/**
 * Created by luis on 5/5/17.
 */

public class LoginUserServer {

    private String msg;

    private String text;

    private User user;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
