package com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

/**
 * Created by pedro del castillo on 25/9/17.
 */

public class NewBeneficiaryFormRequest extends ServerQueryMapRequest {

    public NewBeneficiaryFormRequest(String deliveryMethod, String userToken, String userId, String country, String beneficiaryType) {
        put("deliveryMethod", deliveryMethod);
        put("country", country);
        put("userId", userId);
        put("userToken", userToken);
        put("beneficiaryType", beneficiaryType);
    }
}
