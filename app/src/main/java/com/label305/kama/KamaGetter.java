package com.label305.kama;

import android.content.Context;

import com.label305.kama.exceptions.KamaException;
import com.label305.kama.http.GetExecutor;
import com.label305.kama.http.HttpHelper;

import org.apache.http.HttpResponse;

import java.io.IOException;
import java.util.Map;

public class KamaGetter<ReturnType> extends AbstractKamaRequester<ReturnType> {

    private final GetExecutor mGetExecutor;

    public KamaGetter(final Context context, final String apiKey) {
        super(context, apiKey);
        mGetExecutor = new HttpHelper();
    }

    public KamaGetter(final Context context, final String apiKey, final GetExecutor getExecutor) {
        super(context, apiKey);
        mGetExecutor = getExecutor;
    }

    public KamaGetter(final Class<ReturnType> clzz, final Context context, final String apiKey) {
        super(clzz, context, apiKey);
        mGetExecutor = new HttpHelper();
    }

    public KamaGetter(final Class<ReturnType> clzz, final Context context, final String apiKey, final GetExecutor getExecutor) {
        super(clzz, context, apiKey);
        mGetExecutor = getExecutor;
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
