package com.smallworldfs.moneytransferapp.modules.transactional.domain.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

/**
 * Created by luismiguel on 24/10/17.
 */

public class QuickReminderRequest extends ServerQueryMapRequest {

    public QuickReminderRequest(String userId, String userToken, String country) {

        put("userToken", userToken);
        put("userId", userId);
        put("country", country);

    }
}
