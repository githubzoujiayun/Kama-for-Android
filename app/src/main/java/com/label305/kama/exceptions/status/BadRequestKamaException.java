package com.label305.kama.exceptions.status;

import com.label305.kama.http.StatusCodes;
import com.label305.kama.objects.KamaError;

public class BadRequestKamaException extends HttpResponseKamaException {

    public BadRequestKamaException(final Exception e) {
        super(e, StatusCodes.HTTP_BAD_REQUEST);
    }

    public BadRequestKamaException(final String message) {
        super(message, StatusCodes.HTTP_BAD_REQUEST);
    }

    public BadRequestKamaException(final Exception e, final KamaError kamaError) {
        super(e, kamaError, StatusCodes.HTTP_BAD_REQUEST);
    }

    public BadRequestKamaException(final String message, final KamaError kamaError) {
        super(message, kamaError, StatusCodes.HTTP_BAD_REQUEST);
    }
}
