package com.smallworldfs.moneytransferapp.modules.transactional.domain.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;
import com.smallworldfs.moneytransferapp.modules.login.domain.model.User;
import com.smallworldfs.moneytransferapp.modules.transactional.domain.model.validation.Payout;

public class LocationMapRequest extends ServerQueryMapRequest {

    public LocationMapRequest(Payout payout, User user) {
        put("representativeCode", payout.getRepresentativeCode());
        put("locationCode", payout.getLocationCode());
        put("userId", user.getId());
        put("userToken", user.getUserToken());
    }
}
