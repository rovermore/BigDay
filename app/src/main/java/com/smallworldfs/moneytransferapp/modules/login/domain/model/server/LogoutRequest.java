package com.smallworldfs.moneytransferapp.modules.login.domain.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

/**
 * Created by luismiguel on 5/9/17
 */
public class LogoutRequest extends ServerQueryMapRequest {

    public LogoutRequest(String userId, String userToken) {
        put("userId", userId);
        put("userToken", userToken);
    }
}
