package com.label305.kamav2_android;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.label305.kamav2_android.exceptions.HttpResponseKamaException;
import com.label305.kamav2_android.exceptions.JsonKamaException;
import com.label305.kamav2_android.exceptions.KamaException;
import com.label305.kamav2_android.exceptions.NotAuthorizedKamaException;
import com.label305.kamav2_android.utils.HttpUtils;
import com.label305.stan.utils.HttpHelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"unchecked", "UnusedDeclaration"})
/**
 * Helper class for executing http requests and parsing output. 
 */
public class JsonHelper {

    private static final int HTTP_OK = 200;
    private static final int HTTP_BAD_REQUEST = 400;
    private static final int HTTP_UNAUTHORIZED = 401;
    private static final int HTTP_NOT_FOUND = 404;
    private static final int HTTP_INTERNAL_ERROR = 500;
    private final ObjectMapper mObjectMapper = new ObjectMapper();

    private String mUrl;
    private Class<?> mReturnTypeClass;
    private String mJsonTitle;
    private Map<String, Object> mUrlData;
    private Map<String, Object> mHeaderData;
    private AbstractHttpEntity mPostData;
    private AbstractHttpEntity mPutData;
    private AbstractHttpEntity mDeleteData;
    private Class<?> mErrorTypeClass;
    private String mErrorTitle;

    public void setUrl(final String url) {
        mUrl = url;
    }

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

    public void setPostData(final Map<String, Object> postData) throws KamaException {
        if (postData != null) {
            try {
                mPostData = new UrlEncodedFormEntity(HttpHelper.convert(postData));
            } catch (UnsupportedEncodingException e) {
                throw new KamaException(e);
            }
        }
    }

    public void setPostData(final String jsonData) throws KamaException {
        if (jsonData != null) {
            try {
                mPostData = new StringEntity(jsonData);
            } catch (UnsupportedEncodingException e) {
                throw new KamaException(e);
            }
        }
    }

    public void setPutData(final Map<String, Object> putData) throws KamaException {
        if (putData != null) {
            try {
                mPutData = new UrlEncodedFormEntity(HttpHelper.convert(putData));
            } catch (UnsupportedEncodingException e) {
                throw new KamaException(e);
            }
        }
    }

    public void setPutData(final String jsonData) throws KamaException {
        if (jsonData != null) {
            try {
                mPutData = new StringEntity(jsonData);
            } catch (UnsupportedEncodingException e) {
                throw new KamaException(e);
            }
        }
    }

    public void setDeleteData(final Map<String, Object> deleteData) throws KamaException {
        if (deleteData != null) {
            try {
                mDeleteData = new UrlEncodedFormEntity(HttpHelper.convert(deleteData));
            } catch (UnsupportedEncodingException e) {
                throw new KamaException(e);
            }
        }
    }

