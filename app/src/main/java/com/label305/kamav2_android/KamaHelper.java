package com.label305.kamav2_android;

import android.content.Context;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.j256.ormlite.dao.Dao;
import com.label305.kamav2_android.KamaParam.AuthenticationType;
import com.label305.kamav2_android.auth.AuthDatabaseHelper;
import com.label305.kamav2_android.auth.objects.AuthData;
import com.label305.kamav2_android.exceptions.JsonKamaException;
import com.label305.kamav2_android.exceptions.KamaException;
import com.label305.kamav2_android.exceptions.NotAuthorizedKamaException;
import com.label305.kamav2_android.objects.ErrorObj;

import org.apache.http.HttpResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("UnusedDeclaration")
public class KamaHelper extends JsonHelper {

    private final ObjectMapper mObjectMapper = new ObjectMapper();

    private final AuthDatabaseHelper mAuthDatabaseHelper;
    private final String mApiKey;

    private AuthenticationType mAuthType;

    public KamaHelper(final Context context, final String apiKey) {
        mAuthDatabaseHelper = AuthDatabaseHelper.getHelper(context);
        mApiKey = apiKey;
    }

    public void setAuthType(final AuthenticationType authType) {
        mAuthType = authType;
    }

    public AuthenticationType getAuthType() {
        return mAuthType;
    }

    private void validateAuthType() {
        if (mAuthType == null) {
            throw new IllegalArgumentException("Provide an AUTH_TYPE!");
        }
    }

    @Override
    protected void cleanup() {
        super.cleanup();
        mAuthType = null;
    }

    private void prepareRequest() throws NotAuthorizedKamaException {
        validateAuthType();
        setUrlData(addNecessaryUrlParams(getUrlData(), mAuthType));
        setHeaderData(addNecessaryHeaders(getHeaderData(), mAuthType));
        if (getErrorTypeClass() == null) {
            setErrorTypeClass(ErrorObj.class);
            setErrorTitle("meta");
        }
    }

    @Override
    public Object executeGetObject() throws KamaException {
        prepareRequest();
        return super.executeGetObject();
    }

    @Override
    public List<?> executeGetObjectsList() throws KamaException {
        prepareRequest();
        return super.executeGetObjectsList();
    }

    @Override
    public Object executeDelete() throws KamaException {
        prepareRequest();
        return super.executeDelete();
    }

    @Override
    public List<?> executeDeleteObjectsList() throws KamaException {
        prepareRequest();
        return super.executeDeleteObjectsList();
    }

    @Override
    public Object executePost() throws KamaException {
        prepareRequest();
        return super.executePost();
    }

    @Override
    public List<?> executePostObjectsList() throws KamaException {
        prepareRequest();
        return super.executePostObjectsList();
    }

    @Override
    public Object executePut() throws KamaException {
        prepareRequest();
        return super.executePut();
    }

    @Override
    public List<?> executePutObjectsList() throws KamaException {
        prepareRequest();
        return super.executePutObjectsList();
    }

    @Override
    protected <V> JsonParser getJsonParserFromResponse(final String url, final HttpResponse response, final Class<V> errorObjectClass, final String errorTitle) throws KamaException {
        try {
            JsonParser jsonParser = super.getJsonParserFromResponse(url, response, errorObjectClass, errorTitle);
            JsonNode jsonResponse = mObjectMapper.readTree(jsonParser);
            JsonNode retVal = jsonResponse.get(KamaParam.RESPONSE);
            if (retVal == null) {
                throw new JsonKamaException("Unexpected jsontitle. Not found: " + KamaParam.RESPONSE);
            }
            return retVal.traverse();
        } catch (JsonParseException e) {
            throw new JsonKamaException(e);
        } catch (JsonProcessingException e) {
            throw new JsonKamaException(e);
        } catch (IOException e) {
            throw new JsonKamaException(e);
        }
    }

    private Map<String, Object> addNecessaryUrlParams(final Map<String, Object> urlData, final AuthenticationType authType) {
        if (needsAuthUrl(authType)) {
            Map<String, Object> modifiedUrlData = urlData;
            if (modifiedUrlData == null) {
                modifiedUrlData = new HashMap<String, Object>();
            }

            modifiedUrlData.put(KamaParam.APIKEYPARAM, mApiKey);
            return modifiedUrlData;
        } else {
            return urlData;
        }
    }

    @Override
    protected Map<String, Object> addNecessaryHeaders(final Map<String, Object> headerData) {
        Map<String, Object> modifiedHeaderData = super.addNecessaryHeaders(headerData);
        modifiedHeaderData.put("Accept", "application/vnd.kama-v1+json");
        return modifiedHeaderData;
    }

    protected Map<String, Object> addNecessaryHeaders(final Map<String, Object> headerData, final AuthenticationType authType) throws NotAuthorizedKamaException {
        Map<String, Object> modifiedHeaderData = addNecessaryHeaders(headerData);
        if (needsOAuthHeader(authType)) {
            if (modifiedHeaderData == null) {
                modifiedHeaderData = new HashMap<String, Object>();
            }

            AuthData authToken = getAuthToken();
            modifiedHeaderData.put("Authorization", "OAuth2 " + authToken.getToken());
        }

        return modifiedHeaderData;
    }

    private static boolean needsAuthUrl(final AuthenticationType authType) {
        return authType == AuthenticationType.APIKEY || authType == AuthenticationType.OAUTHANDKEY;
    }

    private static boolean needsOAuthHeader(final AuthenticationType authType) {
        return authType == AuthenticationType.OAUTH2 || authType == AuthenticationType.OAUTHANDKEY;
    }

    public boolean isAuthenticated() {
        boolean authenticated;

        try {
            AuthData authToken = getAuthToken();
            authenticated = authToken != null && authToken.getToken() != null && authToken.getToken().length() > 0;
        } catch (NotAuthorizedKamaException ignored) {
            authenticated = false;
        }

        return authenticated;
    }

    private AuthData getAuthToken() throws NotAuthorizedKamaException {
        AuthData authToken;
        try {
            Dao<AuthData, Integer> kamaDao = mAuthDatabaseHelper.getAuthDataDao();

            List<AuthData> kamaData = kamaDao.queryForAll();

            if (kamaData != null && kamaData.size() > 0) {
                authToken = new AuthData(kamaData.get(0).getToken());
            } else {
                throw new NotAuthorizedKamaException("Not Authorized!");
            }
        } catch (SQLException e) {
            throw new NotAuthorizedKamaException(e);
        }

        return authToken;
    }
}
