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
import com.label305.kamav2_android.exceptions.KamaException;
import com.label305.kamav2_android.exceptions.KamaException_HttpResponse;
import com.label305.kamav2_android.exceptions.KamaException_Json;
import com.label305.kamav2_android.exceptions.KamaException_Not_Authorized;
import com.label305.kamav2_android.objects.AuthData;

public class JsonHelper {
	
	protected AuthData authToken = null;
	
	protected AuthDatabaseHelper databaseHelper;
	protected ObjectMapper mapper;
	
	JsonHelper(AuthDatabaseHelper databaseHelper) {
		this.databaseHelper = databaseHelper;
		mapper = new ObjectMapper();
	}

	public <T> T get(String url, Class<T> retType, Map<String, String> headerData) throws KamaException {
		return this.get(url, retType, null, headerData);
	}
	
	public <T> T get(String url, Class<T> retType, List<NameValuePair> urlData, Map<String, String> headerData) throws KamaException {
		return this.get(url, retType, urlData, headerData, KamaParam.AUTH_TYPE.NONE);
	}
	
	public <T> T get(String url, Class<T> retType, List<NameValuePair> urlData, Map<String, String> headerData, KamaParam.AUTH_TYPE authType) throws KamaException {
		url = this.setUrlParams(url, urlData, authType);
		headerData = this.setAuthHeader(headerData, authType);
		
		HttpResponse httpResponse = HttpHelper.get(url, headerData);
		
		return getObject(httpResponse, retType);
	}
	
	public <T> T post(String url, Class<T> retType, List<NameValuePair> urlData, List<NameValuePair> postData, Map<String, String> headerData, KamaParam.AUTH_TYPE authType) throws KamaException {
		url = this.setUrlParams(url, urlData, authType);
		headerData = this.setAuthHeader(headerData, authType);
		
		HttpResponse httpResponse = HttpHelper.post(url, headerData, postData);
		
		return getObject(httpResponse, retType);
	}
	
	public <T> T put(String url, List<NameValuePair> urlData, List<NameValuePair> putData, Map<String, String> headerData, KamaParam.AUTH_TYPE authType, Class<T> retType) throws KamaException {
		url = this.setUrlParams(url, urlData, authType);
		headerData = this.setAuthHeader(headerData, authType);
		
		HttpResponse httpResponse = HttpHelper.put(url, headerData, putData);
		
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

	private Map<String, String> setAuthHeader(Map<String, String> headerData, KamaParam.AUTH_TYPE authType) throws KamaException_Not_Authorized {
		if (needsOAuthHeader(authType) && isAuthenticated()) 
			headerData.put("Authorization", "OAuth2 " + authToken.getToken());
		
		return headerData;
	}
	 
	protected boolean needsOAuthHeader(KamaParam.AUTH_TYPE authType) {
		if(authType == KamaParam.AUTH_TYPE.OAUTH2 || authType == KamaParam.AUTH_TYPE.OAUTHANDKEY) return true;
		return false;
	}
	
	protected String setUrlParams(String url, List<NameValuePair> urlData, KamaParam.AUTH_TYPE authType) {
		if (authType == KamaParam.AUTH_TYPE.APIKEY || authType == KamaParam.AUTH_TYPE.OAUTHANDKEY || (urlData != null && !urlData.isEmpty())) {
			url += KamaParam.URLPARAM;
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
	
	public String getStringFromResponse(HttpResponse response) throws KamaException_Json, KamaException_Not_Authorized, KamaException_HttpResponse {
		try {
			if (response != null) {

				switch (response.getStatusLine().getStatusCode()) {
				case 200:
					return EntityUtils.toString(response.getEntity());
				case 400:
					throw new KamaException_HttpResponse("Bad Request");
	
				case 401:
					throw new KamaException_Not_Authorized("unauthorized http action");
	
				case 404:
					throw new KamaException_HttpResponse("Not Found");
	
				case 500:
					throw new KamaException_HttpResponse("Internal Server Error");
				}
	
			}
		} catch (IOException e) {
			throw new KamaException_Json(e);
		}
		return null;
	}
}