    public void setDeleteData(final String jsonData) throws KamaException {
        if (jsonData != null) {
            try {
                mDeleteData = new StringEntity(jsonData);
            } catch (UnsupportedEncodingException e) {
                throw new KamaException(e);
            }
        }
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

    public AbstractHttpEntity getPostData() {
        return mPostData;
    }

    public AbstractHttpEntity getPutData() {
        return mPutData;
    }

    public AbstractHttpEntity getDeleteData() {
        return mDeleteData;
    }

    public Class<?> getErrorTypeClass() {
        return mErrorTypeClass;
    }

    public String getErrorTitle() {
        return mErrorTitle;
    }

    public ObjectMapper getMapper() {
        return mObjectMapper;
    }

    /**
     * Execute a get request for a single object. Required parameters: mUrl,
     * returnType.
     *
     * @return the parsed object.
     */
    public Object executeGetObject() throws KamaException {
        validateGetArguments();
        Object result = get(mUrl, mReturnTypeClass, false, mJsonTitle, mUrlData, mHeaderData, mErrorTypeClass, mErrorTitle);
        cleanup();
        return result;
    }

    /**
     * Execute a get request for a list of objects. Required parameters: mUrl,
     * returnType.
     *
     * @return a list of parsed objects.
     */
    public List<?> executeGetObjectsList() throws KamaException {
        validateGetArguments();
        List<?> result = (List<?>) get(mUrl, mReturnTypeClass, true, mJsonTitle, mUrlData, mHeaderData, mErrorTypeClass, mErrorTitle);
        cleanup();
        return result;
    }

    /**
     * Execute a post request for a single object. Required parameters: mUrl,
     * returnType, mPostData.
     *
     * @return a parsed object.
     */
    public Object executePost() throws KamaException {
        validatePostArguments();
        Object result = post(mUrl, mReturnTypeClass, false, mJsonTitle, mUrlData, mHeaderData, mPostData, mErrorTypeClass, mErrorTitle);
        cleanup();
        return result;
    }

    /**
     * Execute a post request for a list of objects. Required parameters: mUrl,
     * returnType, mPostData
     *
     * @return a list of parsed objects.
     */
    public List<?> executePostObjectsList() throws KamaException {
        validatePostArguments();
        List<?> result = (List<?>) post(mUrl, mReturnTypeClass, true, mJsonTitle, mUrlData, mHeaderData, mPostData, mErrorTypeClass, mErrorTitle);
        cleanup();
        return result;
    }

    /**
     * Execute a put request for a single object. Required parameters: mUrl,
     * returnType, mPutData.
     *
     * @return a parsed object.
     */
    public Object executePut() throws KamaException {
        validatePutArguments();
        Object result = put(mUrl, mReturnTypeClass, false, mJsonTitle, mUrlData, mHeaderData, mPutData, mErrorTypeClass, mErrorTitle);
        cleanup();
        return result;
    }

    /**
     * Execute a put request for a list of objects. Required parameters: mUrl,
     * returnType, mPutData
     *
     * @return a list of parsed objects.
     */
    public List<?> executePutObjectsList() throws KamaException {
        validatePutArguments();
        List<?> result = (List<?>) put(mUrl, mReturnTypeClass, true, mJsonTitle, mUrlData, mHeaderData, mPutData, mErrorTypeClass, mErrorTitle);
        cleanup();
        return result;
    }

    /**
     * Execute a delete request for a single object. Required parameters: mUrl,
     * returnType, mDeleteData.
     *
     * @return a parsed object.
     */
    public Object executeDelete() throws KamaException {
        validateDeleteArguments();
        Object result = delete(mUrl, mReturnTypeClass, false, mJsonTitle, mUrlData, mHeaderData, mDeleteData, mErrorTypeClass, mErrorTitle);
        cleanup();
        return result;
    }

    /**
     * Execute a delete request for a list of objects. Required parameters: mUrl,
     * returnType, mDeleteData
     *
     * @return a list of parsed objects.
     */
    public List<?> executeDeleteObjectsList() throws KamaException {
        validateDeleteArguments();
        List<?> result = (List<?>) delete(mUrl, mReturnTypeClass, true, mJsonTitle, mUrlData, mHeaderData, mDeleteData, mErrorTypeClass, mErrorTitle);
        cleanup();
        return result;
    }

    private void validateGetArguments() {
        validateArguments();
        if (mReturnTypeClass == null) {
            throw new IllegalArgumentException("Provide a return type class!");
        }
    }

    private void validatePutArguments() {
        validateArguments();
    }

    private void validateDeleteArguments() {
        validateArguments();
    }

    private void validatePostArguments() {
        validateArguments();
    }

    private void validateArguments() {
        if (mUrl == null) {
            throw new IllegalArgumentException("Provide an url!");
        }
    }

    protected void cleanup() {
        mUrl = null;
        mReturnTypeClass = null;
        mJsonTitle = null;
        mUrlData = null;
        mHeaderData = null;
        mPostData = null;
        mPutData = null;
        mDeleteData = null;
        mErrorTypeClass = null;
        mErrorTitle = null;
    }

    /**
     * @param url
     *            request mUrl
     * @param retType
     *            Return class type
     * @param jsonTitle
     *            Title of JsonArray
     * @param urlData
     *            Url data
     * @param headerData
     *            Header data
     * @return returns object of Class T
     * @throws KamaException
     */
    protected <T, V> T get(final String url, final Class<T> retType, final boolean isList, final String jsonTitle, final Map<String, Object> urlData, final Map<String, Object> headerData,
                           final Class<V> errorObject, final String errorTitle)
            throws KamaException {
        String finalUrl = addUrlParams(url, urlData);
        Map<String, Object> finalHeaderData = addNecessaryHeaders(headerData);

        HttpHelper httpHelper = new HttpHelper();
        try {
            HttpResponse httpResponse = httpHelper.get(finalUrl, finalHeaderData);
            return parseObject(url, httpResponse, retType, isList, jsonTitle, errorObject, errorTitle);
        } catch (JsonParseException e) {
            throw new KamaException(e);
        } catch (IOException e) {
            throw new KamaException(e);
        } finally {
            httpHelper.close();
        }
    }

    /**
     * @param url
     *            request mUrl
     * @param retType
     *            Return class type
     * @param jsonTitle
     *            Title of JsonArray
     * @param urlData
     *            Url data
     * @param headerData
     *            Header data
     * @param postData
     *            Post data
     * @return returns object of Class T
     * @throws KamaException
     */
    protected <T, V> T post(final String url, final Class<T> retType, final boolean isList, final String jsonTitle, final Map<String, Object> urlData, final Map<String, Object> headerData,
                            final AbstractHttpEntity postData,
                            final Class<V> errorObject, final String errorTitle) throws KamaException {
        String finalUrl = addUrlParams(url, urlData);
        Map<String, Object> finalHeaderData = addNecessaryHeaders(headerData);

        HttpHelper httpHelper = new HttpHelper();
        try {
            HttpResponse httpResponse = httpHelper.post(finalUrl, finalHeaderData, postData);
            return parseObject(url, httpResponse, retType, isList, jsonTitle, errorObject, errorTitle);
        } catch (JsonParseException e) {
            throw new KamaException(e);
        } catch (IOException e) {
            throw new KamaException(e);
        } finally {
            httpHelper.close();
        }
    }

    /**
     * @param url
     *            request mUrl
     * @param retType
     *            Return class type
     * @param jsonTitle
     *            Title of JsonArray
     * @param urlData
     *            Url data
     * @param headerData
     *            Header data
     * @return true if successfull, otherwise throws exception
     * @throws KamaException
     */
    protected <T, V> T delete(final String url, final Class<T> retType, final boolean isList, final String jsonTitle, final Map<String, Object> urlData, final Map<String, Object> headerData,
                              final AbstractHttpEntity deleteData,
                              final Class<V> errorObject, final String errorTitle) throws KamaException {
        String finalUrl = addUrlParams(url, urlData);
        Map<String, Object> finalHeaderData = addNecessaryHeaders(headerData);

        HttpHelper httpHelper = new HttpHelper();
        try {
            HttpResponse httpResponse = httpHelper.delete(finalUrl, finalHeaderData, deleteData);
            return parseObject(url, httpResponse, retType, isList, jsonTitle, errorObject, errorTitle);
        } catch (JsonParseException e) {
            throw new KamaException(e);
        } catch (IOException e) {
            throw new KamaException(e);
        } finally {
            httpHelper.close();
        }
    }

    /**
     * @param url
     *            request mUrl
     * @param retType
     *            Return class type
     * @param jsonTitle
     *            Title of JsonArray
     * @param urlData
     *            Url data
     * @param headerData
     *            Header data
     * @param putData
     *            Put data
     * @return returns object of Class T
     * @throws KamaException
     */
    public <T, V> T put(final String url, final Class<T> retType, final boolean isList, final String jsonTitle, final Map<String, Object> urlData, final Map<String, Object> headerData,
                        final AbstractHttpEntity putData, final Class<V> errorObject,
                        final String errorTitle) throws KamaException {
        String finalUrl = addUrlParams(url, urlData);
        Map<String, Object> finalHeaderData = addNecessaryHeaders(headerData);

        HttpHelper httpHelper = new HttpHelper();
        try {
            HttpResponse httpResponse = httpHelper.put(finalUrl, finalHeaderData, putData);
            return parseObject(url, httpResponse, retType, isList, jsonTitle, errorObject, errorTitle);
        } catch (JsonParseException e) {
            throw new KamaException(e);
        } catch (IOException e) {
            throw new KamaException(e);
        } finally {
            httpHelper.close();
        }
    }

    protected <T, V> T parseObject(final String url, final HttpResponse httpResponse, final Class<T> retType, final boolean isList, final String objTitle, final Class<V> errorObject,
                                   final String errorTitle) throws KamaException, IOException {
        JsonParser jsonParser = getJsonParserFromResponse(url, httpResponse, errorObject, errorTitle);

        if (retType == null) {
            return null;
        }

        try {
            return getJsonObject(url, jsonParser, retType, isList, objTitle);
        } catch (JsonParseException e) {
            throw new JsonKamaException(e);
        } catch (JsonMappingException e) {
            throw new JsonKamaException(e);
        } catch (IOException e) {
            throw new JsonKamaException(e);
        }
    }

    protected <T> T getJsonObject(final String url, JsonParser jsonParser, final Class<T> retType, final boolean isList, final String objTitle) throws IOException, JsonKamaException {
        T retVal;
        if (isList) {
            if (jsonParser.isExpectedStartArrayToken()) {
                retVal = mObjectMapper.readValue(jsonParser, mObjectMapper.getTypeFactory().constructCollectionType(List.class, retType));
            } else {
                JsonNode response = mObjectMapper.readTree(jsonParser);
                JsonNode responseStr = response.get(objTitle);
                if (responseStr == null) {
                    throw new JsonKamaException("Unexpected jsontitle. Not found: " + objTitle);
                }
                JsonParser jp1 = responseStr.traverse();
                retVal = mObjectMapper.readValue(jp1, mObjectMapper.getTypeFactory().constructCollectionType(List.class, retType));
            }

        } else {
            if (retType != null) {
                if (objTitle != null) {
                    JsonNode response = mObjectMapper.readTree(jsonParser);
                    JsonNode responseStr = response.get(objTitle);
                    jsonParser = responseStr.traverse();
                }
                retVal = mObjectMapper.readValue(jsonParser, retType);
            } else {
                return null;
            }
        }
        return retVal;
    }

    /**
     * Adds Accept = application/json to the headers.
     *
     * @param headerData
     *            can be null
     */
    protected Map<String, Object> addNecessaryHeaders(final Map<String, Object> headerData) {
        Map<String, Object> modifiedHeaderData = headerData;
        if (modifiedHeaderData == null) {
            modifiedHeaderData = new HashMap<String, Object>();
        }
        modifiedHeaderData.put("Accept", "application/json");

        return modifiedHeaderData;
    }

    /**
     * Adds the url params to the url.
     *
     * @param urlData can be null.
     */
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

    protected <V> JsonParser getJsonParserFromResponse(final String url, final HttpResponse response, final Class<V> errorObjectClass, final String errorTitle) throws KamaException,
                                                                                                                                                                       IOException {
        JsonParser result;

        String responseString = HttpUtils.getStringFromResponse(response);
        int statusCode = response.getStatusLine().getStatusCode();
        if (statusCode == HTTP_OK) {
            JsonFactory jsonFactory = new JsonFactory();
            try {
                result = jsonFactory.createJsonParser(responseString);
            } catch (JsonParseException e) {
                throw new JsonKamaException(e);
            } catch (IOException e) {
                throw new JsonKamaException(e);
            }
        } else {
            throw createKamaException(statusCode, responseString, errorObjectClass, errorTitle, url, response);
        }
        return result;
    }

    private <V> KamaException createKamaException(final int statusCode, final String responseString, final Class<V> errorObjectClass, final String errorTitle, final String url, final HttpResponse
            response) {
        KamaException kamaException;

        V errorObj = null;
        if (errorObjectClass != null) {
            try {
                errorObj = parseErrorObject(url, responseString, errorObjectClass, errorTitle);
            } catch (JsonKamaException e) {
                /* We don't care if the error object parsing fails */
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            } catch (JsonParseException e) {
                /* We don't care if the error object parsing fails */
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            } catch (IOException e) {
                /* We don't care if the error object parsing fails */
                //noinspection CallToPrintStackTrace
                e.printStackTrace();
            }
        }

        switch (statusCode) {
            case HTTP_BAD_REQUEST:
                kamaException = new HttpResponseKamaException("Bad Request. " + url + '\n' + responseString, errorObj, statusCode);
                break;
            case HTTP_UNAUTHORIZED:
                kamaException = new NotAuthorizedKamaException("Unauthorized Action. " + url + '\n' + responseString, errorObj);
                break;
            case HTTP_NOT_FOUND:
                kamaException = new HttpResponseKamaException("Not Found. " + url + '\n' + responseString, errorObj, statusCode);
                break;
            case HTTP_INTERNAL_ERROR:
                kamaException = new HttpResponseKamaException("Internal Server Error. " + url + '\n' + responseString, errorObj, statusCode);
                break;
            default:
                kamaException = new HttpResponseKamaException("Unexpected Error: " + response.getStatusLine() + ". " + url + '\n' + responseString, statusCode);
        }

        return kamaException;
    }

    private <V> V parseErrorObject(final String url, final String responseString, final Class<V> errorObject, final String errorTitle) throws JsonParseException, JsonKamaException, IOException {
        JsonFactory jsonFactory = new JsonFactory();
        JsonParser jp = jsonFactory.createJsonParser(responseString);

        return getJsonObject(url, jp, errorObject, false, errorTitle);
    }
}
