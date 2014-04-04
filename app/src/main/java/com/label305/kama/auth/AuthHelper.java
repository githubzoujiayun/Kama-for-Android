package com.label305.kama.auth;

import android.content.Context;

import com.label305.kama.exceptions.NotAuthorizedKamaException;

import net.smartam.leeloo.common.exception.OAuthProblemException;
import net.smartam.leeloo.common.exception.OAuthSystemException;

public class AuthHelper {

    public static void authenticate(final Context context, final String authUrl, final String apiKey, final String login, final String password) throws NotAuthorizedKamaException {
        MyOAuthClient oAuthClient = new MyOAuthClient(new MyURLConnectionClient());

        try {
            String authToken = oAuthClient.authenticate(authUrl, apiKey, login, password);
            if (authToken != null && authToken.length() > 0) {
                Authorization.setAuthToken(context, authToken);
            }
        } catch (OAuthProblemException e) {
            throw new NotAuthorizedKamaException(e);
        } catch (OAuthSystemException e) {
            throw new NotAuthorizedKamaException(e);
        }
    }

    public static void authenticateFacebook(final Context context, final String authUrl, final String apiKey, final String accessToken) throws NotAuthorizedKamaException {
        MyOAuthClient oAuthClient = new MyOAuthClient(new MyURLConnectionClient());

        try {
            String authToken = oAuthClient.authenticateFacebook(authUrl, apiKey, accessToken);
            if (authToken != null && authToken.length() > 0) {
                Authorization.setAuthToken(context, authToken);
            }
        } catch (OAuthProblemException e) {
            throw new NotAuthorizedKamaException(e);
        } catch (OAuthSystemException e) {
            throw new NotAuthorizedKamaException(e);
        }
    }

    public static void logOut(final Context context) {
        Authorization.setAuthToken(context, null);
    }
}
