package com.label305.kama.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.label305.kama.KamaParam;
import com.label305.kama.MyJsonParser;
import com.label305.kama.exceptions.HttpResponseKamaException;
import com.label305.kama.exceptions.JsonKamaException;
import com.label305.kama.exceptions.KamaException;
import com.label305.kama.exceptions.NotAuthorizedKamaException;
import com.label305.kama.utils.HttpUtils;
import com.label305.stan.http.StatusCodes;

import org.apache.http.HttpResponse;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked", "UnusedDeclaration"})
/**
 * An abstract class which can be extended to execute http requests.
 * Executes the request and parses the result to an object type or a list if necessary.
 */
public abstract class JsonRequestExecutor {

    private static final int HTTP_OK = 200;
    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_UNAUTHORIZED = 401;
    private static final int HTTP_NOT_FOUND = 404;
    private static final int HTTP_INTERNAL_ERROR = 500;

    private final ObjectMapper mObjectMapper = new ObjectMapper();
    private final MyJsonParser mMyJsonParser = new MyJsonParser();

    private String mUrl;
    private Class<?> mReturnTypeClass;
    private String mJsonTitle;
    private Map<String, Object> mUrlData;
    private Map<String, Object> mHeaderData;
    private Class<?> mErrorTypeClass;
    private String mErrorTitle;

    public void setUrl(final String url) {
        mUrl = url;
    }

    /**
     * Set the class type of the object that will be parsed, or the type of the objects in the list that will be parsed.
     */
    public void setReturnTypeClass(final Class<?> returnTypeClass) {
        mReturnTypeClass = returnTypeClass;
    }

    public void setJsonTitle(final String jsonTitle) {
        mJsonTitle = jsonTitle;
    }

    public void setUrlData(final Map<String, Object> urlData) {
        mUrlData = urlData;
    }

    public void setHeaderData(final Map<String, Object> headerData) {
        mHeaderData = headerData;
    }

    public void setErrorTypeClass(final Class<?> errorTypeClass) {
        mErrorTypeClass = errorTypeClass;
    }

    public void setErrorTitle(final String errorTitle) {
        mErrorTitle = errorTitle;
    }

    public String getUrl() {
        return mUrl;
    }

    public Class<?> getReturnTypeClass() {
        return mReturnTypeClass;
    }

    public String getJsonTitle() {
        return mJsonTitle;
    }

    public Map<String, Object> getUrlData() {
        return mUrlData;
    }

    public Map<String, Object> getHeaderData() {
        return mHeaderData;
    }

    public Class<?> getErrorTypeClass() {
        return mErrorTypeClass;
    }

    public String getErrorTitle() {
        return mErrorTitle;
    }

    public Object execute() throws KamaException {
        Object result;

        HttpResponse httpResponse = executeRequest();
        String responseString = HttpUtils.getStringFromResponse(httpResponse);
        if (httpResponse.getStatusLine().getStatusCode() == StatusCodes.HTTP_OK) {
            result = getJsonParser().parseObject(responseString, mReturnTypeClass, mJsonTitle);
        } else {
            throw createKamaException(responseString, httpResponse.getStatusLine().getStatusCode(), mErrorTypeClass, mErrorTitle);
        }

        return result;
    }

    public List<?> executeReturnsObjectsList() throws KamaException {
        List<?> result;

        HttpResponse httpResponse = executeRequest();
        String responseString = HttpUtils.getStringFromResponse(httpResponse);
        if (httpResponse.getStatusLine().getStatusCode() == StatusCodes.HTTP_OK) {
            result = getJsonParser().parseObjectsList(responseString, mReturnTypeClass, mJsonTitle);
        } else {
            throw createKamaException(responseString, httpResponse.getStatusLine().getStatusCode(), mErrorTypeClass, mErrorTitle);
        }

        return result;
    }

    protected abstract HttpResponse executeRequest() throws KamaException;

    protected static String addUrlParams(final String url, final Map<String, Object> urlData) {
        StringBuilder urlBuilder = new StringBuilder(url);

        if (urlData != null && !urlData.isEmpty()) {
            urlBuilder.append(KamaParam.URLPARAM);

            for (Iterator<String> iterator = urlData.keySet().iterator(); iterator.hasNext(); ) {
                String key = iterator.next();
                urlBuilder.append(key).append('=').append(replaceInvalidChars(urlData.get(key).toString()));

                if (iterator.hasNext()) {
                    urlBuilder.append(KamaParam.URLPARAMCONCAT);
                }
            }
        }

        return urlBuilder.toString();
    }


    private static String replaceInvalidChars(final String value) {
        return value.replace(" ", "%20");
    }

    /**
     * Returns the MyJsonParser which will parse received data from the request. May be overridden to return a suitable MyJsonParser.
     */
    protected MyJsonParser getJsonParser() {
        return mMyJsonParser;
    }

    protected Map<String, Object> addNecessaryHeaders(final Map<String, Object> headerData) {
        Map<String, Object> modifiedHeaderData = headerData == null ? new HashMap<String, Object>() : new HashMap<String, Object>(headerData);
        modifiedHeaderData.put(KamaParam.ACCEPT, KamaParam.APPLICATION_JSON);
        return modifiedHeaderData;
    }

    protected <V> KamaException createKamaException(final String responseString, final int statusCode, final Class<V> errorObjectClass, final String errorTitle) {
        KamaException kamaException;

        V errorObj = null;
        if (errorObjectClass != null) {
            try {
                errorObj = getJsonParser().parseObject(responseString, errorObjectClass, errorTitle);
            } catch (JsonKamaException e) {
                /* We don't care if the error object parsing fails */
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            }
        }

        switch (statusCode) {
            case StatusCodes.HTTP_BAD_REQUEST:
                kamaException = new HttpResponseKamaException("Bad Request. " + '\n' + responseString, errorObj, statusCode);
                break;
            case StatusCodes.HTTP_UNAUTHORIZED:
                kamaException = new NotAuthorizedKamaException("Unauthorized Action. " + '\n' + responseString, errorObj);
                break;
            case StatusCodes.HTTP_NOT_FOUND:
                kamaException = new HttpResponseKamaException("Not Found. " + '\n' + responseString, errorObj, statusCode);
                break;
            case StatusCodes.HTTP_INTERNAL_ERROR:
                kamaException = new HttpResponseKamaException("Internal Server Error. " + '\n' + responseString, errorObj, statusCode);
                break;
            default:
                kamaException = new HttpResponseKamaException("Unexpected Error. " + '\n' + responseString, statusCode);
        }

        return kamaException;
    }
}
