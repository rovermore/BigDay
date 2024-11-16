package com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

/**
 * Created by pedro del castillo on 27/9/17.
 */

public class CreateBeneficiaryRequest extends ServerQueryMapRequest {

    public CreateBeneficiaryRequest(String deliveryMethod, String userToken, String country,
                                    String userId, String beneficiaryType) {
        put("deliveryMethod", deliveryMethod);
        put("userToken", userToken);
        put("country", country);
        put("userId", userId);
        put("beneficiaryType", beneficiaryType);
    }
}
