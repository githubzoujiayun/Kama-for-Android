package com.label305.kamav2_android;

import java.io.IOException;
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

/**
 * A helper class to execute GET, POST and PUT requests.
 */
public class HttpHelper {

	AndroidHttpClient mHttpClient = AndroidHttpClient.newInstance("Android");

	/**
	 * Execute a POST request on the url configured
	 * 
	 * @return response data
	 * @throws KamaException
	 * 
	 */
	public HttpResponse post(String url, Map<String, String> headerData, List<NameValuePair> postData) throws KamaException {
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

			return mHttpClient.execute(httpPost);
		} catch (IOException e) {
			throw new KamaException(e);
		}
	}

	/**
	 * Execute a PUT request on the url configured
	 * 
	 * @return response data
	 * @throws KamaException
	 */
	public HttpResponse put(String url, Map<String, String> headerData, List<NameValuePair> putData) throws KamaException {
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

			return mHttpClient.execute(httpPut);

		} catch (IOException e) {
			throw new KamaException(e);
		}
	}

	/**
	 * Execute a GET request on the url configured
	 * 
	 * @return response data
	 * @throws KamaException
	 */
	public HttpResponse get(String url, Map<String, String> headerData) throws KamaException {
		try {
			HttpGet httpGet = new HttpGet(url);
			Iterator<String> keys = headerData.keySet().iterator();
			Iterator<String> values = headerData.values().iterator();

			while (keys.hasNext() && values.hasNext()) {
				httpGet.setHeader(keys.next(), values.next());
			}

			AndroidHttpClient.modifyRequestToAcceptGzipResponse(httpGet);
			return mHttpClient.execute(httpGet);
		} catch (IOException e) {
			throw new KamaException(e);
		}
	}

	public void close() {
		mHttpClient.close();
	}

}
