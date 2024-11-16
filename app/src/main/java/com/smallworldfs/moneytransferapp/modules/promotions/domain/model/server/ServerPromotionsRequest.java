package com.smallworldfs.moneytransferapp.modules.promotions.domain.model.server;

import android.text.TextUtils;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

/**
 * Created by luis on 29/6/17.
 */

public class ServerPromotionsRequest extends ServerQueryMapRequest {

    public ServerPromotionsRequest(String originCountry, String payoutCountry, String clientId){
        if(!TextUtils.isEmpty(originCountry)){
            put("countryOrigin", originCountry);
        }
        if(!TextUtils.isEmpty(payoutCountry)){
            put("countryDestination", payoutCountry);
        }
        if(!TextUtils.isEmpty(clientId)){
            put("clientId", clientId);
        }
    }

}
