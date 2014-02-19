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

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unchecked")
/**
 * Helper class for executing http requests and parsing output. 
 */
public class JsonHelper {

	private String url;
	private Class<?> returnTypeClass;
	private String jsonTitle;
	private Map<String, Object> urlData;
	private Map<String, Object> headerData;
	private Map<String, Object> postData;
	private Map<String, Object> putData;
	private Map<String, Object> deleteData;
	private Class<?> errorTypeClass;
	private String errorTitle;

	public void setUrl(String url) {
		this.url = url;
	}

	public void setReturnTypeClass(Class<?> returnTypeClass) {
		this.returnTypeClass = returnTypeClass;
	}

	public void setJsonTitle(String jsonTitle) {
		this.jsonTitle = jsonTitle;
	}

	public void setUrlData(Map<String, Object> urlData) {
		this.urlData = urlData;
	}

	public void setHeaderData(Map<String, Object> headerData) {
		this.headerData = headerData;
	}

	public void setPostData(Map<String, Object> postData) {
		this.postData = postData;
	}

	public void setPutData(Map<String, Object> putData) {
		this.putData = putData;
	}

	public void setDeleteData(Map<String, Object> deleteData) {
		this.deleteData = deleteData;
	}

	public void setErrorTypeClass(Class<?> errorTypeClass) {
		this.errorTypeClass = errorTypeClass;
	}

	public void setErrorTitle(String errorTitle) {
		this.errorTitle = errorTitle;
	}

	public String getUrl() {
		return url;
	}

	public Class<?> getReturnTypeClass() {
		return returnTypeClass;
	}

	public String getJsonTitle() {
		return jsonTitle;
	}

	public Map<String, Object> getUrlData() {
		return urlData;
	}

	public Map<String, Object> getHeaderData() {
		return headerData;
	}

	public Map<String, Object> getPostData() {
		return postData;
	}

	public Map<String, Object> getPutData() {
		return putData;
	}

	public Map<String, Object> getDeleteData() {
		return deleteData;
	}

	public Class<?> getErrorTypeClass() {
		return errorTypeClass;
	}

	public String getErrorTitle() {
		return errorTitle;
	}

	public ObjectMapper getMapper() {
		return mapper;
	}

	/**
	 * Execute a get request for a single object. Required parameters: url,
	 * returnType.
	 * 
	 * @return the parsed object.
	 */
	public Object executeGetObject() throws KamaException {
		validateGetArguments();
		Object result = get(url, returnTypeClass, false, jsonTitle, urlData, headerData, errorTypeClass, errorTitle);
		cleanup();
		return result;
	}

	/**
	 * Execute a get request for a list of objects. Required parameters: url,
	 * returnType.
	 * 
	 * @return a list of parsed objects.
	 */
	public List<? extends Object> executeGetObjectsList() throws KamaException {
		validateGetArguments();
		List<? extends Object> result = (List<? extends Object>) get(url, returnTypeClass, true, jsonTitle, urlData, headerData, errorTypeClass, errorTitle);
		cleanup();
		return result;
	}

	/**
	 * Execute a post request for a single object. Required parameters: url,
	 * returnType, postData.
	 * 
	 * @return a parsed object.
	 */
	public Object executePost() throws KamaException {
		validatePostArguments();
		Object result = post(url, returnTypeClass, false, jsonTitle, urlData, headerData, postData, errorTypeClass, errorTitle);
		cleanup();
		return result;
	}

	/**
	 * Execute a post request for a list of objects. Required parameters: url,
	 * returnType, postData
	 * 
	 * @return a list of parsed objects.
	 */
	public List<? extends Object> executePostObjectsList() throws KamaException {
		validatePostArguments();
		List<? extends Object> result = (List<? extends Object>) post(url, returnTypeClass, true, jsonTitle, urlData, headerData, postData, errorTypeClass, errorTitle);
		cleanup();
		return result;
	}

	/**
	 * Execute a put request for a single object. Required parameters: url,
	 * returnType, putData.
	 * 
	 * @return a parsed object.
	 */
	public Object executePut() throws KamaException {
		validatePutArguments();
		Object result = put(url, returnTypeClass, false, jsonTitle, urlData, headerData, putData, errorTypeClass, errorTitle);
		cleanup();
		return result;
	}

	/**
	 * Execute a put request for a list of objects. Required parameters: url,
	 * returnType, putData
	 * 
	 * @return a list of parsed objects.
	 */
	public List<? extends Object> executePutObjectsList() throws KamaException {
		validatePutArguments();
		List<? extends Object> result = (List<? extends Object>) put(url, returnTypeClass, true, jsonTitle, urlData, headerData, putData, errorTypeClass, errorTitle);
		cleanup();
		return result;
	}

	/**
	 * Execute a delete request for a single object. Required parameters: url,
	 * returnType, deleteData.
	 * 
	 * @return a parsed object.
	 */
	public Object executeDelete() throws KamaException {
		validateDeleteArguments();
		Object result = delete(url, returnTypeClass, false, jsonTitle, urlData, headerData, deleteData, errorTypeClass, errorTitle);
		cleanup();
		return result;
	}

