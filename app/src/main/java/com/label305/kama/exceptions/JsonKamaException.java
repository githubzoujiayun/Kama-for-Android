package com.label305.kama.exceptions;

@SuppressWarnings("UnusedDeclaration")
public class JsonKamaException extends KamaException {

    public JsonKamaException(final Exception e) {
        super(e);
    }

    public JsonKamaException(final String message) {
        super(message);
    }

    public JsonKamaException(final String message, final Exception e) {
        super(message, e);
    }

}
