package com.smallworldfs.moneytransferapp.modules.calculator.domain.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;

/**
 * Created by luismiguel on 27/6/17.
 */

public class ServerCalculateRequest extends ServerQueryMapRequest {


    public ServerCalculateRequest(String sendingCountry, String sendingCurrency, String payoutCountry,
                                  String payoutCurrency, String beneficiaryId, String operationType, String currencyType, String amount,
                                  String deliveryMethod, String promotionCode, String representativeCode) {

        put("sendingCountry", sendingCountry);
        put("sendingCurrency", sendingCurrency);
        put("payoutCountry", payoutCountry);
        put("payoutCurrency", payoutCurrency);
        put("beneficiaryId", beneficiaryId);
        put("operation", operationType);
        put("currencyType", currencyType);
        put("amount", amount);
        put("deliveryMethod", deliveryMethod);
        put("promotionCode", promotionCode);
        put("representativeCode", representativeCode);
    }
}
