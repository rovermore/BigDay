package com.smallworldfs.moneytransferapp.modules.status.domain.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

import java.util.Locale;

/**
 * Created by luis on 29/9/17.
 */

public class ContactSupportRequest extends ServerQueryMapRequest {
    public ContactSupportRequest(String country){
        put("country", country);
    }
}
