package com.label305.kama;

import android.content.Context;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.label305.kama.auth.Authorization;
import com.label305.kama.exceptions.KamaException;
import com.label305.kama.objects.KamaObject;
import com.label305.kama.utils.KamaParam;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class KamaWrapper<ReturnType> {

    private final AbstractJsonRequester<KamaObject> mJsonRequester;
    private final Class<ReturnType> mReturnTypeClass;
    private final Context mContext;

    private String mJsonTitle;
    private String mApiKey;
    private KamaParam.AuthenticationType mAuthType = KamaParam.AuthenticationType.NONE;

    public KamaWrapper(final Context context, final AbstractJsonRequester<KamaObject> jsonRequester, final Class<ReturnType> returnTypeClass) {
        mContext = context;
        mJsonRequester = jsonRequester;
        mReturnTypeClass = returnTypeClass;
    }

    /**
     * Set the key of the ReturnType object in the returned JSON.
     */
    public void setJsonTitle(final String jsonTitle) {
        mJsonTitle = jsonTitle;
    }

    /**
     * Use no authentication type. This is the default.
     */
    public void useNoAuthentication() {
        mAuthType = KamaParam.AuthenticationType.NONE;
    }

    /**
     * Use an api key for authentication.
     */
    public void useApiKeyAuthentication(final String apiKey) {
        mAuthType = KamaParam.AuthenticationType.APIKEY;
        mApiKey = apiKey;
    }

    /**
     * Use OAuth2 for authentication.
     */
    public void useOAuthAuthentication() {
        mAuthType = KamaParam.AuthenticationType.OAUTH2;
    }

    /**
     * Use OAuth2 and an api key for authentication.
     */
    public void useOAuthAndApiKeyAuthentication(final String apiKey) {
        mAuthType = KamaParam.AuthenticationType.OAUTHANDKEY;
        mApiKey = apiKey;
    }

    /**
     * Executes the wrapped request, using the Kama protocol.
     * @return the object that was embedded in the JSON response.
     * @throws KamaException when something went wrong.
     */
    public ReturnType execute() throws KamaException {
        prepareRequest();


        mJsonTitle = mJsonRequester.getJsonTitle();
        mJsonRequester.setJsonTitle(null);
        try {
            KamaObject execute = mJsonRequester.execute();
            Map<String, Object> responseMap = execute.getResponseMap();
            Object o = responseMap.get(mJsonTitle);
            String json = new ObjectMapper().writeValueAsString(o);
            return new ObjectMapper().readValue(json, mReturnTypeClass);
        } catch (IOException e) {
            throw new KamaException(e);
        }
    }

    /**
     * Executes the wrapped request, using the Kama protocol.
     * @return the list of objects that was embedded in the JSON response.
     * @throws KamaException when something went wrong.
     */
    public List<ReturnType> executeReturnsObjectsList() throws KamaException {
        prepareRequest();

        mJsonTitle = mJsonRequester.getJsonTitle();
        mJsonRequester.setJsonTitle(null);
        try {
            KamaObject execute = mJsonRequester.execute();
            Map<String, Object> responseMap = execute.getResponseMap();
            String json = new ObjectMapper().writeValueAsString(responseMap);
            return new MyJsonParser<>(mReturnTypeClass).parseObjectsList(json, mJsonTitle);
        } catch (IOException e) {
            throw new KamaException(e);
        }
    }

    private void prepareRequest() {
        validateAuthType();
        addNecessaryHeaders();
        addNecessaryUrlParams();
    }

    private void validateAuthType() {
        if (mAuthType == null) {
            throw new IllegalArgumentException("Provide an AUTH_TYPE!");
        }

        switch (mAuthType) {
            case APIKEY:
            case OAUTHANDKEY:
                if (mApiKey == null) {
                    throw new IllegalArgumentException("Provide an API_KEY!");
                }
                break;
        }
    }

    private void addNecessaryHeaders() {
        mJsonRequester.addHeader(KamaParam.ACCEPT, KamaParam.APPLICATION_KAMA);

        boolean shouldAddOAuthHeader = mAuthType == KamaParam.AuthenticationType.OAUTH2 || mAuthType == KamaParam.AuthenticationType.OAUTHANDKEY;
        if (shouldAddOAuthHeader) {
            mJsonRequester.addHeader(KamaParam.AUTHORIZATION, KamaParam.OAUTH2 + Authorization.getAuthToken(mContext));
        }
    }

    private void addNecessaryUrlParams() {
        boolean shouldAddApiParam = mAuthType == KamaParam.AuthenticationType.APIKEY || mAuthType == KamaParam.AuthenticationType.OAUTHANDKEY;
        if (shouldAddApiParam) {
            mJsonRequester.addUrlParameter(KamaParam.APIKEYPARAM, mApiKey);
        }
    }
}
