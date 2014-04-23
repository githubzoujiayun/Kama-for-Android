package com.label305.kama.exceptions.status;

import com.label305.kama.http.StatusCodes;
import com.label305.kama.objects.KamaError;

public class NotFoundKamaException extends HttpResponseKamaException {

    public NotFoundKamaException(final Exception e) {
        super(e, StatusCodes.HTTP_NOT_FOUND);
    }

    public NotFoundKamaException(final String message) {
        super(message, StatusCodes.HTTP_NOT_FOUND);
    }

    public NotFoundKamaException(final Exception e, final KamaError kamaError) {
        super(e, kamaError, StatusCodes.HTTP_NOT_FOUND);
    }

    public NotFoundKamaException(final String message, final KamaError kamaError) {
        super(message, kamaError, StatusCodes.HTTP_NOT_FOUND);
    }
}
