package com.smallworldfs.moneytransferapp.modules.status.domain.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

/**
 * Created by luismiguel on 9/10/17.
 */

public class PayTransactionRequest extends ServerQueryMapRequest {

    public PayTransactionRequest(String userToken, String userId, String mtn) {
        put("userToken", userToken);
        put("userId", userId);
        put("mtn", mtn);
    }
}
