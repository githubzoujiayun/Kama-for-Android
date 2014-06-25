package com.label305.kama;

import com.label305.kama.exceptions.KamaException;
import com.label305.kama.http.HttpHelper;
import com.label305.kama.http.PutExecutor;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class JsonPutter<ReturnType> extends AbstractJsonRequester<ReturnType> {

    private static final String UTF_8 = "UTF-8";

    private final PutExecutor mPutExecutor;

    private AbstractHttpEntity mPutData;

    public JsonPutter() {
        mPutExecutor = new HttpHelper();
    }

    public JsonPutter(final Class<ReturnType> clzz) {
        super(clzz);
        mPutExecutor = new HttpHelper();
    }

    public JsonPutter(final PutExecutor deleteExecutor) {
        mPutExecutor = deleteExecutor;
    }

    public JsonPutter(final Class<ReturnType> clzz, final PutExecutor deleteExecutor) {
        super(clzz);
        mPutExecutor = deleteExecutor;
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
                mPutData = new StringEntity(jsonData, UTF_8);
            } catch (UnsupportedEncodingException e) {
                throw new KamaException(e);
            }
        }
    }

    public void setPutData(final AbstractHttpEntity putData) {
        mPutData = putData;
    }

    @Override
    protected HttpResponse executeRequest(final String parameterizedUrl, final Map<String, Object> headerData) throws IOException {
        return mPutExecutor.put(parameterizedUrl, headerData, mPutData);
    }
}
