package com.label305.kama.exceptions.status;

import com.label305.kama.http.StatusCodes;
import com.label305.kama.objects.KamaError;

public class InternalErrorKamaException extends HttpResponseKamaException {

    public InternalErrorKamaException(final Exception e) {
        super(e, StatusCodes.HTTP_INTERNAL_ERROR);
    }

    public InternalErrorKamaException(final String message) {
        super(message, StatusCodes.HTTP_INTERNAL_ERROR);
    }

    public InternalErrorKamaException(final Exception e, final KamaError kamaError) {
        super(e, kamaError, StatusCodes.HTTP_INTERNAL_ERROR);
    }

    public InternalErrorKamaException(final String message, final KamaError kamaError) {
        super(message, kamaError, StatusCodes.HTTP_INTERNAL_ERROR);
    }
}
