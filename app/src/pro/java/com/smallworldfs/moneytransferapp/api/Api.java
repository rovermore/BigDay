package com.smallworldfs.moneytransferapp.api;


import com.smallworldfs.moneytransferapp.BuildConfig;

/**
 * PRODUCTION
 */

public class Api {
    public static String BASE_URL = BuildConfig.base_url;
    public static String WEB_URL = BuildConfig.web_url;
    public static long TIMEOUT = 300L;

    public interface API_USER {
        String API_CLIENT_ID = BuildConfig.API_CLIENT_ID;
        String API_CLIENT_SECRET = BuildConfig.api_client_secret;
        String API_CLIENT_USER = BuildConfig.api_client_user;
        String API_CLIENT_PASSWORD = BuildConfig.api_client_password;
        String API_GRANT_TYPE = BuildConfig.api_grant_type;
        String ESCRATCH_API_KEY = BuildConfig.escratch_api_key;
        String RISKIFIED_TOKEN = BuildConfig.riskified_token;
    }

}
