package com.smallworldfs.moneytransferapp.modules.status.domain.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

/**
 * Created by luis on 29/9/17.
 */

public class ChangePaymentRequest extends ServerQueryMapRequest {

    public ChangePaymentRequest(String paymentMethod, String userId, String userToken, String mtn, String depositBankBranchId, String depositBankId) {
        put("paymentMethod", paymentMethod);
        put("userId", userId);
        put("userToken", userToken);
        put("mtn", mtn);
        put("depositBankBranchId", depositBankBranchId);
        put("depositBankId", depositBankId);
    }

}
