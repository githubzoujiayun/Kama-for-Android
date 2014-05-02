package com.label305.kama;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.label305.kama.exceptions.KamaException;
import com.label305.kama.objects.KamaObject;
import com.label305.kama.request.AbstractJsonRequester;

import java.io.IOException;
import java.util.Map;

public class NewKamaRequester<ReturnType> {

    private final AbstractJsonRequester<KamaObject> mJsonRequester;
    private final Class<ReturnType> mReturnTypeClass;

    private String mJsonTitle;

    public NewKamaRequester(final AbstractJsonRequester<KamaObject> jsonRequester, Class<ReturnType> returnTypeClass) {
        mJsonRequester = jsonRequester;
        mReturnTypeClass = returnTypeClass;
    }

    public void setJsonTitle(final String jsonTitle) {
        mJsonTitle = jsonTitle;
    }

    public ReturnType execute() throws KamaException, JsonProcessingException {
        KamaObject execute = mJsonRequester.execute();
        Map<String, Object> responseMap = execute.getResponseMap();
        Object o = responseMap.get(mJsonTitle);
        String json = new ObjectMapper().writeValueAsString(o);
        try {
            return new ObjectMapper().readValue(json, mReturnTypeClass);
        } catch (IOException e) {
            throw new KamaException(e);
        }
    }
}
