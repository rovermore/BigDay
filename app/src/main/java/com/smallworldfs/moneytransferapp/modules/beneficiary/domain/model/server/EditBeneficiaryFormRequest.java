package com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

/**
 * Created by pedro del castillo on 25/9/17.
 */

public class EditBeneficiaryFormRequest extends ServerQueryMapRequest {

    public EditBeneficiaryFormRequest(String beneficiaryId, String userToken, String userId, String beneficiaryType) {
        put("beneficiaryId", beneficiaryId);
        put("userId", userId);
        put("userToken", userToken);
        put("beneficiaryType", beneficiaryType);
    }
}
