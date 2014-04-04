package com.label305.kamav2_android.exceptions;

public class NotAuthorizedKamaException extends KamaException {


    public NotAuthorizedKamaException(Exception e) {
        super(e);
    }

    public NotAuthorizedKamaException(Exception e, Object errorObj) {
        super(e, errorObj);
    }

    public NotAuthorizedKamaException(String e) {
        super(e);
    }

    public NotAuthorizedKamaException(String e, Object errorObj) {
        super(e, errorObj);
    }

}
