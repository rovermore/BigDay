package com.smallworldfs.moneytransferapp.modules.country.domain.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

/**
 * Created by luismiguel on 5/5/17.
 */

public class ServerCountryRequest extends ServerQueryMapRequest {
    public ServerCountryRequest(String type) {
        put("type", type);
    }

    public ServerCountryRequest(String type, String countryOrigin) {
        put("type", type);
        put("countryOrigin", countryOrigin);
    }

    public ServerCountryRequest(String type, String countryOrigin, boolean getObjects) {
        put("type", type);
        put("countryOrigin", countryOrigin);
        if (getObjects) {
            put("order", "object");
        }
    }

}
