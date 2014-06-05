package com.label305.kama.exceptions.status;

import com.label305.kama.exceptions.KamaException;

@SuppressWarnings("UnusedDeclaration")
public class HttpResponseKamaException extends KamaException {

    private final int mStatusCode;

    public HttpResponseKamaException(final Exception e, final int statusCode) {
        super(e);
        mStatusCode = statusCode;
    }

    public HttpResponseKamaException(final String message, final int statusCode) {
        super(message);
        mStatusCode = statusCode;
    }

    public HttpResponseKamaException(final Exception e, final Object kamaError, final int statusCode) {
        super(e, kamaError);
        mStatusCode = statusCode;
    }

    public HttpResponseKamaException(final String message, final Object kamaError, final int statusCode) {
        super(message, kamaError);
        mStatusCode = statusCode;
    }

    public int getStatusCode() {
        return mStatusCode;
    }

}
