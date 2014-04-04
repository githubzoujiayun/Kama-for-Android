package com.label305.kamav2_android.auth;

import java.util.HashMap;
import java.util.Map;

import net.smartam.leeloo.client.HttpClient;
import net.smartam.leeloo.client.OAuthClient;
import net.smartam.leeloo.client.request.OAuthClientRequest;
import net.smartam.leeloo.client.response.OAuthAccessTokenResponse;
import net.smartam.leeloo.client.response.OAuthJSONAccessTokenResponse;
import net.smartam.leeloo.common.OAuth;
import net.smartam.leeloo.common.exception.OAuthProblemException;
import net.smartam.leeloo.common.exception.OAuthSystemException;
import net.smartam.leeloo.common.message.types.GrantType;
import android.util.Base64;

public class MyOAuthClient extends OAuthClient {

	public MyOAuthClient(HttpClient oauthClient) {
		super(oauthClient);
		// TODO Auto-generated constructor stub
	}
	
	public <T extends OAuthAccessTokenResponse> T accessToken(
        OAuthClientRequest request,
        Class<T> responseClass)
        throws OAuthSystemException, OAuthProblemException {

        String method = OAuth.HttpMethod.POST;
        Map<String, String> headers = new HashMap<String, String>();
        headers = request.getHeaders();
        headers.put(OAuth.HeaderType.CONTENT_TYPE, OAuth.ContentType.URL_ENCODED);
        
        
        return httpClient.execute(request, headers, method, responseClass);
    }
	
	public String authenticate(String authUrl, String Appkey, String login, String password) throws OAuthSystemException, OAuthProblemException {
		
		OAuthClientRequest request = null;
		
		byte[] basicSource = (login + ":" + password).getBytes();
		String basic = Base64.encodeToString(basicSource, Base64.NO_WRAP);
		Map<String, String> basicHeader = new HashMap<String, String>();
		
		basicHeader.put(OAuth.HeaderType.AUTHORIZATION, "Basic " + basic);
		basicHeader.put("X-Expects", "Kama");

		request = OAuthClientRequest.tokenLocation(authUrl)
				.setGrantType(GrantType.PASSWORD)
				.setUsername(login)
				.setPassword(password)
				.setParameter("app_key", Appkey)
				.buildBodyMessage();

		request.setHeaders(basicHeader);
		
		
		OAuthJSONAccessTokenResponse response = accessToken(request);

		return response.getAccessToken();
	}
	
	public String authenticateFacebook(String authUrl, String Appkey, String AccessToken) throws OAuthSystemException, OAuthProblemException {
		
		OAuthClientRequest request = null;
		
		Map<String, String> header = new HashMap<String, String>();
		
		header.put("X-Expects", "Kama");

		request = OAuthClientRequest.tokenLocation(authUrl)
				.setParameter("grant_type", "facebook_access_token")
				.setParameter("access_token", AccessToken)
				.setParameter("app_key", Appkey)
				.buildBodyMessage();

		request.setHeaders(header);
		
		
		OAuthJSONAccessTokenResponse response = accessToken(request);

		return response.getAccessToken();
	}

}
