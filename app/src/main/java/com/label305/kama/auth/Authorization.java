package com.label305.kama.auth;

import android.content.Context;
import android.preference.PreferenceManager;

import com.label305.kama.exceptions.NotAuthorizedKamaException;

public class Authorization {

    private static final String PREF_AUTH_TOKEN = "kama_auth_token";

    private Authorization() {
    }

    public static String getAuthToken(final Context context) throws NotAuthorizedKamaException {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(PREF_AUTH_TOKEN, null);
    }

    public static void setAuthToken(final Context context, final String authToken) {
        PreferenceManager.getDefaultSharedPreferences(context).edit().putString(PREF_AUTH_TOKEN, authToken).commit();
    }

}
