package com.smallworldfs.moneytransferapp.modules.transactional.domain.model.server;

import android.text.TextUtils;

import com.smallworldfs.moneytransferapp.BuildConfig;
import com.smallworldfs.moneytransferapp.modules.common.domain.model.ServerQueryMapRequest;
import com.smallworldfs.moneytransferapp.data.common.locale.LocaleRepository;

/**
 * Created by luismiguel on 28/7/17.
 */

public class ValidateTransactionRequest extends ServerQueryMapRequest {

    public ValidateTransactionRequest(String userToken, String currency,
                                      String operation, String country,
                                      String nextStep, String userId,
                                      String currentStepId, String amount,
                                      String currencyType, String currencyOrigin,
                                      String beneficiaryId, String beneficiaryType) {
        put("operation", operation);
        put("userToken", userToken);
        put("country", country);
        put("currency", currency);
        put("nextstep", nextStep);
        put("userId", userId);
        put("currentstep", currentStepId);
        put("amount", TextUtils.isEmpty(amount) && BuildConfig.TEST_MODE ? "100" : amount);
        put("currencyType", currencyType);
        put("currencyOrigin", currencyOrigin);
        put("beneficiaryId", beneficiaryId);
        put("beneficiaryType", beneficiaryType);
    }

    public ValidateTransactionRequest() {

    }
}
