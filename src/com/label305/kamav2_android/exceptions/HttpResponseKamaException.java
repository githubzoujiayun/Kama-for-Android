package com.label305.kamav2_android.exceptions;

public class HttpResponseKamaException extends KamaException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -743903660184781303L;

	public HttpResponseKamaException(Exception e) {
		super(e);
	}

	public HttpResponseKamaException(String e) {
		super(e);
	}

}
