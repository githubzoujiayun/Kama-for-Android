package com.label305.kamav2_android;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;

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

public class JsonHelper {

	protected ObjectMapper mapper = new ObjectMapper();

	public <T, U> T get(String url, Class<T> retType, String objTitle) throws KamaException {
		return get(url, retType, null, objTitle, null, null, null, null);
	}

	public <T, U> T get(String url, Class<T> retType, Class<U> listType, String listTitle) throws KamaException {
		return get(url, retType, listType, listTitle, null, null, null, null);
	}

	public <T, U> T get(String url, Class<T> retType, String objTitle, List<NameValuePair> urlData) throws KamaException {
		return get(url, retType, null, objTitle, urlData, null, null, null);
	}

	public <T, U> T get(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData) throws KamaException {
		return get(url, retType, listType, listTitle, urlData, null, null, null);
	}

	public <T, U> T get(String url, Class<T> retType, String objTitle, Map<String, String> headerData) throws KamaException {
		return get(url, retType, null, objTitle, null, headerData, null, null);
	}

	public <T, U> T get(String url, Class<T> retType, Class<U> listType, String listTitle, Map<String, String> headerData) throws KamaException {
		return get(url, retType, listType, listTitle, null, headerData, null, null);
	}
	
	/*
	 * error support
	 */
	
	public <T, U, V> T get(String url, Class<T> retType, String objTitle, Class<V> errorObject, String errorTitle) throws KamaException {
		return get(url, retType, null, objTitle, null, null, errorObject, errorTitle);
	}
	
	public <T, U, V> T get(String url, Class<T> retType, Class<U> listType, String listTitle, Class<V> errorObject, String errorTitle) throws KamaException {
		return get(url, retType, listType, listTitle, null, null, errorObject, errorTitle);
	}
	
	public <T, U, V> T get(String url, Class<T> retType, String objTitle, List<NameValuePair> urlData, Class<V> errorObject, String errorTitle) throws KamaException {
		return get(url, retType, null, objTitle, urlData, null, errorObject, errorTitle);
	}
	
	public <T, U, V> T get(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData, Class<V> errorObject, String errorTitle) throws KamaException {
		return get(url, retType, listType, listTitle, urlData, null, errorObject, errorTitle);
	}
	
	public <T, U, V> T get(String url, Class<T> retType, String objTitle, Map<String, String> headerData, Class<V> errorObject, String errorTitle) throws KamaException {
		return get(url, retType, null, objTitle, null, headerData, errorObject, errorTitle);
	}
	
	public <T, U, V> T get(String url, Class<T> retType, Class<U> listType, String listTitle, Map<String, String> headerData, Class<V> errorObject, String errorTitle) throws KamaException {
		return get(url, retType, listType, listTitle, null, headerData, errorObject, errorTitle);
	}

	/**
	 * @param url
	 *            request url
	 * @param retType
	 *            Return class type
	 * @param listType
	 *            Element class of list items
	 * @param listTitle
	 *            Title of JsonArray
	 * @param urlData
	 *            Url data
	 * @param headerData
	 *            Header data
	 * @return returns object of Class T
	 * @throws KamaException
	 */
	public <T, U, V> T get(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData, Map<String, String> headerData, Class<V> errorObject, String errorTitle) throws KamaException {
		String finalUrl = addUrlParams(url, urlData);
		Map<String, String> finalHeaderData = addNecessaryHeaders(headerData);

		HttpHelper httpHelper = new HttpHelper();
		try {
			try {
				HttpResponse httpResponse = httpHelper.get(finalUrl, finalHeaderData);
				return parseObject(url, httpResponse, retType, listType, listTitle, errorObject, errorTitle);
			} catch (IOException e) {
				throw new KamaException(e);
			}
		} finally {
			httpHelper.close();
		}
	}

	public <T, U> T post(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> postData) throws KamaException {
		return post(url, retType, listType, listTitle, null, null, postData, null, null);
	}

	public <T, U> T post(String url, Class<T> retType, String objTitle, List<NameValuePair> postData) throws KamaException {
		return post(url, retType, null, objTitle, null, null, postData, null, null);
	}

	public <T, U> T post(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData, List<NameValuePair> postData) throws KamaException {
		return post(url, retType, listType, listTitle, urlData, null, postData, null, null);
	}

	public <T, U> T post(String url, Class<T> retType, String objTitle, List<NameValuePair> urlData, List<NameValuePair> postData) throws KamaException {
		return post(url, retType, null, objTitle, urlData, null, postData, null, null);
	}

