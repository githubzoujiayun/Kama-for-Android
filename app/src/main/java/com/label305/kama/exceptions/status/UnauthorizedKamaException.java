package com.label305.kama.exceptions.status;

import com.label305.kama.objects.KamaError;
import com.label305.stan.http.StatusCodes;

public class UnauthorizedKamaException extends HttpResponseKamaException {

    public UnauthorizedKamaException(final Exception e) {
        super(e, StatusCodes.HTTP_UNAUTHORIZED);
    }

    public UnauthorizedKamaException(final Exception e, final KamaError kamaError) {
        super(e, kamaError, StatusCodes.HTTP_UNAUTHORIZED);
    }

    public UnauthorizedKamaException(final String message) {
        super(message, StatusCodes.HTTP_UNAUTHORIZED);
    }

    public UnauthorizedKamaException(final String message, final KamaError kamaError) {
        super(message, kamaError, StatusCodes.HTTP_UNAUTHORIZED);
    }

}
