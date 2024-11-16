package com.smallworldfs.moneytransferapp.modules.status.domain.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

/**
 * Created by luismiguel on 9/10/17.
 */

public class ReceiptRequest extends ServerQueryMapRequest {
    public ReceiptRequest(String userId, String userToken, String mtn, boolean offline, boolean pre) {
        put("userToken", userToken);
        put("userId", userId);
        put("offline", offline ? "1" : "0");
        put("mtn", mtn);
        put("pre", pre ? "1" : "0");
    }
}
