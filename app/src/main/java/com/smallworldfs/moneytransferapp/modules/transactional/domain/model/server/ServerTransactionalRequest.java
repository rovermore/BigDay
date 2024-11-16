package com.smallworldfs.moneytransferapp.modules.transactional.domain.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

/**
 * Created by luismiguel on 21/7/17.
 */

public class ServerTransactionalRequest extends ServerQueryMapRequest {

    public ServerTransactionalRequest(String deliveryMethod, String operation, String countryDestination, String beneficiaryType) {
        put("deliveryMethod", deliveryMethod);
        put("operation", operation);
        put("countryDestination", countryDestination);
        put("beneficiaryType", beneficiaryType);
    }
}
