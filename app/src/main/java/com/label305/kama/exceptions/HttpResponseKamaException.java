package com.label305.kama.exceptions;

@SuppressWarnings("UnusedDeclaration")
public class HttpResponseKamaException extends KamaException {

    private final int mHttpCode;

    public HttpResponseKamaException(final Exception e, final int httpCode) {
        super(e);
        mHttpCode = httpCode;
    }

    public HttpResponseKamaException(final String e, final int httpCode) {
        super(e);
        mHttpCode = httpCode;
    }

    public HttpResponseKamaException(final Exception e, final Object errorObject, final int httpCode) {
        super(e, errorObject);
        mHttpCode = httpCode;
    }

    public HttpResponseKamaException(final String e, final Object errorObject, final int httpCode) {
        super(e, errorObject);
        mHttpCode = httpCode;
    }

    public int getHttpCode() {
        return mHttpCode;
    }

}
