package com.label305.kama.exceptions.status;

import com.label305.kama.objects.KamaError;

import java.net.HttpURLConnection;

public class BadRequestKamaException extends HttpResponseKamaException {

    public BadRequestKamaException(final Exception e) {
        super(e, HttpURLConnection.HTTP_BAD_REQUEST);
    }

    public BadRequestKamaException(final String message) {
        super(message, HttpURLConnection.HTTP_BAD_REQUEST);
    }

    public BadRequestKamaException(final Exception e, final KamaError kamaError) {
        super(e, kamaError, HttpURLConnection.HTTP_BAD_REQUEST);
    }

    public BadRequestKamaException(final String message, final KamaError kamaError) {
        super(message, kamaError, HttpURLConnection.HTTP_BAD_REQUEST);
    }
}
