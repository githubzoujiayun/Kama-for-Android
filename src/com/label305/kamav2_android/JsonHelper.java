package com.label305.kamav2_android;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

import android.content.Context;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.dao.Dao;
import com.label305.kamav2_android.KamaParam.AUTH_TYPE;
import com.label305.kamav2_android.auth.AuthDatabaseHelper;
import com.label305.kamav2_android.auth.objects.AuthData;
import com.label305.kamav2_android.exceptions.KamaException;
import com.label305.kamav2_android.exceptions.HttpResponseKamaException;
import com.label305.kamav2_android.exceptions.JsonKamaException;
import com.label305.kamav2_android.exceptions.NotAuthorizedKamaException;
import com.label305.kamav2_android.utils.HttpUtils;

public class JsonHelper {

	protected AuthData authToken = null;

	protected AuthDatabaseHelper databaseHelper;
	protected ObjectMapper mapper;

	public JsonHelper(Context context, String apiKey) {
		this.databaseHelper = AuthDatabaseHelper.getHelper(context);

		KamaParam.APIKEY = apiKey;

		mapper = new ObjectMapper();
	}

	public <T, U> T get(String url, Class<T> retType, AUTH_TYPE authType, String objTitle) throws KamaException {
		return this.get(url, retType, null, objTitle, authType, new ArrayList<NameValuePair>(), new HashMap<String, String>());
	}

	public <T, U> T get(String url, Class<T> retType, Class<U> listType, String listTitle, AUTH_TYPE authType) throws KamaException {
		return this.get(url, retType, listType, listTitle, authType, new ArrayList<NameValuePair>(), new HashMap<String, String>());
	}

	public <T, U> T get(String url, Class<T> retType, AUTH_TYPE authType, String objTitle, List<NameValuePair> urlData) throws KamaException {
		return this.get(url, retType, null, objTitle, authType, urlData, new HashMap<String, String>());
	}

	public <T, U> T get(String url, Class<T> retType, Class<U> listType, String listTitle, AUTH_TYPE authType, List<NameValuePair> urlData) throws KamaException {
		return this.get(url, retType, listType, listTitle, authType, urlData, new HashMap<String, String>());
	}

	public <T, U> T get(String url, Class<T> retType, AUTH_TYPE authType, String objTitle, Map<String, String> headerData) throws KamaException {
		return this.get(url, retType, null, objTitle, authType, new ArrayList<NameValuePair>(), headerData);
	}

	public <T, U> T get(String url, Class<T> retType, Class<U> listType, String listTitle, AUTH_TYPE authType, Map<String, String> headerData) throws KamaException {
		return this.get(url, retType, listType, listTitle, authType, new ArrayList<NameValuePair>(), headerData);
	}

	/**
	 * @param url
	 *            request url
	 * @param retType
	 *            Return class type
	 * @param listType
	 *            Element class of list items
	 * @param listTitle
	 *            Title of JsonArray
	 * @param authType
	 *            Authentication Type
	 * @param urlData
	 *            Url data
	 * @param headerData
	 *            Header data
	 * @return returns object of Class T
	 * @throws KamaException
	 */
	public <T, U> T get(String url, Class<T> retType, Class<U> listType, String listTitle, AUTH_TYPE authType, List<NameValuePair> urlData, Map<String, String> headerData) throws KamaException {
		String finalUrl = this.setUrlParams(url, urlData, authType);
		Map<String, String> finalHeaderData = this.setHeaders(headerData, authType);

		HttpHelper httpHelper = new HttpHelper();

		try {
			HttpResponse httpResponse = httpHelper.get(finalUrl, finalHeaderData);

			return getObject(httpResponse, retType, listType, listTitle);
		} finally {
			httpHelper.close();
		}
	}

	public <T, U> T post(String url, Class<T> retType, Class<U> listType, String listTitle, AUTH_TYPE authType, List<NameValuePair> postData) throws KamaException {
		return this.post(url, retType, listType, listTitle, authType, new ArrayList<NameValuePair>(), new HashMap<String, String>(), postData);
	}

	public <T, U> T post(String url, Class<T> retType, AUTH_TYPE authType, String objTitle, List<NameValuePair> postData) throws KamaException {
		return this.post(url, retType, null, objTitle, authType, new ArrayList<NameValuePair>(), new HashMap<String, String>(), postData);
	}

	public <T, U> T post(String url, Class<T> retType, Class<U> listType, String listTitle, AUTH_TYPE authType, List<NameValuePair> urlData, List<NameValuePair> postData) throws KamaException {
		return this.post(url, retType, listType, listTitle, authType, urlData, new HashMap<String, String>(), postData);
	}

	public <T, U> T post(String url, Class<T> retType, AUTH_TYPE authType, String objTitle, List<NameValuePair> urlData, List<NameValuePair> postData) throws KamaException {
		return this.post(url, retType, null, objTitle, authType, urlData, new HashMap<String, String>(), postData);
	}

