package com.label305.kamav2_android;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.util.EntityUtils;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.dao.Dao;
import com.label305.kamav2_android.auth.AuthDatabaseHelper;
import com.label305.kamav2_android.auth.objects.AuthData;
import com.label305.kamav2_android.exceptions.KamaException;
import com.label305.kamav2_android.exceptions.KamaException_HttpResponse;
import com.label305.kamav2_android.exceptions.KamaException_Json;
import com.label305.kamav2_android.exceptions.KamaException_Not_Authorized;

public class JsonHelper {

	protected AuthData authToken = null;

	protected AuthDatabaseHelper databaseHelper;
	protected ObjectMapper mapper;

	JsonHelper(AuthDatabaseHelper databaseHelper, String AppKey) {
		this.databaseHelper = databaseHelper;

		KamaParam.APPKEY = AppKey;

		mapper = new ObjectMapper();
	}

	public <T> T get(String url, Class<T> retType, List<NameValuePair> urlData, Map<String, String> headerData, KamaParam.AUTH_TYPE authType) throws KamaException {
		String finalUrl = this.setUrlParams(url, urlData, authType);
		Map<String, String> finalHeaderData = this.setAuthHeader(headerData, authType);

		HttpResponse httpResponse = HttpHelper.get(finalUrl, finalHeaderData);

		return getObject(httpResponse, retType);
	}

	public <T> T post(String url, Class<T> retType, List<NameValuePair> urlData, List<NameValuePair> postData, Map<String, String> headerData, KamaParam.AUTH_TYPE authType) throws KamaException {
		String finalUrl = this.setUrlParams(url, urlData, authType);
		Map<String, String> finalHeaderData = this.setAuthHeader(headerData, authType);

		HttpResponse httpResponse = HttpHelper.post(finalUrl, finalHeaderData, postData);

		return getObject(httpResponse, retType);
	}

	public <T> T put(String url, List<NameValuePair> urlData, List<NameValuePair> putData, Map<String, String> headerData, KamaParam.AUTH_TYPE authType, Class<T> retType) throws KamaException {
		String finalUrl = this.setUrlParams(url, urlData, authType);
		Map<String, String> finalHeaderData = this.setHeaders(headerData, authType);

		HttpResponse httpResponse = HttpHelper.put(finalUrl, finalHeaderData, putData);

		return getObject(httpResponse, retType);
	}

	protected <T> T getObject(HttpResponse httpResponse, Class<T> retType) throws KamaException_Json, KamaException_Not_Authorized, KamaException_HttpResponse {
		String jsonString = getStringFromResponse(httpResponse);
		T retVal = null;

		try {
			if(retType == List.class) {
				retVal = mapper.readValue(jsonString, mapper.getTypeFactory().constructCollectionType(List.class, retType.getEnclosingClass()));
			} else {
				retVal = mapper.readValue(jsonString, retType);
			}
		} catch (JsonParseException e) {
			throw new KamaException_Json(e);
		} catch (JsonMappingException e) {
			throw new KamaException_Json(e);
		} catch (IOException e) {
			throw new KamaException_Json(e);
		}

		return retVal;
	}
	
	protected Map<String, String> setHeaders(Map<String, String> headerData, KamaParam.AUTH_TYPE authType) throws KamaException_Not_Authorized {
		Map<String, String> finalHeaderData = setAuthHeader(headerData, authType);
		
		finalHeaderData.put("Accept", "application/json");

		return finalHeaderData;
	}

	protected Map<String, String> setAuthHeader(Map<String, String> headerData, KamaParam.AUTH_TYPE authType) throws KamaException_Not_Authorized {
		if (needsOAuthHeader(authType) && isAuthenticated()) 
			headerData.put("Authorization", "OAuth2 " + authToken.getToken());

		return headerData;
	}

	private boolean needsOAuthHeader(KamaParam.AUTH_TYPE authType) {
		if(authType == KamaParam.AUTH_TYPE.OAUTH2 || authType == KamaParam.AUTH_TYPE.OAUTHANDKEY) return true;
		return false;
	}

	private boolean needsAuthUrl(KamaParam.AUTH_TYPE authType) {
		if(authType == KamaParam.AUTH_TYPE.APIKEY || authType == KamaParam.AUTH_TYPE.OAUTHANDKEY) return true;
		return false;
	}

	protected String setUrlParams(String url, List<NameValuePair> urlData, KamaParam.AUTH_TYPE authType) {
		if (needsAuthUrl(authType) || (urlData != null && !urlData.isEmpty())) {
			url += KamaParam.URLPARAM;
		}

		if(needsAuthUrl(authType)) {
			url += KamaParam.getApiKey();
		}

		if (urlData != null) {
			for (NameValuePair data : urlData) {
				url += data.getName() + "=" + data.getValue();
			}
		}
		return url;
	}

	public boolean isAuthenticated() throws KamaException_Not_Authorized {

		boolean authenticated = false;

		if (authToken != null && authToken.getToken() != null && authToken.getToken().length() > 0)
			authenticated = true;
		else {
			getAuthToken();

			if (authToken != null && authToken.getToken() != null && authToken.getToken().length() > 0) {
				authenticated = true;
			} else {
				authenticated = false;
			}

		}

		return authenticated;
	}

	private void getAuthToken() throws KamaException_Not_Authorized {

		try {
			// get our dao
			Dao<AuthData, Integer> kamaDao = databaseHelper.getAuthDataDao();

			List<AuthData> kamaData = kamaDao.queryForAll();

			if (kamaData != null && kamaData.size() > 0) {
				authToken = new AuthData(kamaData.get(0).getToken());
			}
		} catch (SQLException e) {
			throw new KamaException_Not_Authorized(e);
		}
	}

	protected String getStringFromResponse(HttpResponse response) throws KamaException_Json, KamaException_Not_Authorized, KamaException_HttpResponse {
		try {
			switch (response.getStatusLine().getStatusCode()) {
			case 200:
				return EntityUtils.toString(response.getEntity());
			case 400:
				throw new KamaException_HttpResponse("Bad Request");
			case 401:
				throw new KamaException_Not_Authorized("Unauthorized Action");
			case 404:
				throw new KamaException_HttpResponse("Not Found");
			case 500:
				throw new KamaException_HttpResponse("Internal Server Error");
			default :
				throw new KamaException_HttpResponse("Unexpected Error");
			}

		} catch (IOException e) {
			throw new KamaException_Json(e);
		}
	}
}
