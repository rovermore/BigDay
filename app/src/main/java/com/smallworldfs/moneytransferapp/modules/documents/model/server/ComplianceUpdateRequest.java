package com.smallworldfs.moneytransferapp.modules.documents.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

/**
 * Created by pedro del castillo on 20/9/17.
 */

public class ComplianceUpdateRequest extends ServerQueryMapRequest {

    public ComplianceUpdateRequest(String userToken, String userId, String complianceId) {
        put("userToken", userToken);
        put("userId", userId);
        put("idCompliance", complianceId);
    }
}