	public <T, U> T post(String url, Class<T> retType, AUTH_TYPE authType, String objTitle, Map<String, String> headerData, List<NameValuePair> postData) throws KamaException {
		return this.post(url, retType, null, objTitle, authType, new ArrayList<NameValuePair>(), headerData, postData);
	}

	public <T, U> T post(String url, Class<T> retType, Class<U> listType, String listTitle, AUTH_TYPE authType, Map<String, String> headerData, List<NameValuePair> postData) throws KamaException {
		return this.post(url, retType, listType, listTitle, authType, new ArrayList<NameValuePair>(), headerData, postData);
	}

	/**
	 * @param url
	 *            request url
	 * @param retType
	 *            Return class type
	 * @param listType
	 *            Element class of list items
	 * @param listTitle
	 *            Title of JsonArray
	 * @param authType
	 *            Authentication Type
	 * @param urlData
	 *            Url data
	 * @param headerData
	 *            Header data
	 * @param postData
	 *            Post data
	 * @return returns object of Class T
	 * @throws KamaException
	 */
	public <T, U> T post(String url, Class<T> retType, Class<U> listType, String listTitle, AUTH_TYPE authType, List<NameValuePair> urlData, Map<String, String> headerData,
			List<NameValuePair> postData) throws KamaException {
		String finalUrl = this.setUrlParams(url, urlData, authType);
		Map<String, String> finalHeaderData = this.setHeaders(headerData, authType);

		HttpHelper httpHelper = new HttpHelper();

		try {
			HttpResponse httpResponse = httpHelper.post(finalUrl, finalHeaderData, postData);

			return getObject(httpResponse, retType, listType, listTitle);
		} finally {
			httpHelper.close();
		}
	}

	public <T, U> T put(String url, Class<T> retType, AUTH_TYPE authType, String objTitle, List<NameValuePair> postData) throws KamaException {
		return this.put(url, retType, null, objTitle, authType, new ArrayList<NameValuePair>(), new HashMap<String, String>(), postData);
	}

	public <T, U> T put(String url, Class<T> retType, Class<U> listType, String listTitle, AUTH_TYPE authType, List<NameValuePair> postData) throws KamaException {
		return this.put(url, retType, listType, listTitle, authType, new ArrayList<NameValuePair>(), new HashMap<String, String>(), postData);
	}

	public <T, U> T put(String url, Class<T> retType, AUTH_TYPE authType, String objTitle, List<NameValuePair> urlData, List<NameValuePair> postData) throws KamaException {
		return this.put(url, retType, null, objTitle, authType, urlData, new HashMap<String, String>(), postData);
	}

	public <T, U> T put(String url, Class<T> retType, Class<U> listType, String listTitle, AUTH_TYPE authType, List<NameValuePair> urlData, List<NameValuePair> postData) throws KamaException {
		return this.put(url, retType, listType, listTitle, authType, urlData, new HashMap<String, String>(), postData);
	}

	public <T, U> T put(String url, Class<T> retType, AUTH_TYPE authType, String objTitle, Map<String, String> headerData, List<NameValuePair> postData) throws KamaException {
		return this.put(url, retType, null, objTitle, authType, new ArrayList<NameValuePair>(), headerData, postData);
	}

	public <T, U> T put(String url, Class<T> retType, Class<U> listType, String listTitle, AUTH_TYPE authType, Map<String, String> headerData, List<NameValuePair> postData) throws KamaException {
		return this.put(url, retType, listType, listTitle, authType, new ArrayList<NameValuePair>(), headerData, postData);
	}

	/**
	 * @param url
	 *            request url
	 * @param retType
	 *            Return class type
	 * @param listType
	 *            Element class of list items
	 * @param listTitle
	 *            Title of JsonArray
	 * @param authType
	 *            Authentication Type
	 * @param urlData
	 *            Url data
	 * @param headerData
	 *            Header data
	 * @param putData
	 *            Put data
	 * @return returns object of Class T
	 * @throws KamaException
	 */
	public <T, U> T put(String url, Class<T> retType, Class<U> listType, String listTitle, AUTH_TYPE authType, List<NameValuePair> urlData, Map<String, String> headerData, List<NameValuePair> putData)
			throws KamaException {
		String finalUrl = this.setUrlParams(url, urlData, authType);
		Map<String, String> finalHeaderData = this.setHeaders(headerData, authType);

		HttpHelper httpHelper = new HttpHelper();

		try {
			HttpResponse httpResponse = httpHelper.put(finalUrl, finalHeaderData, putData);

			return getObject(httpResponse, retType, listType, listTitle);
		} finally {
			httpHelper.close();
		}
	}

