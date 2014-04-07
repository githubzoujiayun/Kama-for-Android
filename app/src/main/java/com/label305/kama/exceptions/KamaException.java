package com.label305.kama.exceptions;

import com.label305.kama.objects.KamaError;

@SuppressWarnings("UnusedDeclaration")
public class KamaException extends Exception {

    private final KamaError mKamaError;

    public KamaException(final Exception e) {
        super(e);
        mKamaError = null;
    }

    public KamaException(final Exception e, final KamaError kamaError) {
        super(e);
        mKamaError = kamaError;
    }

    public KamaException(final String message, final Exception e) {
        super(message, e);
        mKamaError = null;
    }

    public KamaException(final String message, final KamaError kamaError) {
        super(message);
        mKamaError = kamaError;
    }

    public KamaException(final String message) {
        super(message);
        mKamaError = null;
    }

    public KamaException(final KamaError kamaError) {
        mKamaError = kamaError;
    }


    public KamaError getKamaError() {
        return mKamaError;
    }

}
