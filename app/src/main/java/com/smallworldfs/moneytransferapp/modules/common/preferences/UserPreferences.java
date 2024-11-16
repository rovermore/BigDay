package com.smallworldfs.moneytransferapp.modules.common.preferences;

import android.app.Activity;
import android.content.SharedPreferences;

import com.smallworldfs.moneytransferapp.SmallWorldApplication;

/**
 * Created by luismiguel on 3/5/17
 */
public class UserPreferences {
    private static final String PREFS_NAME = "SMALLWORLD";

    private static final String ONBOARD_SHOWED = "ONBOARD_SHOWED";
    private static final String OAUTH_TOKEN = "OAUTH_TOKEN";
    private static final String FIRST_ENTER_APP_WALKTHROUGH = "FIRST_ENTER_APP_WALKTHROUGH";
    private static final String FIRST_TRANSACTION_WALKTHROUGH = "FIRST_TRANSACTION_WALKTHROUGH";


    private SharedPreferences mSharedPrefs;
    private SharedPreferences.Editor mEditor;

    public UserPreferences() {
        this.mSharedPrefs = SmallWorldApplication.getApp().getSharedPreferences(PREFS_NAME, Activity.MODE_PRIVATE);
        this.mEditor = mSharedPrefs.edit();
    }

    public void clear() {
        mEditor.clear().commit();
    }

    public void setOnboardShowed() {
        mEditor.putBoolean(ONBOARD_SHOWED, true);
        mEditor.commit();
    }

    public boolean isOnboardShowed() {
        return mSharedPrefs.getBoolean(ONBOARD_SHOWED, false);
    }

    public void setOAuthToken(String oAuthToken) {
        if (mEditor != null) {
            mEditor.putString(OAUTH_TOKEN, oAuthToken);
            mEditor.commit();
        }

    }

    public void clearOAuthToken() {
        if (mEditor != null) {
            mEditor.remove(OAUTH_TOKEN);
            mEditor.commit();
        }

    }

    public String getOauthToken() {
        if (mSharedPrefs != null) {
            return mSharedPrefs.getString(OAUTH_TOKEN, "");
        }
        return "";
    }

    public boolean isFirstAppWalkThroughShowed() {
        if (mSharedPrefs != null) {
            return mSharedPrefs.getBoolean(FIRST_ENTER_APP_WALKTHROUGH, false);
        }
        return false;
    }

    public void setFirstAppWalkThroughShowed() {
        if (mEditor != null) {
            mEditor.putBoolean(FIRST_ENTER_APP_WALKTHROUGH, true);
            mEditor.commit();
        }
    }

    public void resetFirstAppWalkThroughShowed() {
        if (mEditor != null) {
            mEditor.putBoolean(FIRST_ENTER_APP_WALKTHROUGH, false);
            mEditor.commit();
        }
    }

    public boolean isFirstTransactionWalkThroughShowed() {
        if (mSharedPrefs != null) {
            return mSharedPrefs.getBoolean(FIRST_TRANSACTION_WALKTHROUGH, false);
        }
        return false;
    }

    public void setFirstTransactionWalkThroughShowed() {
        if (mEditor != null) {
            mEditor.putBoolean(FIRST_TRANSACTION_WALKTHROUGH, true);
            mEditor.commit();
        }
    }
}
