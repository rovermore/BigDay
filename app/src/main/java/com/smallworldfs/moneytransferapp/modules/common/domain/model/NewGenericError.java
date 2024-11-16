package com.smallworldfs.moneytransferapp.modules.common.domain.model;

import android.content.Context;
import android.net.ConnectivityManager;

import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.CreateTransactionResponse;
import com.smallworldfs.moneytransferapp.modules.checkout.domain.model.TransactionErrors;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by luismiguel on 1/6/17
 */
public class NewGenericError {

    public ErrorType errorType;

    public String msg = "";

    public String title = "";

    public String text = "";

    public enum ErrorType {
        CONNECTION_ERROR, SERVER_ERROR, AUTH_TOKEN_ERROR, FRAUD_EMAIL, LOGIN_INCORRECT, VALIDATION_ERROR, SESSION_EXPIRED, TAX_CODE_ERROR
    }

    public interface SmallWorldErrorTypes {
        String FRAUD_EMAIL = "login_fraud_email";

        // Login
        String LOGIN_INCORRECT = "LOGIN_INCORRECT";
        String LOGIN_COUNTRY_BLOCK = "LOGIN_COUNTRY_BLOCK";
        String LOGIN_USER_BLOCK = "LOGIN_USER_BLOCK";
        String ERROR_NO_TRANSACTION = "error_no_transaction";
        String INVALID_STATE_ERROR = "login_register_state_inactive";
    }

    public NewGenericError(String msg) {
        this.msg = msg;
    }

    /**
     * Generic SERVER_ERROR
     */
    public NewGenericError() {
        errorType = ErrorType.SERVER_ERROR;
    }

    /**
     * Generic AUTH_TOKEN_ERROR
     *
     * @param msg
     */
    public NewGenericError(String msg, ErrorType type) {
        this.msg = msg;
        this.errorType = type;
    }

    public NewGenericError(String msg, String text, ErrorType type) {
        this.msg = msg;
        this.text = text;
        this.errorType = type;
    }

    public static NewGenericError processError(retrofit2.Response response) {
        if (!isConnectionAvailable()) {
            return new NewGenericError("", ErrorType.CONNECTION_ERROR);
        } else {
            if (response == null) {
                return new NewGenericError();
            } else if (response.errorBody() != null) {
                if (response.code() == 401 || response.code() == 422 || response.code() == 403) {
                    ErrorType errorType;
                    switch (response.code()) {
                        case 401:
                            errorType = ErrorType.AUTH_TOKEN_ERROR;
                            break;
                        case 422:
                            errorType = ErrorType.VALIDATION_ERROR;
                            break;
                        case 403:
                            default:
                                errorType = ErrorType.SESSION_EXPIRED;
                                break;
                    }

                    String msg = "";
                    String text = "";
                    JSONObject jsonObj;

                    try {
                        ResponseBody responseBody = response.errorBody();
                        BufferedSource source = responseBody.source();
                        source.request(Long.MAX_VALUE);
                        Buffer buffer = source.getBuffer();
                        String responseBodyString = buffer.clone().readString(StandardCharsets.UTF_8);

                        jsonObj = new JSONObject(responseBodyString);
                        msg = jsonObj.getString("msg");
                        text = jsonObj.optString("text");
                    } catch (JSONException | IOException e) {
                        e.printStackTrace();
                    }

                    if (msg.equalsIgnoreCase(SmallWorldErrorTypes.LOGIN_INCORRECT) && text != null && !text.isEmpty()) {
                        return new NewGenericError(msg, text, errorType);
                    } else if (msg.contains("mobilePhoneNumber") && errorType.equals(ErrorType.VALIDATION_ERROR)) {
                        JSONObject jsonMsgObject;
                        JSONArray jsonArray;
                        try {
                            jsonMsgObject = new JSONObject(msg);
                            jsonArray = jsonMsgObject.getJSONArray("mobilePhoneNumber");
                            msg = (String) jsonArray.get(0);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        return new NewGenericError(msg, text, errorType);
                    } else {
                        return new NewGenericError(response.raw().message(), errorType);
                    }
                } else {
                    try {
                        JSONObject jsonObj = new JSONObject(response.errorBody().string());
                        NewGenericError error = new NewGenericError(jsonObj.getString("msg"));
                        if (error.msg.equals(SmallWorldErrorTypes.FRAUD_EMAIL)) {
                            return new NewGenericError(response.message(), ErrorType.FRAUD_EMAIL);
                        } else if (error.msg.equals(SmallWorldErrorTypes.LOGIN_INCORRECT) ||
                                error.msg.equals(SmallWorldErrorTypes.LOGIN_COUNTRY_BLOCK) ||
                                error.msg.equals(SmallWorldErrorTypes.LOGIN_USER_BLOCK)) {
                            return new NewGenericError(response.message(), ErrorType.LOGIN_INCORRECT);
                        } else {
                            return new NewGenericError(response.message(), ErrorType.SERVER_ERROR);
                        }
                    } catch (Exception e) {
                        return new NewGenericError(response.message(), ErrorType.SERVER_ERROR);
                    }
                }
            } else if (response.code() == 201) {
                try {
                    CreateTransactionResponse createTransactionResponse = (CreateTransactionResponse) response.body();

                    ArrayList<TransactionErrors> transactionErrors = createTransactionResponse.getErrors();
                    ArrayList<TransactionErrors> blockingErrors = new ArrayList<>();
                    TransactionErrors taxCodeError = null;

                    if (transactionErrors != null) {
                        for (TransactionErrors transactionError: transactionErrors) {
                            if (transactionError.isBlocking()) {
                                blockingErrors.add(transactionError);
                            }

                            if (transactionError.getSubtype().equals("WS_TAX_CODE_DOCUMENT")) {
                                taxCodeError = transactionError;
                            }
                        }
                    }

                    if (blockingErrors.size() > 0) {
                        if (taxCodeError != null) {
                            String title = taxCodeError.getTitle();
                            String description = taxCodeError.getDescription();
                            NewGenericError newGenericError = new NewGenericError(response.message(), ErrorType.TAX_CODE_ERROR);
                            newGenericError.title = title;
                            newGenericError.text = description;
                            return newGenericError;
                        } else {
                            return null;
                        }
                    } else {
                        return null;
                    }
                } catch (Exception e) {
                    return null;
                }
            }
            return null;
        }
    }


    public static boolean isConnectionAvailable() {
        ConnectivityManager cm = (ConnectivityManager)SmallWorldApplication.getApp().getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }

    public ErrorType getErrorType() {
        return errorType;
    }

    public void setErrorType(ErrorType errorType) {
        this.errorType = errorType;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
