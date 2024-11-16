package com.smallworldfs.moneytransferapp.modules.c2b.domain.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

public class C2BRequest extends ServerQueryMapRequest {

    public C2BRequest(String countryDestination) {
        put("countryDestination", countryDestination);
    }
}
