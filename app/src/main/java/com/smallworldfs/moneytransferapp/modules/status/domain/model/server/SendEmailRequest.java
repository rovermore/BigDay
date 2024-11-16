package com.smallworldfs.moneytransferapp.modules.status.domain.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;
import com.smallworldfs.moneytransferapp.utils.Constants;

/**
 * Created by luis on 29/9/17.
 */

public class SendEmailRequest extends ServerQueryMapRequest {
    public SendEmailRequest(String subject, String body, String to, String userToken, String userId) {
        put("subject", subject);
        put("body", body);
        put("to", to);
        put("userToken", userToken);
        put("type", Constants.EMAIL.TYPE);
        put("userId", userId);
        put("key", Constants.EMAIL.EMAIL_KEY);
    }

    public SendEmailRequest(String userId, String country) {
        put("type", Constants.EMAIL.TYPE_VINCULATION);
        put("key", Constants.EMAIL.EMAIL_KEY);
        put("subtype", Constants.EMAIL.SUBTYPE);
        put("userId", userId);
        put("country", country);
    }
}
