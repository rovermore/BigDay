package com.smallworldfs.moneytransferapp.modules.status.domain.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

/**
 * Created by luismiguel on 27/10/17.
 */

public class TipsRequest extends ServerQueryMapRequest {

    public TipsRequest(String userId, String userToken, String paymentMethod, String country) {
        put("userId", userId);
        put("userToken", userToken);
        put("paymentMethod", paymentMethod);
        put("country", country);
    }
}