	/**
	 * Execute a delete request for a list of objects. Required parameters: url,
	 * returnType, deleteData
	 * 
	 * @return a list of parsed objects.
	 */
	public List<? extends Object> executeDeleteObjectsList() throws KamaException {
		validateDeleteArguments();
		List<? extends Object> result = (List<? extends Object>) delete(url, returnTypeClass, true, jsonTitle, urlData, headerData, deleteData, errorTypeClass, errorTitle);
		cleanup();
		return result;
	}

    private void validateGetArguments(){
        validateArguments();
        if(returnTypeClass == null){
            throw new IllegalArgumentException("Provide a return type class!");
        }
    }

	private void validatePutArguments() {
		validateArguments();
		if (putData == null) {
			throw new IllegalArgumentException("Provide put data!");
		}
	}

	private void validateDeleteArguments() {
		validateArguments();
		if (deleteData == null) {
			throw new IllegalArgumentException("Provide delete data!");
		}
	}

	private void validatePostArguments() {
		validateArguments();
		if (postData == null) {
			throw new IllegalArgumentException("Provide post data!");
		}
	}

	private void validateArguments() {
		if (url == null) {
			throw new IllegalArgumentException("Provide an url!");
		}

		if (returnTypeClass == null) {
			throw new IllegalArgumentException("Provide a return type!");
		}
	}

	protected void cleanup() {
		url = null;
		returnTypeClass = null;
		jsonTitle = null;
		urlData = null;
		headerData = null;
		postData = null;
		putData = null;
		deleteData = null;
		errorTypeClass = null;
		errorTitle = null;
	}

	protected ObjectMapper mapper = new ObjectMapper();

