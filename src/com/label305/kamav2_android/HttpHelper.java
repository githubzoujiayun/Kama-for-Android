package com.label305.kamav2_android;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;

import android.net.http.AndroidHttpClient;

import com.label305.kamav2_android.exceptions.KamaException;

public class HttpHelper {

	/**
	 * Execute a POST request on the url configured
	 * 
	 * @return response data
	 * @throws KamaException
	 * 
	 */
	public static HttpResponse post(String url, Map<String, String> headerData, List<NameValuePair> postData) throws KamaException {

		AndroidHttpClient httpClient = AndroidHttpClient.newInstance("Android");
		HttpResponse response;
		
		try {
			HttpPost httpPost = new HttpPost(url);
			
			Iterator<String> keys = headerData.keySet().iterator();
			Iterator<String> values = headerData.values().iterator();

			while (keys.hasNext() && values.hasNext()) {
				httpPost.setHeader(keys.next(), values.next());
			}

			if (postData != null) {
				httpPost.setEntity(new UrlEncodedFormEntity(postData));
			}

			AndroidHttpClient.modifyRequestToAcceptGzipResponse(httpPost);

			response = httpClient.execute(httpPost);

		} catch (Exception e) {
			throw new KamaException(e);
		} finally { 
			if(httpClient != null) httpClient.close();
		}

		return response;
	}

	/**
	 * Execute a PUT request on the url configured
	 * 
	 * @return response data
	 * @throws KamaException 
	 */
	public static HttpResponse put(String url, Map<String, String> headerData, List<NameValuePair> putData) throws KamaException {

		AndroidHttpClient httpClient = AndroidHttpClient.newInstance("Android");
		HttpResponse response;
		
		try {
			HttpPut httpPut = new HttpPut(url);
			Iterator<String> keys = headerData.keySet().iterator();
			Iterator<String> values = headerData.values().iterator();

			while (keys.hasNext() && values.hasNext()) {
				httpPut.setHeader(keys.next(), values.next());
			}

			if (putData != null) {
				httpPut.setEntity(new UrlEncodedFormEntity(putData));
			}

			AndroidHttpClient.modifyRequestToAcceptGzipResponse(httpPut);

			response = httpClient.execute(httpPut);

		} catch (Exception e) {
			throw new KamaException(e);
		} finally { 
			if(httpClient != null) httpClient.close();
		}

		return response;
	}

	/**
	 * Execute a GET request on the url configured
	 * 
	 * @return response data
	 * @throws KamaException 
	 */
	public static HttpResponse get(String url, Map<String, String> headerData) throws KamaException {

		AndroidHttpClient httpClient = AndroidHttpClient.newInstance("Android");
		
		HttpResponse response;
		
		try {
			HttpGet httpGet = new HttpGet(url);
			Iterator<String> keys = headerData.keySet().iterator();
			Iterator<String> values = headerData.values().iterator();

			while (keys.hasNext() && values.hasNext()) {
				httpGet.setHeader(keys.next(), values.next());
			}

			AndroidHttpClient.modifyRequestToAcceptGzipResponse(httpGet);

			response = httpClient.execute(httpGet);

		} catch (Exception e) {
			throw new KamaException(e);
		} finally { 
			if(httpClient != null) httpClient.close();
		}

		return response;
	}


	
}
