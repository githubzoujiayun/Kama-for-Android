package com.label305.kama.exceptions;

@SuppressWarnings("UnusedDeclaration")
public class JsonKamaException extends KamaException {

    public JsonKamaException(final Exception e) {
        super(e);
    }

    public JsonKamaException(final Exception e, final String displayMessage) {
        super(e, displayMessage);
    }

    public JsonKamaException(final String message) {
        super(message);
    }

}
