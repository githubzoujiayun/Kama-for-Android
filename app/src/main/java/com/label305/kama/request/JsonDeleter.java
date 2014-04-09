package com.label305.kama.request;

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

public class JsonDeleter<ReturnType> extends AbstractJsonRequester<ReturnType> {

    private final DeleteExecutor mDeleteExecutor;

    private AbstractHttpEntity mDeleteData;

    public JsonDeleter() {
        mDeleteExecutor = new HttpHelper();
    }

    public JsonDeleter(final Class<ReturnType> clzz) {
        super(clzz);
        mDeleteExecutor = new HttpHelper();
    }

    public JsonDeleter(final DeleteExecutor deleteExecutor) {
        mDeleteExecutor = deleteExecutor;
    }

    public JsonDeleter(final Class<ReturnType> clzz, final DeleteExecutor deleteExecutor) {
        super(clzz);
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
