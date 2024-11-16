package com.smallworldfs.moneytransferapp.modules.calculator.domain.model.server;

import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;
import com.smallworldfs.moneytransferapp.data.common.locale.LocaleRepository;

/**
 * Created by luismiguel on 27/6/17.
 */

public class ServerCurrencieRequest extends ServerQueryMapRequest {


    public ServerCurrencieRequest(String countryOrigin, String countryPayout, String beneficiaryType) {
        put("countryOrigin", countryOrigin);
        put("country", countryPayout);
        put("typeBeneficiary", beneficiaryType);
    }

    public ServerCurrencieRequest(String countryOrigin, String countryPayout, String currency, boolean benefType) {
        put("countryOrigin", countryOrigin);
        put("country", countryPayout);
        if (benefType) {
            put("typeBeneficiary", currency);
        } else {
            put("currency", currency);
        }
    }
}
