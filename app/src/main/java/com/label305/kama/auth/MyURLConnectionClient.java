package com.label305.kama.auth;

import net.smartam.leeloo.client.URLConnectionClient;
import net.smartam.leeloo.client.request.OAuthClientRequest;
import net.smartam.leeloo.client.response.OAuthClientResponse;
import net.smartam.leeloo.client.response.OAuthClientResponseFactory;
import net.smartam.leeloo.common.OAuth;
import net.smartam.leeloo.common.exception.OAuthProblemException;
import net.smartam.leeloo.common.exception.OAuthSystemException;
import net.smartam.leeloo.common.utils.OAuthUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;

public class MyURLConnectionClient extends URLConnectionClient {

    @Override
    public <T extends OAuthClientResponse> T execute(final OAuthClientRequest request, final Map<String, String> headers, final String requestMethod,
                                                     final Class<T> responseClass) throws OAuthSystemException,
                                                                                          OAuthProblemException {

        String responseBody = null;
        URLConnection connection = null;
        int responseCode = 0;
        try {
            URL url = new URL(request.getLocationUri());

            connection = url.openConnection();
            responseCode = -1;
            if (connection instanceof HttpURLConnection) {
                HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

                if (headers != null && !headers.isEmpty()) {
                    for (Map.Entry<String, String> header : headers.entrySet()) {
                        httpURLConnection.addRequestProperty(header.getKey(), header.getValue());
                    }
                }

                if (OAuthUtils.isEmpty(requestMethod)) {
                    httpURLConnection.setRequestMethod(OAuth.HttpMethod.GET);
                } else {
                    httpURLConnection.setRequestMethod(requestMethod);
                    if (requestMethod.equals(OAuth.HttpMethod.POST)) {
                        httpURLConnection.setDoOutput(true);
                        OutputStream ost = httpURLConnection.getOutputStream();
                        PrintWriter pw = new PrintWriter(ost);
                        pw.print(request.getBody());
                        pw.flush();
                        pw.close();
                    }
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
                if (connection instanceof HttpURLConnection) {
                    HttpURLConnection httpURLConnection = (HttpURLConnection) connection;

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
                .createCustomResponse(responseBody, connection.getContentType(), responseCode, responseClass);
    }
}