	/**
	 * @param url
	 *            request url
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
	protected <T, V> T get(String url, Class<T> retType, boolean isList, String jsonTitle, Map<String, Object> urlData, Map<String, Object> headerData, Class<V> errorObject, String errorTitle)
			throws KamaException {
		String finalUrl = addUrlParams(url, urlData);
		Map<String, Object> finalHeaderData = addNecessaryHeaders(headerData);

		HttpHelper httpHelper = new HttpHelper();
		try {
			try {
				HttpResponse httpResponse = httpHelper.get(finalUrl, finalHeaderData);
				return parseObject(url, httpResponse, retType, isList, jsonTitle, errorObject, errorTitle);
			} catch (IOException e) {
				throw new KamaException(e);
			}
		} finally {
			httpHelper.close();
		}
	}

	/**
	 * @param url
	 *            request url
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
	protected <T, V> T post(String url, Class<T> retType, boolean isList, String jsonTitle, Map<String, Object> urlData, Map<String, Object> headerData, Map<String, Object> postData,
			Class<V> errorObject, String errorTitle) throws KamaException {
		String finalUrl = addUrlParams(url, urlData);
		Map<String, Object> finalHeaderData = addNecessaryHeaders(headerData);

		HttpHelper httpHelper = new HttpHelper();
		try {
			try {
				HttpResponse httpResponse = httpHelper.post(finalUrl, finalHeaderData, postData);
				return parseObject(url, httpResponse, retType, isList, jsonTitle, errorObject, errorTitle);
			} catch (IOException e) {
				throw new KamaException(e);
			}
		} finally {
			httpHelper.close();
		}
	}

	/**
	 * @param url
	 *            request url
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
	protected <T, V> T delete(String url, Class<T> retType, boolean isList, String jsonTitle, Map<String, Object> urlData, Map<String, Object> headerData, Map<String, Object> deleteData,
			Class<V> errorObject, String errorTitle) throws KamaException {
		String finalUrl = addUrlParams(url, urlData);
		Map<String, Object> finalHeaderData = addNecessaryHeaders(headerData);

		HttpHelper httpHelper = new HttpHelper();
		try {
			try {
				HttpResponse httpResponse = httpHelper.delete(finalUrl, finalHeaderData, deleteData);
				return parseObject(url, httpResponse, retType, isList, jsonTitle, errorObject, errorTitle);
			} catch (IOException e) {
				throw new KamaException(e);
			}
		} finally {
			httpHelper.close();
		}
	}

	/**
	 * @param url
	 *            request url
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
	public <T, V> T put(String url, Class<T> retType, boolean isList, String jsonTitle, Map<String, Object> urlData, Map<String, Object> headerData, Map<String, Object> putData, Class<V> errorObject,
			String errorTitle) throws KamaException {
		String finalUrl = addUrlParams(url, urlData);
		Map<String, Object> finalHeaderData = addNecessaryHeaders(headerData);

		HttpHelper httpHelper = new HttpHelper();
		try {
			try {
				HttpResponse httpResponse = httpHelper.put(finalUrl, finalHeaderData, putData);
				return parseObject(url, httpResponse, retType, isList, jsonTitle, errorObject, errorTitle);
			} catch (IOException e) {
				throw new KamaException(e);
			}
		} finally {
			httpHelper.close();
		}
	}

	protected <T, V> T parseObject(String url, HttpResponse httpResponse, Class<T> retType, boolean isList, String objTitle, Class<V> errorObject, String errorTitle) throws JsonKamaException,
			NotAuthorizedKamaException, HttpResponseKamaException, JsonParseException, IOException {
		JsonParser jsonParser = getJsonParserFromResponse(url, httpResponse, errorObject, errorTitle);
		T retVal = null;

		try {
			retVal = getJsonObject(url, jsonParser, retType, isList, objTitle);
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

	protected <T> T getJsonObject(String url, JsonParser jsonParser, Class<T> retType, boolean isList, String objTitle) throws JsonParseException, JsonMappingException, IOException, JsonKamaException {
		T retVal = null;
		if (isList) {
			if (jsonParser.isExpectedStartArrayToken())
				retVal = mapper.readValue(jsonParser, mapper.getTypeFactory().constructCollectionType(List.class, retType));
			else {
				JsonNode response = mapper.readTree(jsonParser);
				JsonNode responseStr = response.get(objTitle);
				if (responseStr == null) {
					Buggy.report(new Exception("Unexpected jsontitle. Not found: " + objTitle), url);
					throw new JsonKamaException("Unexpected jsontitle. Not found: " + objTitle);
				}
				JsonParser jp1 = responseStr.traverse();
				retVal = mapper.readValue(jp1, mapper.getTypeFactory().constructCollectionType(List.class, retType));
			}

		} else {
			if (retType != null) {
				if (objTitle != null) {
					JsonNode response = mapper.readTree(jsonParser);
					JsonNode responseStr = response.get(objTitle);
					jsonParser = responseStr.traverse();
				}
				retVal = mapper.readValue(jsonParser, retType);
			} else
				return null;
		}
		return retVal;
	}

	/**
	 * Adds Accept = application/json to the headers.
	 * 
	 * @param headerData
	 *            can be null
	 */
	protected Map<String, Object> addNecessaryHeaders(Map<String, Object> headerData) {
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
	 * @param urlData
	 *            can be null.
	 */
	protected String addUrlParams(String url, Map<String, Object> urlData) {
		String finalUrl = url;

		if (urlData != null && !urlData.isEmpty()) {
			finalUrl += KamaParam.URLPARAM;

			for (Iterator<String> iterator = urlData.keySet().iterator(); iterator.hasNext();) {
				String key = iterator.next();
				finalUrl += key + "=" + replaceInvalidChars(urlData.get(key).toString());

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

	protected <V> JsonParser getJsonParserFromResponse(String url, HttpResponse response, Class<V> errorObject, String errorTitle) throws JsonKamaException, NotAuthorizedKamaException,
			HttpResponseKamaException, JsonParseException, IOException {
		String responseString = HttpUtils.getStringFromResponse(response);
		V errorObj = null;

		int statusCode = response.getStatusLine().getStatusCode();
		switch (statusCode) {
		case 200:
			JsonFactory jsonFactory = new JsonFactory();
			JsonParser jp;
			try {
				jp = jsonFactory.createJsonParser(responseString);
			} catch (JsonParseException e) {
				Buggy.report(e, url + "\n\n" + responseString);
				throw new JsonKamaException(e);
			} catch (IOException e) {
				throw new JsonKamaException(e);
			}
			return jp;
		case 400:
			if (errorObject != null) {
				errorObj = getErrorObject(url, responseString, errorObject, errorTitle);
			}

			throw new HttpResponseKamaException("Bad Request. " + url + "\n" + responseString, errorObj, statusCode);
		case 401:
			if (errorObject != null) {
				errorObj = getErrorObject(url, responseString, errorObject, errorTitle);
			}

			throw new NotAuthorizedKamaException("Unauthorized Action. " + url + "\n" + responseString, errorObj);
		case 404:
			if (errorObject != null) {
				errorObj = getErrorObject(url, responseString, errorObject, errorTitle);
			}

			throw new HttpResponseKamaException("Not Found. " + url + "\n" + responseString, errorObj, statusCode);
		case 500:
			if (errorObject != null) {
				errorObj = getErrorObject(url, responseString, errorObject, errorTitle);
			}

			Buggy.report(new HttpResponseKamaException(response.getStatusLine().getStatusCode() + "\n\n" + responseString, statusCode), url);
			throw new HttpResponseKamaException("Internal Server Error. " + url + "\n" + responseString, errorObj, statusCode);
		default:
			Buggy.report(new HttpResponseKamaException(response.getStatusLine().getStatusCode() + "\n\n" + responseString, statusCode), url);
			throw new HttpResponseKamaException("Unexpected Error: " + response.getStatusLine() + ". " + url + "\n" + responseString, statusCode);
		}
	}

	private <V> V getErrorObject(String url, String responseString, Class<V> errorObject, String errorTitle) throws JsonParseException, IOException, JsonKamaException {
		JsonFactory jsonFactory = new JsonFactory();
		JsonParser jp = jsonFactory.createJsonParser(responseString);

		return getJsonObject(url, jp, errorObject, false, errorTitle);
	}
}
