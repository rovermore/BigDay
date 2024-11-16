package com.smallworldfs.moneytransferapp.modules.status.domain.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

/**
 * Created by luis on 29/9/17.
 */

public class TransactionsRequest extends ServerQueryMapRequest {
    public TransactionsRequest(String userToken, String userId){
        put("userToken", userToken);
        put("userId", userId);
    }
}
