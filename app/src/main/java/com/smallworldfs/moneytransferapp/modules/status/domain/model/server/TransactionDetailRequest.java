package com.smallworldfs.moneytransferapp.modules.status.domain.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

/**
 * Created by luis on 29/9/17.
 */

public class TransactionDetailRequest extends ServerQueryMapRequest {
    public TransactionDetailRequest(String userToken, String userId, String mtn){
        put("userToken", userToken);
        put("userId", userId);
        put("mtn", mtn);
    }
}
