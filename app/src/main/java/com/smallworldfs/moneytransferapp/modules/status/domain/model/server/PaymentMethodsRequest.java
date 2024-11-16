package com.smallworldfs.moneytransferapp.modules.status.domain.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

/**
 * Created by luis on 29/9/17.
 */

public class PaymentMethodsRequest extends ServerQueryMapRequest {

    public PaymentMethodsRequest(String country, String paymentMethod, String userId, String userToken){
        put("country", country);
        put("paymentMethod", paymentMethod);
        put("userId", userId);
        put("userToken", userToken);
    }

}