	public <T, U> T post(String url, Class<T> retType, String objTitle, Map<String, String> headerData, List<NameValuePair> postData) throws KamaException {
		return post(url, retType, null, objTitle, null, headerData, postData, null, null);
	}

	public <T, U> T post(String url, Class<T> retType, Class<U> listType, String listTitle, Map<String, String> headerData, List<NameValuePair> postData) throws KamaException {
		return post(url, retType, listType, listTitle, null, headerData, postData, null, null);
	}
	
	/*
	 * With error handling
	 * 
	 */
	public <T, U, V> T post(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> postData, Class<V> errorObject, String errorTitle) throws KamaException {
		return post(url, retType, listType, listTitle, null, null, postData, errorObject, errorTitle);
	}
	
	public <T, U, V> T post(String url, Class<T> retType, String objTitle, List<NameValuePair> postData, Class<V> errorObject, String errorTitle) throws KamaException {
		return post(url, retType, null, objTitle, null, null, postData, errorObject, errorTitle);
	}
	
	public <T, U, V> T post(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData, List<NameValuePair> postData, Class<V> errorObject, String errorTitle) throws KamaException {
		return post(url, retType, listType, listTitle, urlData, null, postData, errorObject, errorTitle);
	}
	
	public <T, U, V> T post(String url, Class<T> retType, String objTitle, List<NameValuePair> urlData, List<NameValuePair> postData, Class<V> errorObject, String errorTitle) throws KamaException {
		return post(url, retType, null, objTitle, urlData, null, postData, errorObject, errorTitle);
	}
	
	public <T, U, V> T post(String url, Class<T> retType, String objTitle, Map<String, String> headerData, List<NameValuePair> postData, Class<V> errorObject, String errorTitle) throws KamaException {
		return post(url, retType, null, objTitle, null, headerData, postData, errorObject, errorTitle);
	}
	
	public <T, U, V> T post(String url, Class<T> retType, Class<U> listType, String listTitle, Map<String, String> headerData, List<NameValuePair> postData, Class<V> errorObject, String errorTitle) throws KamaException {
		return post(url, retType, listType, listTitle, null, headerData, postData, errorObject, errorTitle);
	}

	/**
	 * @param url
	 *            request url
	 * @param retType
	 *            Return class type
	 * @param listType
	 *            Element class of list items
	 * @param listTitle
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
	public <T, U, V> T post(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData, Map<String, String> headerData, List<NameValuePair> postData, Class<V> errorObject, String errorTitle) throws KamaException {
		String finalUrl = addUrlParams(url, urlData);
		Map<String, String> finalHeaderData = addNecessaryHeaders(headerData);

		HttpHelper httpHelper = new HttpHelper();
		try {
			try {
				HttpResponse httpResponse = httpHelper.post(finalUrl, finalHeaderData, postData);
				return parseObject(url, httpResponse, retType, listType, listTitle, errorObject, errorTitle);
			} catch (IOException e) {
				throw new KamaException(e);
			}
		} finally {
			httpHelper.close();
		}
	}

	public <T, U> T put(String url, Class<T> retType, String objTitle, List<NameValuePair> postData) throws KamaException {
		return put(url, retType, null, objTitle, null, null, postData, null, null);
	}

	public <T, U> T put(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> postData) throws KamaException {
		return put(url, retType, listType, listTitle, null, null, postData, null, null);
	}

	public <T, U> T put(String url, Class<T> retType, String objTitle, List<NameValuePair> urlData, List<NameValuePair> postData) throws KamaException {
		return put(url, retType, null, objTitle, urlData, null, postData, null, null);
	}

	public <T, U> T put(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData, List<NameValuePair> postData) throws KamaException {
		return put(url, retType, listType, listTitle, urlData, null, postData, null, null);
	}

	public <T, U> T put(String url, Class<T> retType, String objTitle, Map<String, String> headerData, List<NameValuePair> postData) throws KamaException {
		return put(url, retType, null, objTitle, null, headerData, postData, null, null);
	}

	public <T, U> T put(String url, Class<T> retType, Class<U> listType, String listTitle, Map<String, String> headerData, List<NameValuePair> postData) throws KamaException {
		return put(url, retType, listType, listTitle, null, headerData, postData, null, null);
	}
	
	/*
	 * Error object below
	 */
	public <T, U, V> T put(String url, Class<T> retType, String objTitle, List<NameValuePair> postData, Class<V> errorObject, String errorTitle) throws KamaException {
		return put(url, retType, null, objTitle, null, null, postData, errorObject, errorTitle);
	}
	
