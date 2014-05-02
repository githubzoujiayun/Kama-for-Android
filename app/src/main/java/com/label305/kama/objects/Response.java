package com.label305.kama.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Response<T> {

    @JsonProperty
    private T t;


    public T getT() {
        return t;
    }
}
