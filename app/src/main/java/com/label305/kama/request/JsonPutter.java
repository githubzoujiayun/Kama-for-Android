package com.label305.kama.request;

import com.label305.kama.exceptions.KamaException;
import com.label305.stan.http.HttpHelper;
import com.label305.stan.http.PutExecutor;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class JsonPutter extends AbstractJsonRequester {

    private final PutExecutor mPutExecutor;

    private AbstractHttpEntity mPutData;

    public JsonPutter() {
        mPutExecutor = new HttpHelper();
    }

    public JsonPutter(final PutExecutor putExecutor) {
        mPutExecutor = putExecutor;
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

    public void setPutData(final AbstractHttpEntity putData) {
        mPutData = putData;
    }

    @Override
    protected HttpResponse executeRequest() throws KamaException {
        if (getUrl() == null) {
            throw new IllegalArgumentException("Provide an url!");
        }

        String finalUrl = addUrlParams(getUrl(), getUrlData());
        Map<String, Object> finalHeaderData = addNecessaryHeaders(getHeaderData());

        try {
            return mPutExecutor.put(finalUrl, finalHeaderData, mPutData);
        } catch (IOException e) {
            throw new KamaException(e);
        }
    }
}
