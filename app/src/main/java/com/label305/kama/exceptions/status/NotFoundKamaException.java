package com.label305.kama.exceptions.status;

import com.label305.kama.objects.KamaError;

import java.net.HttpURLConnection;

public class NotFoundKamaException extends HttpResponseKamaException {

    public NotFoundKamaException(final Exception e) {
        super(e, HttpURLConnection.HTTP_NOT_FOUND);
    }

    public NotFoundKamaException(final String message) {
        super(message, HttpURLConnection.HTTP_NOT_FOUND);
    }

    public NotFoundKamaException(final Exception e, final KamaError kamaError) {
        super(e, kamaError, HttpURLConnection.HTTP_NOT_FOUND);
    }

    public NotFoundKamaException(final String message, final KamaError kamaError) {
        super(message, kamaError, HttpURLConnection.HTTP_NOT_FOUND);
    }
}
