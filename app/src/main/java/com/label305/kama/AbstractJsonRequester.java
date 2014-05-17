package com.label305.kama;

import com.label305.kama.exceptions.JsonKamaException;
import com.label305.kama.exceptions.KamaException;
import com.label305.kama.exceptions.status.BadRequestKamaException;
import com.label305.kama.exceptions.status.HttpResponseKamaException;
import com.label305.kama.exceptions.status.InternalErrorKamaException;
import com.label305.kama.exceptions.status.NotFoundKamaException;
import com.label305.kama.exceptions.status.UnauthorizedKamaException;
import com.label305.kama.objects.KamaError;
import com.label305.kama.utils.HttpUtils;
import com.label305.kama.utils.KamaParam;

import org.apache.http.Header;
import org.apache.http.HttpResponse;

import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * An abstract class which can be extended to execute http requests.
 * Handles url, url parameters and headers.
 * Executes the request and parses the result to an object type or a list if necessary.
 */
public abstract class AbstractJsonRequester<ReturnType> {

    private static final String LOCATION = "Location";

    private final MyJsonParser<ReturnType> mMyJsonParser;

    private final Map<String, Object> mUrlData = new HashMap<>(5);
    private final Map<String, Object> mHeaderData = new HashMap<>(5);

    private String mUrl;
    private String mJsonTitle;

    private static MyJsonParser<?> mErrorJsonParser;
    private static String mErrorTitle;

    AbstractJsonRequester() {
        mMyJsonParser = new MyJsonParser<>(null);
        init();
        mErrorJsonParser = new MyJsonParser<>(KamaError.class);
        mErrorTitle = KamaParam.META;
    }

    AbstractJsonRequester(final Class<ReturnType> returnTypeClass) {
        mMyJsonParser = new MyJsonParser<>(returnTypeClass);
        init();
        mErrorJsonParser = new MyJsonParser<>(KamaError.class);
        mErrorTitle = KamaParam.META;
    }

    private void init() {
        mHeaderData.put(KamaParam.ACCEPT, KamaParam.APPLICATION_JSON);
    }

    /**
     * Set the url to execute the request to.
     */
    public void setUrl(final String url) {
        mUrl = url;
    }

    /**
     * Set the title of the json object returned, or null for no title.
     */
    public void setJsonTitle(final String jsonTitle) {
        mJsonTitle = jsonTitle;
    }

    /**
     * Get the title of the json object
     *
     * @return title name
     */
    public String getJsonTitle() {
        return mJsonTitle;
    }

    /**
     * Add a url parameter.
     *
     * @param key   the key of the parameter.
     * @param value the value of the parameter. Will be displayed using the {@code toString()} method.
     */
    public void addUrlParameter(final String key, final Object value) {
        mUrlData.put(key, value);
    }

    /**
     * Add a header.
     *
     * @param key   the key of the header.
     * @param value the value of the header. Will be displayed using the {@code toString()} method.
     */
    public void addHeader(final String key, final Object value) {
        mHeaderData.put(key, value);
    }

    /**
     * Set custom Error type, error type defaults to KamaError.
     * Don't forget to set the ErrorTitle, if necessary
     * @param customErrorObjType the class type of the custom error object
     */
    public void setCustomErrorObjType(Class<?> customErrorObjType) {
        mErrorJsonParser = new MyJsonParser<>(customErrorObjType);
    }

    /**
     * Set title for the Error Object
     * @param errorTitle the title of the error object
     */
    public static void setErrorTitle(String errorTitle) {
        AbstractJsonRequester.mErrorTitle = errorTitle;
    }

    /**
     * Executes the request, and returns the parsed object.
     */
    public ReturnType execute() throws KamaException {
        if (mUrl == null) {
            throw new IllegalArgumentException("Provide an url!");
        }

        ReturnType result;

        HttpResponse httpResponse = executeRequest(createParameterizedUrl(), mHeaderData);
        String responseString = HttpUtils.getStringFromResponse(httpResponse);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (HttpUtils.isSuccessFul(statusCode)) {
            result = mMyJsonParser.parseObject(responseString, mJsonTitle);
        } else if (HttpUtils.isRedirect(statusCode)) {
            return executeRedirect(httpResponse, responseString, statusCode);
        } else {
            throw createKamaException(responseString, statusCode);
        }

        return result;
    }

