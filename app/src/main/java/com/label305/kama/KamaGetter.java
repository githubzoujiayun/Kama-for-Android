package com.label305.kama;

import android.content.Context;

import com.label305.kama.exceptions.KamaException;
import com.label305.stan.http.GetExecutor;
import com.label305.stan.http.HttpHelper;

import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.Map;

public class KamaGetter extends AbstractKamaRequester {

    private final GetExecutor mGetExecutor;

    public KamaGetter(final Context context, final String apiKey) {
        super(context, apiKey);
        mGetExecutor = new HttpHelper();
    }

    public KamaGetter(final Context context, final String apiKey, final GetExecutor getExecutor) {
        super(context, apiKey);
        mGetExecutor = getExecutor;
    }


    @Override
    protected HttpResponse executeRequest() throws KamaException {
        if (getUrl() == null) {
            throw new IllegalArgumentException("Provide an url!");
        }

        if (getReturnTypeClass() == null) {
            throw new IllegalArgumentException("Provide a return type class!");
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
