package com.smallworldfs.moneytransferapp.modules.oauth.domain.repository;

import android.text.TextUtils;

import com.smallworldfs.moneytransferapp.SmallWorldApplication;
import com.smallworldfs.moneytransferapp.data.auth.oauth.model.RequestOAuthTokenDataModel;
import com.smallworldfs.moneytransferapp.data.oauth.model.ResponseOAuthTokenDataModel;
import com.smallworldfs.moneytransferapp.modules.common.preferences.UserPreferences;
import com.smallworldfs.moneytransferapp.modules.oauth.domain.service.OAuthNetworkDatasource;
import com.smallworldfs.moneytransferapp.utils.ConstantsKt;
import com.smallworldfs.moneytransferapp.utils.Log;

import dagger.hilt.EntryPoint;
import dagger.hilt.InstallIn;
import dagger.hilt.android.EntryPointAccessors;
import dagger.hilt.components.SingletonComponent;
import retrofit2.Call;

/**
 * Created by luismiguel on 3/5/17
 */
public class OAuthRepository {

    @EntryPoint
    @InstallIn(SingletonComponent.class)
    interface DaggerHiltEntryPoint {
        OAuthNetworkDatasource providesOAuthAccessTokenNetworkDatasource();
    }

    public OAuthNetworkDatasource oAuthNetworkDatasource;

    private static OAuthRepository sInstance = null;
    private UserPreferences mPrefs;

    public static OAuthRepository getInstance() {
        if (sInstance == null) {
            sInstance = new OAuthRepository();
            sInstance.mPrefs = new UserPreferences();
        }
        return sInstance;
    }

    public OAuthRepository() {
        OAuthRepository.DaggerHiltEntryPoint hiltEntryPoint =
                EntryPointAccessors.fromApplication(
                        SmallWorldApplication.getApp(),
                        OAuthRepository.DaggerHiltEntryPoint.class);
        oAuthNetworkDatasource = hiltEntryPoint.providesOAuthAccessTokenNetworkDatasource();
    }


    /**
     * Set persisted token in preferences
     *
     * @param oAuthToken
     */
    public void persistOAuthToken(String oAuthToken) {
        mPrefs.setOAuthToken(oAuthToken);
    }


    /**
     * Clear persisted oAuthToken
     */
    public void clearPersistedAccessToken() {
        mPrefs.setOAuthToken("");
    }

    public String getSyncAccessToken(RequestOAuthTokenDataModel request) {
        Call<ResponseOAuthTokenDataModel> call = oAuthNetworkDatasource.getSyncAccessTokenLegacy(request);
        try {
            if (call != null) {
                ResponseOAuthTokenDataModel token = call.execute().body();
                if (token != null && !TextUtils.isEmpty(token.getAccessToken())) {
                    String tokenBearer = "Bearer " + token.getAccessToken();
                    persistOAuthToken(tokenBearer);
                    return tokenBearer;
                }
            } else {
                return ConstantsKt.STRING_EMPTY;
            }
        } catch (Exception e) {
            Log.e("STACK", "getSyncAccessToken:e:----------------------", e);
        }
        return ConstantsKt.STRING_EMPTY;
    }
}