	public <T, U, V> T put(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> postData, Class<V> errorObject, String errorTitle) throws KamaException {
		return put(url, retType, listType, listTitle, null, null, postData, errorObject, errorTitle);
	}
	
	public <T, U, V> T put(String url, Class<T> retType, String objTitle, List<NameValuePair> urlData, List<NameValuePair> postData, Class<V> errorObject, String errorTitle) throws KamaException {
		return put(url, retType, null, objTitle, urlData, null, postData, errorObject, errorTitle);
	}
	
	public <T, U, V> T put(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData, List<NameValuePair> postData, Class<V> errorObject, String errorTitle) throws KamaException {
		return put(url, retType, listType, listTitle, urlData, null, postData, errorObject, errorTitle);
	}
	
	public <T, U, V> T put(String url, Class<T> retType, String objTitle, Map<String, String> headerData, List<NameValuePair> postData, Class<V> errorObject, String errorTitle) throws KamaException {
		return put(url, retType, null, objTitle, null, headerData, postData, errorObject, errorTitle);
	}
	
	public <T, U, V> T put(String url, Class<T> retType, Class<U> listType, String listTitle, Map<String, String> headerData, List<NameValuePair> postData, Class<V> errorObject, String errorTitle) throws KamaException {
		return put(url, retType, listType, listTitle, null, headerData, postData, errorObject, errorTitle);
	}

	/**
	 * @param url
	 *            request url
	 * @param retType
	 *            Return class type
	 * @param listType
	 *            Element class of list items
	 * @param listTitle
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
	public <T, U, V> T put(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData, Map<String, String> headerData, List<NameValuePair> putData, Class<V> errorObject, String errorTitle) throws KamaException {
		String finalUrl = addUrlParams(url, urlData);
		Map<String, String> finalHeaderData = addNecessaryHeaders(headerData);

		HttpHelper httpHelper = new HttpHelper();
		try {
			try {
				HttpResponse httpResponse = httpHelper.put(finalUrl, finalHeaderData, putData);
				return parseObject(url, httpResponse, retType, listType, listTitle, errorObject, errorTitle);
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
	 * @param listType
	 *            Element class of list items
	 * @param listTitle
	 *            Title of JsonArray
	 * @return true if successfull, otherwise throws exception
	 * @throws KamaException
	 */
	public <T, U> T delete(String url, Class<T> retType, Class<U> listType, String listTitle) throws KamaException {
		return delete(url, retType, listType, listTitle, null, null, null, null);
	}
	
	/**
	 * @param url
	 *            request url
	 * @param retType
	 *            Return class type
	 * @param listType
	 *            Element class of list items
	 * @param listTitle
	 *            Title of JsonArray
	 * @param headerData
	 *            Header data
	 * @return true if successfull, otherwise throws exception
	 * @throws KamaException
	 */
	public <T, U> T delete(String url, Class<T> retType, Class<U> listType, String listTitle, Map<String, String> headerData) throws KamaException {
		return delete(url, retType, listType, listTitle, null, headerData, null, null);
	}
	
	/**
	 * @param url
	 *            request url
	 * @param retType
	 *            Return class type
	 * @param listType
	 *            Element class of list items
	 * @param listTitle
	 *            Title of JsonArray
	 * @param urlData
	 *            Url data
	 * @return true if successfull, otherwise throws exception
	 * @throws KamaException
	 */
	public <T, U> T delete(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData) throws KamaException {
		return delete(url, retType, listType, listTitle, urlData, null, null, null);
	}
	
	
	/*
	 * ErrorObjects below
	 */
	public <T, U, V> T delete(String url, Class<T> retType, Class<U> listType, String listTitle, Class<V> errorObject, String errorTitle) throws KamaException {
		return delete(url, retType, listType, listTitle, null, null, errorObject, errorTitle);
	}
	
	public <T, U, V> T delete(String url, Class<T> retType, Class<U> listType, String listTitle, Map<String, String> headerData, Class<V> errorObject, String errorTitle) throws KamaException {
		return delete(url, retType, listType, listTitle, null, headerData, errorObject, errorTitle);
	}
	
	public <T, U, V> T delete(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData, Class<V> errorObject, String errorTitle) throws KamaException {
		return delete(url, retType, listType, listTitle, urlData, null, errorObject, errorTitle);
	}
	
