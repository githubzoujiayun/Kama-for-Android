package com.label305.kama;

import com.label305.kama.exceptions.KamaException;
import com.label305.kama.http.GetExecutor;
import com.label305.kama.http.HttpHelper;
import com.label305.kama.request.AbstractJsonRequester;

import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.Map;

public class JsonGetter<ReturnType> extends AbstractJsonRequester<ReturnType> {

    private final GetExecutor mGetExecutor;

    public JsonGetter() {
        mGetExecutor = new HttpHelper();
    }

    public JsonGetter(final Class<ReturnType> clzz) {
        super(clzz);
        mGetExecutor = new HttpHelper();
    }

    public JsonGetter(final GetExecutor deleteExecutor) {
        mGetExecutor = deleteExecutor;
    }

    public JsonGetter(final Class<ReturnType> clzz, final GetExecutor deleteExecutor) {
        super(clzz);
        mGetExecutor = deleteExecutor;
    }

    @Override
    protected HttpResponse executeRequest() throws KamaException {
        if (getUrl() == null) {
            throw new IllegalArgumentException("Provide an url!");
        }

        String finalUrl = addUrlParams(getUrl(), getUrlData());
        Map<String, Object> finalHeaderData = addNecessaryHeaders(getHeaderData());

        try {
            return mGetExecutor.get(finalUrl, finalHeaderData);
        } catch (IOException e) {
            throw new KamaException(e);
        }
    }
}