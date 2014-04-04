package com.label305.kamav2_android.exceptions;

@SuppressWarnings("UnusedDeclaration")
public class KamaException extends Exception {

    private final Object mErrorObj;

    private int mHttpStatusCode;

    public KamaException(final Exception e) {
        super(e);
        mErrorObj = null;
        mHttpStatusCode = -1;
    }

    public KamaException(final Exception e, final Object errorObj) {
        super(e);
        mErrorObj = errorObj;
        mHttpStatusCode = -1;
    }

    public KamaException(final String e, final Object errorObj) {
        super(e);
        mErrorObj = errorObj;
        mHttpStatusCode = -1;
    }

    public KamaException(final String e) {
        super(e);
        mErrorObj = null;
        mHttpStatusCode = -1;
    }

    public Object getErrorObj() {
        return mErrorObj;
    }

    /**
     * @return the httpStatusCode
     */
    public int getHttpStatusCode() {
        return mHttpStatusCode;
    }

    /**
     * @param httpStatusCode the httpStatusCode to set
     */
    public void setHttpStatusCode(final int httpStatusCode) {
        mHttpStatusCode = httpStatusCode;
    }

}
