package com.label305.kama;

import android.content.Context;

import com.label305.kama.exceptions.KamaException;
import com.label305.stan.http.DeleteExecutor;
import com.label305.stan.http.HttpHelper;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class KamaDeleter<ReturnType> extends AbstractKamaRequester<ReturnType> {

    private final DeleteExecutor mDeleteExecutor;

    private AbstractHttpEntity mDeleteData;

    public KamaDeleter(final Context context, final String apiKey) {
        super(context, apiKey);
        mDeleteExecutor = new HttpHelper();
    }

    public KamaDeleter(final Context context, final String apiKey, final DeleteExecutor deleteExecutor) {
        super(context, apiKey);
        mDeleteExecutor = deleteExecutor;
    }


    public KamaDeleter(final Class<ReturnType> clzz, final Context context, final String apiKey) {
        super(clzz, context, apiKey);
        mDeleteExecutor = new HttpHelper();
    }

    public KamaDeleter(final Class<ReturnType> clzz, final Context context, final String apiKey, final DeleteExecutor deleteExecutor) {
        super(clzz, context, apiKey);
        mDeleteExecutor = deleteExecutor;
    }

    public void setDeleteData(final Map<String, Object> deleteData) throws KamaException {
        if (deleteData != null) {
            try {
                mDeleteData = new UrlEncodedFormEntity(HttpHelper.convert(deleteData));
            } catch (UnsupportedEncodingException e) {
                throw new KamaException(e);
            }
        }
    }

    public void setDeleteData(final String jsonData) throws KamaException {
        if (jsonData != null) {
            try {
                mDeleteData = new StringEntity(jsonData);
            } catch (UnsupportedEncodingException e) {
                throw new KamaException(e);
            }
        }
    }

    public void setDeleteData(final AbstractHttpEntity deleteData) {
        mDeleteData = deleteData;
    }

    @Override
    protected HttpResponse executeRequest() throws KamaException {
        if (getUrl() == null) {
            throw new IllegalArgumentException("Provide an url!");
        }

        String finalUrl = addUrlParams(getUrl(), getUrlData());
        Map<String, Object> finalHeaderData = addNecessaryHeaders(getHeaderData());

        try {
            return mDeleteExecutor.delete(finalUrl, finalHeaderData, mDeleteData);
        } catch (IOException e) {
            throw new KamaException(e);
        }
    }

}
