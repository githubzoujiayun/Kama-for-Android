package com.label305.kamav2_android.exceptions;

public class HttpResponseKamaException extends KamaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -743903660184781303L;

	private int mHttpCode;

	/**
	 * @deprecated use HttpResponseKamaException(Exception, int) instead.
	 */
	@Deprecated
	public HttpResponseKamaException(Exception e) {
		super(e);
	}

	/**
	 * @deprecated use HttpResponseKamaException(String, int) instead.
	 */
	public HttpResponseKamaException(String e) {
		super(e);
	}

	public HttpResponseKamaException(Exception e, int httpCode) {
		super(e);
		mHttpCode = httpCode;
	}

	public HttpResponseKamaException(String e, int httpCode) {
		super(e);
		mHttpCode = httpCode;
	}

	public int getHttpCode() {
		return mHttpCode;
	}

}