	/**
	 * @param url
	 *            request url
	 * @param retType
	 *            Return class type
	 * @param listType
	 *            Element class of list items
	 * @param listTitle
	 *            Title of JsonArray
	 * @param urlData
	 *            Url data
	 * @param headerData
	 *            Header data
	 * @return true if successfull, otherwise throws exception
	 * @throws KamaException
	 */
	public <T, U, V> T delete(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData, Map<String, String> headerData, Class<V> errorObject, String errorTitle) throws KamaException {
		String finalUrl = addUrlParams(url, urlData);
		Map<String, String> finalHeaderData = addNecessaryHeaders(headerData);

		HttpHelper httpHelper = new HttpHelper();
		try {
			try {
				HttpResponse httpResponse = httpHelper.delete(finalUrl, finalHeaderData);
				return parseObject(url, httpResponse, retType, listType, listTitle, errorObject, errorTitle);
			} catch (IOException e) {
				throw new KamaException(e);
			}
		} finally {
			httpHelper.close();
		}
	}
	
	public <T, U, V> T delete(String url, Class<T> retType, String objTitle, Map<String, String> headerData, List<NameValuePair> deleteData, Class<V> errorObject, String errorTitle) throws KamaException {
		return delete(url, retType, null, objTitle, null, headerData, deleteData, errorObject, errorTitle);
	}
	
	/**
	 * @param url
	 *            request url
	 * @param retType
	 *            Return class type
	 * @param listType
	 *            Element class of list items
	 * @param listTitle
	 *            Title of JsonArray
	 * @param urlData
	 *            Url data
	 * @param headerData
	 *            Header data
	 * @return true if successfull, otherwise throws exception
	 * @throws KamaException
	 */
	public <T, U, V> T delete(String url, Class<T> retType, Class<U> listType, String listTitle, List<NameValuePair> urlData, Map<String, String> headerData, List<NameValuePair> deleteData, Class<V> errorObject, String errorTitle) throws KamaException {
		String finalUrl = addUrlParams(url, urlData);
		Map<String, String> finalHeaderData = addNecessaryHeaders(headerData);

		HttpHelper httpHelper = new HttpHelper();
		try {
			try {
				HttpResponse httpResponse = httpHelper.delete(finalUrl, finalHeaderData, deleteData);
				return parseObject(url, httpResponse, retType, listType, listTitle, errorObject, errorTitle);
			} catch (IOException e) {
				throw new KamaException(e);
			}
		} finally {
			httpHelper.close();
		}
	}
	

	protected <T, U, V> T parseObject(String url, HttpResponse httpResponse, Class<T> retType, Class<U> listType, String objTitle, Class<V> errorObject, String errorTitle) throws JsonKamaException, NotAuthorizedKamaException, HttpResponseKamaException, JsonParseException, IOException {
		JsonParser jsonParser = getJsonParserFromResponse(url, httpResponse, errorObject, errorTitle);
		T retVal = null;

		try {
			retVal = getJsonObject(url, jsonParser, retType, listType, objTitle);
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
	
	protected <T, U> T getJsonObject(String url, JsonParser jsonParser, Class<T> retType, Class<U> listType, String objTitle) throws JsonParseException, JsonMappingException, IOException, JsonKamaException {
		T retVal = null;
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
			if(retType != null) {
				if (objTitle != null) {
					JsonNode response = mapper.readTree(jsonParser);
					JsonNode responseStr = response.get(objTitle);
					jsonParser = responseStr.traverse();
				}
				retVal = mapper.readValue(jsonParser, retType);
			}
			else return null;
		}
		return retVal;
	}

	/**
	 * Adds Accept = application/json to the headers.
	 * 
	 * @param headerData
	 *            can be null
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
	 * @param urlData
	 *            can be null.
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

	protected <V> JsonParser getJsonParserFromResponse(String url, HttpResponse response, Class<V> errorObject, String errorTitle) throws JsonKamaException, NotAuthorizedKamaException, HttpResponseKamaException, JsonParseException, IOException {
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
			if(errorObject != null)	errorObj = getErrorObject(url, responseString, errorObject, errorTitle);
			
			throw new HttpResponseKamaException("Bad Request. " + url + "\n" + responseString, errorObj, statusCode);
		case 401:
			if(errorObject != null)	errorObj = getErrorObject(url, responseString, errorObject, errorTitle);
			throw new NotAuthorizedKamaException("Unauthorized Action. " + url + "\n" + responseString, errorObj);
		case 404:
			if(errorObject != null)	errorObj = getErrorObject(url, responseString, errorObject, errorTitle);
			throw new HttpResponseKamaException("Not Found. " + url + "\n" + responseString, errorObj, statusCode);
		case 500:
			if(errorObject != null)	errorObj = getErrorObject(url, responseString, errorObject, errorTitle);
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
		
		return getJsonObject(url, jp, errorObject, null, errorTitle);
	}
	
}
