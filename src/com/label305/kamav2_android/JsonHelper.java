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
import com.label305.stan.asyncutils.Buggy;
import com.label305.stan.utils.HttpHelper;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

import java.io.IOException;
import java.util.*;

public class JsonHelper {

    protected ObjectMapper mapper = new ObjectMapper();

    public <T, U> T get(String url, Class<T> retType, String objTitle) throws KamaException {
        return get(url, retType, null, objTitle, null, null);
    }

    public <T, U> T get(String url, Class<T> retType, Class<U> listType, String listTitle) throws KamaException {
        return get(url, retType, listType, listTitle, null, null);
    }

    public <T, U> T get(String url, Class<T> retType, String objTitle, List<NameValuePair> urlData) throws KamaException {
        return get(url, retType, null, objTitle, urlData, null);
    }

    public <T, U> T get(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData) throws KamaException {
        return get(url, retType, listType, listTitle, urlData, null);
    }

    public <T, U> T get(String url, Class<T> retType, String objTitle, Map<String, String> headerData) throws KamaException {
        return get(url, retType, null, objTitle, null, headerData);
    }

    public <T, U> T get(String url, Class<T> retType, Class<U> listType, String listTitle, Map<String, String> headerData) throws KamaException {
        return get(url, retType, listType, listTitle, null, headerData);
    }

    /**
     * @param url        request url
     * @param retType    Return class type
     * @param listType   Element class of list items
     * @param listTitle  Title of JsonArray
     * @param urlData    Url data
     * @param headerData Header data
     * @return returns object of Class T
     * @throws KamaException
     */
    public <T, U> T get(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData, Map<String, String> headerData)
            throws KamaException {
        String finalUrl = addUrlParams(url, urlData);
        Map<String, String> finalHeaderData = addNecessaryHeaders(headerData);

        HttpHelper httpHelper = new HttpHelper();
        try {
            try {
                HttpResponse httpResponse = httpHelper.get(finalUrl, finalHeaderData);
                return parseObject(url, httpResponse, retType, listType, listTitle);
            } catch (IOException e) {
                throw new KamaException(e);
            }
        } finally {
            httpHelper.close();
        }
    }

    public <T, U> T post(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> postData) throws KamaException {
        return post(url, retType, listType, listTitle, null, null, postData);
    }

    public <T, U> T post(String url, Class<T> retType, String objTitle, List<NameValuePair> postData) throws KamaException {
        return post(url, retType, null, objTitle, null, null, postData);
    }

    public <T, U> T post(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData, List<NameValuePair> postData)
            throws KamaException {
        return post(url, retType, listType, listTitle, urlData, null, postData);
    }

    public <T, U> T post(String url, Class<T> retType, String objTitle, List<NameValuePair> urlData, List<NameValuePair> postData)
            throws KamaException {
        return post(url, retType, null, objTitle, urlData, null, postData);
    }

    public <T, U> T post(String url, Class<T> retType, String objTitle, Map<String, String> headerData, List<NameValuePair> postData)
            throws KamaException {
        return post(url, retType, null, objTitle, null, headerData, postData);
    }

    public <T, U> T post(String url, Class<T> retType, Class<U> listType, String listTitle, Map<String, String> headerData,
                         List<NameValuePair> postData) throws KamaException {
        return post(url, retType, listType, listTitle, null, headerData, postData);
    }

    /**
     * @param url        request url
     * @param retType    Return class type
     * @param listType   Element class of list items
     * @param listTitle  Title of JsonArray
     * @param urlData    Url data
     * @param headerData Header data
     * @param postData   Post data
     * @return returns object of Class T
     * @throws KamaException
     */
    public <T, U> T post(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData,
                         Map<String, String> headerData, List<NameValuePair> postData) throws KamaException {
        String finalUrl = addUrlParams(url, urlData);
        Map<String, String> finalHeaderData = addNecessaryHeaders(headerData);

        HttpHelper httpHelper = new HttpHelper();
        try {
            try {
                HttpResponse httpResponse = httpHelper.post(finalUrl, finalHeaderData, postData);
                return parseObject(url, httpResponse, retType, listType, listTitle);
            } catch (IOException e) {
                throw new KamaException(e);
            }
        } finally {
            httpHelper.close();
        }
    }

    public <T, U> T put(String url, Class<T> retType, String objTitle, List<NameValuePair> postData) throws KamaException {
        return put(url, retType, null, objTitle, null, null, postData);
    }

    public <T, U> T put(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> postData) throws KamaException {
        return put(url, retType, listType, listTitle, null, null, postData);
    }

    public <T, U> T put(String url, Class<T> retType, String objTitle, List<NameValuePair> urlData, List<NameValuePair> postData)
            throws KamaException {
        return put(url, retType, null, objTitle, urlData, null, postData);
    }

    public <T, U> T put(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData, List<NameValuePair> postData)
            throws KamaException {
        return put(url, retType, listType, listTitle, urlData, null, postData);
    }

