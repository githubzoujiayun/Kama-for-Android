package com.label305.kama.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Collections;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class KamaObject {

    @JsonProperty("response")
    private Map<String, Object> mResponseMap;

    public Map<String, Object> getResponseMap() {
        return Collections.unmodifiableMap(mResponseMap);
    }
}