	protected <T, U> T getObject(HttpResponse httpResponse, Class<T> retType, Class<U> listType, String objTitle) throws JsonKamaException, NotAuthorizedKamaException, HttpResponseKamaException {
		JsonParser jsonParser = getJsonParserFromResponse(httpResponse);
		T retVal = null;

		try {
			if (retType == ArrayList.class || retType == List.class) {

				if (jsonParser.isExpectedStartArrayToken())
					retVal = mapper.readValue(jsonParser, mapper.getTypeFactory().constructCollectionType(List.class, listType));
				else {
					JsonNode response = mapper.readTree(jsonParser);
					JsonNode responseStr = response.get(objTitle);
					JsonParser jp1 = responseStr.traverse();
					retVal = mapper.readValue(jp1, mapper.getTypeFactory().constructCollectionType(List.class, listType));
				}

			} else {
				if(objTitle != null) {
					JsonNode response = mapper.readTree(jsonParser);
					JsonNode responseStr = response.get(objTitle);
					jsonParser = responseStr.traverse();
				}
				retVal = mapper.readValue(jsonParser, retType);
			}
		} catch (JsonParseException e) {
			throw new JsonKamaException(e);
		} catch (JsonMappingException e) {
			throw new JsonKamaException(e);
		} catch (IOException e) {
			throw new JsonKamaException(e);
		}

		return retVal;
	}

	protected Map<String, String> setHeaders(Map<String, String> headerData, KamaParam.AUTH_TYPE authType) throws NotAuthorizedKamaException {
		Map<String, String> finalHeaderData = setAuthHeader(headerData, authType);

		finalHeaderData.put("Accept", "application/json");

		return finalHeaderData;
	}

	protected Map<String, String> setAuthHeader(Map<String, String> headerData, KamaParam.AUTH_TYPE authType) throws NotAuthorizedKamaException {
		Map<String, String> finalHeaderData = headerData;
		if (needsOAuthHeader(authType) && isAuthenticated())
			finalHeaderData.put("Authorization", "OAuth2 " + authToken.getToken());

		return finalHeaderData;
	}

	private boolean needsOAuthHeader(KamaParam.AUTH_TYPE authType) {
		if (authType == KamaParam.AUTH_TYPE.OAUTH2 || authType == KamaParam.AUTH_TYPE.OAUTHANDKEY)
			return true;
		return false;
	}

	private boolean needsAuthUrl(KamaParam.AUTH_TYPE authType) {
		if (authType == KamaParam.AUTH_TYPE.APIKEY || authType == KamaParam.AUTH_TYPE.OAUTHANDKEY)
			return true;
		return false;
	}

	protected String setUrlParams(String url, List<NameValuePair> urlData, KamaParam.AUTH_TYPE authType) {
		String finalUrl = url;

		boolean hasParam = false;
		if (needsAuthUrl(authType) || (urlData != null && !urlData.isEmpty())) {
			finalUrl += KamaParam.URLPARAM;
		}

		if (needsAuthUrl(authType)) {
			finalUrl += KamaParam.getApiKey();
			hasParam = true;
		}

		if (urlData != null && !urlData.isEmpty()) {
			if (hasParam) {
				finalUrl += KamaParam.URLPARAMCONCAT;
			}

			Iterator<NameValuePair> iterator = urlData.iterator();
			while (iterator.hasNext()) {
				NameValuePair data = iterator.next();
				finalUrl += data.getName() + "=" + data.getValue();

				if (iterator.hasNext()) {
					finalUrl += KamaParam.URLPARAMCONCAT;
				}
			}
		}
		return finalUrl;
	}

	public boolean isAuthenticated() {
		boolean authenticated = false;

		if (authToken != null && authToken.getToken() != null && authToken.getToken().length() > 0)
			authenticated = true;
		else {
			try {
				getAuthToken();
				if (authToken != null && authToken.getToken() != null && authToken.getToken().length() > 0) {
					authenticated = true;
				} else {
					authenticated = false;
				}
			} catch (NotAuthorizedKamaException e) {
				authenticated = false;
			}
		}

		return authenticated;
	}

	private void getAuthToken() throws NotAuthorizedKamaException {

		try {
			// get our dao
			Dao<AuthData, Integer> kamaDao = databaseHelper.getAuthDataDao();

			List<AuthData> kamaData = kamaDao.queryForAll();

			if (kamaData != null && kamaData.size() > 0) {
				authToken = new AuthData(kamaData.get(0).getToken());
			}
		} catch (SQLException e) {
			throw new NotAuthorizedKamaException(e);
		}
	}

	protected JsonParser getJsonParserFromResponse(HttpResponse response) throws JsonKamaException, NotAuthorizedKamaException, HttpResponseKamaException {

		switch (response.getStatusLine().getStatusCode()) {
		case 200:
			JsonFactory jsonFactory = new JsonFactory();
			JsonParser jp;
			try {
				jp = jsonFactory.createJsonParser(HttpUtils.getStringFromResponse(response));
			} catch (JsonParseException e) {
				throw new JsonKamaException(e);
			} catch (IOException e) {
				throw new JsonKamaException(e);
			}
			return jp;
		case 400:
			throw new HttpResponseKamaException("Bad Request");
		case 401:
			throw new NotAuthorizedKamaException("Unauthorized Action");
		case 404:
			throw new HttpResponseKamaException("Not Found");
		case 500:
			throw new HttpResponseKamaException("Internal Server Error");
		default:
			throw new HttpResponseKamaException("Unexpected Error");
		}

	}
}
