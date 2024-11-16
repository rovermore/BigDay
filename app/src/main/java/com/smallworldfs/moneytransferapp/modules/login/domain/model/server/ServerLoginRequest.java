package com.smallworldfs.moneytransferapp.modules.login.domain.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

/**
 * Created by luismiguel on 5/5/17.
 */

public class ServerLoginRequest extends ServerQueryMapRequest {
    public ServerLoginRequest(String username, String password, String country) {
        put("email", username);
        put("password", password);
        put("country", country);
    }

    public ServerLoginRequest(String appToken) {
        put("limited", "1");
        put("appToken", appToken);
    }
}
