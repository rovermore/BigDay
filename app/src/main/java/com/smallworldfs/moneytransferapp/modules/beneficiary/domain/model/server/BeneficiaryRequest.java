package com.smallworldfs.moneytransferapp.modules.beneficiary.domain.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

/**
 * Created by luismiguel on 20/6/17.
 */

public class BeneficiaryRequest extends ServerQueryMapRequest {

    public BeneficiaryRequest(String userToken, String userId){
        put("userToken", userToken);
        put("userId", userId);
    }

    public BeneficiaryRequest(String userToken, String userId, int limit, int offset, String filter){
        put("userToken", userToken);
        put("userId", userId);
        put("limit", limit);
        put("offset", offset);

        if((filter != null) && (!filter.isEmpty())){
            put("filter", filter);
        }
    }
}
