package com.label305.kamav2_android.auth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

import net.smartam.leeloo.client.URLConnectionClient;
import net.smartam.leeloo.client.request.OAuthClientRequest;
import net.smartam.leeloo.client.response.OAuthClientResponse;
import net.smartam.leeloo.client.response.OAuthClientResponseFactory;
import net.smartam.leeloo.common.OAuth;
import net.smartam.leeloo.common.exception.OAuthProblemException;
import net.smartam.leeloo.common.exception.OAuthSystemException;
import net.smartam.leeloo.common.utils.OAuthUtils;

public class MyURLConnectionClient extends URLConnectionClient {

	public <T extends OAuthClientResponse> T execute(OAuthClientRequest request, Map<String, String> headers,
			String requestMethod, Class<T> responseClass)
					throws OAuthSystemException, OAuthProblemException {

		String responseBody = null;
		URLConnection c = null;
		int responseCode = 0;
		try {
			URL url = new URL(request.getLocationUri());

			c = url.openConnection();
			responseCode = -1;
			if (c instanceof HttpURLConnection) {
				HttpURLConnection httpURLConnection = (HttpURLConnection)c;

				if (headers != null && !headers.isEmpty()) {
					for (Map.Entry<String, String> header : headers.entrySet()) {
						httpURLConnection.addRequestProperty(header.getKey(), header.getValue());
					}
				}

				if (!OAuthUtils.isEmpty(requestMethod)) {
					httpURLConnection.setRequestMethod(requestMethod);
					if (requestMethod.equals(OAuth.HttpMethod.POST)) {
						httpURLConnection.setDoOutput(true);
						OutputStream ost = httpURLConnection.getOutputStream();
						PrintWriter pw = new PrintWriter(ost);
						pw.print(request.getBody());
						pw.flush();
						pw.close();
					}
				} else {
					httpURLConnection.setRequestMethod(OAuth.HttpMethod.GET);
				}

				httpURLConnection.connect();

				InputStream inputStream;



				responseCode = httpURLConnection.getResponseCode();
				if (responseCode == 400) {
					inputStream = httpURLConnection.getErrorStream();
				} else {
					inputStream = httpURLConnection.getInputStream();
				}

				responseBody = OAuthUtils.saveStreamAsString(inputStream);
			}
		} catch (IOException e) {


			//failed first time
			try {
				if (c instanceof HttpURLConnection) {
					HttpURLConnection httpURLConnection = (HttpURLConnection)c;

					InputStream inputStream;

					responseCode = httpURLConnection.getResponseCode();

					if (responseCode == 200) {
						inputStream = httpURLConnection.getInputStream();
					} else {
						inputStream = httpURLConnection.getErrorStream();
					}

					responseBody = OAuthUtils.saveStreamAsString(inputStream);
				}
			} catch (IOException e1) {
				throw new OAuthSystemException(e1);
			}
		}



		return OAuthClientResponseFactory
				.createCustomResponse(responseBody, c.getContentType(), responseCode, responseClass);
	}
}
