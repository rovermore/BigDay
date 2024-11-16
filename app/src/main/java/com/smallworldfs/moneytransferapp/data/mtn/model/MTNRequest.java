package com.smallworldfs.moneytransferapp.data.mtn.model;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

/**
 * Created by pedro del castillo on 21/9/17.
 */

public class MTNRequest extends ServerQueryMapRequest {

    public MTNRequest(String country, String mtn) {
        put("country", country);
        put("mtn", mtn);
    }
}
