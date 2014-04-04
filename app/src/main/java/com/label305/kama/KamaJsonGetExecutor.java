package com.label305.kama;

import android.content.Context;

import com.label305.kama.auth.Authorization;
import com.label305.kama.exceptions.KamaException;
import com.label305.kama.exceptions.NotAuthorizedKamaException;
import com.label305.kama.json.JsonGetExecutor;
import com.label305.kama.objects.ErrorObj;
import com.label305.stan.http.GetExecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class KamaJsonGetExecutor extends JsonGetExecutor {

    private final KamaJsonParser mKamaJsonParser = new KamaJsonParser();

    private final String mApiKey;

    private KamaParam.AuthenticationType mAuthType;
    private final Context mContext;


    public KamaJsonGetExecutor(final Context context, final String apiKey) {
        mApiKey = apiKey;
        mContext = context;
    }

    public KamaJsonGetExecutor(final Context context, final GetExecutor getExecutor, final String apiKey) {
        super(getExecutor);
        mApiKey = apiKey;
        mContext = context;
    }

    @Override
    protected MyJsonParser getJsonParser() {
        return mKamaJsonParser;
    }


    public void setAuthType(final KamaParam.AuthenticationType authType) {
        mAuthType = authType;
    }

    public KamaParam.AuthenticationType getAuthType() {
        return mAuthType;
    }

    @Override
    public Object execute() throws KamaException {
        prepareRequest();
        return super.execute();
    }

    @Override
    public List<?> executeReturnsObjectsList() throws KamaException {
        prepareRequest();
        return super.executeReturnsObjectsList();
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

    private void validateAuthType() {
        if (mAuthType == null) {
            throw new IllegalArgumentException("Provide an AUTH_TYPE!");
        }
    }

    protected Map<String, Object> addNecessaryHeaders(final Map<String, Object> headerData, final KamaParam.AuthenticationType authType) throws NotAuthorizedKamaException {
        Map<String, Object> modifiedHeaderData = addNecessaryHeaders(headerData);
        if (needsOAuthHeader(authType)) {
            if (modifiedHeaderData == null) {
                modifiedHeaderData = new HashMap<String, Object>();
            }

            modifiedHeaderData.put("Authorization", "OAuth2 " + Authorization.getAuthToken(mContext));
        }

        return modifiedHeaderData;
    }


    private static boolean needsOAuthHeader(final KamaParam.AuthenticationType authType) {
        return authType == KamaParam.AuthenticationType.OAUTH2 || authType == KamaParam.AuthenticationType.OAUTHANDKEY;
    }

    private Map<String, Object> addNecessaryUrlParams(final Map<String, Object> urlData, final KamaParam.AuthenticationType authType) {
        Map<String, Object> result;

        if (needsAuthUrl(authType)) {
            Map<String, Object> modifiedUrlData = urlData;
            if (modifiedUrlData == null) {
                modifiedUrlData = new HashMap<String, Object>();
            }

            modifiedUrlData.put(KamaParam.APIKEYPARAM, mApiKey);
            result = modifiedUrlData;
        } else {
            result = urlData;
        }

        return result;
    }

    private static boolean needsAuthUrl(final KamaParam.AuthenticationType authType) {
        return authType == KamaParam.AuthenticationType.APIKEY || authType == KamaParam.AuthenticationType.OAUTHANDKEY;
    }
}
