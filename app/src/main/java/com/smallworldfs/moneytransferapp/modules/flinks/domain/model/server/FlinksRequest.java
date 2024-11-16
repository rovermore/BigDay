package com.smallworldfs.moneytransferapp.modules.flinks.domain.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

public class FlinksRequest extends ServerQueryMapRequest {

    public FlinksRequest (String userId, String userToken){
        put("userToken", userToken);
        put("userId", userId);
    }

}
