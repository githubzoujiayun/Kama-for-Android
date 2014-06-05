package com.label305.kama;

import com.label305.kama.exceptions.KamaException;
import com.label305.kama.http.HttpHelper;
import com.label305.kama.http.PostExecutor;

import org.apache.http.HttpResponse;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.entity.AbstractHttpEntity;
import org.apache.http.entity.StringEntity;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

public class JsonPoster<ReturnType> extends AbstractJsonRequester<ReturnType> {

    private final PostExecutor mPostExecutor;

    private AbstractHttpEntity mPostData;

    public JsonPoster() {
        mPostExecutor = new HttpHelper();
    }

    public JsonPoster(final Class<ReturnType> clzz) {
        super(clzz);
        mPostExecutor = new HttpHelper();
    }

    public JsonPoster(final PostExecutor deleteExecutor) {
        mPostExecutor = deleteExecutor;
    }

    public JsonPoster(final Class<ReturnType> clzz, final PostExecutor deleteExecutor) {
        super(clzz);
        mPostExecutor = deleteExecutor;
    }


    public void setPostData(final Map<String, Object> postData) throws KamaException {
        if (postData != null) {
            try {
                mPostData = new UrlEncodedFormEntity(HttpHelper.convert(postData));
            } catch (UnsupportedEncodingException e) {
                throw new KamaException(e);
            }
        }
    }

    public void setPostData(final String jsonData) throws KamaException {
        if (jsonData != null) {
            try {
                mPostData = new StringEntity(jsonData);
            } catch (UnsupportedEncodingException e) {
                throw new KamaException(e);
            }
        }
    }

    public void setPostData(final AbstractHttpEntity postData) {
        mPostData = postData;
    }

    @Override
    protected HttpResponse executeRequest(final String parameterizedUrl, final Map<String, Object> headerData) throws KamaException {
        try {
            return mPostExecutor.post(parameterizedUrl, headerData, mPostData);
        } catch (IOException e) {
            throw new KamaException(e);
        }
    }
}
