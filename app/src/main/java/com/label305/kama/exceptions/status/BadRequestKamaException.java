package com.label305.kama.exceptions.status;

import java.net.HttpURLConnection;

public class BadRequestKamaException extends HttpResponseKamaException {

    public BadRequestKamaException(final Exception e) {
        super(e, HttpURLConnection.HTTP_BAD_REQUEST);
    }

    public BadRequestKamaException(final String message) {
        super(message, HttpURLConnection.HTTP_BAD_REQUEST);
    }

    public BadRequestKamaException(final Exception e, final Object kamaError) {
        super(e, kamaError, HttpURLConnection.HTTP_BAD_REQUEST);
    }

    public BadRequestKamaException(final String message, final Object kamaError) {
        super(message, kamaError, HttpURLConnection.HTTP_BAD_REQUEST);
    }
}