    public <T, U> T put(String url, Class<T> retType, String objTitle, Map<String, String> headerData, List<NameValuePair> postData)
            throws KamaException {
        return put(url, retType, null, objTitle, null, headerData, postData);
    }

    public <T, U> T put(String url, Class<T> retType, Class<U> listType, String listTitle, Map<String, String> headerData,
                        List<NameValuePair> postData) throws KamaException {
        return put(url, retType, listType, listTitle, null, headerData, postData);
    }

    /**
     * @param url        request url
     * @param retType    Return class type
     * @param listType   Element class of list items
     * @param listTitle  Title of JsonArray
     * @param urlData    Url data
     * @param headerData Header data
     * @param putData    Put data
     * @return returns object of Class T
     * @throws KamaException
     */
    public <T, U> T put(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData,
                        Map<String, String> headerData, List<NameValuePair> putData) throws KamaException {
        String finalUrl = addUrlParams(url, urlData);
        Map<String, String> finalHeaderData = addNecessaryHeaders(headerData);

        HttpHelper httpHelper = new HttpHelper();
        try {
            try {
                HttpResponse httpResponse = httpHelper.put(finalUrl, finalHeaderData, putData);
                return parseObject(url, httpResponse, retType, listType, listTitle);
            } catch (IOException e) {
                throw new KamaException(e);
            }
        } finally {
            httpHelper.close();
        }
    }

    protected <T, U> T parseObject(String url, HttpResponse httpResponse, Class<T> retType, Class<U> listType, String objTitle) throws JsonKamaException,
            NotAuthorizedKamaException, HttpResponseKamaException {
        JsonParser jsonParser = getJsonParserFromResponse(url, httpResponse);
        T retVal = null;

        try {
            if (retType == ArrayList.class || retType == List.class) {

                if (jsonParser.isExpectedStartArrayToken())
                    retVal = mapper.readValue(jsonParser, mapper.getTypeFactory().constructCollectionType(List.class, listType));
                else {
                    JsonNode response = mapper.readTree(jsonParser);
                    JsonNode responseStr = response.get(objTitle);
                    if (responseStr == null) {
                        Buggy.report(new Exception("Unexpected jsontitle. Not found: " + objTitle), url);
                        throw new JsonKamaException("Unexpected jsontitle. Not found: " + objTitle);
                    }
                    JsonParser jp1 = responseStr.traverse();
                    retVal = mapper.readValue(jp1, mapper.getTypeFactory().constructCollectionType(List.class, listType));
                }

            } else {
                if (objTitle != null) {
                    JsonNode response = mapper.readTree(jsonParser);
                    JsonNode responseStr = response.get(objTitle);
                    jsonParser = responseStr.traverse();
                }
                retVal = mapper.readValue(jsonParser, retType);
            }
        } catch (JsonParseException e) {
            Buggy.report(e, url);
            throw new JsonKamaException(e);
        } catch (JsonMappingException e) {
            Buggy.report(e, url);
            throw new JsonKamaException(e);
        } catch (IOException e) {
            throw new JsonKamaException(e);
        }

        return retVal;
    }

    /**
     * Adds Accept = application/json to the headers.
     *
     * @param headerData can be null
     */
    protected Map<String, String> addNecessaryHeaders(Map<String, String> headerData) {
        Map<String, String> modifiedHeaderData = headerData;
        if (modifiedHeaderData == null) {
            modifiedHeaderData = new HashMap<String, String>();
        }
        modifiedHeaderData.put("Accept", "application/json");

        return modifiedHeaderData;
    }

    /**
     * Adds the url params to the url.
     *
     * @param urlData can be null.
     */
    protected String addUrlParams(String url, List<NameValuePair> urlData) {
        String finalUrl = url;

        if (urlData != null && !urlData.isEmpty()) {
            finalUrl += KamaParam.URLPARAM;

            Iterator<NameValuePair> iterator = urlData.iterator();
            while (iterator.hasNext()) {
                NameValuePair data = iterator.next();
                finalUrl += data.getName() + "=" + replaceInvalidChars(data.getValue());

                if (iterator.hasNext()) {
                    finalUrl += KamaParam.URLPARAMCONCAT;
                }
            }
        }

        return finalUrl;
    }

    private String replaceInvalidChars(String value) {
        return value.replace(" ", "%20");
    }

    protected JsonParser getJsonParserFromResponse(String url, HttpResponse response) throws JsonKamaException, NotAuthorizedKamaException,
            HttpResponseKamaException {

        switch (response.getStatusLine().getStatusCode()) {
            case 200:
                JsonFactory jsonFactory = new JsonFactory();
                JsonParser jp;
                try {
                    jp = jsonFactory.createJsonParser(HttpUtils.getStringFromResponse(response));
                } catch (JsonParseException e) {
                    Buggy.report(e, url);
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
                throw new HttpResponseKamaException("Unexpected Error. Statuscode: " + response.getStatusLine().getStatusCode());
        }
    }
}
