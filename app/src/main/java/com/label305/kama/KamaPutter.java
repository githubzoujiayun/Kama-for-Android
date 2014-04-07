package com.label305.kama;

import android.content.Context;

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

public class KamaPutter<ReturnType> extends AbstractKamaRequester<ReturnType> {

    private final PutExecutor mPutExecutor;

    private AbstractHttpEntity mPutData;

    public KamaPutter(final Context context, final String apiKey) {
        super(context, apiKey);
        mPutExecutor = new HttpHelper();
    }

    public KamaPutter(final Context context, final String apiKey, final PutExecutor putExecutor) {
        super(context, apiKey);
        mPutExecutor = putExecutor;
    }

    public KamaPutter(final Class<ReturnType> clzz, final Context context, final String apiKey) {
        super(clzz, context, apiKey);
        mPutExecutor = new HttpHelper();
    }

    public KamaPutter(final Class<ReturnType> clzz, final Context context, final String apiKey, final PutExecutor putExecutor) {
        super(clzz, context, apiKey);
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
