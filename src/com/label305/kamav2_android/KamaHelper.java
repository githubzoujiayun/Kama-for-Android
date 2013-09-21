package com.label305.kamav2_android;

import android.content.Context;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.j256.ormlite.dao.Dao;
import com.label305.kamav2_android.KamaParam.AUTH_TYPE;
import com.label305.kamav2_android.auth.AuthDatabaseHelper;
import com.label305.kamav2_android.auth.objects.AuthData;
import com.label305.kamav2_android.exceptions.HttpResponseKamaException;
import com.label305.kamav2_android.exceptions.JsonKamaException;
import com.label305.kamav2_android.exceptions.KamaException;
import com.label305.kamav2_android.exceptions.NotAuthorizedKamaException;
import com.label305.stan.asyncutils.Buggy;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KamaHelper extends JsonHelper {

    private AuthDatabaseHelper mAuthDatabaseHelper;
    private String mApiKey;

    public KamaHelper(Context context, String apiKey) {
        mAuthDatabaseHelper = AuthDatabaseHelper.getHelper(context);
        mApiKey = apiKey;
    }

    public <T, U> T get(String url, Class<T> retType, String objTitle, AUTH_TYPE authType) throws KamaException {
        return get(url, retType, null, objTitle, null, null, authType, null, null);
    }

    public <T, U> T get(String url, Class<T> retType, Class<U> listType, String listTitle, AUTH_TYPE authType) throws KamaException {
        return get(url, retType, listType, listTitle, null, null, authType, null, null);
    }

    public <T, U> T get(String url, Class<T> retType, String objTitle, List<NameValuePair> urlData, AUTH_TYPE authType) throws KamaException {
        return get(url, retType, null, objTitle, urlData, null, authType, null, null);
    }

    public <T, U> T get(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData, AUTH_TYPE authType)
            throws KamaException {
        return get(url, retType, listType, listTitle, urlData, null, authType, null, null);
    }

    public <T, U> T get(String url, Class<T> retType, String objTitle, Map<String, String> headerData, AUTH_TYPE authType) throws KamaException {
        return get(url, retType, null, objTitle, null, headerData, authType, null, null);
    }

    public <T, U> T get(String url, Class<T> retType, Class<U> listType, String listTitle, Map<String, String> headerData, AUTH_TYPE authType)
            throws KamaException {
        return get(url, retType, listType, listTitle, null, headerData, authType, null, null);
    }

    public <T, U, V> T get(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData,
                        Map<String, String> headerData, AUTH_TYPE authType, Class<V> errorObject, String errorTitle) throws KamaException {
        List<NameValuePair> modifiedUrlData = addNecessaryUrlParams(urlData, authType);
        Map<String, String> modifiedHeaderData = addNecessaryHeaders(headerData, authType);
        return super.get(url, retType, listType, listTitle, modifiedUrlData, modifiedHeaderData, errorObject, errorTitle);
    }

    public <T, U> T post(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> postData, AUTH_TYPE authType)
            throws KamaException {
        return post(url, retType, listType, listTitle, null, null, postData, authType, null, null);
    }

    public <T, U> T post(String url, Class<T> retType, String objTitle, List<NameValuePair> postData, AUTH_TYPE authType) throws KamaException {
        return post(url, retType, null, objTitle, null, null, postData, authType, null, null);
    }

    public <T, U> T post(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData,
                         List<NameValuePair> postData, AUTH_TYPE authType) throws KamaException {
        return post(url, retType, listType, listTitle, urlData, null, postData, authType, null, null);
    }

    public <T, U> T post(String url, Class<T> retType, String objTitle, List<NameValuePair> urlData, List<NameValuePair> postData, AUTH_TYPE authType)
            throws KamaException {
        return post(url, retType, null, objTitle, urlData, null, postData, authType, null, null);
    }

    public <T, U> T post(String url, Class<T> retType, String objTitle, Map<String, String> headerData, List<NameValuePair> postData,
                         AUTH_TYPE authType) throws KamaException {
        return post(url, retType, null, objTitle, null, headerData, postData, authType, null, null);
    }

    public <T, U> T post(String url, Class<T> retType, Class<U> listType, String listTitle, Map<String, String> headerData,
                         List<NameValuePair> postData, AUTH_TYPE authType) throws KamaException {
        return post(url, retType, listType, listTitle, null, headerData, postData, authType, null, null);
    }

    public <T, U, V> T post(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData,
                         Map<String, String> headerData, List<NameValuePair> postData, AUTH_TYPE authType, Class<V> errorObject, String errorTitle) throws KamaException {
        List<NameValuePair> modifiedUrlData = addNecessaryUrlParams(urlData, authType);
        Map<String, String> modifiedHeaderData = addNecessaryHeaders(headerData, authType);
        return super.post(url, retType, listType, listTitle, modifiedUrlData, modifiedHeaderData, postData, errorObject, errorTitle);
    }

    public <T, U> T put(String url, Class<T> retType, String objTitle, List<NameValuePair> postData, AUTH_TYPE authType) throws KamaException {
        return put(url, retType, null, objTitle, null, null, postData, authType, null, null);
    }

    public <T, U> T put(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> postData, AUTH_TYPE authType)
            throws KamaException {
        return put(url, retType, listType, listTitle, null, null, postData, authType, null, null);
    }

    public <T, U> T put(String url, Class<T> retType, String objTitle, List<NameValuePair> urlData, List<NameValuePair> postData, AUTH_TYPE authType)
            throws KamaException {
        return put(url, retType, null, objTitle, urlData, null, postData, authType, null, null);
    }

    public <T, U> T put(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData, List<NameValuePair> postData,
                        AUTH_TYPE authType) throws KamaException {
        return put(url, retType, listType, listTitle, urlData, null, postData, authType, null, null);
    }

    public <T, U> T put(String url, Class<T> retType, String objTitle, Map<String, String> headerData, List<NameValuePair> postData,
                        AUTH_TYPE authType) throws KamaException {
        return put(url, retType, null, objTitle, null, headerData, postData, authType, null, null);
    }

    public <T, U> T put(String url, Class<T> retType, Class<U> listType, String listTitle, Map<String, String> headerData,
                        List<NameValuePair> postData, AUTH_TYPE authType) throws KamaException {
        return put(url, retType, listType, listTitle, null, headerData, postData, authType, null, null);
    }

    public <T, U, V> T put(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData,
                        Map<String, String> headerData, List<NameValuePair> putData, AUTH_TYPE authType, Class<V> errorObject, String errorTitle) throws KamaException {
        List<NameValuePair> modifiedUrlData = addNecessaryUrlParams(urlData, authType);
        Map<String, String> modifiedHeaderData = addNecessaryHeaders(headerData, authType);
        return super.put(url, retType, listType, listTitle, modifiedUrlData, modifiedHeaderData, putData, errorObject, errorTitle);
    }
    
    public <T, U> T delete(String url, Class<T> retType, Class<U> listType, String listTitle, Map<String, String> headerData, AUTH_TYPE authType) throws KamaException {
        return delete(url, retType, listType, listTitle, null, headerData, authType);
    }

    public <T, U> T delete(String url, Class<T> retType, Class<U> listType, String listTitle, AUTH_TYPE authType)
            throws KamaException {
        return delete(url, retType, listType, listTitle, null, null, authType);
    }

    public <T, U> T delete(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData, Map<String, String> headerData, AUTH_TYPE authType) throws KamaException {
        List<NameValuePair> modifiedUrlData = addNecessaryUrlParams(urlData, authType);
        Map<String, String> modifiedHeaderData = addNecessaryHeaders(headerData, authType);
        return super.delete(url, retType, listType, listTitle, modifiedUrlData, modifiedHeaderData, null, null);
    }

    @Override
    protected <V> JsonParser getJsonParserFromResponse(String url, HttpResponse response, Class<V> errorObject, String errorTitle) throws JsonKamaException, NotAuthorizedKamaException,
            HttpResponseKamaException {
        try {
            JsonParser jsonParser = super.getJsonParserFromResponse(url, response, errorObject, errorTitle);
            JsonNode jsonResponse = mapper.readTree(jsonParser);
            JsonNode retVal = jsonResponse.get(KamaParam.RESPONSE);
            if (retVal == null) {
                Buggy.report(new Exception("Unexpected jsontitle. Not found: " + KamaParam.RESPONSE), url);
                throw new JsonKamaException("Unexpected jsontitle. Not found: " + KamaParam.RESPONSE);
            }
            return retVal.traverse();
        } catch (JsonParseException e) {
            Buggy.report(e, url);
            throw new JsonKamaException(e);
        } catch (IOException e) {
            throw new JsonKamaException(e);
        }
    }

    private List<NameValuePair> addNecessaryUrlParams(List<NameValuePair> urlData, AUTH_TYPE authType) {
        if (needsAuthUrl(authType)) {
            List<NameValuePair> modifiedUrlData = urlData;
            if (modifiedUrlData == null) {
                modifiedUrlData = new ArrayList<NameValuePair>();
            }

            modifiedUrlData.add(new BasicNameValuePair(KamaParam.APIKEYPARAM, mApiKey));
            return modifiedUrlData;
        } else {
            return urlData;
        }
    }

    @Override
    protected Map<String, String> addNecessaryHeaders(Map<String, String> headerData) {
        Map<String, String> modifiedHeaderData = super.addNecessaryHeaders(headerData);
        modifiedHeaderData.put("Accept", "application/vnd.kama-v1+json");
        return modifiedHeaderData;
    }

    protected Map<String, String> addNecessaryHeaders(Map<String, String> headerData, AUTH_TYPE authType) throws NotAuthorizedKamaException {
        Map<String, String> modifiedHeaderData = addNecessaryHeaders(headerData);
        if (needsOAuthHeader(authType)) {
            if (modifiedHeaderData == null) {
                modifiedHeaderData = new HashMap<String, String>();
            }

            AuthData authToken = getAuthToken();
            modifiedHeaderData.put("Authorization", "OAuth2 " + authToken.getToken());
        }

        return modifiedHeaderData;
    }

    private boolean needsAuthUrl(KamaParam.AUTH_TYPE authType) {
        if (authType == KamaParam.AUTH_TYPE.APIKEY || authType == KamaParam.AUTH_TYPE.OAUTHANDKEY)
            return true;
        return false;
    }

    private boolean needsOAuthHeader(KamaParam.AUTH_TYPE authType) {
        if (authType == KamaParam.AUTH_TYPE.OAUTH2 || authType == KamaParam.AUTH_TYPE.OAUTHANDKEY)
            return true;
        return false;
    }

    public boolean isAuthenticated() {
        boolean authenticated = false;

        try {
            AuthData authToken = getAuthToken();
            if (authToken != null && authToken.getToken() != null && authToken.getToken().length() > 0) {
                authenticated = true;
            } else {
                authenticated = false;
            }
        } catch (NotAuthorizedKamaException e) {
            authenticated = false;
        }

        return authenticated;
    }

    private AuthData getAuthToken() throws NotAuthorizedKamaException {
        AuthData authToken = null;
        try {
            // get our dao
            Dao<AuthData, Integer> kamaDao = mAuthDatabaseHelper.getAuthDataDao();

            List<AuthData> kamaData = kamaDao.queryForAll();

            if (kamaData != null && kamaData.size() > 0) {
                authToken = new AuthData(kamaData.get(0).getToken());
            }
        } catch (SQLException e) {
            throw new NotAuthorizedKamaException(e);
        }

        return authToken;
    }

	/* DEPRECATED METHODS. ONLY USE METHODS WHICH REQUIRE AN AUTH_TYPE */

    @Override
    @Deprecated
    public <T, U> T get(String url, Class<T> retType, String objTitle) throws KamaException {
        throw new IllegalStateException("Only use methods which require an AUTH_TYPE!");
    }

    @Override
    @Deprecated
    public <T, U> T get(String url, Class<T> retType, Class<U> listType, String listTitle) throws KamaException {
        throw new IllegalStateException("Only use methods which require an AUTH_TYPE!");
    }

    @Override
    @Deprecated
    public <T, U> T get(String url, Class<T> retType, String objTitle, List<NameValuePair> urlData) throws KamaException {
        throw new IllegalStateException("Only use methods which require an AUTH_TYPE!");
    }

    @Override
    @Deprecated
    public <T, U> T get(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData) throws KamaException {
        throw new IllegalStateException("Only use methods which require an AUTH_TYPE!");
    }

    @Override
    @Deprecated
    public <T, U> T get(String url, Class<T> retType, String objTitle, Map<String, String> headerData) throws KamaException {
        throw new IllegalStateException("Only use methods which require an AUTH_TYPE!");
    }

    @Override
    @Deprecated
    public <T, U> T get(String url, Class<T> retType, Class<U> listType, String listTitle, Map<String, String> headerData) throws KamaException {
        throw new IllegalStateException("Only use methods which require an AUTH_TYPE!");
    }

    @Override
    @Deprecated
    public <T, U, V> T get(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData, Map<String, String> headerData, Class<V> errorObj, String errorTitle)
            throws KamaException {
        throw new IllegalStateException("Only use methods which require an AUTH_TYPE!");
    }

    @Override
    @Deprecated
    public <T, U> T post(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> postData) throws KamaException {
        throw new IllegalStateException("Only use methods which require an AUTH_TYPE!");
    }

    @Override
    @Deprecated
    public <T, U> T post(String url, Class<T> retType, String objTitle, List<NameValuePair> postData) throws KamaException {
        throw new IllegalStateException("Only use methods which require an AUTH_TYPE!");
    }

    @Override
    @Deprecated
    public <T, U> T post(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData, List<NameValuePair> postData)
            throws KamaException {
        throw new IllegalStateException("Only use methods which require an AUTH_TYPE!");
    }

    @Override
    @Deprecated
    public <T, U> T post(String url, Class<T> retType, String objTitle, List<NameValuePair> urlData, List<NameValuePair> postData)
            throws KamaException {
        throw new IllegalStateException("Only use methods which require an AUTH_TYPE!");
    }

    @Override
    @Deprecated
    public <T, U> T post(String url, Class<T> retType, String objTitle, Map<String, String> headerData, List<NameValuePair> postData)
            throws KamaException {
        throw new IllegalStateException("Only use methods which require an AUTH_TYPE!");
    }

    @Override
    @Deprecated
    public <T, U> T post(String url, Class<T> retType, Class<U> listType, String listTitle, Map<String, String> headerData,
                         List<NameValuePair> postData) throws KamaException {
        throw new IllegalStateException("Only use methods which require an AUTH_TYPE!");
    }

    @Override
    @Deprecated
    public <T, U, V> T post(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData,
                         Map<String, String> headerData, List<NameValuePair> postData, Class<V> errorObj, String errorTitle) throws KamaException {
        throw new IllegalStateException("Only use methods which require an AUTH_TYPE!");
    }

    @Override
    @Deprecated
    public <T, U> T put(String url, Class<T> retType, String objTitle, List<NameValuePair> postData) throws KamaException {
        throw new IllegalStateException("Only use methods which require an AUTH_TYPE!");
    }

    @Override
    @Deprecated
    public <T, U> T put(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> postData) throws KamaException {
        throw new IllegalStateException("Only use methods which require an AUTH_TYPE!");
    }

    @Override
    @Deprecated
    public <T, U> T put(String url, Class<T> retType, String objTitle, List<NameValuePair> urlData, List<NameValuePair> postData)
            throws KamaException {
        throw new IllegalStateException("Only use methods which require an AUTH_TYPE!");
    }

    @Override
    @Deprecated
    public <T, U> T put(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData, List<NameValuePair> postData)
            throws KamaException {
        throw new IllegalStateException("Only use methods which require an AUTH_TYPE!");
    }

    @Override
    @Deprecated
    public <T, U> T put(String url, Class<T> retType, String objTitle, Map<String, String> headerData, List<NameValuePair> postData)
            throws KamaException {
        throw new IllegalStateException("Only use methods which require an AUTH_TYPE!");
    }

    @Override
    @Deprecated
    public <T, U> T put(String url, Class<T> retType, Class<U> listType, String listTitle, Map<String, String> headerData,
                        List<NameValuePair> postData) throws KamaException {
        throw new IllegalStateException("Only use methods which require an AUTH_TYPE!");
    }

    @Override
    @Deprecated
    public <T, U, V> T put(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData,
                        Map<String, String> headerData, List<NameValuePair> putData, Class<V> errorObj, String errorTitle) throws KamaException {
        throw new IllegalStateException("Only use methods which require an AUTH_TYPE!");
    }
}
