package com.label305.kama.objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("UnusedDeclaration")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ErrorObj {

    @JsonProperty("code")
    private int mStatusCode;

    @JsonProperty("message")
    private String mMessage;

    @JsonProperty("timestamp")
    private String mTimestamp;

    public int getStatusCode() {
        return mStatusCode;
    }

    public String getMessage() {
        return mMessage;
    }

    public String getTimestamp() {
        return mTimestamp;
    }

    public void setStatusCode(final int statusCode) {
        mStatusCode = statusCode;
    }

    public void setMessage(final String message) {
        mMessage = message;
    }

    public void setTimestamp(final String timestamp) {
        mTimestamp = timestamp;
    }
}
