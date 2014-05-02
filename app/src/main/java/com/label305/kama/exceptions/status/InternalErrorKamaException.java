package com.label305.kama.exceptions.status;

import com.label305.kama.objects.KamaError;

import java.net.HttpURLConnection;

public class InternalErrorKamaException extends HttpResponseKamaException {

    public InternalErrorKamaException(final Exception e) {
        super(e, HttpURLConnection.HTTP_INTERNAL_ERROR);
    }

    public InternalErrorKamaException(final String message) {
        super(message, HttpURLConnection.HTTP_INTERNAL_ERROR);
    }

    public InternalErrorKamaException(final Exception e, final KamaError kamaError) {
        super(e, kamaError, HttpURLConnection.HTTP_INTERNAL_ERROR);
    }

    public InternalErrorKamaException(final String message, final KamaError kamaError) {
        super(message, kamaError, HttpURLConnection.HTTP_INTERNAL_ERROR);
    }
}
