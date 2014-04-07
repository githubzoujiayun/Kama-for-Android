package com.label305.kama;

import android.content.Context;

import com.label305.kama.auth.Authorization;
import com.label305.kama.exceptions.JsonKamaException;
import com.label305.kama.exceptions.KamaException;
import com.label305.kama.exceptions.status.BadRequestKamaException;
import com.label305.kama.exceptions.status.HttpResponseKamaException;
import com.label305.kama.exceptions.status.InternalErrorKamaException;
import com.label305.kama.exceptions.status.NotFoundKamaException;
import com.label305.kama.exceptions.status.UnauthorizedKamaException;
import com.label305.kama.objects.KamaError;
import com.label305.kama.parser.KamaJsonParser;
import com.label305.kama.parser.MyJsonParser;
import com.label305.kama.request.AbstractJsonRequester;
import com.label305.kama.utils.HttpUtils;
import com.label305.kama.utils.KamaParam;
import com.label305.stan.http.StatusCodes;

import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class AbstractKamaRequester<ReturnType> extends AbstractJsonRequester<ReturnType> {

    private final KamaJsonParser<ReturnType> mKamaJsonParser;

    private final String mApiKey;
    private final Context mContext;
    private KamaParam.AuthenticationType mAuthType;

    public AbstractKamaRequester(final Context context, final String apiKey) {
        mKamaJsonParser = new KamaJsonParser<ReturnType>(null);
        mApiKey = apiKey;
        mContext = context;
    }

    public AbstractKamaRequester(final Class<ReturnType> returnTypeClass, final Context context, final String apiKey) {
        super(returnTypeClass);
        mKamaJsonParser = new KamaJsonParser<ReturnType>(returnTypeClass);
        mApiKey = apiKey;
        mContext = context;
    }


    @Override
    public ReturnType execute() throws KamaException {
        prepareRequest();

        HttpResponse httpResponse = executeRequest();
        String responseString = HttpUtils.getStringFromResponse(httpResponse);
        if (httpResponse.getStatusLine().getStatusCode() == StatusCodes.HTTP_OK) {
            return mKamaJsonParser.parseObject(responseString, getJsonTitle());
        } else {
            throw createKamaException(responseString, httpResponse.getStatusLine().getStatusCode());
        }
    }

    @Override
    public List<ReturnType> executeReturnsObjectsList() throws KamaException {
        prepareRequest();

        HttpResponse httpResponse = executeRequest();
        String responseString = HttpUtils.getStringFromResponse(httpResponse);
        if (httpResponse.getStatusLine().getStatusCode() == StatusCodes.HTTP_OK) {
            return mKamaJsonParser.parseObjectsList(responseString, getJsonTitle());
        } else {
            throw createKamaException(responseString, httpResponse.getStatusLine().getStatusCode());
        }
    }

    public void setAuthType(final KamaParam.AuthenticationType authType) {
        mAuthType = authType;
    }

    private void prepareRequest() throws UnauthorizedKamaException {
        validateAuthType();
        setUrlData(addNecessaryUrlParams(getUrlData(), mAuthType));
        setHeaderData(addNecessaryHeaders(getHeaderData(), mAuthType));
    }

    private void validateAuthType() {
        if (mAuthType == null) {
            throw new IllegalArgumentException("Provide an AUTH_TYPE!");
        }
    }

    @Override
    protected Map<String, Object> addNecessaryHeaders(final Map<String, Object> headerData) {
        Map<String, Object> modifiedHeaderData = super.addNecessaryHeaders(headerData);
        modifiedHeaderData.put(KamaParam.ACCEPT, KamaParam.APPLICATION_KAMA);
        return modifiedHeaderData;
    }

    protected Map<String, Object> addNecessaryHeaders(final Map<String, Object> headerData, final KamaParam.AuthenticationType authType) throws UnauthorizedKamaException {
        Map<String, Object> modifiedHeaderData = addNecessaryHeaders(headerData);
        if (needsOAuthHeader(authType)) {
            if (modifiedHeaderData == null) {
                modifiedHeaderData = new HashMap<String, Object>();
            }

            modifiedHeaderData.put(KamaParam.AUTHORIZATION, KamaParam.OAUTH2 + Authorization.getAuthToken(mContext));
        }

        return modifiedHeaderData;
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

    @Override
    protected KamaException createKamaException(final String responseString, final int statusCode) {
        KamaException kamaException;

        KamaError kamaError = null;
        try {
            kamaError = new MyJsonParser<KamaError>(KamaError.class).parseObject(responseString, KamaParam.META);
        } catch (JsonKamaException e) {
                /* We don't care if the error object parsing fails */
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }

        switch (statusCode) {
            case StatusCodes.HTTP_BAD_REQUEST:
                kamaException = new BadRequestKamaException(responseString, kamaError);
                break;
            case StatusCodes.HTTP_UNAUTHORIZED:
                kamaException = new UnauthorizedKamaException(responseString, kamaError);
                break;
            case StatusCodes.HTTP_NOT_FOUND:
                kamaException = new NotFoundKamaException(responseString, kamaError);
                break;
            case StatusCodes.HTTP_INTERNAL_ERROR:
                kamaException = new InternalErrorKamaException(responseString, kamaError);
                break;
            default:
                kamaException = new HttpResponseKamaException("Unexpected KamaError. " + '\n' + responseString, statusCode);
        }
        return kamaException;
    }

    private static boolean needsAuthUrl(final KamaParam.AuthenticationType authType) {
        return authType == KamaParam.AuthenticationType.APIKEY || authType == KamaParam.AuthenticationType.OAUTHANDKEY;
    }

    private static boolean needsOAuthHeader(final KamaParam.AuthenticationType authType) {
        return authType == KamaParam.AuthenticationType.OAUTH2 || authType == KamaParam.AuthenticationType.OAUTHANDKEY;
    }
}
