package com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

/**
 * Created by pedro del castillo on 27/9/17.
 */

public class UpdateBeneficiaryRequest extends ServerQueryMapRequest {

    public UpdateBeneficiaryRequest(String deliveryMethod, String userToken, String country,
                                    String userId, String beneficiaryId, String beneficiaryType) {
        put("deliveryMethod", deliveryMethod);
        put("userToken", userToken);
        put("country", country);
        put("userId", userId);
        put("beneficiaryId", beneficiaryId);
        put("beneficiaryType", beneficiaryType);
    }
}
