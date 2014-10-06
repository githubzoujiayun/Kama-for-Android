package com.label305.kama.http;

import android.net.http.AndroidHttpClient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A helper class to execute GET, POST and PUT requests.
 */
public class HttpHelper implements GetExecutor, PostExecutor, PutExecutor, DeleteExecutor {

    private static final int TIMEOUT = 5000;
    private static final String ANDROID = "Android";

    /**
     * The AndroidHttpClient to use. We use a single instance and for now, do not close it. // TODO: When should we??
     */
    private static final AndroidHttpClient HTTP_CLIENT = AndroidHttpClient.newInstance(ANDROID);
    static {
        HttpConnectionParams.setConnectionTimeout(HTTP_CLIENT.getParams(), TIMEOUT);
    }

    @Override
    public HttpResponse delete(final String url, final Map<String, Object> headerData, final HttpEntity deleteDataEntity) throws IOException {
        HttpDeleteWithBody httpDelete = new HttpDeleteWithBody(url);
        Iterator<String> keys = headerData.keySet().iterator();
        Iterator<Object> values = headerData.values().iterator();

        while (keys.hasNext() && values.hasNext()) {
            httpDelete.setHeader(keys.next(), values.next().toString());
        }

        if (deleteDataEntity != null) {
            httpDelete.setEntity(deleteDataEntity);
        }

        AndroidHttpClient.modifyRequestToAcceptGzipResponse(httpDelete);
        return HTTP_CLIENT.execute(httpDelete);
    }

    @Override
    public HttpResponse get(final String url, final Map<String, Object> headerData) throws IOException {
        HttpUriRequest httpGet = new HttpGet(url);
        Iterator<String> keys = headerData.keySet().iterator();
        Iterator<Object> values = headerData.values().iterator();

        while (keys.hasNext() && values.hasNext()) {
            httpGet.setHeader(keys.next(), values.next().toString());
        }

        AndroidHttpClient.modifyRequestToAcceptGzipResponse(httpGet);
        return HTTP_CLIENT.execute(httpGet);
    }

    @Override
    public HttpResponse post(final String url, final Map<String, Object> headerData, final HttpEntity postDataEntity) throws IOException {
        HttpPost httpPost = new HttpPost(url);

        Iterator<String> keys = headerData.keySet().iterator();
        Iterator<Object> values = headerData.values().iterator();

        while (keys.hasNext() && values.hasNext()) {
            httpPost.setHeader(keys.next(), values.next().toString());
        }

        if (postDataEntity != null) {
            httpPost.setEntity(postDataEntity);
        }

        AndroidHttpClient.modifyRequestToAcceptGzipResponse(httpPost);

        return HTTP_CLIENT.execute(httpPost);
    }

    @Override
    public HttpResponse put(final String url, final Map<String, Object> headerData, final HttpEntity putDataEntity) throws IOException {
        HttpPut httpPut = new HttpPut(url);
        Iterator<String> keys = headerData.keySet().iterator();
        Iterator<Object> values = headerData.values().iterator();

        while (keys.hasNext() && values.hasNext()) {
            httpPut.setHeader(keys.next(), values.next().toString());
        }

        if (putDataEntity != null) {
            httpPut.setEntity(putDataEntity);
        }

        AndroidHttpClient.modifyRequestToAcceptGzipResponse(httpPut);

        return HTTP_CLIENT.execute(httpPut);
    }


    public static List<NameValuePair> convert(@NotNull final Map<String, Object> data) {
        List<NameValuePair> results = new ArrayList<>();

        for (final Map.Entry<String, Object> stringObjectEntry : data.entrySet()) {
            String value = stringObjectEntry.getValue() == null ? "null" : stringObjectEntry.getValue().toString();
            results.add(new BasicNameValuePair(stringObjectEntry.getKey(), value));
        }

        return results;
    }
}