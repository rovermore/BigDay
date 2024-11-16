package com.smallworldfs.moneytransferapp.data.oauth.repository.local

import android.content.SharedPreferences
import com.smallworldfs.moneytransferapp.utils.STRING_EMPTY
import javax.inject.Inject

class OAuthLocal @Inject constructor(
    private val sharedPreferences: SharedPreferences
) {

    companion object {
        private const val OAUTH_TOKEN = "OAUTH_TOKEN"
    }

    fun persistOAuthToken(oAuthToken: String) {
        sharedPreferences.edit().putString(OAUTH_TOKEN, oAuthToken).apply()
    }

    fun getPersistedOAuthToken(): String {
        return sharedPreferences.getString(OAUTH_TOKEN, STRING_EMPTY) ?: STRING_EMPTY
    }

    fun clearPersistedOAuthToken() {
        sharedPreferences.edit().remove(OAUTH_TOKEN).apply()
    }
}
