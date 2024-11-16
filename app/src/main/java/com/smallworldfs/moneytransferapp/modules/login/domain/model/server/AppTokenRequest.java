package com.smallworldfs.moneytransferapp.modules.login.domain.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

/**
 * Created by luismiguel on 5/5/17.
 */

public class AppTokenRequest extends ServerQueryMapRequest {
    public AppTokenRequest(String appToken, String country) {
        put("appToken", appToken);
        put("country", country);
    }
}
