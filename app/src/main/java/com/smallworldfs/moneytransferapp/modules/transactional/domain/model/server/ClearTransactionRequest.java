package com.smallworldfs.moneytransferapp.modules.transactional.domain.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

/**
 * Created by luismiguel on 24/10/17.
 */

public class ClearTransactionRequest extends ServerQueryMapRequest {

    public ClearTransactionRequest(String userId, String userToken) {
        put("userId", userId);
        put("userToken", userToken);

    }
}
