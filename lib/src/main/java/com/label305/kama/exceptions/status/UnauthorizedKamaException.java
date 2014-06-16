package com.label305.kama.exceptions.status;

import java.net.HttpURLConnection;

public class UnauthorizedKamaException extends HttpResponseKamaException {

    public UnauthorizedKamaException(final Exception e) {
        super(e, HttpURLConnection.HTTP_UNAUTHORIZED);
    }

    public UnauthorizedKamaException(final Exception e, final Object kamaError) {
        super(e, kamaError, HttpURLConnection.HTTP_UNAUTHORIZED);
    }

    public UnauthorizedKamaException(final String message) {
        super(message, HttpURLConnection.HTTP_UNAUTHORIZED);
    }

    public UnauthorizedKamaException(final String message, final Object kamaError) {
        super(message, kamaError, HttpURLConnection.HTTP_UNAUTHORIZED);
    }

}