    private ReturnType executeRedirect(final HttpResponse httpResponse, final String responseString, final int statusCode) throws KamaException {
        Header[] headers = httpResponse.getHeaders(LOCATION);
        if (headers == null || headers.length == 0) {
            throw createKamaException(responseString, statusCode);
        }
        Header header = headers[0];
        mUrl = header.getValue();
        return execute();
    }

    /**
     * Executes the request, and returns a List with the parsed objects.
     */
    public List<ReturnType> executeReturnsObjectsList() throws KamaException {
        List<ReturnType> result;

        HttpResponse httpResponse = executeRequest(createParameterizedUrl(), mHeaderData);
        String responseString = HttpUtils.getStringFromResponse(httpResponse);
        int statusCode = httpResponse.getStatusLine().getStatusCode();
        if (HttpUtils.isSuccessFul(statusCode)) {
            result = mMyJsonParser.parseObjectsList(responseString, mJsonTitle);
        } else if (HttpUtils.isRedirect(statusCode)) {
            return executeRedirectReturnsObjectsList(httpResponse, responseString, statusCode);
        } else {
            throw createKamaException(responseString, statusCode);
        }

        return result;
    }

    private List<ReturnType> executeRedirectReturnsObjectsList(final HttpResponse httpResponse, final String responseString, final int statusCode) throws KamaException {
        Header[] headers = httpResponse.getHeaders(LOCATION);
        if (headers == null || headers.length == 0) {
            throw createKamaException(responseString, statusCode);
        }
        Header header = headers[0];
        mUrl = header.getValue();
        return executeReturnsObjectsList();
    }

    /**
     * Execute the request and return the HttpResponse.
     *
     * @param parameterizedUrl the complete url including parameters.
     * @param headerData       key-value pairs of headers.
     */
    protected abstract HttpResponse executeRequest(final String parameterizedUrl, final Map<String, Object> headerData) throws KamaException;

    private String createParameterizedUrl() {
        StringBuilder urlBuilder = new StringBuilder(mUrl);

        if (!mUrlData.isEmpty()) {
            urlBuilder.append(KamaParam.URLPARAM);

            for (Iterator<String> iterator = mUrlData.keySet().iterator(); iterator.hasNext(); ) {
                String key = iterator.next();
                urlBuilder.append(key).append('=').append(replaceInvalidChars(mUrlData.get(key).toString()));

                if (iterator.hasNext()) {
                    urlBuilder.append(KamaParam.URLPARAMCONCAT);
                }
            }
        }

        return urlBuilder.toString();
    }

    private static KamaException createKamaException(final String responseString, final int statusCode) {
        KamaException kamaException;

        Object errorObj = null;
        try {
                errorObj = mErrorJsonParser.parseObject(responseString, mErrorTitle);
        } catch (JsonKamaException e) {
            /* We don't care if the error object parsing fails */
            //noinspection CallToPrintStackTrace
            e.printStackTrace();
        }

        switch (statusCode) {
            case HttpURLConnection.HTTP_BAD_REQUEST:
                kamaException = new BadRequestKamaException(responseString, errorObj);
                break;
            case HttpURLConnection.HTTP_UNAUTHORIZED:
                kamaException = new UnauthorizedKamaException(responseString, errorObj);
                break;
            case HttpURLConnection.HTTP_NOT_FOUND:
                kamaException = new NotFoundKamaException(responseString, errorObj);
                break;
            case HttpURLConnection.HTTP_INTERNAL_ERROR:
                kamaException = new InternalErrorKamaException(responseString, errorObj);
                break;
            default:
                kamaException = new HttpResponseKamaException("Unexpected error. " + '\n' + responseString, statusCode);
        }

        return kamaException;
    }

    private static String replaceInvalidChars(final String value) {
        return value.replace(" ", "%20");
    }
}
