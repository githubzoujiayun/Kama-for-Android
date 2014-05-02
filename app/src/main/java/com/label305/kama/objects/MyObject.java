package com.label305.kama.objects;

import com.fasterxml.jackson.annotation.JsonProperty;

public class MyObject {

    public MyObject() {
    }

    @JsonProperty("name")
    private String name;

    public String getName() {
        return name;
    }
}