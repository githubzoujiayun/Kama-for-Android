package com.label305.kama.auth;

import android.util.Base64;

import net.smartam.leeloo.client.HttpClient;
import net.smartam.leeloo.client.OAuthClient;
import net.smartam.leeloo.client.request.OAuthClientRequest;
import net.smartam.leeloo.client.response.OAuthAccessTokenResponse;
import net.smartam.leeloo.client.response.OAuthJSONAccessTokenResponse;
import net.smartam.leeloo.common.OAuth;
import net.smartam.leeloo.common.exception.OAuthProblemException;
import net.smartam.leeloo.common.exception.OAuthSystemException;
import net.smartam.leeloo.common.message.types.GrantType;

import java.util.HashMap;
import java.util.Map;


class MyOAuthClient extends OAuthClient {

    MyOAuthClient(final HttpClient oauthClient) {
        super(oauthClient);
    }

    @Override
    public <T extends OAuthAccessTokenResponse> T accessToken(final OAuthClientRequest request, final Class<T> responseClass) throws OAuthSystemException, OAuthProblemException {
        String method = OAuth.HttpMethod.POST;
        Map<String, String> headers = request.getHeaders();
        headers.put(OAuth.HeaderType.CONTENT_TYPE, OAuth.ContentType.URL_ENCODED);
        return httpClient.execute(request, headers, method, responseClass);
    }

    public String authenticate(final String authUrl, final String appKey, final String login, final String password) throws OAuthSystemException, OAuthProblemException {
        byte[] basicSource = (login + ':' + password).getBytes();
        String basic = Base64.encodeToString(basicSource, Base64.NO_WRAP);
        Map<String, String> basicHeader = new HashMap<String, String>();

        basicHeader.put(OAuth.HeaderType.AUTHORIZATION, "Basic " + basic);
        basicHeader.put("X-Expects", "Kama");

        OAuthClientRequest request = OAuthClientRequest.tokenLocation(authUrl)
                .setGrantType(GrantType.PASSWORD)
                .setUsername(login)
                .setPassword(password)
                .setParameter("app_key", appKey)
                .buildBodyMessage();

        request.setHeaders(basicHeader);
        OAuthJSONAccessTokenResponse response = accessToken(request);

        return response.getAccessToken();
    }

    public String authenticateFacebook(final String authUrl, final String appKey, final String accessToken) throws OAuthSystemException, OAuthProblemException {
        OAuthClientRequest request;

        Map<String, String> header = new HashMap<String, String>();

        header.put("X-Expects", "Kama");

        request = OAuthClientRequest.tokenLocation(authUrl)
                .setParameter("grant_type", "facebook_access_token")
                .setParameter("access_token", accessToken)
                .setParameter("app_key", appKey)
                .buildBodyMessage();

        request.setHeaders(header);
        return accessToken(request).getAccessToken();
    }

    public String authenticateLinkedIn(final String authUrl, final String appKey, final String accessToken, final String accesTokenSecret) throws OAuthSystemException, OAuthProblemException {
        OAuthClientRequest request;

        Map<String, String> header = new HashMap<String, String>();

        header.put("X-Expects", "Kama");

        request = OAuthClientRequest.tokenLocation(authUrl)
                .setParameter("grant_type", "service_linkedin")
                .setParameter("oauth_token",accessToken)
                .setParameter("oauth_token_secret",accesTokenSecret)
                .setParameter("app_key", appKey)
                .buildBodyMessage();

        request.setHeaders(header);
        return accessToken(request).getAccessToken();
    }
}
