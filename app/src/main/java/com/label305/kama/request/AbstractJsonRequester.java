package com.label305.kama.request;

import com.label305.kama.exceptions.KamaException;
import com.label305.kama.exceptions.status.BadRequestKamaException;
import com.label305.kama.exceptions.status.HttpResponseKamaException;
import com.label305.kama.exceptions.status.InternalErrorKamaException;
import com.label305.kama.exceptions.status.NotFoundKamaException;
import com.label305.kama.exceptions.status.UnauthorizedKamaException;
import com.label305.kama.parser.MyJsonParser;
import com.label305.kama.utils.HttpUtils;
import com.label305.kama.utils.KamaParam;

import org.apache.http.HttpResponse;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * An abstract class which can be extended to execute http requests.
 * Executes the request and parses the result to an object type or a list if necessary.
 */
public abstract class AbstractJsonRequester<ReturnType> {

    private final MyJsonParser<ReturnType> mMyJsonParser;

    private final Map<String, Object> mUrlData = new HashMap<>(5);
    private final Map<String, Object> mHeaderData = new HashMap<>(5);

    private String mUrl;
    private String mJsonTitle;

    protected AbstractJsonRequester() {
        mMyJsonParser = new MyJsonParser<ReturnType>(null);
    }

    protected AbstractJsonRequester(final Class<ReturnType> returnTypeClass) {
        mMyJsonParser = new MyJsonParser<ReturnType>(returnTypeClass);
    }

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

    protected String getUrl() {
        return mUrl;
    }

    public void setUrl(final String url) {
        mUrl = url;
    }

    protected String getJsonTitle() {
        return mJsonTitle;
    }

    /**
     * Set the title of the json object returned, or null for no title.
     */
    public void setJsonTitle(final String jsonTitle) {
        mJsonTitle = jsonTitle;
    }

    protected Map<String, Object> getUrlData() {
        return mUrlData;
    }

    public void addUrlParameter(final String key, final Object value) {
        mUrlData.put(key, value);
    }

    /**
     * Set url parameters to be appended to the url.
     * @deprecated use {@link #addUrlParameter(String, Object)} instead.
     */
    @Deprecated
    public void setUrlData(final Map<String, Object> urlData) {
        for (final Map.Entry<String, Object> entry : urlData.entrySet()) {
            mUrlData.put(entry.getKey(), entry.getValue());
        }
    }

    protected Map<String, Object> getHeaderData() {
        return mHeaderData;
    }

    public void addHeader(final String key, final Object value) {
        mHeaderData.put(key, value);
    }

    /**
     * Set the data which should be put in the headers.
     * @deprecated use {@link #addHeader(String, Object)} instead;
     */
    @Deprecated
    public void setHeaderData(final Map<String, Object> headerData) {
        for (final Map.Entry<String, Object> entry : headerData.entrySet()) {
            addHeader(entry.getKey(), entry.getValue());
        }
    }

    public ReturnType execute() throws KamaException {
        ReturnType result;

        HttpResponse httpResponse = executeRequest();
        String responseString = HttpUtils.getStringFromResponse(httpResponse);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (HttpUtils.isSuccessFul(statusCode)) {
            result = mMyJsonParser.parseObject(responseString, mJsonTitle);
        } else {
            throw createKamaException(responseString, statusCode);
        }

        return result;
    }

    public List<ReturnType> executeReturnsObjectsList() throws KamaException {
        List<ReturnType> result;

        HttpResponse httpResponse = executeRequest();
        String responseString = HttpUtils.getStringFromResponse(httpResponse);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (HttpUtils.isSuccessFul(statusCode)) {
            result = mMyJsonParser.parseObjectsList(responseString, mJsonTitle);
        } else {
            throw createKamaException(responseString, statusCode);
        }

        return result;
    }

    protected abstract HttpResponse executeRequest() throws KamaException;

    protected Map<String, Object> addNecessaryHeaders(final Map<String, Object> headerData) {
        Map<String, Object> modifiedHeaderData = headerData == null ? new HashMap<String, Object>() : new HashMap<String, Object>(headerData);
        modifiedHeaderData.put(KamaParam.ACCEPT, KamaParam.APPLICATION_JSON);
        return modifiedHeaderData;
    }

    protected KamaException createKamaException(final String responseString, final int statusCode) {
        KamaException kamaException;

        switch (statusCode) {
            case HttpURLConnection.HTTP_BAD_REQUEST:
                kamaException = new BadRequestKamaException(responseString);
                break;
            case HttpURLConnection.HTTP_UNAUTHORIZED:
                kamaException = new UnauthorizedKamaException(responseString);
                break;
            case HttpURLConnection.HTTP_NOT_FOUND:
                kamaException = new NotFoundKamaException(responseString);
                break;
            case HttpURLConnection.HTTP_INTERNAL_ERROR:
                kamaException = new InternalErrorKamaException(responseString);
                break;
            default:
                kamaException = new HttpResponseKamaException("Unexpected error. " + '\n' + responseString, statusCode);
        }

        return kamaException;
    }
}
