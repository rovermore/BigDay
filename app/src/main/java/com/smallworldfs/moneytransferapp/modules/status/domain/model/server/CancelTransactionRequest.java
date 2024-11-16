package com.smallworldfs.moneytransferapp.modules.status.domain.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

/**
 * Created by luismiguel on 10/10/17.
 */

public class CancelTransactionRequest  extends ServerQueryMapRequest {
    public CancelTransactionRequest(String userId, String userToken, String mtn) {
        put("userToken", userToken);
        put("userId", userId);
        put("mtn", mtn);
    }
}
